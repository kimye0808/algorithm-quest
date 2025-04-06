import java.io.*;
import java.util.*;

/*
 * 설계 + 구현 완료 : 1시간 34분
 * 디버깅 : 시간 초과
 * 사실상 틀린 문제
 * 설계를 잘못했고 문제에 드러나지 않은 조건들을 고려하지 못했다
 */
/*
 * [실수한 점]
 * 1. smell 처리:
 * -> 나는 smell 객체를 만들어서 보드판으로 처리할려고 함
 * -> Smell[][] 이렇게 했는데 이러면 칸마다 Smell 객체 1개
 * -> 문제는 2칸 전의 Smell을 삭제함
 * -> 그럴 듯 해보이지만 만약 해당 턴에 물고기 죽어서 특정 칸에 Smell 객체가 생기고
 * -> 그 칸이 2칸 전의 Smell 객체와 겹친다면? 해당 턴의 Smell 객체도 사라지게 됨
 * -> 설계를 잘못했다
 * -> Smell List를 만들어서 처리하든가
 * -> 애초에 (1) S가 최대 100이고 (2) 2턴 전의 Smell은 삭제해야 하므로
 * -> (3) 현재 턴에 물고기 움직임에 영향을 주는 Smell은 -1 턴, -2 턴 밖에 없음
 * -> 사실상 boolean[턴][r][c]로도 처리 가능하며 이러면 Smell을 굳이 삭제 하는
 * -> 불필요한 함수를 수행하지 않아도 된다는 소리
 * -> 물고기마다 smell을 만든다는 문제의 내용에 의문을 가졌어야 했다
 * => 특정 턴의 방문 처리나 객체 배열 처리할때 현재 턴이나 다른 턴의 방문 처리나 객체 배열
 * => 처리에 영향을 주지는 않을지 고민해보자
 * 
 * 2. 상어 이동 처리:
 * -> 상어는 움직인 다음에 갔던 곳을 또 갈 수 있음 (문제에서 재방문 불가 같은 조건 없음)
 * -> 근데 이미 다녀가서 물고기 먹어치웠는데 굳이 갈 이유가 있냐라고 생각할 수 있음
 * -> 그래서 나는 방문 처리했었는데
 * -> 생각해보니 물고기 먹은 수가 같으면 방향문자열 사전순으로 처리함
 * -> 그러면 물고기 먹은 수는 같더라도 재방문한 방식이 사전순으로 앞설 수 있음
 * => 방문 처리가 정말로 필요한가 고민해보고 특히나 우선순위 처리가 요구되어 있다면
 * => 한 번 더 고민해보자
 * 
 * [알게 된 점]
 * 1. String 문자열 사전순 앞서는 거 처리:
 * -> String 배열이나 리스트를 사전순으로 정렬한다 -> Arrays.sort Collections.sort
 * -> 이렇게 하면 되고
 * -> 문자열 끼리 1:1로 사전순 비교한다고 치면
 * -> s1.compareTo(s2) 를 이용하면 된다 (어차피 String 내부에 이걸로 정렬함)
 * -> 작을수록 사전순으로 앞서기 때문에 음수면 s1이 사전순으로 앞서는거
 */
class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	// 물고기 방향 1-based
	static int[] FDR = { 0, 0, -1, -1, -1, 0, 1, 1, 1 };
	static int[] FDC = { 0, -1, -1, 0, 1, 1, 1, 0, -1 };
	// 상어 방향 1-based
	static int[] SDR = { 0, -1, 0, 1, 0 };
	static int[] SDC = { 0, 0, -1, 0, 1 };

	static class Fish {
		int r, c, dir;
		boolean isDead;

		Fish(int r, int c, int dir) {
			this.r = r;
			this.c = c;
			this.dir = dir;
			isDead = false;
		}

		Fish(Fish other) {
			this.r = other.r;
			this.c = other.c;
			this.dir = other.dir;
			isDead = false;
		}
	}

	static int M, S; // M 은 물고기 수, S 는 연습 횟수
	static List<Fish>[][] fishMap; // 1-based
	static List<Fish> fishes; // 0-based
	static List<Fish> fishClones; // 0-based
	static boolean[][][] smellMap; // 1-based, 턴, 좌표 r, c
	static int sharkr, sharkc;
	static String aptPath;
	static int maxFishCnt;
	static int[][] sharkAte;

	@SuppressWarnings("unchecked")
	static void init() throws Exception {
		st = new StringTokenizer(br.readLine().trim());
		M = Integer.parseInt(st.nextToken());
		S = Integer.parseInt(st.nextToken());

		fishMap = new ArrayList[5][5];
		for (int r = 1; r <= 4; r++) {
			for (int c = 1; c <= 4; c++) {
				fishMap[r][c] = new ArrayList<>();
			}
		}
		fishes = new ArrayList<>();
		// fishclone은 나중에
		smellMap = new boolean[101][5][5];

		for (int idx = 0; idx < M; idx++) {
			st = new StringTokenizer(br.readLine().trim());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());

			fishes.add(new Fish(r, c, d));
		}

		st = new StringTokenizer(br.readLine().trim());
		sharkr = Integer.parseInt(st.nextToken());
		sharkc = Integer.parseInt(st.nextToken());
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		init();

		int answer = simulate();
		bw.write(answer + "");
		bw.flush();
		bw.close();
	}

	static boolean outOfBorder(int r, int c) {
		return r < 1 || c < 1 || r > 4 || c > 4;
	}

	static void initSharkDirStatus() {
		aptPath = "444";
		maxFishCnt = 0;
		sharkAte = new int[5][5];
	}

	static int simulate() {
		for (int sidx = 1; sidx <= S; sidx++) {
			// 1. copyFIsh
			copyFish();
			// 2. moveFish
			moveFish(sidx);
			// 3-1. findDirShark
			initSharkDirStatus();
			findSharkDir(sharkr, sharkc, new ArrayList<>(), 0);
			// 3-2. moveShark
			moveShark(sidx);

			// 4. deleteSmell
//			deleteSmell(sidx);

			// 5. welcomeClonefish
			welcomeCloneFish();
		}
		return getResult();
	}

	static int getResult() {
		int answer = 0;
		for (Fish fish : fishes) {
			if (!fish.isDead)
				answer++;
		}

		return answer;
	}

	static void copyFish() {
		fishClones = new ArrayList<>();
		for (Fish fish : fishes) {
			if (fish.isDead)
				continue;
			fishClones.add(new Fish(fish)); // 무조건 새 fish 만들어야 함
		}
	}

	@SuppressWarnings("unchecked")
	static void moveFish(int turn) {
		fishMap = new ArrayList[5][5];
		for (int r = 1; r <= 4; r++) {
			for (int c = 1; c <= 4; c++) {
				fishMap[r][c] = new ArrayList<>();
			}
		}
		// fish 객체 좌표 갱신
		for (Fish fish : fishes) {
			if (fish.isDead)
				continue;
			int r = fish.r;
			int c = fish.c;
			int d = fish.dir;

			// 방향 주의하자 - 방향 숫자는 시계방향 1-8인데 찾을때는 반시계 회전임
			for (int dirCnt = 1; dirCnt <= 8; dirCnt++) {
				int nr = r + FDR[d];
				int nc = c + FDC[d];

				if (outOfBorder(nr, nc)) {
					d = ((d - 1) - 1 + 8) % 8 + 1;
					continue;
				}
				if (sharkr == nr && sharkc == nc) {
					d = ((d - 1) - 1 + 8) % 8 + 1;
					continue;
				}
				if (smellMap[turn - 1][nr][nc]) {
					d = ((d - 1) - 1 + 8) % 8 + 1;
					continue;
				}
				if (turn > 2 && smellMap[turn - 2][nr][nc]) {
					d = ((d - 1) - 1 + 8) % 8 + 1;
					continue;
				}

				fish.r = nr;
				fish.c = nc;
				fish.dir = d;
				break;
			}
		}
		// fishMap 갱신
		for (Fish fish : fishes) {
			if (fish.isDead)
				continue;
			fishMap[fish.r][fish.c].add(fish);
		}
	}

	static void findSharkDir(int r, int c, List<Integer> dirs, int fishCnt) {
		if (dirs.size() == 3) {
			StringBuilder sb = new StringBuilder();
			for (int didx = 0; didx < 3; didx++) {
				sb.append(dirs.get(didx));
			}

			String nowPath = sb.toString();

			if (maxFishCnt < fishCnt) {
				aptPath = nowPath;
				maxFishCnt = fishCnt;
			} else if (maxFishCnt == fishCnt) {
				if (aptPath.compareTo(nowPath) > 0) {
					aptPath = nowPath; // 더 작은게 더 사전순으로 앞섬
					maxFishCnt = fishCnt;
				}
			}

			return;
		}

		for (int dir = 1; dir <= 4; dir++) {
			int nr = r + SDR[dir];
			int nc = c + SDC[dir];

			if (outOfBorder(nr, nc))
				continue;

			int nxtFishCnt = fishCnt + fishMap[nr][nc].size();
			// 이미 상어가 먹은곳이라면 더하지 않는다
			if (sharkAte[nr][nc] >= 1) {
				nxtFishCnt = fishCnt;
			}
			sharkAte[nr][nc] += 1;
			dirs.add(dir);
			findSharkDir(nr, nc, dirs, nxtFishCnt);
			dirs.remove(dirs.size() - 1);
			sharkAte[nr][nc] -= 1;
		}
	}

	static void moveShark(int turn) {
		int nr = sharkr;
		int nc = sharkc;
		boolean[][] alreadyRemoved = new boolean[5][5];
		for (char dirChar : aptPath.toCharArray()) {
			int dir = dirChar - '0';

			nr += SDR[dir];
			nc += SDC[dir];

			if (fishMap[nr][nc].size() > 0) {
				smellMap[turn][nr][nc] = true;
				for (Fish fish : fishMap[nr][nc]) {
					fish.isDead = true;
				}
			}
		}
		sharkr = nr;
		sharkc = nc;
	}
// 불필요한 수
//	static void deleteSmell(int turn) {
//		if (turn > 2) {
//			for (int r = 1; r <= 4; r++) {
//				for (int c = 1; c <= 4; c++) {
//					if (smellMap[r][c] == null)
//						continue;
//
//					if (smellMap[turn-2][r][c]) {
//						smellMap[turn-2][r][c] = null;
//					}
//				}
//			}
//		}
//	}

	static void welcomeCloneFish() {
		for (Fish fish : fishClones) {
			if (fish.isDead)
				continue;
			fishes.add(fish);
		}
	}
}