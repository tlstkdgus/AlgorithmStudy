# 최적화 분석

제공해주신 코드는 전형적인 백트래킹(부분집합 생성)과 BFS/DFS를 이용한 연결성 검사 문제 해결 방식입니다. BOJ 17471 (게리맨더링) 문제는 N의 범위가 10으로 작기 때문에, 이 방식이 일반적으로 허용됩니다. 하지만 몇 가지 개선점을 적용하여 시간/공간 효율을 약간 높이고 가독성을 개선할 수 있습니다.

---

### 현재 코드 분석

*   **알고리즘:** N개의 구역을 두 팀으로 나누는 모든 경우의 수를 백트래킹(`makeSubset`)으로 생성하고, 각 경우에 대해 두 팀 모두 연결되어 있는지 BFS(`isConnected`)로 확인한 후, 인구 차이를 계산합니다.
*   **시간 복잡도:**
    *   `makeSubset`: $N$개의 구역을 두 팀으로 나누는 모든 경우의 수 ($2^N$가지)를 탐색합니다.
    *   `isConnected`: BFS/DFS는 $O(V+E)$ 시간 복잡도를 가집니다. 여기서 $V$는 구역 수 ($N$), $E$는 간선 수 ($M$)입니다. 각 경우의 수마다 두 번 호출됩니다.
    *   `calculatePopulationDifference`: $O(N)$ 시간 복잡도를 가집니다.
    *   **총:** $O(2^N \times (N + M))$
    *   N=10일 때, $2^{10} \times (10 + 10 \times 9 / 2) = 1024 \times (10 + 45) = 1024 \times 55 \approx 5.6 \times 10^4$
    *   이 정도 연산은 N=10에서 1초 내에 충분히 실행됩니다.
*   **공간 복잡도:**
    *   `population`, `adjList`, `isSelected`, `visited`, `queue`: $O(N+M)$
    *   재귀 스택: $O(N)$
    *   **총:** $O(N+M)$
    *   N=10일 때도 $O(10+45)$ 정도로 매우 적습니다.

---

### 개선점 제안

현재 코드는 N=10이라는 제약 조건 내에서 충분히 효율적입니다. 하지만 다음의 측면에서 더 최적화할 수 있습니다.

#### 1. 시간 복잡도 개선 방법

*   **대칭성 활용 (Search Space 절반으로 줄이기):**
    *   두 개의 그룹 (A, B)으로 나누는 경우, (A, B)와 (B, A)는 본질적으로 같은 분할입니다. `makeSubset`은 이 두 경우를 모두 생성합니다.
    *   이를 방지하기 위해, 특정 구역 (예: 1번 구역)을 항상 한쪽 그룹(예: `isGroupA[1] = true`)에 포함시키도록 고정할 수 있습니다. 이렇게 하면 탐색 공간이 $2^N$에서 $2^{N-1}$로 절반으로 줄어듭니다.
    *   이렇게 해도 모든 유효한 분할을 찾을 수 있습니다.
*   **빈 그룹 조기 확인:**
    *   `makeSubset`의 베이스 케이스에서 `countA == 0 || countA == N` 인 경우 (즉, 한쪽 그룹이 비어있는 경우) 즉시 리턴하여 불필요한 `isConnected` 호출을 막을 수 있습니다. 현재 코드에도 유사한 로직이 있지만, `isConnected` 내부에서 `targetCount == 0`을 확인하는 대신, `makeSubset`의 베이스 케이스에서 미리 판단하는 것이 좋습니다.

#### 2. 공간 복잡도 개선 방법

*   **재귀 스택 깊이:** N=10이므로 $O(N)$ 재귀 스택은 문제가 되지 않습니다.
*   **지역 변수 재사용:** `isConnected` 내 `Queue`와 `boolean[] visited`는 함수 호출마다 새로 생성됩니다. 이는 작은 N에서는 큰 문제가 아니지만, 가능하다면 멤버 변수로 선언하고 `clear()` 또는 `Arrays.fill()`로 초기화하여 객체 생성 오버헤드를 줄일 수 있습니다. (하지만 현대 JVM은 이 정도는 잘 최적화하므로 체감 성능 향상은 미미할 수 있습니다.) 현재 방식도 메모리 관리 측면에서는 명확합니다.

#### 3. 가독성 개선

*   **변수명 변경:** `isSelected`를 `isGroupA`와 같이 더 명확하게 변경하여 `true`가 어떤 그룹을 의미하는지 직관적으로 알 수 있도록 합니다. `targetTeam`도 `targetGroupA` 등으로 변경하면 좋습니다.
*   **매직 넘버 제거:** `N+1`과 같은 인덱스 처리 부분을 `N`의 범위를 고려한 명확한 주석이나 상수 선언으로 보완할 수 있습니다. (경쟁 프로그래밍에서는 1-based indexing이 흔하므로 이 자체는 큰 문제는 아닙니다.)
*   **주석 추가:** 복잡한 로직이나 최적화 부분에 주석을 달아 이해를 돕습니다.
*   **Stream API (선택적):** 인구 합계를 계산하는 부분에서 Stream API를 사용할 수도 있으나, 성능 상의 이점은 없으며 오히려 미세하게 느릴 수 있어 경쟁 프로그래밍에서는 일반적인 `for` 루프가 선호됩니다.

---

### 최종 최적화된 코드

아래 코드는 위에서 제시된 시간 복잡도 개선 (대칭성 활용, 빈 그룹 조기 확인) 및 가독성 개선을 적용한 버전입니다. 공간 복잡도는 N=10에서 큰 문제가 없으므로 현재 구조를 유지합니다.

```java
package BOJ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class BOJ_17471_Optimized {
    static int N;
    static int[] population;
    static ArrayList<Integer>[] adjList;
    static boolean[] isGroupA; // true면 Group A, false면 Group B에 속함
    static int minDiff = Integer.MAX_VALUE;

    /**
     * N개의 구역을 두 그룹(Group A, Group B)으로 나누는 모든 경우의 수를 재귀적으로 탐색합니다.
     * 첫 번째 구역은 항상 Group A에 할당하여 대칭적인 중복을 방지합니다 (탐색 공간 절반).
     *
     * @param depth 현재 구역 번호 (1부터 N까지)
     */
    static void divideDistricts(int depth) {
        // Base Case: 모든 구역에 대한 그룹 할당이 완료된 경우
        if (depth == N + 1) {
            // 그룹 A에 속한 구역의 수를 계산
            int countA = 0;
            for (int i = 1; i <= N; i++) {
                if (isGroupA[i]) {
                    countA++;
                }
            }

            // 어느 한 쪽 그룹이 비어있으면 유효한 분할이 아님
            // (즉, countA == 0 이면 Group A가 비었고, countA == N 이면 Group B가 비었다는 의미)
            if (countA == 0 || countA == N) {
                return; // 다음 경우의 수 탐색
            }

            // 두 그룹이 각각 연결되어 있는지 확인
            boolean isAConnected = isConnected(true);  // Group A의 연결성 확인
            boolean isBConnected = isConnected(false); // Group B의 연결성 확인

            if (isAConnected && isBConnected) {
                calculatePopulationDifference(); // 두 그룹 모두 연결되어 있다면 인구 차이 계산
            }
            return;
        }

        // Optimization: 1번 구역은 항상 Group A에 할당하여 대칭적인 중복을 제거 (2^N -> 2^(N-1))
        // 이로 인해 (Group A, Group B)와 (Group B, Group A) 중 한 가지만 탐색하게 됩니다.
        if (depth == 1) {
            isGroupA[depth] = true; // 1번 구역은 Group A에 포함
            divideDistricts(depth + 1); // 다음 구역으로 넘어감
        } else {
            // 현재 구역을 Group A에 할당하는 경우
            isGroupA[depth] = true;
            divideDistricts(depth + 1);

            // 현재 구역을 Group B에 할당하는 경우
            isGroupA[depth] = false;
            divideDistricts(depth + 1);
        }
    }

    /**
     * 특정 그룹(targetGroupA)에 속한 모든 구역들이 서로 연결되어 있는지 BFS를 통해 확인합니다.
     *
     * @param targetGroupA 확인할 그룹. true면 Group A, false면 Group B를 의미
     * @return 해당 그룹이 연결되어 있으면 true, 아니면 false
     */
    static boolean isConnected(boolean targetGroupA) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[N + 1]; // BFS 방문 여부 기록

        int startNode = -1;       // BFS 시작점
        int targetCount = 0;      // targetGroupA에 속한 총 구역 수

        // targetGroupA에 속하는 구역들을 찾고, BFS 시작점 설정 및 총 구역 수 계산
        for (int i = 1; i <= N; i++) {
            if (isGroupA[i] == targetGroupA) {
                targetCount++;
                if (startNode == -1) { // 첫 번째로 찾은 구역을 시작점으로 설정
                    startNode = i;
                }
            }
        }

        // (이 부분은 divideDistricts에서 빈 그룹 검사를 하므로 불필요하지만,
        // isConnected 자체의 견고성을 위해 유지할 수 있습니다.)
        // 만약 targetGroupA에 속한 구역이 하나도 없다면 연결되었다고 볼 수 없음 (유효한 그룹이 아님)
        if (targetCount == 0) return false;

        // BFS 시작
        queue.offer(startNode);
        visited[startNode] = true;
        int connectedCount = 1; // 시작 구역부터 연결된 구역의 수

        while (!queue.isEmpty()) {
            int curr = queue.poll();

            for (int next : adjList[curr]) {
                // 이웃 구역이 targetGroupA에 속하고 아직 방문하지 않았다면 탐색
                if (!visited[next] && isGroupA[next] == targetGroupA) {
                    visited[next] = true;
                    queue.offer(next);
                    connectedCount++;
                }
            }
        }

        // BFS를 통해 연결된 구역의 수가 targetGroupA의 총 구역 수와 같으면 연결된 것임
        return connectedCount == targetCount;
    }

    /**
     * Group A와 Group B의 총 인구수를 계산하고, 그 차이를 최소값(minDiff)과 비교하여 업데이트합니다.
     */
    static void calculatePopulationDifference() {
        int sumA = 0;
        int sumB = 0;

        for (int i = 1; i <= N; i++) {
            if (isGroupA[i]) {
                sumA += population[i];
            } else {
                sumB += population[i];
            }
        }

        int diff = Math.abs(sumA - sumB);
        minDiff = Math.min(diff, minDiff);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        population = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            population[i] = sc.nextInt();
        }

        adjList = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            adjList[i] = new ArrayList<>();
        }

        for (int i = 1; i <= N; i++) {
            int adjCount = sc.nextInt();
            for (int j = 0; j < adjCount; j++) {
                int neighbor = sc.nextInt();
                adjList[i].add(neighbor);
            }
        }

        isGroupA = new boolean[N + 1];

        // 구역 분할 시작 (1번 구역은 divideDistricts 내부에서 Group A로 고정됨)
        divideDistricts(1);

        // minDiff가 초기값(Integer.MAX_VALUE)이라면 유효한 분할을 찾지 못한 것
        System.out.println(minDiff == Integer.MAX_VALUE ? -1 : minDiff);

        sc.close();
    }
}
```

---

### 왜 이 방법이 최선인가?

1.  **문제의 특성 (N이 작음):**
    *   N이 최대 10인 경우, $2^N$ 가지의 모든 부분집합을 생성하는 것은 계산적으로 충분히 가능합니다. $2^{10} = 1024$는 매우 작은 숫자입니다.
    *   따라서 지수 시간 복잡도를 가지더라도, N이 작으면 이는 효과적인 방법이 됩니다. N이 커지면 (예: N=20) 이 방법은 불가능하며, 다른 알고리즘 (예: Meet-in-the-Middle, 동적 계획법 등)을 고려해야 합니다.

2.  **모든 경우의 수 탐색의 필요성:**
    *   이 문제는 "두 그룹으로 나누어지는 모든 경우의 수" 중에서 특정 조건을 만족하는 (두 그룹 모두 연결되고, 인구 차이가 최소인) 경우를 찾아야 합니다.
    *   그래프의 연결성은 구역들이 어떻게 분할되는지에 따라 복잡하게 변하기 때문에, 특정 휴리스틱이나 그리디 알고리즘으로 최적해를 찾는 것이 매우 어렵거나 불가능합니다. 모든 경우를 살펴보는 것이 안전하고 확실한 방법입니다.

3.  **대칭성 활용 ($2^{N-1}$):**
    *   $2^N$ 탐색 공간에서 대칭적인 중복을 제거하여 $2^{N-1}$로 줄이는 것은 이 유형의 문제에서 적용할 수 있는 가장 기본적인 시간 복잡도 최적화 중 하나입니다. 이는 전체 연산 시간을 절반으로 줄여줍니다. N=10일 경우, $1024$에서 $512$로 줄어들어 약 2배 빨라집니다.

4.  **효율적인 연결성 검사 (BFS/DFS):**
    *   그래프에서 연결성을 검사하는 가장 효율적인 방법은 BFS 또는 DFS입니다. $O(V+E)$의 선형 시간에 동작하므로, $N=10, M=45$에서는 매우 빠르게 동작합니다.

종합적으로 볼 때, N의 제약 조건과 문제의 본질(모든 분할 고려)을 고려했을 때, 백트래킹을 이용한 $2^{N-1}$ 부분집합 생성과 각 경우마다 BFS로 연결성을 검사하는 방법은 BOJ 17471 문제에 대해 **가장 실용적이고 최적인 접근 방식**입니다.