import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.StringTokenizer;

public class BOJ_17471 {
	static List<List<Integer>> powSetA, powSetB;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int N = Integer.parseInt(st.nextToken());
		int[] populations = new int[N + 1];
		int totalPop = 0;
		Map<Integer, List<Integer>> districts = new HashMap<>();

		st = new StringTokenizer(br.readLine());
		for (int i = 1; i <= N; i++) {
			int temp = Integer.parseInt(st.nextToken());
			populations[i] = temp;
			totalPop += temp;
		}

		for (int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine());
			List<Integer> connected = new ArrayList<>();
			int nConnected = Integer.parseInt(st.nextToken());
			for (int j = 0; j < nConnected; j++) {
				connected.add(Integer.parseInt(st.nextToken()));
			}
			districts.put(i, connected);
		}

		boolean[] isIncluded = new boolean[N + 1];
		powSetA = new ArrayList<>();
		powSetB = new ArrayList<>();

		powerset(isIncluded, N, 1);

		Queue<Integer> q = new LinkedList<>();
		int minDiff = Integer.MAX_VALUE;
		boolean possible = false;

		for (int i = 0; i < powSetA.size(); i++) {
			List<Integer> distA = powSetA.get(i);
			List<Integer> distB = powSetB.get(i);
			boolean[] isVisitedA = new boolean[N + 1];
			boolean[] isVisitedB = new boolean[N + 1];
			
			q.clear();
			boolean validA = search(q, districts, distA, isVisitedA);
			q.clear();
			boolean validB = search(q, districts, distB, isVisitedB);
			if (validA && validB) {
				possible = true;
				int popA = 0, popB = 0;
				for (int idx = 0; idx < distA.size(); idx++) {
					popA += populations[distA.get(idx)];
				}
				popB = totalPop - popA;
				int diff = Math.abs(popA - popB);
				minDiff = Math.min(diff, minDiff);
			} else
				continue;
		}

		if (possible)
			System.out.print(minDiff);
		else
			System.out.print("-1");

	}

	public static void powerset(boolean[] isIncluded, int nDist, int idx) {
		if (idx == nDist) {
			List<Integer> distA = new ArrayList<>();
			List<Integer> distB = new ArrayList<>();
			for (int i = 1; i <= nDist; i++) {
				if (isIncluded[i])
					distA.add(i);
				else
					distB.add(i);
			}
			powSetA.add(distA);
			powSetB.add(distB);
			return;
		}

		isIncluded[idx] = true;
		powerset(isIncluded, nDist, idx + 1);
		isIncluded[idx] = false;
		powerset(isIncluded, nDist, idx + 1);
	}

	public static boolean search(Queue<Integer> q, Map<Integer, List<Integer>> dists, List<Integer> distList,
			boolean[] isVisited) {

		if (distList.isEmpty())
			return false;

		int start = distList.get(0);
		q.offer(start);
		isVisited[start] = true;

		while (!q.isEmpty()) {
			int current = q.poll();

			List<Integer> adjs = dists.get(current);
			if (adjs != null) {
				for (int adj : adjs) {
					if (!isVisited[adj] && distList.contains(adj)) {
						isVisited[adj] = true;
						q.offer(adj);
					}
				}
			}
		}

		for (int dist : distList) {
			if (!isVisited[dist])
				return false;
		}

		return true;
	}

//	public boolean search(Queue<Integer> q, Map<Integer, List<Integer>> dists, List<Integer> distList,
//			boolean[] isVisited) {
//		if (q.isEmpty()) {
//			for (int dist : distList) {
//				if (!isVisited[dist])
//					return false;
//			}
//			return true;
//		}
//
//		else {
//			int i = q.poll();
//			isVisited[i] = true;
//			List<Integer> adjs = dists.get(i);
//			if (adjs.size() != 0) {
//				for (int idx = 0; idx < adjs.size(); idx++) {
//					q.offer(adjs.get(idx));
//				}
//			}
//			return search(q, dists, distList, isVisited);
//		}
//
//	}
}
