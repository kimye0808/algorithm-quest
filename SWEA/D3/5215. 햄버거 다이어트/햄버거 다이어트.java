import java.io.*;
import java.util.*;
/*
 * 칼로리 L
 * 칼로리 L 이하에서 가장 높은 점수의 합
 * 같은 재료를 여러 번 사용할 수 없다
 * 
1
5 1000
100 200
300 500
250 300
500 1000
400 400
점수 칼로리

일반적인
[배낭 문제]

[1. 부분문제 식별]
(하향식 접근)
최대 1000 칼로리 까지 일때 최대 만족도 문제
-> 3번 인덱스의 재료를 골랐다면? ->
최대 700 칼로리 까지 일때 최대 만족도 문제 (3번 인덱스 제외하고 사용해야 함)
->
중복 부분문제 구조

(상향식 접근으로 전환)
최대 칼로리 L2인 문제  -> 최대 칼로리 L1 인 문제를 해결할때 사용

인덱스(재료)는?
재료도 고려해줘야 한다


[2. 상태 정의]
f(i) = V
i 인덱스의 재료까지 고려했을때 최대 만족도 V
는 칼로리 조건을 만족하기엔 부족하다 -> 상태의 차원을 늘린다

index : index의 재료를 넣냐 안넣냐가 중요하다
calorie : index의 재료의 칼로리를 고려하냐 안하냐가 중요하다
V : index와 calorie의 영향을 받는 최대 만족도

f(index, calorie) = V
index 인덱스까지의 재료를 고려했을때, 칼로리가 calorie까지 가능할때, 최대 만족도 V


for index
	for calorie
	인가
for calorie
	for index
	인가


예를 들어서
1번 인덱스까지의 재료를 고려했을때
최대 칼로리는 0~1000까지 증가

[1][200] = [1][0] + good(1)
[1][500] 은 ? [1][500] = [1][300] + good(1) 
...


[상태 전이 표]
    | 1 | 2 | 3 | 4 | 5 재료| 
0   | 0 | 0 | 생략
생략
200 |100|100|
생략
300 |100|100
생략
400 |100|100
생략
500 |100|100 vs 300 갱신 => 300
생략
1000|100|
칼로리

f(i, j) = V
i 인덱스의 재료를 고려했을 때 칼로리가 j일때 최대 만족도 V

[3. 상태 전이]
f(i, j) = max(f(i-1, j-cal[i]) + good[i], f(i-1, j));
 */
public class Solution {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static int N, L;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int T;
		T = Integer.parseInt(br.readLine().trim());

		for (int testCase = 1; testCase <= T; testCase++) {
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(testCase).append(" ");
			
			// 입력 받기
			st = new StringTokenizer(br.readLine().trim());
			N=Integer.parseInt(st.nextToken());
			L=Integer.parseInt(st.nextToken());
			
			int[] cal = new int[N+1]; // 1-based
			int[] good = new int[N+1];
			for(int idx=1; idx<=N; idx++) {
				st = new StringTokenizer(br.readLine().trim());
				good[idx] = Integer.parseInt(st.nextToken());
				cal[idx] = Integer.parseInt(st.nextToken());
			}
			
			// 상태
			int[][] state = new int[N+1][L+1];
			
			// 상태 전이
			// idx를 바깥쪽 루프로 설정하고 안쪽 루프를 칼로리로 설정하는 이유:
			// 1. 상태 전이: 설정된 이전 상태 값을 이용해야만 한다
			// 1-1. 칼로리가 바깥쪽 루프라면 상태 정의 및 전이가 깨진다 -> 최대 칼로리에 대해서 한 번에 모든 재료들을 고려하는데
			// 1-2. 그렇게 되면 위의 전이식은 해당 재료를 순차적으로 넣게 됨, 원하는 결과가 나오지 않고, 재료 순서가 바뀌면 값이 달라진다
			// 2. 1번 재료만 고려했을때, 2번 재료까지 고려했을때, 3번 재료까지 고려했을때 ...
			// 2-1. index를 증가시키면서 재료를 포함할지 안할지 결정해야 한다
			// 3. 1번 재료를 고려할때 최대 칼로리 수치들에 대해서 싹 최대 호감도 갱신
			// 3-1. 2번 재료도 고려할때 1번까지 고려한 것에 대해서 2번 재료를 추가적으로 고려해서 최대 칼로리 수치들에 대해서 싹 최대 호감도 갱신
			// 3-2. 반복
			for(int idx=1; idx<=N; idx++) {
				for(int calorie = 0; calorie <= L; calorie++) {
					if(calorie - cal[idx] >= 0) { // idx의 재료를 넣을 수 있다
						state[idx][calorie] = 
								Math.max(
										state[idx-1][calorie-cal[idx]] + good[idx], 
										state[idx-1][calorie]
								);
					}else { // 어차피 idx의 재료를 못넣는 상태
						state[idx][calorie] = state[idx-1][calorie];
					}
				}
			}
			
			sb.append(state[N][L]).append("\n");
			bw.write(sb.toString());
		}
		bw.flush();
		bw.close();
	}
}
