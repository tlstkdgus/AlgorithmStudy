package simulation;

import java.util.Scanner;

// 로봇 청소기

public class BOJ_14503 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 방의 크기 N, M
		int N = sc.nextInt();
		int M = sc.nextInt();
		sc.nextLine();
		// 좌표
		int r = sc.nextInt();
		int c = sc.nextInt();
		
		// 바라보는 방향
		int d = sc.nextInt();
		sc.nextLine();
		// 방 생성
		int[][] arr = new int[N][M];
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				arr[i][j] = sc.nextInt();
			}
		}
		
		// 델타 탐색용 - 상/우/하/좌
		int[] dx = {-1, 0, 1, 0};
		int[] dy = {0, 1, 0, -1};
		
		// 정답 변수
		int ans = 0;
		
		// 갈 곳이 있는가?
		boolean isContinue = true;
		
		// 갈 곳이 없을 때까지 계속
		while(isContinue) {
			// 현재 칸이 아직 청소되지 않았다면 청소한다.
			// 청소한 칸은 2로 둔다.
			if(arr[r][c] == 0) {
				arr[r][c] = 2;
				ans++;
			}
			// 청소하지 않은 칸이 있는가?
			boolean cleaned = true;
			for(int i = 0; i < 4; i++) {
				int cr = r + dx[i];
				int cc = c + dy[i];
				if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 0) {
					cleaned = false;
					break;
				}
			}
			
			// 청소하지 않은 칸이 없다면
			if(cleaned) {
				// 후진
				if(d == 0) {
					int cr = r + dx[2];
					int cc = c + dy[2];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 1) {
						isContinue = false;
						break;
					}
					else {
						r = cr;
						c = cc;
					}
				}
				else if(d == 1) {
					int cr = r + dx[3];
					int cc = c + dy[3];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 1) {
						isContinue = false;
						break;
					} else {
						r = cr;
						c = cc;
					}
				}
				else if(d == 2) {
					int cr = r + dx[0];
					int cc = c + dy[0];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 1) {
						isContinue = false;
						break;
					} else {
						r = cr;
						c = cc;
					}
				}
				else if(d == 3) {
					int cr = r + dx[1];
					int cc = c + dy[1];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 1) {
						isContinue = false;
						break;
					} else {
						r = cr;
						c = cc;
					}
				}
			}
			// 청소 안 한 칸이 있다면
			else {
				if(d == 0) {
					d = 3;
					int cr = r + dx[d];
					int cc = c + dy[d];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 0) {
						r = cr;
						c = cc;
						arr[r][c] = 2;
						ans++;
					}
				}
				else if(d == 1) {
					d = 0;
					int cr = r + dx[d];
					int cc = c + dy[d];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 0) {
						r = cr;
						c = cc;
						arr[r][c] = 2;
						ans++;
					}
				} else if(d == 2) {
					d = 1;
					int cr = r + dx[d];
					int cc = c + dy[d];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 0) {
						r = cr;
						c = cc;
						arr[r][c] = 2;
						ans++;
					}
				} else if(d == 3) {
					d = 2;
					int cr = r + dx[d];
					int cc = c + dy[d];
					if(cr >= 0 && cr < N && cc >= 0 && cc < M && arr[cr][cc] == 0) {
						r = cr;
						c = cc;
						arr[r][c] = 2;
						ans++;
					}
				}
			}
		}
		System.out.println(ans);
	}
}
