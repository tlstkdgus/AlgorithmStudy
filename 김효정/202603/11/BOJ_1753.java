import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BOJ_1753 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int V = Integer.parseInt(st.nextToken());
		int E = Integer.parseInt(st.nextToken());
		
		int start = Integer.parseInt(br.readLine().trim());
		
		List<Node>[] graph = new ArrayList[V+1];
		for (int i = 1; i <= V; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for (int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			graph[u].add(new Node(v,w));
		}
		
		PriorityQueue<Node> pq = new PriorityQueue<>();
		int[] minWeights = new int[V+1];
		Arrays.fill(minWeights, Integer.MAX_VALUE);
		minWeights[start] = 0;
		
		pq.offer(new Node(start, 0));
		
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			
			if (curr.w > minWeights[curr.v]) continue;
				
			for (Node e : graph[curr.v]) {
				int nextW = minWeights[curr.v] + e.w;
				
				if (nextW < minWeights[e.v]) {
					minWeights[e.v] = nextW;
					pq.offer(new Node(e.v, nextW));
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= V; i++) {
			if (minWeights[i] == Integer.MAX_VALUE) sb.append("INF\n");
			else sb.append(minWeights[i]).append("\n");
		}
		System.out.println(sb);
	}
}

class Node implements Comparable<Node>{
	int v;
	int w;
	
	public Node() {
	}

	public Node(int v, int w) {
		super();
		this.v = v;
		this.w = w;
	}

	@Override
	public int compareTo(Node o) {
		return this.w - o.w;
	}
}
