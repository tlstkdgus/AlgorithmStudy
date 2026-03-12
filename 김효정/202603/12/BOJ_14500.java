import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_14500 {
	static int N, M;
	static int[][] numbers;
	static boolean[][] visited;
	static int maxSum;
	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		numbers = new int[N][M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				numbers[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		visited = new boolean[N][M];
		maxSum = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				visited[i][j] = true;
				dfs(i, j, 1, numbers[i][j]);
				visited[i][j] = false;
				maxSum = Math.max(tShape(i, j), maxSum);

			}
		}
		System.out.println(maxSum);
	}

	static void dfs(int r, int c, int depth, int sum) {
		if (depth == 4) {
			maxSum = Math.max(maxSum, sum);
			return;
		}

		for (int i = 0; i < 4; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			if (nr < 0 || nr >= N || nc < 0 || nc >= M || visited[nr][nc]) {
				continue;
			}
			visited[nr][nc] = true;
			dfs(nr, nc, depth + 1, sum + numbers[nr][nc]);
			visited[nr][nc] = false;
		}
	}

	static int tShape(int r, int c) {
		int max = 0;
		for (int i = 0; i < 4; i++) {
			int tmpSum = numbers[r][c];
			for (int j = 0; j < 4; j++) {
				if (j == i)
					continue;
				int nr = r + dr[j];
				int nc = c + dc[j];
				if (nr < 0 || nr >= N || nc < 0 || nc >= M) {
					tmpSum = 0;
					break;
				}
				tmpSum += numbers[nr][nc];
			}
			max = Math.max(max, tmpSum);
		}
		return max;
	}

}
