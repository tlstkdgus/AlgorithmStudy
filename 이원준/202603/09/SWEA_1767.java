package backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SWEA_1767 {
	static int T, N;
	static int[][] coreArr;
	static int maxCore;
	static List<int[]> coreList;
	static int minLineLength;
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		T = sc.nextInt();
		
		for(int tc = 1; tc <= T; tc++) {
			// 최대 코어수, 최소 길이 초기화
			maxCore = 0;
			minLineLength = Integer.MAX_VALUE;
			
			// 배열 생성 및 코어 좌표 리스트 생성
			N = sc.nextInt();
			coreArr = new int[N][N];
			coreList = new ArrayList<>();
			
			// 초기 코어 개수(벽에 붙어있는 코어)
			int initialCore = 0;
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					coreArr[i][j] = sc.nextInt();
					// 코어일 경우
					if(coreArr[i][j] == 1) {
						// 벽에 붙어 있다면 코어 개수 추가
						if(i == 0 || j == 0 || i == N-1 || j == N-1) {
							initialCore++;
						// 아니면 코어 좌표 리스트에 좌표 추가
						} else {
							coreList.add(new int[] {i, j});
						}
					}
				}
			}
			
			// dfs 로직
			dfs(0, initialCore, 0);
			System.out.println("#"+tc+" "+minLineLength);
		}
	}
	
	static void dfs(int depth, int coreCount, int currLength) {
		// 모든 코어를 다 탐색했을 경우
		if(depth == coreList.size()) {
			// 작동하는 코어 수가 기존 최대 코어 수보다 클경우
			if(coreCount > maxCore) {
				// 최대 코어 수 갱신, 선 길이 최솟값도 갱신
				maxCore = coreCount;
				minLineLength = currLength;
			}
			// 최대 코어수랑 같을 경우
			else if(coreCount == maxCore) {
				// 현재 길이와 최소 길이 비교
				minLineLength = Math.min(minLineLength, currLength);
			}
			return;
		}
		
		// 코어 좌표 입력
		int r = coreList.get(depth)[0];
		int c = coreList.get(depth)[1];
		
		// 델타 탐색
		for(int d = 0; d < 4; d++) {
			// 4방향 탐색하면서 선의 길이 구해놓기
			int wireLen = checkAndDraw(r, c, d, 2);
			
			// 길이가 0보다 크다면
			if(wireLen > 0) {
				// 깊이, 코어 개수 추가해주고, 현재 길이에 앞에서 구해놓은 길이 추가
				dfs(depth + 1, coreCount + 1, currLength + wireLen);
				// 다시 원상태로 돌리기(백트래킹)
				checkAndDraw(r, c, d, 0);
			}
		}
		
		// 코어 작동이 안되거나, 일부러 작동 안해도 최적인 경우 존재 가능성
		dfs(depth + 1, coreCount, currLength);
	}
	
	// 선 그리기 함수
	static int checkAndDraw(int r, int c, int dr, int value) {
		int nx = r;
		int ny = c;
		int length = 0;
		
		// 판단
		// 2라는 값을 받아왔을 경우
		if(value == 2) {
			// 해당 방향 끝까지 탐색
			while(true) {
				nx += dx[dr];
				ny += dy[dr];
				
				// 벽 만나면 멈추기
				if(nx < 0 || nx >= N || ny < 0 || ny >= N) break;
				
				// 다른 코어나 다른 선을 만났을 경우 0을 리턴
				if(coreArr[nx][ny] != 0) return 0;
			}
		}
		
		// 좌표 되돌리기
		nx = r;
		ny = c;
		
		// 본격적으로 칠한다
		while(true) {
			nx += dx[dr];
			ny += dy[dr];
			if(nx < 0 || nx >= N || ny < 0 || ny >= N) break;
			
			// 칠하면서 길이도 추가해준다.
			coreArr[nx][ny] = value;
			length++;
		}
		
		return length;
		
	}
}
