import java.io.*;
import java.util.*;

/*
X가 3으로 나누어 떨어지면, 3으로 나눈다.
X가 2로 나누어 떨어지면, 2로 나눈다.
1을 뺀다.
세 가지 연산을 적절히 사용해서 1로 만들어야 한다
연산 횟수의 최솟값

BFS로는 못하나?

[상태정의]:
f(x)=V , X가 x일때 연산 횟수 최솟값 V
f2(3*x) = V2

[상태 전이]
f = min(f2 + 1, f3(2*x) + 1, f4(x+1) + 1))
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int N = Integer.parseInt(br.readLine().trim());
		
		int[] state = new int[N+1];
		Arrays.fill(state, Integer.MAX_VALUE);
		
		state[N] = 0;
		for(int n=N; n>=1; n--) {
			if(n+1 <= N) {
				state[n] = Math.min(state[n], state[n+1] + 1);
			}
			if(3*n <= N) {
				state[n] = Math.min(state[n], state[3*n] + 1);
			}
			if(2*n <= N) {
				state[n] = Math.min(state[n], state[2*n] + 1);
			}
		}
		
		bw.write(state[1]+"");
		bw.flush();
		bw.close();
	}
}
