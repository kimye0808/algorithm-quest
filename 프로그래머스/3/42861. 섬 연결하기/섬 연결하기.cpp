#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int p[110];
int r[110];

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
    
    p[b] = a;
    if(r[a]==r[b]){
        r[a]=r[b]+1;
    }
}


// MST 크루스칼
int solution(int n, vector<vector<int>> costs) {
    int answer = 0;
    
    for(int i=1; i<=n; i++){
        p[i] = i;
    }
    
    sort(costs.begin(), costs.end(), [](vector<int> a, vector<int> b){
        return a[2] < b[2];
    });
    
    int cnt = 0;
    for(int i=0; i<(int)costs.size(); i++){
        int u = costs[i][0];
        int v = costs[i][1];
        if(Find(u) != Find(v)){
            Union(u, v);
            answer += costs[i][2];
            cnt++;
            if(cnt == n){
                break;
            }
        }
    }
    
    
    return answer;
}