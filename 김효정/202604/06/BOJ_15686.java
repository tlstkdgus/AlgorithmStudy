import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BOJ_15686 {
	static int N, M;
	static List<int[]> chickens = new ArrayList<>();
	static List<int[]> houses = new ArrayList<>();
	static boolean isSel[];
	static int minDist;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				int tmp = Integer.parseInt(st.nextToken());
				if (tmp == 2) {
					chickens.add(new int[] { i, j });
				}
				if (tmp == 1) {
					houses.add(new int[] { i, j });
				}
			}
		}

		minDist = Integer.MAX_VALUE;
		isSel = new boolean[chickens.size()];
		comb(0, 0);
		System.out.println(minDist);

	}

	static void comb(int idx, int sel) {
		if (sel == M) {
			int tmpDist = 0;
			for (int i = 0; i < houses.size(); i++) {
				int[] housePos = houses.get(i);
				int distOneHouse = Integer.MAX_VALUE;
				for (int j = 0; j < chickens.size(); j++) {
					if (isSel[j]) {
						int[] chickPos = chickens.get(j);
						int tmpOneHouse = getDist(chickPos[0], chickPos[1], housePos[0], housePos[1]);
						distOneHouse = Math.min(distOneHouse, tmpOneHouse);
					}
				}
				tmpDist += distOneHouse;
			}

			minDist = Math.min(minDist, tmpDist);
			return;
		}

		for (int i = idx; i < chickens.size(); i++) {
			isSel[i] = true;
			comb(i + 1, sel + 1);
			isSel[i] = false;
		}
	}

	static int getDist(int ri, int ci, int rf, int cf) {
		return Math.abs(ri - rf) + Math.abs(ci - cf);
	}
}
