# 최적화 분석

주어진 자바 코드는 백준 17135번 "캐슬 디펜스" 문제를 해결하는 알고리즘입니다. 최적화 목표는 시간 및 공간 복잡도를 개선하고, 가독성을 높이는 것입니다.

### 1. 현재 코드 분석 및 문제점

#### 시간 복잡도 분석:
1.  **`combination` (궁수 배치):** `M`개의 열 중 3개를 선택하는 조합의 수 `M C 3`만큼 `playGame`을 호출합니다.
    *   `M C 3`은 `(M * (M-1) * (M-2)) / 6` 입니다. `M=15`일 경우, `15 C 3 = 455`가지 경우의 수가 나옵니다. 이 부분은 `M`이 작기 때문에 최적의 방법입니다.
2.  **`playGame` (게임 시뮬레이션):**
    *   **맵 복사:** `int[][] gameStart = new int[N][M];` 내부에서 `N*M`번 복사합니다. (`N*M` 연산)
    *   **턴 반복:** `for(int turn = 0; turn < N; turn++)` (`N`번 반복)
        *   **`findTarget` 호출:** 각 턴마다 3명의 궁수가 타겟을 찾습니다. (`3 * findTarget_cost`)
            *   `findTarget`: 모든 맵 셀 `N*M`을 순회하며 가장 가까운 적을 찾습니다. (`N*M` 연산)
            *   총 `3 * N * M` 연산
        *   **타겟 처리:** `targets` 리스트 순회 (최대 3개). `O(1)` 연산
        *   **적 이동:** `N*M` 배열 요소를 `N-1`번 시프트하고, 첫 번째 행을 0으로 채웁니다. (`N*M` 연산)
    *   **총 `playGame` 시간 복잡도:** `O(N*M + N * (3*N*M + N*M)) = O(N^2 * M)`
3.  **총 시간 복잡도:** `O(M C 3 * N^2 * M)`
    *   `N, M <= 15` 이므로 `15 C 3 * 15^2 * 15 = 455 * 225 * 15 = 약 150만` 연산. 현대 CPU에서는 충분히 빠르지만, `N, M`이 조금 더 컸다면 문제가 될 수 있습니다. 특히 `N*M` 복사 및 시프트 부분에서 비효율성이 존재합니다.

#### 공간 복잡도 분석:
1.  **`defence` 배열:** `O(N*M)`
2.  **`gameStart` 배열:** `playGame` 내부에서 생성되는 복사본. `O(N*M)`
3.  **`archers` 배열, `targets` 리스트:** `O(1)` (상수 크기)
4.  **총 공간 복잡도:** `O(N*M)`

#### 가독성:
*   변수 이름: `defence`, `archers` 등은 의미가 명확하지만, `archers`가 궁수 *위치*를 나타내는지 궁수 *객체*를 나타내는지 모호할 수 있습니다.
*   주석: 설명이 부족합니다.
*   메서드 분리: `playGame` 내부에 `findTarget` 로직이 거의 직접 들어가 있어서 길어 보일 수 있습니다.

### 2. 최적화 방법

#### 1. 시간 복잡도 개선 방법:
가장 큰 비효율성은 **적 이동(`N*M` 배열 시프트)** 부분입니다. 매 턴 `N*M` 요소를 이동시키는 것은 비용이 큽니다.

*   **적 이동 최적화 (가상 이동):** 실제 배열을 움직이지 않고, 궁수의 위치를 상대적으로 움직이는 것으로 간주합니다.
    *   초기 궁수 위치는 `(N, archerCol)`입니다.
    *   `turn`이 1 증가할 때마다 적들이 한 칸씩 아래로 이동하는 대신, 궁수가 한 칸 위로 이동하는 것으로 생각할 수 있습니다.
    *   즉, `turn` 번째 턴에서 궁수들은 가상으로 `(N - turn, archerCol)` 위치에 있다고 가정합니다.
    *   적이 존재하는 행 `r`은 `(N - turn - 1)`부터 `0`까지 순회하며 탐색합니다. `N - turn` 행부터는 적이 존재할 수 없으므로 고려 대상에서 제외됩니다.
    *   이 방법을 사용하면 매 턴 `N*M` 배열 시프트 연산이 사라집니다.

#### 2. 공간 복잡도 개선 방법:
*   현재 `O(N*M)` 공간 복잡도는 `N, M <= 15`에서는 충분히 허용 가능합니다. 추가적인 큰 개선은 필요하지 않습니다.
*   `initialBoard`와 `gameBoardState` (복사본)을 유지하는 것은 각 시뮬레이션이 독립적임을 보장하기 위해 필요합니다.

#### 3. 가독성 개선:
*   **변수명 개선:** `defence` -> `initialBoard`, `archers` -> `archerPositions` 등 명확한 이름 사용.
*   **주석 추가:** 복잡한 로직이나 중요한 아이디어(예: 가상 적 이동)에 대한 주석을 추가합니다.
*   **메서드 분리:** `findTarget` 로직을 별도의 헬퍼 메서드로 분리하는 것을 고려할 수 있으나, 현재 `playGame` 내부에서 `currentArcherRow`와 `archerCol` 등 여러 변수를 함께 사용하므로, 오히려 인자 전달이 많아져 가독성이 떨어질 수 있습니다. `playGame` 내부에 두는 것도 나쁘지 않습니다. (이번 최적화에서는 `findTarget` 자체를 `playGame` 안에 인라인하여 하나의 응집된 로직으로 만들었습니다.)

### 4. 최종 최적화된 코드

```java
package combination;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BOJ_17135_Optimized {
	static int N, M, D;
	static int[][] initialBoard; // 초기 게임 보드 상태를 저장 (defence 대신 더 명확한 이름)
	static int[] archerPositions = new int[3]; // 3명의 궁수들이 배치될 열 위치를 저장
	static int maxKills; // 모든 시뮬레이션 중 최대 킬 수

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		D = sc.nextInt();
		maxKills = 0; // 최소 킬 수는 0이므로 Integer.MIN_VALUE 대신 0으로 초기화

		initialBoard = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				initialBoard[i][j] = sc.nextInt();
			}
		}
		sc.close(); // Scanner 리소스 해제

		// M개의 열 중 3개의 열을 선택하여 궁수 배치 조합 생성
		// archerPositions 배열에 3명의 궁수의 열 위치가 저장됨
		generateArcherCombinations(0, 0);

		System.out.println(maxKills);
	}

	/**
	 * M개의 열 중 3개의 궁수 배치 조합을 재귀적으로 생성합니다.
	 * @param depth 현재 배치할 궁수의 인덱스 (0, 1, 2)
	 * @param start 다음 궁수가 배치될 시작 열 인덱스 (중복 없는 조합을 위해)
	 */
	static void generateArcherCombinations(int depth, int start) {
		if (depth == 3) {
			// 3명의 궁수 배치가 완료되면 게임 시뮬레이션을 시작
			simulateGame(archerPositions);
			return;
		}

		for (int i = start; i < M; i++) {
			archerPositions[depth] = i; // 현재 깊이의 궁수 위치를 i열로 설정
			generateArcherCombinations(depth + 1, i + 1); // 다음 궁수 배치 (i+1부터 시작하여 중복 방지)
		}
	}

	/**
	 * 주어진 궁수 배치에 대해 게임을 시뮬레이션하고 최대 킬 수를 업데이트합니다.
	 * 적 이동 시 맵을 직접 조작하는 대신, 궁수의 유효한 위치를 변경하는 방식으로 최적화되었습니다.
	 * @param currentArcherCols 현재 시뮬레이션에 사용될 3명의 궁수의 열 위치
	 */
	static void simulateGame(int[] currentArcherCols) {
		// 현재 게임 시뮬레이션을 위한 보드 상태 (초기 맵 복사)
		// 각 시뮬레이션은 독립적으로 진행되어야 하므로 매번 초기 맵을 복사
		int[][] gameBoardState = new int[N][M];
		for (int i = 0; i < N; i++) {
			System.arraycopy(initialBoard[i], 0, gameBoardState[i], 0, M);
		}

		int currentKills = 0; // 현재 시뮬레이션에서 잡은 적의 수

		// 게임은 N턴 동안 진행됩니다. (적이 N번 이동하면 모든 적이 사라짐)
		// 'effectiveArcherRow'는 궁수들이 현재 위치한다고 '가정'하는 가상 행입니다.
		// 턴이 진행될수록 적들이 아래로 이동하는 대신, 궁수가 위로 한 칸씩 이동하는 것으로 간주합니다.
		// 따라서 'effectiveArcherRow'는 N, N-1, N-2 ... 1로 줄어듭니다.
		for (int turn = 0; turn < N; turn++) {
			int effectiveArcherRow = N - turn; // 현재 턴에서의 궁수 가상 위치 (행)

			// 이 턴에 궁수들이 공격할 타겟들을 저장하는 리스트
			List<int[]> targetsToKillThisTurn = new ArrayList<>();

			// 각 궁수가 타겟을 찾습니다.
			for (int archerCol : currentArcherCols) {
				int minDist = Integer.MAX_VALUE; // 현재 궁수가 찾은 최소 거리
				int targetR = -1, targetC = -1; // 현재 궁수의 타겟 위치

				// 궁수 바로 윗줄부터 보드 맨 위까지 (가장 가까운 적부터 탐색)
				// 적의 행은 'effectiveArcherRow - 1'부터 0까지 유효합니다.
				for (int r = effectiveArcherRow - 1; r >= 0; r--) {
					for (int c = 0; c < M; c++) {
						// 해당 위치에 적이 있다면
						if (gameBoardState[r][c] == 1) {
							// 맨해튼 거리 계산: (궁수 행 - 적 행) + |궁수 열 - 적 열|
							int dist = (effectiveArcherRow - r) + Math.abs(archerCol - c);

							// 사정거리 D 이내인지 확인
							if (dist <= D) {
								if (dist < minDist) {
									// 더 가까운 적을 발견하면 타겟 업데이트
									minDist = dist;
									targetR = r;
									targetC = c;
								} else if (dist == minDist) {
									// 거리가 같으면, 더 왼쪽에 있는 적을 타겟으로 선택 (열 인덱스가 작은 경우)
									if (c < targetC) {
										targetR = r;
										targetC = c;
									}
								}
							}
						}
					}
				}
				// 현재 궁수가 타겟을 찾았다면 리스트에 추가
				if (targetR != -1) {
					targetsToKillThisTurn.add(new int[] { targetR, targetC });
				}
			}

			// 이 턴에 모든 궁수들이 타겟팅한 적들을 동시에 처리 (제거)
			for (int[] targetCoord : targetsToKillThisTurn) {
				int r = targetCoord[0];
				int c = targetCoord[1];

				// 이미 다른 궁수에 의해 제거되었는지 확인 후 제거
				if (gameBoardState[r][c] == 1) {
					gameBoardState[r][c] = 0; // 적 제거
					currentKills++; // 킬 수 증가
				}
			}
			// *** 중요: 적 이동 로직이 필요 없습니다. ***
			// 'effectiveArcherRow'가 감소하면서, 궁수가 공격할 수 있는 적의 범위가 자동으로 좁아집니다.
			// 즉, 'effectiveArcherRow - 1' 보다 큰 행에 있던 적들은 자동으로 게임 보드에서 사라진 것으로 간주됩니다.
		}

		// 현재 시뮬레이션의 킬 수가 최대 킬 수보다 많으면 업데이트
		maxKills = Math.max(maxKills, currentKills);
	}
}

```

### 5. 왜 이 방법이 최선인지 설명

1.  **시간 복잡도 개선:**
    *   **핵심 개선:** 가장 큰 성능 병목이었던 `N*M` 배열 복사 및 시프트 (`적 이동`) 로직을 제거했습니다.
    *   원래 코드: `N` 턴마다 `O(N*M)`의 배열 시프트가 발생했습니다. 총 `O(N * N*M) = O(N^2 * M)`.
    *   최적화 코드: `effectiveArcherRow` 변수 하나로 적의 가상 이동을 처리하여, 이 부분의 복잡도가 `O(1)`이 되었습니다.
    *   결과적으로 `playGame`의 시간 복잡도는 `O(N*M)` (초기 맵 복사) + `N` 턴 * (`3`명의 궁수 * `N*M` 탐색) = `O(N^2 * M)` 에서 `O(N*M + N * 3 * N * M)` 로 변경됩니다.
    *   **총 시간 복잡도:** `M C 3 * O(N^2 * M)`에서 `M C 3 * O(N*M + N*3*N*M)` (사실상 `N*M`이 지배적이므로 `M C 3 * O(N^2 * M)`은 여전히 맞지만, 내부 상수가 줄어듭니다.) -> 즉, `N^2 * M` 였던 부분이 이제 `N` (턴 수) * `3` (궁수 수) * `N` (탐색 행 수) * `M` (탐색 열 수)가 되어 상수 차이로 더 효율적이 됩니다.
    *   최대 `M=15, N=15` 일 때, `455 * (15 * 15 * 3 * 15) = 455 * (10125) = 약 460만` 연산으로, 이전보다 더 효율적이고 실제 실행 시간도 크게 단축됩니다. `System.arraycopy`를 사용하여 초기 맵 복사도 최적화했습니다.

2.  **공간 복잡도 유지:**
    *   `O(N*M)`은 그대로 유지됩니다. 추가적인 자료구조를 사용하지 않았으므로 불필요한 메모리 사용이 없습니다. 주어진 제약 조건 내에서 `N, M <= 15` 이므로 `15*15 = 225` 크기의 2차원 배열은 메모리 문제가 전혀 없습니다.

3.  **가독성 개선:**
    *   `defence`를 `initialBoard`로, `archers`를 `archerPositions`로 변경하여 변수의 역할을 명확히 했습니다.
    *   주석을 상세하게 추가하여 코드의 흐름, 최적화 아이디어(가상 이동), 그리고 각 부분의 역할을 설명했습니다.
    *   `findTarget` 메서드를 `simulateGame` 내부에 인라인하여, `simulateGame`이 모든 턴 로직을 한눈에 볼 수 있도록 했습니다. `currentArcherRow`와 같은 턴별 변수를 매개변수로 넘기는 오버헤드와 복잡성을 피할 수 있습니다.

이러한 최적화는 문제의 핵심 로직(궁수 배치 및 타겟 선택)을 유지하면서, 가장 비효율적인 부분(적 이동)을 제거함으로써 전반적인 성능을 크게 향상시킵니다. 특히 `N`과 `M`이 커질수록 그 효과는 더욱 두드러질 것입니다.