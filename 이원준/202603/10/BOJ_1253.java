package backtracking;

import java.util.Arrays;
import java.util.Scanner;

public class BOJ_1253 {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		int[] numArr = new int[N];
		for(int i = 0; i < N; i++) {
			numArr[i] = sc.nextInt();
		}
		
		Arrays.sort(numArr);
		int count = 0;
		
		for(int i = 0; i < N; i++) {
			int target = numArr[i];
			
			int left = 0;
			int right = N - 1;
			
			while(left < right) {
				if(left == i) {
					left++;
					continue;
				}
				
				if(right == i) {
					right--;
					continue;
				}
				
				int sum = numArr[left] + numArr[right];
				
				if(sum == target) {
					count++;
					break;
				} else if(sum < target) {
					left++;
				} else if(sum > target) {
					right--;
				}
			}
		}
		
		System.out.println(count);
		
	}	
}
