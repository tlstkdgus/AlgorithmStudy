# 최적화 분석

제공해주신 코드는 '두 선거구' 문제를 해결하기 위한 백트래킹(재귀를 이용한 부분집합 생성)과 BFS를 사용하고 있습니다. N이 최대 10이라는 제약 조건 때문에 $2^N$에 다항 시간을 곱하는 방식으로 해결할 수 있습니다.

현재 코드의 주요 최적화 포인트는 다음과 같습니다:

1.  **시간 복잡도:**
    *   `powerset`에서 모든 $2^N$개의 부분집합을 미리 `powSetA`, `powSetB`에 저장하는 과정.
    *   `search` 함수 내에서 `distList.contains(adj)` 호출. `ArrayList.contains`는 `O(K)` 시간 복잡도를 가지므로, BFS 내부에서 반복적으로 호출될 경우 전체 BFS 시간 복잡도가 `O(V*K + E*K)`로 증가할 수 있습니다. 여기서 `K`는 `distList`의 크기입니다.

2.  **공간 복잡도:**
    *   `powSetA`, `powSetB`에 $2^N$개의 `List<Integer>` 객체를 저장하는 것은 `O(N * 2^N)` 공간을 필요로 합니다. N=10일 경우 1024개의 리스트 각각에 최대 10개의 정수가 들어갈 수 있어 약 10KB 정도이지만, 더 큰 N에서는 문제가 될 수 있습니다.

3.  **가독성:**
    *   일부 변수명(예: `districts` 맵, `powset` 함수의 `nDist`, `idx`)이 일반적인 그래프 알고리즘 또는 백트래킹 컨텍스트에서 더 명확하게 변경될 수 있습니다.
    *   `powset`이 `powSetA`와 `powSetB`를 채운 후, 메인 루프에서 다시 이들을 꺼내 처리하는 구조는 직접적으로 로직을 연결하는 것보다 단계가 많습니다.

---

### 최적화 목표 및 방법

1.  **시간 복잡도 개선:**
    *   **`search` 함수 최적화:** `distList.contains(adj)` 대신, 현재 생성된 부분집합의 멤버십을 `O(1)`에 확인할 수 있는 `boolean[]` 배열을 사용합니다. 이 `boolean[]` 배열은 `powerset` 함수에서 만들어진 `isIncluded` 배열을 활용할 수 있습니다.
    *   **`powerset` 호출 횟수 감소:** 부분집합 A와 B는 서로의 여집합 관계이므로, `(A, B)`와 `(B, A)`는 본질적으로 같은 파티션입니다. 중복 계산을 피하기 위해 한 쪽만 검사하도록 합니다. 예를 들어, 1번 도시는 항상 첫 번째 선거구에 속한다고 가정하면, 전체 경우의 수는 $2^{N-1}$로 절반으로 줄어듭니다.
    *   **불필요한 리스트 생성 지연:** `powSetA`, `powSetB`를 미리 채우는 대신, 부분집합이 완성되는 시점(`powerset`의 베이스 케이스)에 바로 유효성 검사 및 인구 차이 계산을 수행합니다.

2.  **공간 복잡도 개선:**
    *   `powSetA`, `powSetB` 전역 변수를 제거하고, 필요한 리스트는 `powerset` 재귀 호출의 베이스 케이스에서 일시적으로 생성하여 사용합니다. 이렇게 하면 공간 복잡도가 `O(N)`으로 줄어듭니다 (재귀 스택 및 임시 리스트).

3.  **가독성 개선:**
    *   변수명 (`districts` -> `adjList`, `powset` -> `generatePartitions`, `isIncluded` -> `isIncludedInGroupA` 등)을 더 명확하게 변경합니다.
    *   `search` 함수를 `isConnected`로 변경하여 목적을 명확히 합니다.
    *   메인 로직과 재귀/BFS 로직을 분리하여 각 함수의 역할을 명확히 합니다.

---

### 최종 최적화된 코드

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ_17471_Optimized {
    static int N; // 도시의 개수
    static int[] populations; // 각 도시의 인구
    static List<List<Integer>> adjList; // 도시 간 연결 정보를 저장하는 인접 리스트
    static boolean[] isIncludedInGroupA; // 각 도시가 그룹 A에 포함되는지 여부 (true면 A, false면 B)
    static int totalPopulation; // 전체 도시의 총 인구
    static int minPopulationDiff = Integer.MAX_VALUE; // 두 선거구의 최소 인구 차이

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        N = Integer.parseInt(br.readLine());
        populations = new int[N + 1]; // 1-indexed
        adjList = new ArrayList<>();
        for (int i = 0; i <= N; i++) {
            adjList.add(new ArrayList<>());
        }
        isIncludedInGroupA = new boolean[N + 1];

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            populations[i] = Integer.parseInt(st.nextToken());
            totalPopulation += populations[i];
        }

        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int nConnected = Integer.parseInt(st.nextToken());
            for (int j = 0; j < nConnected; j++) {
                adjList.get(i).add(Integer.parseInt(st.nextToken()));
            }
        }

        // 부분집합 생성 시작
        // 1번 도시는 항상 그룹 A에 포함시킨다 (중복 계산 방지 및 그룹 A가 비어있지 않도록 보장).
        // 따라서 2번 도시부터 N번 도시까지의 포함 여부를 결정한다.
        isIncludedInGroupA[1] = true; 
        generatePartitions(2); 

        if (minPopulationDiff == Integer.MAX_VALUE) {
            System.out.print("-1");
        } else {
            System.out.print(minPopulationDiff);
        }
        br.close();
    }

    /**
     * 재귀를 통해 N개의 도시를 두 그룹(A, B)으로 나누는 모든 경우의 수를 생성합니다.
     * @param idx 현재 처리할 도시의 인덱스 (1부터 N까지)
     */
    public static void generatePartitions(int idx) {
        // 기저 조건: 모든 도시의 그룹 배정이 완료되었을 때
        if (idx == N + 1) {
            // 현재 isIncludedInGroupA 배열 상태를 기반으로 실제 그룹 A와 B 리스트를 생성합니다.
            List<Integer> groupA = new ArrayList<>();
            List<Integer> groupB = new ArrayList<>();
            int sumA = 0; // 그룹 A의 인구 합계

            for (int i = 1; i <= N; i++) {
                if (isIncludedInGroupA[i]) {
                    groupA.add(i);
                    sumA += populations[i];
                } else {
                    groupB.add(i);
                }
            }

            // 두 그룹 모두 비어있지 않아야 유효한 선거구 분할입니다.
            // (isIncludedInGroupA[1] = true 덕분에 groupA는 항상 비어있지 않습니다. groupB만 확인하면 됩니다.)
            if (groupB.isEmpty()) { 
                return;
            }

            // 각 그룹의 연결성을 확인합니다.
            boolean[] visitedForGroupA = new boolean[N + 1];
            boolean[] visitedForGroupB = new boolean[N + 1];

            // isConnected 함수에 어떤 그룹을 검사할 것인지(`isGroupA`)를 넘겨줍니다.
            if (isConnected(groupA, true, visitedForGroupA) && 
                isConnected(groupB, false, visitedForGroupB)) {
                
                int sumB = totalPopulation - sumA;
                minPopulationDiff = Math.min(minPopulationDiff, Math.abs(sumA - sumB));
            }
            return;
        }

        // 재귀 호출: 현재 도시 `idx`를 그룹 A에 포함시키는 경우
        isIncludedInGroupA[idx] = true;
        generatePartitions(idx + 1);

        // 재귀 호출: 현재 도시 `idx`를 그룹 B에 포함시키는 경우
        isIncludedInGroupA[idx] = false;
        generatePartitions(idx + 1);
    }

    /**
     * 주어진 도시 그룹이 모두 연결되어 있는지 BFS를 사용하여 확인합니다.
     * @param groupList 확인할 도시 그룹의 리스트 (예: groupA 또는 groupB)
     * @param isGroupA  현재 확인하는 그룹이 A인지 (true) B인지 (false)
     * @param visited   BFS 방문 여부를 저장할 배열 (재사용 방지를 위해 매 호출마다 초기화 필요)
     * @return 그룹의 모든 도시가 연결되어 있으면 true, 아니면 false
     */
    public static boolean isConnected(List<Integer> groupList, boolean isGroupA, boolean[] visited) {
        // 빈 그룹은 유효한 연결 상태로 볼 수 없습니다 (문제 조건상 선거구는 비어있으면 안 됨).
        if (groupList.isEmpty()) {
            return false;
        }

        Queue<Integer> q = new LinkedList<>();
        int startNode = groupList.get(0); // 그룹 내 첫 번째 도시에서 BFS 시작
        q.offer(startNode);
        visited[startNode] = true;
        int visitedCount = 1; // 현재 그룹 내에서 방문한 도시의 수

        while (!q.isEmpty()) {
            int current = q.poll();

            for (int neighbor : adjList.get(current)) {
                // 이웃 도시가 현재 검사 중인 그룹에 속하고, 아직 방문하지 않았다면 큐에 추가
                boolean neighborIsInCurrentGroup = isGroupA ? isIncludedInGroupA[neighbor] : !isIncludedInGroupA[neighbor];
                
                if (neighborIsInCurrentGroup && !visited[neighbor]) {
                    visited[neighbor] = true;
                    q.offer(neighbor);
                    visitedCount++;
                }
            }
        }

        // 그룹 내 모든 도시를 방문했다면 연결된 것으로 판단
        return visitedCount == groupList.size();
    }
}
```

---

### 왜 이 방법이 최선인지 설명

1.  **시간 복잡도 개선:**
    *   **`generatePartitions` (이전 `powerset`)**: `N`개의 도시에 대해 각각 그룹 A 또는 B에 속할지 결정하므로 $2^N$가지 경우의 수를 탐색합니다. 하지만 `1번 도시는 항상 그룹 A에 속한다`는 제약을 통해 실제로는 $2^{N-1}$가지 경우의 수만 고려하게 됩니다. 각 경우의 수에서 `groupA`, `groupB` 리스트를 만드는 데 `O(N)` 시간이 걸립니다.
    *   **`isConnected` (이전 `search`)**:
        *   `distList.contains(adj)` (O(K)) 대신 `neighborIsInCurrentGroup` (O(1))로 변경하여 BFS의 효율성을 크게 높였습니다.
        *   이제 BFS는 일반적인 그래프 탐색과 동일하게 `O(V_s + E_s)`의 시간 복잡도를 가집니다. 여기서 `V_s`는 현재 부분집합의 노드 수 (최대 N), `E_s`는 현재 부분집합 내의 간선 수 (최대 N^2)입니다. 최악의 경우 O(N^2) (N개의 노드와 최대 N(N-1)/2개의 간선)으로 볼 수 있습니다.
    *   **총 시간 복잡도**: $O(2^{N-1} \cdot (N + N^2 + N^2)) = O(2^{N-1} \cdot N^2)$. N=10일 때, $2^9 \cdot 10^2 = 512 \cdot 100 = 51,200$ 연산으로, 이전 코드의 `O(2^N \cdot N^3)` (N=10일 때 약 $10^6$ 이상)에 비해 훨씬 빠르고, N=10의 제약 조건에서는 충분히 효율적입니다.

2.  **공간 복잡도 개선:**
    *   `powSetA`와 `powSetB` 전역 `List<List<Integer>>`를 제거하여 `O(N * 2^N)` 공간을 `O(1)` (상수 공간)으로 줄였습니다. (재귀 호출 스택이 `O(N)` 공간을 사용하고, `groupA`/`groupB` 임시 리스트가 `O(N)` 공간을 사용하지만, 이는 호출당 한 번씩만 발생하므로 전체적으로는 `O(N)` 공간 복잡도를 가집니다.)

3.  **가독성 및 유지보수성 개선:**
    *   **명확한 변수명:** `N`, `populations`, `adjList`, `isIncludedInGroupA`, `totalPopulation`, `minPopulationDiff` 등은 각 변수의 역할을 명확히 설명합니다.
    *   **함수 분리 및 역할 정의:** `generatePartitions`는 부분집합 생성에만 집중하고, `isConnected`는 연결성 확인에만 집중합니다. `main` 함수는 전체 흐름을 담당하여 각 기능의 역할이 뚜렷해졌습니다.
    *   **중복 방지 로직:** 1번 도시를 항상 그룹 A에 포함시키는 전략으로 불필요한 대칭적 파티션(`(A, B)`와 `(B, A)`) 생성을 방지하여 코드의 논리적 오류 가능성을 줄이고 효율을 높였습니다. 또한, 그룹 A가 항상 비어있지 않음을 보장하여 예외 처리 로직을 간소화했습니다.

이 최적화 방법은 주어진 N의 범위(N <= 10)에서 가능한 모든 분할을 탐색하는 백트래킹 기반 알고리즘 중 가장 효율적인 방법 중 하나입니다. N이 더 커진다면 동적 계획법이나 네트워크 플로우 같은 다른 접근 방식이 필요할 수 있지만, N=10에서는 이 브루트 포스 + 가지치기(불필요한 중복 및 비연결 그래프 제거) 방식이 최선입니다.