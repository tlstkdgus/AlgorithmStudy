import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Samsung_2025_SH_PM_1 {
	static final int[] dr = { 0, 1, 0, -1 };
	static final int[] dc = { 1, 0, -1, 0 };
	static int[][] map;
	static int[][] cleaners;
	static boolean[][] isVisited;
	static boolean[][] isCleanerHere;
	static int N, K, L;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		L = Integer.parseInt(st.nextToken());

		map = new int[N][N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		cleaners = new int[K][2];
		isCleanerHere = new boolean[N][N];

		for (int i = 0; i < K; i++) {
			st = new StringTokenizer(br.readLine());
			
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			cleaners[i][0] = row - 1;
			cleaners[i][1] = col - 1;
			
			isCleanerHere[row-1][col-1] = true;
		}

		for (int t = 0; t < L; t++) {

			int totalDust = 0;

			move();
//			System.out.println("Move");
//			printMap();
//			System.out.println("Clean");
			clean();
//			printMap();
//			System.out.println("addDust");
			addDust();
//			printMap();
//			System.out.println("Diffuse");
			diffuseDust();
//			printMap();

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (map[i][j] > 0) totalDust += map[i][j];
				}
			}

			System.out.println(totalDust);
		}

	}

	static void move() {
		for (int i = 0; i < K; i++) {
			isCleanerHere[cleaners[i][0]][cleaners[i][1]] = false;
			int[] coords = bfs(cleaners[i][0], cleaners[i][1]);
			cleaners[i] = coords;
			isCleanerHere[cleaners[i][0]][cleaners[i][1]] = true;
		}
	}

	static int[] bfs(int r, int c) {
		isVisited = new boolean[N][N];
		PriorityQueue<Cell> pq = new PriorityQueue<>();
		int dist = 0;
		pq.offer(new Cell(r, c, dist, map[r][c]));
		isVisited[r][c] = true;

		while (!pq.isEmpty()) {
			Cell curr = pq.poll();
			int cR = curr.row;
			int cC = curr.col;
			if (curr.p > 0) {
				return new int[] { cR, cC };
			}

			for (int i = 0; i < 4; i++) {
				int nr = cR + dr[i];
				int nc = cC + dc[i];

				if (nr >= 0 && nr < N && nc >= 0 && nc < N && !isVisited[nr][nc] && map[nr][nc] >= 0) {
					if (isCleanerHere[nr][nc]) continue;
					isVisited[nr][nc] = true;
					pq.offer(new Cell(nr, nc, curr.dist + 1, map[nr][nc]));
				}
			}
		}
		return new int[] { r, c };
	}

	static void clean() {

		for (int cleanerIdx = 0; cleanerIdx < K; cleanerIdx++) {
			int r = cleaners[cleanerIdx][0];
			int c = cleaners[cleanerIdx][1];

			PriorityQueue<Dust> pq = new PriorityQueue<>();

			for (int d = 0; d < 4; d++) {
				Dust dust = new Dust(d, 0);
				dust.pSum += Math.min(map[r][c], 20);
				for (int i = 0; i < 4; i++) {
					if (Math.floorMod(d - i, 4) == 2)
						continue;
					int nr = r + dr[i];
					int nc = c + dc[i];
					if (nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] <= 0)
						continue;

//					if (isCleanerHere[nr][nc]) continue;

					dust.pSum += Math.min(map[nr][nc], 20);
				}
				pq.offer(dust);
			}

			Dust dustThisCell = pq.poll();
			int dir = dustThisCell.d;

			map[r][c] = Math.max(map[r][c] - 20, 0);

			for (int i = 0; i < 4; i++) {
				if (Math.floorMod(dir - i, 4) == 2)
					continue;
				int nr = r + dr[i];
				int nc = c + dc[i];
				if (nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] <= 0)
					continue;

//				if (isCleanerHere[nr][nc]) continue;

				map[nr][nc] = Math.max(map[nr][nc] - 20, 0);
			}
		}
	}

	static void addDust() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (map[i][j] > 0)
					map[i][j] += 5;
			}
		}
	}

	static void diffuseDust() {
		int[][] delta = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (map[i][j] == 0) {
					int totalP = 0;
					for (int d = 0; d < 4; d++) {
						int nr = i + dr[d];
						int nc = j + dc[d];

						if (nr < 0 || nr >= N || nc < 0 || nc >= N)
							continue;
						if (map[nr][nc] <= 0) continue;

						totalP += map[nr][nc];
					}
					delta[i][j] += totalP / 10;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] += delta[i][j];
			}
		}
	}
	
	static void printMap() {
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.toString(map[i])); 
		}
	}

}

class Cell implements Comparable<Cell> {
	int row;
	int col;
	int dist;
	int p;

	public Cell() {
	}

	public Cell(int row, int col, int dist, int p) {
		super();
		this.row = row;
		this.col = col;
		this.dist = dist;
		this.p = p;
	}

	@Override
	public int compareTo(Cell o) {
		if (this.dist != o.dist) {
			return this.dist - o.dist;
		} else if (this.row != o.row) {
			return this.row - o.row;
		} else
			return this.col - o.col;
	}
}

class Dust implements Comparable<Dust> {
	int d;
	int pSum;

	public Dust() {
	}

	public Dust(int d, int pSum) {
		super();
		this.d = d;
		this.pSum = pSum;
	}

	@Override
	public int compareTo(Dust o) {
		if (this.pSum != o.pSum) {
			return (o.pSum - this.pSum);
		}
		return this.d - o.d;
	}

}
