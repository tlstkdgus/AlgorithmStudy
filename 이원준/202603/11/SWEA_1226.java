package com.ssafy.swea;

import java.util.Scanner;

public class SWEA_1226 {
	static int tNum;
	static int startX, startY;
	static int finishX, finishY;
	static int[][] map;
	static boolean[][] visited;
	static boolean isExist;
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		for(int tc = 1; tc <= 10; tc++) {
			tNum = sc.nextInt();
			map = new int[16][16];
			
			startX = 0;
			startY = 0;
			finishX = 0;
			finishY = 0;
			isExist = false;
			
			for(int i = 0; i < 16; i++) {
				String line = sc.next();
				for(int j = 0; j < 16; j++) {
					map[i][j] = line.charAt(j) - '0';
					if(map[i][j] == 2) {
						startX = i;
						startY = j;
					}
					if(map[i][j] == 3) {
						finishX = i;
						finishY = j;
					}
				}
			}
			
			visited = new boolean[16][16];
			
			dfs(startX, startY);
			System.out.println("#"+tNum+" "+((isExist) ? 1 : 0));
		}
	}
	
	static void dfs(int x, int y) {
		visited[x][y] = true;
		if(map[x][y] == 3) {
			isExist = true;
			return;
		}
		
		for(int d = 0; d < 4; d++) {
			int cx = x + dx[d];
			int cy = y + dy[d];
			
			if(cx < 0 || cx >= 16 || cy < 0 || cy >= 16) continue;
			if(map[cx][cy] == 1 || visited[cx][cy] == true) continue;
			
			dfs(cx, cy);
		}
	}
}

