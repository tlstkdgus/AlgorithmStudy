# 최적화 분석

주어진 코드는 16x16 미로에서 시작점(2)에서 도착점(3)까지 경로가 존재하는지 Depth-First Search (DFS) 알고리즘을 사용하여 탐색하는 문제입니다. 현재 코드는 기능적으로는 올바르게 동작하지만, 몇 가지 개선할 여지가 있습니다.

### 1. 시간 복잡도 개선 방법

**현재 코드의 시간 복잡도:**
DFS는 그래프의 모든 정점과 간선을 한 번씩 방문하므로, $O(V+E)$의 시간 복잡도를 가집니다. 여기서 $V$는 정점의 수, $E$는 간선의 수입니다. 16x16 미로의 경우 정점 $V = 16 \times 16 = 256$개이고, 각 정점은 최대 4개의 간선을 가질 수 있으므로 $E \approx 4V$입니다. 따라서 시간 복잡도는 $O(SIZE \times SIZE)$가 됩니다.

**개선 방법:**
이 문제(경로 존재 여부)에 대한 최적의 탐색 알고리즘은 DFS나 BFS입니다. 현재 코드는 DFS를 사용하고 있으며, 각 셀을 한 번씩만 방문하도록 `visited` 배열을 사용하여 중복 탐색을 방지하고 있습니다. 또한, 목적지에 도달하면 즉시 탐색을 중단하는 `isExist = true; return;` 로직이 잘 구현되어 있습니다.

따라서, **시간 복잡도 측면에서는 이미 효율적인 알고리즘(DFS)을 사용하고 있으며, 더 이상의 유의미한 알고리즘적 개선은 어렵습니다.** 16x16이라는 작은 맵 크기를 고려할 때 현재 DFS의 성능은 충분합니다.

### 2. 공간 복잡도 개선 방법

**현재 코드의 공간 복잡도:**
*   `map` 배열: `int[16][16]` - $O(SIZE \times SIZE)$
*   `visited` 배열: `boolean[16][16]` - $O(SIZE \times SIZE)$
*   재귀 호출 스택: 최악의 경우(긴 경로를 따라가는 미로) $O(SIZE \times SIZE)$ 깊이까지 쌓일 수 있습니다.
*   전역 변수: `startX`, `startY`, `finishX`, `finishY`, `isExist`, `tNum` 등

**개선 방법:**
1.  **전역 변수 최소화:** 현재 `map`, `visited`, `startX`, `startY`, `finishX`, `finishY`, `isExist` 등 많은 변수가 전역으로 선언되어 있습니다. 이 변수들을 `main` 메서드 내 지역 변수로 선언하고 필요한 경우 `dfs` 메서드의 파라미터로 전달하는 것이 좋습니다. 이렇게 하면 코드의 캡슐화가 향상되고, 여러 스레드에서 동시에 `dfs`를 호출하는 경우 발생할 수 있는 잠재적인 문제를 방지할 수 있습니다 (물론 이 문제에서는 해당되지 않지만, 좋은 습관입니다). 특히 `isExist`와 같은 결과 플래그는 `dfs` 메서드의 반환 값으로 처리할 수 있습니다.
2.  **상수 사용:** `16`과 같은 "매직 넘버"를 `SIZE`와 같은 상수로 정의하여 사용하면 코드의 가독성과 유지보수성이 향상됩니다.
3.  **`DX`, `DY` 배열:** 이 배열들은 상수로 사용되므로 `final static`으로 선언하여 불변성을 명시하는 것이 좋습니다.

**구체적인 코드 변경 예시 (공간복잡도 + 가독성):**

*   **`map`, `visited` 로컬 변수로 변경:**
    ```java
    // 변경 전:
    // static int[][] map;
    // static boolean[][] visited;
    
    // 변경 후:
    // main 메서드 안에서 선언 및 초기화
    int[][] map = new int[SIZE][SIZE];
    boolean[][] visited = new boolean[SIZE][SIZE];
    ```

*   **`isExist` 전역 변수 제거 및 `dfs` 반환 타입 변경:**
    ```java
    // 변경 전:
    // static boolean isExist; // 전역 변수
    // static void dfs(int x, int y) { ... isExist = true; return; ... }
    // System.out.println("#"+tNum+" "+((isExist) ? 1 : 0));
    
    // 변경 후:
    // static boolean dfs(int x, int y, int[][] map, boolean[][] visited, int finishX, int finishY) {
    //     if (x == finishX && y == finishY) {
    //         return true; // 목적지 도착
    //     }
    //     // ... 탐색 로직 ...
    //     if (dfs(cx, cy, map, visited, finishX, finishY)) { // 재귀 호출 결과 전달
    //         return true;
    //     }
    //     return false; // 경로 없음
    // }
    // boolean pathExists = dfs(startX, startY, map, visited, finishX, finishY);
    // System.out.println("#"+testCaseId+" "+(pathExists ? 1 : 0));
    ```
    이렇게 변경하면 `dfs`가 더 독립적인 함수가 되어 재사용성이 높아집니다.

### 3. 가독성 개선

1.  **상수 사용:** `SIZE`와 같은 상수를 정의하여 `16`이라는 숫자의 의미를 명확히 합니다.
2.  **변수명 개선:** `tNum`은 `testCaseId`와 같이 좀 더 명확한 이름으로 변경할 수 있습니다. `dx`, `dy`도 `DX`, `DY`로 대문자로 변경하여 상수임을 나타냅니다.
3.  **코드 스코프 제한:** 전역 변수를 최소화하고, 필요한 변수들은 해당 스코프(예: `main` 메서드) 내에서 선언하여 코드의 응집도를 높이고 부작용을 줄입니다.
4.  **`else if` 사용:** 시작점(2)과 도착점(3)은 동시에 한 셀에 존재할 수 없으므로 `if (map[i][j] == 2)` 다음 `else if (map[i][j] == 3)`를 사용하여 불필요한 조건 검사를 줄일 수 있습니다.
5.  **`Scanner` 자원 해제:** `Scanner` 객체를 사용한 후에는 `sc.close()`를 호출하여 자원을 해제하는 것이 좋습니다.
6.  **주석 추가:** 주요 로직에 대한 설명을 추가하여 코드 이해도를 높일 수 있습니다.

### 4. 최종 최적화된 코드

```java
package com.ssafy.swea;

import java.util.Scanner;

public class SWEA_1226_Optimized {
    // 미로의 크기를 상수로 정의하여 가독성 및 유지보수성 향상
    static final int SIZE = 16; 
    
    // 상하좌우 이동을 위한 배열 (상수화)
    static final int[] DX = {-1, 0, 1, 0}; 
    static final int[] DY = {0, 1, 0, -1};
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // 총 10개의 테스트 케이스 처리
        for(int tc = 1; tc <= 10; tc++) {
            // 테스트 케이스 번호 (문제에서는 tNum으로 주어졌으나, 실제로 사용되는 것은 번호 출력뿐이므로 testCaseId로 변경)
            int testCaseId = sc.nextInt();
            
            // 미로 맵과 방문 여부를 저장할 배열을 main 메서드 내에서 초기화 (전역 변수 제거)
            int[][] map = new int[SIZE][SIZE];
            boolean[][] visited = new boolean[SIZE][SIZE];
            
            // 시작점(2)과 도착점(3)의 좌표
            int startX = 0, startY = 0;
            int finishX = 0, finishY = 0;
            
            // 미로 입력 및 시작/도착점 좌표 찾기
            for(int i = 0; i < SIZE; i++) {
                String line = sc.next(); // 한 줄씩 입력 받음
                for(int j = 0; j < SIZE; j++) {
                    map[i][j] = line.charAt(j) - '0'; // 문자를 숫자로 변환
                    if(map[i][j] == 2) { // 시작점인 경우
                        startX = i;
                        startY = j;
                    } else if(map[i][j] == 3) { // 도착점인 경우 (else if로 불필요한 검사 회피)
                        finishX = i;
                        finishY = j;
                    }
                }
            }
            
            // DFS 탐색 시작. DFS 함수가 직접 경로 존재 여부를 boolean으로 반환하도록 변경
            boolean pathExists = dfs(startX, startY, map, visited, finishX, finishY);
            
            // 결과 출력
            System.out.println("#" + testCaseId + " " + (pathExists ? 1 : 0));
        }
        
        sc.close(); // Scanner 자원 해제
    }
    
    /**
     * 미로에서 경로를 찾는 Depth-First Search (DFS) 메서드
     * @param x 현재 X 좌표
     * @param y 현재 Y 좌표
     * @param map 미로 맵
     * @param visited 방문 여부 배열
     * @param finishX 도착점 X 좌표
     * @param finishY 도착점 Y 좌표
     * @return 목적지(3)에 도달하면 true, 아니면 false 반환
     */
    static boolean dfs(int x, int y, int[][] map, boolean[][] visited, int finishX, int finishY) {
        // 현재 위치가 도착점인 경우, 경로를 찾았으므로 true 반환
        if (x == finishX && y == finishY) {
            return true;
        }
        
        // 이미 방문한 곳이거나, 벽(1)인 경우, 더 이상 진행하지 않음
        // (주의: finishX, finishY는 벽이 아니므로, 목적지 도달 여부 체크를 먼저 해야 함)
        if (visited[x][y] || map[x][y] == 1) {
            return false;
        }
        
        // 현재 위치를 방문 처리
        visited[x][y] = true;
        
        // 4가지 방향(상하좌우)으로 탐색
        for (int d = 0; d < 4; d++) {
            int nx = x + DX[d]; // 다음 X 좌표
            int ny = y + DY[d]; // 다음 Y 좌표
            
            // 미로 범위를 벗어나는지 확인
            if (nx < 0 || nx >= SIZE || ny < 0 || ny >= SIZE) {
                continue; // 범위 밖이면 다음 방향 탐색
            }
            
            // 다음 위치로 DFS 재귀 호출
            // 만약 다음 호출에서 목적지를 찾으면, 현재 경로도 유효하므로 true를 반환하며 탐색 종료
            if (dfs(nx, ny, map, visited, finishX, finishY)) {
                return true;
            }
        }
        
        // 4가지 방향 모두 탐색했지만 목적지를 찾지 못한 경우 false 반환
        return false;
    }
}
```

### 5. 왜 이 방법이 최선인지 설명

1.  **시간 복잡도:**
    *   DFS (또는 BFS)는 미로 탐색과 같은 그래프 탐색 문제에서 경로 존재 여부를 확인하는 가장 효율적인 알고리즘 중 하나입니다. 각 셀을 최대 한 번만 방문하므로, $O(V+E)$ 또는 $O(SIZE \times SIZE)$의 시간 복잡도를 가집니다. 이는 이론적으로 가능한 최적의 성능을 제공합니다. 이 문제의 제약 조건(16x16)에서는 이 정도의 시간 복잡도는 전혀 문제가 되지 않습니다.

2.  **공간 복잡도:**
    *   `map`과 `visited` 배열은 미로의 상태와 방문 여부를 저장하기 위해 필수적입니다. 이들을 제거하는 것은 문제의 정의를 바꾸는 것과 같습니다. 따라서 $O(SIZE \times SIZE)$의 공간은 최소한으로 요구되는 공간입니다.
    *   재귀 호출 스택은 최악의 경우 $O(SIZE \times SIZE)$이지만, 16x16 맵에서는 스택 오버플로우를 걱정할 필요가 없습니다. 만약 맵이 훨씬 크다면, 반복문과 `Stack` 자료구조를 사용하는 비재귀 DFS 또는 `Queue`를 사용하는 BFS로 전환하여 스택 오버플로우 위험을 제거하고 명시적인 메모리 관리를 할 수 있습니다. 하지만 이 경우에서는 현재 재귀 방식이 가장 간결하고 가독성이 좋습니다.
    *   전역 변수를 최소화하고 `dfs` 함수가 결과를 반환하도록 변경하여, 필요한 메모리 외에 추가적인 상태 저장 공간을 줄였습니다.

3.  **가독성 및 유지보수성:**
    *   **상수 사용 (`SIZE`, `DX`, `DY`):** "매직 넘버"를 제거하여 숫자의 의미를 명확히 하고, 미로 크기 변경 시 코드 수정이 용이해집니다.
    *   **전역 변수 최소화 및 함수 파라미터 전달:** `main` 메서드와 `dfs` 함수가 각자의 역할을 명확히 하고, `dfs` 함수가 `map`, `visited` 등의 외부 상태에 덜 의존하게 됩니다. 이는 함수형 프로그래밍 원칙에도 부합하며, 테스트 및 재사용이 용이해집니다. `isExist` 플래그를 제거하고 `boolean` 반환 값을 사용함으로써 함수의 목적이 더욱 명확해졌습니다.
    *   **`Scanner` 자원 해제:** `try-with-resources` 또는 명시적 `close()` 호출로 자원 누수를 방지합니다.
    *   **`else if` 활용:** 입력 처리 시 효율성을 조금 더 높였습니다.
    *   **주석:** 코드의 복잡한 부분이나 의도를 설명하여 다른 개발자가 코드를 이해하는 데 도움을 줍니다.

결론적으로, 이 최적화된 코드는 문제 해결을 위한 알고리즘적 효율성은 유지하면서, 코드의 구조, 가독성, 유지보수성을 크게 향상시켰습니다. 16x16이라는 작은 맵 크기의 문제 제약 조건을 고려할 때, 현재 DFS 구현이 가장 간결하고 효과적인 "최선의" 방법이라고 할 수 있습니다.