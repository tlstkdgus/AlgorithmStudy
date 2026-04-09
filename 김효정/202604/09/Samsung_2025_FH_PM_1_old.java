import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Samsung_2025_FH_PM_1_old {
	static int N, Q;
	static int[][] map;
	static boolean[][] isVisited;
	static boolean[] isAlive;
	static int[][] experiments;
	static Colony[] colonies;

	static final int[] dr = { -1, 1, 0, 0 };
	static final int[] dc = { 0, 0, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		map = new int[N][N];
		experiments = new int[Q + 1][4];
		colonies = new Colony[Q + 1];
		isAlive = new boolean[Q + 1];

		for (int i = 1; i <= Q; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 4; j++) {
				experiments[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 1; i <= Q; i++) {
			insert(i, experiments[i]);
			immigrate();
			int res = recordResult();

			sb.append(res).append("\n");
		}

	}

	static void insert(int expID, int[] exp) {

		int ri = exp[0], ci = exp[1], rf = exp[2], cf = exp[3];
		for (int r = ri; r < rf; r++) {
			for (int c = ci; c < cf; c++) {
				map[r][c] = expID;
			}
		}

		int area = (rf - ri) * (cf - ci);
		colonies[expID] = new Colony(expID, area);
		isAlive[expID] = true;

		// BFS로 반갈죽 찾기

		Set<Integer> idToKill = new HashSet<>();
		boolean[] isChecked = new boolean[Q + 1];
		isVisited = new boolean[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				if (map[i][j] == expID)
					continue;

				if (isChecked[map[i][j]])
					idToKill.add(map[i][j]);

				bfs(i, j, map[i][j]);
				isChecked[map[i][j]] = true;
			}
		}

		for (int i = 0; i < N; i++) {  {
				if (idToKill.contains(map[i][j])) {
					map[i][j] = 0;
				}
			}
		}
		
		
	}

	static void bfs(int r, int c, int idToSearch) {
		Queue<Integer> q = new ArrayDeque<>();
		q.offer(r * 100 + c);
		isVisited[r][c] = true;

		while (!q.isEmpty()) {
			int curr = q.poll();
			int currR = curr / 100;
			int currC = curr % 100;

			for (int d = 0; d < 4; d++) {
				int nr = currR + dr[d];
				int nc = currC + dc[d];

				if (nr >= 0 && nr < N && nc >= 0 && nc < N && !isVisited[nr][nc]) {
					if (map[nr][nc] == idToSearch) {
						isVisited[nr][nc] = true;
						q.offer(nr * 100 + nc);
					}
				}
			}

		}

	}

	static void immigrate() {

	}

	static void recordResult() {

	}

}

class Colony implements Comparable<Colony> {
	int id;
	int area;

	public Colony() {
	}

	public Colony(int id, int area) {
		super();
		this.id = id;
		this.area = area;
	}

	@Override
	public int compareTo(Colony o) {
		if (this.area != o.area) {
			return o.area - this.area;
		} else
			return this.id - o.id;
	}
}
