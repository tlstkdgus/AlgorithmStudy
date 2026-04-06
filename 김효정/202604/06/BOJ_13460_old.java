
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ_13460_old {
	static final int[] dr = { -1, 0, 1, 0 };
	static final int[] dc = { 0, -1, 0, 1 };
	static boolean[][] isVisited; 

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());

		char[][] map = new char[N][M];
		isVisited = new boolean[N][M];

		int rRed = 0, cRed = 0;
		int rBlue = 0, cBlue = 0;
		int rGoal = 0, cGoal = 0;

		for (int i = 0; i < N; i++) {
			String str = br.readLine();
			for (int j = 0; j < M; j++) {
				char tmp = str.charAt(j);
				if (tmp == 'R') {
					rRed = i;
					cRed = j;
				}
				if (tmp == 'B') {
					rBlue = i;
					cBlue = j;
				}
				if (tmp == 'O') {
					rGoal = i;
					cGoal = j;
				}
			}
		}
		
		int move = 0;
		int minMove = -1;
		
		Queue<Integer> qR = new ArrayDeque<>();
		Queue<Integer> qB = new ArrayDeque<>();
		
		qR.offer(rRed * 100 + cRed);
		qB.offer(rBlue * 100 + cBlue);
		isVisited[rRed][cRed] = true;
		
		while (!qR.isEmpty() && move < 10) {
			int currR = qR.poll();
			int currB = qB.poll();
			int currRedRow = currR / 100;
			int currBlueRow = currB / 100;
			int currRedCol = currR % 100;
			int currBlueCol = currB % 100;
			
			for (int i = 0; i < 4; i++) {
				int nrRed = currRedRow + dr[i]; 
				int ncRed = currRedCol + dc[i];
				
				int nrBlue = currBlueRow + dr[i];
				int ncBlue = currBlueCol + dc[i];
				
				if (isVisited[nrRed][ncRed]) continue;
				
				move++;
				if (map[nrRed][ncRed] != '#') {
					if (map[nrRed][ncRed] == '.' || (map[nrRed][ncRed] == 'B'  && map[nrBlue][ncBlue] == '.')) {
						qR.offer(nrRed * 100 + ncRed);
						isVisited[nrRed][ncRed] = true;
					}
					if (map[nrRed][ncRed] == 'O') {
						minMove = move;
						break;
					}
				}
				
				if (map[nrBlue][ncBlue] != '#') {
					if (map[nrBlue][ncBlue] == '.' || (map[nrBlue][ncBlue] == 'R'  && map[nrRed][ncRed] == '.')) {
						qB.offer(nrBlue * 100 + ncBlue);
					}
					if (map[nrBlue][ncBlue] == 'O') continue;
				}
			}
		}
		
		System.out.println(minMove);

	}
}
