import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_1202_old {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());

		int[] mass = new int[N];
		int[] value = new int[N];

		double[] v2m = new double[N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			mass[i] = Integer.parseInt(st.nextToken());
			value[i] = Integer.parseInt(st.nextToken());
			v2m[i] = (double) value[i] / mass[i];
		}

		int[] capa = new int[K];
		for (int i = 0; i < K; i++) {
			st = new StringTokenizer(br.readLine());
			capa[i] = Integer.parseInt(st.nextToken());
		}

		int nBag = K;
		int totalValue = 0;

		while (nBag > 0) {
			double maxV2M = 0;
			int maxIdx = 0;
			for (int i = 0; i < N; i++) {
				if (v2m[i] > maxV2M) {
					maxV2M = v2m[i];
					maxIdx = i;
				}
			}

			int minCapa = Integer.MAX_VALUE;
			int minCapaIdx = -1;

			for (int i = 0; i < K; i++) {
				if (mass[maxIdx] <= capa[i] && capa[i] < minCapa) {
					minCapa = capa[i];
					minCapaIdx = i;
				}
			}

			if (minCapaIdx != -1) {
				totalValue += value[maxIdx];
				capa[minCapaIdx] = 0;
				mass[maxIdx] = -1;
				value[maxIdx] = -1;
				v2m[maxIdx] = -1;
				nBag--;
			}

		}
		System.out.println(totalValue);
	}

}
