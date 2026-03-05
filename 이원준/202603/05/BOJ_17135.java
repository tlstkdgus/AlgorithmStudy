package combination;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BOJ_17135 {
	static int N, M, D;
	static int[][] defence;
	static int[] archers = new int[3];
	static int maxKills;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		D = sc.nextInt();
		maxKills = Integer.MIN_VALUE;
		
		sc.nextLine();
		
		defence = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				defence[i][j] = sc.nextInt();
			}
		}
		
		combination(0, 0);
		
		System.out.println(maxKills);
	}
	
	static void combination(int depth, int start) {
		if(depth == 3) {
			playGame(archers);
			return;
		}
		
		for(int i = start; i < M; i++) {
			archers[depth] = i;
			combination(depth + 1, i + 1);
		}
	}
	
	static void playGame(int[] archers) {
		// 원본 맵 복사
		int[][] gameStart = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				gameStart[i][j] = defence[i][j];
			}
		}
		
		int currentKills = 0;
		
		for(int turn = 0; turn < N; turn++) {
			List<int[]> targets = new ArrayList<>();
			findTarget(targets, gameStart);
			
			for(int[] arr : targets) {
				int r = arr[0];
				int c = arr[1];
				
				if(gameStart[r][c] == 1) {
					gameStart[r][c] = 0;
					currentKills++;
				}
			}
			
			for(int i = N-1; i >= 1; i--) {
				for(int j = 0; j < M; j++) {
					gameStart[i][j] = gameStart[i-1][j];
				}
			}
			
			for(int j = 0; j < M; j++) {
				gameStart[0][j] = 0;
			}
		}
		
		maxKills = Math.max(maxKills, currentKills);
	}
	
	static void findTarget(List<int[]> targets, int[][] gameStart) {
		for(int i = 0; i < 3; i++) {
			int archerR = N;
			int archerC = archers[i];
			
			int minDist = Integer.MAX_VALUE;
			int minR = -1;
			int minC = -1;
			
			for(int j = N-1; j >= 0; j--) {
				for(int k = 0; k < M; k++) {
					if(gameStart[j][k] == 1) {
						int dist = Math.abs(archerR - j) + Math.abs(archerC - k);
						
						if(dist <= D) {
							if(dist < minDist) {
								minDist = dist;
								minR = j;
								minC = k;
							} else if (dist == minDist) {
								if(k < minC) {
									minR = j;
									minC = k;
								}
							}
						}
					}
				}
			}
			if(minDist != Integer.MAX_VALUE) {
				targets.add(new int[] {minR, minC});
			}
		}
	}
}
