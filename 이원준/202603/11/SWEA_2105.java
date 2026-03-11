package com.ssafy.swea;

import java.util.Scanner;

public class SWEA_2105 {
	static int T, N;
	static int[][] map;
	static boolean[] visitedDessert;
	static int startX;
	static int startY;
	static int maxDessert;
	
	static int[] dx = {1, 1, -1, -1};
	static int[] dy = {1, -1, -1, 1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		T = sc.nextInt();
		for(int tc = 1; tc <= T; tc++) {
			N = sc.nextInt();
			map = new int[N][N];
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					map[i][j] = sc.nextInt();
				}
			}
			visitedDessert = new boolean[101];
			maxDessert = -1;
			for(int i = 0; i < N-1; i++) {
				for(int j = 0; j < N-1; j++) {
					startX = i;
					startY = j;
					
					visitedDessert[map[i][j]] = true;
					
					dfs(i, j, 0, 1);
					
					visitedDessert[map[i][j]] = false;
				}
			}
			System.out.println("#"+tc+" "+maxDessert);
		}
	}
	
	static void dfs(int r, int c, int dir, int count) {
		for(int i = dir; i < dir+2; i++) {
			if(i == 4) {
				break;
			}
			int next_R = r + dx[i];
			int next_C = c + dy[i];
			
			if(next_R == startX && next_C == startY && i == 3) {
				maxDessert = Math.max(maxDessert, count);
				return;
			}
			
			if(next_R < 0 || next_R >= N || next_C < 0 || next_C >= N) {
				continue;
			}
			
			if(!visitedDessert[map[next_R][next_C]]) {
				visitedDessert[map[next_R][next_C]] = true;
				dfs(next_R, next_C, i, count + 1);
				visitedDessert[map[next_R][next_C]] = false;
			}
		}
	}
}
