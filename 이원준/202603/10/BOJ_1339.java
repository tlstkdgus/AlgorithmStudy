package backtracking;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class BOJ_1339 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		
		int ans = 0;
		
		int[] alphabet = new int[26];
		
		for(int i = 0; i < N; i++) {
			
			String word = sc.next();
			int len = word.length();
			
			for(int j = 0; j < len; j++) {
				int idx = word.charAt(j) - 'A';
				alphabet[idx] += (int) Math.pow(10, len - 1 - j);
			}
		}
		
		Arrays.sort(alphabet);
		
		int num = 9;
		while(num > 0) {
			for(int i = alphabet.length - 1; i >= 0; i--) {
				ans += num*alphabet[i];
				num--;
			}
		}
		
		System.out.println(ans);
	}
}
