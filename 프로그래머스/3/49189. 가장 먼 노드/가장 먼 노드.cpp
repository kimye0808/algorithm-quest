#include <string>
#include <vector>
#include <queue>
#include <algorithm>
#include <iostream>
using namespace std;

typedef pair<int,int> P;

struct Edge{
    int to;
    int cost;
    // Edge(int t){
    //     to=t;
    //     cost=1; 
    // }
    Edge(int t):to(t),cost(1){};
};

int solution(int n, vector<vector<int>> edge) {
    int answer = 0;
    
    vector<Edge> adj[n+1];
    
    for(int i=0; i<(int)edge.size(); i++){
        int f = edge[i][0];
        int t = edge[i][1];
        
        adj[f].push_back(Edge(t));
        adj[t].push_back(Edge(f));
    }
    
    vector<bool> vis(n+1);
    vector<int> dist(n+1, 1e9);
    
    // min heap
    priority_queue<P, vector<P>, greater<P> > q;//pair<int,int> <dist, node>
    q.push({0,1});
    dist[1] = 0;
    
    while(!q.empty()){
        auto [d, node] = q.top();
        q.pop();
        
        if(vis[node]) continue;
        
        vis[node] = true;
        
        for(Edge& e : adj[node]){
            int t = e.to;
            if(dist[t] > dist[node] + 1){
                dist[t] = dist[node]+1;
                q.push({dist[t], t});
            }
        }
    }
    
    int maxd = 0;
    for(int i=1; i<=n; i++){
        if(dist[i] != 1e9 && maxd < dist[i]){
            maxd = dist[i];
        }
    }
    
    for(int i=1; i<=n; i++){
        if(dist[i] == maxd){
            answer++;
        }
    }
    
    
    return answer;
}