import java.io.*;
import java.util.*;

/*
 * [실수한점]
 * 1. 방향 인덱스를 어떻게 할 지 정해놨었는데 다른 방식으로 방향 인덱스를 설정함
 * 1-1. 그래서 토네이도 이동 방향 방식이랑 모래 이동방향 방식이 불일치함
 * -> 방향 인덱스 설정 통일할때 주의하자. 안쓰는 방향 인덱스 방식은 지우자
 * 
 * 2. 1-based 인데 경계값 벗어날때 outOfBorder 경계 처리 잘못함
 * -> 1-based면 0보다 작거나 N보다 크거나이다
 * 
 * 3. 토네이도 이동에서 언제 거리를 증가시킬지, 방향을 전환할지 갱신을 잘못함
 * -> 거리 증가, 방향 전환 갱신 타이밍에 유의하자
 * 
 * 4. 토네이도 이동할때 좌표를 누적 갱신해줘야 하는데 그때 그때 계산함
 * -> 좌표 누적 갱신 vs 매번 새롭게 갱신, 뭐가 더 적절한지 생각하고 짜자
 * 
 * 5. 문제 설명 잘못 이해, a 에서 보드판 안에 있는 모래들만 제외하는줄 앎
 * -> 문제를 완벽하게 이해하고 설계한다
 * 
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	// 시계방향으로 0-7
	static int DR[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int DC[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	// 0, 2, 4, 6 이 토네이도 이동방향
	
	static int N;
	static int[][] board; // 1-based

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		init();
		
		int answer = doSimulation();
		
		bw.write(answer+"");
		bw.flush();bw.close();
	}
	
	/*
	 * 초기화 + 입력
	 */
	static void init() throws Exception {
		N = Integer.parseInt(br.readLine().trim());

		board = new int[N + 1][N + 1];

		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 1; c <= N; c++) {
				board[r][c] = Integer.parseInt(st.nextToken());
			}
		}
	}
	
	static int doSimulation() {
		return moveTornado();
	}
	
	static boolean outOfBorder(int r, int c) {
		return r<1||c<1||r>N||c>N;
	}
	
	static int getNextTorDir(int dir) {
		return (dir-2 + 8) %8;
	}
	
	static void distribute(int totalSand, int dir, int dist, 
			double percent, int r, int c, int[] outInSand) 
	{
		long nextSand = (long)((double)totalSand * percent);
		int nr = r + dist * DR[dir];
		int nc = c + dist * DC[dir];
		if(outOfBorder(nr, nc))	outInSand[0] += nextSand;
		else {
			board[nr][nc] += nextSand;
			outInSand[1] += nextSand;
		}
	}
	
	static int getAptDir(int dir, int angle) {
		return (dir+angle+8)%8;
	}
	
	static int blowSand(int r, int c, int dir) {
		int sand = board[r][c];
		int[] outInSand = {0,0}; // 0:나간 모래, 1:보드에 있는 모래 
		// 5%
		distribute(sand, dir, 2, 5.0/100.0, r, c, outInSand);
		
		// 10%
		distribute(sand, getAptDir(dir, 1), 1, 10.0/100.0, r, c, outInSand);
		distribute(sand, getAptDir(dir, -1), 1, 10.0/100.0, r, c, outInSand);
		// 7%
		distribute(sand, getAptDir(dir, 2), 1, 7.0/100.0, r, c, outInSand);
		distribute(sand, getAptDir(dir, -2), 1, 7.0/100.0, r, c, outInSand);
		// 2%
		distribute(sand, getAptDir(dir, 2), 2, 2.0/100.0, r, c, outInSand);
		distribute(sand, getAptDir(dir, -2), 2, 2.0/100.0, r, c, outInSand);
		// 1%
		distribute(sand, getAptDir(dir, 3), 1, 1.0/100.0, r, c, outInSand);
		distribute(sand, getAptDir(dir, -3), 1, 1.0/100.0, r, c, outInSand);
		// a
		int nextSand = sand - outInSand[1] - outInSand[0];
		int nr = r + DR[dir];
		int nc = c + DC[dir];
		if(outOfBorder(nr, nc)) outInSand[0] += nextSand;
		else {
			board[nr][nc] += nextSand;
			outInSand[1] += (int)nextSand;
		}
		
		return outInSand[0];
	}
	
	
	/*
	 * 토네이도 이동, 
	 * moveTornado
	 * 	blowSand
	 */
	static int moveTornado() {
		int r = N/2+1, c= N/2+1; // 시작 좌표
		int dir = 6;
		int distLimit = 1;
		int sandSum = 0;
		
		int nr = r;
		int nc = c;

		while(true) { // (1,1) 까지 계속 반복한다
			for(int movePattern = 0; movePattern < 2; movePattern++) {
				for(int dist=1; dist<=distLimit; dist++) {
					nr += DR[dir];
					nc += DC[dir];
					
					// blowSand
					sandSum += blowSand(nr, nc, dir);
					
					// (1,1)까지 이동한 뒤 소멸 이니까 아마 
					// (1,1)도 모래 날리고나서 종료
					if(nr == 1 && nc == 1) {
						return sandSum;
					}
				}
				dir = getNextTorDir(dir);
			}
			distLimit++;
		}
	}
}