
import java.io.*;
import java.util.*;

/*
 * S는 A와 B 각 원소의 곱들의 합
 * S가 최소가 되도록 A를 재배치하면 되는데
 * B를 재배치 해서는 안된다
 * =>
 * 곱들의 합인데
 * B의 큰 값 * A의 작은 값 들의 합 이 최소
 * B를 내림 차순 , A를 오름 차순 정렬
 * 곱들의 합을 구한다.
 */
public class Main {
	public static void main(String[] args) throws Exception{
		Scanner sc = new Scanner(System.in);
		int N = sc.nextInt();
		
		Integer[] A = new Integer[N];
		Integer[] B = new Integer[N];
		
		for(int idx=0; idx<N; idx++) {
			A[idx] = sc.nextInt();
		}
		for(int idx=0; idx<N; idx++) {
			B[idx] = sc.nextInt();
		}
		
		Arrays.sort(A);
		Arrays.sort(B, (x, y) -> {
			return Integer.compare(y, x);
		});
		
		int sum = 0;
		for(int idx=0; idx<N; idx++) {
			sum += A[idx] * B[idx];
		}
		
		System.out.println(sum);
	}
}
