# 최적화 분석

주어진 문제는 `N`개의 우리에 각각 `0`에서 `X`마리의 햄스터를 배치하여, `M`개의 조건(특정 구간의 햄스터 수 합계)을 만족하면서 전체 햄스터 수의 합을 최대로 만드는 문제입니다. 원본 코드는 완전 탐색(DFS)을 사용하여 모든 가능한 햄스터 배치 경우의 수를 확인하고 있습니다.

---

### 원본 코드 분석 및 문제점

**1. 시간 복잡도:**
*   `N`개의 우리 각각에 `0`부터 `X`까지 `X+1`가지의 선택지가 있습니다. 따라서 총 `(X+1)^N`가지의 배치 경우의 수가 존재합니다.
*   `dfs` 함수의 마지막 (기저 사례)에서, 모든 `M`개의 조건을 확인합니다. 각 조건을 확인하는 데 `R-L+1`번의 합계 계산이 필요하며, 최악의 경우 `N`에 비례합니다.
*   따라서 전체 시간 복잡도는 `O((X+1)^N * M * N)`입니다.
*   예시: `N=10, X=10, M=10`일 때, `11^10 * 10 * 10`은 약 `2.5 * 10^12` (2조 5천억)으로, 이는 제한 시간(보통 1초) 내에 절대 풀 수 없는 매우 큰 숫자입니다. 이 부분이 가장 큰 병목입니다.

**2. 공간 복잡도:**
*   `notes` 배열: `O(M * 3)`
*   `cages`, `bestCages`: `O(N)`
*   DFS 재귀 스택: 최악의 경우 `O(N)`
*   전체적으로 `O(M + N)`으로, 이 정도면 충분히 효율적입니다.

**3. 가독성:**
*   `notes[i][0]`, `notes[i][1]`, `notes[i][2]`와 같이 배열 인덱스로 접근하는 방식은 어떤 값이 `L`, `R`, `S`를 의미하는지 파악하기 어렵습니다. 별도의 클래스를 사용하면 가독성이 높아집니다.
*   변수명은 비교적 명확합니다.

---

### 최적화 목표

1.  **시간 복잡도 개선:** `(X+1)^N`을 줄이는 것은 불가능하더라도, 곱해지는 `M * N` 부분을 없애거나 대폭 줄여야 합니다. 이를 위해 가지치기(Pruning) 전략을 도입하고, 부분 합 계산을 최적화해야 합니다.
2.  **공간 복잡도 개선:** 현재로서는 크게 개선할 여지가 없습니다. `N`과 `M`에 비례하는 정보는 필수적으로 저장해야 합니다.
3.  **가독성 개선:** `Note` 클래스 도입 및 주석 추가.

---

### 최적화 방법

**1. 시간 복잡도 개선 상세**

*   **가지치기 (Pruning) - 조기 종료 조건 확인:**
    *   원본 코드는 모든 `N`개의 우리에 햄스터를 배치한 후에야 `M`개의 조건을 모두 확인합니다. 이는 너무 늦습니다.
    *   `cages[idx]`에 햄스터를 배치할 때, **해당 `idx`를 끝으로 하는 모든 조건 `(L, R=idx, S)`**을 즉시 확인합니다. 만약 이 조건 중 하나라도 만족하지 않으면, 현재의 `cages[idx]` 배치 및 그 이후의 모든 배치는 유효하지 않으므로 즉시 백트랙(return)합니다.
    *   이를 위해 `notes` 배열을 미리 `notesEndingAt[R]` (R값으로 그룹화된 노트 리스트) 형태로 재구성하여 특정 `idx`에서 확인해야 할 노트를 빠르게 찾을 수 있도록 합니다.

*   **부분 합 계산 최적화 (Prefix Sum):**
    *   조건 `(L, R, S)`를 확인할 때 `sum(cages[L]...cages[R])`를 계산하는 것은 `O(R-L+1)` 시간이 걸립니다.
    *   DFS를 진행하면서 `cages[idx]`를 결정할 때마다 `currentPrefixSum[idx] = currentPrefixSum[idx-1] + cages[idx]`와 같이 누적 합(Prefix Sum)을 계산하여 저장합니다.
    *   그러면 특정 구간 `[L, R]`의 합은 `currentPrefixSum[R] - currentPrefixSum[L-1]`로 `O(1)` 시간에 계산할 수 있습니다.

*   **최대 합계 기반 가지치기 (Optimization Pruning):**
    *   현재까지의 햄스터 합 `currentTotalSum`과 앞으로 남은 우리 `(N - idx + 1)`에 최대로 `X`마리씩 햄스터를 넣었을 때의 예상 최대 합 `currentTotalSum + (N - idx + 1) * X`가 이미 `maxSum`보다 작다면, 이 경로는 절대 `maxSum`을 갱신할 수 없으므로 탐색할 필요가 없습니다. 즉시 백트랙합니다.

**2. 공간 복잡도 개선 상세**
*   `notesEndingAt` 리스트 배열을 사용하면 `notes` 배열이 `Note` 객체들로 구성됩니다. 이는 기존 `M*3` 크기 배열을 `M`개의 `Note` 객체와 `N`개의 `ArrayList` 레퍼런스로 재구성하는 것으로, 본질적인 공간 복잡도는 `O(M+N)`으로 동일합니다.
*   `currentPrefixSum` 배열은 `O(N)`의 추가 공간을 사용하지만, 이는 시간 복잡도 개선에 필수적입니다.

**3. 가독성 개선 상세**
*   `Note` 클래스를 정의하여 `L, R, S`를 명확하게 표현합니다.
*   코드에 적절한 주석을 추가하여 로직을 설명합니다.

---

### 최종 최적화된 코드

```java
package com.ssafy.swea;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SWEA_8275_Optimized {
    static int N, X, M;
    
    // 가독성 개선을 위한 Note 클래스 정의
    static class Note {
        int L, R, S;
        Note(int l, int r, int s) {
            this.L = l;
            this.R = r;
            this.S = s;
        }
    }
    
    // R 값을 기준으로 노트를 그룹화하여 빠른 접근을 위한 리스트 배열
    // notesEndingAt[r]에는 R값이 r인 모든 Note 객체들이 저장됩니다.
    static List<Note>[] notesEndingAt; 

    static int[] cages; // 현재 DFS 경로에서 각 우리에 배치된 햄스터 수
    static int[] currentPrefixSum; // cages 배열의 누적 합 (Prefix Sum)
                                  // currentPrefixSum[k] = cages[1] + ... + cages[k]
    
    static int[] bestCages; // 찾은 최대 합을 만드는 햄스터 배치
    static int maxSum;     // 찾은 최대 햄스터 합

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        
        for(int tc = 1; tc <= T; tc++) {
            N = sc.nextInt();
            X = sc.nextInt();
            M = sc.nextInt();
            
            // 각 테스트 케이스마다 notesEndingAt 초기화
            notesEndingAt = new ArrayList[N + 1];
            for (int i = 0; i <= N; i++) {
                notesEndingAt[i] = new ArrayList<>();
            }

            // M개의 조건을 읽어와 notesEndingAt에 저장
            // 조건의 R값(오른쪽 끝 인덱스)을 기준으로 그룹화
            for(int i = 0; i < M; i++) {
                int l = sc.nextInt();
                int r = sc.nextInt();
                int s = sc.nextInt();
                notesEndingAt[r].add(new Note(l, r, s)); 
            }
            
            cages = new int[N + 1];
            currentPrefixSum = new int[N + 1]; // currentPrefixSum[0]은 0으로 초기화됨
            bestCages = new int[N + 1];
            maxSum = -1; // 최대 합을 -1로 초기화하여, 유효한 해가 없는 경우를 표현
            
            // DFS 시작: 첫 번째 우리(인덱스 1)부터, 현재까지의 총합 0
            dfs(1, 0); 
            
            System.out.print("#"+tc);
            if(maxSum == -1) {
                System.out.println(" -1"); // 유효한 해가 없는 경우 -1 출력
            } else {
                for(int i = 1; i <= N; i++) {
                    System.out.print(" "+bestCages[i]);
                }
                System.out.println();
            }
        }
        sc.close();
    }
    
    /**
     * DFS를 통해 햄스터 배치를 탐색하는 함수
     * @param idx 현재 배치할 우리 번호 (1부터 N까지)
     * @param currentTotalSum cages[1]부터 cages[idx-1]까지의 햄스터 총 합
     */
    static void dfs(int idx, int currentTotalSum) {
        // 가지치기 1: 현재까지의 합과 남은 우리에 최대로 X마리를 넣어도 maxSum을 넘지 못한다면 백트랙
        // (N - idx + 1)은 현재 idx를 포함하여 남은 우리의 개수
        if (currentTotalSum + (N - idx + 1) * X < maxSum) {
            return; 
        }

        // 기저 사례: 모든 우리에 햄스터 배치가 완료된 경우 (idx가 N+1이 됨)
        if (idx == N + 1) {
            // 이 시점까지 도달했다는 것은 모든 제약 조건을 만족했다는 의미
            // 따라서 현재 배치의 총합이 maxSum보다 크면 갱신
            if (currentTotalSum > maxSum) {
                maxSum = currentTotalSum;
                // 현재 cages 배열을 bestCages에 복사
                System.arraycopy(cages, 0, bestCages, 0, N + 1);
            }
            return;
        }
        
        // 현재 우리(cages[idx])에 0부터 X마리까지 햄스터를 배치 시도
        for (int i = 0; i <= X; i++) {
            cages[idx] = i; // 햄스터 배치
            // Prefix Sum 업데이트: 현재 우리까지의 누적 합 계산
            currentPrefixSum[idx] = currentPrefixSum[idx-1] + i;
            int newTotalSum = currentTotalSum + i; // 전체 햄스터 합 업데이트
            
            boolean isValidPlacement = true;
            
            // 가지치기 2: 현재 우리(idx)를 R값으로 하는 모든 조건을 검사
            // 이 검사는 currentPrefixSum을 이용해 O(1) 시간에 이루어집니다.
            for (Note note : notesEndingAt[idx]) {
                // [note.L, note.R] 구간의 합 계산 (note.R은 현재 idx와 동일)
                int partSum = currentPrefixSum[note.R] - currentPrefixSum[note.L - 1];
                
                // 만약 구간 합이 조건 S와 다르면, 이 배치는 유효하지 않으므로 백트랙
                if (partSum != note.S) {
                    isValidPlacement = false;
                    break;
                }
            }
            
            // 현재 배치가 모든 조건을 만족한다면, 다음 우리로 DFS 탐색 진행
            if (isValidPlacement) {
                dfs(idx + 1, newTotalSum);
            }
        }
    }
}
```

---

### 왜 이 방법이 최선인가?

1.  **시간 복잡도 개선 (핵심):**
    *   원본 코드의 `O((X+1)^N * M * N)`에서 `M * N` 부분이 거의 제거되었습니다.
    *   `partSum` 계산은 `currentPrefixSum`을 이용하여 `O(1)`이 됩니다.
    *   조건 확인은 `notesEndingAt[idx]`에 있는 노트 수에 비례하며, 각 `idx`마다 `X+1`번 수행됩니다. 총 `M`개의 노트가 `N`개의 `R`값에 분산되므로, 평균적으로 `M/N`개 정도의 노트를 확인합니다.
    *   가장 중요한 것은, `dfs` 도중에 **조건 위반을 발견하면 즉시 가지치기(백트랙)**를 수행하여 불필요한 탐색을 기하급수적으로 줄인다는 점입니다. 이는 전체 탐색 공간 `(X+1)^N`을 효과적으로 축소시킵니다.
    *   `if (currentTotalSum + (N - idx + 1) * X < maxSum)`와 같은 상한선 가지치기는 최적화 문제에서 성능 향상에 매우 큰 기여를 합니다.
    *   이러한 접근 방식은 NP-hard 문제(특정 합계 만족 문제)에서 `N`과 `X`가 비교적 작을 때 가장 효과적인 방법 중 하나인 **백트래킹 + 공격적인 가지치기** 전략입니다.

2.  **공간 복잡도:**
    *   `currentPrefixSum` 배열이 `O(N)`만큼 추가되었고, `notesEndingAt`은 `O(M+N)`의 공간을 사용하지만, 이는 원래 `O(M)`이었던 `notes` 배열을 재구성한 것이므로, 본질적으로는 `O(M+N)`으로 동일합니다. 효율적인 시간 복잡도를 위해 필요한 최소한의 추가 공간입니다.

3.  **가독성:**
    *   `Note` 클래스 도입으로 코드의 의미가 훨씬 명확해졌습니다.
    *   `notesEndingAt`과 `currentPrefixSum`과 같은 변수명은 그 목적을 잘 나타내며, 주석을 통해 각 가지치기 조건의 역할이 명확히 설명되었습니다.

이 최적화된 방법은 주어진 제약 조건(`N`, `X`, `M`의 최대값) 하에서 `SWEA_8275` 문제를 효율적으로 해결하기 위한 표준적인 백트래킹 및 가지치기 기법을 적용한 결과이며, 대부분의 경우 시간 제한 내에 답을 찾을 수 있을 것입니다.