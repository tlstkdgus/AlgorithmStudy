import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_16236 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		
		int[][] arr = new int[N][N];
		
		for (int i=0; i<N;i++) {
			st = new StringTokenizer(br.readLine());
			for (int j =0; j<N;j++) {
				int temp = Integer.parseInt(st.nextToken());
				if (temp == 9) {
					int initRow = i;
					int initCol = j;
				}
				arr[i][j] = temp;
			}
		}
		
		
		
	}

}
