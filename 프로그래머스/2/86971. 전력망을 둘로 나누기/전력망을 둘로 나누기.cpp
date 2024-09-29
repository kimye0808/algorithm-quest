#include <string>
#include <vector>
#include <algorithm>

using namespace std;

vector<int> p(101);
vector<int> r(101);

int Find(int a){
    if(a != p[a]){
        p[a] = Find(p[a]);
    }
    
    return p[a];
}

void Union(int a, int b){
    a = Find(a);
    b = Find(b);
    
    if(a == b) return;
    if(r[a] < r[b]) swap(a, b);
    
    p[b] = a;
    
    if(r[a] == r[b]){
        r[a]++;
    }
}


int solution(int n, vector<vector<int>> wires) {
    int answer = 1e9;

    for(int j=0; j<(int)wires.size(); j++){
        
        for(int i=1; i<n+1; i++){
            p[i] = i;
            r[i] = 1;
        }
        
        for(int i=0; i<(int)wires.size(); i++){
            if(i != j){
                int a = wires[i][0];
                int b = wires[i][1];

                Union(a, b); 
            }
        }
        
        vector<int> parts(n+1, 0);
        
        for(int i=1; i<=n; i++){
            parts[Find(i)]++;
        }
        
        vector<int> areas;
        for(int i=1; i <= n; i++){
            if(parts[i]){
                areas.push_back(parts[i]);
            }
        }
        
        if(areas.size() != 2){
            continue;
        }
        
        answer = min(answer, areas[1] - areas[0] > 0 ? areas[1] - areas[0] : areas[0] - areas[1]);
        
    }

    
    return answer;
}