import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ_14502 {
	static int N, M, maxSafeZone = 0;
	static int[][] map;
	static List<int[]> emptySpaces = new ArrayList<>();
	static List<int[]> viruses = new ArrayList<>();
	static int[] dr = { -1, 1, 0, 0 };
	static int[] dc = { 0, 0, -1, 1 };

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		map = new int[N][M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				int tmp = Integer.parseInt(st.nextToken());
				if (tmp == 0) {
					emptySpaces.add(new int[] {i,j});
				}
				
				if (tmp == 2) {
					viruses.add(new int[] {i,j});
				}
				map[i][j] = tmp;
			}
		}
		
		selectWalls(0, 0);
		System.out.println(maxSafeZone);

	}

	static void selectWalls(int start, int count) {
		if (count == 3) {
			spreadVirus();
			return;
		}

		for (int i = start; i < emptySpaces.size(); i++) {
			int[] pos = emptySpaces.get(i);
			map[pos[0]][pos[1]] = 1; // 벽 세우기
			selectWalls(i + 1, count + 1);
			map[pos[0]][pos[1]] = 0; // 되돌리기 (백트래킹 핵심)
		}
	}

	// BFS로 바이러스 퍼뜨리고 안전 영역 계산
	static void spreadVirus() {
		int[][] tempMap = new int[N][M];
		// map을 tempMap으로 깊은 복사 (매우 중요)

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				tempMap[i][j] = map[i][j];
			}
		}

		Queue<int[]> q = new ArrayDeque<>();
		for (int[] v : viruses)
			q.offer(v);

		while (!q.isEmpty()) {
			int[] curr = q.poll();
			for (int i = 0; i < 4; i++) {
				int nr = curr[0] + dr[i];
				int nc = curr[1] + dc[i];
				if (nr >= 0 && nr < N && nc >= 0 && nc < M && tempMap[nr][nc] == 0) {
					tempMap[nr][nc] = 2;
					q.offer(new int[] { nr, nc });
				}
			}
		}
		// tempMap에서 0의 개수를 세고 maxSafeZone 갱신
		int nSafe = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (tempMap[i][j] == 0) {
					nSafe += 1;
				}
			}
		}

		maxSafeZone = Math.max(nSafe, maxSafeZone);

	}

}
