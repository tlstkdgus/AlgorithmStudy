package backtracking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BOJ_14502 {
	static int N, M;
	static int[][] originalMap;
	static List<int[]> blankList;
	static int[][] newWallArr;
	static int maxPlace;
	static int[] dr = {-1, 1, 0, 0};
	static int[] dc = {0, 0, -1, 1};
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		
		maxPlace = Integer.MIN_VALUE;
		blankList = new ArrayList<>();
		
		originalMap = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				originalMap[i][j] = sc.nextInt();
				if(originalMap[i][j] == 0) {
					blankList.add(new int[] {i,j});
				}
			}
		}
		
		newWallArr = new int[3][2];
		
		comb(0, 0);
		System.out.println(maxPlace);
	}
	
	static void comb(int depth, int start) {
		if(depth == 3) {
			goVirus();
			return;
		}
		
		for(int i = start; i < blankList.size(); i++) {
			newWallArr[depth] = blankList.get(i);
			comb(depth + 1, i + 1);
		}
	}
	
	static void goVirus() {
		int currentBlank = 0;
		int[][] copyMap = new int[N][M];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				copyMap[i][j] = originalMap[i][j];
			}
		}
		
		for(int[] point : newWallArr) {
			copyMap[point[0]][point[1]] = 1;
		}
		
		Queue<int[]> queue = new LinkedList<>();
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				if(copyMap[i][j] == 2) {
					queue.add(new int[] {i, j});
				}
			}
		}
		
		while(!queue.isEmpty()) {
			int[] curr = queue.poll();
			int r = curr[0];
			int c = curr[1];
			
			for(int d = 0; d < 4; d++) {
				int nr = r + dr[d];
				int nc = c + dc[d];
				
				if(nr >= 0 && nr < N && nc >= 0 && nc < M) {
					if(copyMap[nr][nc] == 0) {
						copyMap[nr][nc] = 2;
						queue.add(new int[] {nr, nc});
					}
				}
			}
		}
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				if(copyMap[i][j] == 0) {
					currentBlank++;
				}
			}
		}
		
		maxPlace = Math.max(currentBlank, maxPlace);
	}
	
	
}
