#include <bits/stdc++.h>

using namespace std;
/*
destination 에서 sources의 모든 원소와의 최단거리
= 다익스트라

간선은 양방향 간선, cost가 모두 1
*/

typedef pair<int,int> P;
const int MAX = INT_MAX;

int N;
vector<vector<int>> edges; // 노드 당 갈 수 있는 간선

vector<int> calcDist(int dest){
    vector<int> dist(N+1, MAX);
    priority_queue<P, vector<P>, greater<P>> q;
    q.push({0, dest});
    
    dist[dest] = 0;
    
    while(!q.empty()){
        P now = q.top(); q.pop();
        int nowDist = now.first;
        int nowNode = now.second;
        
        if(dist[nowNode] < nowDist){
            continue;
        }
        
        for(int to : edges[nowNode]){
            if(dist[to] > nowDist + 1){
                dist[to] = nowDist + 1;
                q.push({dist[to], to});
            }
        }
    }
    return dist;
}

vector<int> getResult(vector<int>& dist, vector<int>& sources){
    vector<int> answer;
    for(int idx=0; idx<sources.size(); idx++){
        int targetNode = sources[idx];
        if(dist[targetNode] == MAX){
            answer.push_back(-1);
        }else{
            answer.push_back(dist[targetNode]);
        }
    }
    
    return answer;
}

vector<int> solution(int n, vector<vector<int>> roads, vector<int> sources, int dest) {
    N = n;
    
    // init
    edges.assign(N+1, vector<int>());
    // 도로
    for(int idx=0; idx<roads.size(); idx++){
        int from = roads[idx][0];
        int to = roads[idx][1];
        
        edges[from].push_back(to);
        edges[to].push_back(from);
    }
    

    vector<int> dist = calcDist(dest);
    
    vector<int> answer = getResult(dist, sources);
    
    return answer;
}