import java.io.*;
import java.util.*;

/*
 * 설계 + 구현 : 50분
 * 디버깅 : 10분
 * 이건 좀 쉬운듯?
 */
/*
 * 구름 = 객체
 * 구름의 이동 과 물복사 버그 방향은 따로 고려하는게 실수 안할듯
 * 3.에서 구름을 굳이 삭제할 필요가 없다
 * 4.에서 구름 리스트를 이용해서 물복사버그
 * 5.에서 구름 리스트 돌면서 해당 위치로 boolean 보드판 만들고
 * 구름 리스트는 새로 초기화, 그리고 boolean 보드판을 이용해서
 * 새로운 구름 리스트로 대체
 */
/*
 * 실수한 점:
 * 1. 새롭게 이동된 좌표를 구했지만 객체 좌표를 갱신하지 않았음
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static class Cloud {
		int r, c;

		Cloud(int r, int c) {
			this.r = r;
			this.c = c;
		}
	}
	// 구름의 이동방향
	static int[] CDX = {0, 0, -1, -1, -1, 0, 1, 1, 1 }; // 1-based 주의
	static int[] CDY = {0, -1, -1, 0, 1, 1, 1, 0, -1 };

	// 물 복사 버그 방향
	static int[] WDX = {-1, -1, 1, 1};
	static int[] WDY = {-1, 1, 1, -1};
	
	static List<Cloud> clouds;
	static int N, M; // N은 보드판 길이, M은 명령 횟수
	static int[][] waters; // 1-based
	static int[][] orders;

	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		clouds = new ArrayList<>();
		waters = new int[N + 1][N + 1]; // 1-based
		orders = new int[M][2]; // 0-based

		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 1; c <= N; c++) {
				waters[r][c] = Integer.parseInt(st.nextToken());
			}
		}

		for (int oidx = 0; oidx < M; oidx++) {
			st = new StringTokenizer(br.readLine().trim());
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());

			orders[oidx][0] = d;
			orders[oidx][1] = s;
		}
	}
	
	static boolean outOfBorder(int r, int c) {
		return r < 1 || c < 1 || r > N || c > N;
	}
	
	static void print() {
		for(int r=1; r<=N; r++) {
			for(int c=1; c<=N; c++) {
				System.out.print(waters[r][c] +" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		init();
		
		int answer = doSimulation();
		
		bw.write(answer+"");
		bw.flush();
		bw.close();
	}
	
	static void initCloud() {
		Cloud c1 = new Cloud(N, 1);
		Cloud c2 = new Cloud(N, 2);
		Cloud c3 = new Cloud(N-1, 1);
		Cloud c4 = new Cloud(N-1, 2);
		
		clouds.add(c1);
		clouds.add(c2);
		clouds.add(c3);
		clouds.add(c4);
	}
	
	static int doSimulation() {
		initCloud();
		for(int[] order : orders) {
			int d = order[0];
			int s = order[1];
			
			// 구름의 이동
			moveCloud(d, s);
			
			copyWaterBug();
			
			makeCloud();
		}
		return getWaterSum();
	}
	
	
	static void moveCloud(int d, int s) {
		for(Cloud cloud : clouds) {
			int r = cloud.r;
			int c = cloud.c;
			// 이동 0-based로 만들어서 처리
			int nr = r + s * CDX[d] - 1;
			int nc = c + s * CDY[d] - 1;
			// 경계값 넘어가면 순환 처리, 다시 1-based로 복구
			nr = (nr + 1000 * N) % N + 1;
			nc = (nc + 1000 * N) % N + 1;
			
			cloud.r = nr;
			cloud.c = nc;
			
			waters[nr][nc] += 1;
		}
	}
	
	static void copyWaterBug() {
		for(Cloud cloud : clouds) {
			int r = cloud.r;
			int c = cloud.c;
			
			int waterCnt = 0;
			for(int dir = 0; dir < 4; dir++) {
				int nr = r + WDX[dir];
				int nc = c + WDY[dir];
				
				if(outOfBorder(nr, nc)) continue;
				if(waters[nr][nc] >= 1) {
					waterCnt++;
				}
			}
			
			waters[r][c] += waterCnt;
		}
	}
	
	static void makeCloud() {
		boolean[][] deadCloudPos = new boolean[N+1][N+1];
		for(Cloud cloud : clouds) {
			deadCloudPos[cloud.r][cloud.c] = true; 
		}
		
		// static clouds 리스트 새로 초기화
		clouds = new ArrayList<>();
		
		for(int row=1; row<=N; row++) {
			for(int col=1; col<=N; col++) {
				if(waters[row][col] >= 2 && !deadCloudPos[row][col]) {
					Cloud babyCloud = new Cloud(row, col);
					waters[row][col] -= 2;
					
					clouds.add(babyCloud);
				}
			}
		}
	}
	
	static int getWaterSum() {
		int sum = 0;
		for(int row = 1; row<=N; row++) {
			for(int col = 1; col<=N; col++) {
				if(waters[row][col] < 1) continue;
				sum += waters[row][col];
			}
		}
		return sum;
	}
}