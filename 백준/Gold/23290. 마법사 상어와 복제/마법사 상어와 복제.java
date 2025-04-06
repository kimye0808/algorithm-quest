import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
 * 공부용 Claude.ai 풀이
 * 
 * [내 풀이와 차이점]
 * 0. 소멸 조건이 있다면 정말로 소멸 flag를 만들지, 보드판에 갱신할지, 아니면 새로 보드판
 * -> 초기화 하는 방식으로 해결할지도 고민해보좌
 * 
 * 1. List<Fish> 리스트를 만들지 않고 List<Fish>[][] fishMap으로만 처리했다
 * -> 시간 복잡도 차이도 거의 안나고 무엇보다도 이러면 물고기 소멸 처리가 굉장히 쉽다
 * => List<객체>[][] 맵 방식을 사용할때 소멸 처리한다해도 굳이 리스트를 만들어야 하는가
 * => 고민해보는게 좋을 것 같다
 * 
 * 2. smellMap을 int[r][c] = 소멸될 시간 으로 처리했다
 * -> 이러면 객체 맵 보다도 실수할 여지도 적고 중복 좌표에 Smell 처리도 되고
 * -> 갱신 시간에 소멸 처리도 쉽다
 * => 좌표에 특정 객체가 있고 소멸시켜야 한다면 좌표 배열 = 소멸 시간 방식도 고려해보자
 * 
 * 3. 상어 재방문 처리를 깔끔하게 함
 */
public class Main {
    // 8방향: ←, ↖, ↑, ↗, →, ↘, ↓, ↙ (1부터 시작)
    static int[] dx = {0, 0, -1, -1, -1, 0, 1, 1, 1};
    static int[] dy = {0, -1, -1, 0, 1, 1, 1, 0, -1};
    
    // 상어 이동 4방향: 상, 좌, 하, 우 (사전순)
    static int[] sdx = {-1, 0, 1, 0};
    static int[] sdy = {0, -1, 0, 1};
    
    static int M, S;
    static int sharkX, sharkY;
    static List<Fish>[][] fishMap = new ArrayList[5][5]; // 물고기 위치 관리
    static int[][] smellMap = new int[5][5]; // 물고기 냄새 관리 (값: 냄새가 사라지는 시간)
    static List<Fish> fishCopy = new ArrayList<>(); // 복제될 물고기 저장
    
    // 상어 이동 관련 변수
    static int maxRemovableFish;
    static int[] bestPath;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        M = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());
        
        // 맵 초기화
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                fishMap[i][j] = new ArrayList<>();
            }
        }
        
        // 물고기 정보 입력
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int fx = Integer.parseInt(st.nextToken());
            int fy = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            fishMap[fx][fy].add(new Fish(fx, fy, d));
        }
        
        // 상어 위치 입력
        st = new StringTokenizer(br.readLine());
        sharkX = Integer.parseInt(st.nextToken());
        sharkY = Integer.parseInt(st.nextToken());
        
        // S번의 연습 수행
        for (int time = 1; time <= S; time++) {
            // 1. 복제 마법 시전 (물고기 상태 저장)
            castCopyMagic();
            
            // 2. 물고기 이동
            moveFish();
            
            // 3. 상어 이동
            moveShark(time);
            
            // 4. 냄새 제거 (두 번 전 연습에서 생긴 냄새)
            removeSmell(time);
            
            // 5. 복제 마법 완료
            completeCopyMagic();
        }
        
        // 격자에 있는 물고기 수 계산
        System.out.println(countFish());
    }
    
    // 1. 복제 마법 시전
    private static void castCopyMagic() {
        fishCopy.clear();
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                for (Fish fish : fishMap[i][j]) {
                    fishCopy.add(new Fish(fish.x, fish.y, fish.dir));
                }
            }
        }
    }
    
    // 2. 물고기 이동
    private static void moveFish() {
        List<Fish>[][] newFishMap = new ArrayList[5][5];
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                newFishMap[i][j] = new ArrayList<>();
            }
        }
        
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                for (Fish fish : fishMap[i][j]) {
                    boolean canMove = false;
                    int dir = fish.dir;
                    int nx = 0;  // 초기화 추가
                    int ny = 0;  // 초기화 추가
                    
                    // 이동 가능한 방향 찾기 (최대 8번)
                    for (int d = 0; d < 8; d++) {
                        nx = fish.x + dx[dir];
                        ny = fish.y + dy[dir];
                        
                        // 이동 가능 조건: 격자 범위 내, 상어 없음, 냄새 없음
                        if (nx >= 1 && nx <= 4 && ny >= 1 && ny <= 4 &&
                            !(nx == sharkX && ny == sharkY) && smellMap[nx][ny] == 0) {
                            canMove = true;
                            break;
                        }
                        
                        // 반시계 45도 회전
                        dir = (dir == 1) ? 8 : dir - 1;
                    }
                    
                    // 이동 가능하면 이동, 불가능하면 제자리
                    if (canMove) {
                        newFishMap[nx][ny].add(new Fish(nx, ny, dir));
                    } else {
                        newFishMap[fish.x][fish.y].add(new Fish(fish.x, fish.y, fish.dir));
                    }
                }
            }
        }
        
        // 이동 후 맵 업데이트
        fishMap = newFishMap;
    }
    
    // 3. 상어 이동
    private static void moveShark(int time) {
        maxRemovableFish = -1;
        bestPath = new int[3];
        
        // DFS로 상어의 최적 이동 경로 찾기
        findSharkPath(0, 0, new int[3], new boolean[5][5]);
        
        // 선택된 경로로 상어 이동
        for (int d : bestPath) {
            sharkX += sdx[d];
            sharkY += sdy[d];
            
            // 물고기 제거 및 냄새 남기기
            if (!fishMap[sharkX][sharkY].isEmpty()) {
                fishMap[sharkX][sharkY].clear();
                smellMap[sharkX][sharkY] = time + 2; // 두 번 후에 냄새 사라짐
            }
        }
    }
    
    // 상어 이동 경로 탐색 (DFS)
    private static void findSharkPath(int depth, int fishCount, int[] path, boolean[][] visited) {
        if (depth == 3) {
            // 최적 경로 업데이트
            if (fishCount > maxRemovableFish) {
                maxRemovableFish = fishCount;
                System.arraycopy(path, 0, bestPath, 0, 3);
            } else if (fishCount == maxRemovableFish) {
                // 사전순 비교
                int pathNum = path[0] * 100 + path[1] * 10 + path[2];
                int resultNum = bestPath[0] * 100 + bestPath[1] * 10 + bestPath[2];
                if (pathNum < resultNum) {
                    System.arraycopy(path, 0, bestPath, 0, 3);
                }
            }
            return;
        }
        
        int x = sharkX;
        int y = sharkY;
        
        // 현재까지의 경로를 따라 위치 계산
        for (int i = 0; i < depth; i++) {
            x += sdx[path[i]];
            y += sdy[path[i]];
        }
        
        // 4방향 탐색
        for (int d = 0; d < 4; d++) {
            int nx = x + sdx[d];
            int ny = y + sdy[d];
            
            // 격자 범위 체크
            if (nx < 1 || nx > 4 || ny < 1 || ny > 4) continue;
            
            path[depth] = d;
            
            int addedFish = 0;
            // 아직 방문하지 않았고 물고기가의 있는 칸이면 물고기 수 계산
            if (!visited[nx][ny] && !fishMap[nx][ny].isEmpty()) {
                addedFish = fishMap[nx][ny].size();
                visited[nx][ny] = true;
                findSharkPath(depth + 1, fishCount + addedFish, path, visited);
                visited[nx][ny] = false; // 백트래킹
            } else {
                findSharkPath(depth + 1, fishCount, path, visited);
            }
        }
    }
    
    // 4. 냄새 제거
    private static void removeSmell(int time) {
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                if (smellMap[i][j] == time) {
                    smellMap[i][j] = 0;
                }
            }
        }
    }
    
    // 5. 복제 마법 완료
    private static void completeCopyMagic() {
        for (Fish fish : fishCopy) {
            fishMap[fish.x][fish.y].add(new Fish(fish.x, fish.y, fish.dir));
        }
    }
    
    // 격자에 있는 물고기 수 계산
    private static int countFish() {
        int count = 0;
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) {
                count += fishMap[i][j].size();
            }
        }
        return count;
    }
    
    // 물고기 클래스
    static class Fish {
        int x, y, dir;
        
        public Fish(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }
}