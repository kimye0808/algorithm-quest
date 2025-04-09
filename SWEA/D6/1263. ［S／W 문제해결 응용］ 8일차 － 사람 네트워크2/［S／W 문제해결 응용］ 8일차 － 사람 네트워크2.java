import java.io.*;
import java.util.*;

/*
 * 플로이드는
 * N^3이라 1000^3하면 시간 초과 날 것 같은데
 * 근데 왠지 문제가 플로이드 문제인 것 같음
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N;
	static int[][] adj; // 0-based
	static int MAX;
	
	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int T = Integer.parseInt(br.readLine().trim());
		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");

			MAX = 5000;
			
			st = new StringTokenizer(br.readLine().trim());
			N = Integer.parseInt(st.nextToken());
			adj = new int[N][N];
			
			// adj 초기 MAX로 초기화
			for(int idx=0; idx<N; idx++) {
				for(int jdx=0; jdx<N; jdx++) {
					adj[idx][jdx] = MAX;
				}
			}
			
			// 입력 받기
			for(int f=0; f<N; f++) {
				for(int t=0; t<N; t++) {
					int num = Integer.parseInt(st.nextToken()); 
					if(num==1) {
						adj[f][t] = num;
					}
				}
			}
			
			// 플로이드로 점간 거리 처리
			for(int k=0; k<N; k++) {
				for(int f=0; f<N; f++) {
					for(int t=0; t<N; t++) {
							if(adj[f][t] > adj[f][k] + adj[k][t]) {
								adj[f][t] = adj[f][k] + adj[k][t];
							}
					}
				}
			}
			
			// 거리 합 계산
			int[] CC = new int[N];
			for(int f=0; f<N; f++) {
				for(int t=0; t<N; t++) {
					if(f==t) continue;
					if(adj[f][t] != 0 || adj[t][f] != 0) {
						CC[f] += Math.min(adj[f][t], adj[t][f]);
					}
				}
			}
			
			// 최소 값 구하기
			int minCC = MAX;
			for(int idx=0; idx<N; idx++) {
				if(minCC > CC[idx]) {
					minCC = CC[idx];
				}
			}
			
			sb.append(minCC+"").append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
