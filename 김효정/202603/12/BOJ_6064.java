import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_6064 {
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine().trim());
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int M = Integer.parseInt(st.nextToken());
			int N = Integer.parseInt(st.nextToken());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			
			int k = 0;
			int xi = 1, yi = 1;
			boolean flag = false;
			while ((xi!=M) || (yi!=N)) {
				k++;
				if (xi == x && yi == y) {
					flag = true;
					break;
				}
				xi = (xi < M) ? xi + 1 : 1;
				yi = (yi < N) ? yi + 1 : 1;
			}
			if (flag) System.out.println(k);
			else System.out.println(-1);
		}
	}
}
