import java.io.*;
import java.util.*;

/*
공부용 Cluade.ai 의 풀이
[내 풀이와의 차이점]
0. 애초에 소멸되는 파이어볼의 제거를 고려하지 않음
보통 flag 처리하거나 제거하거나 (나는 객체 flag + 보드판 제거 둘 다 했고 실수함)
그러는데 문제 조건을 잘 생각해보면 애초에 그럴 필요가 없음
0-1. 파이어볼의 이동에서 파이어볼의 보드판은 필요하지 않고 결과만 갱신해주면 됨.
0-2. 중첩 파이어볼 병합+분할에서 어차피 기존 파이어볼은 다 제거하는데 그냥 새로 파이어볼 리스트
     만들어 버리면 그걸 신경 쓸 필요가 없음

1. 나는 map을 Set으로 처리했는데 얘는 List로 처리함->시간 훨씬 빠름
-> move하는 중일때는 map과 무관하기 때문
map을 제거를 고려하지 않아도 되며
파이어볼이 소멸되었는지 유무를 굳이 체크할 필요가 없음
map에서 굳이 제거 삭제 시간을 들일 필요가 없다는 것
그냥 move 전에 map을 아예 새로 초기화해버림
어떻게든 move한 결과만 map에 기록하면 파이어볼 중첩 병합 + 분할 과정에서 사용할 수 있다는 것

2. 파이어볼 리스트를 병합+분할 처리 과정에서 새로 만든 리스트로 대체함
파이어볼의 소멸 유무를 고려할 필요가 없음 그냥 새로 만든 리스트에 안넣으면 됨
문제 조건에서 병합 + 분할하면 기존에 있던 파이어볼들은 싹 제거되는거니까 
애초에 제거하는 수고를 덜지 않고 그냥 새로 리스트를 만들어 버림

3. 좌표 갱신
            int nr = fireball.r + dr[fireball.d] * (fireball.s % N);
            int nc = fireball.c + dc[fireball.d] * (fireball.s % N);
            
            // 격자 범위를 벗어나면 연결된 반대편으로 이동
            nr = ((nr - 1 + N * 1000) % N) + 1;
            nc = ((nc - 1 + N * 1000) % N) + 1;
처음에는 그냥 이동시키고
격자 범위를 벗어나는 것을 나중에 고려해서 갱신해 버림
그리고 애매하게 N보다 크게 움직이는 것을 고려해서 그냥 쿨하게 1000*N만큼 박아버림
마찬가지로 1-based를 0-based로 만들고 처리하고 1-based로 만듦
이러면 또 굳이 다른 함수를 만들 필요도 없긴 함

기타. r, c의 방향을 dr, dc로 유사하게 통일
*/
public class Main {
    static int N, M, K;
    static List<Fireball>[][] map;
    static List<Fireball> fireballs = new ArrayList<>();
    
    // 방향 벡터 (상, 우상, 우, 우하, 하, 좌하, 좌, 좌상)
    static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dc = {0, 1, 1, 1, 0, -1, -1, -1};
    
    static class Fireball {
        int r, c, m, s, d;
        
        public Fireball(int r, int c, int m, int s, int d) {
            this.r = r;
            this.c = c;
            this.m = m; // 질량
            this.s = s; // 속력
            this.d = d; // 방향
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken()); // 격자 크기
        M = Integer.parseInt(st.nextToken()); // 파이어볼 수
        K = Integer.parseInt(st.nextToken()); // 이동 명령 횟수
        
        // 맵 초기화
        map = new ArrayList[N + 1][N + 1];
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                map[i][j] = new ArrayList<>();
            }
        }
        
        // 파이어볼 정보 입력
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            
            fireballs.add(new Fireball(r, c, m, s, d));
        }
        
        // K번 이동 시뮬레이션
        for (int k = 0; k < K; k++) {
            // 1. 모든 파이어볼 이동
            moveFireballs();
            
            // 2. 파이어볼 합치기 및 분리
            mergeAndDivideFireballs();
        }
        
        // 남아있는 파이어볼 질량의 합 계산
        int answer = 0;
        for (Fireball fireball : fireballs) {
            answer += fireball.m;
        }
        
        System.out.println(answer);
    }
    
    // 파이어볼 이동 함수
    static void moveFireballs() {
        // 맵 초기화
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                map[i][j].clear();
            }
        }
        
        // 각 파이어볼 이동
        for (Fireball fireball : fireballs) {
            int nr = fireball.r + dr[fireball.d] * (fireball.s % N);
            int nc = fireball.c + dc[fireball.d] * (fireball.s % N);
            
            // 격자 범위를 벗어나면 연결된 반대편으로 이동
            nr = ((nr - 1 + N * 1000) % N) + 1;
            nc = ((nc - 1 + N * 1000) % N) + 1;
            
            fireball.r = nr;
            fireball.c = nc;
            
            // 이동 후 위치에 파이어볼 추가
            map[nr][nc].add(fireball);
        }
    }
    
    // 파이어볼 합치기 및 분리 함수
    static void mergeAndDivideFireballs() {
        List<Fireball> newFireballs = new ArrayList<>();
        
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                // 해당 칸에 파이어볼이 2개 이상 있는 경우
                if (map[i][j].size() >= 2) {
                    int sumM = 0; // 질량 합
                    int sumS = 0; // 속력 합
                    boolean allEven = true; // 모든 방향이 짝수인지
                    boolean allOdd = true; // 모든 방향이 홀수인지
                    
                    // 같은 칸에 있는 파이어볼 정보 합치기
                    for (Fireball fb : map[i][j]) {
                        sumM += fb.m;
                        sumS += fb.s;
                        
                        if (fb.d % 2 == 0) {
                            allOdd = false;
                        } else {
                            allEven = false;
                        }
                    }
                    
                    int newM = sumM / 5; // 새 질량
                    int newS = sumS / map[i][j].size(); // 새 속력
                    
                    // 질량이 0인 경우 소멸
                    if (newM == 0) continue;
                    
                    // 방향 결정
                    int[] newDirs;
                    if (allEven || allOdd) {
                        newDirs = new int[]{0, 2, 4, 6}; // 모두 짝수거나 모두 홀수면 0, 2, 4, 6
                    } else {
                        newDirs = new int[]{1, 3, 5, 7}; // 그렇지 않으면 1, 3, 5, 7
                    }
                    
                    // 4개의 새로운 파이어볼 생성
                    for (int dir : newDirs) {
                        newFireballs.add(new Fireball(i, j, newM, newS, dir));
                    }
                } 
                // 해당 칸에 파이어볼이 1개만 있는 경우
                else if (map[i][j].size() == 1) {
                    newFireballs.add(map[i][j].get(0));
                }
            }
        }
        
        // 새로운 파이어볼 목록으로 갱신
        fireballs = newFireballs;
    }
}