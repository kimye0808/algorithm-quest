import java.io.*;
import java.util.*;

class Main{
	static BufferedReader br;
	static StringTokenizer st;
	
	static int T;
	static int N;
	static int[] candies;
	
	static void makeEven() {
		for(int idx=0; idx<N; idx++) {
			if(candies[idx] % 2 == 1) {
				candies[idx] += 1;
			}
		}
	}
	
	static void rotateCandies() {
		int[] willCandies = new int[N]; // idx번째 값은 idx+1에 전해질 사탕 수
		
		for(int idx=0; idx<N; idx++) {
			willCandies[idx] = candies[idx] / 2;
			candies[idx] -= candies[idx]/2;
		}
		
		for(int idx=0; idx<N; idx++) {
			int nextIdx = (idx + 1) % N;
			candies[nextIdx] += willCandies[idx];
		}
	}
	
	static void printCandies() {
		for(int idx=0; idx<N; idx++) {
			System.out.print(candies[idx]+" ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		
		T = Integer.parseInt(br.readLine().trim());
		
		for(int testCase=0; testCase<T; testCase++) {
			
			int answer = 0;
			
			N = Integer.parseInt(br.readLine().trim());
			
			candies = new int[N];
			
			st = new StringTokenizer(br.readLine().trim());
			for(int idx=0; idx<N; idx++) {
				candies[idx] = Integer.parseInt(st.nextToken());
			}
			
			// 첫번째로 캔디 홀짝 체크
			makeEven();
			// 이후 옆 애한테 주기 -> 캔디 홀짝 체크 반복
			while(true) {
				boolean done = true;
				int candCnt = candies[0];
				for(int idx=1; idx<N; idx++) {
					if(candCnt != candies[idx]) {
						done = false;
						break;
					}
				}
				if(done) break;
				
				answer++; 
				
				rotateCandies();
				
				makeEven();
			}
			
			System.out.println(answer);
		}
	}
}