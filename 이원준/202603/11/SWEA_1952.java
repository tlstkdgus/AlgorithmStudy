package com.ssafy.swea;

import java.util.Scanner;

public class SWEA_1952 {
	static int d, m1, m3, y;
	static int[] month;
	static int ans;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		int T = sc.nextInt();
		
		for(int tc = 1; tc <= T; tc++) {
			d = sc.nextInt();	// 1일권
			m1 = sc.nextInt();	// 1달권
			m3 = sc.nextInt();	// 3달권
			y = sc.nextInt();	// 1년권
			
			month = new int[13];
			for(int i = 1; i < 13; i++) {
				month[i] = sc.nextInt();
			}
			
			ans = y;	// 1년권으로 초기화
			swimfee(1, 0);
			System.out.println("#"+tc+" "+ans);
		}
	}
	
	// m: 이용권을 고르기 위한 달
	// fee: 지금까지 누적해온 비용
	static void swimfee(int m, int fee) {
		if(fee >= ans) return;
		
		//종료파트
		if(m > 12) {
			ans = Math.min(ans, fee);
			return;
		}
		
		if(month[m] == 0) {
			swimfee(m + 1, fee);
		}
		else {
			swimfee(m + 1, fee + (d * month[m]));
			
			swimfee(m + 1, fee + m1);
			
			swimfee(m + 3, fee + m3);
		}
	}
}	
