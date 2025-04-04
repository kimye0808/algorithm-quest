import java.io.*;
import java.util.*;

/*
 * 플로이드 워셜
 * 학생 수가 최대 500이므로
 * N^3해도 무난하다
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static int N, M;
	static boolean[][] relations;

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		int T = Integer.parseInt(br.readLine().trim());

		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");

			N = Integer.parseInt(br.readLine().trim());
			M = Integer.parseInt(br.readLine().trim());

			relations = new boolean[N+1][N+1];

			for (int midx = 0; midx < M; midx++) {
				st = new StringTokenizer(br.readLine().trim());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());

				relations[a][b] = true;
			}

			for (int k = 1; k <= N; k++) {
				for (int idx = 1; idx <= N; idx++) {
					for (int jdx = 1; jdx <= N; jdx++) {
						if (relations[idx][k] && relations[k][jdx]) {
							relations[idx][jdx] = true;
						}
					}
				}
			}

			int answer=0;
			for (int idx = 1; idx <= N; idx++) {
				boolean invalid = false;
				for (int jdx = 1; jdx <= N; jdx++) {
					if(idx==jdx) continue;
					// a-> b b->a 두 경우 중에 하나라도 가능한지 체크해야 한다
					if (!relations[idx][jdx] && !relations[jdx][idx]) {
						invalid = true;
					}
				}
				if(!invalid) {
					answer++;
				}
			}
			
			sb.append(answer).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
