# 코드 리뷰

**파일**: 김효정/202603/03/BOJ_17471.java

안녕하세요! BOJ 17471 (게리맨더링) 문제 풀이에 대한 코드 리뷰 요청 감사합니다. 비판적이지만 건설적인 관점에서 분석해 드리겠습니다.

---

### 코드 분석 및 리뷰

**문제 요약:** N개의 구역을 두 개의 선거구(A, B)로 나눕니다. 각 선거구는 비어있지 않아야 하며, 각 선거구에 속한 구역들은 서로 연결되어 있어야 합니다. 두 선거구의 인구 차이를 최소화하는 것이 목표입니다.

제출하신 코드는 모든 가능한 구역 분할 조합(부분집합)을 생성하고, 각 분할에 대해 BFS(너비 우선 탐색)를 사용하여 연결성을 확인하는 방식으로 문제를 해결하고 있습니다.

---

### 1. 시간/공간 복잡도

*   **입력 처리 (초기화):**
    *   `populations` 배열, `districts` 맵 초기화: `O(N + M)` (M은 총 간선의 수).
*   **부분집합 생성 (`powerset` 메소드):**
    *   `N`개의 구역 각각에 대해 포함/미포함 두 가지 선택지가 있으므로 `2^N`개의 부분집합이 생성됩니다.
    *   각 부분집합을 생성할 때 `for` 루프를 돌며 `distA`, `distB`에 요소를 추가하므로 `O(N)` 시간이 걸립니다.
    *   따라서 `powerset` 호출 및 `powSetA`, `powSetB` 채우는 데 걸리는 시간 복잡도는 `O(N * 2^N)`입니다.
    *   `powSetA`와 `powSetB`에 저장되는 데이터의 총량은 `O(N * 2^N)`이므로 공간 복잡도도 `O(N * 2^N)`입니다.
*   **메인 루프 (`for (int i = 0; i < powSetA.size(); i++)`):**
    *   `powSetA.size()`는 `2^N`개이므로, 루프는 `2^N`번 반복됩니다.
    *   각 루프 내에서 두 번의 `search` 호출이 있습니다.
*   **연결성 확인 (`search` 메소드 - **핵심 병목**):**
    *   BFS의 기본 시간 복잡도는 `O(V + E)`입니다. 여기서 `V`는 현재 그룹의 구역 수(`|distList|`), `E`는 해당 그룹 내의 간선 수입니다.
    *   문제는 `if (!isVisited[adj] && distList.contains(adj))` 부분입니다.
        *   `distList.contains(adj)`는 `ArrayList`의 `contains` 메소드를 사용하므로, `distList`의 크기(`O(N)`)에 비례하는 시간이 걸립니다.
        *   최악의 경우, 한 구역에 연결된 모든 간선(`degree`)마다 `O(N)` 탐색이 발생합니다. 그래프 전체의 간선 수가 `M`일 때, `distList.contains` 호출은 총 `O(M * N)`의 시간을 소모하게 됩니다.
        *   따라서 `search` 메소드의 총 시간 복잡도는 `O(V + M * N)` = `O(N + M * N)` = `O(N^3)` (최대 간선 수 `M`은 `O(N^2)`이므로)입니다.
*   **전체 시간 복잡도:**
    *   `O(N * 2^N)` (부분집합 생성) + `O(2^N * N^3)` (메인 루프 내 search 호출)
    *   지배적인 항은 `O(2^N * N^3)`입니다. N=10일 때, `1024 * 1000 = 10^6` 정도여서 최악의 경우 아슬아슬하게 통과할 수도 있습니다. (실제 테스트 케이스는 평균적으로 덜 빡빡하기 때문)
*   **전체 공간 복잡도:**
    *   `O(N * 2^N)` (부분집합 저장) + `O(N + M)` (구역 정보, 인접 리스트)
    *   지배적인 항은 `O(N * 2^N)`입니다. N=10일 때, `10 * 1024` = 약 10KB 정도이므로 충분합니다.

---

### 2. 논리 오류 여부

코드는 전체적으로 문제 해결의 논리를 잘 따르고 있습니다. 몇 가지 사소한 부분이 있지만, 결과의 정확성에 치명적인 논리 오류는 발견되지 않았습니다.

*   **부분집합 생성 시 빈 집합 처리:** `powerset` 메소드는 `distA`가 비어 있거나 `distB`가 비어 있는 경우를 생성합니다. 예를 들어, `distA`가 `{1,2,3}`이고 `distB`가 `{}`인 경우입니다. 문제 조건에서 "각 선거구는 적어도 하나의 구역을 포함해야 한다"고 명시되어 있습니다.
    *   현재 코드는 `search` 메소드 시작 부분에 `if (distList.isEmpty()) return false;`가 있어서, 이 경우 `false`를 반환하도록 처리하고 있습니다. 이는 문제 조건에 맞춰 올바르게 동작합니다. 따라서 `powerset`에서 명시적으로 필터링하지 않아도 결과적으로는 문제가 없습니다. 다만, 미리 필터링하면 불필요한 `search` 호출을 줄일 수 있습니다.
*   **`search` 메소드의 `isVisited` 인자:** `main` 메소드에서 `isVisitedA`와 `isVisitedB`를 각각 새로 생성하여 `search`에 전달하는 방식은 올바릅니다. (다른 그룹의 방문 여부에 영향을 주지 않아야 하므로)

---

### 3. 더 효율적인 방법

주요 개선점은 `search` 메소드의 효율성입니다.

*   **`search` 메소드 최적화 (`distList.contains()` 제거):**
    가장 큰 병목은 `distList.contains(adj)`입니다. `ArrayList.contains()`는 리스트를 순회하기 때문에 `O(N)` 시간이 걸립니다. 이를 `O(1)`로 개선해야 합니다.
    *   **방법:** `search` 메소드를 호출하기 전에, 현재 그룹(`distList`)에 속하는 구역인지를 빠르게 확인할 수 있는 `boolean[]` 배열(예: `isMemberOfCurrentGroup`)을 만들어서 사용합니다.
    ```java
    public static boolean search(Queue<Integer> q, Map<Integer, List<Integer>> dists, List<Integer> distList, boolean[] isMemberOfCurrentGroup) {
        if (distList.isEmpty()) return false;

        // isMemberOfCurrentGroup 배열은 search 호출 전에 main에서 (또는 이 메서드 시작 시) 초기화되어야 합니다.
        // 예를 들어:
        // boolean[] isMemberOfCurrentGroup = new boolean[N + 1];
        // for (int node : distList) {
        //     isMemberOfCurrentGroup[node] = true;
        // }
        
        // BFS 자체의 방문 여부를 위한 배열 (전체 구역의 방문 여부와는 다름)
        boolean[] visitedInBFS = new boolean[isMemberOfCurrentGroup.length]; 

        int start = distList.get(0);
        q.offer(start);
        visitedInBFS[start] = true;
        int visitedCount = 1; // 현재 그룹 내에서 BFS로 방문한 구역 수

        while (!q.isEmpty()) {
            int current = q.poll();

            List<Integer> adjs = dists.get(current);
            if (adjs != null) { 
                for (int adj : adjs) {
                    // 1. 인접 구역이 현재 그룹에 속하며 (O(1) 확인)
                    // 2. BFS에서 아직 방문하지 않았다면
                    if (isMemberOfCurrentGroup[adj] && !visitedInBFS[adj]) {
                        visitedInBFS[adj] = true;
                        q.offer(adj);
                        visitedCount++;
                    }
                }
            }
        }
        // BFS를 마쳤을 때, 방문한 구역 수가 그룹의 전체 구역 수와 같다면 연결된 것
        return visitedCount == distList.size();
    }
    ```
    이 최적화를 적용하면 `search` 메소드의 시간 복잡도는 `O(N + M)`으로 줄어들어, 전체 시간 복잡도가 `O(2^N * (N + M))`이 됩니다. N=10, M=N^2일 때 `1024 * (10 + 100) = 1024 * 110`으로 약 `10^5` 정도로 훨씬 빨라집니다.

*   **부분집합 생성 효율화 (중복 제거):**
    현재 `powerset`는 `(A, B)`와 `(B, A)`를 모두 생성합니다. 예를 들어 `({1,2}, {3,4})`와 `({3,4}, {1,2})`를 모두 만듭니다. 우리는 이 중 하나만 확인해도 됩니다.
    *   `distA.size()`가 `N/2`보다 큰 경우는 처리하지 않도록 `powerset`에서 `return`하는 조건을 추가하면, 반복 횟수를 절반으로 줄일 수 있습니다. (단, `distA.size() == N/2`인 경우는 정확히 한 번만 추가되도록 해야 합니다.)
    *   예를 들어, `if (distA.isEmpty() || distB.isEmpty()) return;` 후에 `if (distA.size() > nDist / 2) return;` 조건을 추가할 수 있습니다.
    *   이렇게 하면 `main` 루프의 반복 횟수는 `(2^N - 2) / 2`가 됩니다. (빈 집합 두 개를 제외하고 2로 나눔)

---

### 4. 놓친 엣지 케이스

*   **`N=1` 일 때:** `powerset`는 `({1}, {})`와 `({}, {1})`를 생성하고, `search`가 `distList.isEmpty()`를 체크하여 `false`를 반환합니다. 결과적으로 `possible`은 `false`가 되고 `-1`을 출력합니다. 이는 "두 선거구로 나누어야 하며 각 선거구는 적어도 하나의 구역을 포함해야 한다"는 문제 조건에 따라 올바른 동작입니다.
*   **연결되지 않은 그래프:** 구역들이 서로 연결되어 있지 않은 경우, `search` 메소드는 연결성을 올바르게 판단하여 `false`를 반환합니다. 모든 부분집합에 대해 연결성이 확인되지 않으면 `possible`이 `false`로 남아 `-1`을 출력합니다. 이 또한 올바른 동작입니다.

제출하신 코드는 엣지 케이스를 잘 처리하고 있습니다.

---

### 5. 코드 개선점

*   **`search` 메소드 내 `distList.contains(adj)` 제거:** 위 `더 효율적인 방법` 섹션에서 설명한 대로 `boolean[] isMemberOfCurrentGroup` 배열을 활용하여 `O(1)` 체크로 변경하는 것이 가장 중요합니다.
*   **`powerset` 메소드의 부분집합 필터링:**
    ```java
    public static void powerset(boolean[] isIncluded, int nDist, int idx) {
        if (idx == nDist + 1) { // Standard base case: all N items considered (idx goes from 1 to nDist+1)
            List<Integer> distA = new ArrayList<>();
            List<Integer> distB = new ArrayList<>();
            for (int i = 1; i <= nDist; i++) {
                if (isIncluded[i])
                    distA.add(i);
                else
                    distB.add(i);
            }
            // 1. 문제 조건: 각 선거구는 적어도 하나의 구역을 포함해야 한다.
            if (distA.isEmpty() || distB.isEmpty()) {
                return;
            }
            // 2. 효율성: (A, B)와 (B, A)는 같은 경우이므로, distA의 크기가 N/2를 초과하면 대칭되는 경우가 이미 처리되었거나 처리될 것이므로 스킵.
            //    (N이 짝수일 때 distA.size() == N/2 인 경우는 한 번만 추가됨)
            if (distA.size() > nDist / 2) {
                return;
            }
            powSetA.add(distA);
            powSetB.add(distB);
            return;
        }

        isIncluded[idx] = true;
        powerset(isIncluded, nDist, idx + 1);
        isIncluded[idx] = false;
        powerset(isIncluded, nDist, idx + 1);
    }
    ```
    *   (`idx == nDist` 대신 `idx == nDist + 1`로 변경하는 것이 `N`개의 모든 항목을 처리하는 표준적인 재귀 종료 조건입니다.)
*   **인접 리스트 자료구조:** `Map<Integer, List<Integer>>` 대신 `List<List<Integer>>` (또는 `ArrayList<ArrayList<Integer>>`)를 사용하는 것이 좋습니다. 구역 번호가 1부터 N까지 연속적이기 때문에, 배열 기반 리스트가 인덱스 접근에 있어 약간 더 효율적이고 메모리 사용도 줄일 수 있습니다. (물론 `HashMap`도 N이 작을 때는 큰 차이가 없습니다.)
    ```java
    // main 메소드 내에서
    // Map<Integer, List<Integer>> districts = new HashMap<>(); 대신
    List<List<Integer>> districts = new ArrayList<>();
    for (int i = 0; i <= N; i++) { // 1-indexing을 위해 N+1 크기 초기화
        districts.add(new ArrayList<>());
    }
    // 인접 정보 읽을 때
    // districts.put(i, connected); 대신
    districts.set(i, connected); // i번째 인덱스에 연결 리스트 추가
    ```
*   **변수명/가독성:** 전반적으로 변수명이 명확하여 가독성이 좋습니다.

---

### 최종 요약

작성하신 코드는 문제의 요구사항을 정확히 이해하고 부분집합 + BFS를 통한 해결 전략을 잘 구현했습니다. 특히 `search` 메소드의 `distList.isEmpty()` 체크나 `main` 루프에서 `isVisitedA`, `isVisitedB`, `q.clear()`를 적절히 사용하는 등 세심한 부분들이 잘 처리되어 있습니다.

가장 중요한 개선점은 `search` 메소드의 `distList.contains(adj)` 부분을 `boolean[]` 배열을 활용한 `O(1)` 체크로 변경하는 것입니다. 이 변경만으로도 시간 복잡도가 `O(2^N * N^3)`에서 `O(2^N * N^2)` (또는 `O(2^N * (N+M))`)로 크게 향상되어, N=10과 같은 문제 제약 조건에서 훨씬 안정적으로 통과할 수 있습니다. `powerset`에서 중복되는 부분집합 생성을 줄이는 최적화도 상수 시간 계수를 줄이는 데 도움이 될 것입니다.

훌륭한 접근 방식입니다!