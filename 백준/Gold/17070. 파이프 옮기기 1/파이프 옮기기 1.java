import java.io.*;
import java.util.*;

/*
 * 벽이랑 파이프 때문에 dp가 될까 싶었는데 생각해보니 가능할듯
 * [상태정의]
 * f(r,c) = V
 * r행 c열까지의 보드판이 있으면 r행 c열에 파이프의 한쪽 끝이 있는 경우의 수
 * 이걸로는 부족하다 여기에 파이프 놓인 모양까지 고려해야 한다
 * 
 *  f(r, c, shape) = V
 * r행 c열까지의 보드판이 있으면 r행 c열에 파이프의 한쪽 끝이 있고 모양은 shape일때 경우의 수 V
 * 
 * [상태 전이]
 * 복잡하다
 * shape
 * 0 = 가로
 * 1 = 세로
 * 2 = 대각선
 * 
 * 0은 (r, c-1) 모양 0과 2에서 가져올 수 있다
 * 1은 (r-1, c) 모양 1과 2에서 가져올 수 있다
 * 2는 (r-1, c-1) 에서 모양 0, 1, 2 다 가능하지만 반드시 [r-1,c] [r,c-1] [r-1,c-1] 벽이 아니어야 한다 
 */
public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int N;
	static int[][] board;
	static int[][][] state;
	
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		N = Integer.parseInt(br.readLine().trim());
		board = new int[N+1][N+1];
		state = new int[N+1][N+1][3];
		
		for(int row=1; row<=N; row++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int col=1; col<=N; col++) {
				board[row][col] = Integer.parseInt(st.nextToken());
			}
		}
		
		state[1][2][0]=1;
		
		for(int row=1; row<=N; row++) {
			for(int col=1; col<=N; col++) {
				if(board[row][col] == 1) continue;
				
				int TopWall = board[row-1][col];
				int LeftWall = board[row][col-1];
				int TopLeftWall = board[row-1][col-1];
				
				// 모양 0
				if(LeftWall != 1) {
					state[row][col][0] += state[row][col-1][0] + state[row][col-1][2];
				}
				// 모양 1
				if(TopWall != 1) {
					state[row][col][1] += state[row-1][col][1] + state[row-1][col][2];
				}

				// 모양 2
				if(TopLeftWall != 1) {
					if(TopWall != 1 && LeftWall != 1 && TopLeftWall != 1) {
						state[row][col][2] += state[row-1][col-1][0] + state[row-1][col-1][1] + state[row-1][col-1][2];
					}
				}
			}
		}

		int sum=0;
		for(int shape=0; shape<3; shape++) {
			sum += state[N][N][shape];
		}
		
		bw.write(sum+"");
		bw.flush();
		bw.close();
	}
}
