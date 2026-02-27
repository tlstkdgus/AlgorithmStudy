# 최적화 분석

주어진 코드는 N개의 숫자를 배열에 저장하고 정렬한 뒤, M개의 숫자에 대해 이진 탐색을 수행하여 존재 여부를 확인하는 방식입니다. 이 방식은 `N log N` (정렬) + `M log N` (M번의 이진 탐색)의 시간 복잡도를 가집니다.

최적화를 위해 다음 사항들을 고려하겠습니다.

---

### 1. 시간 복잡도 개선 방법

*   **현재 문제점**: `N log N`의 정렬 시간과 `M`번의 이진 탐색(`M log N`) 시간이 소요됩니다. `N`과 `M`이 각각 100,000까지 주어질 수 있으므로, 총 `(100,000 + 100,000) * log(100,000)` 대략 `200,000 * 17` = `3,400,000` 연산이 발생합니다. 이는 충분히 빠른 시간 안에 통과할 수 있는 수준이지만, 더 빠른 방법이 있습니다.
*   **개선 방법 (HashSet 사용)**:
    *   `HashSet`은 원소를 저장하고(`add`), 원소의 존재 여부를 확인(`contains`)하는 데 평균적으로 `O(1)`의 시간 복잡도를 가집니다.
    *   따라서, N개의 숫자를 `HashSet`에 저장하는 데 `N * O(1) = O(N)` 시간이 소요됩니다.
    *   M개의 숫자에 대해 `HashSet`에 존재하는지 확인하는 데 `M * O(1) = O(M)` 시간이 소요됩니다.
    *   **총 시간 복잡도**: `O(N + M)` (평균적으로).
    *   이는 기존의 `O(N log N + M log N)`보다 훨씬 효율적입니다. `100,000 + 100,000 = 200,000` 연산으로, 기존 방식보다 약 17배 빠릅니다.
*   **개선 방법 (빠른 입출력 사용)**:
    *   `Scanner` 대신 `BufferedReader`와 `StringTokenizer`를 사용하여 입력 처리 속도를 높입니다.
    *   `System.out.println` 대신 `StringBuilder`를 사용하여 출력 처리 속도를 높입니다. `M`번의 `println` 호출은 많은 I/O 오버헤드를 발생시킬 수 있습니다.

### 2. 공간 복잡도 개선 방법

*   **현재 문제점**: `int[] A` 배열에 N개의 숫자를 저장하므로 `O(N)`의 공간 복잡도를 가집니다.
*   **개선 방법 (HashSet 사용)**:
    *   `HashSet<Integer>`에 N개의 숫자를 저장하므로 여전히 `O(N)`의 공간 복잡도를 가집니다.
    *   `Integer` 객체는 `int` 원시 타입보다 더 많은 메모리를 사용하지만, N의 최대값이 100,000이므로 `O(N)` 공간은 문제 없습니다. (약 `100,000 * (4 바이트 + 객체 오버헤드)` 정도)
*   **결론**: 이 문제에서 공간 복잡도를 `O(N)` 미만으로 줄이는 것은 사실상 불가능합니다. N개의 숫자를 어딘가에 저장해야 M개의 질의에 답할 수 있기 때문입니다. 따라서, 공간 복잡도는 `O(N)`으로 유지하되, 시간 복잡도를 최적화하는 데 집중합니다.

### 3. 가독성 개선

*   **변수명**: `sc`, `A`, `n`, `m` 등 짧은 변수명을 사용했지만, 필요에 따라 더 명확한 이름을 사용할 수 있습니다. (예: `scanner`, `numbersArray`, `numCount`, `targetCount`). 다만, 경쟁 프로그래밍에서는 짧은 변수명이 흔합니다.
*   **사용자 정의 `binarySearch` 함수 대신 표준 라이브러리 활용**: `Arrays.binarySearch()`를 사용하면 직접 이진 탐색 로직을 구현할 필요가 없어 코드가 간결해지고 잠재적인 버그를 줄일 수 있습니다. (단, `HashSet` 접근 방식에서는 이 함수가 필요 없어집니다.)
*   **`HashSet` 사용 시**: `numbers.contains(target)`과 같이 자연어와 유사한 메소드를 사용하므로 코드가 직관적입니다.

### 4. 최종 최적화된 코드 (HashSet + 빠른 입출력)

가장 큰 성능 개선은 `HashSet`을 통한 시간 복잡도 `O(N+M)` 달성입니다. 여기에 빠른 입출력을 적용하면 효율을 극대화할 수 있습니다.

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.StringTokenizer;

public class BOJ_1920_Optimized {
    public static void main(String[] args) throws IOException {
        // 1. 빠른 입력을 위한 BufferedReader 및 StringTokenizer
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 2. 빠른 출력을 위한 StringBuilder
        StringBuilder sb = new StringBuilder();

        // N개의 숫자를 읽어 HashSet에 저장
        int N = Integer.parseInt(br.readLine());
        // 3. 시간 복잡도 개선을 위해 HashSet 사용
        HashSet<Integer> numbers = new HashSet<>();
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numbers.add(Integer.parseInt(st.nextToken()));
        }

        // M개의 숫자에 대해 존재 여부 확인
        int M = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine()); // 새 라인을 읽기 위해 StringTokenizer 재할당
        for (int i = 0; i < M; i++) {
            int target = Integer.parseInt(st.nextToken());
            if (numbers.contains(target)) { // O(1) 평균 시간 복잡도
                sb.append(1).append('\n');
            } else {
                sb.append(0).append('\n');
            }
        }

        // 최종 결과 출력
        System.out.print(sb.toString());
        
        // 리소스 해제
        br.close();
    }
}
```

---

### 5. 왜 이 방법이 최선인지 설명

이 `HashSet`을 사용한 방법이 최선인 이유는 다음과 같습니다:

1.  **압도적인 시간 복잡도**:
    *   기존 방식은 `O(N log N + M log N)`의 시간 복잡도를 가집니다. (N을 정렬하는 시간 + M번의 이진 탐색 시간)
    *   `HashSet` 방식은 평균적으로 `O(N + M)`의 시간 복잡도를 가집니다. (N개의 요소를 삽입하는 시간 + M개의 요소를 탐색하는 시간)
    *   `log N`은 `N`이 100,000일 때 약 17 정도이므로, `N + M`은 `(N + M) log N`보다 훨씬 작습니다. 이는 대량의 데이터 처리 시 성능 차이를 크게 벌립니다.
2.  **간결한 코드와 높은 가독성**:
    *   `HashSet`은 내부적으로 해싱을 통해 빠르게 원소를 관리하므로, 개발자가 복잡한 정렬 알고리즘이나 이진 탐색 로직을 직접 구현할 필요 없이 `add()`와 `contains()` 메소드만으로 문제를 해결할 수 있습니다. 이는 코드의 양을 줄이고 가독성을 높여줍니다.
3.  **적절한 공간 복잡도**:
    *   `O(N)`의 공간 복잡도를 가지며, N이 100,000일 때 충분히 허용 가능한 메모리 사용량입니다.
    *   이 문제의 특성상 N개의 숫자를 저장하지 않고 M개의 질의에 답하는 것은 불가능하므로, `O(N)`은 사실상 최적의 공간 활용입니다.
4.  **빠른 입출력 적용**:
    *   `BufferedReader`, `StringTokenizer`, `StringBuilder`를 사용하여 자바의 기본 입출력(`Scanner`, `System.out.println`)에서 발생할 수 있는 잠재적인 시간 초과 문제를 방지합니다. 이는 특히 `N`과 `M`이 클 때 중요합니다.

따라서, 평균적인 경우에서 `HashSet`을 활용한 `O(N+M)`의 시간 복잡도를 달성하고, 빠른 입출력을 적용하는 것이 이 문제에 대한 가장 효율적이고 실용적인 최적화 방법입니다.