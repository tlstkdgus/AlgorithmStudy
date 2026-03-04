package simulation;

import java.util.Scanner;

public class BOJ_14499 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		// 지도의 세로, 가로 길이
		int N = sc.nextInt();
		int M = sc.nextInt();
		// 주사위의 좌표
		int x = sc.nextInt();
		int y = sc.nextInt();
		// 명령어의 개수
		int K = sc.nextInt();
		
		sc.nextLine();
		
		// 지도 생성
		int[][] arr = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				arr[i][j] = sc.nextInt();
			}
		}
		
		// 명령어 저장 배열
		int[] con = new int[K];
		for(int i = 0; i < K; i++) {
			con[i] = sc.nextInt();
		}
		
		// 주사위의 전개도상 가로, 세로 배열
		int[] row = {0, 0, 0, 0};
		int[] col = {0, 0, 0, 0};
		
		// 동, 서, 북, 남 델타탐색 배열
		int[] dx = {0, 0, -1, 1};
		int[] dy = {1, -1, 0, 0};
		
		// 명령어 시작
		for(int i = 0; i < K; i++) {
			int d = con[i]-1;
			
			int cx = x + dx[d];
			int cy = y + dy[d];
			
			if(cx < 0 || cx >= N || cy < 0 || cy >= M) {
				continue;
			}
			
			x = cx;
			y = cy;
			
			if(d == 0) {
				
				int cycle = row[3];
				row[3] = 0;
				for(int j = 2; j >= 0; j--) {
					row[j+1] = row[j];
				}
				row[0] = cycle;
				col[3] = row[3];
				col[1] = row[1];
				
				if(arr[x][y] == 0) {
					arr[x][y] = row[3];
				} else {
					row[3] = arr[x][y];
					arr[x][y] = 0;
					col[3] = row[3];
				}
			}
			else if(d == 1) {
				int cycle = row[0];
				row[0] = 0;
				for(int j = 0; j < 3; j++) {
					row[j] = row[j+1];
				}
				row[3] = cycle;
				col[3] = row[3];
				col[1] = row[1];
				
				if(arr[x][y] == 0) {
					arr[x][y] = row[3];
				} else {
					row[3] = arr[x][y];
					arr[x][y] = 0;
					col[3] = row[3];
				}
			}
			else if(d == 2) {
				int cycle = col[0];
				col[0] = 0;
				for(int j = 0; j < 3; j++) {
					col[j] = col[j+1];
				}
				col[3] = cycle;
				row[3] = col[3];
				row[1] = col[1];
				
				if(arr[x][y] == 0) {
					arr[x][y] = col[3];
				} else {
					col[3] = arr[x][y];
					arr[x][y] = 0;
					row[3] = col[3];
				}
			}
			else if(d == 3) {
				int cycle = col[3];
				col[3] = 0;
				for(int j = 2; j >= 0; j--) {
					col[j+1] = col[j];
				}
				col[0] = cycle;
				row[3] = col[3];
				row[1] = col[1];
				
				if(arr[x][y] == 0) {
					arr[x][y] = col[3];
				} else {
					col[3] = arr[x][y];
					arr[x][y] = 0;
					row[3] = col[3];
				}
			}
			System.out.println(col[1]);
		}
	}
}
