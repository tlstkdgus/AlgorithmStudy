import java.util.Scanner;

public class BOJ_2448 {
	static char[][] figure;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		figure = new char[N][2*N];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 2*N; j++) {
				figure[i][j] = ' ';
			}
		}
		
		
		draw(0, N-1, N);
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 2*N; j++) {
				sb.append(figure[i][j]);
			}
			sb.append("\n");
		}
		
		System.out.println(sb);

	}

	static void draw(int r, int c, int N) {
		if (N == 3) {
			figure[r][c] = '*';
			figure[r+1][c-1] = '*';
			figure[r+1][c+1] = '*';
			for (int j = c-2; j <= c+2; j++) {
				figure[r+2][j] = '*';
			}
			return;
		}
		
		draw(r, c, N/2);
		draw(r + N/2, c - N/2, N/2);
		draw(r + N/2, c + N/2, N/2);
	}
}
