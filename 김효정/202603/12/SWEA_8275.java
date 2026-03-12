import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.StringTokenizer;

public class SWEA_8275 {
	static int N, X, M, maxSum;
	static int[] result, output;
	static int[][] records;

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			X = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());

			records = new int[M][3];
			for (int i = 0; i < M; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < 3; j++) {
					records[i][j] = Integer.parseInt(st.nextToken());
				}
			}

			result = new int[N + 1];
			output = new int[N + 1];
			maxSum = -1;

			dfs(1, 0);

			System.out.print("#" + tc + " ");
			if (maxSum == -1) {
				System.out.print(maxSum);
			} else {
				for (int i = 1; i < output.length; i++) {
					System.out.print(output[i] + " ");
				}
			}
			System.out.println();

		}
	}

	static void dfs(int depth, int currentSum) {
		if (depth == N + 1) {
			if (check(currentSum)) {
				if (currentSum > maxSum) {
					maxSum = currentSum;
					output = result.clone();
				}
			}
			return;
		}

		for (int i = 0; i <= X; i++) {
			result[depth] = i;
			dfs(depth + 1, currentSum + i);
		}
	}

	static boolean check(int currentSum) {
		for (int i = 0; i < M; i++) {
			int l = records[i][0];
			int r = records[i][1];
			int s = records[i][2];

			int sum = 0;
			for (int k = l; k <= r; k++) {
				sum += result[k];
			}
			if (sum != s)
				return false;
		}
		return true;
	}
}
