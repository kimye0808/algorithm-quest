#include<vector>
#include<queue>
using namespace std;

int dx[] = {1, 0, -1, 0};
int dy[] = {0, 1, 0, -1};
void bfs(int n, int m, vector<vector<int>>& d, vector<vector<int>>& maps){
    queue<pair<int,int>> q;
    q.push({0,0});
    d[0][0] = 0;
    
    while(!q.empty()){
        auto [x,y] = q.front();
        q.pop();
        
        if(x == n-1 && y == m-1){
            break;
        }
                
        for(int dir=0; dir<4; dir++){
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            
            if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
            if(d[nx][ny] != -1) continue;
            if(maps[nx][ny] == 0) continue;
            
            d[nx][ny] = d[x][y] + 1;
            q.push({nx, ny});
        }
    }
    
    
}

int solution(vector<vector<int>> maps)
{
    int answer = 0;
    int n = maps.size();
    int m = maps[0].size();
    
    vector<vector<int>> d(n, vector<int>(m, -1));
    
    bfs(n,m,d, maps);
    
    answer = d[n-1][m-1] == -1 ? -1 : d[n-1][m-1] + 1;
    return answer;
}