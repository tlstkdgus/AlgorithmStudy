package backtracking;

import java.util.Scanner;

public class BOJ_17406 {
	static int N, M, K;
	static int[][] arr;
	static int[][] cal;
	static int[][] rotate;
	static boolean[] visited;
	static int minArrValue = Integer.MAX_VALUE;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		M = sc.nextInt();
		K = sc.nextInt();
		
		sc.nextLine();
		
		arr = new int[N+1][M+1];
		
		for(int i = 1; i <= N; i++) {
			for(int j = 1; j <= M; j++) {
				arr[i][j] = sc.nextInt();
			}
		}
		
		visited = new boolean[K];
		
		cal = new int[K][3];
		for(int i = 0; i < K; i++) {
			for(int j = 0; j < 3; j++) {
				cal[i][j] = sc.nextInt(); 
			}
		}
		
		rotate = new int[K][3];
		
		perm(0);
		
		System.out.println(minArrValue);
	}
	
	static void perm(int depth) {
		if(depth == K) {
			calculate();
			return;
		}
		
		for(int i = 0; i < K; i++) {
			if(!visited[i]) {
				visited[i] = true;
				rotate[depth] = cal[i];
				
				perm(depth + 1);
				
				visited[i] = false;
			}
		}
	}
	
	static void calculate() {
		int[][] copy = new int[N+1][M+1];
		for(int j = 1; j <= N; j++) {
			for(int k = 1; k <= M; k++) {
				copy[j][k] = arr[j][k];
			}
		}
		
		for(int i = 0; i < rotate.length; i++) {
			int r = rotate[i][0];
			int c = rotate[i][1];
			int s = rotate[i][2];
			rotateMap(copy, r, c, s);
		}
		
		int currentMinValue = Integer.MAX_VALUE;
		
		for(int i = 1; i <= N; i++) {
			int rowSum = 0;
			for(int j = 1; j <= M; j++) {
				rowSum += copy[i][j];
			}
			currentMinValue = Math.min(currentMinValue, rowSum);
		}
		
		minArrValue = Math.min(currentMinValue, minArrValue);
	}
	
	static void rotateMap(int[][] copyMap, int r, int c, int s) {
		for(int i = 1; i <= s; i++) {
			int top = r-i;
			int bottom = r+i;
			int left = c-i;
			int right = c+i;
			
			int temp = copyMap[top][left];
			
			//왼쪽 변
			for(int j = top; j < bottom; j++) {
				copyMap[j][left] = copyMap[j+1][left];
			}
			//아래쪽 변
			for(int j = left; j < right; j++) {
				copyMap[bottom][j] = copyMap[bottom][j+1];
			}
			//오른쪽 변
			for(int j = bottom; j > top; j--) {
				copyMap[j][right] = copyMap[j-1][right];
			}
			//왼쪽 변
			for(int j = right; j > left+1; j--) {
				copyMap[top][j] = copyMap[top][j-1];
			}
			
			copyMap[top][left+1] = temp;
		}
	}
}
