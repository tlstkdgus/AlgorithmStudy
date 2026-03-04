import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BOJ_14284 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int n = Integer.parseInt(st.nextToken());
		int m = Integer.parseInt(st.nextToken());

		int inf = Integer.MAX_VALUE;
		int[] minWeights = new int[n + 1];
		Arrays.fill(minWeights, inf);

		List<Node>[] graph = new ArrayList[n + 1];

		for (int i = 0; i < graph.length; i++) {
			graph[i] = new ArrayList<>();
		}

		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine());
			int s = Integer.parseInt(st.nextToken());
			int f = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			graph[s].add(new Node(f, w));
			graph[f].add(new Node(s, w));
		}

		st = new StringTokenizer(br.readLine());
		int s = Integer.parseInt(st.nextToken());
		int t = Integer.parseInt(st.nextToken());

		PriorityQueue<Node> pQ = new PriorityQueue<>();
		pQ.offer(new Node(s, 0));
		minWeights[s] = 0;

		while (!pQ.isEmpty()) {
			Node curr = pQ.poll();

			if (minWeights[curr.vertex] < curr.weight)
				continue;

			for (Node next : graph[curr.vertex]) {
				int weight = minWeights[curr.vertex] + next.weight;

				if (weight < minWeights[next.vertex]) {
					minWeights[next.vertex] = weight;
					pQ.offer(new Node(next.vertex, weight));
				}
			}
		}

		System.out.println(minWeights[t]);

	}
}

class Node implements Comparable<Node> {
	int vertex;
	int weight;

	public Node(int vertex, int weight) {
		super();
		this.vertex = vertex;
		this.weight = weight;
	}

	@Override
	public int compareTo(Node o) {
		return this.weight - o.weight;
	}
}
