import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class BOJ_15656 {
	static int[] nums;
	static int[] sets;
	static boolean[] visited;
	static StringBuilder sb;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		
		nums = new int[N];
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			nums[i] = Integer.parseInt(st.nextToken());
		}
		
		Arrays.sort(nums);
		
		sets = new int[M];
		sb = new StringBuilder();
		visited = new boolean[N];
		
		permutation(N, M, 0);
		System.out.println(sb);

	}

	public static void permutation(int n, int r, int depth) {
		if (depth == r) {
			for (int i = 0; i < r; i++) {
				sb.append(sets[i] + " ");
			}
			sb.append("\n");
			return;
		}

		for (int i = 0; i < n; i++) {
			if (!visited[i] && (nums[i] >= sets[Math.max(0, depth-1)])) {
				visited[i] = true;
				sets[depth] = nums[i];
				permutation(n, r, depth + 1);
				visited[i] = false;
			}
		}
	}
}