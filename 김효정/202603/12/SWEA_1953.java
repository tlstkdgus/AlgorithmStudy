import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class SWEA_1953 {
	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };
	static int N, M;
	static int cnt, time;
	static int[][] underground;
	static boolean[][] visited;

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			int R = Integer.parseInt(st.nextToken());
			int C = Integer.parseInt(st.nextToken());
			int L = Integer.parseInt(st.nextToken());

			underground = new int[N][M];
			visited = new boolean[N][M];

			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					underground[i][j] = Integer.parseInt(st.nextToken());
				}
			}

			time = 1;
			cnt = 1;
			bfs(R, C, time, L);
			System.out.println("#" + tc + " " + cnt);

		}
	}

	static void bfs(int initR, int initC, int time, int L) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] { initR, initC });
		visited[initR][initC] = true;

		while (!q.isEmpty()) {
			if (time == L)
				break;
			
			int size = q.size();
			for (int i = 0; i < size; i++) {
				int[] curr = q.poll();
				int r = curr[0];
				int c = curr[1];
				int[] iter = getDir(underground[r][c]);

				for (int dir : iter) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];
					if (nr >= 0 && nr < N && nc >= 0 && nc < M && !visited[nr][nc]
							&& isConnected(dir, underground[nr][nc])) {
						q.offer(new int[] { nr, nc });
						visited[nr][nc] = true;
						cnt++;
					}
				}
			}
			time++;
		}
	}


	static int[] getDir(int pipeType) {
		switch (pipeType) {
		case 1:
			return new int[] { 0, 1, 2, 3 };
		case 2:
			return new int[] { 0, 1 };
		case 3:
			return new int[] { 2, 3 };
		case 4:
			return new int[] { 0, 3 };
		case 5:
			return new int[] { 1, 3 };
		case 6:
			return new int[] { 1, 2 };
		case 7:
			return new int[] { 0, 2 };
		default:
			return new int[] {};
		}
	}

	static boolean isConnected(int dir, int nPipe) {
		switch (dir) {
		case 0:
			return (nPipe == 1 || nPipe == 2 || nPipe == 5 || nPipe == 6);
		case 1:
			return (nPipe == 1 || nPipe == 2 || nPipe == 4 || nPipe == 7);
		case 2:
			return (nPipe == 1 || nPipe == 3 || nPipe == 4 || nPipe == 5);
		case 3:
			return (nPipe == 1 || nPipe == 3 || nPipe == 6 || nPipe == 7);
		}
		return false;
	}
}

//static void dfs(int initR, int initC, int time, int L) {
//Stack<int[]> s = new Stack<>();
//s.add(new int[] { initR, initC });
//visited[initR][initC] = true;
//
//while (!s.isEmpty() && time < L) {
//	int[] curr = s.pop();
//	int r = curr[0];
//	int c = curr[1];
//	int[] iter = getDir(underground[r][c]);
//
//	for (int dir : iter) {
//		int nr = r + dr[dir];
//		int nc = c + dc[dir];
//		if (nr >= 0 && nr < N && nc >= 0 && nc < M && !visited[nr][nc] && isConnected(dir, underground[nr][nc])) {
//			s.push(new int[] { nr, nc });
//			visited[nr][nc] = true;
//			cnt++;
//		}
//	}
//	time++;
//}
//}
