import java.io.*;
import java.util.*;

/*
 * 어렵다..
 * 문제 해결을 위해 모든 경우를 시도하면 시간 초과 발생
 * 최소 날짜를 구하려면 2만큼 성장시키는 것을 최대한 많이 해야 함
 * 따라서 모든 나무를 2로 줄일 수 있는 경우로 가정
 * 예: 입력이 2 3 10 5라면
 * 높이 차: 8 7 0 5
 * 2로 줄이는 횟수: 4 + 3 + 0 + 2 = 9
 * 1로 줄이는 횟수: 0 + 1 + 0 + 1 = 2
 * 홀수와 짝수 횟수가 맞지 않음
 * 최소 날짜를 위해 짝수 날이 N개라면 홀수 날은 N개 이상이어야 함
 * 짝수 날을 홀수 날로 변환하며 조정
 * 예: 짝수 횟수 1회를 버리고 홀수 횟수 2회로 변경
 * 1회: 홀수 날(1,3,5,7), 짝수 날(2,4,6,8,10,12,14,16)
 * 2회: 홀수 날(1,3,5,7,9,11), 짝수 날(2,4,6,8,10,12,14)
 * 여기까지가 최적
 * 3회: 홀수 날(1,3,5,7,9,11,13,15), 짝수 날(2,4,6,8,10,12)
 * 여기는 최적이 아님
 */
public class Solution {
    static BufferedReader br;
    static BufferedWriter bw;
    static StringTokenizer st;

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // 테스트 케이스 수 입력
        int T = Integer.parseInt(br.readLine().trim());
        for (int testCase = 1; testCase <= T; testCase++) {
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(testCase).append(" ");

            // 나무 개수 입력
            int N = Integer.parseInt(br.readLine().trim());
            int[] tree = new int[N];

            // 최대 높이 찾기
            int maxHeight = 0;
            st = new StringTokenizer(br.readLine().trim());
            for (int idx = 0; idx < N; idx++) {
                tree[idx] = Integer.parseInt(st.nextToken());
                maxHeight = Math.max(maxHeight, tree[idx]);
            }

            // 홀수 날(1만큼 성장)과 짝수 날(2만큼 성장) 계산
            int oneDays = 0;
            int twoDays = 0;
            for (int idx = 0; idx < N; idx++) {
                int gap = maxHeight - tree[idx];
                twoDays += gap / 2; // 2로 나눈 몫: 짝수 날 횟수
                oneDays += gap % 2; // 2로 나눈 나머지: 홀수 날 횟수
            }

            // 최소 날짜 계산
            int totalDays = 0;
            if (oneDays == 0 && twoDays == 0) {
                totalDays = 0; // 모든 나무가 이미 최대 높이
            } else {
                // 짝수 날이 홀수 날보다 많을 경우 조정
            	// twoDays 와 oneDays + 1 과 비교해야 하는데
            	// 그 이유는 짝수 개 날짜 하나가 더 있는 상황도 최적이기 떄문
            	// 홀이 9일까지 짝이 12일까지면 각각 5일, 6일이지만 최적
            	// twoDays > oneDays 하면
            	// 해당 상황에서도 변환해서 7일 5일이 됨
                while (twoDays > oneDays + 1) {
                    twoDays--;
                    oneDays += 2; // 짝수 날 1회를 홀수 날 2회로 변환
                }
                
                // while 에서 최적으로 배치해놨고
                // 결과는 단순히 배치된 oneDays와 twoDays를 비교해야 한다
    			if(oneDays > twoDays) { 
    				totalDays = oneDays * 2 - 1;
    				
    			} else if(twoDays > oneDays) {
    				totalDays = twoDays * 2;
    				
    			} else {
    				totalDays = oneDays + twoDays;
    			}
            }

            sb.append(totalDays).append("\n");
            bw.write(sb.toString());
        }
        bw.flush();
        bw.close();
    }
}