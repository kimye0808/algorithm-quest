import java.io.*;
import java.util.*;

/*
 * 문제 이해 + 설계 : 34분
 * 구현 완료 : 2시간 3분 ( 1시간 30분 정도 걸림)
 * 디버깅 완료 + 예제 통과 : 3시간 15분
 * 했는데 시간초과
 * 다시 moveBeads 구현 : 20분
 * 
 * 빡세다
 */
/*
 * 실수한 점:
 * 1. 블리자드 마법 방향 인덱스는 1-based인데 0-based 로 처리
 * => 방향 인덱스가 몇부터 시작하는지 적어두거나 체크하고 시작하자
 * 
 * 2. moveBeads 루프 종료 조건에서 헤맴 <- 이거 결국 시간초과 발생함
 * -> 대충 이렇게 하면 되지 않을까 하고 설계하고 구현도 생각을 그만두고 했는데
 * -> 그 점에서 문제가 발생해서 여기서 디버깅 시간을 제일 많이 썼다
 * => 루프 종료 조건을 제대로 설계하고 들어가자, 특히나 무한 루프면 더 주의
 * 
 * 3. 구슬 변화 이후 구슬 개수가 보드판 보다 많아지면 소멸되는 처리 빼먹음
 * -> 문제 조건을 분명히 체크했어야 했는데 빼먹었다
 * => 구슬 소멸이나 범위 초과 같은 조건은 꼭 주의하자
 * 
 * 4. 보드판 참조하면서 동시에 갱신하던 문제
 * -> changeBeads에서 구슬판을 참조하는 동시에 구슬판을 갱신했었다
 * -> 그러니까 제대로 갱신이 되지 않는 분명 옛날 구슬판을 참조해서 사실상
 * -> 새 구슬판을 만들다시피 하는데 옛날 구슬판에다 갱신함
 * -> 갱신 문제 발생
 * => 보드판 참조와 동시에 보드판 갱신 혹은 객체 참조와 동시에 객체 갱신 같은 것을 할때는
 * => 특히나 주의하고, 새롭게 임시 보드판이나 객체를 만들어서 해당 객체에 갱신한 다음에
 * => 그걸로 새로 초기화 해야 하지는 않는지 고려하자
 * 
 * 5. moveBeads 시간 효율 문제
 * -> 시간초과 난 moveBeads는 무한 루프 돌면서 빈 칸 리스트를 만들어서
 * -> 더 이상 빈칸이 없을떄 까지 다음 칸 번호에서 땡겨오는 방식이었는데
 * -> 이 과정 자체가 시간 초과 원인 이라고 판단
 * -> 더 좋은 방법이 업을까 했는데
 * -> 아예 빈칸 제외한 구슬 리스트를 만들어서
 * -> 칸 번호에 맞게 다시 채운 다음에 구슬판을 대체하는 방식으로 바꿈
 * => 최대한 무한루프 방식의 풀이는 피하자. 실수하기 쉽고, 정해가 아닌 경우가 많은 듯.   
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	// 블리자드 마법 방향
	static int[] MDR = { 0, -1, 1, 0, 0 }; // 1,2,3,4 방향 주의
	static int[] MDC = { 0, 0, 0, -1, 1 };

	// 칸 넘버대로 순회 방향
	static int[] TDR = { 0, 1, 0, -1 };
	static int[] TDC = { -1, 0, 1, 0 };

	static int N; // 보드판 크기
	static int M; // 블리자드 마법 횟수
	static int[][] beads; // 보드판 위의 구슬
	static List<int[]> numPosList; // 칸 넘버의 좌표 리스트
	static int[][] orders; // 마법 명령 정보: d, s

	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		beads = new int[N + 1][N + 1]; // 1-based
		numPosList = new ArrayList<>();
		orders = new int[M][2]; // 0-based

		for (int r = 1; r <= N; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int c = 1; c <= N; c++) {
				beads[r][c] = Integer.parseInt(st.nextToken());
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
	
	static void print() {
		for(int r=1; r<=N; r++) {
			for(int c=1; c<=N; c++) {
				System.out.print(beads[r][c]+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();

		int[] result = doSimulation();
		
		int answer = result[1] + 2 * result[2] + 3 * result[3];
		
		bw.write(answer+"");
		bw.flush();
		bw.close();
	}

	static void makeNumPosList() {
		// 시작은 상어 위치
		int sr = (N + 1) / 2;
		int sc = (N + 1) / 2;
		numPosList.add(new int[] { sr, sc }); // 0번은 상어 위치
		int nr = sr;
		int nc = sc;
		int dir = 0; // 초기엔 왼쪽
		int maxLength = 1;

		// 1 방향 1 방향 2 방향 2 방향 ...
		boolean allFound = false;
		while (true) {
			// 같은 최대 길이는 2번까지
			for (int pCnt = 1; pCnt <= 2; pCnt++) {
				for (int dist = 1; dist <= maxLength; dist++) {
					nr += TDR[dir];
					nc += TDC[dir];

					numPosList.add(new int[] { nr, nc });

					// 종료 지점까지 전부 넣었으면
					if (nr == 1 && nc == 1) {
						allFound = true;
						break;
					}
				}
				if (allFound)
					break;
				dir = (dir + 1) % 4;
			}
			if (allFound)
				break;
			maxLength += 1;
		}
	}

	static void magicBlizzard(int magicDir, int magicSize) {
		int sr = (N + 1) / 2;
		int sc = (N + 1) / 2;

		for (int dist = 1; dist <= magicSize; dist++) {
			int nr = sr + dist * MDR[magicDir];
			int nc = sc + dist * MDC[magicDir];

			beads[nr][nc] = 0;
		}
	}

	static int[] doSimulation() {
		int[] totalBurstCnt = new int[4]; // 1-based : 1, 2, 3
		
		// makeNumPosList
		makeNumPosList();

		for (int oidx = 0; oidx < M; oidx++) {
			// magicBlizzard
			magicBlizzard(orders[oidx][0], orders[oidx][1]);
			
			while (true) {
				// moveBeads
				moveBeads();
				// burstBeads
				int[] burstCnts = burstBeads();

				// if 폭발개수 == 0 break
				int sum = 0;
				for (int bidx = 1; bidx <= 3; bidx++) {
					sum += burstCnts[bidx];
					totalBurstCnt[bidx] += burstCnts[bidx];
				}
				if (sum == 0)
					break;
			}
			// changeBeads
			changeBeads();
		}
		
		return totalBurstCnt;
	}
	
//	시간 초과 버전
//	static void moveBeads() {
//		while (true) {
//			List<int[]> emptyList = new ArrayList<>();
//			int beadsCnt = 0;
//			int consEmptyCnt = 0;
//			for (int nidx = 1; nidx < numPosList.size(); nidx++) {
//				int[] pos = numPosList.get(nidx);
//				int br = pos[0];
//				int bc = pos[1];
//
//				if (beads[br][bc] == 0) {
//					consEmptyCnt++;
//					emptyList.add(new int[] { nidx, br, bc });
//				}else {
//					consEmptyCnt=0;
//					beadsCnt++;
//				}
//			}
//
//			if(N*N - 1 - beadsCnt == consEmptyCnt){
//				break;
//			}
//
//			// 구슬 한 칸 이동
//			for (int[] emptyRoom : emptyList) {
//				int emptyIdx = emptyRoom[0];
//				int emptyR = emptyRoom[1];
//				int emptyC = emptyRoom[2];
//
//				if (emptyIdx != N * N - 1) { // 마지막 칸이 아니라면
//					int[] nextPos = numPosList.get(emptyIdx + 1); // 다음칸
//
//					beads[emptyR][emptyC] = beads[nextPos[0]][nextPos[1]];
//					beads[nextPos[0]][nextPos[1]] = 0;
//				}
//			}
//		}
//	}
	
	static void moveBeads() {
		// 빈 칸 제외 구슬 리스트를 뽑아서
		List<int[]> beadsList = new ArrayList<>();
		for (int nidx = 1; nidx < numPosList.size(); nidx++) {
			int[] pos = numPosList.get(nidx);
			int br = pos[0];
			int bc = pos[1];

			if (beads[br][bc] != 0) {
				beadsList.add(new int[] { br, bc });
			}
		}
		
		// 다시 채우는 방식 으로 빈칸 땡기기를 대체한다
		// 시간효율이 좋아진다... 
		int[][] tmpBeads = new int[N+1][N+1];
		int beadsIdx = 0;
		for (int nidx = 1; nidx < numPosList.size(); nidx++) {
			int[] pos = numPosList.get(nidx);
			int br = pos[0];
			int bc = pos[1];
			
			// beads리스트를 초과하면 탈출
			if(beadsIdx >= beadsList.size()) break;
			int[] beadsPos = beadsList.get(beadsIdx);
			tmpBeads[br][bc] = beads[beadsPos[0]][beadsPos[1]];
			beadsIdx++;
		}
		beads = tmpBeads; // static beads를 바뀐 beads로 대체
	}

	static int[] burstBeads() {
		int[] burstCnt = new int[4]; // 1-based, 1, 2, 3
		// 연속된 구슬 리스트
		List<int[]> consList = new ArrayList<>();
		for (int idx = 1; idx < numPosList.size(); idx++) {
			int[] nowPos = numPosList.get(idx);
			int nowPosGroup = beads[nowPos[0]][nowPos[1]];
			// 0이면 구슬이 아니다
			if (nowPosGroup == 0)
				continue;

			if (consList.size() == 0) {
				consList.add(new int[] { nowPos[0], nowPos[1] });
				continue;
			}
			// 연속 리스트가 있으면
			if (consList.size() >= 1) {
				int[] repPos = consList.get(0);
				int groupNum = beads[repPos[0]][repPos[1]];
				// 같은 그룹이면
				if (groupNum == nowPosGroup) {
					consList.add(nowPos);
					continue;
				} else { // 다른 그룹이면
					if (consList.size() >= 4) {
						// 폭발 구슬 개수 갱신
						burstCnt[groupNum] += consList.size();
						// 폭발 시킨다
						for (int consIdx = 0; consIdx < consList.size(); consIdx++) {
							int[] tmpPos = consList.get(consIdx);
							beads[tmpPos[0]][tmpPos[1]] = 0;
						}
					}
					// 리스트 초기화하고 새 그룹 만들기
					consList = new ArrayList<>();
					consList.add(nowPos);
				}
			}
		}
		// 잔반 처리 : cons가 남아있으면 4칸 이상이면 0만든다
		if (consList.size() >= 4) {
			int[] repPos = consList.get(0);
			int groupNum = beads[repPos[0]][repPos[1]];
			// 폭발 구슬 개수 갱신
			burstCnt[groupNum] += consList.size();
			// 폭발 시키고 리스트 초기화
			for (int consIdx = 0; consIdx < consList.size(); consIdx++) {
				int[] tmpPos = consList.get(consIdx);
				beads[tmpPos[0]][tmpPos[1]] = 0;
			}
			consList = new ArrayList<>();
		}

		return burstCnt;
	}

	/*
	 * 구슬 변화시키는 함수
	 */
	static void changeBeads() {
		List<List<int[]>> groups = new ArrayList<>();
		// 연속된 구슬 리스트
		List<int[]> consList = new ArrayList<>();
		for (int idx = 1; idx < numPosList.size(); idx++) {
			int[] nowPos = numPosList.get(idx);
			int nowPosGroup = beads[nowPos[0]][nowPos[1]];
			// 0이면 구슬이 아니다
			if (nowPosGroup == 0)
				continue;

			if (consList.size() == 0) {
				consList.add(new int[] { nowPos[0], nowPos[1] });
				continue;
			}
			// 연속 리스트가 있으면
			if (consList.size() >= 1) {
				int[] repPos = consList.get(0);
				int groupNum = beads[repPos[0]][repPos[1]];
				// 같은 그룹이면
				if (groupNum == nowPosGroup) {
					consList.add(nowPos);
				} else { // 다른 그룹이면
					groups.add(consList); // 그룹 리스트에 해당 그룹을 넣는다
					// 리스트 초기화하고 새 그룹 만들기
					consList = new ArrayList<>();
					consList.add(nowPos);
				}
			}
		}
		// 잔반 처리 : cons가 남아있으면 group 리스트에 넣는다
		if (consList.size() > 0) {
			groups.add(consList);
		}

		// 새롭게 만들어질 구슬판
		int[][] tempBeads = new int[N+1][N+1];
		int squareNum = 1;
		for (int gidx = 0; gidx < groups.size(); gidx++) {
			// 구슬이 칸 수보다 많아지면 그러한 구슬은 사라짐
			if(squareNum >= numPosList.size()) break;
			// A 처리
			int[] pos = numPosList.get(squareNum);

			int beadsCnt = groups.get(gidx).size();
			tempBeads[pos[0]][pos[1]] = beadsCnt;
			squareNum++;

			// 구슬이 칸 수보다 많아지면 그러한 구슬은 사라짐
			if(squareNum >= numPosList.size()) break;
			// B 처리
			pos = numPosList.get(squareNum);

			int[] repPos = groups.get(gidx).get(0);
			int groupNum = beads[repPos[0]][repPos[1]];
			tempBeads[pos[0]][pos[1]] = groupNum;
			squareNum++;
		}
		
		// 새로운 구슬판으로 static 변수를 대체한다
		beads = tempBeads;
	}
}