import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_2630 {
	static int[][] paper;
	static int[] cnt;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int N = Integer.parseInt(br.readLine().trim());
		paper = new int[N][N];
		StringTokenizer st;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				paper[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		cnt = new int[2];
		divide(0, 0, N);

		System.out.println(cnt[0]);
		System.out.println(cnt[1]);

	}

	static void divide(int r, int c, int N) {
		int color = paper[r][c];
		boolean isSquare = true;
		for (int i = r; i < r + N; i++) {
			for (int j = c; j < c + N; j++) {
				if (paper[i][j] != color) {
					isSquare = false;
					break;
				}
			}
		}

		if (isSquare) {
			cnt[color]++;
			return;
		}

		int[] rP = { r, r, r + N / 2, r + N / 2 };
		int[] cP = { c, c + N / 2, c, c + N / 2 };

		for (int n = 0; n < 4; n++) {
			divide(rP[n], cP[n], N / 2);
		}
	}
}
