package BruteForce;

import java.util.Scanner;

public class BOJ_14500 {
	static int N, M;
	static int[][] arr;
	static int maxVal;
	static boolean[][] visited;
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		
		arr = new int[N][M];
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				arr[i][j] = sc.nextInt();
			}
		}
		
		maxVal = 0;
		visited = new boolean[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				visited[i][j] = true;
				dfs(i, j, 1, arr[i][j]);
				visited[i][j] = false;
			}
		}
		
		System.out.println(maxVal);
	}
	
	static void dfs(int r, int c, int depth, int sum) {
		if(depth == 4) {
			maxVal = Math.max(maxVal, sum);
			return;
		}
		
		for(int d = 0; d < 4; d++) {
			int nr = r + dx[d];
			int nc = c + dy[d];
			
			if(nr < 0 || nr >= N || nc < 0 || nc >= M || visited[nr][nc] == true) {
				continue;
			}
			
			if(depth == 2) {
				visited[nr][nc] = true;
				dfs(r, c, depth+1, sum+arr[nr][nc]);
				visited[nr][nc] = false;
			}
			
			visited[nr][nc] = true;
			dfs(nr, nc, depth+1, sum+arr[nr][nc]);
			visited[nr][nc] = false;
		}
	}
}
