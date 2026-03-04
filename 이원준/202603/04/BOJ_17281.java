package simulation;

import java.util.Scanner;

public class BOJ_17281 {
	static int[] lineup = new int[10];
	static boolean[] visited = new boolean[10];
	static int N, maxScore;
	static int[][] result;
	
	
	static void makeLineup(int depth) {
		if(depth == 10) {
			playGame();
			return;
		}
		
		if(depth == 4) {
			makeLineup(depth + 1);
			return;
		}
		
		for(int i = 2; i <= 9; i++) {
			if(!visited[i]) {
				visited[i] = true;
				lineup[depth] = i;
				
				makeLineup(depth + 1);
				
				visited[i] = false;
			}
		}
	}
	
	static void playGame() {
		int score = 0;
		
		int batterIdx = 1;
		
		for(int inning = 1; inning <= N; inning++) {
			int outs = 0;
			boolean[] base = new boolean[4];
			
			while(outs < 3) {
				int player = lineup[batterIdx];
				
				int hit = result[inning][player];
				
				if(hit == 0) {
					outs++;
				} else {
					if(hit == 1) {
						if(base[3] == true) {
							score++;
							base[3] = false;
						}
						base[3] = base[2];
						base[2] = base[1];
						base[1] = true;
					}
					else if(hit == 2) {
						if(base[3] == true) {
							score++;
							base[3] = false;
						}
						if(base[2] == true) {
							score++;
							base[2] = false;
						}
						base[3] = base[1];
						base[2] = true;
						base[1] = false;
					}
					else if(hit == 3) {
						if(base[3] == true) {
							score++;
							base[3] = false;
						}
						if(base[2] == true) {
							score++;
							base[2] = false;
						}
						if(base[1] == true) {
							score++;
							base[1] = false;
						}
						base[3] = true;
						base[2] = false;
						base[1] = false;
					}
					else if(hit == 4) {
						if(base[3] == true) {
							score++;
							base[3] = false;
						}
						if(base[2] == true) {
							score++;
							base[2] = false;
						}
						if(base[1] == true) {
							score++;
							base[1] = false;
						}
						score++;
					}
				}
				batterIdx++;
				
				if(batterIdx > 9) {
					batterIdx = 1;
				}
 			}
		}
		maxScore = Math.max(maxScore, score);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		
		result = new int[N+1][10];
		
		lineup[4] = 1;
		visited[1] = true;
		maxScore = Integer.MIN_VALUE;
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= 9; j++) {
				result[i][j] = sc.nextInt();
			}
		}
		
		makeLineup(1);
		System.out.println(maxScore);
	}
}
