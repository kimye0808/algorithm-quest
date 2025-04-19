import java.io.*;
import java.util.*;

/*
 * 너무 어렵게 생각한듯
 * 정방향 누적합 중 가장 큰 K개를 고른다
 * 
 * 가능한 이유
 * 3 2 0 -1 4 일 경우
 * 3 5 5 4 8
 * 
 * 1,2,4번 인덱스 선택,
 * 
 * 3 5
 * 3 5 5
 * 3 5 5 4 8 
 * 
 * 자연스럽게 누적합 = 해당 교실까지 방문 점수에 더해짐
 * 누적합 K 개의 합 = 총 점수
 */
class Main{
	static int N, K;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		N = sc.nextInt();
		K = sc.nextInt();
		
		int[] Ascores = new int[N];
		Long[] accSum = new Long[N];
		
		for(int idx=0; idx<N; idx++) {
			Ascores[idx] = sc.nextInt();
		}
		
		long sum = 0;
		for(int idx=0; idx < N; idx++) {
			sum += Ascores[idx];
			accSum[idx] = sum;
		}
		
		Arrays.sort(accSum, new Comparator<Long>() {
			public int compare(Long a, Long b) {
				return Long.compare(b, a);
			}
		});

		long answer = 0;
		for(int idx=0; idx < K; idx++) {
			answer += accSum[idx];
		}
		
		System.out.println(answer);
	}
}