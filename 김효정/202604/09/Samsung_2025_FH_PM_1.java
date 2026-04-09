import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class Samsung_2025_FH_PM_1 {
	static int N, Q;
	static int[][] map;
	static int[][] newMap;
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
		newMap = new int[N][N];
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
	    
	    // 1. 새로운 미생물 덮어쓰기
	    for (int r = ri; r < rf; r++) {
	        for (int c = ci; c < cf; c++) {
	            map[r][c] = expID;
	        }
	    }
	    isAlive[expID] = true;

	    // 2. 파편화(반갈죽) 검사 및 살아남은 미생물 면적 재계산
	    Set<Integer> idToKill = new HashSet<>();
	    boolean[] isChecked = new boolean[Q + 1];
	    int[] currentArea = new int[Q + 1]; // 살아남은 녀석들의 찐 면적을 저장할 배열
	    isVisited = new boolean[N][N];

	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            int currentId = map[i][j];
	            
	            if (currentId == 0) continue; // 빈칸 패스
	            currentArea[currentId]++;     // 맵 돌면서 겸사겸사 최신 면적 카운트!
	            
	            if (currentId == expID) continue; // 새로 들어온 애는 파편화 검사 패스
	            if (isVisited[i][j]) continue;    // 💡 핵심! 이미 같은 덩어리로 방문했다면 패스

	            // 여기서부터는 '새로운 덩어리'를 발견했을 때만 실행됨
	            if (isChecked[currentId]) {
	                // 이미 전에 이 ID의 다른 덩어리를 본 적이 있다면? -> 파편화 당첨!
	                idToKill.add(currentId);
	            }
	            
	            bfs(i, j, currentId); // 이 덩어리 전체에 isVisited 도장 쾅!
	            isChecked[currentId] = true; // 이 ID는 이제 덩어리 1개 발견했음 기록
	        }
	    }

	    // 3. 파편화된 놈들 맵에서 삭제 & 생존자들 상태 업데이트
	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            if (idToKill.contains(map[i][j])) {
	                map[i][j] = 0; // 맵에서 지우기 (한 번의 N*N 순회로 끝!)
	            }
	        }
	    }

	    // 객체 정보 최신화
	    for (int id : idToKill) {
	        isAlive[id] = false;
	    }
	    
	    // 💡 Phase 2를 위해, 모든 살아있는 미생물의 최신 면적을 객체에 업데이트
	    for (int id = 1; id <= expID; id++) {
	        if (isAlive[id]) {
	            colonies[id] = new Colony(id, currentArea[id]);
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
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if 
			}
		}

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
