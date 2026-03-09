# 코드 리뷰

**파일**: 이원준/202603/09/SWEA_1767.java

안녕하세요! SWEA 1767 (프로세서 연결) 문제 풀이에 대한 상세한 리뷰입니다. 코드가 전반적으로 잘 작성되어 있으며, 백트래킹 접근 방식도 올바르게 적용되었습니다. 건설적인 비판과 개선점을 중심으로 살펴보겠습니다.

---

### 1. 시간/공간 복잡도

**시간 복잡도 (Time Complexity):**

*   **dfs 함수의 탐색 공간:** `coreList`에 있는 각 코어 (`M`개)에 대해 5가지 선택지 (상하좌우 4방향 연결, 또는 연결하지 않음)가 있습니다. 따라서 재귀 호출의 수는 최악의 경우 `O(5^M)`이 됩니다.
*   **`checkAndDraw` 함수:** 각 `dfs` 호출 내에서 `checkAndDraw` 함수는 그리드를 최대 `N`만큼 순회합니다. 이 함수는 연결 가능 여부를 판단할 때 한 번, 실제로 선을 그리거나 지울 때 한 번, 총 두 번의 순회를 합니다.
*   **총 시간 복잡도:** `O(M * N * 5^M)`
    *   `M`: `coreList.size()`, 즉 테두리에 있지 않은 프로세서의 개수.
    *   `N`: 그리드의 크기.
*   **문제 제약 조건:**
    *   `N`은 7 이상 12 이하의 정수입니다.
    *   프로세서의 총 개수는 1개 이상 12개 이하입니다.
    *   이는 `M`도 최대 12라는 의미입니다. (왜냐하면 `M`은 `initialCore`와 합쳐져 총 프로세서 수가 되기 때문)
*   **계산:** `M = 12`, `N = 12`라고 가정하면, `12 * 12 * 5^12 = 144 * 244,140,625 ≈ 3.5 * 10^10`이 됩니다. 이 값은 일반적인 제한 시간(1초~3초) 내에 통과하기에는 매우 큰 숫자입니다.
*   **실제 성능:** 이 정도의 이론적 최악 복잡도에도 불구하고 보통 통과하는 이유는 다음과 같습니다:
    *   **가지치기 (Pruning) 효과:** `dfs` 내에서 `maxCore`를 갱신하며, 이후 `coreCount`가 `maxCore`보다 낮을 가능성이 있는 경로는 탐색하지 않을 수 있어 실제 탐색 공간이 크게 줄어듭니다. (현재 코드에는 명시적인 가지치기 조건은 없지만, `maxCore`와 `minLineLength` 업데이트 로직이 간접적인 효과를 줍니다.)
    *   **평균 케이스:** 실제 테스트 케이스가 최악의 복잡도를 유발하는 경우가 드물 수 있습니다.
    *   **상수 요인:** 자바의 실행 속도 및 `checkAndDraw`의 상수 요인이 작을 수 있습니다.

**공간 복잡도 (Space Complexity):**

*   `coreArr`: `O(N^2)` - 그리드 저장
*   `coreList`: `O(M)` - 내부 코어 좌표 저장
*   재귀 스택: `O(M)` - `dfs` 호출 깊이
*   **총 공간 복잡도:** `O(N^2 + M)`
    *   `N=12, M=12`일 때, `12^2 + 12 = 144 + 12 = 156` 정도로 매우 작습니다. 공간 복잡도는 매우 효율적입니다.

### 2. 논리 오류 여부

논리적으로 큰 오류는 없어 보입니다.

*   **초기 코어 개수 처리 (`initialCore`):** 벽에 붙어있는 코어는 이미 전원에 연결된 것으로 간주하고 `initialCore`에 합산하는 방식은 올바릅니다. 이들은 선 길이가 0이므로 `minLineLength`에 영향을 주지 않으면서 `maxCore`를 높이는 데 기여합니다.
*   **`dfs` 함수의 핵심 로직:**
    *   `maxCore`를 최대로 유지하고, `maxCore`가 같을 경우 `minLineLength`를 최소로 유지하는 로직이 정확하게 구현되어 있습니다.
    *   각 코어에 대해 4방향으로 선을 연결 시도하고 (연결 가능하면), 연결하지 않는 경우 (`dfs(depth + 1, coreCount, currLength);`)를 모두 탐색하는 것이 백트래킹의 기본 원리를 잘 따르고 있습니다. 이 "연결하지 않는 경우"의 탐색이 중요한데, 모든 코어를 연결할 필요는 없기 때문입니다.
*   **`checkAndDraw` 함수의 선 그리기/지우기:**
    *   선을 그리기 전에 `value == 2`인 경우, 해당 방향으로 끝까지 갈 수 있는지 (다른 코어나 선을 만나지 않는지) 먼저 확인합니다.
    *   충돌 없이 끝까지 도달할 수 있다면, `coreArr`에 실제로 선을 그리고 길이를 반환합니다.
    *   백트래킹 시 `value == 0`으로 호출하여 그린 선을 다시 지우는 것도 정확합니다.
    *   이중 순회 방식 (검사 -> 그리기)은 안전하고 일반적인 방법입니다.

### 3. 더 효율적인 방법

시간 복잡도에서 언급했듯이, 현재 코드의 이론적 최악 시간 복잡도는 다소 높습니다. 다음은 효율성을 높일 수 있는 몇 가지 방법입니다.

1.  **가지치기 (Pruning) 추가:** `dfs` 함수 시작 부분에 강력한 가지치기 조건을 추가할 수 있습니다.
    ```java
    static void dfs(int depth, int coreCount, int currLength) {
        // [핵심 개선점] 가지치기:
        // 남은 모든 코어를 연결하더라도 현재까지의 maxCore를 갱신할 수 없다면, 더 이상 탐색할 필요가 없습니다.
        // 현재 연결된 코어 수 + 앞으로 연결할 수 있는 최대 코어 수 (남은 코어 수)
        if (coreCount + (coreList.size() - depth) < maxCore) {
            return; 
        }
        
        // 모든 코어를 다 탐색했을 경우 (기존 베이스 케이스)
        if (depth == coreList.size()) {
            if (coreCount > maxCore) {
                maxCore = coreCount;
                minLineLength = currLength;
            } else if (coreCount == maxCore) {
                minLineLength = Math.min(minLineLength, currLength);
            }
            return;
        }
        // ... (기존 코드 계속)
    }
    ```
    이 가지치기는 `maxCore`를 *갱신*할 가능성이 없을 때 경로를 잘라내므로 매우 효과적입니다.

2.  **`minLineLength`에 대한 가지치기 (선택적):** `maxCore`가 이미 정해진 상태에서 `minLineLength`를 줄이는 것이 목표일 때 사용할 수 있습니다.
    ```java
    static void dfs(int depth, int coreCount, int currLength) {
        // ... (위의 maxCore 가지치기)
        
        // maxCore가 이미 현재 coreCount + 남은 코어 수로 도달 가능하고,
        // 현재 currLength가 minLineLength보다 크거나 같으면, 더 이상 탐색할 필요가 없습니다.
        // (단, 이 조건은 maxCore가 갱신되는 상황에서 더 조심스럽게 사용해야 합니다.
        //  일반적으로는 maxCore에 대한 가지치기가 더 중요합니다.)
        // if (coreCount == maxCore && currLength >= minLineLength) {
        //     return;
        // }
        
        // ... (기존 코드 계속)
    }
    ```
    이 `minLineLength` 가지치기는 `maxCore`가 아직 확정되지 않았을 때는 오히려 최적해를 놓칠 수 있으므로, 일반적으로 `maxCore` 가지치기가 더 우선시됩니다. 현재 코드의 `maxCore` 갱신 로직이 이미 `minLineLength` 최소화를 포함하고 있으므로, 이 조건은 덜 중요합니다.

### 4. 놓친 엣지 케이스

현재 코드는 대부분의 엣지 케이스를 잘 처리합니다.

*   **모든 코어가 벽에 붙어있는 경우:** `coreList`가 비어있고, `dfs(0, initialCore, 0)`이 호출되어 `depth == coreList.size()`가 바로 참이 됩니다. `maxCore = initialCore`, `minLineLength = 0`으로 정확하게 설정됩니다.
*   **코어가 없는 경우:** `initialCore = 0`, `coreList` 비어있고, `dfs(0,0,0)`이 호출되어 `maxCore = 0`, `minLineLength = 0`으로 정확하게 설정됩니다.
*   **단일 코어가 중앙에 있는 경우:** 4방향을 모두 탐색하며 연결 시도 및 연결하지 않는 경우를 처리합니다.
*   **코어들이 서로 막고 있는 경우:** `checkAndDraw` 함수에서 다른 코어나 선을 만날 경우 `0`을 반환하므로, 연결이 불가능한 상황을 정확하게 처리합니다.
*   **아무 코어도 연결할 수 없는 경우:** "연결하지 않는" `dfs` 호출 (`dfs(depth + 1, coreCount, currLength);`)이 있으므로, 이 경우도 `maxCore`는 `initialCore`가 되고 `minLineLength`는 0이 됩니다.

전반적으로 엣지 케이스 처리도 잘 되어 있습니다.

### 5. 코드 개선점

1.  **가지치기 로직 추가 (위에서 설명):** 가장 중요한 개선점입니다.
2.  **상수(Magic Number) 사용 줄이기:**
    *   `coreArr`에서 `1`은 코어, `0`은 빈 공간, `2`는 연결된 선을 의미합니다. `static final int` 변수로 정의하여 가독성을 높일 수 있습니다.
    ```java
    static final int CORE = 1;
    static final int EMPTY = 0;
    static final int WIRE = 2;
    // ...
    // if(coreArr[i][j] == CORE) { ... }
    // if(coreArr[nx][ny] != EMPTY) return 0;
    // coreArr[nx][ny] = value; // value는 WIRE 또는 EMPTY
    ```
3.  **변수명 명확화:**
    *   `dr`은 `delta direction`이 아니라 `direction index`를 의미하므로, `direction`이나 `dirIdx` 등으로 변경하면 더 직관적일 수 있습니다. (예: `for(int dir = 0; dir < 4; dir++)`)
4.  **Scanner 닫기:** `main` 메서드 끝에서 `sc.close();`를 호출하여 리소스를 해제하는 것이 좋습니다.
5.  **`checkAndDraw` 함수 내부 `value` 사용:**
    `checkAndDraw` 함수에서 `if(value == 2)` 로직 블록은 사실상 '선을 그릴 수 있는지 확인하는 과정'입니다. `value`를 인자로 받아 그 값을 사용하는 대신, `boolean checkOnly`와 같은 인자를 추가하여 의도를 명확히 할 수도 있습니다. 현재 방식도 동작에는 문제 없습니다.

---

### 결론

제시된 코드는 SWEA 1767 문제의 요구사항을 정확히 이해하고 백트래킹을 활용하여 구현한 훌륭한 솔루션입니다. 특히 `initialCore` 처리, `dfs`의 모든 경우의 수 탐색 (연결/미연결), 그리고 `checkAndDraw`를 통한 충돌 감지 및 백트래킹이 견고합니다.

가장 중요한 개선점은 **가지치기 (Pruning)** 로직을 `dfs` 함수에 추가하여 탐색 효율을 높이는 것입니다. 이 부분을 적용하면 `N`과 `M`의 제약 조건에도 불구하고 시간 초과 없이 통과할 가능성이 매우 높아질 것입니다. 나머지 개선점들은 주로 코드 가독성과 유지보수성에 관한 내용입니다.