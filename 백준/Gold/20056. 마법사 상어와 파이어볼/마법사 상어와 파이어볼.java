import java.io.*;
import java.util.*;

/*
 * 배운것:
 * 순환 좌표 갱신 + 1-based 인덱싱
 * (((r - 1) - s) % N + N) % N + 1; - 1-based
 * r-1 해서 0-based로 만들어서 처리한 다음에(계산이 편함)
 * 결과값에 +1해서 1-based로 만들어야 한다(1-based로 통일)
 * 실수:
 * 1. 상태 갱신을 제대로 했는지 (좌표, 보드판) 체크하자
 * 2. 보드판, 좌표를 혼용할 경우 둘 다 적절하게 초기화 했는지, 초기 데이터를 넣어줬는지
 * 2-1.갱신을 올바르게 하고 있는지 체크해야 한다.
 * 3. 순환한다면 N보다 큰 범위를 줬을때 어떻게 될지 생각하자.
 * 4. 1-based인지, 0-based인지 분명히 인지하고 되도록 통일시키자
 * 
 * 파이어볼 객체:
 * - r, c 좌표
 * - isRemoved
 * - m 질량
 * - s 속력 (칸만큼 이동)
 * - d 방향
 * - idx 인덱스 (같은 칸 체크 용)
 *
 * K번 명령
 * 1. 이동 -> 1-N N-1 순환 가능 고려
 * 1-0. 파이어볼이 isRemoved면 제외
 * 1-1. 객체 좌표만 변경하면 된다.
 * 1-1-0-0. Set<index>[r][c]에서 삭제
 * 1-1-0-1. 증가면 (r/c + s) % N
 * 1-1-0-2. 감소면 (r/c - s + N) % N
 * 1-1-1. 방향이 대각선(1,3,5,7)이면 r+, c+ 둘 다
 * 1-1-2. 방향이 가로세로면 r또는 c 하나만
 * 1-1-3. Set<index>[nr][nc]에 넣기
 * 2. 같은 칸의 파이어볼 합치기
 * 2-1. Set<index>[N][N] 이 size() > 1
 * 2-1-0. 파이어볼 객체들의 방향 갱신, 파볼 개수, 파이어볼 객체 질량 합, 방향 체크는 2로 나머지 구해서 나머지를 &,| 처리
 * 2-1-1. 해당 칸의 첫번째 인덱스 빼고 다른 파이어볼 객체들은 제거 처리, flag 처리
 * 2-1-1-1. 다른 파이어볼 객체 idx List에 저장해놨다가 나중에 Set에서 삭제
 * 2-1-2. 해당 첫번재 인덱스의 파이어볼의 질량 합, 속력 합 갱신
 * 2-2. 4개의 파이어볼 분할
 * 2-2-1. 파이어볼 객체 4개 생성
 * 2-2-1-2. 방향은 2-1-0에서 구한 것을 이용해서 모두 홀수면 둘 다 1 모두 짝수면 둘 다 0
 * 2-2-1-2-1. 뭐가 섞여 있으면 1또는 0이 섞여 있음
 * 2-2-2. 질량 합 / 5 처리해서 만들어진 객체에 갱신
 * 2-2-3. 속력 합/ 파볼 개수 처리해서 만들어진 객체에 갱신
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	// 방향 0~7
	static int DX[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	static int DY[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

	static class Ball {
		int r, c; // 좌표
		int m; // 질량
		int s; // 속력
		int d; // 방향
		int idx; // 인덱스
		boolean isRemoved; // 제거 유무

		Ball(int r, int c, int m, int s, int d, int idx) {
			this.r = r;
			this.c = c;
			this.m = m;
			this.s = s;
			this.d = d;
			this.idx = idx;
			this.isRemoved = false;
		}
	}

	static int N, M, K; // N은 보드판 크기, M은 초기 파이어볼 개수, K는 명령 횟수
	static Set<Integer>[][] board; // 1-based
	static List<Ball> balls;

	static int addPosRow(int r, int c, int s) {
		// return (r + s) % N; // 행으로 증가할때 좌표 리턴
		return (((r - 1) + s) % N + N) % N + 1; // 1-based
	}

	static int addPosCol(int r, int c, int s) {
		// return (c + s) % N; // 열로 증가할때 좌표 리턴
		return (((c - 1) + s) % N + N) % N + 1;  // 1-based
	}

	static int minusPosRow(int r, int c, int s) {
		// return (r - s + N) % N; // 행으로 감소할때 좌표 리턴
		// 주의!!! 이러면 안된다. s가 N보다 커지면? ex) -1 % 4 = -1 결과가 나옴
		// java에서는 %는 앞 숫자의 부호를 따른다 ex) -1 % 4 -> 몫: 0 나머지: -1
		// 순환 케이스는 범위 보다 크게 증가 감소할 수 있는지, 체크하자
		// 만약 그렇다면 어떻게 해야할까
		// 바로 한 번 더 +N %N 처리하는 것이다
		// 순환 증가 감소에서 안전하게 처리할 수 있다.
		return (((r - 1) - s) % N + N) % N + 1; 

	}

	static int minusPosCol(int r, int c, int s) {
		// return (c - s + N) % N; // 열로 감소할때 좌표 리턴
		return (((c - 1) - s) % N + N) % N + 1; 
	}

	static int[] getNextPos(int r, int c, int dir, int s) {
		int nr = r;
		int nc = c;
		if (dir == 0) {
			nr = minusPosRow(r, c, s);
			nc = c;
		} else if (dir == 1) {
			nr = minusPosRow(r, c, s);
			nc = addPosCol(r, c, s);
		} else if (dir == 2) {
			nr = r;
			nc = addPosCol(r, c, s);
		} else if (dir == 3) {
			nr = addPosRow(r, c, s);
			nc = addPosCol(r, c, s);
		} else if (dir == 4) {
			nr = addPosRow(r, c, s);
			nc = c;
		} else if (dir == 5) {
			nr = addPosRow(r, c, s);
			nc = minusPosCol(r, c, s);
		} else if (dir == 6) {
			nr = r;
			nc = minusPosCol(r, c, s);
		} else if (dir == 7) {
			nr = minusPosRow(r, c, s);
			nc = minusPosCol(r, c, s);
		}
		return new int[] { nr, nc };
	}

	// * 1. 이동 -> 1-N N-1 순환 가능 고려
	// * 1-0. 파이어볼이 isRemoved면 제외
	// * 1-1. 객체 좌표만 변경하면 된다.
	// * 1-1-0-0. Set<index>[r][c]에서 삭제
	// * 1-1-0-1. 증가면 (r/c + s) % N
	// * 1-1-0-2. 감소면 (r/c - s + N) % N
	// * 1-1-1. 방향이 대각선(1,3,5,7)이면 r+, c+ 둘 다
	// * 1-1-2. 방향이 가로세로면 r또는 c 하나만
	// * 1-1-3. Set<index>[nr][nc]에 넣기
	static void move() {
		for (int bidx = 0; bidx < balls.size(); bidx++) {
			Ball ball = balls.get(bidx);
			if (ball.isRemoved)
				continue;

			int r = ball.r;
			int c = ball.c;
			int dir = ball.d;
			int s = ball.s;
			board[r][c].remove(bidx);

			int[] nextPos = getNextPos(r, c, dir, s);

			int nr = nextPos[0];
			int nc = nextPos[1];
			ball.r = nr;
			ball.c = nc;
			board[nr][nc].add(bidx);
		}
	}

	// * 2. 같은 칸의 파이어볼 합치기
	// * 2-1. Set<index>[N][N] 이 size() > 1
	// * 2-1-0. 파이어볼 객체들의 방향 갱신, 파볼 개수, 파이어볼 객체 질량 합, 방향 체크는 2로 나머지 구해서 나머지를 &,| 처리
	// * 2-1-1. 해당 칸의 첫번째 인덱스 빼고 다른 파이어볼 객체들은 제거 처리, flag 처리
	// * 2-1-1-1. 다른 파이어볼 객체 idx List에 저장해놨다가 나중에 Set에서 삭제
	// * 2-2. 4개의 파이어볼 분할
	// * 2-2-1. 파이어볼 객체 4개 생성
	// * 2-2-1-2. 방향은 2-1-0에서 구한 것을 이용해서 모두 홀수면 둘 다 1 모두 짝수면 둘 다 0
	// * 2-2-1-2-1. 뭐가 섞여 있으면 1또는 0이 섞여 있음...이라 했었는데 이거 실수인가 싶어서
	// * 그냥 짝 홀 체크해주기로 함 직접
	// * 2-2-2. 질량 합 / 5 처리해서 만들어진 객체에 갱신
	// * 2-2-3. 속력 합/ 파볼 개수 처리해서 만들어진 객체에 갱신
	static void mergeBalls() {
		for (int r = 1; r <= N; r++) {
			for (int c = 1; c <= N; c++) {
				if (board[r][c].size() <= 1)
					continue;

				// 합치기
				int ballCnt = 0;
				int mSum = 0;
				int sSum = 0;
				boolean allEven = true;
				boolean allOdd = true;
				List<Integer> bIdxList = new ArrayList<>();
				for (int bidx : board[r][c]) {
					ballCnt++;
					mSum += balls.get(bidx).m;
					sSum += balls.get(bidx).s;
					if (balls.get(bidx).d % 2 == 0) {
						allOdd = false;
					} else {
						allEven = false;
					}
					balls.get(bidx).isRemoved = true;
					bIdxList.add(bidx);
				}
				// ball 삭제
				for (int bidx : bIdxList) {
					board[r][c].remove(bidx);
				}

				// 분할
				int nxtM = mSum / 5;
				// 만약 질량이 0이 되었다면 소멸된다
				if (nxtM == 0) {
					continue;
				}
				int nxtS = sSum / ballCnt;

				int[] dirs;
				if (allEven || allOdd) {
					dirs = new int[] { 0, 2, 4, 6 };
				} else {
					dirs = new int[] { 1, 3, 5, 7 };
				}

				for (int d : dirs) {
					Ball newBall = new Ball(r, c, nxtM, nxtS, d, balls.size());
					balls.add(newBall);
					board[r][c].add(newBall.idx);
				}
			}
		}
	}

	/*
	 * 남아있는 파볼 질량의 합을 구한다
	 */
	static int getResult() {
		int answer = 0;
		for (int bidx = 0; bidx < balls.size(); bidx++) {
			Ball ball = balls.get(bidx);
			if (ball.isRemoved)
				continue;
			answer += ball.m;
		}
		return answer;
	}

	static int doSimulation() {
		for (int k = 0; k < K; k++) {
			// 1. 이동
			move();
			// 2. 같은 칸 파이어볼 합치기 + 분할
			mergeBalls();
		}
		return getResult();
	}

	@SuppressWarnings("unchecked")
	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		// 초기화
		board = new Set[N + 1][N + 1]; // 1-based
		for (int r = 0; r <= N; r++) {
			for (int c = 0; c <= N; c++) {
				board[r][c] = new HashSet<>();
			}
		}
		balls = new ArrayList<>();

		// 파볼 초기화
		for (int idx = 0; idx < M; idx++) {
			st = new StringTokenizer(br.readLine().trim());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());

			balls.add(new Ball(r, c, m, s, d, idx));
			board[r][c].add(idx);
		}
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();

		int answer = doSimulation();

		bw.write(answer + "");
		bw.flush();
		bw.close();
	}
}
