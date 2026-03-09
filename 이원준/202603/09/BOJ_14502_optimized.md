# 최적화 분석

주어진 코드는 백트래킹(조합)과 BFS를 사용하여 연구실 안전 영역의 최대 크기를 찾는 전형적인 문제 해결 방식입니다. N, M의 최대 크기가 8x8로 작은 편이므로 현재 로직도 시간 제한 내에 통과할 수 있지만, 더 효율적이고 가독성이 좋은 코드로 개선할 수 있습니다.

---

### 1. 시간 복잡도 개선 방법

**핵심 아이디어:** BFS 시 빈 칸을 세는 과정을 최적화하고, 불필요한 탐색을 줄입니다.

1.  **초기 빈 칸 및 바이러스 위치 사전 계산:**
    *   `originalMap`을 입력받을 때, 전체 빈 칸의 개수(`initialEmptyCellsCount`)와 초기 바이러스 위치(`initialVirusPositions`)를 미리 저장합니다.
    *   이렇게 하면 `goVirus()` (변경된 `simulateVirusSpread()`) 함수 내에서 매번 맵을 순회하며 바이러스를 찾거나 빈 칸을 세는 작업을 줄일 수 있습니다.

2.  **BFS 중 빈 칸 개수 실시간 업데이트:**
    *   `goVirus()` 함수 내에서, `copyMap` 생성 후 `currentBlank`를 `initialEmptyCellsCount - 3` (3개의 새 벽으로 인해 줄어든 빈 칸)으로 초기화합니다.
    *   BFS를 진행하면서 바이러스가 새로운 빈 칸(`0`)을 감염시킬 때마다(`copyMap[nr][nc] = 2`), `currentBlank` 값을 1씩 감소시킵니다.
    *   이렇게 하면 BFS 종료 후 `N*M` 맵 전체를 다시 순회하며 빈 칸을 세는 작업(O(N*M))을 생략할 수 있습니다.

3.  **가지치기 (Pruning):**
    *   BFS를 시작하기 전에, 이미 현재까지 발견된 `maxPlace` (최대 안전 영역)보다 이번 시나리오에서 *최대* 나올 수 있는 안전 영역이 더 작다면 더 이상 BFS를 진행하지 않고 즉시 반환할 수 있습니다.
    *   `simulateVirusSpread()` 시작 시: `if (initialEmptyCellsCount - 3 <= maxSafeCells) return;` (단, 이 가지치기는 BFS가 이미 확산되기 전의 최대치를 고려하는 것이므로, 확산 후의 빈 칸이 더 적을 가능성을 배제하지 못해 효과가 제한적일 수 있습니다. 단순 빈 칸 감소 로직만으로도 충분합니다.)

**구체적인 코드 변경 예시:**

```java
// 기존:
// int currentBlank = 0;
// ... (BFS 끝난 후)
// for(int i = 0; i < N; i++) {
//     for(int j = 0; j < M; j++) {
//         if(copyMap[i][j] == 0) {
//             currentBlank++;
//         }
//     }
// }

// 개선:
static int initialEmptyCellsCount; // 전역 변수 추가
static List<Point> initialVirusPositions; // 전역 변수 추가

// main() 함수에서 맵 초기화 시:
for(int i = 0; i < N; i++) {
    for(int j = 0; j < M; j++) {
        originalMap[i][j] = sc.nextInt();
        if(originalMap[i][j] == EMPTY) { // EMPTY는 상수화 예정
            blankList.add(new Point(i,j)); // Point는 Record/Class로 변경 예정
            initialEmptyCellsCount++; // 초기 빈 칸 개수 카운트
        } else if (originalMap[i][j] == VIRUS) { // VIRUS는 상수화 예정
            initialVirusPositions.add(new Point(i,j)); // 초기 바이러스 위치 저장
        }
    }
}

// simulateVirusSpread() 함수 내에서:
int currentSafeCells = initialEmptyCellsCount - 3; // 3개의 벽으로 줄어든 빈 칸
// ...
// 큐에 초기 바이러스 추가 부분:
// 기존: 맵 전체를 순회하며 2를 찾아 추가
// 개선:
for (Point p : initialVirusPositions) {
    queue.add(p);
}
// ...
// BFS 중 0 -> 2 로 바뀔 때:
if(copyMap[nr][nc] == EMPTY) {
    copyMap[nr][nc] = VIRUS;
    queue.add(new Point(nr, nc));
    currentSafeCells--; // 빈 칸 개수 감소
}
// ...
maxSafeCells = Math.max(currentSafeCells, maxSafeCells); // 마지막 순회 없이 바로 업데이트
```

### 2. 공간 복잡도 개선 방법

**핵심 아이디어:** 불필요한 객체 생성 및 복사를 최소화하고, 자료형을 효율적으로 사용합니다.

1.  **`int[]` 대신 `Point` Record/Class 사용:**
    *   현재 `blankList`와 `newWallArr` 등에서 `int[]`를 좌표로 사용하고 있습니다. `int[]`는 `(r, c)`를 명시적으로 나타내지 않아 가독성이 떨어지고, 매번 새로운 배열 객체를 생성해야 할 수 있습니다.
    *   Java 16부터 도입된 `record Point(int r, int c) {}`를 사용하면 불변 객체로 좌표를 표현할 수 있어, 객체 생성 비용이 약간 증가할 수 있지만 코드를 훨씬 명확하게 만들고 필드 접근을 `p.r()` `p.c()`로 할 수 있습니다. (Java 8 등 이전 버전에서는 간단한 `Point` 클래스를 직접 정의해야 합니다.)
    *   공간 복잡도 자체에는 큰 변화가 없지만, 가독성 측면에서 큰 개선이 있습니다.

2.  **맵 복사는 필수:**
    *   `goVirus()` (변경된 `simulateVirusSpread()`) 함수 내에서 `copyMap`을 생성하는 것은 필수적입니다. `originalMap`은 다음 조합을 위해 원본 상태를 유지해야 하기 때문입니다. `N, M`이 최대 8이므로 `N*M` 배열 복사는 64개 요소를 복사하는 것에 불과하여 공간 및 시간 비용이 매우 낮습니다.

**구체적인 코드 변경 예시:**

```java
// 기존:
// List<int[]> blankList;
// int[][] newWallArr;
// new int[] {i,j}

// 개선:
record Point(int r, int c) {} // Point 레코드 또는 클래스 정의

static List<Point> blankList;
static Point[] newWallPositions; // 변수명 변경

// main() 함수에서:
blankList.add(new Point(i,j)); // 좌표 저장
// newWallArr = new int[3][2];
newWallPositions = new Point[3]; // Point 타입으로 변경

// comb() 함수 내에서:
// newWallArr[depth] = blankList.get(i);
newWallPositions[depth] = blankList.get(i);

// goVirus() 함수 내에서:
// for(int[] point : newWallArr) { copyMap[point[0]][point[1]] = 1; }
for(Point p : newWallPositions) { copyMap[p.r()][p.c()] = WALL; } // p.r(), p.c() 사용
// ...
// int[] curr = queue.poll(); int r = curr[0]; int c = curr[1];
Point curr = queue.poll(); int r = curr.r(); int c = curr.c();
```

### 3. 가독성 개선

**핵심 아이디어:** 상수 사용, 변수명 명확화, 함수명 명확화, 일관된 코딩 스타일 유지.

1.  **상수(Constants) 사용:**
    *   맵의 상태(`0`, `1`, `2`)를 나타내는 숫자 대신 의미 있는 상수를 사용합니다.
    *   `EMPTY`, `WALL`, `VIRUS`와 같이 상수를 정의하면 코드를 읽을 때 각 숫자가 무엇을 의미하는지 즉시 파악할 수 있습니다.

2.  **변수명 및 함수명 명확화:**
    *   `maxPlace` -> `maxSafeCells` (최대 안전 영역을 더 명확하게 표현).
    *   `newWallArr` -> `newWallPositions` (새로 세울 벽의 위치를 더 명확하게 표현).
    *   `comb` -> `combineWalls` (벽을 조합하는 행위를 더 명확하게 표현).
    *   `goVirus` -> `simulateVirusSpread` (바이러스 확산 시뮬레이션을 더 명확하게 표현).

3.  **`Scanner` 자원 관리:**
    *   `Scanner` 객체를 사용한 후에는 `.close()` 메서드를 호출하여 자원을 해제하는 것이 좋습니다.

4.  **클래스 멤버 접근 제어자:**
    *   모든 필드와 메서드가 `static`으로 선언되어 있지만, `public` 대신 `private` 또는 `protected`와 같은 적절한 접근 제어자를 사용하는 것이 일반적인 객체 지향 프로그래밍 관례입니다. 이 문제에서는 `static` 유틸리티 클래스처럼 사용되므로 큰 문제는 아니지만, 더 큰 프로젝트에서는 중요합니다. (여기서는 `private static`을 기본으로 사용합니다.)

**구체적인 코드 변경 예시:**

```java
// 상수 정의
private static final int EMPTY = 0;
private static final int WALL = 1;
private static final int VIRUS = 2;

// 변수명 변경
// static int maxPlace; -> private static int maxSafeCells;
// static int[][] newWallArr; -> private static Point[] newWallPositions;

// 함수명 변경
// static void comb(int depth, int start) -> private static void combineWalls(int depth, int start)
// static void goVirus() -> private static void simulateVirusSpread()

// Scanner 닫기
sc.close();
```

---

### 4. 최종 최적화된 코드

```java
package backtracking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BOJ_14502_Optimized {

    // --- 상수 정의 (가독성 개선) ---
    private static final int EMPTY = 0; // 빈 칸
    private static final int WALL = 1;  // 벽
    private static final int VIRUS = 2; // 바이러스

    // --- 전역 변수 선언 ---
    private static int N, M;
    private static int[][] originalMap; // 원본 맵
    private static List<Point> blankList; // 빈 칸의 좌표 목록
    private static List<Point> initialVirusPositions; // 초기 바이러스 위치 목록 (시간 복잡도 개선)
    private static Point[] newWallPositions; // 새로 세울 3개의 벽의 좌표 (변수명 변경, Point 사용)
    private static int maxSafeCells; // 최대 안전 영역 크기 (변수명 변경)
    private static int initialEmptyCellsCount; // 초기 빈 칸의 총 개수 (시간 복잡도 개선)

    // --- 상하좌우 이동을 위한 배열 ---
    private static int[] dr = {-1, 1, 0, 0};
    private static int[] dc = {0, 0, -1, 1};

    // --- 좌표를 표현하는 record (Java 16+ / Class 사용 가능) (공간/가독성 개선) ---
    record Point(int r, int c) {}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        N = sc.nextInt();
        M = sc.nextInt();

        maxSafeCells = 0; // 초기값은 0 (모두 감염될 경우 0이 될 수 있음)
        blankList = new ArrayList<>();
        initialVirusPositions = new ArrayList<>();
        initialEmptyCellsCount = 0; // 초기 빈 칸 개수 초기화

        originalMap = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                originalMap[i][j] = sc.nextInt();
                if (originalMap[i][j] == EMPTY) {
                    blankList.add(new Point(i, j));
                    initialEmptyCellsCount++; // 빈 칸 개수 카운트
                } else if (originalMap[i][j] == VIRUS) {
                    initialVirusPositions.add(new Point(i, j)); // 초기 바이러스 위치 저장
                }
            }
        }

        newWallPositions = new Point[3]; // 3개의 벽을 저장할 배열 초기화

        // 3개의 벽을 세우는 모든 조합 탐색
        combineWalls(0, 0);

        System.out.println(maxSafeCells);

        sc.close(); // Scanner 자원 해제 (가독성 개선)
    }

    /**
     * N개의 빈 칸 중 3개의 벽을 세울 위치를 조합으로 선택합니다.
     * @param depth 현재까지 선택한 벽의 개수
     * @param start 다음 벽을 선택할 시작 인덱스
     */
    private static void combineWalls(int depth, int start) {
        if (depth == 3) { // 3개의 벽을 모두 선택했으면
            simulateVirusSpread(); // 바이러스 확산 시뮬레이션 시작
            return;
        }

        for (int i = start; i < blankList.size(); i++) {
            newWallPositions[depth] = blankList.get(i); // 현재 벽 위치 선택
            combineWalls(depth + 1, i + 1); // 다음 벽 선택 (중복 방지를 위해 i+1부터 시작)
        }
    }

    /**
     * 선택된 3개의 벽을 세운 후 바이러스 확산을 시뮬레이션하고 안전 영역을 계산합니다.
     * (시간 복잡도 개선: 빈 칸 실시간 카운팅, 초기 바이러스 사전 주입)
     */
    private static void simulateVirusSpread() {
        // 새로 3개의 벽이 생겼으므로, 초기 빈 칸에서 3을 뺀 값으로 시작 (시간 복잡도 개선)
        int currentSafeCells = initialEmptyCellsCount - 3;
        
        // (선택적 가지치기) 만약 3개의 벽을 세우고 난 후의 빈 칸 수가 현재 최대 안전 영역보다 작거나 같으면
        // 바이러스가 확산되기 전에도 이미 최적해가 아니므로 더 이상 시뮬레이션할 필요가 없습니다.
        // 이 가지치기는 BFS 자체를 건너뛸 수 있으나, 확산 후 줄어들 빈 칸을 고려하면 효과가 제한적일 수 있습니다.
        // 현재 로직은 BFS 중 실시간으로 빈 칸을 줄이므로 이 단계의 가지치기는 생략합니다.

        // 원본 맵을 복사하여 사용 (원본 맵을 보존해야 함)
        int[][] copyMap = new int[N][M];
        for (int i = 0; i < N; i++) {
            System.arraycopy(originalMap[i], 0, copyMap[i], 0, M); // 효율적인 배열 복사
        }

        // 새로운 벽 3개를 copyMap에 배치
        for (Point p : newWallPositions) {
            copyMap[p.r()][p.c()] = WALL; // Point의 r, c 필드 사용
        }

        // BFS를 위한 큐 초기화
        Queue<Point> queue = new LinkedList<>();

        // 초기 바이러스 위치를 큐에 모두 추가 (사전 계산된 리스트 활용, 시간 복잡도 개선)
        for (Point p : initialVirusPositions) {
            queue.add(p);
        }

        // BFS 시작: 바이러스 확산
        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            int r = curr.r();
            int c = curr.c();

            for (int d = 0; d < 4; d++) { // 상하좌우 4방향 탐색
                int nr = r + dr[d];
                int nc = c + dc[d];

                // 맵 범위 내에 있고, 아직 빈 칸인 경우
                if (nr >= 0 && nr < N && nc >= 0 && nc < M && copyMap[nr][nc] == EMPTY) {
                    copyMap[nr][nc] = VIRUS; // 바이러스 확산
                    queue.add(new Point(nr, nc));
                    currentSafeCells--; // 안전 영역 1 감소 (시간 복잡도 개선)
                }
            }
        }

        // 모든 바이러스 확산 후 남은 안전 영역의 최댓값 업데이트
        maxSafeCells = Math.max(currentSafeCells, maxSafeCells);
    }
}
```

---

### 5. 왜 이 방법이 최선인지 설명

1.  **시간 복잡도 측면:**
    *   **조합 (3개의 벽 설치):** `C(Blank_count, 3)` = `C(N*M - Initial_Walls - Initial_Viruses, 3)`입니다. N, M이 최대 8이므로 빈 칸은 최대 64개입니다. `C(64, 3)`은 약 41,664가지 경우의 수를 가집니다. 이는 문제의 제약 조건 (N, M <= 8)에 대해 필연적으로 수행해야 하는 계산이며, 이보다 더 적은 조합을 찾는 방법은 없습니다.
    *   **BFS (바이러스 확산):** 각 조합에 대해 O(N*M)의 시간 복잡도를 가집니다. 맵을 복사하고, 모든 바이러스가 퍼져나가는 과정은 맵의 모든 셀을 최대 한 번씩 방문하기 때문입니다.
    *   **개선된 BFS:**
        *   `initialEmptyCellsCount`와 `initialVirusPositions`를 미리 계산함으로써, 각 `simulateVirusSpread()` 호출 시 `N*M` 순회를 두 번(초기 바이러스 찾기, 빈 칸 카운트) 줄였습니다. 이는 `C(64, 3)`번 반복되므로 `2 * N*M * C(64,3)` 만큼의 연산량을 줄인 효과가 있습니다. (약 `2 * 64 * 41664` = `5백만` 연산 감소)
        *   BFS 중에 `currentSafeCells`를 실시간으로 감소시킴으로써, BFS 종료 후 맵 전체를 다시 순회(`N*M`)하며 빈 칸을 세는 과정을 없앴습니다. 이 또한 `N*M * C(64,3)` 만큼의 연산량을 줄이는 효과가 있습니다.
    *   총 시간 복잡도는 `O(C(Blank_count, 3) * N*M)`으로 동일하지만, 상수 인자가 줄어들어 실제 실행 시간이 빨라집니다. N, M이 작으므로 이 정도 개선으로도 충분합니다.

2.  **공간 복잡도 측면:**
    *   `originalMap`, `blankList`, `initialVirusPositions`, `copyMap`, `queue` 모두 O(N*M)의 공간을 사용합니다.
    *   `copyMap`은 각 시뮬레이션마다 생성되어야 하므로 O(N*M)이 필요합니다. 이를 줄이는 것은 불가능하며, 깊은 복사(`System.arraycopy`를 활용한)는 배열 내의 객체 자체가 아닌 값만 복사하기에 효율적입니다.
    *   `Point` Record를 사용한다고 해서 `int[]`에 비해 공간이 유의미하게 늘어나지 않습니다. 오히려 명시적인 필드명을 통해 코드를 더 안전하게 만들 수 있습니다.
    *   `N, M <= 8`이라는 작은 제약 조건 때문에 `N*M` 공간은 최대 64개 `int` (또는 `Point`) 객체에 불과하여 메모리 사용량이 극히 적습니다. 이 이상의 공간 최적화는 큰 의미가 없습니다.

3.  **가독성 측면:**
    *   **상수 사용:** `0, 1, 2` 대신 `EMPTY, WALL, VIRUS`를 사용함으로써 맵의 각 셀이 나타내는 의미를 명확하게 알 수 있습니다.
    *   **`Point` Record 사용:** `int[] {r, c}` 대신 `Point(r, c)`를 사용하고 `p.r()`, `p.c()`로 접근함으로써, 배열의 어떤 인덱스가 행이고 어떤 인덱스가 열인지 추측할 필요 없이 코드를 직관적으로 이해할 수 있습니다.
    *   **변수/함수명 개선:** `maxPlace` → `maxSafeCells`, `goVirus` → `simulateVirusSpread` 등 의미를 명확히 하는 이름 변경은 코드의 목적과 흐름을 쉽게 파악하도록 돕습니다.
    *   **Scanner 자원 해제:** `sc.close()`를 통해 자원 관리의 모범 사례를 따릅니다.

**결론:**
이러한 최적화는 N, M이 작은 문제에서는 성능에 결정적인 영향을 주지 않을 수 있지만, N, M이 조금 더 커지는 상황에서는 필수적이며, 코드의 유지보수성과 이해도를 크게 높여줍니다. 현재 주어진 문제의 맥락에서 시간/공간 복잡도 측면에서 더 근본적인 알고리즘 변경 없이 도달할 수 있는 가장 효율적인 접근 방식이며, 가독성 측면에서도 크게 개선되었습니다.