import java.io.*;
import java.util.*;

/*
 * 그냥 백트래킹하면 시간초과
 * 어떻게 해야할까
 * 바로 이전 것과 달라야 한다
 * 
 * dp로 접근해보자
 * f(i, num) = true/false
 * i번째 인덱스에 num일때 가능한지 안한지
 * f(i, num) = (num != num2)인 것 중에서 f(i+1, num2) 들의 || 연산
 * 
 * top down으로 접근할 수 있나
 * 근데 이 방식은 시간 복잡도 계산하기가 너무 어려운듯
 * 
 * 전체 상태수 * 각 상태에서의 연산량
 * day * dduck * for kind 문
 * N * 10 * 10 정도?
 * 
 * 시간 초과:
 * 원인 : memo 2차원 배열로 해당 상태를 파고 들어갔을때 성공 실패 유무만 기록
 * 하지만 실제로는 성공이든 실패든 방문했다면 리턴 해야 함
 * vis 2차원 배열도 추가
 */
class Main{
	static BufferedReader br;
	static StringTokenizer st;
	
	static int N;
	static List<Integer>[] dducks;
	static List<Integer> logs;
	
	static boolean[][] memo;
	static boolean[][] vis;
	
	/*
	 * day일 날에 이전 떡이 dduck일때
	 */
	static boolean makeDducks(int day, int dduck) {
		// N일까지 갔으면 성공
		if(day == N) {
			return true;
		}
		// 방문한거면 성공/실패 결과 리턴
		if(vis[day][dduck]) {
			return memo[day][dduck];
		}
		
		vis[day][dduck] = true;
		
		// 미리 false로 생각
		boolean result = false;
		for(int kind : dducks[day]) {
			// 같은 종류면 패스
			if(dduck == kind) continue;
			
			result = makeDducks(day+1, kind);
			if(result) {
				// 역순으로 저장함
				// N-1일, N-2일 ... 1일
				logs.add(kind);
				break;
			}
		}

		return memo[day][dduck] = result;
	}
	
	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		
		N = Integer.parseInt(br.readLine().trim());
		
		dducks = new ArrayList[N];
		for(int idx=0; idx < N; idx++) {
			dducks[idx] = new ArrayList<>();
		}
		memo = new boolean[N][10];
		vis = new boolean[N][10];
		
		for(int idx=0; idx < N; idx++) {
			st = new StringTokenizer(br.readLine().trim());
			
			int M = Integer.parseInt(st.nextToken());
			for(int jdx=0; jdx<M; jdx++) {
				int num = Integer.parseInt(st.nextToken());
				
				dducks[idx].add(num);
			}
		}
		
		boolean found = false;
		for(int idx=0; idx< dducks[0].size(); idx++) {
			logs = new ArrayList<>(); 
			if(makeDducks(1, dducks[0].get(idx))) {
				found = true;
				logs.add(dducks[0].get(idx));
				break;
			}
		}
		if(!found) {
			System.out.println(-1);
			return;
		}else {
			// 역순으로 저장된 상태여서 역순으로 봄
			for(int idx=N-1; idx >= 0; idx--) {
				System.out.println(logs.get(idx));
			}
		}
	}
}