# 최적화 분석

주어진 코드는 배열을 이용한 최소 힙(Min-Heap) 구현입니다. 최소 힙은 데이터를 삽입하고(insert) 가장 작은 값을 삭제(delete min)하는 연산을 효율적으로 수행하는 데 최적화된 자료구조입니다. 각 연산의 시간 복잡도는 $O(\log N)$입니다.

이 코드는 이미 최소 힙의 표준적인 배열 구현 방식을 따르고 있으며, 입출력 성능을 위해 `BufferedReader`와 `StringBuilder`를 사용하는 등 기본적인 최적화는 되어 있습니다. 따라서 **시간 복잡도와 공간 복잡도 측면에서 근본적인(점근적) 개선은 어렵습니다.** 최소 힙 연산 자체가 $O(\log N)$의 시간 복잡도를 가지기 때문입니다.

하지만, **가독성 개선** 및 **코드의 구조화**를 통해 유지보수성과 이해도를 높일 수 있으며, 이는 넓은 의미의 "최적화"로 볼 수 있습니다. 또한, 미세한 상수 시간 개선을 위한 리팩토링도 고려할 수 있습니다.

---

### 1. 시간 복잡도 개선 방법

*   **현재 상태**: 각 삽입(`x > 0`) 및 삭제(`x = 0`) 연산은 $O(\log N)$의 시간 복잡도를 가집니다. 총 $N$번의 연산이 수행되므로 전체 시간 복잡도는 $O(N \log N)$입니다. 이는 비교 기반 힙(comparison-based heap)이 달성할 수 있는 최적의 시간 복잡도입니다.
*   **개선 여지**:
    *   **점근적($O()$) 개선은 불가능합니다.** 문제의 요구사항(삽입, 최소값 삭제)을 $O(\log N)$보다 빠르게 처리하는 비교 기반 힙은 존재하지 않습니다. 예를 들어, 특정 조건(정수 범위가 매우 작거나, 특정 패턴을 가지는 경우)에서는 다른 자료구조(예: Radix Heap)를 고려할 수도 있지만, 일반적인 정수 힙 문제에서는 이 방법이 최선입니다.
    *   **상수 시간 개선**:
        *   코드 내에서 반복적으로 사용되는 `siftUp` (삽입 시) 및 `siftDown` (삭제 시) 로직을 별도의 헬퍼 메서드로 분리하면, 코드가 더 명확해지고 잠재적으로 JIT 컴파일러가 더 효율적인 코드를 생성하는 데 도움이 될 수 있습니다 (매우 미미한 수준).
        *   `swap` 연산을 별도 메서드로 분리하는 것도 가독성을 높이고 코드 중복을 줄입니다.
        *   루프 내에서 불필요한 계산을 줄이거나 변수 할당을 최소화할 수 있지만, 현재 코드도 이미 잘 작성되어 있어 큰 개선은 어렵습니다.

### 2. 공간 복잡도 개선 방법

*   **현재 상태**: 힙을 저장하기 위한 `int[] heap = new int[N+1];` 배열을 사용하며, $N$은 최대 연산 횟수이자 힙에 저장될 수 있는 최대 원소의 개수를 의미합니다. 따라서 공간 복잡도는 $O(N)$입니다.
*   **개선 여지**:
    *   **점근적($O()$) 개선은 불가능합니다.** 힙에 최대 $N$개의 원소를 저장해야 하므로, $O(N)$ 공간은 필연적입니다.
    *   **미세한 개선**: Java의 `PriorityQueue`를 사용하면 내부적으로 `Object[]` 배열을 사용하므로 `Integer` 객체에 대한 오버헤드가 발생할 수 있습니다. 반면, `int[]` 배열을 직접 사용하는 것은 기본형(primitive type)을 저장하므로 박싱/언박싱 오버헤드가 없으며, 메모리 효율이 좋습니다. 현재 코드는 이미 이 장점을 활용하고 있으므로 이 부분에서 더 이상의 개선은 없습니다.

### 3. 가독성 개선

가장 큰 개선점은 `siftUp` (percolate-up 또는 heapify-up) 및 `siftDown` (percolate-down 또는 heapify-down) 로직을 별도의 헬퍼 메서드로 분리하는 것입니다. 이는 코드의 역할을 명확히 하고 `main` 메서드의 복잡성을 줄여줍니다.

*   **`insert(int value)` 메서드**: 힙에 값을 추가하고 `siftUp`을 호출하여 힙 속성을 유지합니다.
*   **`deleteMin()` 메서드**: 힙에서 최솟값을 제거하고 `siftDown`을 호출하여 힙 속성을 유지합니다.
*   **`siftUp(int index)` 메서드**: 특정 인덱스에서 부모 노드와 비교하며 위로 올라가 힙 속성을 복원합니다.
*   **`siftDown(int index)` 메서드**: 특정 인덱스에서 자식 노드와 비교하며 아래로 내려가 힙 속성을 복원합니다.
*   **`swap(int i, int j)` 메서드**: 배열의 두 원소를 교환하는 간단한 유틸리티 메서드입니다.

**코드 변경 예시**:

**Before (Original code's insert part):**

```java
// ...
else { // Insert operation
    heap[++size] = x; // Add new element to the end
    int j = size;     // Start from the newly added element's index

    // Sift-up logic
    while (j > 1 && heap[j/2] > heap[j]) { // While not root and parent is larger
        int temp = heap[j];
        heap[j] = heap[j/2];
        heap[j/2] = temp;
        j /= 2; // Move up to parent
    }
}
// ...
```

**After (Optimized code's insert part with helper methods):**

```java
// ...
else { // Insert operation
    insert(x); // Calls a helper method
}
// ...

// Helper methods outside main
private static void insert(int value) {
    heap[++size] = value; // Add to the end
    siftUp(size);        // Restore heap property by moving up
}

private static void siftUp(int index) {
    while (index > 1 && heap[index / 2] > heap[index]) {
        swap(index, index / 2); // Use a dedicated swap method
        index /= 2;
    }
}

private static void swap(int i, int j) {
    int temp = heap[i];
    heap[i] = heap[j];
    heap[j] = temp;
}
```

이러한 분리는 `main` 메서드가 고수준의 로직(입력 처리 및 연산 선택)에 집중하게 하고, 힙의 내부 로직은 별도의 메서드에서 처리하게 하여 가독성을 크게 향상시킵니다.

### 4. 최종 최적화된 코드

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BOJ_1927_Optimized {
    // 힙 배열과 현재 힙의 크기를 클래스 멤버 변수로 선언하여 헬퍼 메서드에서 접근 가능하게 합니다.
    // 1-based indexing을 사용하므로 0번 인덱스는 비워둡니다.
    private static int[] heap;
    private static int size; // 현재 힙에 들어있는 요소의 개수

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int N = Integer.parseInt(br.readLine().trim());
        
        // 힙 배열을 N+1 크기로 초기화 (최대 N개의 요소 + 1-based indexing)
        heap = new int[N + 1]; 
        size = 0; // 힙 초기 크기 0
        StringBuilder sb = new StringBuilder(); // 결과 출력을 위한 StringBuilder
        
        for (int i = 0; i < N; i++) {
            int x = Integer.parseInt(br.readLine().trim());
            
            if (x == 0) { // 0이 입력되면 가장 작은 값 출력 및 제거
                if (size == 0) { // 힙이 비어있으면 0 출력
                    sb.append("0\n");
                } else { // 힙이 비어있지 않으면 최소값 삭제
                    sb.append(deleteMin() + "\n");
                }
            } else { // 0이 아니면 값을 힙에 추가
                insert(x);
            }
        }
        
        System.out.println(sb);
    }

    /**
     * 힙에 새 값을 삽입합니다.
     * 새로운 값을 힙의 마지막에 추가하고, 힙 속성을 유지하기 위해 siftUp(상향 조정)을 수행합니다.
     * @param value 삽입할 정수 값
     */
    private static void insert(int value) {
        heap[++size] = value; // 힙의 마지막에 추가하고 size 증가
        siftUp(size);         // 추가된 위치부터 힙 속성 복원
    }

    /**
     * 힙에서 가장 작은 값(루트)을 삭제하고 반환합니다.
     * 힙이 비어있지 않다고 가정합니다.
     * 루트 값을 저장하고, 힙의 마지막 요소를 루트로 이동시킨 후,
     * 힙 속성을 유지하기 위해 siftDown(하향 조정)을 수행합니다.
     * @return 힙에서 삭제된 가장 작은 값
     */
    private static int deleteMin() {
        int min = heap[1];     // 가장 작은 값 (루트) 저장
        heap[1] = heap[size--]; // 힙의 마지막 요소를 루트로 이동 후 size 감소
        siftDown(1);           // 루트부터 힙 속성 복원
        return min;
    }

    /**
     * 특정 인덱스의 요소가 힙 속성을 위반할 경우, 부모와 비교하여 위로 이동시킵니다.
     * (상향 조정, percolate-up)
     * @param index 현재 위치
     */
    private static void siftUp(int index) {
        // 현재 노드가 루트가 아니고, 부모 노드가 현재 노드보다 크면
        while (index > 1 && heap[index / 2] > heap[index]) {
            swap(index, index / 2); // 부모와 교환
            index /= 2;              // 부모의 위치로 이동
        }
    }

    /**
     * 특정 인덱스의 요소가 힙 속성을 위반할 경우, 자식과 비교하여 아래로 이동시킵니다.
     * (하향 조정, percolate-down)
     * @param index 현재 위치
     */
    private static void siftDown(int index) {
        // 현재 노드가 적어도 하나의 자식(왼쪽 자식)을 가질 경우
        while (2 * index <= size) {
            int leftChild = 2 * index;
            int rightChild = 2 * index + 1;
            int smallerChild = leftChild; // 일단 왼쪽 자식을 더 작은 자식으로 가정

            // 오른쪽 자식이 존재하고, 왼쪽 자식보다 작으면 오른쪽 자식을 선택
            if (rightChild <= size && heap[rightChild] < heap[leftChild]) {
                smallerChild = rightChild;
            }

            // 현재 노드가 더 작은 자식보다 작거나 같으면 힙 속성 만족, 중단
            if (heap[index] <= heap[smallerChild]) {
                break;
            }

            // 그렇지 않으면 현재 노드와 더 작은 자식을 교환
            swap(index, smallerChild);
            index = smallerChild; // 더 작은 자식의 위치로 이동
        }
    }

    /**
     * 힙 배열에서 두 인덱스의 요소를 교환합니다.
     * @param i 첫 번째 인덱스
     * @param j 두 번째 인덱스
     */
    private static void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
```

### 5. 왜 이 방법이 최선인지 설명

1.  **자료구조의 선택**: 이 문제는 정수들을 삽입하고 가장 작은 값을 효율적으로 찾아 삭제하는 요구사항을 가지고 있습니다. 이를 위한 가장 적합하고 효율적인 자료구조는 **최소 힙(Min-Heap)**입니다. 최소 힙은 삽입과 삭제 연산을 모두 $O(\log N)$ 시간에 처리할 수 있어, 문제의 $N$이 최대 10만일 때 $N \log N$ (약 $10^5 \times 17 \approx 1.7 \times 10^6$)으로 충분히 빠른 속도를 보장합니다.

2.  **힙 구현 방식**:
    *   **배열 기반 구현**: 코드는 배열을 사용하여 힙을 구현합니다. 이는 `java.util.PriorityQueue`가 객체 기반(`Integer` 래퍼 클래스 사용)으로 구현되는 것에 비해, `int`와 같은 기본형(primitive type)을 직접 저장하므로 **메모리 오버헤드가 적고** 박싱/언박싱 과정이 없어 **성능 면에서 유리**합니다. 또한, 연속된 메모리 할당으로 캐시 효율성도 좋습니다.
    *   **1-based indexing**: 배열의 0번 인덱스를 사용하지 않고 1번 인덱스부터 시작하는 방식은 부모-자식 관계(`parent = index / 2`, `left_child = index * 2`, `right_child = index * 2 + 1`)를 계산하기 매우 편리하여 코드의 직관성과 구현의 용이성을 높입니다.

3.  **시간 및 공간 복잡도**:
    *   **시간 복잡도**: $N$번의 연산에 대해 각 연산이 $O(\log N)$이므로, 총 $O(N \log N)$입니다. 이는 비교 기반 최소 힙이 달성할 수 있는 **점근적으로 최적의 시간 복잡도**입니다.
    *   **공간 복잡도**: 힙의 최대 크기가 $N$이므로, 이를 저장하기 위한 배열의 공간 복잡도는 $O(N)$입니다. 이 역시 힙에 $N$개의 요소를 저장해야 하므로 **필수적인 공간 복잡도**입니다.

4.  **가독성 및 유지보수성**:
    *   `insert`, `deleteMin`, `siftUp`, `siftDown`, `swap`과 같은 **핵심 연산들을 별도의 헬퍼 메서드로 분리**했습니다. 이는 `main` 메서드의 역할을 명확하게(입력 처리 및 연산 선택) 만들고, 힙의 내부 로직은 각 메서드에 캡슐화되어 코드를 더 쉽게 이해하고 유지보수할 수 있게 합니다.
    *   명확한 메서드 이름은 각 코드 블록이 어떤 역할을 하는지 빠르게 파악할 수 있도록 돕습니다.

5.  **입출력 최적화**:
    *   `BufferedReader`를 사용하여 대량의 입력을 빠르게 처리합니다.
    *   `StringBuilder`를 사용하여 여러 번의 `System.out.println()` 호출 대신 결과를 모아서 한 번에 출력하므로, I/O 오버헤드를 줄여줍니다.

결론적으로, 이 최적화된 코드는 최소 힙이라는 문제에 가장 적합한 자료구조와 구현 방식을 사용하고 있으며, 시간/공간 복잡도 측면에서 이론적으로 달성 가능한 최적을 만족합니다. 또한, 코드의 구조화를 통해 가독성과 유지보수성을 극대화하여 실제 프로그래밍 환경에서 "최선"이라고 할 수 있는 솔루션입니다.