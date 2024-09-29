#include <string>
#include <algorithm>
#include <vector>

using namespace std;

// greedy
int solution(vector<vector<int>> sizes) {
    int answer = 0;
    
    int w = 0;
    int h = 0;
    for(auto& size : sizes){
        int lw = max(size[0], size[1]);
        int lh = min(size[0], size[1]);
        
        w = max(lw, w);
        h = max(lh, h);
    }
    
    answer = w * h;
    
    return answer;
}