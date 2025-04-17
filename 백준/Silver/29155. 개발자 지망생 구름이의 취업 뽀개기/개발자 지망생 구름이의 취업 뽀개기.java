import java.io.*;
import java.util.*;

/*
 * 난이도 같은 거 연속 -> 푸는 시간 + 두 문제 푸는데 걸리는 시간 차이만큼 증가
 * 난이도 다른 거 연속 -> 푸는 시간 + 60분
 * 난이도마다 오름차순 정렬해서 앞에서 몇개 꺼내면 된다
 * 어차피 단계 바뀔때는 60 고정이라 상관없고
 * 단계 비슷할때는 
 * 예를 들어
 * 1단계가
 * 4개를 골라야 하고
 * 후보가 1 4 4 4 4 7
 * 이 있으면
 * 1 4 4 4
 * 또는
 * 4 4 4 4
 * 가 최적
 * 근데 1 4 4 4 하나 4 4 4 4 하나 결과는 같음
 * 그래서 그냥 오름차순 배열에서 순서대로 뽑으면 된다
 */
public class Main {
	static int N;
	static int[] ps;
	static List<Integer>[] pInfos;
	
	
	public static void main(String[] args) {
		Scanner sc=  new Scanner(System.in);
		
		N = sc.nextInt();
		pInfos = new ArrayList[6]; // 1-based
		for(int pnum=1; pnum<=5; pnum++) {
			pInfos[pnum] = new ArrayList<>();
		}
		
		ps = new int[6]; // 1~5
		for(int idx=1; idx<=5; idx++) {
			ps[idx] = sc.nextInt();
		}
		
		// 문제들 난이도 + 걸리는 시간 입력
		for(int pidx=0; pidx<N; pidx++) {
			int k = sc.nextInt();
			int t = sc.nextInt();
			
			pInfos[k].add(t);
		}
		

		int totalTime = 0;

		for(int pnum=1; pnum<=5; pnum++) {
			// 단계 바뀌면 60 추가
			if(pnum != 1) totalTime += 60;
			
			// 오름차순 정렬
			Collections.sort(pInfos[pnum]);
			
			// 진행
			int prevTime = 0;
			for(int idx=0; idx < ps[pnum]; idx++) {
				totalTime += pInfos[pnum].get(idx);
				if(prevTime != 0) {
					totalTime += pInfos[pnum].get(idx) - prevTime;
				}
				prevTime = pInfos[pnum].get(idx);
			}
		}
		
		System.out.println(totalTime);
	}
}
