# 최적화 분석

주어진 `SWEA_7733` 클래스는 비어있습니다. 이 문제는 일반적으로 SW Expert Academy의 "7733. 치즈 도둑" 문제 (Cheese Thief)를 의미합니다. 해당 문제의 맥락을 기반으로 최적화를 진행하겠습니다.

---

### **문제 분석 (SWEA 7733: 치즈 도둑)**

**문제 요약:**
N x N 크기의 치즈 덩어리가 주어집니다. 각 칸에는 치즈의 '맛있는 정도'가 숫자로 표시되어 있습니다. 매일 치즈가 녹는데, '오늘의 맛없는 정도' D 이하의 맛있는 정도를 가진 치즈는 모두 녹아 없어집니다. 우리는 "가장 많은 수의 덩어리"가 남아있는 날의 덩어리 개수를 찾아야 합니다. (덩어리는 상하좌우로 인접한 치즈 칸들의 연결된 집합입니다.)

**제약 조건:**
*   N은 2 이상 100 이하
*   치즈의 맛있는 정도는 1 이상 100 이하
*   (Implicitly) 오늘의 맛없는 정도 D는 0부터 100까지 고려할 수 있습니다. (0은 아무것도 녹지 않음, 100은 맛있는 정도 100 이하가 다 녹음)

---

### **기존 알고리즘 (추정) 및 최적화 방향**

비어있는 클래스이므로, 일반적인 문제 해결 방식을 기반으로 최적화를 설명합니다.

**기존 접근 방식 (Baseline):**
1.  **날짜(D) 반복:** 가능한 모든 '오늘의 맛없는 정도' D에 대해 반복합니다. (보통 D는 0부터 최대 맛있는 정도까지)
2.  **그리드 생성:** 각 D에 대해, 원본 치즈 그리드에서 `맛있는 정도 > D`인 칸만 남기고 새로운 그리드를 (논리적으로 또는 실제 배열로) 생성합니다.
3.  **컴포넌트 개수 세기:** 생성된 그리드에서 연결된 치즈 덩어리(Connected Components)의 개수를 BFS (Breadth-First Search) 또는 DFS (Depth-First Search)를 사용하여 계산합니다.
4.  **최대값 갱신:** 계산된 덩어리 개수가 현재까지의 최대 덩어리 개수보다 크면 갱신합니다.

**최적화 방향:**

#### 1. 시간 복잡도 개선 방법

*   **현재 분석:**
    *   D의 반복: `MaxTasteLevel` (최대 100)
    *   각 D마다 그리드 순회 및 BFS/DFS: `N * N` (그리드 크기)
    *   BFS/DFS 자체는 각 칸과 간선을 한 번씩만 방문하므로 `O(N*N)`
    *   총 시간 복잡도: `O(MaxTasteLevel * N*N)`
    *   N=100, MaxTasteLevel=100일 때, `100 * 100 * 100 = 1,000,000` 연산. 이는 1초 내에 충분히 해결 가능한 범위입니다.
*   **개선 방법:**
    *   이 문제의 제약 조건 하에서는 `O(MaxTasteLevel * N*N)`보다 더 효율적인 알고리즘(예: N^2 -> N log N)을 찾는 것은 매우 어렵거나 불필요합니다. 각 D마다 치즈 상태가 완전히 달라지므로, 매번 BFS/DFS를 수행하는 것이 합리적입니다.
    *   **실질적인 개선은 코드 구현의 효율성에서 나옵니다.**
        *   **빠른 I/O:** `BufferedReader`와 `StringTokenizer`를 사용하여 입력 처리 시간을 줄입니다.
        *   **불필요한 D 반복 줄이기:** D는 실제 치즈 값 중 최대값까지만 반복해도 충분합니다. 예를 들어, 그리드 내 최대 맛있는 정도가 50이라면 D=51 이후는 D=50과 결과가 동일할 것이므로, 0부터 `MaxTasteValueInGrid`까지만 반복해도 됩니다. (물론 문제 제약이 100이므로 0~100으로 해도 무방하며, 코드 단순성을 위해 보통 100으로 합니다.)
        *   **BFS/DFS의 효율적인 구현:** `visited` 배열을 사용하여 중복 방문을 막고, 큐/스택 관리를 효율적으로 합니다.

#### 2. 공간 복잡도 개선 방법

*   **현재 분석:**
    *   원본 치즈 그리드: `O(N*N)`
    *   `visited` 배열 (BFS/DFS용): `O(N*N)`
    *   BFS 큐 / DFS 스택: 최악의 경우 `O(N*N)`
    *   총 공간 복잡도: `O(N*N)`
    *   N=100일 때, `100 * 100 = 10,000` 칸. 정수 배열로 약 40KB, boolean 배열로 약 10KB. 이것 또한 충분히 허용 가능한 범위입니다.
*   **개선 방법:**
    *   **공간 복잡도 역시 `O(N*N)`이 최적입니다.** 원본 그리드와 방문 여부를 저장하기 위해 `N*N` 크기의 배열은 필수적입니다.
    *   `visited` 배열은 각 D마다 새로 생성하기보다 `boolean[][]`으로 선언하고 `Arrays.fill` 등으로 초기화하는 것이 미세하게 빠를 수 있지만, 실제로는 새 배열을 할당하는 비용도 미미합니다. 중요한 것은 재사용성을 명확히 하는 것입니다.

#### 3. 가독성 개선

*   **변수명 및 함수명:** 의미 있는 이름을 사용합니다 (예: `meltLevel` 대신 `currentDay`, `blocksCount` 대신 `currentBlocks`).
*   **구조화:** BFS/DFS 로직을 별도의 헬퍼 메서드로 분리하여 `main` 메서드의 복잡도를 줄입니다.
*   **상수 사용:** 방향 배열 (`dr`, `dc`) 등을 static final로 선언하여 코드의 의도를 명확히 합니다.
*   **Point 클래스 사용:** BFS 큐에 `int[]` 대신 `Point` 객체를 사용하여 `r`과 `c`가 무엇을 의미하는지 명확히 합니다.
*   **주석:** 복잡하거나 중요한 로직에 주석을 달아 이해를 돕습니다.

---

### **최종 최적화된 코드**

아래 코드는 "치즈 도둑" 문제에 대한 표준적이고 효율적인 해결책입니다.

```java
package com.ssafy.swea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class SWEA_7733 {

    static int N; // 그리드의 크기
    static int[][] cheeseGrid; // 원본 치즈 그리드
    static boolean[][] visited; // BFS/DFS 방문 여부를 기록할 배열
    
    // 상하좌우 4방향 이동을 위한 배열
    static int[] dr = {-1, 1, 0, 0}; // 행 (Row) 변화량
    static int[] dc = {0, 0, -1, 1}; // 열 (Column) 변화량

    static int maxCheeseValue; // 치즈 맛있는 정도의 최대값 (D 반복 횟수를 최적화하기 위함)

    // BFS 큐에 저장할 좌표를 위한 클래스
    static class Point {
        int r, c;
        Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine()); // 테스트 케이스 개수

        for (int testCase = 1; testCase <= T; testCase++) {
            N = Integer.parseInt(br.readLine());
            cheeseGrid = new int[N][N];
            maxCheeseValue = 0; // 그리드 내 최대 맛있는 정도를 찾기 위해 초기화

            // 그리드 입력 및 최대 맛있는 정도 찾기
            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    cheeseGrid[i][j] = Integer.parseInt(st.nextToken());
                    if (cheeseGrid[i][j] > maxCheeseValue) {
                        maxCheeseValue = cheeseGrid[i][j];
                    }
                }
            }

            int maxBlocks = 1; // 최소 덩어리 개수는 1 (아무것도 녹지 않는 날(D=0)에는 적어도 1개 존재)

            // '오늘의 맛없는 정도(meltLevel)'를 0부터 그리드 내 최대 맛있는 정도까지 반복
            // meltLevel = 0: 아무 치즈도 녹지 않음 (맛있는 정도 > 0인 모든 치즈가 남음)
            // meltLevel = maxCheeseValue: 맛있는 정도 <= maxCheeseValue인 모든 치즈가 녹음
            // 그 이상의 meltLevel은 maxCheeseValue와 결과가 동일하므로 반복할 필요 없음
            for (int meltLevel = 0; meltLevel <= maxCheeseValue; meltLevel++) {
                visited = new boolean[N][N]; // 매일 새로운 방문 배열 초기화
                int currentBlocks = 0; // 현재 meltLevel에서의 덩어리 개수

                // 모든 칸을 순회하며 덩어리 탐색
                for (int r = 0; r < N; r++) {
                    for (int c = 0; c < N; c++) {
                        // 만약 현재 칸에 치즈가 남아있고 (맛있는 정도 > meltLevel)
                        // 아직 방문하지 않았다면 새로운 덩어리 발견
                        if (cheeseGrid[r][c] > meltLevel && !visited[r][c]) {
                            bfs(r, c, meltLevel); // BFS로 연결된 모든 치즈 칸 방문 처리
                            currentBlocks++; // 덩어리 개수 증가
                        }
                    }
                }
                // 최대 덩어리 개수 갱신
                if (currentBlocks > maxBlocks) {
                    maxBlocks = currentBlocks;
                }
            }

            System.out.println("#" + testCase + " " + maxBlocks);
        }
    }

    /**
     * BFS를 사용하여 주어진 위치에서 시작하는 치즈 덩어리를 탐색하고 방문 처리합니다.
     *
     * @param startR 시작 행
     * @param startC 시작 열
     * @param meltLevel 현재 녹는 수준 (이 값 이하의 치즈는 녹음)
     */
    public static void bfs(int startR, int startC, int meltLevel) {
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startR, startC));
        visited[startR][startC] = true; // 시작 지점 방문 처리

        while (!queue.isEmpty()) {
            Point current = queue.poll();

            // 상하좌우 4방향 탐색
            for (int i = 0; i < 4; i++) {
                int nextR = current.r + dr[i];
                int nextC = current.c + dc[i];

                // 1. 그리드 범위 체크
                // 2. 현재 칸에 치즈가 남아있는지 확인 (맛있는 정도 > meltLevel)
                // 3. 아직 방문하지 않은 칸인지 확인
                if (nextR >= 0 && nextR < N && nextC >= 0 && nextC < N &&
                    cheeseGrid[nextR][nextC] > meltLevel && !visited[nextR][nextC]) {
                    
                    visited[nextR][nextC] = true; // 방문 처리
                    queue.add(new Point(nextR, nextC)); // 큐에 추가하여 탐색 계속
                }
            }
        }
    }
}

```

---

### **왜 이 방법이 최선인지 설명**

1.  **알고리즘적 효율성 (시간 복잡도):**
    *   이 문제의 핵심은 '오늘의 맛없는 정도' D에 따라 치즈 상태가 변하고, 각 상태에서 독립적으로 덩어리 개수를 세어야 한다는 점입니다.
    *   최대 `MaxTasteLevel`번 (여기서는 `maxCheeseValue`까지) '오늘의 맛없는 정도'를 변경하며 시뮬레이션합니다.
    *   각 시뮬레이션 단계에서는 `N x N` 그리드의 모든 칸을 한 번씩 순회하며, 아직 방문하지 않은 치즈 덩어리를 발견하면 BFS/DFS를 시작합니다. BFS/DFS는 연결된 모든 칸을 효율적으로 방문하므로, 한 번의 BFS/DFS는 `O(N*N)` 시간을 소요합니다.
    *   따라서 총 시간 복잡도는 `O(MaxTasteLevel * N*N)`이며, 주어진 제약 조건 (N=100, MaxTasteLevel=100) 하에서는 약 100만 번의 연산으로 매우 효율적입니다. 이보다 더 빠르게 모든 D에 대한 최대 덩어리 개수를 찾는 일반적인 방법은 존재하지 않습니다.
    *   `maxCheeseValue`를 사용하여 불필요한 D 반복을 줄이는 것은 미세한 최적화이지만, 실질적인 실행 시간 감소에 기여합니다.

2.  **공간 효율성 (공간 복잡도):**
    *   원본 치즈 그리드 `cheeseGrid` (`N*N`)와 방문 여부를 기록하는 `visited` 배열 (`N*N`)은 문제 해결에 필수적인 정보이므로 `O(N*N)`의 공간은 최적입니다.
    *   BFS에 사용되는 `Queue`도 최악의 경우 그리드의 모든 칸을 담을 수 있으므로 `O(N*N)`의 공간이 필요합니다.
    *   이 역시 주어진 제약 조건(N=100) 하에서 약 10KB ~ 40KB 정도로 메모리 제한을 충분히 만족합니다.

3.  **가독성 및 유지보수성:**
    *   **명확한 변수명 및 함수명:** `cheeseGrid`, `meltLevel`, `maxBlocks`, `bfs` 등 변수와 함수 이름이 그 목적을 명확히 설명합니다.
    *   **모듈화:** BFS 로직이 `bfs`라는 별도의 헬퍼 메서드로 분리되어 `main` 메서드의 핵심 로직을 간결하게 유지하고 재사용성을 높였습니다.
    *   **주석:** 중요한 로직이나 초기화 부분에 주석을 추가하여 코드 이해도를 높였습니다.
    *   **구조화된 데이터:** `Point` 클래스를 사용하여 큐에 좌표를 저장함으로써 `int[]`를 사용하는 것보다 데이터를 더 의미 있고 타입 안전하게 다룰 수 있습니다.
    *   **상수 사용:** `dr`, `dc` 배열을 전역 정적 변수로 선언하여 방향 이동 로직을 간결하고 재사용 가능하게 만들었습니다.

이러한 이유들로 인해 제시된 코드는 "SWEA 7733 치즈 도둑" 문제에 대한 가장 효율적이고 합리적인 최적화된 해결책이라고 할 수 있습니다.