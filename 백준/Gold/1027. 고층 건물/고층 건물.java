import java.io.*;
import java.util.*;

/*
 * 처음에 하나씩 입력 받으면서 스택이나 링크드 리스트로
 * 처리하면 되지 않을까 생각했었는데
 * 그건 틀린 풀이
 * 문제 조건에 따르면 애초에 제일 높다고 해서
 * 제일 많은 빌딩을 볼 수 있다는 보장도 없음
 * 
 * 빌딩을 본다는 조건:
 * A가 B를 볼 수 있다 = 두 지붕을 잇는 선분이 A와 B를
 * 제외한 다른 고층 빌딩을 지나거나 접하지 않아야 한다
 * 
 * 그냥 높으면 다 보이는 줄...
 * 
 * N이 50이하이므로 그냥 완탐 돌려도 될 정도임
 * for i
 * 	i번째 빌딩 기준으로 왼쪽 오른쪽 가보면서 보이나 체크
 * 
 * 1 3 5 면 1에서 5는 보임
 * 1 4 5 면 1에서 5는 못봄
 * 즉 a + (b-a)/2 보다 같거나 큰 높이가 있는지 또 반복문 돌리면 됨
 * 라고 하면 안된다
 * 왜 이렇게 하냐
 * 기울기로 비교해야지 중간점으로 되겠냐
 * 경사가 더 급한게 나오면 안보이기 시작하는거임
 * 음수든 양수든 중간에 기울기가 더 큰 녀석이 나오면 안보인다
 * 다르게 표현하면 기울기가 제일 큰 애보다 작으면 안보인다
 */
class Main{
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N;
	static int[] buildings;
	
	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		N = Integer.parseInt(br.readLine().trim());
		buildings = new int[N];
		
		st = new StringTokenizer(br.readLine().trim());
		for(int idx=0; idx<N; idx++) {
			buildings[idx] = Integer.parseInt(st.nextToken());
		}
		
		int answer=0;
		for(int idx=0; idx<N; idx++) {
			int rightCnt=0;
//			double maxIncl = Double.MIN_VALUE; 주의! double의 minvalue는
			// 음수가 아니라 0에 가장 가까운 최소값이라함..
			double maxIncl = Double.NEGATIVE_INFINITY;
			for(int jdx=idx+1; jdx<N; jdx++) {
				int A = buildings[idx];
				int B = buildings[jdx];
				
				int width = jdx-idx;
				int height = B-A;
				double incl = (double)height/(double)width; 
				
				if(maxIncl < incl) {
					maxIncl = incl;
					rightCnt++;
				}
			}
			int leftCnt=0;
//			maxIncl = Double.MIN_VALUE;
			maxIncl = Double.NEGATIVE_INFINITY;
			for(int jdx = idx-1; jdx>=0; jdx--) {
				int A = buildings[idx];
				int B = buildings[jdx];

				int width = idx-jdx;
				int height = B-A;
				double incl = (double)height/(double)width; 
				
				if(maxIncl < incl) {
					maxIncl = incl;
					leftCnt++;
				}
			}
			answer = Math.max(answer, leftCnt+rightCnt);
		}
		bw.write(answer+"");
		bw.flush();
		bw.close();
	}
}