package com.ssafy.swea;

import java.util.Scanner;

public class SWEA_8275 {
	static int T;
	static int N, X, M;
	static int[][] notes;
	static int[] place;
	
	static int[] cages;
	static int[] bestCages;
	static int maxSum;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		T = sc.nextInt();
		
		for(int tc = 1; tc <= T; tc++) {
			N = sc.nextInt();
			X = sc.nextInt();
			M = sc.nextInt();
			
			sc.nextLine();
			
			notes = new int[M][3];
			for(int i = 0; i < M; i++) {
				notes[i][0] = sc.nextInt();
				notes[i][1] = sc.nextInt();
				notes[i][2] = sc.nextInt();
			}
			
			cages = new int[N+1];
			bestCages = new int[N+1];
			maxSum = -1;
			
			dfs(1, 0);
			if(maxSum == -1) {
				System.out.println("#"+tc+" "+maxSum);
			} else {
				System.out.print("#"+tc);
				for(int i = 1; i <= N; i++) {
					System.out.print(" "+bestCages[i]);
				}
				System.out.println();
			}
		}
	}
	
	static void dfs(int idx, int currentSum) {
		if(idx == N + 1) {
			boolean isValid = true;
			for(int i = 0; i < M; i++) {
				int l = notes[i][0];
				int r = notes[i][1];
				int s = notes[i][2];
				
				int partSum = 0;
				for(int j = l; j < r+1; j++) {
					partSum += cages[j];
				}
				
				if(partSum != s) {
					isValid = false;
					break;
				}
			}
			
			if(isValid) {
				if(currentSum > maxSum) {
					maxSum = currentSum;
					for(int i = 0; i < cages.length; i++) {
						bestCages[i] = cages[i];
					}
				}
			}
			return;
		}
		
		for(int i = 0; i < X+1; i++) {
			cages[idx] = i;
			dfs(idx + 1, currentSum + i);
		}
	}
}
