# 최적화 분석

주어진 자바 코드는 "단어 수학" 문제(BOJ 1339)를 해결하기 위한 것으로, 각 알파벳에 0부터 9까지의 숫자를 할당하여 주어진 단어들의 합을 최대로 만드는 문제입니다. 현재 코드도 문제의 핵심 로직을 잘 구현하고 있지만, 몇 가지 최적화 및 가독성 개선의 여지가 있습니다.

---

### 1. 시간 복잡도 개선 방법

**현재 코드의 시간 복잡도:**

1.  **입력 처리 및 자리수 합 계산:**
    *   `N`개의 단어를 반복합니다.
    *   각 단어의 길이 `len`만큼 반복합니다. (`N` * `L_max`, 여기서 `L_max`는 최대 단어 길이)
    *   `Math.pow(10, ...)` 연산은 내부적으로 부동 소수점 연산을 포함하며, 정수 곱셈보다 느릴 수 있습니다. 이 부분이 반복문 내에서 자주 호출되므로 성능에 영향을 줄 수 있습니다.
2.  **정렬:**
    *   `Arrays.sort(alphabet)`: 알파벳 개수는 26개로 고정되어 있으므로, `O(26 log 26)`는 상수 시간 `O(1)`로 간주할 수 있습니다.
3.  **최종 합 계산:**
    *   `while(num > 0)` 루프는 최대 9번 실행되며, 그 안에 `for` 루프는 26번 실행됩니다. 이 역시 상수 시간 `O(1)`로 간주할 수 있습니다.

따라서 전체 시간 복잡도는 `O(N * L_max)` (여기서 `L_max`는 입력 단어 중 가장 긴 단어의 길이)에 `Math.pow` 연산의 오버헤드가 더해진 형태입니다.

**개선 방법:**

*   **`Math.pow` 제거:** `Math.pow`를 사용하는 대신, 10의 거듭제곱을 직접 계산하는 정수 연산을 사용합니다.
    *   단어를 역순으로 순회하면서 `currentPowerOf10` 변수를 1부터 시작하여 10씩 곱해나가면 됩니다.

**구체적인 코드 변경 예시:**

```java
// 변경 전:
// for(int j = 0; j < len; j++) {
//     int idx = word.charAt(j) - 'A';
//     alphabet[idx] += (int) Math.pow(10, len - 1 - j);
// }

// 변경 후:
int currentPowerOf10 = 1;
for(int j = word.length() - 1; j >= 0; j--) { // 단어를 역순으로 순회
    int charIndex = word.charAt(j) - 'A';
    placeValueSums[charIndex] += currentPowerOf10; // placeValueSums는 alphabet의 변경된 이름
    currentPowerOf10 *= 10;
}
```
이 변경을 통해 `N * L_max` 부분의 상수 인자를 줄여 실질적인 성능을 향상시킬 수 있습니다.

### 2. 공간 복잡도 개선 방법

**현재 코드의 공간 복잡도:**

1.  `alphabet` 배열: `int[26]`으로, 알파벳 개수가 26개로 고정되어 있으므로 상수 공간 `O(1)`을 사용합니다.
2.  `word` 변수: 단어의 길이에 비례하는 공간 `O(L_max)`을 사용합니다.
3.  `Scanner` 등 라이브러리 객체: 시스템에서 할당하는 공간입니다.

**개선 방법:**

이미 `alphabet` 배열의 크기가 고정되어 있어 `O(1)` 공간 복잡도를 가지므로, **별도의 공간 복잡도 개선은 불필요합니다.** 이 배열은 문제 해결을 위해 필수적으로 사용해야 하는 최소한의 공간입니다.

### 3. 가독성 개선

**현재 코드의 가독성:**

1.  **변수명:** `alphabet`이라는 변수명은 단순히 알파벳을 의미하는 것인지, 각 알파벳의 "가중치"나 "점수"를 의미하는 것인지 명확하지 않을 수 있습니다.
2.  **마지막 루프:**
    ```java
    int num = 9;
    while(num > 0) {
        for(int i = alphabet.length - 1; i >= 0; i--) {
            ans += num*alphabet[i];
            num--;
        }
    }
    ```
    이 부분은 의도치 않게 `num`이 너무 빨리 0이 되어 `for` 루프가 중간에 끊길 수 있습니다. (예: `alphabet` 배열에 5개의 유효한 숫자만 있고 나머지는 0인 경우, `num`이 4까지 감소한 후 `while` 루프가 종료되어 `alphabet`의 나머지 값들은 처리되지 않을 수 있음).
    올바른 의도는 정렬된 `alphabet` 배열의 큰 값부터 9, 8, 7...을 순서대로 할당하는 것이므로, 루프 구조를 더 명확하게 변경할 필요가 있습니다.

**개선 방법:**

1.  **변수명 개선:** `alphabet` 대신 `placeValueSums`나 `charWeights`와 같이 의미를 명확히 하는 이름을 사용합니다.
2.  **마지막 루프 개선:** 정렬된 배열을 역순으로 순회하면서 `currentDigit` 변수를 9부터 1씩 감소시키며 할당하고, 해당 `placeValueSums` 값이 0이면 더 이상 처리할 필요가 없으므로 `break`합니다.
3.  **코드 정리:** 불필요한 import문 (`Collections`)을 제거하고, `Scanner`를 사용 후 닫는(`sc.close()`) 습관을 들입니다.
4.  **주석 추가:** 코드의 핵심 로직에 대한 설명을 추가하여 이해도를 높입니다.

**구체적인 코드 변경 예시:**

```java
// 변경 전:
// int[] alphabet = new int[26];

// 변경 후:
// Stores the sum of place values for each character.
// E.g., for "ABC", A gets 100, B gets 10, C gets 1.
// If "DCA" also exists, A gets 1 (from "DCA") + 100 (from "ABC") = 101.
int[] placeValueSums = new int[26];


// 변경 전 (마지막 루프):
// int num = 9;
// while(num > 0) {
//     for(int i = alphabet.length - 1; i >= 0; i--) {
//         ans += num*alphabet[i];
//         num--;
//     }
// }

// 변경 후 (마지막 루프):
int currentDigit = 9;
// Iterate from the largest place value sum (end of sorted array)
// and assign decreasing digits.
for(int i = placeValueSums.length - 1; i >= 0; i--) {
    // Only assign digits to characters that actually appeared in words
    // (i.e., have a non-zero place value sum).
    if (placeValueSums[i] == 0) {
        // All remaining elements are also 0 (due to sorting).
        // No more characters to assign digits to.
        break;
    }
    ans += currentDigit * placeValueSums[i];
    currentDigit--;
    if (currentDigit < 0) { // 숫자가 0보다 작아지면 (즉, 0까지 다 할당했으면) 종료.
        break;
    }
}
```
`currentDigit < 0` 조건은 사실 `placeValueSums[i] == 0` 조건과 함께 처리될 수 있습니다. 0 이상의 `placeValueSums`를 가진 알파벳의 개수가 최대 10개이므로, `currentDigit`는 0까지 내려갈 수 있으며, `placeValueSums[i] == 0`을 만나면 자동 종료됩니다. 따라서 `if (currentDigit < 0)`는 생략 가능하지만, 명시적으로 넣어주는 것도 나쁘지 않습니다. 여기서는 자연스러운 흐름을 위해 `placeValueSums[i] == 0`으로 충분하다고 판단하여 제외하겠습니다.

### 4. 최종 최적화된 코드

```java
package backtracking;

import java.util.Arrays;
import java.util.Scanner;

public class BOJ_1339_Optimized {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // 단어의 개수

        int totalMaxSum = 0; // 최종 결과 (최대 합)

        // 각 알파벳(A-Z)이 가지는 자릿값의 합을 저장하는 배열.
        // 예를 들어 "GCF"와 "AC"가 주어졌다면:
        // 'G'는 100의 자리 (단어 GCF에서)
        // 'C'는 10의 자리 (단어 GCF에서) + 10의 자리 (단어 AC에서) = 20의 자리
        // 'F'는 1의 자리 (단어 GCF에서)
        // 'A'는 1의 자리 (단어 AC에서)
        // 이 배열에는 각 알파벳에 해당하는 자릿값들의 총합이 저장됩니다.
        int[] placeValueSums = new int[26];

        for (int i = 0; i < N; i++) {
            String word = sc.next();
            // Math.pow 대신 정수 곱셈으로 10의 거듭제곱을 계산하여 성능 개선
            int currentPowerOf10 = 1; // 1, 10, 100, 1000 ...
            // 단어의 뒤에서부터 앞으로 순회하여 각 문자의 자릿값을 더합니다.
            // 예: "ABC" -> C(1의 자리), B(10의 자리), A(100의 자리)
            for (int j = word.length() - 1; j >= 0; j--) {
                int charIndex = word.charAt(j) - 'A'; // 'A'를 0으로 하는 인덱스
                placeValueSums[charIndex] += currentPowerOf10;
                currentPowerOf10 *= 10;
            }
        }

        // 각 알파벳의 자릿값 합을 오름차순으로 정렬합니다.
        // 가장 큰 자릿값 합을 가진 알파벳에 가장 큰 숫자(9)를 할당하기 위함입니다.
        Arrays.sort(placeValueSums);

        int currentDigit = 9; // 할당할 숫자 (9부터 시작)

        // 정렬된 배열을 역순으로 순회하며, 각 알파벳의 자릿값 합에 해당하는 숫자를 곱하여 총합을 계산합니다.
        for (int i = placeValueSums.length - 1; i >= 0; i--) {
            // 자릿값 합이 0인 알파벳은 단어에 등장하지 않았거나, 이미 모든 유효한 숫자를 할당했습니다.
            // 정렬된 배열이므로, 0이 나오면 그 앞의 값들도 모두 0입니다.
            if (placeValueSums[i] == 0 || currentDigit < 0) { // currentDigit < 0은 보통 발생하지 않지만, 방어적으로 추가 가능.
                                                            // placeValueSums[i] == 0만으로도 충분.
                break;
            }
            totalMaxSum += currentDigit * placeValueSums[i];
            currentDigit--; // 다음 알파벳에는 1 감소된 숫자를 할당
        }

        System.out.println(totalMaxSum);
        sc.close(); // Scanner 자원 해제
    }
}
```

### 5. 왜 이 방법이 최선인지 설명

1.  **그리디 알고리즘의 적용:**
    *   이 문제는 각 알파벳에 할당될 숫자를 결정하는 문제입니다. 어떤 알파벳이 더 큰 숫자(9, 8, ...)를 받아야 가장 큰 합을 만들 수 있을까요?
    *   예를 들어 'A'가 100의 자리에 한 번, 10의 자리에 두 번 등장했다면 총 100 + 10 + 10 = 120의 가중치를 가집니다. 'B'가 1000의 자리에 한 번 등장했다면 1000의 가중치를 가집니다.
    *   당연히 가중치가 1000인 'B'가 가중치가 120인 'A'보다 더 큰 숫자를 받아야 전체 합이 커집니다.
    *   따라서 각 알파벳이 전체 단어들에서 차지하는 '자리값의 합'을 계산하고, 이 '자리값 합'이 큰 순서대로 9, 8, 7...의 숫자를 할당하는 **그리디(Greedy) 전략**이 최적의 해를 보장합니다. 이 최적화된 코드는 이 그리디 전략을 정확하게 구현하고 있습니다.

2.  **시간 복잡도 최적화 (`O(N * L_max)`)**:
    *   가장 큰 비중을 차지하는 `Math.pow` 연산을 제거하고 정수 곱셈으로 대체함으로써, 각 문자를 처리하는 데 걸리는 시간을 최소화했습니다.
    *   `N`개의 단어를 읽고 각 단어의 `L`개의 문자를 처리하는 것은 문제의 특성상 피할 수 없는 작업입니다. 따라서 `O(N * L_max)`는 입력 데이터를 모두 한 번씩 읽어야 하는 문제에서 달성할 수 있는 **이론적인 최저 시간 복잡도(Lower Bound)**에 가깝습니다. 정렬 및 최종 합 계산 부분은 알파벳 개수가 26으로 상수이므로 `O(1)`에 해당합니다.

3.  **공간 복잡도 최적화 (`O(1)`)**:
    *   `placeValueSums` 배열은 알파벳 개수에 해당하는 26개의 `int` 값을 저장하므로, 이는 상수 공간 `O(1)`입니다.
    *   이 배열은 각 알파벳의 가중치를 저장하기 위해 필수적이며, 더 적은 공간으로 이 정보를 저장하는 것은 불가능합니다. 따라서 **이론적인 최저 공간 복잡도**를 달성했다고 볼 수 있습니다. (문자열 `word`를 저장하는 공간은 `O(L_max)`이지만, 이는 입력 자체의 크기이므로 보통 제외하고 설명합니다.)

4.  **가독성 및 유지보수성**:
    *   의미가 명확한 변수명 (`placeValueSums`, `totalMaxSum`, `currentPowerOf10`, `currentDigit`)을 사용하여 코드의 의도를 쉽게 파악할 수 있습니다.
    *   `Math.pow`와 같은 부동 소수점 연산을 정수 연산으로 바꾸어 불필요한 오버헤드를 줄이고, 정수 연산의 정확성을 보장합니다.
    *   주석을 추가하여 복잡한 로직(자릿값 합 계산 방식, 그리디 전략)을 설명함으로써 코드 이해도를 높였습니다.
    *   불필요한 import문 제거 및 `Scanner` 자원 해제는 좋은 코딩 습관이며, 유지보수 측면에서 바람직합니다.

결론적으로, 이 최적화된 코드는 문제 해결을 위한 가장 효율적인 알고리즘(그리디)을 사용하며, 시간 및 공간 복잡도 측면에서 이론적인 최저 수준에 도달했고, 가독성까지 개선하여 **가장 최선의 방법**이라고 할 수 있습니다.