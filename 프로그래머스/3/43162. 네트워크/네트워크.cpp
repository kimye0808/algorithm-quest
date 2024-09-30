#include <string>
#include <vector>
#include <queue>

using namespace std;

vector<int> adj[201];
bool vis[201];

void bfs(int s){
    queue<int> q;
    q.push(s);
    vis[s] = true;
    
    while(!q.empty()){
        auto tp = q.front();
        q.pop();
        
        for(auto to : adj[tp]){
            if(vis[to]) continue;
        
            vis[to] = true;
            q.push(to);
        }

    }
}

int solution(int n, vector<vector<int>> c) {
    int answer = 0;
    
    for(int i=0; i<(int)c.size(); i++){
        for(int j=0; j<(int)c[i].size();j++){
            if(c[i][j]){
                adj[i].push_back(j);            
            }
        }
    }
    
    for(int i=0; i<n;i++){
        if(!vis[i]){
            bfs(i);
            answer++;
        }
    }
    
    
    return answer;
}