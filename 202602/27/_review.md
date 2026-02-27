# 코드 리뷰

**파일**: 202602/27/BOJ_1920.java

제출하신 BOJ_1920 알고리즘 풀이에 대해 비판적이지만 건설적인 관점에서 리뷰해 드리겠습니다.

---

### 1. 시간/공간 복잡도

*   **시간 복잡도:**
    *   `N`개의 정수를 입력받아 배열 `A`에 저장: `O(N)`
    *   `Arrays.sort(A)`: Java의 `Arrays.sort`는 Dual-Pivot Quicksort를 사용하며, 평균 및 최악의 경우 `O(N log N)`의 시간 복잡도를 가집니다.
    *   `M`개의 타겟 숫자에 대해 `binarySearch` 호출: 각 `binarySearch` 호출은 정렬된 배열 `A`에 대해 `O(log N)`의 시간 복잡도를 가집니다. 따라서 `M`번 호출하면 `O(M log N)`이 됩니다.
    *   **총 시간 복잡도:** `O(N log N + M log N)`
    *   `N`과 `M`이 최대 100,000일 때, `10^5 * log(10^5)`는 약 `10^5 * 17`이므로, `1.7 * 10^6` 정도의 연산 횟수가 예상됩니다. 이는 일반적인 1초 시간 제한(대략 `10^8` 연산) 내에 충분히 통과할 수 있는 수준입니다.

*   **공간 복잡도:**
    *   배열 `A`에 `N`개의 정수를 저장: `O(N)`
    *   `Scanner` 객체 및 스택 프레임 등은 무시할 수 있습니다.
    *   `Arrays.sort()`는 In-place 정렬이지만, 재귀 호출을 위한 스택 공간으로 `O(log N)` 정도가 필요할 수 있습니다.
    *   **총 공간 복잡도:** `O(N)`
    *   `N`이 최대 100,000일 때, `10^5`개의 `int` (4바이트)를 저장하는 데 약 0.4MB가 소요됩니다. 이는 일반적인 메모리 제한(256MB 또는 512MB) 내에 충분합니다.

---

### 2. 논리 오류 있는지

*   **`binarySearch` 함수:** 전형적인 이진 탐색 구현으로 논리적인 오류는 없습니다.
    *   `while (left <= right)` 조건은 `left`와 `right`가 같아지는 경우(배열에 하나의 원소만 남았을 때)까지 올바르게 탐색을 진행합니다.
    *   `mid = (left + right) / 2` 계산은 중간 인덱스를 올바르게 찾습니다.
    *   `arr[mid] == target`, `arr[mid] < target`, `arr[mid] > target`에 따른 `left`와 `right`의 조정 역시 표준적인 이진 탐색 로직을 따릅니다.
*   **전체 로직:** 주어진 문제를 해결하기 위한 "정렬 후 이진 탐색" 전략은 매우 적절하며, 구현 또한 해당 전략을 정확히 따르고 있습니다.
*   **결론:** 논리적 오류는 없습니다.

---

### 3. 더 효율적인 방법

현재 솔루션은 이미 효율적인 편이지만, 이론적으로 더 빠른 방법이 존재합니다.

*   **HashSet 사용 (해싱):**
    *   **전략:** `N`개의 숫자를 `HashSet`에 저장합니다. 그리고 `M`개의 타겟 숫자에 대해 `HashSet.contains()` 메서드를 사용하여 존재 여부를 확인합니다.
    *   **시간 복잡도:**
        *   `N`개의 숫자를 `HashSet`에 추가: 평균 `O(N)` (각 삽입이 평균 `O(1)`)
        *   `M`개의 타겟 숫자를 `HashSet`에서 검색: 평균 `O(M)` (각 검색이 평균 `O(1)`)
        *   **총 평균 시간 복잡도:** `O(N + M)`
    *   **공간 복잡도:** `HashSet`에 `N`개의 숫자를 저장하므로 `O(N)`
    *   **장점:** `O(N + M)`은 `O(N log N + M log N)`보다 이론적으로 더 빠릅니다. `log N`이 약 17이므로, 약 17배 정도의 연산 수 차이가 발생할 수 있습니다. 특히 `N`과 `M`이 매우 클 때 유리합니다.
    *   **단점:** `HashSet`은 내부적으로 해시 테이블을 관리하는 오버헤드가 있어서, `N`이 작을 때는 정렬+이진탐색보다 실측 시간이 더 오래 걸릴 수도 있습니다. 최악의 경우 해시 충돌로 인해 `O(N^2)` 또는 `O(NM)`이 될 수도 있지만, Java의 `HashSet`은 잘 구현되어 있어 일반적인 경우에는 평균 시간 복잡도를 따릅니다.

    **HashSet을 사용한 코드 예시:**
    ```java
    import java.util.*;

    public class BOJ_1920_HashSet {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            int n = sc.nextInt();
            Set<Integer> A = new HashSet<>(); // HashSet 사용
            for (int i = 0; i < n; i++) {
                A.add(sc.nextInt());
            }

            int m = sc.nextInt();
            for (int i = 0; i < m; i++) {
                int target = sc.nextInt();
                if (A.contains(target)) { // HashSet의 contains 메서드 사용
                    System.out.println(1);
                } else {
                    System.out.println(0);
                }
            }

            sc.close();
        }
    }
    ```

---

### 4. 놓친 엣지 케이스

제출하신 코드는 대부분의 엣지 케이스를 잘 처리합니다.

*   **빈 배열 (N=0):** 문제의 제약 조건에 N은 1 이상으로 주어지는 경우가 많지만, 만약 N=0이라면:
    *   `new int[0]`은 유효한 배열을 생성합니다.
    *   `Arrays.sort(A)`는 빈 배열에 대해 아무 작업도 하지 않습니다.
    *   `binarySearch(A, target)` 호출 시 `arr.length - 1`은 `-1`이 되고, `left=0`, `right=-1`이 되어 `while (left <= right)` 조건이 바로 거짓이 되므로 `0`을 올바르게 반환합니다.
*   **단일 원소 배열 (N=1):** `binarySearch`가 올바르게 작동합니다.
*   **타겟이 배열의 최소값/최대값인 경우:** 올바르게 탐색됩니다.
*   **배열의 모든 원소가 동일한 경우:** 올바르게 작동합니다.
*   **`mid = (left + right) / 2` 오버플로우:**
    *   `left`와 `right`가 모두 매우 큰 `int` 값일 경우 `left + right`가 `Integer.MAX_VALUE`를 초과하여 오버플로우가 발생할 수 있습니다.
    *   하지만 배열의 인덱스는 `0`부터 `arr.length - 1`까지이며, `N`의 최대값이 100,000이므로 `left`와 `right`는 최대 99,999 정도입니다. 따라서 `left + right`는 `2 * 99,999 = 199,998`로 `Integer.MAX_VALUE`를 훨씬 밑돌기 때문에 오버플로우 걱정은 없습니다.
    *   **안전한 계산법 (습관화 권장):** `mid = left + (right - left) / 2`를 사용하면 오버플로우를 완전히 방지할 수 있습니다.

**결론:** 놓친 주요 엣지 케이스는 없습니다. 코드가 견고합니다.

---

### 5. 코드 개선점

현재 코드는 매우 깔끔하고 잘 작동하지만, 몇 가지 개선점을 제안할 수 있습니다.

1.  **입출력 속도 개선 (필수 아님, 하지만 경쟁 프로그래밍에서 유용):**
    *   `Scanner`와 `System.out.println`은 대량의 입출력 시 성능 저하의 원인이 될 수 있습니다. `N`과 `M`이 10만 개이므로, 출력 라인이 많아질 경우 `BufferedWriter`나 `StringBuilder`를 사용하는 것이 좋습니다.
    *   **`StringBuilder` 사용 예시:**
        ```java
        import java.util.*;
        import java.io.*; // BufferedReader, BufferedWriter 사용 시 필요

        public class BOJ_1920 {
            public static void main(String[] args) throws IOException { // throws IOException 추가
                // Scanner 대신 BufferedReader 사용
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                // 결과 저장을 위한 StringBuilder
                StringBuilder sb = new StringBuilder();

                int n = Integer.parseInt(br.readLine());
                int[] A = new int[n];
                StringTokenizer st = new StringTokenizer(br.readLine()); // 한 줄로 입력될 경우
                for (int i = 0; i < n; i++) {
                    A[i] = Integer.parseInt(st.nextToken());
                }
                Arrays.sort(A);

                int m = Integer.parseInt(br.readLine());
                st = new StringTokenizer(br.readLine()); // 한 줄로 입력될 경우
                for (int i = 0; i < m; i++) {
                    int target = Integer.parseInt(st.nextToken());
                    sb.append(binarySearch(A, target)).append("\n"); // 결과 누적
                }

                System.out.print(sb.toString()); // 한 번에 출력
                br.close();
                // sc.close(); 는 필요 없음 (사용하지 않았으므로)
            }

            // ... binarySearch 함수는 동일 ...
            static int binarySearch(int[] arr, int target) {
                int left = 0;
                int right = arr.length - 1;

                while (left <= right) {
                    int mid = (left + right) / 2; // 혹은 left + (right - left) / 2
                    if (arr[mid] == target) {
                        return 1;
                    } else if (arr[mid] < target) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                return 0;
            }
        }
        ```
        *주의: `br.readLine()`은 한 줄을 통째로 읽으므로, 숫자들이 공백으로 구분되어 한 줄에 입력되는 경우 `StringTokenizer`를 사용해야 합니다. 각 숫자가 한 줄에 하나씩 입력된다면 `Integer.parseInt(br.readLine())`을 반복해서 사용하면 됩니다.*

2.  **`mid` 계산 방식 (습관화):**
    *   `int mid = left + (right - left) / 2;`
    *   위에서 언급했듯이, 잠재적 오버플로우 방지 및 안전한 코드 작성을 위한 좋은 습관입니다. 이 문제에서는 필수적이지 않지만, 알아두면 좋습니다.

3.  **내장 이진 탐색 함수 활용:**
    *   직접 `binarySearch` 함수를 구현하는 것은 학습에 매우 좋지만, 실제 문제 풀이에서는 Java 라이브러리의 `Arrays.binarySearch()`를 활용하여 코드를 더 간결하게 만들 수 있습니다.
    *   `Arrays.binarySearch(A, target)`는 타겟을 찾으면 해당 인덱스를 반환하고, 찾지 못하면 음수를 반환합니다.
    *   **사용 예시:**
        ```java
        // ... (main 함수 내에서) ...
        for (int i = 0; i < m; i++) {
            int target = sc.nextInt();
            if (Arrays.binarySearch(A, target) >= 0) { // 0 이상의 값이 반환되면 찾은 것
                System.out.println(1);
            } else {
                System.out.println(0);
            }
        }
        // ...
        ```
    *   이렇게 하면 `binarySearch` 메소드를 따로 구현할 필요가 없어 코드가 줄어듭니다.

---

### 총평

제출하신 코드는 BOJ 1920번 문제를 해결하기 위한 **훌륭하고 견고한 솔루션**입니다. 기본적인 알고리즘 지식(정렬 및 이진 탐색)을 정확히 이해하고 구현했습니다. 시간/공간 복잡도 측면에서도 해당 문제의 제약 조건에 적합하며, 논리적 오류나 놓친 엣지 케이스도 없습니다.

경쟁 프로그래밍 환경에서 아주 미세한 성능 최적화나 코드 간결성을 추구한다면 `HashSet` 사용, 빠른 입출력, `Arrays.binarySearch` 활용 등을 고려할 수 있지만, 현재 코드만으로도 문제 해결에는 전혀 지장이 없습니다. 잘하셨습니다!