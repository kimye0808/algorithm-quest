import java.io.*;
import java.util.*;

/*
 * 단순하게
 * -뺄 수 있으면 빼고, 2개 돌려받고
 * 반복
 * 정렬 문제
 * 
 * 문자열마다 개수 지정
 * totEvCnt - 총진화횟수
 * String[] - 인덱스별 포켓몬 이름
 * evCnts[] - 인덱스별 진화횟수
 * candies[] - 인덱스별 캔디 보유량
 */
class Main{
	static BufferedReader br;
	static StringTokenizer st;
	
	static int N;
	static int totEvCnt;
	static String[] pokeNames;
	static int[] evCnts;
	static int[] candies;
	static int[] evCandies;
	
	static int getEvCnt(int idx) {
		int cnt = 0;
		while(true) {
			if(candies[idx] - evCandies[idx] >= 0) {
				candies[idx] -= evCandies[idx];
				candies[idx] += 2;
				cnt++;
			}else {
				break;
			}
		}
		return cnt;
	}
	
	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		
		N = Integer.parseInt(br.readLine().trim());
		
		pokeNames = new String[N];
		evCnts = new int[N];
		candies = new int[N];
		evCandies = new int[N];
		
		for(int idx=0; idx<N; idx++) {
			String name = br.readLine().trim();
			
			pokeNames[idx] = name;
			
			st = new StringTokenizer(br.readLine().trim());
			evCandies[idx] = Integer.parseInt(st.nextToken());
			candies[idx] = Integer.parseInt(st.nextToken());
			
			// 진화 횟수 파악
			evCnts[idx] = getEvCnt(idx);
			totEvCnt += evCnts[idx];
		}
		
		int maxIdx=0;
		int maxNum = 0;
		for(int idx=0; idx<N; idx++) {
			if(maxNum < evCnts[idx]) {
				maxNum = evCnts[idx];
				maxIdx = idx;
			}
		}
		
		System.out.println(totEvCnt);
		System.out.println(pokeNames[maxIdx]);
	}
}