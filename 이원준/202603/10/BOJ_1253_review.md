# 코드 리뷰

**파일**: 이원준/202603/10/BOJ_1253.java

안녕하세요! 알고리즘 풀이에 대한 상세 리뷰 요청 주셔서 감사합니다. 제시해주신 코드는 백준 1253번 '좋다' 문제를 해결하기 위한 좋은 접근 방식입니다. 비판적이지만 건설적인 관점에서 분석해 드리겠습니다.

---

### 1. 시간/공간 복잡도

*   **시간 복잡도:** O(N^2)
    *   `Scanner`를 이용한 입력: O(N)
    *   `Arrays.sort(numArr)`: O(N log N) (주로 QuickSort 또는 MergeSort 기반)
    *   메인 `for` 루프: `N`번 반복합니다.
        *   내부 `while` 루프 (투 포인터): `left`와 `right` 포인터가 `N`번 이하로 이동하며 탐색합니다. 따라서 O(N) 입니다.
    *   총 시간 복잡도: O(N log N) + O(N * N) = **O(N^2)**
    *   N의 최대값이 2000이므로, N^2는 4,000,000번의 연산에 해당합니다. 이는 일반적인 1~2초 시간 제한 내에서 충분히 통과할 수 있는 수준입니다.

*   **공간 복잡도:** O(N)
    *   `numArr` 배열: O(N)의 공간을 사용합니다.
    *   그 외 변수들 (`N`, `count`, `i`, `target`, `left`, `right`, `sum`): O(1)의 추가 공간을 사용합니다.
    *   총 공간 복잡도: **O(N)**

### 2. 논리 오류 여부

제시된 코드는 문제의 요구사항을 정확하게 반영하고 있으며, 논리 오류는 발견되지 않았습니다.

*   **정렬(Sorting):** 투 포인터 기법을 사용하기 위해 배열을 정렬하는 것은 올바른 첫 단계입니다.
*   **타겟 설정:** 각 숫자를 돌아가며 `target`으로 설정하고, 해당 `target`이 다른 두 수의 합으로 표현될 수 있는지 확인하는 방식은 정확합니다.
*   **투 포인터 (Two Pointers):** 정렬된 배열에서 두 수의 합을 찾는 전형적인 투 포인터 알고리즘을 잘 적용했습니다.
    *   `sum == target`일 때 `count++` 후 `break` 하는 것은 하나의 `target`에 대해 한 번만 카운트하면 되므로 효율적이고 정확합니다.
    *   `sum < target`이면 `left`를 증가시켜 합을 키우고, `sum > target`이면 `right`를 감소시켜 합을 줄이는 방식도 표준입니다.
*   **'다른 두 수' 조건 처리:** 가장 중요한 부분 중 하나인데, `if (left == i)`와 `if (right == i)` 조건으로 현재 탐색 중인 `target` 숫자 자체를 두 수의 합에 포함시키지 않도록 처리한 점이 훌륭합니다.
    *   예를 들어, `numArr[i]`가 `numArr[left] + numArr[right]`가 되어야 하는데, `left == i`라면 `numArr[i]`를 자기 자신과 다른 수의 합으로 만드는 상황이 되어 문제의 '다른 두 수' 조건에 위배됩니다. 이를 `continue`로 건너뛰어 올바르게 처리하고 있습니다.

### 3. 더 효율적인 방법

현재 구현하신 O(N^2) 시간 복잡도는 해당 문제의 N(최대 2000) 제약 조건 하에서 **사실상 가장 효율적인 방법**으로 간주됩니다.

*   **O(N log N) 시도:**
    만약 N^2보다 더 빠른 O(N log N) 해법을 고려한다면, 정렬 후 각 `target`에 대해 `numArr[left] + numArr[right] = target`을 만족하는 `right`를 이진 탐색으로 찾는 방식을 생각해볼 수 있습니다. 하지만 이 경우에도 `left`를 고정하고 `right`를 찾으려면 `target - numArr[left]`를 이진 탐색해야 하는데, 이는 `N * N * log N`이 되어 현재 O(N^2)보다 더 느려집니다.
    투 포인터는 정렬된 배열에서 두 숫자의 합을 찾는 데 있어 N번의 탐색에 O(N)이 걸리므로, 총 N^2가 최적입니다.

따라서, 이 문제에 대해 현재 코드보다 유의미하게 더 빠른 시간 복잡도를 가진 알고리즘은 찾기 어렵습니다.

### 4. 놓친 엣지 케이스

제시된 코드는 대부분의 엣지 케이스를 잘 처리합니다.

*   **N이 작을 때 (N=0, 1, 2):**
    *   N=0: `numArr` 생성 안 됨. (문제에서 N은 1 이상이 보장될 것)
    *   N=1: `for` 루프가 돌지 않거나 `while(left < right)`가 실행되지 않아 `count`는 0이 됩니다. (옳음)
    *   N=2: `left < right` 조건이 성립하지 않거나 `left == i` 또는 `right == i`로 인해 `continue`되어 `count`는 0이 됩니다. (옳음)
    *   (예: `[1, 2]`. `target=1`일 때 `left=0`이지만 `left==i`로 `left`는 1이 되고 `left<right` 조건에 맞지 않아 종료. `target=2`일 때 `right=1`이지만 `right==i`로 `right`는 0이 되고 `left<right` 조건에 맞지 않아 종료. 결과는 0.)
*   **모든 숫자가 같을 때 (예: `[0, 0, 0, 0, 0]`):**
    *   `numArr = [0, 0, 0, 0, 0]` (N=5)
    *   `target`이 `numArr[0]` (값 0)일 때, `left=0, right=4`.
        *   `left == i`이므로 `left++` (left=1).
        *   `left=1, right=4`. `sum = numArr[1] + numArr[4] = 0 + 0 = 0`.
        *   `sum == target`이므로 `count++` (`count=1`) 후 `break`.
    *   `target`이 `numArr[1]` (값 0)일 때, `left=0, right=4`.
        *   `sum = numArr[0] + numArr[2] = 0 + 0 = 0`. (`numArr[0]`와 `numArr[2]`는 `numArr[1]`이 아니므로 올바른 조합).
        *   `sum == target`이므로 `count++` (`count=2`) 후 `break`.
    *   이런 식으로 모든 0은 '좋은 수'로 판정되어 `count`가 N이 됩니다. 이는 올바른 동작입니다.
*   **음수 포함:** 정렬 알고리즘은 음수에도 잘 작동하며, 투 포인터 로직 역시 음수가 포함된 경우에도 문제없이 동작합니다.
*   **숫자 범위 및 오버플로우:** 문제에서 숫자의 절댓값이 10억을 넘지 않는다고 했으므로, 두 숫자의 합은 20억을 넘지 않아 `int` 타입으로 충분히 처리 가능합니다.

### 5. 코드 개선점

현재 코드는 기능적으로 완벽하고 효율도 좋지만, 몇 가지 사소한 개선점을 제안하자면 다음과 같습니다.

1.  **입력 처리 속도 개선 (Optional, Competitive Programming 습관):**
    *   `Scanner` 대신 `BufferedReader`를 사용하는 것이 대량의 입력 처리 시 더 빠릅니다. N=2000에서는 `Scanner`도 대부분 문제없지만, N이 더 크거나 시간 제한이 빡빡한 문제에서는 `BufferedReader`를 쓰는 것이 좋습니다.
    ```java
    import java.io.BufferedReader;
    import java.io.InputStreamReader;
    import java.io.IOException;
    // ...
    public static void main(String[] args) throws IOException { // IOException 추가
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] numArr = new int[N];
        String[] s = br.readLine().split(" ");
        for(int i = 0; i < N; i++) {
            numArr[i] = Integer.parseInt(s[i]);
        }
        // ...
    }
    ```

2.  **`left == i`와 `right == i` 조건의 가독성:**
    현재 코드는 정확하지만, `continue` 문이 반복문의 흐름을 건너뛰기 때문에, 특정 로직을 이해할 때 한 번 더 생각하게 만들 수 있습니다. 현재 방식도 좋지만, `if-else if` 구조를 조금 더 명시적으로 만들거나, `left`와 `right`를 움직인 후 다시 조건을 확인하는 방식으로 구현할 수도 있습니다.
    *   현재 코드가 `left`, `right`를 먼저 움직이고 `continue`로 다시 조건을 확인하는 방식이라 오히려 더 깔끔할 수 있습니다. 굳이 바꿀 필요는 없으나, '개선점'으로 고려될 수 있는 지점입니다.

**결론적으로, 제시해주신 코드는 매우 훌륭합니다.** 문제의 핵심 로직을 정확하게 파악하고 있으며, 효율적인 알고리즘(정렬 + 투 포인터)을 사용하여 시간/공간 복잡도 측면에서도 최적의 성능을 보여줍니다. 특별한 논리 오류나 놓친 엣지 케이스는 없으며, 사소한 입력 처리 속도 개선 외에는 크게 손볼 부분이 없습니다. 아주 잘 작성된 코드입니다!