
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ_13460 {
	static int N, M;
	static char[][] map;
	static boolean[][][][] isVisited;
	static final int[] dr = { -1, 0, 1, 0 };
	static final int[] dc = { 0, -1, 0, 1 };

	static class State {
		int rr, rc, br, bc, moveCnt;

		public State() {
			// TODO Auto-generated constructor stub
		}

		public State(int rr, int rc, int br, int bc, int moveCnt) {
			super();
			this.rr = rr;
			this.rc = rc;
			this.br = br;
			this.bc = bc;
			this.moveCnt = moveCnt;
		}
		
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		map = new char[N][M];
		isVisited = new boolean[N][M][N][M];
		
		int initRr =0, initRc = 0, initBr = 0, initBc = 0;
		
		for (int i = 0; i < N; i++) {
			
			String str = br.readLine();
			for (int j = 0; j < M; j++) {
				map[i][j] = str.charAt(j);
				if (map[i][j] == 'R') {
					initRr = i;
					initRc = j;
				} else if (map[i][j] == 'B') {
					initBr = i;
					initBc = j;
				}
			}
		}
		
		System.out.println(bfs(initRr, initRc, initBr, initBc));
	}
	
	
	static int bfs(int rR, int cR, int rB, int cB) {
		Queue<State> q = new ArrayDeque<>();
		q.offer(new State(rR, cR, rB, cB, 0));
		isVisited[rR][cR][rB][cB] = true;
		
		while(!q.isEmpty()) {
			State curr = q.poll();
			
			if (curr.moveCnt >= 10) {
				return -1;
			}
			
			for (int i = 0; i < 4; i++) {
				int nRr = curr.rr;
				int nRc = curr.rc;
				int rMove = 0;
				
				while (map[nRr + dr[i]][nRc + dc[i]] != '#' && map[nRr][nRc] != 'O') {
					nRr += dr[i];
					nRc += dc[i];
					rMove++;
				}
				
				int nBr = curr.br;
				int nBc = curr.bc;
				int bMove = 0;
				
				while (map[nBr + dr[i]][nBc + dc[i]] != '#' && map[nBr][nBc] != 'O') {
					nBr += dr[i];
					nBc += dc[i];
					bMove++;
				}
				
				if (map[nBr][nBc] == 'O') continue;
				if (map[nRr][nRc] == 'O') {
					return curr.moveCnt + 1;
				}
				
				if (nRr == nBr && nRc == nBc) {
					if (rMove > bMove) {
						nRr -= dr[i];
						nRc -= dc[i];
					} else {
						nBr -= dr[i];
						nBc -= dc[i];
					}
				}
				
				if (!isVisited[nRr][nRc][nBr][nBc]) {
					isVisited[nRr][nRc][nBr][nBc] = true;
					q.offer(new State(nRr, nRc, nBr, nBc, curr.moveCnt+1));
				}
			}
		}
		return -1;
	}
}
