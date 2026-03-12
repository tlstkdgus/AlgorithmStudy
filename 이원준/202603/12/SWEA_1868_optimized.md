# 최적화 분석

SWEA 1868 (지뢰찾기) 문제에 대한 알고리즘 최적화 분석 및 코드입니다.

---

## 알고리즘 최적화 분석

제공된 코드는 지뢰찾기 게임의 클릭 횟수를 계산하는 로직을 구현하고 있습니다. 기본적인 BFS를 활용하여 인접 지뢰가 없는 셀(이하 '0-셀')을 탐색하고, 이후 나머지 지뢰 없는 셀을 처리하는 방식은 지뢰찾기 문제의 표준적인 접근 방식이며 이미 효율적인 편입니다.

하지만 몇 가지 측면에서 가독성 개선 및 사소한 최적화를 고려해볼 수 있습니다.

### 1. 시간 복잡도 개선 방법

*   **현재 시간 복잡도**: `T * O(N^2)`
    *   `mineCount` (-> `adjacentMineCount`) 계산: 모든 `N*N` 셀에 대해 8방 탐색을 수행하므로 `O(N^2 * 8)` -> `O(N^2)`.
    *   첫 번째 `for` 루프 (0-셀 BFS): 모든 셀을 한 번씩 방문하고 각 셀에서 최대 8번의 인접 셀 탐색을 수행하므로 전체 BFS 과정은 `O(N^2)`.
    *   두 번째 `for` 루프 (남은 셀 처리): 모든 `N*N` 셀을 한 번씩 탐색하므로 `O(N^2)`.
    *   총 시간 복잡도는 각 테스트 케이스마다 `O(N^2)`이 됩니다.

*   **개선 방향**:
    *   **명확한 비고정 개선은 어렵습니다.** 이 문제는 모든 셀을 적어도 한 번은 확인해야 하므로 `O(N^2)`보다 더 빠르게 해결하기는 어렵습니다. 주어진 코드는 이미 `O(N^2)`의 최적 시간 복잡도를 달성하고 있습니다.
    *   **상수 시간 개선**:
        *   BFS 내부에서 `visited` 배열을 확인하는 순서를 최적화하는 것은 이미 잘 되어 있습니다. (경계 조건 및 방문 여부 먼저 확인).
        *   `dr`, `dc` 배열을 `final`로 선언하여 컴파일러에게 변경되지 않음을 알리는 것은 미미한 상수 시간 최적화 가능성이 있지만, 실제 성능에 큰 영향을 주지는 않습니다.

### 2. 공간 복잡도 개선 방법

*   **현재 공간 복잡도**: `O(N^2)`
    *   `bomb` (-> `grid`): `char[N][N]` -> `O(N^2)`
    *   `mineCount` (-> `adjacentMineCount`): `int[N][N]` -> `O(N^2)`
    *   `visited`: `boolean[N][N]` -> `O(N^2)`
    *   `Queue`: 최악의 경우 (모든 셀이 지뢰가 아니고 0-셀로 연결된 경우) `O(N^2)` 크기까지 확장될 수 있습니다.

*   **개선 방향**:
    *   **명확한 비고정 개선은 어렵습니다.** 문제의 특성상 `N*N` 격자 정보를 저장해야 하므로 `O(N^2)` 이상의 공간 복잡도를 줄이기는 어렵습니다.
    *   `adjacentMineCount` 배열은 BFS 탐색 전에 모든 '.' 셀에 대해 계산되므로, 이 배열을 없애고 BFS 내부에서 필요할 때마다 `calculateAdjacentMines`를 호출하는 방식은 가능하지만, 이는 **시간 복잡도를 증가**시키는 (매번 8방 탐색을 다시 해야 하므로) 역효과를 낳습니다. 따라서 현재처럼 `adjacentMineCount`를 미리 계산하는 것이 시간 효율적입니다.
    *   `char` 타입인 `grid`와 `int` 타입인 `adjacentMineCount`, `boolean` 타입인 `visited`는 각각의 목적에 맞는 가장 효율적인 자료형입니다.

### 3. 가독성 개선

*   **변수명 변경**:
    *   `bomb` -> `grid`: 초기 입력이 지뢰 여부뿐만 아니라 빈 셀 정보도 포함하므로 `grid`가 더 포괄적인 의미를 가집니다.
    *   `mineCount` -> `adjacentMineCount`: 어떤 셀에 인접한 지뢰의 개수를 명확히 나타냅니다.
    *   `bfs_bomb` -> `exploreZeroCellArea` 또는 `bfsClick`: 해당 BFS의 목적(0-셀 영역 탐색 및 클릭)을 명확히 합니다.
    *   `curr`, `nextR`, `nextC` -> `current`, `currentRow`, `currentCol`, `neighborR`, `neighborC` 등: 의미를 더 명확하게 전달합니다.
    *   `points` -> `queue`: 큐 자료구조임을 더 직관적으로 알 수 있습니다.
*   **주석 추가**: 각 단계(인접 지뢰 개수 계산, 0-셀 BFS, 나머지 셀 클릭)와 핵심 함수에 대한 주석을 추가하여 코드의 흐름과 목적을 쉽게 이해할 수 있도록 합니다.
*   **자원 관리**: `Scanner` 사용 후 `sc.close()`를 호출하여 자원을 해제합니다.
*   **Static 변수 사용 범위**: `T` 변수는 각 테스트 케이스마다 초기화되어야 하고 `main` 메서드 안에서만 사용되므로, `static`으로 선언하기보다는 `main` 메서드 내 지역 변수로 두는 것이 좋습니다. `N`은 여러 static 메서드에서 사용되므로 `static`으로 두는 것이 현재 구조에서는 합리적입니다.
*   **`final` 키워드**: `dr`, `dc`와 같이 한 번 초기화된 후 변경되지 않는 배열에 `final`을 붙여 코드의 의도를 명확히 합니다.

---

## 최종 최적화된 코드

```java
package com.ssafy.swea;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SWEA_1868_Optimized { // 클래스 이름을 최적화를 반영하여 변경
    static int N; // 그리드 크기 (static으로 유지, 여러 메서드에서 사용)
    static char[][] grid; // 'bomb'을 'grid'로 변경하여 더 포괄적인 의미 부여
    static int[][] adjacentMineCount; // 'mineCount'를 'adjacentMineCount'로 변경
    static boolean[][] visited; // 방문 여부
    static int clickCount; // 클릭 횟수

    // 8방향 탐색을 위한 상대 좌표 (상, 하, 좌, 우, 좌상, 우상, 좌하, 우하)
    static final int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1}; // final 키워드 추가
    static final int[] dc = {0, 0, -1, 1, -1, 1, -1, 1}; // final 키워드 추가

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt(); // 테스트 케이스 개수는 main 메서드 내 지역 변수로 선언

        for (int tc = 1; tc <= T; tc++) {
            N = sc.nextInt();
            grid = new char[N][N];
            for (int i = 0; i < N; i++) {
                String line = sc.next();
                for (int j = 0; j < N; j++) {
                    grid[i][j] = line.charAt(j);
                }
            }

            // 각 테스트 케이스마다 배열 초기화
            adjacentMineCount = new int[N][N];
            visited = new boolean[N][N]; // boolean 배열은 기본적으로 false로 초기화됨
            clickCount = 0;

            // Phase 1: 모든 '.' 셀에 대해 인접한 지뢰의 개수를 미리 계산합니다.
            // 이 정보는 BFS에서 '0-셀'을 효과적으로 탐색하고 확산하는 데 사용됩니다.
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    if (grid[r][c] == '.') {
                        adjacentMineCount[r][c] = calculateAdjacentMines(r, c);
                    }
                }
            }

            // Phase 2: 첫 번째 탐색 - '0-셀' (인접 지뢰가 없는 셀)을 클릭합니다.
            // '0-셀'을 클릭하면 BFS를 통해 연결된 모든 '0-셀'과 그 인접 셀들이 자동으로 열립니다.
            // 각 0-셀 영역은 단 한 번의 클릭으로 처리됩니다.
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    // 아직 방문하지 않은 '.' 셀이면서 인접 지뢰가 0개인 경우
                    if (grid[r][c] == '.' && !visited[r][c] && adjacentMineCount[r][c] == 0) {
                        clickCount++; // 새로운 '0-셀' 영역 시작이므로 클릭 횟수 증가
                        exploreZeroCellArea(r, c); // BFS를 시작하여 해당 영역을 탐색
                    }
                }
            }

            // Phase 3: 두 번째 탐색 - 아직 방문하지 않은 나머지 '.' 셀을 클릭합니다.
            // 이 셀들은 인접 지뢰가 있어(adjacentMineCount > 0) Phase 2의 BFS에 의해 자동으로 열리지 않은 셀들입니다.
            // 각 셀은 개별적인 클릭이 필요합니다.
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    // 아직 방문하지 않은 '.' 셀인 경우
                    if (grid[r][c] == '.' && !visited[r][c]) {
                        clickCount++; // 개별 클릭 횟수 증가
                        visited[r][c] = true; // 방문 처리
                    }
                }
            }

            System.out.println("#" + tc + " " + clickCount);
        }
        sc.close(); // Scanner 자원 해제
    }

    /**
     * 특정 셀 (r, c)의 주변 8방향에 있는 지뢰의 개수를 계산합니다.
     * 이 함수는 grid[r][c]가 '.' 인 경우에만 호출됩니다.
     *
     * @param r 행 인덱스
     * @param c 열 인덱스
     * @return 인접한 지뢰의 개수
     */
    static int calculateAdjacentMines(int r, int c) {
        int count = 0;
        for (int d = 0; d < 8; d++) {
            int nr = r + dr[d];
            int nc = c + dc[d];

            // 경계 조건 확인 및 인접 셀이 지뢰('*')인지 확인
            if (nr >= 0 && nr < N && nc >= 0 && nc < N && grid[nr][nc] == '*') {
                count++;
            }
        }
        return count;
    }

    /**
     * '0-셀' (인접 지뢰가 없는 셀)에서 시작하여 BFS를 수행하여 연결된 모든 '0-셀' 영역을 탐색하고 방문 처리합니다.
     * '0-셀'이 열리면 인접한 모든 셀들도 함께 열리는 지뢰찾기 규칙을 모방합니다.
     * 인접 셀 중 '0-셀'이 있다면, 그 셀도 큐에 추가되어 추가 탐색을 이어갑니다.
     *
     * @param startR 시작 행
     * @param startC 시작 열
     */
    static void exploreZeroCellArea(int startR, int startC) {
        Queue<int[]> queue = new LinkedList<>(); // 'points'를 'queue'로 변경
        queue.offer(new int[]{startR, startC});
        visited[startR][startC] = true; // 시작 셀 방문 처리

        while (!queue.isEmpty()) {
            int[] current = queue.poll(); // 'curr'를 'current'로 변경
            int currentRow = current[0];
            int currentCol = current[1];

            // 8방향 인접 셀 탐색
            for (int d = 0; d < 8; d++) {
                int neighborR = currentRow + dr[d]; // 'nextR'을 'neighborR'로 변경
                int neighborC = currentCol + dc[d]; // 'nextC'를 'neighborC'로 변경

                // 경계 조건 및 이미 방문한 셀인 경우 스킵
                if (neighborR < 0 || neighborR >= N || neighborC < 0 || neighborC >= N || visited[neighborR][neighborC]) {
                    continue;
                }

                // 인접 셀이 '.'인 경우에만 처리
                if (grid[neighborR][neighborC] == '.') {
                    visited[neighborR][neighborC] = true; // 방문 처리
                    // 인접 셀이 '0-셀'이라면 큐에 추가하여 탐색을 확장
                    if (adjacentMineCount[neighborR][neighborC] == 0) {
                        queue.offer(new int[]{neighborR, neighborC});
                    }
                }
            }
        }
    }
}
```

---

## 왜 이 방법이 최선인지 설명

1.  **시간 복잡도 `O(N^2)` 유지**:
    *   이 문제는 `N*N` 크기의 격자를 모두 확인해야 하므로, 어떤 알고리즘이든 최소 `O(N^2)`의 시간이 필요합니다.
    *   제시된 최적화 코드는 인접 지뢰 개수 계산(`O(N^2)`), 0-셀 영역 BFS 탐색(`O(N^2)`, 각 셀과 간선이 최대 한 번 처리됨), 나머지 셀 처리(`O(N^2)`)의 세 단계로 이루어져 있어 총 `O(N^2)`의 시간을 소비합니다. 이는 문제 해결에 필요한 최적의 시간 복잡도입니다.
    *   `adjacentMineCount` 배열을 미리 계산하여 BFS 시 `calculateAdjacentMines`를 반복 호출하는 오버헤드를 줄인 것은 상수 시간 관점에서 효율적인 선택입니다.

2.  **공간 복잡도 `O(N^2)` 유지**:
    *   문제의 특성상 `N*N` 크기의 그리드(`grid`), 인접 지뢰 개수(`adjacentMineCount`), 방문 여부(`visited`)를 저장해야 합니다. 이들은 모두 `O(N^2)`의 공간을 요구합니다.
    *   BFS에 사용되는 큐 또한 최악의 경우(`N*N`개의 셀이 모두 연결된 0-셀 영역일 때) `O(N^2)`개의 요소를 저장할 수 있습니다.
    *   이러한 공간 사용은 그리드 기반 문제에서 일반적이며, 정보 손실 없이 공간 복잡도를 줄이는 것은 거의 불가능합니다. 따라서 현재 `O(N^2)`의 공간 복잡도는 최적입니다.

3.  **가독성 및 유지보수성 향상**:
    *   **명확한 변수명**: `bomb` -> `grid`, `mineCount` -> `adjacentMineCount`, `bfs_bomb` -> `exploreZeroCellArea` 등으로 변경하여 각 변수와 함수의 역할을 직관적으로 이해할 수 있도록 했습니다. 이는 코드 이해도를 높여 디버깅 및 향후 유지보수를 용이하게 합니다.
    *   **상세한 주석**: 각 로직의 주요 단계(Phase 1, 2, 3)와 핵심 함수에 주석을 추가하여 코드의 흐름과 구현 의도를 명확히 설명합니다.
    *   **자원 관리**: `Scanner` 객체 사용 후 `sc.close()`를 호출하여 자원을 적절하게 해제하고 잠재적인 리소스 누수를 방지합니다.
    *   **`final` 키워드**: `dr`, `dc` 배열에 `final`을 선언하여 이 배열들이 변경되지 않는 상수임을 명시함으로써 코드의 안전성을 높이고 가독성을 개선합니다.

**결론적으로, 이 최적화는 주어진 문제의 핵심 로직을 변경하지 않으면서도 시간 및 공간 복잡도의 최적 성능을 유지하고, 동시에 코드의 가독성과 유지보수성을 크게 향상시켰습니다.** 경쟁 프로그래밍 환경에서 이 정도 수준의 구현은 일반적으로 최고의 성능을 발휘합니다.