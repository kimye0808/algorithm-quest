import java.io.*;
import java.util.*;

/*
 * 참고용 Claude.ai 의 풀이
 * 내 풀이와의 다른점
 * 1. Smell 객체의 2차원 배열로 상태 체크
 * -> 보드판을 2개 만들 필요 없이 객체 배열 하나 만으로도 상태 체크 가능
 * -> 보드판 2개 관리하다가 발생하는 실수를 줄일 수 있다
 * 
 * 2. 중복 충돌 체크를 상어객체(좌표) + 보드판(id)로 체크
 * -> 동시에 이동하는 상어들이 충돌할 경우 중복 좌표가 발생
 * -> 상어를 객체로 관리하는 것은 동일 
 * -> 보드판을 사용한다는 것은 비슷하나, 나는 전체 턴을 고려하면서
 * -> 현재 턴을 카운팅하면서 이전에 있던 좌표의 카운팅까지 갱신하는
 * -> 실수하기 쉬운 방법이라면, 얘는 그냥 해당 턴의 중복만 고려하면 되므로
 * -> 해당 턴의 보드판만 만들고 보드판에 우선순위 고려할때 필요한 아이디를 기록,
 * -> 좌표에 아이디 있는지 체크해서 갱신하는 방식 채택
 * 
 * 3. nxtDirection 뿐만 아니라 nxtPosition
 * -> 다음 좌표 이동 + 다음 방향 까지 묶어서 배열로 리턴하는 함수를 만들어서
 * -> 코드를 조금 더 깔끔하게, 실수를 덜하게 만듦
 * 
 * 4. priority direction의 방향 인덱스도 통일
 * -> 나는 1-based, 1-based, 0-based를 사용
 * -> 얘는 1-based로 통일해서 실수를 줄일 수 있음
 */
public class Main {
    static int N, M, K;
    static int[] dx = {0, -1, 1, 0, 0}; // 상하좌우 (1-indexed)
    static int[] dy = {0, 0, 0, -1, 1};
    static int[][][] priorityDirections; // [상어번호][현재방향][우선순위]
    static Shark[] sharks;
    static Smell[][] smellMap;
    
    static class Shark {
        int id, x, y, direction;
        boolean isAlive;
        
        public Shark(int id, int x, int y, int direction) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.isAlive = true;
        }
    }
    
    static class Smell {
        int sharkId;
        int remainingTime;
        
        public Smell(int sharkId, int remainingTime) {
            this.sharkId = sharkId;
            this.remainingTime = remainingTime;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()); // 격자 크기
        M = Integer.parseInt(st.nextToken()); // 상어 수
        K = Integer.parseInt(st.nextToken()); // 냄새 지속 시간
        
        // 초기화
        initializeGame(br);
        
        // 시뮬레이션 실행
        int result = runSimulation();
        System.out.println(result);
    }
    
    static void initializeGame(BufferedReader br) throws IOException {
        sharks = new Shark[M + 1]; // 1번부터 M번까지의 상어
        smellMap = new Smell[N][N]; // 냄새 정보를 저장할 배열
        
        // 격자 정보 입력
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int sharkId = Integer.parseInt(st.nextToken());
                if (sharkId > 0) {
                    sharks[sharkId] = new Shark(sharkId, i, j, 0); // 방향은 아직 모름
                }
                smellMap[i][j] = new Smell(0, 0); // 초기 냄새 없음
            }
        }
        
        // 상어 초기 방향 입력
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= M; i++) {
            if (sharks[i] != null) {
                sharks[i].direction = Integer.parseInt(st.nextToken());
            }
        }
        
        // 각 상어의 방향 우선순위 입력
        priorityDirections = new int[M + 1][5][5]; // [상어번호][현재방향][우선순위]
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= 4; j++) { // 현재 방향(상하좌우)
                st = new StringTokenizer(br.readLine());
                for (int k = 1; k <= 4; k++) { // 우선순위
                    priorityDirections[i][j][k] = Integer.parseInt(st.nextToken());
                }
            }
        }
        
        // 초기 냄새 뿌리기
        for (int i = 1; i <= M; i++) {
            if (sharks[i] != null && sharks[i].isAlive) {
                int x = sharks[i].x;
                int y = sharks[i].y;
                smellMap[x][y] = new Smell(i, K);
            }
        }
    }
    
    static int runSimulation() {
        int time = 0;
        
        while (time <= 1000) {
            // 1번 상어만 남았는지 확인
            if (isOnlyShark1Left()) {
                return time;
            }
            
            // 시간 증가
            time++;
            
            // 상어 이동
            moveAllSharks();
            
            // 냄새 감소 및 새 냄새 뿌리기
            updateSmells();
        }
        
        return -1; // 1000초가 지나도 1번 상어만 남지 않음
    }
    
    static boolean isOnlyShark1Left() {
        for (int i = 2; i <= M; i++) {
            if (sharks[i] != null && sharks[i].isAlive) {
                return false;
            }
        }
        return true;
    }
    
    static void moveAllSharks() {
        int[][] nextPositions = new int[N][N]; // 다음 위치에 있는 상어의 번호
        Arrays.stream(nextPositions).forEach(row -> Arrays.fill(row, 0));
        
        for (int i = 1; i <= M; i++) {
            Shark shark = sharks[i];
            if (shark == null || !shark.isAlive) continue;
            
            // 다음 위치와 방향 결정
            int[] nextMove = findNextPosition(shark);
            int nx = nextMove[0];
            int ny = nextMove[1];
            int nd = nextMove[2];
            
            // 다음 위치에 상어 배치 (충돌 처리)
            if (nextPositions[nx][ny] == 0) {
                nextPositions[nx][ny] = i;
                shark.x = nx;
                shark.y = ny;
                shark.direction = nd;
            } else {
                // 더 작은 번호의 상어가 이미 있으면 현재 상어는 죽음
                if (nextPositions[nx][ny] < i) {
                    shark.isAlive = false;
                } else {
                    sharks[nextPositions[nx][ny]].isAlive = false;
                    nextPositions[nx][ny] = i;
                    shark.x = nx;
                    shark.y = ny;
                    shark.direction = nd;
                }
            }
        }
    }
    
    static int[] findNextPosition(Shark shark) {
        // 빈 칸 찾기
        for (int p = 1; p <= 4; p++) {
            int priority = priorityDirections[shark.id][shark.direction][p];
            int nx = shark.x + dx[priority];
            int ny = shark.y + dy[priority];
            
            if (isValid(nx, ny) && smellMap[nx][ny].remainingTime == 0) {
                return new int[]{nx, ny, priority};
            }
        }
        
        // 자신의 냄새가 있는 칸 찾기
        for (int p = 1; p <= 4; p++) {
            int priority = priorityDirections[shark.id][shark.direction][p];
            int nx = shark.x + dx[priority];
            int ny = shark.y + dy[priority];
            
            if (isValid(nx, ny) && smellMap[nx][ny].sharkId == shark.id) {
                return new int[]{nx, ny, priority};
            }
        }
        
        // 이 문제에서는 이동할 수 없는 경우가 없음
        return new int[]{-1, -1, -1};
    }
    
    static boolean isValid(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }
    
    static void updateSmells() {
        // 냄새 감소
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (smellMap[i][j].remainingTime > 0) {
                    smellMap[i][j].remainingTime--;
                    if (smellMap[i][j].remainingTime == 0) {
                        smellMap[i][j].sharkId = 0;
                    }
                }
            }
        }
        
        // 새로운 냄새 뿌리기
        for (int i = 1; i <= M; i++) {
            Shark shark = sharks[i];
            if (shark != null && shark.isAlive) {
                smellMap[shark.x][shark.y] = new Smell(i, K);
            }
        }
    }
}