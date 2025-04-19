import java.io.*;
import java.util.*;

/*
 * 처음에 백트래킹 이나 순열인가 싶었는데 N이 4242로 불가능
 * 그러면 DP나 그리디인데
 * DP로는 힘들 것 같아서 그리디이지 않을까 생각
 * 정답은 next permutation 방식과 유사
 * 
 * 4 2 라면
 * 1 2 3 4 이고
 * 0 1 2 3 이 해당 숫자 보다 작은 개수
 * 
 * 정답 = 3 1 2 4 인데
 * 3(2) + 나머지 오름차순 출력
 * 
 * K가 5라면
 * 4(3) + 3(2) + 나머지 오름차순 출력
 * 인데 4(3)->3(2)를 어떻게 나타낼 것인가
 * 일단은 1,2,3,4... 방식이기 때문에 어떤 수든 합으로 표현 가능하긴 함
 * 가능한 방법이 여러가지일 것이고 그 중에서 하나만 출력하면 되니까 어떻게든 만들면 됨
 * 
 * K보다 작은 수 중에서 최댓값 적기
 * 4
 * 4보다 작 개수는 3개
 * K가 5일때 5-3 = 2
 * 근데 2는 결국 3의 값
 * 2+1 해서 3 참조
 * 4 3 까지 완료
 * 남은 K는 0
 * 앞에 과정에서 썼던 것들은 진행 중에 flag 체크하고
 * 이제 순회하면서 flag 아닌 얘들 오름차순 출력
 */
class Main{
	static BufferedReader br;
	static StringTokenizer st;
	
	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine().trim());
		
		int N = Integer.parseInt(st.nextToken());
		int K = Integer.parseInt(st.nextToken());
		
		boolean[] used = new boolean[N+1];
		
		int num = Math.min(K+1, N);
		while(true) {
			if(K - (num - 1) >= 0 && num > 1 && num <= N) {
				if(used[num]) {
					num -= 1;
					continue;
				}
				used[num] = true;
				System.out.print(num+" ");
				
				K -= (num-1);
				num = Math.min(K+1, N);
			}else { 
				break;
			}
		}
		
		for(int idx = 1; idx <= N; idx++) {
			if(used[idx]) continue;
			System.out.print(idx+" ");
		}
	}
}