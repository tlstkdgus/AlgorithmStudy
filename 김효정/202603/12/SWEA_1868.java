import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

public class SWEA_1868 {
	static int[] dr = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int[] dc = { 0, -1, -1, -1, 0, 1, 1, 1 };
	static char[][] field;
	static int[][] cntMap;
	static int N, nClick, nUnknown;

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= T; tc++) {
			N = Integer.parseInt(br.readLine().trim());
			field = new char[N][N];
			nUnknown = 0;

			for (int i = 0; i < N; i++) {
				String st = br.readLine();
				char[] col = st.toCharArray();
				for (int j = 0; j < N; j++) {
					char tmp = col[j];
					if (tmp == '.')
						nUnknown++;
					field[i][j] = tmp;
				}
			}

			cntMap = new int[N][N];

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					cntMap[i][j] = countMine(i, j);
				}
			}

			nClick = 0;

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (field[i][j] == '.' && cntMap[i][j] == 0)
						click(i, j);
				}
			}

			nClick += nUnknown;
			System.out.println("#" + tc + " " + nClick);

		}
	}

	static void click(int rClick, int cClick) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] { rClick, cClick });
		field[rClick][cClick] = (char) (cntMap[rClick][cClick] + '0');
		nClick++;
		nUnknown--;

		while (!q.isEmpty()) {
			int size = q.size();
			for (int i = 0; i < size; i++) {
				int[] curr = q.poll();
				int r = curr[0];
				int c = curr[1];

				if (cntMap[r][c] == 0) {
					for (int dir = 0; dir < 8; dir++) {
						int nr = r + dr[dir];
						int nc = c + dc[dir];
						if (nr >= 0 && nr < N && nc >= 0 && nc < N && field[nr][nc] == '.') {

							field[nr][nc] = (char) (cntMap[nr][nc] + '0');
							q.offer(new int[] { nr, nc });
							nUnknown--;
						}
					}
				}
			}
		}
	}

	static int countMine(int r, int c) {
		int n = 0;
		for (int i = 0; i < 8; i++) {
			int nr = r + dr[i];
			int nc = c + dc[i];
			if (nr < 0 || nr >= N || nc < 0 || nc >= N)
				continue;
			if (field[nr][nc] == '*')
				n++;
		}
		return n;
	}
}
