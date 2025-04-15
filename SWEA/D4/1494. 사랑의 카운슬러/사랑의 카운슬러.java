import java.io.*;
import java.util.*;

/*
 * 임의의 지렁이 두 마리를 매칭 후, 지렁이가 다른 지렁이가 있는 곳으로 가게 한다
 * 가능한 지렁이들이 움직인 벡터 합의 크기를 작게 한다
 * A -> B면 벡터는 B - A
 * 벡터들의 합의 크기
 * 벡터 크기 = x*x + y*y
 * 모든 좌표값은 10만 보다 작거나 같은 정수
 * 벡터 크기는 long으로 해야 함 
 * 
 * N마리 중에 2마리씩 매칭
 * NC2 N-2C2 N-4C2 ... 2C2
 * N은 최대 20
 * 근데 이러면 20! 수준 너무 크다
 * 
 * 어차피 벡터 합 의 크기를 구하는 거니까
 * 벡터 합을 구하면 되는 거고
 * 벡터 합은 사실 +인 녀석들 반, -인 녀석들 반 나눠서 더하면 되는거 아닌가
 * 그러면 사실상
 * 2^20 으로 바꿀 수 있음
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N;
	static int[][] earthWorms;
	static int plusX;
	static int plusY;
	static int minusX;
	static int minusY;
	static long answer;
	
	static void makePlusMinus(int idx, int plusCnt, int minusCnt) {
		if(idx == N) {
			if(plusCnt == minusCnt && plusCnt == N/2) {
				// 벡터 합을 계산한다
				long x = plusX - minusX;
				long y = plusY - minusY;
				long vectorsize = x*x + y*y;
				
				answer = Math.min(answer, vectorsize);
			}
			return;
		}
		// 플러스 팀에 넣거나
		plusX += earthWorms[idx][0];
		plusY += earthWorms[idx][1];
		makePlusMinus(idx+1, plusCnt+1, minusCnt);
		plusX -= earthWorms[idx][0];
		plusY -= earthWorms[idx][1];
		// 마이너스 팀에 넣거나
		minusX += earthWorms[idx][0];
		minusY += earthWorms[idx][1];
		makePlusMinus(idx+1, plusCnt, minusCnt+1);
		minusX -= earthWorms[idx][0];
		minusY -= earthWorms[idx][1];
	}
	
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		int T = Integer.parseInt(br.readLine().trim());
		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");
			
			N = Integer.parseInt(br.readLine().trim());
			
			earthWorms = new int[N][2];
			for(int idx=0; idx<N; idx++) {
				st = new StringTokenizer(br.readLine().trim());
				earthWorms[idx][0] = Integer.parseInt(st.nextToken());
				earthWorms[idx][1] = Integer.parseInt(st.nextToken());
			}

			answer = Long.MAX_VALUE;
			makePlusMinus(0, 0, 0);

			sb.append(answer).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
