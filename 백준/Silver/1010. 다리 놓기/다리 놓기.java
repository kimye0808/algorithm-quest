import java.io.*;
import java.util.*;

/*
 * N, M
 * N개의 다리를 놓아야 한다
 * N,M은 최대 30
 * 다리를 놓을때 이전에 다리를 놓은 인덱스 이후에 다리를 놓아야 한다
 * 그냥 로우하게 하면
 * 30! 이라 시간초과난다
 * 
 * dp로 접근해보자
 * [상태 정의]
 * i번째 다리를 j번째 사이트에 놓을려고 할때 사이트의 인덱스 j는
 * i-1번째 다리를 놓은 사이트의 인덱스 j`보다 커야 한다
 * 다리 인덱스, 사이트 인덱스, 그때의 경우의 수(부분 문제)
 * 고려하는 다리 인덱스를 하나씩 늘려가면서 그때의 상태를 갱신해 나간다
 *  
 * f(i, j) i번재 인덱스의 다리까지 고려했을때, 사이트가 j개 있을때, 다리 놓는 경우의 수
 *
 * [상태 전이]
 * f(i, j) = f(i-1, j-1,-2,-3 ...0)의 합
 * 
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int T = Integer.parseInt(br.readLine().trim());
		for(int t=0; t<T; t++) {
			st = new StringTokenizer(br.readLine().trim());
			
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			
			int[][] state = new int[N+1][M+1];
			Arrays.fill(state[0], 1);
			for(int idx=1; idx<=N; idx++) {
				for(int jdx=1; jdx<=M; jdx++) {
					for(int kdx=jdx-1; kdx>= 0; kdx--) {
						state[idx][jdx] += state[idx-1][kdx];
					}
				}
			}
			
			bw.write(state[N][M]+"\n");
		}
		
		bw.flush();
		bw.close();
	}
}
