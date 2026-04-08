import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class Samsung_2025_FH_AM_1 {

	static int[][] Belief;
	static int[][] Food;
	static boolean[][] isInGroup;
	static boolean[][] isDefending;
	static List<Believer> spreaders;

	static final int[] dr = { -1, 1, 0, 0 }; // 상 하 좌 우
	static final int[] dc = { 0, 0, -1, 1 }; // 상 하 좌 우
	static final int[] foodOrder = { 7, 6, 5, 3, 1, 2, 4 };

	static int N, T;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());

		Belief = new int[N][N];
		Food = new int[N][N];

		for (int i = 0; i < N; i++) {
			char[] foods = br.readLine().toCharArray();
			for (int j = 0; j < N; j++) {
				if (foods[j] == 'T')
					Food[i][j] = 4;
				if (foods[j] == 'C')
					Food[i][j] = 2;
				if (foods[j] == 'M')
					Food[i][j] = 1;
			}
		}

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				Belief[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 0; i < T; i++) {
			morning();
			noon();
			evening();
			printBelief();
		}

	}

	static void morning() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Belief[i][j] += 1;
			}
		}
	}

	static void noon() {
	    spreaders = new ArrayList<>();
	    isInGroup = new boolean[N][N]; // 하루에 딱 한 번만 생성!

	    for (int i = 0; i < N; i++) {
	        for (int j = 0; j < N; j++) {
	            if (isInGroup[i][j]) continue;

	            int foodToSearch = Food[i][j];
	            
	            // 1. PQ 대신 빠르고 가벼운 ArrayDeque 사용
	            Queue<Believer> q = new ArrayDeque<>();
	            List<Believer> members = new ArrayList<>();

	            Believer startPerson = new Believer(Belief[i][j], 0, i, j, foodToSearch);
	            q.offer(startPerson);
	            members.add(startPerson);
	            isInGroup[i][j] = true; // isVisited 없이 isInGroup 하나로 통일

	            while (!q.isEmpty()) {
	                Believer curr = q.poll();
	                
	                for (int d = 0; d < 4; d++) {
	                    int nr = curr.r + dr[d];
	                    int nc = curr.c + dc[d];
	                    
	                    if (nr >= 0 && nr < N && nc >= 0 && nc < N 
	                            && !isInGroup[nr][nc] && Food[nr][nc] == foodToSearch) {
	                        Believer nextNode = new Believer(Belief[nr][nc], 0, nr, nc, foodToSearch);
	                        q.offer(nextNode);
	                        members.add(nextNode);
	                        isInGroup[nr][nc] = true;
	                    }
	                }
	            }

	            Collections.sort(members);
	            Believer Head = members.get(0);

	            // 2. N*N 전체 맵 스캔 대신, members 리스트만 순회하여 신앙심 차감!
	            for (Believer member : members) {
	                if (member.r != Head.r || member.c != Head.c) {
	                    Belief[member.r][member.c]--;
	                }
	            }

	            Head.B += members.size() - 1;
	            Belief[Head.r][Head.c] = Head.B;
	            spreaders.add(Head);
	        }
	    }
	}

	static void evening() {
		Collections.sort(spreaders);
		isDefending = new boolean[N][N];

		for (Believer spreader : spreaders) {
//			System.out.println(spreader.r + " " + spreader.c);
			if (isDefending[spreader.r][spreader.c])
				continue;
			spreader.x = spreader.B - 1;
			int dir = spreader.B % 4;
			spreader.B = 1;
			Belief[spreader.r][spreader.c] = 1;

			int r = spreader.r;
			int c = spreader.c;

			boolean stopSpreading = false;

			while (true) {
				int nr = r + dr[dir];
				int nc = c + dc[dir];


				if (nr < 0 || nr >= N || nc < 0 || nc >= N || spreader.x <= 0) {
					stopSpreading = true;
					break;
				}

				if (Food[nr][nc] == spreader.food) {
					r = nr;
					c = nc;
					continue;
				}
				
				int y = Belief[nr][nc];

				if (spreader.x > y) { // 강한전파
					Food[nr][nc] = spreader.food;
					spreader.x = Math.max(0, spreader.x - (y+1));
					Belief[nr][nc] += 1;
					isDefending[nr][nc] = true;

				} else { // 약한 전파
					Food[nr][nc] = spreader.food | Food[nr][nc]; // 비트마스킹 음식 표시
					Belief[nr][nc] += spreader.x;
					spreader.x = 0;
					isDefending[nr][nc] = true;
				}

				if (spreader.x <= 0) {
					stopSpreading = true;
					break;
				}

				r = nr;
				c = nc;
			}

			if (stopSpreading)
//				for (int rrr = 0; rrr < N; rrr++) {
//					System.out.println(Arrays.toString(Belief[rrr]));
//				}
				continue;
			
		}
	}

	static void printBelief() {
		StringBuilder sb = new StringBuilder();
		for (int food : foodOrder) {
			int sumBelief = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (Food[i][j] == food) {
						sumBelief += Belief[i][j];
					}
				}
			}

			sb.append(sumBelief).append(" ");
		}
		System.out.println(sb);
	}
}

class Believer implements Comparable<Believer> {
	int B;
	int x;
	int r;
	int c;
	int food;
	int tier;

	public Believer() {
	}

	public Believer(int b, int x, int r, int c, int food) {
		super();
		B = b;
		this.x = x;
		this.r = r;
		this.c = c;
		this.food = food;
		this.tier = Integer.bitCount(food);
	}

	@Override
	public int compareTo(Believer o) {
		if (this.tier != o.tier) {
			return this.tier - o.tier;
		} else if (this.B != o.B) {
			return o.B - this.B;
		} else if (this.r != o.r) {
			return this.r - o.r;
		} else
			return this.c - o.c;
	}

}
