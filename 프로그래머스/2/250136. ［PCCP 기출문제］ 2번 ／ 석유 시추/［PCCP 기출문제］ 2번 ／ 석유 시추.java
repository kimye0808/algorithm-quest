import java.io.*;
import java.util.*;

/*
열마다 탐색

1. for col 열 탐색
방문 처리 초기화
2. for row 
2-1. bfs 로 덩어리 탐색
2-2. 석유 칸 합 카운팅
2-3. 최댓값 갱신
이렇게 하면 500 * 500 * (bfs) 하면 시간 초과

미리 bfs로 덩어리 계산,
이때 방문한 좌표들을 리스트에 넣음
좌표 리스트 순회하면서 해당 덩어리 번호 매기기

List sizeOil : idx 별 오일 사이즈
int[][] oilBoard : 좌표별 기름 해당 idx

0. 미리 bfs 돌려서 sizeOil과 oilBoard 채우기
1. 열 순회하기
2. 행 순회하기
3. 좌표에 해당하는 오일 덩어리 크기 더하기 (이때 중복은 제외)
*/
class Solution {
    static int[][] Land;
    static List<Integer> sizeOil;
    static int[][] oilBoard;
    static int N, M;
    
    static int[] DX = {1, 0, -1, 0};
    static int[] DY = {0, 1, 0, -1};
    
    static boolean outOfBorder(int x, int y){
        return x < 0 || y < 0 || x >= N || y >= M;
    }
    
    static int bfs(int sx, int sy, boolean[][] vis, int oIdx){
        List<int[]> oils = new ArrayList<>(); // 오일 좌표 리스트
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{sx, sy});
        vis[sx][sy] = true;
        
        int size = 0;
        
        while(!q.isEmpty()){
            int[] now = q.poll();
            int x = now[0];
            int y = now[1];
            
            oils.add(new int[]{x,y});
            size++;
            
            for(int dir=0; dir<4; dir++){
                int nx = x + DX[dir];
                int ny = y + DY[dir];
                
                if(outOfBorder(nx, ny)) continue;
                if(vis[nx][ny]) continue;
                if(Land[nx][ny] == 0) continue;
                
                vis[nx][ny] = true;
                q.offer(new int[]{nx, ny});
            }
        }
        
        // 추가된 오일 좌표들을 오일 보드판에 갱신
        for(int[] oilPos : oils){
            oilBoard[oilPos[0]][oilPos[1]] = oIdx;
        }
        
        // 오일 덩어리 사이즈 반환
        return size;
    }
    
    static void preprocess(){
        boolean[][] vis = new boolean[N][M];
        
        int oilIdx = 0; // 오일 덩어리 인덱스
        
        for(int row=0; row < N; row++){
            for(int col=0; col<M; col++){
                if(!vis[row][col] && Land[row][col] == 1){
                    int localOilSize = bfs(row, col, vis, oilIdx);

                    sizeOil.add(localOilSize);
                    oilIdx++;
                }        
            }
        }
    }
    
    static int calcTotalOil(){
        int maxOilSize = 0;
        
        for(int col=0; col<M; col++){
            int localSum = 0;
            Set<Integer> used = new HashSet<>();
            
            for(int row=0; row<N; row++){
                if(Land[row][col] == 0) continue; // 벽이면 무시하고 넘어감
                int oilIndex = oilBoard[row][col];
                if(!used.contains(oilIndex)){
                    localSum += sizeOil.get(oilIndex);
                    used.add(oilIndex);
                }
            }
            
            if(maxOilSize < localSum){
                maxOilSize = localSum;
            }
        }
        
        return maxOilSize;
    }
    
    public int solution(int[][] land) {
        Land = land;
        N = land.length;
        M = land[0].length;
        
        sizeOil = new ArrayList<>();
        oilBoard = new int[N][M];
        
        // bfs해서 미리 오일 덩어리들 계산
        preprocess();
        
        int answer = 0;
        answer = calcTotalOil();
    
        return answer;      
    }
}