#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;
/*
일단 격자칸 보고 BFS? 
불가능함 높이 * 격자 크기 만해도 300 * 300 * 1만 이고, 무조건 모든 칸을 돌아야 함

모든 칸 방문 -> 이동의 합을 최소가 되게 한다 -> MST
height 이하 이동 cost = 0, height 보다 큰 이동 cost = 해당 거리
이 합이 최소가 되게 함

격자 판을 그래프처럼 생각
모든 칸 마다 4방향(갈 수 있으면) 엣지 처리
MST를 그리면 사다리 설치 비용의 최솟값을 구할 수 있다
*/
struct Edge{
    int from, to, cost;
    Edge(int from, int to, int cost):from(from),to(to),cost(cost){}
    
    bool operator< (const Edge& other) const{
        return cost < other.cost;
    }
};

vector<Edge> edges;
vector<vector<int>> Land;
vector<vector<int>> nodeNums; // 격자 판에 메기는 번호판: 12345678...
int Height;
int N;
vector<int> p;
vector<int> r;


void init(){
    p.assign(N*N+1, 0); // 주의하자 격자판에 번호 새긴거라 1~N*N임
    for(int node=1; node< N*N+1; node++){
        p[node]=node;
    }
    
    r.assign(N*N+1, 0);
    
    nodeNums.assign(N, vector<int>(N));
}

// 유니온 파인드
int Find(int a){
    if(p[a] != a){
        p[a] = Find(p[a]);
    }
    return p[a];
}
void Union(int a, int b){
    a = Find(a);
    b = Find(b);
    if(a==b) return;
    if(r[a] < r[b]) swap(a,b);
    
    p[b]=a;
    if(r[a]==r[b]){
        r[a]=r[b]+1;
    }
}

// 경계 처리
bool outOfBorder(int r, int c){
    return r<0 || c<0 || r>=N || c>=N;
}

// land에서 엣지 만들기
void makeEdges(){
    int DR[] = {1,0,-1,0};
    int DC[] = {0,1,0,-1};
    
    int nodeNumber=1; // 격자 칸에 매기는 번호

    // 번호 세기기
    for(int row=0; row<N; row++){
        for(int col=0; col<N; col++){
            nodeNums[row][col] = nodeNumber++;        
        }
    }
    for(int row=0; row<N; row++){
        for(int col=0; col<N; col++){
            for(int dir=0; dir<4; dir++){
                int nr = row + DR[dir];
                int nc = col + DC[dir];
                
                if(outOfBorder(nr,nc)) continue;
                
                int toNode = nodeNums[nr][nc];
                int fromNode = nodeNums[row][col];
                
                int gap = (int)abs(Land[row][col] - Land[nr][nc]);
                int acc = 0; // 코스트
                if(gap > Height){ // H 보다 크면 사다리
                    acc = gap;
                }
                
                edges.push_back(Edge(fromNode, toNode, acc));
            }        
        }
    }
}




int solution(vector<vector<int>> land, int height) {
    N = land.size();
    Height = height;
    Land = land;
    
    init();
    
    makeEdges();
    
    sort(edges.begin(), edges.end());
    
    int answer = 0;
    for(Edge e : edges){
        int from = e.from;
        int to = e.to;
        int cost = e.cost;
        if(Find(from) != Find(to)){
            Union(from, to);
            answer += cost;
        }
    }
    
    return answer;
}