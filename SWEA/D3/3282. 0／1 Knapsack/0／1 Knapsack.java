import java.io.*;
import java.util.*;

/*
 * 0/1 배낭 문제
 * [상태 정의]:
 * f(idx, volume) = cost
 * idx까지 고려했을때 최대 volume을 담을 수 있는 상황에서 최대 cost
 * 
 * [상태 전이]:
 * 담을 수 있으면
 * f(idx, volume) = max(f(idx-1, volume-V) + V, f(idx-1, V)
 * 
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N, K;
	static int[] volume;
	static int[] cost;
	static int[][] state; // dp 용 상태
	
	static void init() throws Exception{
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		volume = new int[N+1];
		cost = new int[N+1];
		state = new int[N+1][K+1];
		
		for(int idx=1; idx<=N; idx++) {
			st = new StringTokenizer(br.readLine().trim());
			volume[idx] = Integer.parseInt(st.nextToken());
			cost[idx] = Integer.parseInt(st.nextToken());
		}
	}
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
	
		int T = Integer.parseInt(br.readLine().trim());
	
		for(int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");

			init();
			
			for(int idx=1; idx<=N; idx++) {
				for(int v=1; v<=K; v++) {
					if(v-volume[idx] >= 0) { // 담을 수 있으면
						state[idx][v] = Math.max(state[idx-1][v-volume[idx]] + cost[idx], state[idx-1][v]);
					}else { // 담을 수 없으면
						state[idx][v] = state[idx-1][v];
					}
				}
			}
			
			sb.append(state[N][K]).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
