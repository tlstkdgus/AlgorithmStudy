import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Scanner;

public class BOJ_13549 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int K = sc.nextInt();

		int[] dist = new int[100002];
		Arrays.fill(dist, Integer.MAX_VALUE);

		Deque<Integer> q = new ArrayDeque<>();

		int out = bfs(q, dist, N, K);
		System.out.println(out);

	}

	public static int bfs(Deque<Integer> q, int[] dist, int start, int end) {
		dist[start] = 0;
		q.offer(start);

		while (!q.isEmpty()) {
			int current = q.poll();

			if (current == end) {
				return dist[current];
			}

			int next = 0;
			
			for (int i = 0; i < 3; i++) {
				next = (i == 0) ? current - 1 : (i == 1) ? current + 1 : 2*current;
				
				if (next > 100001 || next < 0) continue;
				
				int newcost = dist[current] + ((i == 2) ? 0 : 1);
				if (newcost < dist[next]) {
					if (i != 2) {
						q.offer(next);
					} else q.offerFirst(next);
					
					dist[next] = newcost;
				}
			}
		}
		return -1;
	}
}