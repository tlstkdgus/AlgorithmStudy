import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Samsung_2025_SH_AM_1 {
	static int N, M, nBox;
	static int[][] storage;
	static boolean[] isInStorage;
	static int[][] boxes;
	static List<Integer> insertOrder;
	static StringBuilder sb;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		storage = new int[N + 1][N + 1];
		isInStorage = new boolean[100];
		boxes = new int[100][4];
		insertOrder = new ArrayList<>();

		nBox = 0;

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int k = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());

			load(k, h, w, c);
			insertOrder.add(k);
			isInStorage[k] = true;
			nBox++;
		}

		sb = new StringBuilder();

		while (nBox > 0) {
			if (!unloadLeft() & !unloadRight()) break;
		}
		
		System.out.println(sb);

	}

	static void load(int k, int h, int w, int c) {
		int bottomRow = N;
		for (int row = h; row < N; row++) {
			int nextRow = row + 1;
			boolean isClear = true;
			for (int col = c; col < c + w; col++) {
				if (storage[nextRow][col] != 0) {
					isClear = false;
					break;
				}
			}
			if (!isClear) {
				bottomRow = row;
				break;
			}
		}

		for (int row = bottomRow; row > bottomRow - h; row--) {
			for (int col = c; col < c + w; col++) {
				storage[row][col] = k;
			}
		}
		boxes[k][0] = h;
		boxes[k][1] = w;
		boxes[k][2] = bottomRow;
		boxes[k][3] = c;
	}

	static boolean unloadLeft() {
		for (int id = 1; id < 100; id++) {
			if (!isInStorage[id])
				continue; // 없으면 패스

			int h = boxes[id][0];
			int w = boxes[id][1];
			int r = boxes[id][2];
			int c = boxes[id][3];

			// 왼쪽 비어있는지 탐색
			boolean isleftClear = true;
			for (int col = c; col > 1; col--) {
				int leftCol = col - 1;
				for (int row = r; row > r - h; row--) {
					if (storage[row][leftCol] != 0) {
						isleftClear = false;
						break;
					}
				}
				if (!isleftClear)
					break;
			}

			if (isleftClear) {
				isInStorage[id] = false;
				nBox--;
				sb.append(id).append("\n");

				for (int row = r; row > r - h; row--) {
					for (int col = c; col < c + w; col++) {
						storage[row][col] = 0;
					}
				}
				
				applyGravity();
				return true;
			}
		}
		return false;
	}

	static boolean unloadRight() {
		for (int id = 1; id < 100; id++) {
			if (!isInStorage[id])
				continue; // 없으면 패스

			int h = boxes[id][0];
			int w = boxes[id][1];
			int r = boxes[id][2];
			int c = boxes[id][3];
			
			boolean isRightClear = true;
			for (int col = c + w; col <= N; col++) {
			    for (int row = r; row > r - h; row--) {
			        if (storage[row][col] != 0) {
			            isRightClear = false;
			            break;
			        }
			    }
			    if (!isRightClear) break;
			}

//			boolean isRightClear = true;
//			for (int col = c + w - 1; col <= N; col++) {
//				int RightCol = col + 1;
//				for (int row = r; row > r - h; row--) {
//					if (storage[row][RightCol] != 0) {
//						isRightClear = false;
//						break;
//					}
//				}
//				if (!isRightClear)
//					break;
//			}

			// 비어있다면 제거
			if (isRightClear) {
				isInStorage[id] = false;
				nBox--;
				sb.append(id).append("\n");

				for (int row = r; row > r - h; row--) {
					for (int col = c; col < c + w; col++) {
						storage[row][col] = 0;
					}
				}
				
				applyGravity();
				return true;
			}
		}
		return false;

	}
	
	static void applyGravity() {
		for (int k : insertOrder) {
	        if (!isInStorage[k]) continue;

	        int boxH = boxes[k][0];
	        int boxW = boxes[k][1];
	        int boxR = boxes[k][2];
	        int boxC = boxes[k][3];

	        for (int row = boxR; row > boxR - boxH; row--) {
	            for (int col = boxC; col < boxC + boxW; col++) {
	                storage[row][col] = 0;
	            }
	        }

	        int bottomRow = N;
	        for (int row = boxR; row < N; row++) {
	            boolean isBottomClear = true;
	            for (int col = boxC; col < boxC + boxW; col++) {
	                if (storage[row + 1][col] != 0) {
	                    isBottomClear = false;
	                    break;
	                }
	            }
	            if (!isBottomClear) {
	                bottomRow = row;
	                break;
	            }
	        }

	        for (int row = bottomRow; row > bottomRow - boxH; row--) {
	            for (int col = boxC; col < boxC + boxW; col++) {
	                storage[row][col] = k;
	            }
	        }
	        
	        boxes[k][2] = bottomRow; 
	    }
	}

}
