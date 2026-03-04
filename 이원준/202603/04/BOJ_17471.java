package BOJ;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class BOJ_17471 {
	static int N;
	static int[] population;
	static ArrayList<Integer>[] adjList;
	static boolean[] isSelected;
	static int minDiff = Integer.MAX_VALUE;
	
	static void makeSubset(int depth) {
		if(depth == N + 1) {
			boolean isAConnected = isConnected(true);
			boolean isBConnected = isConnected(false);
			
			if(isAConnected && isBConnected) {
				calculatePopulationDifference();
			}
			return;
		}
		
		isSelected[depth] = true;
		makeSubset(depth+1);
		
		isSelected[depth] = false;
		makeSubset(depth+1);
	}
	
	static boolean isConnected(boolean targetTeam) {
		Queue<Integer> queue = new LinkedList<>();
		boolean[] visited = new boolean[N+1];
		
		int startNode = -1;
		int targetCount = 0;
		
		for(int i = 1; i <= N; i++) {
			if(isSelected[i] == targetTeam) {
				targetCount++;
				if(startNode == -1) {
					startNode = i;
				}
			}
		}
		
		if(targetCount == 0) return false;
		
		queue.offer(startNode);
		visited[startNode] = true;
		int count = 1;
		
		while (!queue.isEmpty()) {
			int curr = queue.poll();
			
			for(int next : adjList[curr]) {
				if(!visited[next] && isSelected[next] == targetTeam) {
					visited[next] = true;
					queue.offer(next);
					count++;
				}
			}
		}
		
		return count == targetCount;
	}
	
	static void calculatePopulationDifference() {
		int Asum = 0;
		int Bsum = 0;
		
		for(int i = 1; i <= N; i++) {
			if(isSelected[i]) {
				Asum += population[i];
			} else {
				Bsum += population[i];
			}
		}
		
		int diff = Math.abs(Asum - Bsum);
		minDiff = Math.min(diff, minDiff);
	}
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		N = sc.nextInt();
		population = new int[N+1];
		for(int i = 1; i <= N; i++) {
			population[i] = sc.nextInt();
		}
		
		adjList = new ArrayList[N + 1];
		
		for(int i = 1; i <= N; i++) {
			adjList[i] = new ArrayList<>();
		}
		
		for(int i = 1; i <= N; i++) {
			int adjCount = sc.nextInt();
			
			for(int j = 0; j < adjCount; j++) {
				int neighbor = sc.nextInt();
				adjList[i].add(neighbor);
			}
		}
		
		isSelected = new boolean[N+1];
		
		makeSubset(1);
		
		System.out.println(minDiff == Integer.MAX_VALUE ? -1 : minDiff);
	}
}
