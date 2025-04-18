import java.io.*;
import java.util.*;

/*
 * 단어 개수 100개 이하,
 * 단어 길이는 10 이하므로
 * 로우하게 해도 가능하다
 * 
 * 비슷한 단어:
 * 1. 완전 같은 단어
 * 2. 문자가 다른데 개수가 1 차이나는 단어
 * 3. 문자가 다른데 다른게 1개인 단어
 */
class Main{
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int N = Integer.parseInt(br.readLine().trim());
		
		int[] firstUsed = new int[26];
		
		String firstWord = br.readLine().trim();
		for(int idx=0; idx<firstWord.length(); idx++) {
			firstUsed[firstWord.charAt(idx)-'A']++;
		}
		
		int answer = 0;
		for(int idx=0; idx<N-1; idx++) {
			String word = br.readLine().trim();
			// 현재 단어 알파벳을 카운팅한다
			int[] nowUsed = new int[26];
			for(char c : word.toCharArray()) {
				nowUsed[c-'A']++;
			}
			
			// 현재 단어 알파벳을 체크한다
			int diffSum = 0;
			for(int jdx=0; jdx<26; jdx++) {
				diffSum += Math.abs(nowUsed[jdx] - firstUsed[jdx]);
			}
			
			// 다른 개수가 2여도 길이 차이가 1이하이면 ABB ABC 같이 비슷한 문자로 처리됨
			// 다른 개수가 1이어도 길이 차이가 1이하면 ABB AB 같이 비슷한 문자로 처리됨
			if(diffSum <= 2) {
				if(Math.abs(firstWord.length() - word.length()) <= 1) {
					answer++;
				}
			}
		}
		
		System.out.println(answer);
	}
}