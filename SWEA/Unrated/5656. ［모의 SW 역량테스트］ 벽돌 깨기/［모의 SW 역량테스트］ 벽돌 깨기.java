import java.io.*;
import java.util.*;

/*
일단 다 해봐야 한다
열마다 구슬을 중복해서 쏠 수 있다
W 개의 열에서 N을 중복 순열 만든다
해당 순열로 다 해본다

def makeBeadsPerm:
1. W 까지의 수 중에서 N개를 중복해서 뽑아서 배열에 저장한다
중복 순열 구하는 것을 이상하게 짬 그래서 헤맴

def bombBead:int[][] tempBoard
0. List<int[]> burstedBeads 터진 구슬 좌표 리스트
1. 입력받은 좌표를 기준으로 해당 칸 포함, 상하좌우로는 (구슬 넘버 - 1) 칸 탐색하면서 burstedBeads에 넣기
2. burstedBeads를 순회하면서 해당 칸을 0으로 만들고 연쇄적으로 burstedBeads 수행한다, tempBoard 갱신, 넘겨준다
3. tempboard 리턴
=> 이러면 안됨
한 번에 모아서 터트려야 하기 때문에
그냥 bfs 돌려서 리스트 모은다음에 터트려야 한다

def fallBlocks:int[][] tempBoard
1. tempBoard에서 컬럼 마다 리스트를 뽑는다
2. 해당 칼럼 리스트 에서 row 역순으로 순회하면서 0을 제외하고 새로 리스트를 만든다
3. 해당 리스트로 tempBoard를 대체한다
4. tempBoard 리턴

def countBlocks
1. 전체 순회해서 1이상 카운팅
2. 카운트 리턴

def throwBead:
0-0. tempBoard 는 originBoard의 카피본, 작업수행할때 갱신, 넘겨준다
0. beadsPerm 돌면서 다음의 작업 수행
1. 해당 열에서 처음으로 나오는 0보다 큰 숫자를 찾는다
2. 해당 숫자를 찾으면 bombBead로 tempBoard 갱신
3. fallBlocks 수행해서 tempBoard 갱신
4. countBlocks 수행
5. 최댓값 갱신 

구하는 것: 최대한 많은 벽돌을 제거하는데, 그때 남은 벽돌 개수
 */
class Solution
{
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N, C, R; // N 번의 구슬 던지기, C 가로, R 세로
	static int answer;
	static int burstedBlockCnt;
	static int localBurstedBlockCnt; // 시뮬 돌릴때 초기화 해주는 터지는 블록 개수
	static int[][] originBoard;
	static int[] beadsPerm;
	
	static void init() throws Exception{
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		R = Integer.parseInt(st.nextToken());
		
		answer=0;
		originBoard = new int[R][C];
		beadsPerm = new int[N];
		burstedBlockCnt = 0;
		localBurstedBlockCnt = 0;
		
		for(int r=0; r<R; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int c=0; c<C; c++) {
				originBoard[r][c] = Integer.parseInt(st.nextToken());
			}
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		int T;
		T=Integer.parseInt(br.readLine().trim());

		for(int testCase = 1; testCase <= T; testCase++)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");
			init();
			
			simulate();
			
			sb.append(answer).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
	
	static void printBoard(String name, int[][] board) {
		System.out.println(name);
		for(int r=0; r<R; r++) {
			for(int c=0; c<C; c++) {
				System.out.print(board[r][c] +" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static void simulate() {
		 makeBeadsPerm(0);
	}
	
	/*
	def makeBeadsPerm:
		1. W 까지의 수 중에서 N개를 중복해서 뽑아서 배열에 저장한다
		2. 기저 조건 : 개수가 N개가 되면 작업 수행, W 넘어서면 리턴
	*/
	static void makeBeadsPerm(int depth) { 
		if(depth == N) { // 다 채움
			// 작업 수행
			localBurstedBlockCnt = 0;
			throwsBead();
			return;
		}
		// 안뽑거나
		for(int jdx= 0; jdx < C; jdx++) {
			// 뽑거나
			beadsPerm[depth] = jdx;
			makeBeadsPerm(depth+1);
		}
	}

	
	static boolean outOfBorder(int r, int c) {
		return r < 0 || c < 0 || r >= R || c >= C;
	}
	
	static int[] DR = {-1,0,1,0};
	static int[] DC = {0, 1,0,-1};
	/*
	def bombBead:
	잘못 생각함
	한 번에 모아서 터트려야함
	*/
	static List<int[]> bombBead(int[][] tempBoard, int sr, int sc) {
		List<int[]> burstedBeads = new ArrayList<>();
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[] {sr, sc, tempBoard[sr][sc]-1});
		boolean[][] vis = new boolean[R][C];
		vis[sr][sc] = true;
		
		while(!q.isEmpty()) {
			int[] now = q.poll();
			int r = now[0];
			int c = now[1];
			int maxDist = now[2];
			
			burstedBeads.add(new int[] {r,c});
			
			for(int dir=0; dir<4; dir++) {
				for(int dist=0; dist<=maxDist; dist++) {
					int nr = r + dist * DR[dir];
					int nc = c + dist * DC[dir];
					
					if(outOfBorder(nr, nc)) continue;
					if(tempBoard[nr][nc] == 0) continue;
					if(vis[nr][nc]) continue;
					
					vis[nr][nc]=true;

					q.offer(new int[] {nr, nc, tempBoard[nr][nc]-1});
				}
			}
		}
		
		return burstedBeads;
	}
	
	/*
	def fallBlocks:int[][] tempBoard
	1. tempBoard에서 컬럼 마다 리스트를 뽑는다
	2. 해당 칼럼 리스트 에서 row 역순으로 순회하면서 0을 제외하고 새로 리스트를 만든다
	3. 해당 리스트로 tempBoard를 대체한다
	4. tempBoard 리턴
	*/
	static int[][] fallBlocks(int[][] tempBoard) {
		List<Integer>[] columnList = new ArrayList[C];
		for(int idx=0; idx<C; idx++) {
			columnList[idx] = new ArrayList<>();
		}
		
		for(int c=0; c<C; c++) {
			for(int r=R-1; r>=0; r--) {
				if(tempBoard[r][c] != 0) {
					columnList[c].add(tempBoard[r][c]);	
				}
			}
		}
		
		int[][] nextBoard = new int[R][C];
		for(int c=0; c<C; c++) {
			int columnListSize = columnList[c].size();
			int columnIdx = 0;
			for(int r=R-1; r>=0; r--) {
				if(columnIdx < columnListSize) {
					nextBoard[r][c] = columnList[c].get(columnIdx);
					columnIdx++;
				}
			}
		}
		
		return nextBoard;
	}
	
	static int countBlocks(int[][] tempBoard) {
		int cnt=0;
		for(int r=0; r<R; r++) {
			for(int c=0; c<C; c++) {
				if(tempBoard[r][c] > 0) {
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	/*
	def throwBead:
		0-0. tempBoard 는 originBoard의 카피본, 작업수행할때 갱신, 넘겨준다
		0. beadsPerm 돌면서 다음의 작업 수행
		1. 해당 열에서 처음으로 나오는 0보다 큰 숫자를 찾는다
		2. 해당 숫자를 찾으면 bombBead로 tempBoard 갱신
		3. fallBlocks 수행해서 tempBoard 갱신
		4. countBlocks 수행
		5. 최댓값 갱신 
	*/
	static void throwsBead() {
		localBurstedBlockCnt=0;
		// 복사 보드판
		int[][] tempBoard = new int[R][C];
		for(int r=0; r<R; r++) {
			for(int c=0; c<C; c++) {
				tempBoard[r][c] = originBoard[r][c];
			}
		}
		// 0 작업 수행
		for(int idx=0; idx<N; idx++) {
			int targetColumn = beadsPerm[idx];
			// 처음 깨기
			int targetNum = -1;
			int targetRow = -1;
			for(int row=0; row<R; row++) {
				if(tempBoard[row][targetColumn] > 0) {
					targetRow = row;
					targetNum = tempBoard[row][targetColumn];
					break;
				}
			}
			// 이미 전부 0인경우
			if(targetNum == -1 && targetRow == -1) continue;
			
			// bomb 시작
			List<int[]> burstedBombs = bombBead(tempBoard, targetRow, targetColumn);
			for(int[] bursted : burstedBombs) {
				int r = bursted[0];
				int c = bursted[1];
				tempBoard[r][c] = 0;
				localBurstedBlockCnt++;
			}
			
			// 3. fallBlocks 수행해서 tempBoard 갱신
			tempBoard = fallBlocks(tempBoard);
		}
		
		// 4. countBlocks 수행
		if(burstedBlockCnt < localBurstedBlockCnt) {
			burstedBlockCnt = localBurstedBlockCnt;
			answer = countBlocks(tempBoard);
		}
	}
	
}