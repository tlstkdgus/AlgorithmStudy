import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BOJ_11279 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int N = Integer.parseInt(br.readLine().trim());
		
		int[] heap = new int[N+1];
		int size = 0;
		int minValue = Integer.MAX_VALUE;
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < N; i++) {
			int x = Integer.parseInt(br.readLine().trim());
			if (x == 0) {
				if (size == 0) sb.append("0\n");
				
				else {
					minValue = heap[1];
					heap[1] = heap[size--];
					int j = 1;

					while (2*j <= size) {
						int left = 2*j;
						int right = 2*j+1;
						int largerChild = left;
						
						if (right <= size && heap[right] > heap[left]) {
							largerChild = right;
						}
						
						if (heap[j] >= heap[largerChild]) break;
						
						int temp = heap[j];
						heap[j] = heap[largerChild];
						heap[largerChild] = temp;
						
						j = largerChild;
					}
					sb.append(minValue +"\n");
				}
			}
			
			else {
				heap[++size] = x;
				int j = size;
				
				while (j > 1 && heap[j/2] < heap[j]) {
					int temp = heap[j];
					heap[j] = heap[j/2];
					heap[j/2] = temp;
					j /= 2;
				}
			}
		}
		
		System.out.println(sb);
	}

}
