import java.io.*;
import java.util.*;

/*
 * 공부용 Claude.ai 풀이
 * [내 풀이와의 차이점]
 * 1. 방향 통합
 * -> 에어컨 방향 vs 벽 방향 을 통합시켰다
 * -> 어떻게 했냐면 애초에 에어컨 방향은 blowWind만 하고 직접적인 변화가 있지 않다
 * -> 방향 상수를 정의: UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
 * -> 따로 방향별 좌표 변화량 배열:  DR = {-1, 0, 1, 0};  // 상, 우, 하, 좌
 * -> 입력 에어컨 방향과 내부 방향 매핑 : int[] AIRCON_DIR_MAP = {RIGHT, LEFT, UP, DOWN};
 * 2. 방향 처리 + 벽 처리
 * -> 미리 세 방향 이동 좌표를 기준 방향으로 표현
 * -> 방향 처리도 통일 시켰기 때문에 쉽게 할 수 있고
 * -> 특히 벽도 그냥 좌표로 처리해서 더 직관적으로 바꾸고 방향 별 일반화도 가능
 * 자세한건 뒤에 내용 추가, 입력 방향 내부 방향 일치 테크닉은 유익한듯

Claude의 해설
1. **기본 방향 상수 정의**:
   ```java
   static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
   ```
   이 방식은 시계 방향으로 순서대로 번호를 매기기 때문에 방향 계산이 직관적입니다.

2. **각 방향별 좌표 변화량 배열**:
   ```java
   static final int[] DR = {-1, 0, 1, 0};  // 상, 우, 하, 좌
   static final int[] DC = {0, 1, 0, -1};  // 상, 우, 하, 좌
   ```
   이렇게 하면 방향 번호로 좌표 변화를 쉽게 계산할 수 있습니다.

3. **입력 방향과 내부 방향 매핑**:
   ```java
   // 에어컨 입력값(1,2,3,4)을 내부 방향(RIGHT,LEFT,UP,DOWN)으로 변환
   static final int[] AIRCON_DIR_MAP = {RIGHT, LEFT, UP, DOWN};
   ```
   이 배열을 사용하면 `airconDir = AIRCON_DIR_MAP[inputValue - 1]`로 쉽게 변환할 수 있습니다.

4. **반대 방향 계산**:
   방향의 반대 방향은 `(dir + 2) % 4`로 계산할 수 있습니다:
   - 상(0)의 반대는 하(2)
   - 우(1)의 반대는 좌(3)
   - 하(2)의 반대는 상(0)
   - 좌(3)의 반대는 우(1)

5. **인접 방향 계산**:
   - 왼쪽으로 90도: `(dir + 3) % 4`
   - 오른쪽으로 90도: `(dir + 1) % 4`

6. **대각선 방향 표현**:
   복잡한 대각선 이동을 표현하기 위해 다음과 같은 배열을 사용할 수 있습니다:
   ```java
   static final int[][][] DIAGONALS = {
       {{-1, -1}, {-1, 0}, {-1, 1}},  // 상 방향일 때: 좌상, 상, 우상
       {{-1, 1}, {0, 1}, {1, 1}},     // 우 방향일 때: 우상, 우, 우하
       {{1, 1}, {1, 0}, {1, -1}},     // 하 방향일 때: 우하, 하, 좌하
       {{1, -1}, {0, -1}, {-1, -1}}   // 좌 방향일 때: 좌하, 좌, 좌상
   };
   ```
   이 배열을 사용하면 에어컨 방향에 따른 바람의 확산 방향을 쉽게 계산할 수 있습니다.

7. **벽 확인 로직 통합**:
   ```java
   boolean[][][] walls = new boolean[R+1][C+1][4];
   ```
   각 칸마다 4방향에 벽이 있는지를 직관적으로 저장합니다.

방향 통합의 실질적인 효과:

1. **코드 중복 제거**: 방향별로 별도의 함수(blowLeft, blowRight 등)가 필요 없어집니다.

2. **버그 감소**: 방향 처리를 일관되게 하므로 실수할 가능성이 줄어듭니다.

3. **확장성 향상**: 새로운 방향 관련 기능이 필요하면 쉽게 추가할 수 있습니다.

구체적인 적용 예시를 보면:

```java
// 예: 특정 방향으로 이동
int newR = r + DR[direction];
int newC = c + DC[direction];

// 예: 반대 방향 계산
int oppositeDir = (direction + 2) % 4;

// 예: 대각선 확산 처리 (방향에 따른 3가지 확산 방향)
for (int i = 0; i < 3; i++) {
    int newR = r + DIAGONALS[direction][i][0];
    int newC = c + DIAGONALS[direction][i][1];
    // 이후 처리...
}
```
 */
class Main {
    static BufferedReader br;
    static BufferedWriter bw;
    static StringTokenizer st;

    // 방향 통합 (상, 우, 하, 좌)
    static final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    static final int[] DR = {-1, 0, 1, 0};
    static final int[] DC = {0, 1, 0, -1};
    
    // 에어컨 방향 매핑 (1,2,3,4 입력 → 실제 방향)
    static final int[] AIRCON_DIR_MAP = {RIGHT, LEFT, UP, DOWN};

    // 대각선 방향 표현 (각 방향별 왼쪽 대각선, 직진, 오른쪽 대각선)
    static final int[][][] DIAGONALS = {
        {{-1, -1}, {-1, 0}, {-1, 1}},  // 상
        {{-1, 1}, {0, 1}, {1, 1}},     // 우
        {{1, 1}, {1, 0}, {1, -1}},     // 하
        {{1, -1}, {0, -1}, {-1, -1}}   // 좌
    };

    static int R, C, K;
    static int M; // 벽 개수
    static boolean[][][] walls; // [r][c][dir] = r,c에서 dir 방향에 벽이 있는지 여부
    static int[][] temperature; // 온도 맵
    static List<int[]> companies; // 회사 위치 목록
    static List<int[]> aircons; // 에어컨 위치와 방향 목록

    static void init() throws Exception {
        st = new StringTokenizer(br.readLine().trim());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        temperature = new int[R + 1][C + 1];
        walls = new boolean[R + 1][C + 1][4];
        companies = new ArrayList<>();
        aircons = new ArrayList<>();

        for (int r = 1; r <= R; r++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int c = 1; c <= C; c++) {
                int value = Integer.parseInt(st.nextToken());
                if (value == 5) {
                    companies.add(new int[]{r, c});
                } else if (value > 0 && value < 5) {
                    int dir = AIRCON_DIR_MAP[value - 1]; // 방향 매핑
                    aircons.add(new int[]{r, c, dir});
                }
            }
        }

        M = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int type = Integer.parseInt(st.nextToken());

            if (type == 0) { // 위쪽 벽
                walls[r][c][UP] = true;
                if (r - 1 >= 1) {
                    walls[r - 1][c][DOWN] = true;
                }
            } else { // 오른쪽 벽
                walls[r][c][RIGHT] = true;
                if (c + 1 <= C) {
                    walls[r][c + 1][LEFT] = true;
                }
            }
        }
    }

    static boolean isOutOfBounds(int r, int c) {
        return r < 1 || c < 1 || r > R || c > C;
    }

    static void print() {
        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                System.out.print(temperature[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        bw = new BufferedWriter(new OutputStreamWriter(System.out));

        init();
        int answer = simulate();
        
        bw.write(Integer.toString(answer));
        bw.flush();
        bw.close();
    }

    static int simulate() {
        for (int time = 0; time <= 100; time++) {
            // 1. 모든 회사 위치가 원하는 온도 이상이면 종료
            if (isComfortable()) {
                return time;
            }

            // 2. 에어컨 바람 분산
            for (int[] aircon : aircons) {
                blowWind(aircon[0], aircon[1], aircon[2]);
            }

            // 3. 온도 조절 (섞기)
            adjustTemperature();

            // 4. 가장자리 온도 감소
            decreaseEdgeTemperature();
        }
        
        return 101; // 100초 내에 조건을 만족하지 못한 경우
    }

    static boolean isComfortable() {
        for (int[] company : companies) {
            if (temperature[company[0]][company[1]] < K) {
                return false;
            }
        }
        return true;
    }

    static void decreaseEdgeTemperature() {
        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                if (r == 1 || c == 1 || r == R || c == C) {
                    if (temperature[r][c] > 0) {
                        temperature[r][c]--;
                    }
                }
            }
        }
    }

    static void adjustTemperature() {
        int[][] tempDiff = new int[R + 1][C + 1]; // 온도 변화량 저장

        // 각 칸마다 온도 차이 계산
        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                for (int dir = 0; dir < 4; dir++) {
                    int nr = r + DR[dir];
                    int nc = c + DC[dir];

                    if (isOutOfBounds(nr, nc) || walls[r][c][dir]) {
                        continue;
                    }

                    int diff = temperature[r][c] - temperature[nr][nc];
                    if (diff > 0) {
                        int amount = diff / 4;
                        tempDiff[r][c] -= amount;
                        tempDiff[nr][nc] += amount;
                    }
                }
            }
        }

        // 온도 조절 반영
        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                temperature[r][c] += tempDiff[r][c];
            }
        }
    }

    static void blowWind(int airconR, int airconC, int airconDir) {
        // 에어컨에서 바람이 나오는 시작 위치
        int startR = airconR + DR[airconDir];
        int startC = airconC + DC[airconDir];
        
        if (isOutOfBounds(startR, startC)) {
            return;
        }

        temperature[startR][startC] += 5;

        // BFS로 바람 확산
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[R + 1][C + 1];
        
        queue.offer(new int[]{startR, startC, 4}); // r, c, 남은 바람 세기
        visited[startR][startC] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];
            int strength = current[2];

            if (strength == 0) {
                continue; // 바람 세기가 0이면 더 이상 확산하지 않음
            }

            // 3개 방향으로 바람 확산 (왼쪽 대각선, 직진, 오른쪽 대각선)
            for (int i = 0; i < 3; i++) {
                int newR = r + DIAGONALS[airconDir][i][0];
                int newC = c + DIAGONALS[airconDir][i][1];

                if (isOutOfBounds(newR, newC) || visited[newR][newC]) {
                    continue;
                }

                // 벽 체크
                boolean blocked = false;
                
                // 직진이 아닌 경우(대각선) 추가 벽 체크 필요
                if (i == 0) { // 왼쪽 대각선
                    int leftDir = (airconDir + 3) % 4;
                    blocked = walls[r][c][leftDir] || walls[newR][newC][(airconDir + 2) % 4];
                } else if (i == 2) { // 오른쪽 대각선
                    int rightDir = (airconDir + 1) % 4;
                    blocked = walls[r][c][rightDir] || walls[newR][newC][(airconDir + 2) % 4];
                } else { // 직진
                    blocked = walls[r][c][airconDir] || walls[newR][newC][(airconDir + 2) % 4];
                }

                if (blocked) {
                    continue;
                }

                visited[newR][newC] = true;
                temperature[newR][newC] += strength;
                queue.offer(new int[]{newR, newC, strength - 1});
            }
        }
    }
}