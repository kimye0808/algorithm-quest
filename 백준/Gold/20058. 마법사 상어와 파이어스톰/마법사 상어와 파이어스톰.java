import java.io.*;
import java.util.*;

/*
 * 문제 풀이 (설계 + 구현) : 1시간 10분
 * 디버깅 및 재구현 (rotateAtLevel) : 50분
 * 총 2시간
 */
/*
 * 실수한 점:
 * 1. 문제를 제대로 해석하지 못함
 * -> 얼음이 녹는 시점: 나는 아무 생각 없이 좌표 순회하면서 주변 3개 미만이면 감소
 * -> 그러면 안됨... 그렇게 되면 앞의 결과가 뒤의 결과에 영황을 준다
 * -> 문제에서 의미하는 바는 인접 얼음 3칸 미만인 얼음이면 동시에 -1 감소한다는 것!
 * => 이벤트 발생 타이밍을 제대로 이해할 것
 * 
 * 2. 문제를 제대로 안읽음
 * -> 문제는 분명히 90도 회전
 * -> 근데 나는 한 칸씩 시계방향으로 이동하는 회전을 구현함
 * => 회전 요구사항을 제대로 읽읍시다
 */
/*
 * 알게 된 점:
 * rotate90 2차원 배열
 * 막 좌표축 변환하고 그러면 어지러운데
 * 그냥 그대로 배열을 
 * 	static void rotateAtLevel(int sr, int sc, int level) {
		int Adder = (int)Math.pow(2, level)-1;
		int Max = (int)Math.pow(2,  level);
		
		for(int row = sr; row <= row + Adder; row++) {
			for(int col = sc; col <= col + Adder; col++) {
				// 0-based인 0,0 좌표축으로 변환
				int r = row - sr;
				int c = col - sc;
				// 좌표축 변환해서 회전식 사용하고, 다시 sr sc 더해서 좌표축 복구
				board[c + sr][Max - r - 1+ sc] = board[r + sr][c + sc];
			}
		}
		
	}
	
	이런 식으로 제자리에서 이동시킬려고 하면 덮어쓴 걸 또 덮어쓰는
	사실상 회전이 안됨 잘못된 방식
 * 		int[][] tempBoard = new int[Max][Max];

		for (int row = 0; row < Max; row++) {
			for (int col = 0; col < Max; col++) {
				tempBoard[col][Max - row - 1] = board[sr + row][sc + col];
			}
		}

		for (int row = 0; row < Max; row++) {
			for (int col = 0; col < Max; col++) {
				board[sr + row][sc + col] = tempBoard[row][col];
			}
		}
 *	이런 식으로 해당 부분만 다른 임시 배열에 복사해놨다가
 *	다시 해당 부분만 원본 배열에 복붙하는 방식으로 직관적이며 손쉽게 구현할 수 있음
 *	옳은 풀이.
 *	그리고 위의 풀이는 0-based로 만들어서 푸는데
 *	90도 회전 같은 것은 0-based가 수학적으로 더 직관적이다
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static int[][] board;
	static int N, Q; // 2^N*2^N 보드판, Q는 파스 횟수
	static int LENGTH; // N이 보드판 길이가 아니라 LENGTH가 보드판 길이임을 주의
	static int[] levels;

	/*
	 * 초기화 + 입력
	 */
	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		LENGTH = (int) Math.pow(2, N);
		board = new int[LENGTH + 1][LENGTH + 1]; // 1-based
		levels = new int[Q + 1]; // 1-based

		for (int r = 1; r <= LENGTH; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 1; c <= LENGTH; c++) {
				board[r][c] = Integer.parseInt(st.nextToken());
			}
		}

		st = new StringTokenizer(br.readLine().trim());
		for (int q = 1; q <= Q; q++) {
			levels[q] = Integer.parseInt(st.nextToken());
		}
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();

		int[] result = doSimulation();

		bw.write(result[0] + "\n");
		bw.write(result[1] + "");
		bw.flush();
		bw.close();
	}

	/*
	 * 해당 레벨에 맞게 해당 좌표를 시작점으로 해서 90도 시계방향 회전
	 */
	static void rotateAtLevel(int sr, int sc, int level) {
		int Max = (int) Math.pow(2, level);

		int[][] tempBoard = new int[Max][Max];

		// 시작점 기준으로 원본 배열을 임시 배열에 복사 
		for (int row = 0; row < Max; row++) {
			for (int col = 0; col < Max; col++) {
				tempBoard[col][Max - row - 1] = board[sr + row][sc + col];
			}
		}

		// 원본 배열에 임시 배열 복사
		for (int row = 0; row < Max; row++) {
			for (int col = 0; col < Max; col++) {
				board[sr + row][sc + col] = tempBoard[row][col];
			}
		}
	}

	static void rotate(int level) {
		if (level == 0)
			return;
		// 시작 좌표
		int sr = 1;
		int sc = 1;
		int Adder = (int) Math.pow(2, level); // 시작 좌표들 2^level 만큼 증가
		// rotate 시작점 순회
		for (int r = sr; r <= LENGTH; r += Adder) {
			for (int c = sc; c <= LENGTH; c += Adder) {
				rotateAtLevel(r, c, level);
			}
		}
	}

	/*
	 * 인접 3개 미만의 얼음을 모아놨다가 녹입니다
	 */
	static void meltIce() {
		int[][] ADJ = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		List<int[]> targetIce = new ArrayList<>();

		for (int r = 1; r <= LENGTH; r++) {
			for (int c = 1; c <= LENGTH; c++) {
				// 이미 얼음이 아니면 다음
				if (board[r][c] == 0)
					continue;
				// 인접 얼음 개수 체크
				int iceCnt = 0;
				for (int dir = 0; dir < 4; dir++) {
					int nr = r + ADJ[dir][0];
					int nc = c + ADJ[dir][1];

					if (outOfBorder(nr, nc))
						continue;
					if (board[nr][nc] == 0)
						continue;

					iceCnt++;
				}
				if (iceCnt < 3) {
					targetIce.add(new int[] { r, c });
				}
			}
		}

		for (int[] icePos : targetIce) {
			board[icePos[0]][icePos[1]] -= 1;
		}
	}

	/*
	 * BFS로 덩어리의 크기를 센다
	 */
	static int countPiece(boolean[][] vis, int sr, int sc) {
		Deque<int[]> q = new ArrayDeque<>();
		q.offer(new int[] { sr, sc });
		vis[sr][sc] = true;

		int loafSize = 0;

		int DR[] = { -1, 0, 1, 0 };
		int DC[] = { 0, 1, 0, -1 };

		while (!q.isEmpty()) {
			int[] now = q.poll();
			int r = now[0];
			int c = now[1];

			loafSize += 1;

			for (int dir = 0; dir < 4; dir++) {
				int nr = r + DR[dir];
				int nc = c + DC[dir];

				if (outOfBorder(nr, nc))
					continue;
				if (board[nr][nc] == 0)
					continue;
				if (vis[nr][nc])
					continue;

				vis[nr][nc] = true;
				q.offer(new int[] { nr, nc });
			}
		}

		return loafSize;
	}

	/*
	 * 1. 얼음 합을 구한다
	 * 2. BFS로 가장 큰 덩어리 얼음의 크기를 구한다
	 */
	static int[] getResult() {
		int iceSum = 0;
		for (int row = 1; row <= LENGTH; row++) {
			for (int col = 1; col <= LENGTH; col++) {
				if (board[row][col] >= 1) {
					iceSum += board[row][col];
				}
			}
		}

		int maxLoafSize = 0;
		boolean[][] vis = new boolean[LENGTH + 1][LENGTH + 1];
		for (int row = 1; row <= LENGTH; row++) {
			for (int col = 1; col <= LENGTH; col++) {
				if (!vis[row][col] && board[row][col] != 0) {
					maxLoafSize = Math.max(maxLoafSize, countPiece(vis, row, col));
				}
			}
		}

		return new int[] { iceSum, maxLoafSize };
	}

	static int[] doSimulation() {
		for (int q = 1; q <= Q; q++) {
			// rotate
			rotate(levels[q]);
			// meltIce
			meltIce();
		}
		// getResult
		return getResult();
	}

	static boolean outOfBorder(int r, int c) {
		return r < 1 || c < 1 || r > LENGTH || c > LENGTH;
	}

	// 디버깅용
	static void print() {
		for (int r = 1; r <= LENGTH; r++) {
			for (int c = 1; c <= LENGTH; c++) {
				System.out.print(board[r][c] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}