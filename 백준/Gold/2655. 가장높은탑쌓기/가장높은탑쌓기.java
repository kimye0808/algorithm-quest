import java.io.*;
import java.util.*;

/*
 * 0. 로우하게 해보면 벽돌의 수는 최대 100개라 백트래킹은 시간 초과
 * 0-0. dp 느낌 나는데 1시간 고민해도 뭔지 몰랐음
 * 
 * 1. 벽돌이 위로 쌓인다고 했을때, 가장 위에 있는 벽돌의 넓이 와 무게가 중요함
 * 2. 사실상 가장 위에 있는 벽돌의 넓이와 무게 기준으로 높이를 생각해볼 수 있다 <- (1)
 * 3. 그러면 어떤 벽돌이 가장 위에 있는지 체크해야 하기 때문에 결국 인덱스도 고려해줘야 함
 * 4. 그러면 3차원 dp인가 생각할 수도 있는데 벽돌의 넓이, 무게는 1만이기 때문에 불가능
 * 4-1. 또한 가장 위에 있는 벽돌의 넓이와 높이를 차원을 넓히면서까지 넣을 필요가 있나?
 * 5. 그러면 특정 인덱스의 벽돌을 가장 높은 벽돌이라고 고려했을때 최대 높이를 구하는 방법이지 않을까?
 * 6. 이 형태는 가장 증가하는 부분 수열의 길이 (LIS) 와 유사함
 * 7. LIS에서 고려하는 것은 인덱스와 길이 이며, 특정 인덱스의 문자를 마지막으로 하는 가장 긴 문자열의 길이(1씩 증가)
 * 7-1. 를 갱신해 나감
 * 8. 마찬가지로 이 문제도 1씩 증가하던 문자열의 (가로)길이가 여러 수치로 증가할 수 있는 (세로) 높이가 됨
 * 9. 또한 특정 인덱스의 문자를 마지막(가로)에서 특정 인덱스의 블록을 제일 위(세로)로 방향만 바뀌고 수치만 다양해진 것
 * 10. 하지만 LIS에서는 현재 숫자보다 작은 숫자들 중에서 (1차원) 가장 긴 문자열 을 찾았다면
 * 11. 이 문제는 넓이, 무게로 2차원을 고려하게 됨
 * 12. 주요 차이점: 
 * 12-1. 길이(1차원) -> 넓이, 무게(2차원)으로 증가,
 * 12-2. 길이 1 증가 -> 높이 (다양한 수치) 증가,
 * 12-3. 가장 긴 부분 문자열의 가장 마지막 인덱스(가로) -> 가장 높은 블록에서 가장 위의 블록(세로)
 * 13. 다른 점은 1차원 -> 2차원으로 고려할게 증가했다는 것인데, 이는 생각해보면 간단하게 2차원을 사실상 1차원으로 만들 수 있다
 * 13-2. 넓이나 무게 둘 중 하나로 내림차순 정렬을 하고 시작하면, 쉽게 2차원 -> 1차원으로 바꿀 수 있다.
 * 13-3. 그 이후로는 LIS를 적용하면 사실상 1차원이다
 * 14. 이전 것의 기록은 해당 dp 배열을 갱신할때 이전 것을 기록하는 방식으로 해결 가능하다
 */
public class Main {
	static int A = 0, H = 1, W = 2, INDEX = 3;
	static int BCNT;
	static int[][] blocks;
	static int[] DP;
	static int[] logs;
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		BCNT = sc.nextInt();
		
		blocks = new int[BCNT][4];
		DP = new int[BCNT];
		logs = new int[BCNT];
		
		for(int idx=0; idx<BCNT; idx++) {
			blocks[idx][A] = sc.nextInt();
			blocks[idx][H] = sc.nextInt();
			blocks[idx][W] = sc.nextInt();
			blocks[idx][INDEX] = idx;
		}
		
		// 넓이 or 무게로 내림차순 정렬, 여기선 넓이
		Arrays.sort(blocks, new Comparator<int[]>(){
			@Override
			public int compare(int[] o1, int[] o2) {
				return Integer.compare(o2[A], o1[A]);
			}
		});
		
		for(int idx=0; idx<BCNT; idx++) {
			int maxHeight = 0;
			int aptIdx=idx;
			for(int jdx=0; jdx < idx; jdx++) {
				if(maxHeight < DP[jdx] && blocks[jdx][W] >= blocks[idx][W]) {
					maxHeight = DP[jdx];
					aptIdx = jdx;
				}
			}
			DP[idx] = maxHeight + blocks[idx][H];
			logs[idx] = aptIdx;
		}
		
		// 결과 가장 큰 값 찾기
		int maxHeight = 0;
		int aptIdx = 0;
		for(int idx=0; idx<BCNT; idx++) {
			if(maxHeight < DP[idx]) {
				maxHeight = DP[idx];
				aptIdx = idx;
			}
		}
		
		// 이제 벽돌 로그 구하기
		List<Integer> answerLogs = new ArrayList<>();
		int answerCnt = 0;
		while(true) {
			answerCnt++;
			answerLogs.add(aptIdx);
			if(aptIdx == logs[aptIdx]) break;
			aptIdx = logs[aptIdx];
		}

		// 이러면 정렬된 배열 인덱스로 출력함
//		System.out.println(answerCnt);
//		for(int log : answerLogs) {
//			System.out.println(log+1);
//		}
		
		System.out.println(answerCnt);
		for(int log : answerLogs) {
			System.out.println(blocks[log][INDEX] + 1);
		}
		
	}
}
