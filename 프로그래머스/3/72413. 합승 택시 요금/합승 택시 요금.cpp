#include <string>
#include <vector>
#include <queue>
#include <algorithm>
using namespace std;

typedef pair<int,int> P;

struct Edge{
    int to;
    int cost;
    Edge(int t, int c):to(t), cost(c){};
};


vector<Edge> adj[1001];


vector<int> dijk(int s, int n){
    priority_queue<P, vector<P>, greater<P>> q;
    q.push({0, s});
    vector<bool> vis(n+1);
    vector<int> dist(n+1, 1e9);
    dist[s] = 0;
    
    while(!q.empty()){
        auto [d, u] = q.top();
        q.pop();
        
        if(vis[u]) continue;
        vis[u] = true;
        
        for(auto& e : adj[u]){
            int v = e.to;
            int c = e.cost;
            
            if(dist[v] > d + c){
                dist[v] = d + c;
                q.push({dist[v], v});
            }
        }
    }
    
    return dist;
}


// 다익스트라 한 번 돌려서 공통지점 찾고 
// 거기서 각각 다익스트라 돌려서 A와 B 까지 거리 구하기
int solution(int n, int s, int a, int b, vector<vector<int>> fares) {
    int answer = 0;
    
    for(int i=0; i<(int)fares.size(); i++){
        int f = fares[i][0];
        int t = fares[i][1];
        int c = fares[i][2];
        
        adj[f].push_back(Edge(t,c));
        adj[t].push_back(Edge(f,c));
    }
    
    vector<int> d = dijk(s, n);
    
    int distsum = 1e9;
    for(int i=1; i<=n; i++){
        int localsum = d[i];
        vector<int> ktoend = dijk(i, n);
        
        if(ktoend[a] == 1e9 || ktoend[b] == 1e9){
            continue;
        }
        localsum += ktoend[a] + ktoend[b];
        distsum = min(distsum, localsum);
    }
    
    answer = distsum;
    
    return answer;
}