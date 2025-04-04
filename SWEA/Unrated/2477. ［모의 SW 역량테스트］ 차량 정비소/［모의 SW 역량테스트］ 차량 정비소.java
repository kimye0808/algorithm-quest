import java.io.*;
import java.util.*;
import javax.activation.CommandInfo;

/* 접수 창구 및 정비 창구의 담당자 업무 능력이 달라서 담당자 별 처리 시간이 다르다.
 * 한 담당자의 처리 시간은 고객과 고장의 종류에 상관 없이 항상 같다.
 * 
 * 1. 접수 창구 -> 2. 정비 창구 -> 3. 설문지(종료)
 * 고객번호 k의 고객이 차량 정비소에 도착하는 시간은 tk이다. (1 ≤ k ≤ K)
 * 
 * A:접수 창구의 우선순위는 아래와 같다.
   ① 여러 고객이 기다리고 있는 경우 고객번호가 낮은 순서대로 우선 접수한다.
   ② 빈 창구가 여러 곳인 경우 접수 창구번호가 작은 곳으로 간다.
 * B:정비 창구의 우선순위는 아래와 같다.
   1. 먼저 기다리는 고객이 우선한다. -> 들어온 순서나 시간을 알아야 한다
   2. 두 명 이상의 고객들이 접수 창구에서 동시에 접수를 완료하고 정비 창구로 이동한 경우,
   2-1. 동시에 들어오는 경우는 일반적인 큐로 처리하면 안된다. 객체에 시간을 기록한다든가 로 처리
   2-2. 이용했던 접수 창구번호가 작은 고객이 우선한다. -> 이용했던 접수 창구 번호도 알아야 한다  
   3. 빈 창구가 여러 곳인 경우 정비 창구번호가 작은 곳으로 간다.
 * 
 * t가 0일대 처리 시간되면 바로 다음 창구로 넘어간다, 바로 대기 인력이 채워진다
 * 
 * A 는 타겟 접수 창구 번호, B는 타겟 정비 창구 번호
 * 창구번호는 최대 9
 * 창구에서 처리 시간들은 최대 20
 * 고객의 도착시간은 최대 1000
 * 창구번호 고객번호는 1-based
 */
/*
 * 숫자들이 작아서 구현만 하면 되는 문제
 * 고객 객체 : 
 * 도착시간
 * 접수창구idx
 * 접수창구 들어온 시간
 * 접수종료시간
 * 정비창구idx
 * 정비창구 들어온 시간
 * 정비창구 종료 시간
 * 
 * 고객 리스트 List<Man>
 * 
 * 창구는 상태 갱신 하는게 창구 사용하는 인간 idx 밖에 없으므로 그냥 객체로 하지 말자
 * 접수 창구 시간 int[]
 * 접수 창구 리스트 int[] idx는 창구idx 값은 사용중인 인간 idx (0은 빈 창구)
 * 정비 창구 시간 int[]
 * 정비 창구 리스트 int[] 동일
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static class Man {
		int idx;
		int receptIdx, repairIdx; // 접수창구idx, 정비창구idx
		int receptStartTime, repairStartTime; // 접수창구 들어온 시간, 정비창구 들어온 시간
		int receptTermTime, repairTermTime; // 접수종료시간, 정비창구 종료 시간

		Man(int idx) {
			this.idx = idx;
		}
	}

	static Man[] mans;
	static int[] arriveTimes; // 고객별 도착시간
	static int[] receptTimes;
	static int[] repairTimes;
	static int[] manInRecept;
	static int[] manInRepair;
	static PriorityQueue<Man> receptAwaitQ;
	static PriorityQueue<Man> repairAwaitQ;

	static int N, M, K, A, B;
	/*
	 * N=Integer.parseInt(st.nextToken()); // 접수 창구 개수
	 * M=Integer.parseInt(st.nextToken()); // 정비 창구 개수
	 * K=Integer.parseInt(st.nextToken()); // 고객 수
	 * A=Integer.parseInt(st.nextToken()); // 타겟 접수 창구
	 * B=Integer.parseInt(st.nextToken()); // 타겟 정비 창구
	 */

	static void receptMan(int time) {
		// System.out.println("시간 :"+time);
		// 1-0. 창구에서 시간 다 됐으면 정비 창구 대기 큐로 넘긴다
		for (int idx = 1; idx <= N; idx++) {
			if (manInRecept[idx] != 0) {
				int inManIdx = manInRecept[idx];
				if (time == mans[inManIdx].receptTermTime) {
					repairAwaitQ.offer(mans[inManIdx]);
					manInRecept[idx] = 0; // 창구를 비운다
				}
			}
		}

//		for(int idx=1; idx<=N;idx++) {
//			System.out.print(manInRecept[idx]+" ");
//		}
//		System.out.println();
		
		for (int manIdx = 1; manIdx <= K; manIdx++) {
			if (arriveTimes[manIdx] == time) {
				// 1-1. 먼저 대기 큐에 넣는다
				receptAwaitQ.offer(mans[manIdx]);
			}
		}
		
		// 1-2. 자리비면 대기 큐에서 뽑아서 넣는다
		// 빈공간이 있으면 넣는다
		for (int idx = 1; idx <= N; idx++) {
			if (manInRecept[idx] == 0) {
				if (!receptAwaitQ.isEmpty()) {
					Man tmpMan = receptAwaitQ.poll();
					manInRecept[idx] = tmpMan.idx;
					mans[tmpMan.idx].receptIdx = idx;
					mans[tmpMan.idx].receptStartTime = time;
					mans[tmpMan.idx].receptTermTime = time + receptTimes[idx];
				}
			}
		}
	}
	
	static void repairMan(int time) {
		// 우선, 정비 창구에서 시간 다 된놈들 뺀다
		for (int cIdx = 1; cIdx <= M; cIdx++) {
			if (manInRepair[cIdx] != 0) {
				int inManIdx = manInRepair[cIdx];
				if(mans[inManIdx].repairTermTime == time) {
					manInRepair[cIdx] = 0;
				}
			}
		}
		for (int cIdx = 1; cIdx <= M; cIdx++) {
			if (manInRepair[cIdx] == 0) {
				if (!repairAwaitQ.isEmpty()) {
					Man man = repairAwaitQ.poll();
					manInRepair[cIdx] = man.idx;
					man.repairIdx = cIdx;
					man.repairStartTime = time;
					man.repairTermTime = time + repairTimes[cIdx];
				}
			}
		}	
	}

	static void timer() {
		for (int time = 0; time <= 4005; time++) {
			// 1. 접수 창구
			receptMan(time);

			// 2. 정비 창구
			repairMan(time);
		}
	}
	
	static int getResult() {
		int sum=0;
		for(int mIdx=1; mIdx<=K; mIdx++) {
			if(mans[mIdx].receptIdx == A && mans[mIdx].repairIdx == B) {
				sum += mIdx;
			}
			// System.out.println("유저"+mIdx+":"+mans[mIdx].receptIdx+" , "+mans[mIdx].repairIdx);
		}
		return sum;
	}

	static int doSimulation() {
		timer();
		
		return getResult();
	}

	/*
	 * 초기화 + 입력
	 */
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken()); // 접수 창구 개수
		M = Integer.parseInt(st.nextToken()); // 정비 창구 개수
		K = Integer.parseInt(st.nextToken()); // 고객 수
		A = Integer.parseInt(st.nextToken()); // 타겟 접수 창구
		B = Integer.parseInt(st.nextToken()); // 타겟 정비 창구

		mans = new Man[K + 1];
		for (int kidx = 1; kidx <= K; kidx++) {
			mans[kidx] = new Man(kidx);
		}
		manInRecept = new int[N + 1];
		receptTimes = new int[N + 1];
		
		manInRepair = new int[M + 1];
		repairTimes = new int[M + 1];
		
		arriveTimes = new int[K + 1];

		/*
		 * ① 여러 고객이 기다리고 있는 경우 고객번호가 낮은 순서대로 우선 접수한다. ② 빈 창구가 여러 곳인 경우 접수 창구번호가 작은 곳으로
		 * 간다.
		 */
		receptAwaitQ = new PriorityQueue<>(new Comparator<Man>() {
			@Override
			public int compare(Man o1, Man o2) {
				return Integer.compare(o1.idx, o2.idx);
			}
		});

		/*
		 * ① 먼저 기다리는 고객이 우선한다. ② 두 명 이상의 고객들이 접수 창구에서 동시에 접수를 완료하고 정비 창구로 이동한 경우, 이용했던
		 * 접수 창구번호가 작은 고객이 우선한다. ③ 빈 창구가 여러 곳인 경우 정비 창구번호가 작은 곳으로 간다
		 */
		repairAwaitQ = new PriorityQueue<>(new Comparator<Man>() {
			@Override
			public int compare(Man o1, Man o2) {
				// 1번 조건
				if (o1.receptTermTime != o2.receptTermTime) {
					return Integer.compare(o1.receptTermTime, o2.receptTermTime);
				}
				// 2번 조건
				return Integer.compare(o1.receptIdx, o2.receptIdx);
			}
		});

		// ai 입력
		st = new StringTokenizer(br.readLine().trim());
		for (int aidx = 1; aidx <= N; aidx++) {
			receptTimes[aidx] = Integer.parseInt(st.nextToken());
		}
		// bi 입력
		st = new StringTokenizer(br.readLine().trim());
		for (int bidx = 1; bidx <= M; bidx++) {
			repairTimes[bidx] = Integer.parseInt(st.nextToken());
		}
		// 사람 입력
		st = new StringTokenizer(br.readLine().trim());
		for (int kidx = 1; kidx <= K; kidx++) {
			arriveTimes[kidx] = Integer.parseInt(st.nextToken());
		}
		
//		for(int man=1; man<=K; man++) {
//			System.out.println("man : "+man+" "+arriveTimes[man]);
//		}
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		int T = Integer.parseInt(br.readLine().trim());

		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");

			init();
			
			int answer = doSimulation();
			if(answer == 0) answer = -1;
			
			sb.append(answer).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
