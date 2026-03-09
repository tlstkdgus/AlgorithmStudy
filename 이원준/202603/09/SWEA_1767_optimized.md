# 최적화 분석

주어진 코드는 백트래킹(DFS)을 사용하여 최대 연결 코어 수를 찾고, 그 중 최소 전선 길이를 구하는 문제인 SWEA 1767번 (프로세서 연결)을 해결합니다. 이 코드를 다음 요구사항에 맞춰 최적화하고 설명하겠습니다.

---

### 분석 및 최적화 방향

1.  **시간복잡도 개선 (가장 중요)**
    *   **가지치기 (Pruning):** 현재 DFS는 모든 가능한 경로를 탐색합니다. 만약 현재까지 연결된 코어 수와 앞으로 연결할 수 있는 모든 코어 수를 합쳐도 이미 찾은 `maxCore`보다 작다면, 해당 경로는 더 이상 탐색할 필요가 없습니다. 이 "조기 종료" 조건은 시간 복잡도를 크게 줄여줍니다. 이것이 이 문제에서 가장 핵심적인 최적화입니다.
    *   `checkAndDraw` 함수의 효율성: 현재 `checkAndDraw`는 `value == 2`일 때 먼저 경로를 확인하고, 그 다음 다시 경로를 따라가며 선을 그립니다. 이미 효율적으로 작성되었지만, "경로 확인"과 "경로 그리기/지우기"의 역할을 명확히 분리하여 가독성을 높일 수 있습니다.

2.  **공간복잡도 개선**
    *   `N`이 최대 12인 상황에서 `coreArr`(`N*N`)과 `coreList`는 문제 해결에 필수적인 데이터 구조입니다. 재귀 스택 또한 `coreList`의 크기에 비례합니다. 이 문제의 본질적인 특성상 공간 복잡도는 `O(N^2)`이 최선이며, 크게 줄일 방법은 없습니다.

3.  **가독성 개선**
    *   **상수 사용:** 마법 숫자(magic number) `0`, `1`, `2` 대신 의미 있는 상수를 사용합니다 (`EMPTY`, `PROCESSOR`, `WIRE`).
    *   **함수 분리 및 이름 변경:** `checkAndDraw`는 두 가지 역할을 동시에 수행하므로, 이를 `getConnectLength` (확인 및 길이 반환)와 `setWire` (격자 상태 변경)로 분리하여 각 함수의 역할을 명확히 합니다.
    *   **주석 추가:** 중요한 로직에 주석을 추가하여 이해를 돕습니다.
    *   `Scanner` 닫기: 리소스 누수를 방지합니다.

4.  **최종 최적화된 코드**
    위에서 언급된 개선 사항들을 적용하여 최적화된 코드를 제시합니다.

---

### 1. 시간복잡도 개선 방법

**핵심:** **백트래킹 가지치기 (Pruning)**

*   **현재 코드의 문제점:** DFS의 `depth == coreList.size()` (모든 코어를 탐색했을 경우) 조건에 도달해야만 `maxCore`와 `minLineLength`를 갱신합니다. 즉, 현재 경로가 `maxCore`를 절대 달성할 수 없음을 미리 알 수 있어도 끝까지 탐색합니다.
*   **개선 방법:** `dfs` 함수 시작 부분에 다음과 같은 가지치기 조건을 추가합니다.
    ```java
    if (coreCount + (coreList.size() - depth) < maxCore) {
        return; // 현재까지 연결된 코어 수 + 남은 코어 수를 모두 연결해도 maxCore보다 작다면, 이 경로는 무의미하므로 탐색 중단
    }
    ```
    *   `coreCount`: 현재까지 성공적으로 연결된 코어 수.
    *   `(coreList.size() - depth)`: 앞으로 시도해볼 수 있는 남은 비벽 코어의 수.
    *   이 조건을 통해 `maxCore`를 넘어서는 것이 불가능한 가지는 일찍 잘라내어 탐색 공간을 획기적으로 줄일 수 있습니다. 이것이 `N=12`와 같이 `coreList.size()`가 커질 수 있는 문제에서 시간 초과를 피하는 가장 중요한 방법입니다.

---

### 2. 공간복잡도 개선 방법

*   이 문제에서 `coreArr`(`int[N][N]`)는 격자 상태를 저장하는 데 필수적이며, `coreList`(`List<int[]>`)는 연결해야 할 코어들의 좌표를 저장하는 데 필요합니다.
*   재귀 호출 스택은 `coreList.size()`에 비례하며, 최악의 경우 `O((N-2)^2)`까지 갈 수 있습니다.
*   이러한 데이터 구조는 문제의 본질적인 요구사항이므로, `O(N^2)`이라는 공간 복잡도는 이 문제에 대해 최적입니다. 따라서, 공간 복잡도를 크게 개선할 여지는 없습니다.

---

### 3. 가독성 개선

*   **상수 사용:**
    ```java
    // 변경 전:
    // if(coreArr[i][j] == 1) { ... if(coreArr[nx][ny] != 0) return 0; ... }
    // coreArr[nx][ny] = value; // value는 2 또는 0
    // 변경 후:
    static final int EMPTY = 0;
    static final int PROCESSOR = 1;
    static final int WIRE = 2;
    // 사용 예:
    // if(coreArr[i][j] == PROCESSOR) { ... if(coreArr[nr][nc] != EMPTY) return 0; ... }
    // coreArr[nr][nc] = value; // value는 WIRE 또는 EMPTY
    ```
*   **함수 분리 및 이름 변경:**
    *   `checkAndDraw(r, c, dr, value)` 함수를 두 개의 명확한 역할로 나눕니다.
        *   `getConnectLength(r, c, dr)`: 해당 방향으로 전선을 연결할 수 있는지 확인하고, 연결 가능하다면 길이를 반환, 불가능하면 0 반환. (격자 변경 없음)
        *   `setWire(r, c, dr, value, length)`: 지정된 방향과 길이만큼 격자에 전선을 그리거나(WIRE) 지웁니다(EMPTY). (격자 변경)
*   **주석 추가:** 코드의 핵심 로직 (특히 `dfs`의 가지치기 조건)에 설명을 추가합니다.
*   **Scanner 닫기:** `sc.close();`를 `main` 메서드 끝에 추가하여 자원을 해제합니다.

---

### 4. 최종 최적화된 코드

```java
package backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SWEA_1767 {
    // --- 가독성 개선을 위한 상수 선언 ---
    static final int EMPTY = 0;       // 빈 공간
    static final int PROCESSOR = 1;   // 프로세서
    static final int WIRE = 2;        // 전선

    // --- 전역 변수 (경쟁 프로그래밍에서 흔히 사용) ---
    static int T;                     // 테스트 케이스 수
    static int N;                     // 격자 크기
    static int[][] coreArr;           // 맵 (격자) 배열
    static int maxCore;               // 연결된 최대 코어 수
    static List<int[]> coreList;      // 벽에 붙어있지 않은 코어들의 좌표 리스트
    static int minLineLength;         // maxCore를 달성했을 때의 최소 전선 길이
    
    // 델타 배열: 상, 하, 좌, 우
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        T = sc.nextInt();
        
        for(int tc = 1; tc <= T; tc++) {
            // 각 테스트 케이스마다 변수 초기화
            maxCore = 0;
            minLineLength = Integer.MAX_VALUE;
            
            N = sc.nextInt();
            coreArr = new int[N][N];
            coreList = new ArrayList<>();
            
            int initialCore = 0; // 벽에 붙어있어 이미 연결된 것으로 간주되는 코어 수
            
            for(int i = 0; i < N; i++) {
                for(int j = 0; j < N; j++) {
                    coreArr[i][j] = sc.nextInt();
                    // 프로세서일 경우
                    if(coreArr[i][j] == PROCESSOR) {
                        // 벽에 붙어 있는 코어는 초기 연결 코어 수에 추가
                        if(i == 0 || j == 0 || i == N-1 || j == N-1) {
                            initialCore++;
                        // 벽에 붙어있지 않은 코어는 coreList에 추가 (연결 시도 대상)
                        } else {
                            coreList.add(new int[] {i, j});
                        }
                    }
                }
            }
            
            // DFS 탐색 시작
            // depth: coreList에서 현재 탐색 중인 코어의 인덱스
            // coreCount: 현재까지 연결된 총 코어 수 (initialCore 포함)
            // currLength: 현재까지 사용된 총 전선 길이
            dfs(0, initialCore, 0);
            
            System.out.println("#"+tc+" "+minLineLength);
        }
        sc.close(); // Scanner 자원 해제
    }
    
    /**
     * DFS (백트래킹) 함수
     * @param depth      coreList에서 현재 처리할 코어의 인덱스
     * @param coreCount  현재까지 연결된 총 코어 수 (initialCore 포함)
     * @param currLength 현재까지 사용된 총 전선 길이
     */
    static void dfs(int depth, int coreCount, int currLength) {
        // --- 시간복잡도 개선: 가지치기 (Pruning) ---
        // (가장 중요한 최적화)
        // 현재까지 연결된 코어 수와 앞으로 연결할 수 있는 모든 코어 수를 더해도
        // 이미 발견된 최대 코어 수(maxCore)보다 작다면, 이 경로는 더 이상 탐색할 필요가 없다.
        // 현재 maxCore가 0이 아니라면 (최소한 한 번의 유효한 경로를 찾았다면)
        // 이 가지치기는 매우 효과적이다.
        if (coreCount + (coreList.size() - depth) < maxCore) {
            return;
        }

        // --- 백트래킹의 종료 조건 (Base Case) ---
        // 모든 비(非)벽 코어를 다 탐색했을 경우
        if(depth == coreList.size()) {
            // 현재 코어 수가 기존 최대 코어 수보다 크다면 갱신
            if(coreCount > maxCore) {
                maxCore = coreCount;
                minLineLength = currLength;
            } 
            // 현재 코어 수가 기존 최대 코어 수와 같고, 전선 길이가 더 짧다면 갱신
            else if(coreCount == maxCore) {
                minLineLength = Math.min(minLineLength, currLength);
            }
            return;
        }
        
        int r = coreList.get(depth)[0]; // 현재 코어의 행 좌표
        int c = coreList.get(depth)[1]; // 현재 코어의 열 좌표
        
        // --- 현재 코어를 4방향으로 연결 시도 ---
        for(int d = 0; d < 4; d++) {
            // 전선을 연결할 수 있는지 확인하고, 가능하다면 전선 길이 반환 (격자 변경 없음)
            int wireLen = getConnectLength(r, c, d);
            
            if(wireLen > 0) { // 전선 연결이 가능하다면 (길이가 0보다 크다면)
                setWire(r, c, d, WIRE, wireLen); // 전선 그리기 (격자에 WIRE로 표시)
                // 다음 코어로 재귀 호출 (코어 수 증가, 전선 길이 추가)
                dfs(depth + 1, coreCount + 1, currLength + wireLen);
                setWire(r, c, d, EMPTY, wireLen); // 백트래킹: 그린 전선 지우기 (격자에 EMPTY로 복원)
            }
        }
        
        // --- 현재 코어를 연결하지 않는 경우 ---
        // 이 코어를 연결하지 않아도 최적의 해가 나올 수 있으므로 탐색해야 한다.
        // 다음 코어로 재귀 호출 (코어 수와 전선 길이는 그대로)
        dfs(depth + 1, coreCount, currLength);
    }
    
    /**
     * 특정 코어에서 특정 방향으로 전선을 연결할 수 있는지 확인하고 길이를 반환하는 함수
     * @param r   코어의 행 좌표
     * @param c   코어의 열 좌표
     * @param dr  탐색 방향 (0:상, 1:하, 2:좌, 3:우)
     * @return    연결 가능한 전선 길이 (연결 불가 시 0 반환)
     */
    static int getConnectLength(int r, int c, int dr) {
        int len = 0;
        int nr = r;
        int nc = c;
        
        while(true) {
            nr += dx[dr]; // 다음 행
            nc += dy[dr]; // 다음 열
            
            // 맵의 경계를 벗어나면 (벽에 도달) 연결 성공
            if(nr < 0 || nr >= N || nc < 0 || nc >= N) {
                return len;
            }
            
            // 다른 프로세서나 이미 연결된 전선을 만나면 연결 불가
            if(coreArr[nr][nc] != EMPTY) {
                return 0;
            }
            len++; // 전선 길이 증가
        }
    }
    
    /**
     * 특정 코어에서 특정 방향으로 전선을 그리거나 지우는 함수
     * (getConnectLength 함수로 연결 가능성이 확인된 후에 사용)
     * @param r      코어의 행 좌표
     * @param c      코어의 열 좌표
     * @param dr     탐색 방향 (0:상, 1:하, 2:좌, 3:우)
     * @param value  그릴 값 (WIRE: 전선, EMPTY: 빈 공간)
     * @param length 그릴/지울 전선의 길이
     */
    static void setWire(int r, int c, int dr, int value, int length) {
        int nr = r;
        int nc = c;
        
        for(int i = 0; i < length; i++) {
            nr += dx[dr];
            nc += dy[dr];
            coreArr[nr][nc] = value;
        }
    }
}
```

---

### 5. 왜 이 방법이 최선인지 설명

1.  **백트래킹 (DFS) 활용:**
    *   이 문제는 각 코어에 대해 "연결할 것인가, 말 것인가"를 결정하고, 연결한다면 "어떤 방향으로 연결할 것인가"를 선택하는 조합 최적화 문제입니다. 모든 가능한 선택지를 탐색해야 하므로, 백트래킹을 기반으로 한 DFS가 가장 적합한 접근 방식입니다.

2.  **가지치기 (Pruning)를 통한 시간 복잡도 최적화:**
    *   `if (coreCount + (coreList.size() - depth) < maxCore)` 조건은 이 문제의 핵심 최적화입니다. `N`이 12일 경우, 비(非)벽 코어의 수가 최대 100개까지 될 수 있습니다. 단순히 `2^100`에 가까운 경우의 수를 모두 탐색하는 것은 불가능합니다.
    *   이 가지치기 조건은 현재 경로가 아무리 최선을 다해도 이미 발견된 `maxCore`보다 더 나은 결과를 낼 수 없다는 것이 명확해지면, 즉시 해당 경로의 탐색을 중단시킵니다. 이는 탐색 공간을 극적으로 줄여주어 제한 시간(Time Limit) 내에 문제를 해결할 수 있게 합니다. 이 가지치기가 없다면 시간 초과(TLE)가 발생할 가능성이 매우 높습니다.
    *   문제에서 `maxCore`를 우선하고 그 다음 `minLineLength`를 찾는다는 요구사항에 따라, `maxCore`에 대한 가지치기는 다른 어떤 최적화보다 우선적으로 고려되어야 합니다.

3.  **효율적인 경로 확인 및 상태 변경:**
    *   `getConnectLength` 함수는 실제로 격자 상태를 변경하지 않고 오직 "경로 확인 및 길이 계산" 역할만 수행합니다.
    *   `setWire` 함수는 "격자 상태 변경" (전선 그리기 또는 지우기) 역할만 담당합니다.
    *   이러한 역할 분리는 코드의 가독성을 높이고 각 함수의 책임을 명확하게 합니다. 또한, `getConnectLength`에서 경로를 한 번만 스캔하여 길이를 반환함으로써 불필요한 중복 탐색을 방지합니다.

4.  **메모리 효율성:**
    *   `coreArr` (격자) 및 `coreList` (코어 목록)는 문제 상태를 저장하는 데 필수적인 데이터 구조입니다. 재귀 스택도 `coreList`의 크기에 비례합니다. `N=12`일 때 `O(N^2)`의 공간 복잡도는 문제의 제약 조건과 본질적인 해결 방식에 비추어 최적의 상태이며, 추가적인 공간 절약은 어렵습니다.

5.  **전역 변수 활용:**
    *   경쟁 프로그래밍에서는 자주 사용되는 방식으로, `N`, `coreArr`, `maxCore` 등 여러 함수에서 공유하는 변수들을 전역으로 선언하여 매번 인자로 전달하는 오버헤드를 줄이고 코드 길이를 단축시킵니다. 이는 성능에 미미한 영향을 주지만, 간결한 코드 작성에 유리합니다.

이러한 접근 방식은 조합 최적화 문제에서 흔히 사용되는 표준 기법들을 효과적으로 적용하여, 제한된 시간 및 메모리 제약 조건 하에서 최적의 해를 찾도록 설계되었습니다.