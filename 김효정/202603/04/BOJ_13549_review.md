# 코드 리뷰

**파일**: 김효정/202603/04/BOJ_13549.java

안녕하세요! BOJ 13549 (숨바꼭질 3) 문제 풀이에 대한 리뷰를 요청하셨네요. 코드를 잘 작성하셨습니다. 0-1 BFS의 핵심 아이디어를 정확하게 구현하셨어요.

아래는 요청하신 분석 항목별로 코드를 리뷰해 드립니다.

---

### BOJ_13549 코드 리뷰

**1. 시간/공간 복잡도**

*   **시간 복잡도: O(MAX\_POS)**
    *   이는 그래프 탐색 알고리즘(BFS)의 기본 시간 복잡도 `O(V + E)`와 유사합니다.
    *   `V` (정점의 수)는 `MAX_POS` (최대 100,001)에 해당합니다.
    *   `E` (간선의 수)는 각 정점에서 최대 3개의 간선(`-1`, `+1`, `*2`)이 존재하므로 대략 `3 * V`가 됩니다.
    *   따라서 전체 시간 복잡도는 `O(V)` 즉 `O(100001)`이 되며, 이는 매우 효율적인 성능입니다. 각 노드는 `dist`가 갱신될 때 큐에 한 번 추가되며, 최대 한 번 처리됩니다.
*   **공간 복잡도: O(MAX\_POS)**
    *   `dist` 배열의 크기가 `100002`이므로 `O(MAX_POS)`에 해당합니다.
    *   `Deque`에 저장될 수 있는 최대 원소의 개수도 `MAX_POS`에 비례합니다.
    *   따라서 전체 공간 복잡도는 `O(100001)`이며, 이 역시 문제의 제약 조건 내에서 충분히 허용 가능한 수준입니다.

**2. 논리 오류**

이 코드에는 **명백한 논리 오류는 없습니다.**
0-1 BFS의 핵심 아이디어를 정확하게 구현하여 최단 거리를 잘 찾아냅니다.

*   **0-1 BFS 구현:** 비용이 0인 이동(`2*current`)은 `offerFirst`로 덱의 앞에 삽입하고, 비용이 1인 이동(`current-1`, `current+1`)은 `offer`로 덱의 뒤에 삽입하는 방식은 0-1 BFS의 정석적인 구현입니다. 이를 통해 일반적인 BFS처럼 현재 단계에서 1의 비용이 드는 노드들을 처리하기 전에, 0의 비용으로 갈 수 있는 모든 노드를 먼저 처리하게 하여 최단 경로를 보장합니다.
*   **`dist` 배열 활용:** `dist` 배열을 `Integer.MAX_VALUE`로 초기화하고, 새로운 경로가 더 짧을 경우에만 `dist` 값을 갱신하고 큐에 추가하는 방식은 다익스트라나 BFS 기반 최단 경로 알고리즘에서 매우 중요한 정확한 방법입니다.
*   **경계 조건 처리:** `next > 100001 || next < 0`을 통해 유효하지 않은 인덱스 접근을 방지하는 것도 올바릅니다.

**3. 더 효율적인 방법**

현재 구현된 0-1 BFS는 이 문제에 대해 **가장 효율적인 일반적인 방법 중 하나**입니다. 엣지 가중치가 0 또는 1인 그래프에서 최단 경로를 찾는 데 O(V+E) 시간 복잡도를 달성할 수 있는 알고리즘은 0-1 BFS가 최적에 가깝습니다.

특정 `N`과 `K` 값에 대해 더 빠른 (예: 수학적 규칙) 방법이 있을 수 있지만, 범용적인 해법으로는 현재 코드가 매우 훌륭합니다. 예를 들어 `N >= K`인 경우 `N-K`가 답이 되지만, BFS는 이를 자동으로 찾아내며 전체 복잡도를 크게 늘리지 않습니다.

**4. 놓친 엣지 케이스**

대부분의 엣지 케이스를 잘 처리하고 있습니다.

*   **`N == K`:** `dist[start] = 0`이므로 즉시 0을 반환합니다. (정상)
*   **`N > K`:** (예: N=10, K=5) `current-1` 연산이 최단 경로를 찾게 됩니다. (`2*current`나 `current+1`은 거리가 멀어지므로 선택되지 않거나 더 긴 경로로 간주됩니다.)
*   **`N = 0`:** `current=0`일 때 `2*current`는 여전히 0이 되므로 문제가 없습니다. `current-1`은 유효하지 않다고 처리됩니다.
*   **`K`가 최대 범위 근처일 때:** `dist` 배열의 크기가 `100002`이고 `MAX_POS`가 `100001`이므로, `K`가 `100000`인 경우에도 `K+1` (`100001`)까지의 인덱스를 안전하게 처리할 수 있습니다.
*   **`K < N`이더라도 `2*N`이 `K`와 가까운 경우:** 예를 들어 `N=10, K=12`. 일반적인 생각으로는 `10 -> 11 -> 12` (2초)가 떠오르지만, `10 -> 20 -> 19 -> ... -> 12`와 같은 경로는 훨씬 비효율적입니다. 0-1 BFS는 이러한 경로를 탐색하더라도 `dist` 배열을 통해 더 짧은 경로를 유지하므로 문제가 없습니다.

**5. 코드 개선점**

몇 가지 가독성 및 관례에 대한 개선점을 제안합니다.

*   **매직 넘버 제거:** `100002`, `100001`, `0`, `1` 등 상수로 정의할 수 있는 값들을 상수로 선언하면 코드의 의미를 명확히 하고 유지보수를 용이하게 합니다.
    ```java
    // class BOJ_13549 { ... }
    private static final int MAX_POSITION = 100000; // 문제에서 N, K의 최대값
    private static final int DIST_ARRAY_SIZE = MAX_POSITION + 2; // 배열은 0부터 MAX_POSITION+1까지 필요할 수 있으므로
    private static final int TELEPORT_COST = 0;
    private static final int WALK_COST = 1;
    ```
    그리고 `dist` 배열 선언 시 `new int[DIST_ARRAY_SIZE]`, 경계 체크 시 `next > MAX_POSITION || next < 0` 등으로 사용하면 좋습니다.

*   **Scanner 리소스 닫기:** `Scanner` 객체는 사용 후 닫아주는 것이 좋습니다. `main` 메서드 끝에 `sc.close();`를 추가해주세요.
    ```java
    // main 메서드 마지막 부분
    System.out.println(out);
    sc.close(); // 추가
    ```

*   **`for` 루프 내 `next` 계산 개선 (가독성):** 현재 `i` 값에 따른 삼항 연산자는 효율적이지만, 각 이동 유형이 무엇을 의미하는지 한눈에 파악하기는 다소 어려울 수 있습니다. 각 연산을 명시적으로 작성하는 것이 가독성 측면에서 더 나을 수 있습니다.
    ```java
    // 기존 코드
    // int next = 0;
    // for (int i = 0; i < 3; i++) {
    //     next = (i == 0) ? current - 1 : (i == 1) ? current + 1 : 2*current;
    //     ...
    //     int newcost = dist[current] + ((i == 2) ? 0 : 1);
    //     ...
    // }

    // 개선 제안: 각 연산을 개별적으로 처리
    // 1. current - 1
    int nextMinus1 = current - 1;
    int costMinus1 = WALK_COST;
    if (nextMinus1 >= 0) { // 하한만 체크, 상한은 100000 넘어도 문제없음 (K보다 작으면 100000 넘을 일 없기 때문)
        if (dist[current] + costMinus1 < dist[nextMinus1]) {
            dist[nextMinus1] = dist[current] + costMinus1;
            q.offer(nextMinus1);
        }
    }

    // 2. current + 1
    int nextPlus1 = current + 1;
    int costPlus1 = WALK_COST;
    if (nextPlus1 <= MAX_POSITION + 1) { // 100001까지 방문 가능
        if (dist[current] + costPlus1 < dist[nextPlus1]) {
            dist[nextPlus1] = dist[current] + costPlus1;
            q.offer(nextPlus1);
        }
    }

    // 3. 2 * current
    int nextTeleport = current * 2;
    int costTeleport = TELEPORT_COST;
    if (nextTeleport <= MAX_POSITION + 1) { // 텔레포트 상한은 다른 것과 동일하게
        // 텔레포트 비용이 0이므로, dist[current]와 같거나 작아야 한다.
        // 이 로직은 항상 0-cost 이동이 다른 이동보다 우선적으로 처리되도록 보장한다.
        if (dist[current] + costTeleport < dist[nextTeleport]) {
            dist[nextTeleport] = dist[current] + costTeleport;
            q.offerFirst(nextTeleport); // 0-cost 이므로 offerFirst
        }
    }
    ```
    이 방식은 코드가 조금 길어질 수 있지만, 각 이동이 명확히 분리되어 있어 어떤 작업이 일어나는지 이해하기가 훨씬 쉽습니다. 특히 조건문 `(i == 2) ? 0 : 1`와 같은 부분을 피할 수 있습니다.
    참고로, `next > 100001` 부분은 `MAX_POSITION`에 따라 `next > MAX_POSITION + 1` 정도로 변경될 수 있습니다. (K가 10만일 때 N=5만에서 *2하면 10만) 즉, `MAX_POSITION`보다 K가 더 클 수도 있기 때문에, 탐색 범위는 `MAX_POSITION`보다 조금 더 넓게 잡아주는 것이 안전합니다. 문제에선 N,K가 100,000 이하이므로 `100001`은 적절합니다.

*   **`dist` 배열 크기에 대한 약간의 명확화:** `dist` 배열의 인덱스는 0부터 시작하여 `MAX_POSITION`까지 사용되므로, `MAX_POSITION + 1` 크기가 필요합니다. `100002`는 `MAX_POSITION + 2`와 같으므로 인덱스 `0`부터 `100001`까지 커버됩니다. 이는 K가 100000일 때 `K+1` (100001)로 이동하는 경우까지 고려한 것으로 보이며, 충분히 안전합니다.

---

**결론적으로,** 현재 코드는 문제의 핵심을 정확히 꿰뚫고 있으며 효율적인 알고리즘으로 작성되었습니다. 몇 가지 코드 스타일 및 가독성 개선점은 있지만, 기능적으로는 매우 훌륭한 솔루션입니다. 잘하셨습니다!