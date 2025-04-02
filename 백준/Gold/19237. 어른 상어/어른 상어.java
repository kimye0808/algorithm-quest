import java.io.*;
import java.util.*;

/*
 * 상어는 4마리 존재
 * 상어당 방향 우선순위가 존재
 * 현재 방향에서 우선순위에 따라 완전 다른 방향으로도 이동 가능
 * 상어가 한 턴 이동하면, 좌표 하나에 여러 상어들이 존재 가능
 *
 * 모든 상어가 자신의 위치에 자신의 냄새를 뿌린다.
 * 그 후 1초마다 모든 상어가 동시에 상하좌우로 인접한 칸 중 하나로 이동하고,
 * 자신의 냄새를 그 칸에 뿌린다.
 * 냄새는 상어가 k번 이동하고 나면 사라진다. -> 특정 좌표에 냄새 : 누구 것인지, 남은 시간
 * 상어가 죽어도 냄새는 남아있다
 *
 * 냄새는 특정 좌표로 검색할 수 있어야 한다
 * 누구 것인지 알 수 있어야 한다
 * 남은 시간을 알 수 있어야 한다
 *
 * 보드판은 최대 400이므로
 * 매 번 갱신해줘도 될 것 같다
 * 턴은 어차피 최대 1000이니까
 *
 * sWhoBoard[][]
 * sTimeBoard[][]
 * 매 번 갱신한다
 *
 * 각 상어가 이동 방향을 결정할 때는, 먼저 인접한 칸 중 아무 냄새가 없는 칸의 방향으로 잡는다.
 * 그런 칸이 없으면 자신의 냄새가 있는 칸의 방향으로 잡는다.-> 냄새가 누구것인지 구별 가능해야 함
 * 이때 가능한 칸이 여러 개일 수 있는데, 그 경우에는 특정한 우선순위를 따른다.
 *
 * 한턴에
 *  1. 상어 냄새 남김
 * 2. 상어 동시 이동(한 좌표에 여러 상어 중첩 가능)
 * 3. 중복 좌표시 번호가 더 작은 상어는 남고 나머지 상어는 제거
 *
 * 상어 : 좌표, 현재방향, 죽었나살았나
 * 좌표 중복 카운팅 : 좌표 카운팅용 보드판 만들어서 해결
 * 냄새 : 보드판에 새겨도 충분하다 좌표로 접근 가능해야 하고, 보드판의 크기가 작고 턴 수도 최대 1000이므로 가능하다
 *
 * 아니다 상어 위치를 좌표도 해야하고
 * 중복 제거 때문에
 * 보드판으로 카운팅도 하는게 편할 것 같다 <- 이부분 어떻게 처리하는지 다른 사람 것도 봐야 할듯
 *
 * 아 문제 잘못 읽었다
 * 상어 마릿수가 최대 M마리
 * 4마리 고정이아님 하
 * 
 * + 또 잘못한게
 * 상어가 이동한 후에 -> 냄새 감소 갱신
 * 상어 이동 -> 중복 제거 -> (현재 상어 있는 칸 제외) 냄새 감소 갱신
 * 해야 함
 * 안그러면 현재 상어 있는 칸도 같이 감소갱신해버림
 * 
 *  그냥 간단하게
 *  상어 이동 -> 중복 제거 -> 냄새 감소 갱신 -> 현재 상어 냄새 갱신
 *  이렇게 하자
 *  
 *  빡세네
 *  논리 구조를 완벽에 가깝게 검증하고 구현에 들어가자
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static class Shark {
		int x, y, dir;
		boolean isDead;

		Shark(int x, int y) {
			this.x = x;
			this.y = y;
			isDead = false;
		}
	}

	// 위, 아래, 왼쪽, 오른쪽
	static int DX[] = { 0, -1, 1, 0, 0 }; // 1번부터 시작하는거 주의
	static int DY[] = { 0, 0, 0, -1, 1 };

	static int N, M, k;
	static int[][][] sharkDirs; // i번 상어, j번 방향일때, k번 우선순위인 방향
	static int[][] sWhoBoard;
	static int[][] sTimeBoard;
	static int[][] sCntBoard;
	static Shark[] sharks;
	
	/*
	 * 상어 냄새 갱신
	 */
	static void renewSmell() {
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				if (sTimeBoard[r][c] > 0) {
					sTimeBoard[r][c] -= 1;
					if (sTimeBoard[r][c] == 0) {
						sWhoBoard[r][c] = 0;
					}
				}
			}
		}
		// 현재 상어 갱신
		for(int snum=1; snum <= M; snum++) {
			if(!sharks[snum].isDead) {
				int r = sharks[snum].x;
				int c = sharks[snum].y;
				sWhoBoard[r][c] = snum;
				sTimeBoard[r][c] = k;
			}
		}
	}
	
	/*
	 * 중복 상어 제거
	 */
	static void removeDupSharks() {
		for (int r = 0; r < N; r++) {
			for (int c = 0; c < N; c++) {
				//중복시
				if (sCntBoard[r][c] > 1) {
					boolean dupFound = false;

					for (int snum = 1; snum <= M; snum++) {
						if (sharks[snum].isDead)
							continue; // 죽은 상어는 제외

						int sx = sharks[snum].x;
						int sy = sharks[snum].y;

						if (r == sx && c == sy) {
							if (dupFound) { // 처음 말고 다른 애들은 제거
								sharks[snum].isDead = true;
							}
							dupFound = true;
						}
					}
					sCntBoard[r][c] = 1;
				}
			}	
		}
	}

	/*
	 * 상어를 시간에 따라 이동
	 */
	static int moveSharks() {
		int time = 1;
		for (time = 1; time <= 1000; time++) {
			// 1. 상어 동시 이동
			for (int snum = 1; snum <= M; snum++) {
				if (sharks[snum].isDead)
					continue;

				int nowDir = sharks[snum].dir;

				int[] dirs = sharkDirs[snum][nowDir];
				int x = sharks[snum].x;
				int y = sharks[snum].y;

				// 먼저 인접한 칸 중 아무 냄새가 없는 칸의 방향(우선순위는 기본)
				boolean found = false;
				int emptyDir = 0;
				int mySmellDir = 0;
				for (int dir : dirs) {
					int nx = x + DX[dir];
					int ny = y + DY[dir];

					if (outOfBorder(nx, ny))
						continue;

					if (sTimeBoard[nx][ny] == 0) {
						found = true;
						emptyDir = dir;
						break;
					} else if (sWhoBoard[nx][ny] == snum && mySmellDir == 0) {
//						System.out.println(dir);
						mySmellDir = dir;
					}
				}

				int nx = x;
				int ny = y;
				int nxtDir = nowDir;
				if (!found) {
					// 찾지 못했다면 자신의 냄새 방향 칸으로 간다. 이건 무조건 있을 수 밖에 없음 이동할떄 생각해보면 결국 지나온길
					if (mySmellDir == 0)
						System.out.println("뭔가 이상함");
					nx = x + DX[mySmellDir];
					ny = y + DY[mySmellDir];
					nxtDir = mySmellDir;
				} else { // 빈칸이라면
//					System.out.println("snum: "+snum+ ", dir: "+emptyDir);
					nx = x + DX[emptyDir];
					ny = y + DY[emptyDir];
					nxtDir = emptyDir;
				}
				// 샤크 좌표 갱신
				sharks[snum].x = nx;
				sharks[snum].y = ny;
				// 샤크 방향 갱신
				sharks[snum].dir = nxtDir;

				sCntBoard[nx][ny]++;
				sCntBoard[x][y]--;	
			}

			// 2. 상어 중복 제거
			removeDupSharks();
			// 3. 상어 냄새 갱신
			renewSmell();
			// 4. 1번 상어만 남았는지 체크
			boolean finished = true;
			for (int snum = 2; snum <= M; snum++) {
				if (!sharks[snum].isDead) {
					finished = false;
					break;
				}
			}
			if (finished) {
				break;
			}
		}
		
		return time;
	}

	/*
	 * 입력 + 초기화
	 */
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());

		// 초기화
		sharkDirs = new int[M + 1][5][4]; // i번 상어, j번 방향일때, k번 우선순위인 방향
		sWhoBoard = new int[N][N];
		sTimeBoard = new int[N][N];
		sCntBoard = new int[N][N];
		sharks = new Shark[M + 1]; // 1번 인덱스부터 시작

		// 상어 좌표 입력
		for (int r = 0; r < N; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 0; c < N; c++) {
				int snum = Integer.parseInt(st.nextToken());
				if (snum != 0) {
					sharks[snum] = new Shark(r, c);
					sCntBoard[r][c]++;
				}
			}
		}

		// 상어 방향 입력
		st = new StringTokenizer(br.readLine().trim());
		for (int snum = 1; snum <= M; snum++) {
			sharks[snum].dir = Integer.parseInt(st.nextToken());
		}

		// 방항 우선순위 입력
		for (int snum = 1; snum <= M; snum++) {
			for (int dir = 1; dir <= 4; dir++) {
				st = new StringTokenizer(br.readLine().trim());
				for (int pidx = 0; pidx < 4; pidx++) {
					sharkDirs[snum][dir][pidx] = Integer.parseInt(st.nextToken());
				}
			}
		}

		// 0. 초기 상어의 냄새를 남기기
		for (int snum = 1; snum <= M; snum++) {
			int x = sharks[snum].x;
			int y = sharks[snum].y;

			sWhoBoard[x][y] = snum;
			sTimeBoard[x][y] = k;
		}
	}

	/*
	 * 경계 체크
	 */
	static boolean outOfBorder(int x, int y) {
		return x < 0 || y < 0 || x >= N || y >= N;
	}

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();

		int answer = moveSharks();

		if (answer == 1001) {
			bw.write(-1 + "");
		} else {
			bw.write(answer + "");
		}

		bw.flush();
		bw.close();
	}
}
