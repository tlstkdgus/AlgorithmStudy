# 최적화 분석

주어진 코드는 BOJ 1202 "보석 도둑" 문제를 해결하려는 시도로 보입니다. 현재 코드의 시간 복잡도는 매우 비효율적이며, 대규모 입력에 대해 `시간 초과(TLE)`가 발생할 것입니다.

---

### 문제 분석 (BOJ 1202 - 보석 도둑)

*   **N**: 보석의 개수
*   **K**: 가방의 개수
*   각 보석: 질량 (M), 가격 (V)
*   각 가방: 담을 수 있는 최대 질량 (C)
*   가방 하나에는 보석 하나만 넣을 수 있습니다.
*   가장 비싸게 보석을 훔치는 방법을 찾아야 합니다 (총 가격의 최댓값).

**현재 코드의 문제점:**

1.  **시간 복잡도:**
    *   `while (nBag > 0)` 루프는 K번 실행됩니다.
    *   내부에서 `maxV2M`을 찾기 위해 보석 배열을 전부 탐색 (O(N))합니다.
    *   그 후 `minCapa`를 찾기 위해 가방 배열을 전부 탐색 (O(K))합니다.
    *   따라서 전체 시간 복잡도는 약 **O(K * (N + K))** 입니다. N과 K가 각각 최대 300,000이므로, 이는 (300,000)^2 = 9 * 10^10 에 가까운 연산이 되어 TLE가 발생합니다.

2.  **잘못된 그리디 전략:**
    *   `v2m` (가치/질량 비율)이 높은 보석을 먼저 고르려는 시도는 무한 분할이 가능한 "연속 배낭 문제"에서는 유효하지만, "0-1 배낭 문제" (하나의 아이템을 통째로 넣거나 말거나)에서는 항상 최적해가 되지 않습니다. 특히 이 문제에서는 각 가방에 하나의 보석만 들어간다는 제약이 있어 더욱 그렇습니다.
    *   또한, `mass[maxIdx] = -1` 등으로 사용된 보석과 가방을 마킹하는 방식은 배열의 재탐색 시 비활성화된 요소를 건너뛰지 못하거나, 조건문을 추가해야 하는 비효율을 낳습니다.

---

### 최적화 방안

이 문제는 전형적인 **그리디 알고리즘 + 우선순위 큐 (PriorityQueue)**를 사용하여 해결할 수 있습니다.

#### 1. 시간 복잡도 개선 방법

핵심 아이디어:
1.  가방 용량이 작은 것부터 채우는 것이 유리합니다. 작은 가방에는 선택의 폭이 좁기 때문입니다.
2.  작은 가방부터 처리해 나가면서, 현재 가방에 넣을 수 있는 보석들 중 가장 가치가 높은 보석을 선택합니다.

**구체적인 단계:**

1.  **보석 정렬:** 보석들을 `질량(mass)`을 기준으로 오름차순 정렬합니다. (질량이 같으면 가치는 상관없습니다. 우선순위 큐가 처리할 것입니다.)
2.  **가방 정렬:** 가방들을 `용량(capacity)`을 기준으로 오름차순 정렬합니다.
3.  **우선순위 큐 사용:** 현재 가방에 담을 수 있는 보석들 중에서 `가장 가치가 높은` 보석을 효율적으로 찾기 위해 `최대 힙(Max-Heap)` 형태의 `PriorityQueue`를 사용합니다. 이 큐에는 보석의 `가치(value)`만 저장합니다.
4.  **순회:** 정렬된 가방들을 하나씩 순회합니다. 각 가방을 처리할 때마다:
    *   현재 가방의 용량으로 담을 수 있는 보석들을 (질량이 현재 가방 용량 이하인 보석들) `PriorityQueue`에 추가합니다. 이때 보석은 한 번만 `PriorityQueue`에 추가되도록 관리합니다.
    *   `PriorityQueue`가 비어있지 않다면, 가장 가치가 높은 보석(즉, 큐의 `peek()` 값)을 꺼내어 `totalValue`에 더합니다. 이 보석은 현재 가방에 할당된 것으로 간주됩니다.

**새로운 시간 복잡도:**

*   보석 정렬: `O(N log N)`
*   가방 정렬: `O(K log K)`
*   가방 순회 (K번):
    *   보석을 `PriorityQueue`에 추가: 각 보석은 최대 한 번만 추가됩니다. 총 `N`번의 추가 연산은 `O(N log N)` 입니다.
    *   `PriorityQueue`에서 보석 추출: 각 가방마다 최대 한 번 추출됩니다. 총 `K`번의 추출 연산은 `O(K log N)` 입니다. (PQ의 최대 크기가 N이므로)
*   전체 시간 복잡도: **O(N log N + K log K)**. N, K가 300,000일 때, 이는 약 300,000 * log(300,000) = 300,000 * 18 = 약 5.4 * 10^6 연산으로, 수초 내에 완료됩니다.

#### 2. 공간 복잡도 개선 방법

*   `Gem` 객체 배열: `O(N)`
*   가방 용량 배열: `O(K)`
*   `PriorityQueue`: 최악의 경우 모든 보석의 가치를 저장할 수 있으므로 `O(N)`

최적화된 코드의 공간 복잡도는 **O(N + K)** 입니다. 이는 현재 코드와 동일한 수준이지만, 데이터 구조의 효율적인 활용으로 메모리 접근 패턴이 더 좋아집니다.

#### 3. 가독성 개선

*   `Gem` 클래스를 정의하여 보석의 `질량(mass)`과 `가치(value)`를 묶어서 관리합니다. 이는 `int[] mass`, `int[] value` 처럼 두 개의 배열을 사용하는 것보다 직관적입니다.
*   변수명을 더 명확하게 지정하고, 필요한 주석을 추가합니다.
*   Fast I/O (`BufferedReader`, `StringTokenizer`)는 이미 사용 중이므로 유지합니다.

---

### 4. 최종 최적화된 코드

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

// 보석 정보를 담는 클래스
class Gem implements Comparable<Gem> {
    int mass;
    int value;

    public Gem(int mass, int value) {
        this.mass = mass;
        this.value = value;
    }

    // 질량을 기준으로 오름차순 정렬
    @Override
    public int compareTo(Gem other) {
        return this.mass - other.mass;
    }
}

public class BOJ_1202_optimized { // 클래스 이름 변경
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // 보석의 개수
        int K = Integer.parseInt(st.nextToken()); // 가방의 개수

        Gem[] gems = new Gem[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            gems[i] = new Gem(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        int[] bags = new int[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            bags[i] = Integer.parseInt(st.nextToken());
        }

        // 1. 보석을 질량 기준으로 오름차순 정렬
        Arrays.sort(gems);

        // 2. 가방을 용량 기준으로 오름차순 정렬
        Arrays.sort(bags);

        // 3. 우선순위 큐 (Max-Heap) 생성
        // 가치가 높은 보석을 먼저 꺼내기 위해 내림차순 정렬 (최대 힙)
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        long totalValue = 0; // 총 가치 (값이 커질 수 있으므로 long 사용)
        int gemIdx = 0;       // 현재 처리 중인 보석의 인덱스

        // 4. 가방을 용량이 작은 것부터 순회
        for (int i = 0; i < K; i++) {
            // 현재 가방에 담을 수 있는 모든 보석을 우선순위 큐에 추가
            // (질량이 현재 가방 용량보다 작거나 같은 보석들)
            while (gemIdx < N && gems[gemIdx].mass <= bags[i]) {
                pq.add(gems[gemIdx].value);
                gemIdx++;
            }

            // 우선순위 큐가 비어있지 않다면, 가장 가치가 높은 보석을 꺼내어 가방에 넣음
            if (!pq.isEmpty()) {
                totalValue += pq.poll();
            }
        }

        System.out.println(totalValue);
        br.close();
    }
}
```

---

### 5. 왜 이 방법이 최선인지 설명

이 그리디 전략이 최적인 이유는 다음과 같습니다.

1.  **작은 가방부터 처리하는 이점:**
    *   가방의 용량을 오름차순으로 정렬하여 처리함으로써, 작은 가방에 넣을 수 없는 보석은 나중에 큰 가방에서도 선택될 수 있습니다.
    *   반대로, 큰 가방부터 처리하면 그 가방에 들어갈 수 있는 선택지가 너무 많아 어떤 보석을 넣는 것이 최적인지 판단하기 어렵습니다. 예를 들어, 큰 가방에 작은 보석을 넣으면 더 큰 보석이 들어갈 자리를 낭비할 수 있습니다.
    *   작은 가방부터 처리하면, 해당 가방에 넣을 수 있는 보석들 중에서 `최고 가치`의 보석을 선택하는 것이 항상 최적입니다. 왜냐하면 이 가방에 넣지 않고 다른 보석을 넣거나 비워두면, `더 적은 가치`를 얻거나 아예 `가치 0`을 얻게 됩니다. 더 가치 있는 보석을 이 가방에 넣지 않고 나중에 더 큰 가방에 넣으려고 보류해도, 그 보석을 나중에 넣을 수 있는 가방은 현재 가방보다 `용량이 같거나 더 큰` 가방뿐이며, 그 가방에 넣을 수 있는 다른 보석이 더 많아질 뿐입니다. 따라서 지금 가장 비싼 보석을 작은 가방에 넣는 것이 다른 가방의 선택지를 최소한으로 제한하면서 현재 가방의 가치를 최대화하는 방법입니다.

2.  **우선순위 큐의 역할:**
    *   가방들을 용량 순으로 처리하면서, 현재 가방 용량(`bags[i]`)에 담을 수 있는 모든 보석들을 `PriorityQueue`에 `가치`만 넣어 관리합니다.
    *   `PriorityQueue`는 `최대 힙`으로 구현되어 있어, `peek()` 또는 `poll()` 연산을 통해 현재 가방에 담을 수 있는 보석들 중에서 `가장 가치가 높은` 보석을 `O(log N)` 시간에 빠르게 찾을 수 있습니다.
    *   이러한 효율적인 탐색 덕분에, `K`개의 가방 각각에 대해 `N`개의 보석을 일일이 탐색하는 비효율적인 O(KN) 탐색을 피하고, 총 `O(N log N + K log K)`라는 효율적인 시간 복잡도를 달성할 수 있습니다.

이 방법은 그리디 선택이 지역적으로 최적일 뿐만 아니라 전역적으로도 최적해를 이끌어내는 대표적인 예시이며, 이러한 유형의 문제(예: 특정 제약 내에서 가장 좋은 n개 선택)를 해결하는 데 가장 효율적인 표준적인 접근 방식입니다.