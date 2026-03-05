# 최적화 분석

주어진 코드는 백트래킹을 사용하여 배열 회전 연산의 모든 순열을 탐색하고, 각 순열마다 배열을 복사하고 회전시킨 후 행의 최소 합을 계산합니다. 이 과정에서 비효율적인 부분이 존재하며, 이를 최적화할 수 있습니다.

---

### 1. 시간 복잡도 개선 방법

**문제점:**
`perm` 함수에서 `depth == K`일 때 호출되는 `calculate()` 함수는 매번 원본 배열 `arr`를 `copy`라는 새 배열로 `O(N*M)` 시간에 복사합니다. 총 `K!`개의 순열이 존재하므로, 배열 복사 작업만 `K! * O(N*M)`의 시간 복잡도를 가집니다. `N, M <= 50, K <= 6`일 때 `720 * 50 * 50 = 1,800,000`번의 정수 복사가 발생합니다. 여기에 각 순열마다 `K`개의 회전 연산과 `O(N*M)`의 행 합 계산이 추가됩니다.

**개선 방법 (되돌리기 - Revert Operations):**
새로운 배열을 계속 복사하는 대신, 단 하나의 작업용 배열(`workingArr`)을 사용합니다.
1.  `main` 함수 시작 시, 원본 배열 `originalArr`를 `workingArr`로 한 번 깊은 복사합니다.
2.  `perm` 함수에서 재귀 호출하기 전에 `workingArr`에 현재 회전 연산을 적용합니다 (`rotateMap`).
3.  `perm` 함수가 재귀 호출에서 돌아온 후 (백트래킹 시점), 이전에 적용했던 회전 연산을 `workingArr`에서 **되돌립니다** (`unRotateMap`).
4.  `depth == K` (모든 회전 연산이 적용된 상태)가 되면, `workingArr`에 대해 행의 최소 합을 계산합니다.

이 방법을 사용하면 `K!`번의 `O(N*M)` 배열 복사를 피하고, 대신 `K`번의 `O(s^2)` 회전 및 `K`번의 `O(s^2)` 역회전 연산으로 대체됩니다. 여기서 `s`는 회전 연산의 크기 (`s <= max(N, M)/2`)입니다. 보통 `s^2`는 `N*M`보다 훨씬 작기 때문에 훨씬 효율적입니다.

**시간 복잡도 비교:**
*   **원본:** `O(K! * (N*M_copy + K*s_max^2 + N*M_sum))`
*   **최적화:** `O(N*M_initial_copy + K! * (K*s_max^2_rotation + K*s_max^2_unrotation + N*M_sum))`
    *   `K*s_max^2`가 `N*M`보다 작으므로, `K!`번 발생하는 `N*M_copy`를 제거한 것이 주요 개선점입니다.

### 2. 공간 복잡도 개선 방법

**문제점:**
`calculate()` 함수 내에서 `copy` 배열이 `K!`번 생성되고, 함수 종료 시 가비지 컬렉션의 대상이 됩니다. 이는 반복적인 메모리 할당 및 해제를 유발하여 성능 저하의 원인이 될 수 있습니다. 각 `copy` 배열은 `O(N*M)`의 공간을 차지합니다.

**개선 방법:**
1.  `workingArr`라는 하나의 전역(또는 인스턴스) 배열만 유지하고 이를 계속해서 수정합니다.
2.  `unRotateMap`을 통해 변경된 상태를 이전으로 되돌리므로, 추가적인 `N*M` 크기의 배열 할당이 필요 없습니다.
3.  최대 `O(N*M)` 크기의 `originalArr`와 `workingArr`만 메모리에 유지됩니다.

**공간 복잡도 비교:**
*   **원본:** 최대 `O(N*M)` (현재 `copy` 배열과 `arr` 배열)
*   **최적화:** `O(N*M)` (고정된 `originalArr`와 `workingArr`만 사용)
    *   할당 및 해제 오버헤드를 줄이는 것이 주된 이점입니다.

### 3. 가독성 개선

*   변수명: `cal` -> `rotationOps`, `arr` -> `originalArr`, `minArrValue` -> `minOverallValue` 등 좀 더 의미를 명확히 하는 이름으로 변경합니다.
*   함수명: `calculate` -> `calculateMinRowSum`, `rotateMap` -> `rotateClockwise` (정확한 회전 방향 명시) 또는 `performRotation` 등으로 변경하여 함수의 목적을 명확히 합니다. `unRotateMap` 함수를 분리하여 역회전 로직을 명확히 합니다.
*   인덱싱: 1-based 인덱싱을 유지하되, `top`, `bottom`, `left`, `right` 변수를 더 명확히 사용합니다.
*   `Scanner` 닫기: `sc.close()`를 추가하여 자원 누수를 방지합니다.

---

### 4. 최종 최적화된 코드

주요 변경 사항:
1.  `originalArr`와 `workingArr` 두 개의 2D 배열을 전역으로 선언합니다.
2.  `main`에서 `originalArr`를 입력받고, `workingArr`에 `originalArr`를 깊은 복사합니다.
3.  `perm` 함수 내에서 재귀 호출 전에 `rotateMap` (시계 방향)을 적용하고, 재귀 호출 후에 `unRotateMap` (반시계 방향)을 적용하여 상태를 되돌립니다.
4.  `calculateMinRowSum` 함수는 `depth == K`일 때 `workingArr`를 인자로 받아 행의 최소 합을 계산합니다.
5.  `rotateMap`은 원본 코드의 로직을 따르며, 이는 실제로는 **시계 방향** 회전임을 확인했습니다.
6.  `unRotateMap`은 `rotateMap`의 역방향인 **반시계 방향** 회전을 수행하도록 새로 구현합니다.

```java
package backtracking;

import java.util.Scanner;

public class BOJ_17406_Optimized {
    static int N, M, K;
    static int[][] originalArr; // 원본 배열의 상태를 저장
    static int[][] workingArr;  // 회전 연산이 적용될 작업용 배열
    static int[][] rotationOps; // 각 회전 연산의 (r, c, s) 정보를 저장
    static boolean[] visited;   // 회전 연산 방문 여부
    static int minOverallValue = Integer.MAX_VALUE; // 전체 최소 행 합

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        N = sc.nextInt();
        M = sc.nextInt();
        K = sc.nextInt();

        originalArr = new int[N + 1][M + 1];
        workingArr = new int[N + 1][M + 1];

        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= M; j++) {
                originalArr[i][j] = sc.nextInt();
            }
        }

        rotationOps = new int[K][3]; // {r, c, s}
        for (int i = 0; i < K; i++) {
            for (int j = 0; j < 3; j++) {
                rotationOps[i][j] = sc.nextInt();
            }
        }
        sc.close();

        visited = new boolean[K];
        
        // 초기 작업용 배열을 원본 배열로 복사
        deepCopy(originalArr, workingArr);

        perm(0); // 순열 생성 시작

        System.out.println(minOverallValue);
    }

    // 2D 배열을 깊은 복사하는 헬퍼 함수
    static void deepCopy(int[][] source, int[][] destination) {
        for (int i = 1; i <= N; i++) {
            System.arraycopy(source[i], 1, destination[i], 1, M);
        }
    }

    // 회전 연산의 모든 순열을 생성하는 함수
    static void perm(int depth) {
        if (depth == K) {
            // 모든 K개의 회전 연산이 workingArr에 순서대로 적용된 상태
            // 이제 이 workingArr에 대해 행의 최소 합을 계산
            minOverallValue = Math.min(minOverallValue, calculateMinRowSum(workingArr));
            return;
        }

        for (int i = 0; i < K; i++) {
            if (!visited[i]) {
                visited[i] = true;
                int r = rotationOps[i][0];
                int c = rotationOps[i][1];
                int s = rotationOps[i][2];

                // 현재 workingArr에 회전 연산 적용 (시계 방향)
                rotateClockwise(workingArr, r, c, s);

                perm(depth + 1); // 다음 연산 순열 탐색

                // 백트래킹: 이전에 적용한 회전 연산을 되돌림 (반시계 방향)
                unRotateCounterClockwise(workingArr, r, c, s);
                
                visited[i] = false; // 방문 상태 초기화
            }
        }
    }

    // 주어진 배열에서 각 행의 합 중 최솟값을 계산하는 함수
    static int calculateMinRowSum(int[][] map) {
        int currentMin = Integer.MAX_VALUE;
        for (int i = 1; i <= N; i++) {
            int rowSum = 0;
            for (int j = 1; j <= M; j++) {
                rowSum += map[i][j];
            }
            currentMin = Math.min(currentMin, rowSum);
        }
        return currentMin;
    }

    // 시계 방향으로 배열을 회전하는 함수 (원본 코드의 rotateMap과 동일한 로직)
    static void rotateClockwise(int[][] map, int r, int c, int s) {
        for (int i = 1; i <= s; i++) { // 각 레이어별로 회전
            int top = r - i;
            int bottom = r + i;
            int left = c - i;
            int right = c + i;

            int temp = map[top][left]; // (top, left) 값을 임시 저장

            // 왼쪽 변: 위로 쉬프트 (아래 값들이 위로 이동)
            for (int j = top; j < bottom; j++) {
                map[j][left] = map[j + 1][left];
            }
            // 아래 변: 왼쪽으로 쉬프트 (오른쪽 값들이 왼쪽으로 이동)
            for (int j = left; j < right; j++) {
                map[bottom][j] = map[bottom][j + 1];
            }
            // 오른쪽 변: 아래로 쉬프트 (위 값들이 아래로 이동)
            for (int j = bottom; j > top; j--) {
                map[j][right] = map[j - 1][right];
            }
            // 위 변: 오른쪽으로 쉬프트 (왼쪽 값들이 오른쪽으로 이동)
            for (int j = right; j > left + 1; j--) {
                map[top][j] = map[top][j - 1];
            }
            
            map[top][left + 1] = temp; // 임시 저장했던 (top, left) 값을 (top, left+1)에 삽입
        }
    }

    // 반시계 방향으로 배열을 회전하는 함수 (rotateClockwise의 역방향)
    static void unRotateCounterClockwise(int[][] map, int r, int c, int s) {
        for (int i = 1; i <= s; i++) { // 각 레이어별로 회전
            int top = r - i;
            int bottom = r + i;
            int left = c - i;
            int right = c + i;

            int temp = map[top][left]; // (top, left) 값을 임시 저장 (반시계 방향 회전의 시작점)

            // 위 변: 왼쪽으로 쉬프트 (오른쪽 값들이 왼쪽으로 이동)
            for (int j = left; j < right; j++) {
                map[top][j] = map[top][j + 1];
            }
            // 오른쪽 변: 위로 쉬프트 (아래 값들이 위로 이동)
            for (int j = top; j < bottom; j++) {
                map[j][right] = map[j + 1][right];
            }
            // 아래 변: 오른쪽으로 쉬프트 (왼쪽 값들이 오른쪽으로 이동)
            for (int j = right; j > left; j--) {
                map[bottom][j] = map[bottom][j - 1];
            }
            // 왼쪽 변: 아래로 쉬프트 (위 값들이 아래로 이동)
            for (int j = bottom; j > top + 1; j--) {
                map[j][left] = map[j - 1][left];
            }
            
            map[top + 1][left] = temp; // 임시 저장했던 (top, left) 값을 (top+1, left)에 삽입
        }
    }
}
```

---

### 5. 왜 이 방법이 최선인지 설명

이 최적화 방법은 다음과 같은 이유로 해당 문제 유형에서 최선에 가깝습니다.

1.  **필수적인 복잡도 유지:**
    *   문제 자체가 `K`개의 연산을 "모든 순열"로 적용해야 하므로, `K!`의 순열을 생성하는 것은 피할 수 없는 시간 복잡도입니다. (NP-hard 문제의 일종)
    *   각 회전 연산 자체는 `O(s^2)`에 최적으로 수행됩니다. (`s`는 회전 반경)
    *   각 배열의 행 합을 계산하는 것은 `O(N*M)`에 최적으로 수행됩니다.

2.  **병목 구간 개선:**
    *   원본 코드의 가장 큰 병목은 각 순열마다 `O(N*M)` 크기의 배열을 `K!`번 새로 복사하는 작업이었습니다. 이는 CPU 캐시 미스 증가, 힙 메모리 할당 및 가비지 컬렉션 부하를 야기합니다.
    *   최적화된 코드는 단 하나의 작업용 배열(`workingArr`)을 사용하고, `rotateMap` 및 `unRotateMap`을 통해 이 배열을 직접 수정합니다. 이로써 `K!`번의 `O(N*M)` 복사 연산이 사라지며, 대신 `2 * K! * K * O(s^2)` (회전 + 역회전) 연산으로 대체됩니다.
    *   일반적으로 `s^2`는 `N*M`보다 훨씬 작으므로, 이는 상당한 성능 향상을 가져옵니다. 예를 들어, `N=50, M=50, s=25`일 때 `N*M = 2500`이지만 `s^2 = 625`입니다. `2500`번의 복사를 `625`번의 회전으로 대체하는 것은 훨씬 효율적입니다.

3.  **메모리 효율성:**
    *   `K!`번의 배열 복사로 인한 `K! * O(N*M)`의 임시 메모리 할당이 없어집니다.
    *   전역으로 선언된 `originalArr`와 `workingArr`가 각각 `O(N*M)`의 고정된 공간을 차지하므로, 총 메모리 사용량이 예측 가능하고 안정적입니다.

4.  **백트래킹 패턴의 모범 사례:**
    *   재귀 호출 전 상태 변경, 재귀 호출 후 상태 복원(되돌리기)은 백트래킹 알고리즘에서 흔히 사용되는 최적화 기법입니다. 이는 `workingArr`와 `unRotateMap`을 사용하는 이 접근 방식의 핵심입니다.

따라서 이 방법은 문제의 근본적인 복잡도를 유지하면서, 반복적인 복사로 인한 비효율적인 부분을 제거하여 시간 및 공간 효율성 측면에서 가장 합리적인 최적화라고 할 수 있습니다.