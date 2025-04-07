import java.io.*;
import java.util.*;

/*
 * 총 3시간 40분
 * 너무 빡세다 정신나갈뻔
 * 좋은 방식으로 구현한 것 같지 않다
 * 그나마 다행인 것은 단게별로 체크할 수 있었던 점
 */
class Main{
	static BufferedReader br;
	static BufferedWriter bw;
	static StringTokenizer st;
	
	static int[] DR = {-1, 0, 1, 0};
	static int[] DC = {0, 1, 0, -1};
	
	static int N, K; // N은 어항의수, K는 Max-Min 차이가 이하여야 하는 타겟 기준
	static int[] floorArr;
	static int floorStartIdx;
	
	static void init() throws Exception{
		st = new StringTokenizer(br.readLine().trim());
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		floorArr = new int[N];
		floorStartIdx = 0;
		
		st = new StringTokenizer(br.readLine().trim());
		for(int idx=0; idx<N; idx++) {
			floorArr[idx] = Integer.parseInt(st.nextToken());
		}
	}
	
	static int calcMaxMinGap() {
		int maxNum=0;
		int minNum=10001;
		for(int idx=0; idx<floorArr.length; idx++) {
			if(maxNum < floorArr[idx]) {
				maxNum = floorArr[idx];
			}
			if(minNum > floorArr[idx]) {
				minNum = floorArr[idx];
			}
		}
		
		if(maxNum==0 || minNum == 10001) {
			System.out.println("max min 잘못함");
		}
		
		return maxNum - minNum; 
	}
	
	static void addFishToMin() {
		int minNum=10001;
		for(int idx=0; idx<floorArr.length; idx++) {
			if(minNum > floorArr[idx]) {
				minNum = floorArr[idx];
			}
		}
		
		List<Integer> minList = new ArrayList<>();
		for(int idx=0; idx<floorArr.length; idx++) {
			if(minNum == floorArr[idx]) {
				minList.add(idx);
			}
		}
		
		for(int idx : minList) {
			floorArr[idx]++;
		}
		
	}
	
	static void printGroup(int[][] group) {
		for(int r=0; r<group.length;r++) {
			for(int c=0; c<group[r].length; c++) {
				System.out.print(group[r][c] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	static int[][] organizeFirst(int[][] group, int startRow) {
		// 그룹의 가로길이 만큼 증가
		int END = floorStartIdx + group[0].length;
		// 길이 초과시 실패 리턴
		int NextIdx = END + group.length;
		if(NextIdx > floorArr.length) return null;
		
		for(int idx = floorStartIdx, jdx = 0; idx < END; idx++, jdx++) {
			group[startRow][jdx] = floorArr[idx];
		}
		floorStartIdx = END;
		
		// 성공
		return rotate90(group);
	}
	
	
	static int[][] rotate90(int[][] group) {
		// M*N을 N*M으로 만든다
		int M = group.length;
		int N = group[0].length;
		
		// N * M
		int[][] tempGroup = new int[N][M];
		
		for(int r=0; r<M; r++) {
			for(int c=0; c<N; c++) {
				tempGroup[c][M-1-r] = group[r][c];
			}
		}
		
		// 여기서 새로 재할당하면 인자를 넘겨준 호출한 함수에서의 group은 변하지 않음을 주의
		group = new int[N][M]; 
		
		for(int row=0; row<N; row++) {
			for(int col=0; col<M; col++) {
				group[row][col] = tempGroup[row][col];
			}
		}
	
		return group;
	}
	
	static int[][] mergeGroup(int[][] group){
		int width = floorArr.length - floorStartIdx;
		int[][] nextGroup = new int[group.length+1][width];
		
		for(int r=0; r<group.length; r++) {
			for(int c=0; c<width; c++) {
				if(c < group[r].length) {
					nextGroup[r][c] = group[r][c];
				}else {
					nextGroup[r][c] = 0;
				}
			}
		}
		for(int col=floorStartIdx; col<N; col++) {
			nextGroup[group.length][col - floorStartIdx] = floorArr[col];
		}
		return nextGroup;
	}
	
	static int[][] controlFishCountOne(int[][] group) {
		group = mergeGroup(group);
		
		int R = group.length;
		int C = group[0].length;
		int[][] scoreLogs = new int[R][C];
		
		for(int row=0; row < R; row++) {
			for(int col=0; col < C; col++) {
				if(group[row][col] == 0) continue;
				
				for(int dir = 0; dir<4; dir++) {
					int nr = row + DR[dir];
					int nc = col + DC[dir];
					
					if(nr<0||nc<0||nr >= R || nc >= C) continue;
					if(group[nr][nc] == 0) continue;
					
					if(group[row][col] > group[nr][nc]) {
						int gap = (group[row][col] - group[nr][nc])/5;
						if(gap > 0) {
							scoreLogs[row][col] -= gap;
							scoreLogs[nr][nc] += gap;
						}
					}
				}
			}
		}
		
		for(int row=0; row<R; row++) {
			for(int col=0; col<C; col++) {
				group[row][col] += scoreLogs[row][col];
			}
		}
		
		return group;
	}
	
	static int[][] controlFishCountTwo(int[][] group) {
		int R = group.length;
		int C = group[0].length;
		int[][] scoreLogs = new int[R][C];
		
		for(int row=0; row < R; row++) {
			for(int col=0; col < C; col++) {
				if(group[row][col] == 0) continue;
				
				for(int dir = 0; dir<4; dir++) {
					int nr = row + DR[dir];
					int nc = col + DC[dir];
					
					if(nr<0||nc<0||nr >= R || nc >= C) continue;
					if(group[nr][nc] == 0) continue;
					
					if(group[row][col] > group[nr][nc]) {
						int gap = (group[row][col] - group[nr][nc])/5;
						if(gap > 0) {
							scoreLogs[row][col] -= gap;
							scoreLogs[nr][nc] += gap;
						}
					}
				}
			}
		}
		
		for(int row=0; row<R; row++) {
			for(int col=0; col<C; col++) {
				group[row][col] += scoreLogs[row][col];
			}
		}
		
		return group;
	}
	
	/*
	 * floorArr 을 group을 핀 상태로 한다
	 */
	static void serialize(int[][] group) {
		int length = 0;
		for(int r=0; r<group.length; r++) {
			for(int c=0; c<group[r].length; c++) {
				if(group[r][c] != 0) {
					length++;
				}
			}
		}
		
		floorArr = new int[length];
		floorStartIdx = 0;
		
		int fidx=0;
		for(int c=0; c<group[0].length; c++) {
			for(int r= group.length-1; r >= 0; r--) {
				if(group[r][c] != 0) {
					floorArr[fidx++] = group[r][c];
				}
			}
		}
	}
	
	static int[][] rotate180(int[][] arr){
		int R = arr.length;
		int C = arr[0].length;
		
		int[][] tempArr = new int[R][C];
		
		for(int r=0; r<R; r++) {
			for(int c=0; c<C; c++) {
				tempArr[R-1-r][C-1-c] = arr[r][c];
			}
		}
		
		return tempArr;
	}
	
	static int[][] organizeSecond() {
		int[] baseArr = new int[floorArr.length/2];
		int[] halfArr = new int[floorArr.length/2];
		System.arraycopy(floorArr, 0, halfArr, 0, floorArr.length/2);
		System.arraycopy(floorArr, floorArr.length/2, baseArr, 0, floorArr.length/2);
		
		int[] after180halfArr = new int[floorArr.length/2];
		// 1차원 배열 180도 회전
		for(int idx = halfArr.length-1, jdx = 0; idx >=0; idx--, jdx++) {
			after180halfArr[jdx] = halfArr[idx];
		}
		
		int[][] nextDoubleArr = new int[2][halfArr.length];
		for(int col=0; col<halfArr.length; col++) {
			nextDoubleArr[0][col] = after180halfArr[col];
			nextDoubleArr[1][col] = baseArr[col];
		}
		
		// 2페이즈 : 2층을 N/2 공중부양 후 위에 붙이기
		int N = halfArr.length/2;
		int[][] halfDoubleArr = new int[2][N];
		int[][] baseDoubleArr = new int[2][N];
		for(int r=0; r< 2; r++) {
			for(int c=0; c<N; c++) {
				halfDoubleArr[r][c] = nextDoubleArr[r][c];
			}
			for(int c=N, jdx = 0; c<2*N; c++, jdx++) {
				baseDoubleArr[r][jdx] = nextDoubleArr[r][c];
			}
		}
		
		int[][] after180halfDoubleArr = rotate180(halfDoubleArr);
		
		int[][] nextArr = new int[4][after180halfDoubleArr[0].length];
		
		for(int r=0; r<2; r++) {
			for(int c=0; c<after180halfDoubleArr[r].length; c++) {
				nextArr[r][c] = after180halfDoubleArr[r][c];
			}
		}
		for(int r=2, j=0; r < 4; r++, j++) {
			for(int c=0; c<baseDoubleArr[j].length; c++) {
				nextArr[r][c] = baseDoubleArr[j][c];
			}
		}
		
		return nextArr;
	}
	
	static int simulate() {
		int turn = 0;
		while(true) {
			// calcMaxMinGap
			int gap = calcMaxMinGap();
			if(gap <= K) break;
			turn++;
			
			// min 어항에 물고기 한 마리씩 넣기, 여러 마리면 다 넣기
			addFishToMin();
			
			// organizeFirst - 1부터 증가시키면서 마는거
			int[][] group = new int[1][1];
			group[0][0] = floorArr[0];
			floorStartIdx += 1;
			int[][] nxtGroup = new int[2][1];
			nxtGroup[0][0] = group[0][0];
			
			// 실패하면 기존 group 그대로 간다
			while((nxtGroup = organizeFirst(nxtGroup, nxtGroup.length - 1)) != null) {
				group = nxtGroup;
				nxtGroup = new int[group.length+1][group[0].length];
				for(int r=0; r<group.length; r++) {
					for(int c=0; c<group[r].length; c++) {
						nxtGroup[r][c] = group[r][c];
					}
				}
			}
			
			// controlFishCount
			group = controlFishCountOne(group);
			
			// serialize
			serialize(group);
			
			// organizeSecond - N/2씩 2번 마는거
			group = organizeSecond();
			
			// 다시 물고기 조절 2
			group = controlFishCountTwo(group);
			
			// serialize
			serialize(group);
			
		}
		
		return turn;
	}
	

	public static void main(String[] args) throws Exception{
		br = new BufferedReader(new InputStreamReader(System.in));
		bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		init();
		
		int answer = simulate();
		System.out.println(answer);
	}
}