import java.io.*;
import java.util.*;

/*
 * 공부용 Claude.ai 풀이
 * 
 * [내 풀이와 차이점]:
 * #1. 그냥 핵심을 짚었다, 구슬을 일차원으로 관리했다
 * 애초에 구슬들은 전체 함수에 의하면
 * 보드판에서 관리될 필요가 없었다 그냥 일차원 구슬 리스트로 관리하면 됐다
 * 
 * 그니까 
 * 블리자드 마법 -> (빈칸 채우기 -> 폭발) 반복 + 구슬 변화 -> 구슬 판 갱신
 * 이거 반복인데
 * 
 * 1. 블리자드 마법 = 보드판에서 처리
 * 2. (빈칸 채우기 -> 폭발) = 굳이 보드판? 
 * 3. (구슬 변화) 굳이 보드판?
 * 4. 구슬 판 갱신 = 보드판 갱신
 * 사실상 2번 3번 과정에서 굳이 보드판에서 처리하기 보다는
 * 1번에서 일차원 구슬 리스트만 뽑아서 4번에서 구슬 리스트로 보드판 갱신하는 방식이
 * 훨씬 효율적이고 실수를 줄일 수 있고 구현이 편하다
 * 
 * => 보드판 에서 정보를 추출해서 차원을 줄인 리스트로 만들어서 요구하는 함수 작업을
 * 수행할 수 있는지 고민해보자
 * 
 * #2. 폭발 갱신
 * 폭발 함수를 투 포인터로 구현했다
 * 사실 나도 투 포인터 고민했었는데 그냥 무난하게 리스트로도 될 것 같아서 했는데
 * 투 포인터 풀이가 오히려 실수를 덜했을 것 같다
 * 
 */
public class Main {
    static int N, M;
    static int[][] board;
    static int sharkRow, sharkCol;
    static int[] dr = {0, -1, 1, 0, 0}; // 0, 상, 하, 좌, 우
    static int[] dc = {0, 0, 0, -1, 1};
    static int[] marbleCount = new int[4]; // 폭발한 구슬의 개수를 저장 (1, 2, 3번 구슬)
    static List<Integer> marbleList = new ArrayList<>(); // 구슬을 일차원 리스트로 저장

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        board = new int[N + 1][N + 1];
        sharkRow = sharkCol = (N + 1) / 2;
        
        // 보드 초기화
        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        
        // 블리자드 마법 M번 수행
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            
            // 1. 블리자드 마법 수행
            blizzard(d, s);
            
            // 2. 구슬을 일차원 리스트로 변환
            convertToList();
            
            // 3. 구슬 폭발 (더 이상 폭발할 구슬이 없을 때까지)
            while (explodeMarbles()) {
                // 폭발 후 빈 칸을 채우기 위해 리스트 갱신
                removeEmptySpaces();
            }
            
            // 4. 구슬 변화
            transformMarbles();
            
            // 5. 변화된 구슬을 보드에 적용
            applyToBoard();
        }
        
        // 결과 계산: 1*폭발한 1번 구슬 + 2*폭발한 2번 구슬 + 3*폭발한 3번 구슬
        int result = marbleCount[1] + 2 * marbleCount[2] + 3 * marbleCount[3];
        System.out.println(result);
    }
    
    // 블리자드 마법으로 구슬 파괴
    static void blizzard(int d, int s) {
        int r = sharkRow;
        int c = sharkCol;
        
        for (int i = 1; i <= s; i++) {
            r += dr[d];
            c += dc[d];
            
            if (r < 1 || r > N || c < 1 || c > N) break;
            
            if (board[r][c] > 0) {
                board[r][c] = 0; // 구슬 파괴
            }
        }
    }
    
    // 보드를 일차원 리스트로 변환
    static void convertToList() {
        marbleList.clear();
        
        int[] directions = {3, 2, 4, 1}; // 좌, 하, 우, 상 순서로 탐색
        int r = sharkRow;
        int c = sharkCol;
        int dir = 0;
        int step = 1;
        int count = 0;
        int totalSteps = 0;
        
        while (totalSteps < N * N - 1) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < step; j++) {
                    r += dr[directions[dir]];
                    c += dc[directions[dir]];
                    
                    totalSteps++;
                    if (totalSteps >= N * N - 1) break;
                    
                    if (board[r][c] > 0) {
                        marbleList.add(board[r][c]);
                    }
                }
                
                if (totalSteps >= N * N - 1) break;
                dir = (dir + 1) % 4;
            }
            step++;
        }
        
        // 빈 칸 제거
        removeEmptySpaces();
    }
    
    // 빈 칸 제거
    static void removeEmptySpaces() {
        // marbleList에서 0이 아닌 원소만 남김
        marbleList.removeIf(marble -> marble == 0);
    }
    
    // 구슬 폭발
    static boolean explodeMarbles() {
        boolean exploded = false;
        List<Integer> newList = new ArrayList<>();
        
        int i = 0;
        while (i < marbleList.size()) {
            int j = i;
            int currentMarble = marbleList.get(i);
            
            // 연속된 같은 구슬 찾기
            while (j < marbleList.size() && marbleList.get(j) == currentMarble) {
                j++;
            }
            
            int count = j - i;
            
            if (count >= 4) {
                // 구슬 폭발
                exploded = true;
                marbleCount[currentMarble] += count; // 폭발한 구슬 개수 카운트
                i = j;
            } else {
                // 폭발하지 않는 경우 새 리스트에 추가
                for (int k = i; k < j; k++) {
                    newList.add(marbleList.get(k));
                }
                i = j;
            }
        }
        
        marbleList = newList;
        return exploded;
    }
    
    // 구슬 변화
    static void transformMarbles() {
        if (marbleList.isEmpty()) return;
        
        List<Integer> newList = new ArrayList<>();
        
        int i = 0;
        while (i < marbleList.size() && newList.size() < N * N - 1) {
            int j = i;
            int currentMarble = marbleList.get(i);
            
            // 연속된 같은 구슬 찾기
            while (j < marbleList.size() && marbleList.get(j) == currentMarble) {
                j++;
            }
            
            int count = j - i;
            
            // A(개수), B(구슬 번호) 추가
            newList.add(count);
            newList.add(currentMarble);
            
            i = j;
        }
        
        marbleList = newList;
        
        // 보드 크기 초과 시 자르기
        if (marbleList.size() > N * N - 1) {
            marbleList = marbleList.subList(0, N * N - 1);
        }
    }
    
    // 변환된 구슬을 보드에 적용
    static void applyToBoard() {
        // 보드 초기화
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                board[i][j] = 0;
            }
        }
        
        if (marbleList.isEmpty()) return;
        
        int[] directions = {3, 2, 4, 1}; // 좌, 하, 우, 상 순서로 탐색
        int r = sharkRow;
        int c = sharkCol;
        int dir = 0;
        int step = 1;
        int count = 0;
        int totalSteps = 0;
        int idx = 0;
        
        while (totalSteps < N * N - 1 && idx < marbleList.size()) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < step; j++) {
                    r += dr[directions[dir]];
                    c += dc[directions[dir]];
                    
                    totalSteps++;
                    if (totalSteps >= N * N - 1 || idx >= marbleList.size()) break;
                    
                    board[r][c] = marbleList.get(idx++);
                }
                
                if (totalSteps >= N * N - 1 || idx >= marbleList.size()) break;
                dir = (dir + 1) % 4;
            }
            step++;
        }
    }
}