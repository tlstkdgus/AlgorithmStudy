package com.ssafy.swea;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class SWEA_1868 {
	static int T, N;
	static char[][] bomb;
	static int[][] mineCount;
	static boolean[][] visited;
	static int clickCount;
	static int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[] dc = {0, 0, -1, 1, -1, 1, -1, 1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		T = sc.nextInt();
		for(int tc = 1; tc <= T ; tc++) {
			N = sc.nextInt();
			bomb = new char[N][N];
			for(int i = 0; i < N; i++) {
				String line = sc.next();
				for(int j = 0; j < N; j++) {
					bomb[i][j] = line.charAt(j);
				}
			}
			
			mineCount = new int[N][N];
			visited = new boolean[N][N];
			clickCount = 0;
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(bomb[i][j] == '.') {
						mineCount[i][j] = bombCount(i, j);
					}
				}
			}
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(bomb[i][j] == '.' && visited[i][j] == false && mineCount[i][j] == 0) {
						clickCount++;
						bfs_bomb(i, j);
					}
				}
			}
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(bomb[i][j] == '.' && visited[i][j] == false) {
						clickCount++;
						visited[i][j] = true;
					}
				}
			}
			
			System.out.println("#"+tc+" "+clickCount);
		}
	}
	
	static int bombCount(int r, int c) {
		int count = 0;
		for(int d = 0; d < 8; d++) {
			int nr = r + dr[d];
			int nc = c + dc[d];
			
			if(nr >= 0 && nr < N && nc >= 0 && nc < N && bomb[nr][nc] == '*') {
				count++;
			}
		}
		return count;
	}
	
	static void bfs_bomb(int startR, int startC) {
		Queue<int[]> points = new LinkedList<>();
		points.offer(new int[] {startR, startC});
		visited[startR][startC] = true;
		
		while(!points.isEmpty()) {
			int[] curr = points.poll();
			int currR = curr[0];
			int currC = curr[1];
			
			for(int d = 0; d < 8; d++) {
				int nextR = currR + dr[d];
				int nextC = currC + dc[d];
				
				if(nextR < 0 || nextR >= N || nextC < 0 || nextC >= N || visited[nextR][nextC] == true) {
					continue;
				}
				
				if(bomb[nextR][nextC] == '.') {
					visited[nextR][nextC] = true;
					if(mineCount[nextR][nextC] == 0) {
						points.offer(new int[] {nextR, nextC});
					}
				}
			}
		}
	}
}
