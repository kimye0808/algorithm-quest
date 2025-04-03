import java.io.*;
import java.util.*;

/*
 * 1번 집의 색은 2번 집의 색과 같지 않아야 한다.
 * N번 집의 색은 N-1번 집의 색과 같지 않아야 한다.
 * i(2 ≤ i ≤ N-1)번 집의 색은 i-1번, i+1번 집의 색과 같지 않아야 한다.
 * 
 * [상태 정의]:
 * f(i) = V  : i 고려했을때 최소 비용 V
 * 지만 조건을 만족시키기에는 부족
 * 
 * n-1번 집과 색 n+1번 집과 색이 달라야 하는 조건을 만족시켜야 한다
 * 
 * index
 * color
 * 
 * f1(i,color) = V : i번째 집을 고려하고 해당 색이 color일때 비용의 최솟값 V
 * f2(i-1, color`) = V` : i-1번째 집을 고려하고 해당 색이 color`일때 비용의 최솟값 V`
 * 
 * f2 -> f1
 * [상태 전이]:
 * f(i, color) = min(f(i-1, 조건만족color) + cost[해당color])
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static final int R = 0;
	static final int G = 1;
	static final int B = 2;
	
	static int N;
	static int[][] cost;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		N = Integer.parseInt(br.readLine().trim());
		cost = new int[N+1][3];
		
		for(int home=1; home<=N; home++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int color=0; color<3; color++) {
				cost[home][color] = Integer.parseInt(st.nextToken());
			}
		}
		
		int[][] state = new int[N+1][3];
		
		for(int idx=1; idx<=N; idx++) {
			state[idx][R] = Math.min(state[idx-1][G] + cost[idx][R], state[idx-1][B] + cost[idx][R]);
			state[idx][G] = Math.min(state[idx-1][R] + cost[idx][G], state[idx-1][B] + cost[idx][G]);
			state[idx][B] = Math.min(state[idx-1][R] + cost[idx][B], state[idx-1][G] + cost[idx][B]);
		}
		
		int answer = Integer.MAX_VALUE;
		for(int color=R; color<=B; color++) {
			answer = Math.min(answer, state[N][color]);
		}
		
		bw.write(answer+"");
		bw.flush();
		bw.close();
	}
}
