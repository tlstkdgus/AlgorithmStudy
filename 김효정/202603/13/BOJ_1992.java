import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BOJ_1992 {
	static int[][] image;
	static StringBuilder sb;

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int N = Integer.parseInt(br.readLine().trim());
		image = new int[N][N];
		for (int i = 0; i < N; i++) {
			String st = br.readLine();
			for (int j = 0; j < N; j++) {
				image[i][j] = st.charAt(j) - '0';
			}
		}

		sb = new StringBuilder();
		compress(0, 0, N);
		System.out.println(sb);

	}

	static void compress(int r, int c, int N) {
		int test = image[r][c];
		boolean flag = true;

		for (int i = r; i < r + N; i++) {
			for (int j = c; j < c + N; j++) {
				if (image[i][j] != test) {
					flag = false;
					break;
				}
			}
		}

		if (flag) {
			sb.append(test);
			return;
		}
		sb.append("(");
		int[] rP = { r, r, r + N / 2, r + N / 2 };
		int[] cP = { c, c + N / 2, c, c + N / 2 };

		for (int n = 0; n < 4; n++) {
			compress(rP[n], cP[n], N / 2);
		}
		sb.append(")");

	}
}