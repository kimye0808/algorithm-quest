import java.io.*;
import java.util.*;

/*
 * 초기 줄기세포 : 비활성
 * 생명력 수치가 X인 줄기 세포는 X시간 동안 비활성, X시간 이후 활성 상태
 * 활성 상태에서 X시간 동안 존재, X시간 후에 세포 죽음
 * 비활성 -> X시간 -> 활성 -> X시간 -> 죽음
 * 죽더라도 해당 그리드셀 차지
 * 
 * 활성화된 줄기 세포는 1시간 동안 네 방향 동시 번식, 늘어난 줄기 세포는 비활성 상태
 * 이미 (죽음 + 비활성 + 활성) 줄기 세포 존재하는 곳은 번식 X
 * 하나의 그리드 셀에 동시 번식하려고 하는 경우, 생명력 수치가 높은 줄기 세포가 해당 그리드 셀 차지
 * 동시 번식, 생명력 수치 높은 순서로 차지
 * 
 * 그리드의 크기는 무한하다
 * 
 * K시간 후 살아있는 줄기 세포(비활성 + 활성)의 총 개수?
 */
/*
 * 그리드: 무한하다는 것이 중요. K시간의 범위가 정해져 있어서 그냥 어느 정도 최대 크기가 있긴 함
 * K시간 동안 확장한다 쳐도 최대 300 밖에 안됨
 * 대충 1000 * 1000 하고 초기 판을 가운데에 놓으면 충분할듯
 * 그게 아니면 hashSet으로 string을 키로 좌표 표현해야 할 것 같다
 * 근데 그건 좀 귀찮으므로
 * 그냥 500, 500 부터 입력받은 보드판을 복사해서 500,500을 원점으로 생각하자
 * 
 * 줄기 세포(객체): 
 * 좌표, 비활성/활성/죽음 상태, 생명력 수치
 */
public class Solution{
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		int T = Integer.parseInt(br.readLine().trim());
		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");

			init();

			int answer = simulate();

			sb.append(answer).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}

	static int INACTIVE = 0, ACTIVE = 1, DEAD = 2;
	static int[] DR = { -1, 0, 1, 0 };
	static int[] DC = { 0, 1, 0, -1 };

	static class Cell {
		int r, c;
		int state;
		int power;
		int inactStartTime;
		int actStartTime;

		Cell(int r, int c, int power, int inactStartTime) {
			this.r = r;
			this.c = c;
			this.power = power;
			this.state = INACTIVE;
			this.inactStartTime = inactStartTime;
		}

		@Override
		public boolean equals(Object obj) {
			if(this==obj) return true;
			if(obj == null || getClass() != obj.getClass()) return false;
			Cell other = (Cell) obj;
			return (other.r == r && other.c == c && other.power == power &&
					other.inactStartTime == inactStartTime && other.actStartTime == actStartTime);
		}
		@Override
		public int hashCode() {
			return Objects.hash(r,c,state,power,inactStartTime,actStartTime);
		}
	}

	static Cell[][] grid; // 확장된 보드판
	static int[][] inputGrid; // 입력받은 보드판
	static Set<Cell> cells; // cell 집합

	static int N, M;
	static int K;

	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		grid = new Cell[1000][1000];
		inputGrid = new int[N][M];
		cells = new HashSet<>();

		for (int row = 0; row < N; row++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int col = 0; col < M; col++) {
				inputGrid[row][col] = Integer.parseInt(st.nextToken());
			}
		}

		for (int row = 0; row < N; row++) {
			for (int col = 0; col < M; col++) {
				if(inputGrid[row][col] == 0) continue;
				int power = inputGrid[row][col];
				Cell cell = new Cell(row + 500, col + 500, power, 0);

				grid[row + 500][col + 500] = cell;
				cells.add(cell);
			}
		}
	}

	static int simulate() {
		for (int turn = 1; turn <= K; turn++) {
			List<Cell> addedCells = new ArrayList<>();
			List<Cell> deletedCells = new ArrayList<>();
			for (Cell nowCell : cells) {
				if (nowCell.state == DEAD)
					continue; // 혹시 몰라서 처리

				// 1. 활성화면
				if (nowCell.state == ACTIVE) {
					// 1-1. 활성화를 비활성화로 복제
					if (turn == nowCell.actStartTime + 1) {
						copyCell(nowCell, turn, addedCells);
					}
					// 1-2. 활성화를 사망 처리
					if (turn == nowCell.actStartTime + nowCell.power) {
						nowCell.state = DEAD;
						deletedCells.add(nowCell);
					}
				}

				// 2. 비활성화를 활성화 처리
				if (nowCell.state == INACTIVE) {
					if (turn == nowCell.inactStartTime + nowCell.power) {
						nowCell.actStartTime = turn;
						nowCell.state = ACTIVE;
					}
				}
			}
			// 기존 것들을 삭제 한다
			for (Cell deletedCell : deletedCells) {
				cells.remove(deletedCell);
			}
			// 새로 생긴 것들을 추가 한다
			for (Cell addedCell : addedCells) {
				cells.add(addedCell);
			}
		}
		
		int answer = getResult();
		return answer;
	}

	/*
	 * 결과 얻는 함수
	 */
	private static int getResult() {
		int cnt = 0;

		Iterator<Cell> cellIter = cells.iterator();
		while (cellIter.hasNext()) {
			Cell cell = cellIter.next();
			if (cell.state == INACTIVE || cell.state == ACTIVE)
				cnt++;
		}
		return cnt;
	}

	/*
	 * 비활성 셀들을 복제한다
	 */
	private static void copyCell(Cell nowCell, int turn, List<Cell> addedCells) {
		int nowr = nowCell.r;
		int nowc = nowCell.c;
		for (int dir = 0; dir < 4; dir++) {
			int newr = nowr + DR[dir];
			int newc = nowc + DC[dir];

			// grid가 null인 경우에만 새롭게 추가
			if (grid[newr][newc] == null) {
				Cell newCell = new Cell(newr, newc, nowCell.power, turn);
				grid[newr][newc] = newCell;
				addedCells.add(newCell);
			} else {
				if (grid[newr][newc].state == INACTIVE) {
					// 비활성화된 상태인데 현재 타임에 생성된 거라면
					if (grid[newr][newc].inactStartTime == turn) {
						// 생명력 수치가 더 높은 줄기 세포로 대체한다
						if (nowCell.power > grid[newr][newc].power) {
							Cell newCell = new Cell(newr, newc, nowCell.power, turn);

							addedCells.remove(grid[newr][newc]);
							grid[newr][newc] = newCell;
							addedCells.add(newCell);
						}
					}
				}
			}
		}
	}
}
