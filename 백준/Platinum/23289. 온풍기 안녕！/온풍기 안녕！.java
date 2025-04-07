import java.io.*;
import java.util.*;

/*
 * 코드트리에서 풀었던 풀이를 백준 식에 맞게 가져옴
 * 좋은 풀이는 아니라고 생각
 */
/*
 * 어려웠던 점 꼬였던 원인은 바로
 * 방향의 불일치라고 생각함
 * 
 * 특히나 에어컨 방향과 벽의 방향이 다른 것이
 * 굉장히 헷갈렸음
 * 
 * 그리고 벽을 어떻게 해야 효율적으로 표현할지 고민. 여기선 비트마스킹 했는데
 * 더 좋은 풀이가 있을 것이라 생각
 * 
 * 그리고 에어컨 바람 갱신을 어떻게 해야 4방향 하드코딩하지 않고 효율적으로
 * 짤 수 있는지 궁금
 */
/*
 * 실수한 점: 
 * 1. 탐색 BFS 종료 조건이 바람 세기 0이어야 하는데 1로 처리
 * 2. 방향 불일치로 인한 많은 시간낭비
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	// 에어컨 방향 : 우 좌 상 하
	static int[] ADR = { 0, 0, -1, 1 }; // -1 빼고 시작
	static int[] ADC = { 1, -1, 0, 0 };

	// 벽 체크 방향 : 상 우 하 좌
	static int[] WDR = { -1, 0, 1, 0 };
	static int[] WDC = { 0, 1, 0, -1 };

	static int R, C, M, K; // N 벽자 크기, M 벽 개수, K 시원함 정도

	static int[][] inputBoard; // 입력 보드
	static int[][][] coolDiffs; // r, c, 4 방향
	static int[][] wallBits; // r, c, 값 = 벽 비트
	static List<int[]> companies;
	static List<int[]> aircons;
	static int[][] windBoard;

	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		inputBoard = new int[R + 1][C + 1];
		coolDiffs = new int[R + 1][C + 1][4];
		wallBits = new int[R + 1][C + 1];
		windBoard = new int[R + 1][C + 1]; // 바람 결과 보드판
		companies = new ArrayList<>(); // r, c
		aircons = new ArrayList<>(); // r,c, 방향

		
		
		for (int r = 1; r <= R; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 1; c <= C; c++) {
				inputBoard[r][c] = Integer.parseInt(st.nextToken());
				if (inputBoard[r][c] == 5) {
					companies.add(new int[] { r, c });
				} else if (inputBoard[r][c] > 0 && inputBoard[r][c] < 5) {
					int adir = inputBoard[r][c] - 1;
					aircons.add(new int[] { r, c, adir });
				}
			}
		}
		
		M = Integer.parseInt(br.readLine().trim());

		for (int widx = 0; widx < M; widx++) {
			st = new StringTokenizer(br.readLine().trim());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int wdir = Integer.parseInt(st.nextToken());
			if (wdir == 0) {
				wallBits[r][c] |= (1 << 0);
				if (r - 1 >= 1) {
					wallBits[r - 1][c] |= (1 << 2);
				}
			} else if (wdir == 1) {
				wallBits[r][c] |= (1 << 1);
				if (c + 1 <= C) {
					wallBits[r][c + 1] |= (1 << 3);
				}
			}
		}
	}

	static boolean outOfBorder(int r, int c) {
		return r < 1 || c < 1 || r > R || c > C;
	}

	static void print() {
		for(int r=1; r<=R; r++) {
			for(int c=1; c<=C; c++) {
				System.out.print(windBoard[r][c] +" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();
		
		int answer = simulate();
		bw.write(answer+"");
		bw.flush();
		bw.close();
	}

	static int simulate() {
		int answer = 101;
		for (int time = 0; time <= 100; time++) {
			// 4. allCheck
			if(allCheck()) {
				answer = time;
				break;
			}
			// 1. blowWind
			for (int aidx = 0; aidx < aircons.size(); aidx++) {
				int[] aircon = aircons.get(aidx);
				blowWind(aircon[0], aircon[1], aircon[2]);
				// print();
			}
			//print();
			// 2. mixCoolWind
			mixCoolWind();
			//print();
			// 3. minusCool
			minusCool();
			//print();
		}
		//System.out.println("final: ");
		//print();
		return answer;
	}
	
	static boolean allCheck() {
		for(int[] company : companies) {
			if(windBoard[company[0]][company[1]] < K) {
				return false;
			}
		}
		return true;
	}
	
	static void minusCool() {
		for(int r=1; r<=R; r++) {
			for(int c=1; c<=C; c++) {
				if(r==1 || c==1 || r==R || c==C) {
					if(windBoard[r][c] != 0) {
						windBoard[r][c] -= 1;
					}
				}
			}
		}
	}
	
	static void mixCoolWind() {
		coolDiffs = new int[R+1][C+1][4];
		// 바람 차이 미리 계산
		for(int r=1; r<=R; r++) {
			for(int c=1; c<=C; c++) {
				for(int dir=0; dir < 4; dir++) {
					int nr = r + WDR[dir];
					int nc = c + WDC[dir];
					
					if(outOfBorder(nr, nc)) continue;
					// 벽이 존재하면 패스
					if((wallBits[r][c] & (1<<dir)) == (1<<dir)) continue;
					
					int nowWind = windBoard[r][c];
					int nextWind = windBoard[nr][nc];
					if(nowWind > nextWind) {
						coolDiffs[r][c][dir] = nowWind - nextWind;
					}
				}
			}
		}
		
		// 동시에 바람 분배
		for(int r=1; r<=R; r++) {
			for(int c=1; c<=C; c++) {
				for(int dir=0; dir < 4; dir++) {
					int nr = r + WDR[dir];
					int nc = c + WDC[dir];
					
					if(outOfBorder(nr, nc)) continue;
					
					int windGap = coolDiffs[r][c][dir];
					if(windGap > 0) {
						windBoard[r][c] -= windGap/4;
						windBoard[nr][nc] += windGap/4;
					}
					
				}
			}
		}
	}
	
	static void blowLeft(int[] now, Deque<int[]> q, boolean[][] vis) {
		// 좌상, 좌, 좌하
		int r = now[0];
		int c = now[1];
		int[][] WPOS = { { -1, -1 }, { 0, -1 }, { 1, -1 } };
		for (int dir = 0; dir < 3; dir++) {
			int nr = r + WPOS[dir][0];
			int nc = c + WPOS[dir][1];

			if (outOfBorder(nr, nc))
				continue;
			if (vis[nr][nc])
				continue;

			// 벽 체크 방향 : 상 우 하 좌
			// 도착 벽 체크
			if ((wallBits[nr][nc] & (1 << 1)) == (1 << 1))
				continue;
			// 지금 벽 체크
			if (dir == 0 && ((wallBits[r][c] & (1 << 0)) == (1 << 0)))
				continue;
			else if (dir == 2 && ((wallBits[r][c] & (1 << 2)) == (1 << 2)))
				continue;
			
			vis[nr][nc] = true;
			int amount = now[2];
			windBoard[nr][nc] += amount;
			
			q.offer(new int[] {nr, nc, amount - 1});
		}
	}
	static void blowUp(int[] now, Deque<int[]> q, boolean[][] vis) {
		// 좌상, 좌, 좌하
		int r = now[0];
		int c = now[1];
		int[][] WPOS = { { -1, 1 }, { -1, 0 }, { -1, -1 } }; // 오른쪽 화살표 먼저
		for (int dir = 0; dir < 3; dir++) {
			int nr = r + WPOS[dir][0];
			int nc = c + WPOS[dir][1];

			if (outOfBorder(nr, nc))
				continue;
			if (vis[nr][nc])
				continue;

			// 벽 체크 방향 : 상 우 하 좌
			// 도착 벽 체크
			if ((wallBits[nr][nc] & (1 << 2)) == (1 << 2)) // 아래쪽 벽
				continue;
			// 지금 벽 체크
			if (dir == 0 && ((wallBits[r][c] & (1 << 1)) == (1 << 1)))
				continue;
			else if (dir == 2 && ((wallBits[r][c] & (1 << 3)) == (1 << 3)))
				continue;
			
			vis[nr][nc] = true;
			int amount = now[2];
			windBoard[nr][nc] += amount;
			
			q.offer(new int[] {nr, nc, amount - 1});
		}
	}
	static void blowDown(int[] now, Deque<int[]> q, boolean[][] vis) {
		// 좌상, 좌, 좌하
		int r = now[0];
		int c = now[1];
		int[][] WPOS = { { 1, -1 }, { 1, 0 }, { 1, 1 } };
		for (int dir = 0; dir < 3; dir++) {
			int nr = r + WPOS[dir][0];
			int nc = c + WPOS[dir][1];

			if (outOfBorder(nr, nc))
				continue;
			if (vis[nr][nc])
				continue;
			
			// 벽 체크 방향 : 상 우 하 좌
			// 도착 벽 체크
			if ((wallBits[nr][nc] & (1 << 0)) == (1 << 0))
				continue;

			// 지금 벽 체크
			if (dir == 0 && ((wallBits[r][c] & (1 << 3)) == (1 << 3)))
				continue;
			else if (dir == 2 && ((wallBits[r][c] & (1 << 1)) == (1 << 1)))
				continue;
			
			vis[nr][nc] = true;
			int amount = now[2];
			windBoard[nr][nc] += amount;
			
			q.offer(new int[] {nr, nc, amount - 1});
		}
	}
	static void blowRight(int[] now, Deque<int[]> q, boolean[][] vis) {
		int r = now[0];
		int c = now[1];
		int[][] WPOS = { { 1, 1 }, { 0, 1 }, { -1, 1 } };
		for (int dir = 0; dir < 3; dir++) {
			int nr = r + WPOS[dir][0];
			int nc = c + WPOS[dir][1];

			if (outOfBorder(nr, nc))
				continue;
			if (vis[nr][nc])
				continue;

			// 벽 체크 방향 : 상 우 하 좌
			// 도착 벽 체크
			if ((wallBits[nr][nc] & (1 << 3)) == (1 << 3))
				continue;

			// 지금 벽 체크
			if (dir == 0 && ((wallBits[r][c] & (1 << 2)) == (1 << 2)))
				continue;
			else if (dir == 2 && ((wallBits[r][c] & (1 << 0)) == (1 << 0)))
				continue;
			
			vis[nr][nc] = true;
			int amount = now[2];
			windBoard[nr][nc] += amount;
			
			q.offer(new int[] {nr, nc, amount - 1});
		}
	}
	
	static void blowWind(int ar, int ac, int adir) {
		int sr = ar + ADR[adir];
		int sc = ac + ADC[adir];
		if (outOfBorder(sr, sc))
			return;

		windBoard[sr][sc] += 5;

		Deque<int[]> q = new ArrayDeque<>();
		boolean[][] vis = new boolean[R + 1][C + 1];
		q.offer(new int[] { sr, sc, 4 }); // r, c, 바람 증가량

		while(!q.isEmpty()) {
			int[] now = q.poll();
			
			if(now[2] == 0) continue; // 바람의 세기가 0이면 더이상 전파 X
			
			// 에어컨 방향 : 우 좌 상 하
			if (adir == 0) { // left
				blowRight(now, q, vis);
			}else if(adir == 1) { // top
				blowLeft(now, q, vis);
			}else if(adir == 2) {  // right
				blowUp(now, q, vis);
			}else if(adir == 3) { // down
				blowDown(now, q, vis);
			}
		}
	}
}
