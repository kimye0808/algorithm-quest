import java.io.*;
import java.util.*;

public class Main {
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;

	static List<int[]> zeros;
	static int[][] board;
	static boolean[][][] squarenums;
	static boolean[][] rownums;
	static boolean[][] colnums;

	static boolean fillSudoku(int zidx) {
		if(zidx == zeros.size()) {
			return true;
		}
		
		int r=zeros.get(zidx)[0];
		int c=zeros.get(zidx)[1];
		
		for(int num=1; num<=9; num++) {
			if(!squarenums[r/3][c/3][num] && !rownums[r][num] && !colnums[c][num]) {
				squarenums[r/3][c/3][num]=true;
				rownums[r][num]=true;
				colnums[c][num]=true;
				
				board[r][c] = num;
				if(fillSudoku(zidx+1)) {
					return true;
				}
				board[r][c] = 0;
				
				squarenums[r/3][c/3][num]=false;
				rownums[r][num]=false;
				colnums[c][num]=false;
			}
		}
		
		return false;
	}

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));

		zeros = new ArrayList<>();
		rownums = new boolean[10][10];
		colnums = new boolean[10][10];
		squarenums = new boolean[3][3][10];
		board = new int[9][9];
		
		for (int r = 0; r < 9; r++) {
			String line = br.readLine().trim();
			for(int c=0; c<9; c++) {
				board[r][c] = line.charAt(c)-'0';
				if(board[r][c] == 0) {
					zeros.add(new int[] {r,c});
				}
			}
		}

		// 전처리하기
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				rownums[r][board[r][c]] = true;
			}
		}
		for (int c = 0; c < 9; c++) {
			for (int r = 0; r < 9; r++) {
				colnums[c][board[r][c]] = true;
			}
		}
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				int pr = r / 3;
				int pc = c / 3;
				squarenums[pr][pc][board[r][c]] = true;
			}
		}

		
		fillSudoku(0);
		
		for(int r=0; r<9; r++) {
			for(int c=0; c<9; c++) {
				System.out.print(board[r][c]);
			}
			System.out.println();
		}
	}
}
