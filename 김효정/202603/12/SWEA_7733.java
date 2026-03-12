import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class SWEA_7733 {
	static int[] dr = {-1, 1, 0, 0};
	static int[] dc = {0, 0, -1, 1};
	static int N, nClump;
	static int[][] cheese;
	static boolean[][] visited;
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		for (int tc = 1; tc <= T; tc++) {
			N = Integer.parseInt(br.readLine().trim());
			cheese = new int[N][N];
			
			for (int i = 0; i < N; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				for (int j = 0; j < N; j++) {
					cheese[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			
			int maxClump = 0;
			
			for (int day = 0; day<=100; day++) {
				nClump = 0;
				visited = new boolean[N][N];
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (!visited[i][j] && cheese[i][j] > day) {
							bfs(i, j, day);
						}
					}
				}
				maxClump = Math.max(maxClump, nClump);
			}
			
			System.out.println("#" + tc +" " + maxClump);
		}
	}
	
	static void bfs(int rInit, int cInit, int day) {
		nClump++;
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] {rInit,cInit});
		visited[rInit][cInit] = true;
		
		while (!q.isEmpty()) {
			int size = q.size();
			for (int i  = 0; i < size; i++) {
				int[] curr = q.poll();
				int r = curr[0];
				int c = curr[1];
				
				for (int dir = 0; dir < 4 ; dir++) {
					int nr = r + dr[dir];
					int nc = c + dc[dir];
					
					if (nr >= 0 && nr < N && nc >= 0 && nc < N && !visited[nr][nc] && cheese[nr][nc] > day) {
						visited[nr][nc] = true;
						q.offer(new int[] {nr, nc});
					}
				}
			}
		}
	}
}
