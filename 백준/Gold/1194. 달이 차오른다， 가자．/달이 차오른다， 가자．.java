import java.io.*;
import java.util.*;

/*
 * 갈 수 있는 모든 경우를 가보는 수 밖에 없다
 * 이때, 방문한 곳은 또 갈 수 있긴 하다
 * 대문자 ABCDEF 를 만나면 
 * 대응되는 열쇠 abcdef 가 있어야 한다
 * 0 시작, 1 도착
 * # 벽
 * 모든 경우를 가보고
 * 소문자를 만나면 열쇠를 갱신한다
 * 대문자를 만나면 열쇠가 있는지 확인하고 있으면 이동 가능하고 아니면 리턴한다
 * 
 * 백트래킹
 * 
 * 인데 시간초과 나서
 * BFS에
 * keyStatus 까지 추가하고
 * 방문 처리에 
 * [r][c][key비트] 까지 해줌
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static int[] DR = { -1, 0, 1, 0 };
	static int[] DC = { 0, 1, 0, -1 };

	static int N, M;
	static char[][] miro;
	static int answer = Integer.MAX_VALUE;

	static boolean outOfBorder(int r, int c) {
		return r < 0 || c < 0 || r >= N || c >= M;
	}

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		/*
		 * 초기화 + 입력
		 */
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		miro = new char[N][M];
		boolean[][][] vis = new boolean[N][M][1 << 6];
		int sr = 0, sc = 0;

		for (int r = 0; r < N; r++) {
			miro[r] = br.readLine().trim().toCharArray();
			for (int c = 0; c < M; c++) {
				char giho = miro[r][c];
				if (giho == '0') {
					sr = r;
					sc = c;
				}
			}
		}

		Deque<int[]> q = new ArrayDeque<>();
		q.offer(new int[] { sr, sc, 0, 0 });

		vis[sr][sc][0] = true;

		while (!q.isEmpty()) {
			int[] now = q.poll();
			int r = now[0];
			int c = now[1];
			int keyStatus = now[2];
			int count = now[3];

			if (miro[r][c] == '1') {
				answer = Math.min(answer, count);
			}

			for (int dir = 0; dir < 4; dir++) {
				int nr = r + DR[dir];
				int nc = c + DC[dir];

				if (outOfBorder(nr, nc))
					continue;
				if (miro[nr][nc] == '#')
					continue;

				// 열쇠 처리
				int keyNum = miro[nr][nc] - 'a';
				if (keyNum >= 0 && keyNum <= 5) {
					int nextKeyStatus = keyStatus | (1 << keyNum);
					if (vis[nr][nc][nextKeyStatus])
						continue;
					vis[nr][nc][nextKeyStatus] = true;

					q.offer(new int[] { nr, nc, nextKeyStatus, count + 1 });

				} else {
					// 문 처리
					int doorNum = miro[nr][nc] - 'A';
					if (doorNum >= 0 && doorNum <= 5) {
						if ((keyStatus & (1 << doorNum)) == (1 << doorNum)) {
							if (vis[nr][nc][keyStatus])
								continue;
							vis[nr][nc][keyStatus] = true;

							q.offer(new int[] { nr, nc, keyStatus, count + 1 });
						}
					} else {
						if (vis[nr][nc][keyStatus])
							continue;
						vis[nr][nc][keyStatus] = true;

						q.offer(new int[] { nr, nc, keyStatus, count + 1 });
					}
				}
			}
		}

		if (answer == Integer.MAX_VALUE)
			answer = -1;
		bw.write(answer + "");
		bw.flush();
		bw.close();
	}
}
