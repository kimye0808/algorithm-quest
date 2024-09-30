#include <string>
#include <queue>
#include <vector>

using namespace std;

int solution(string begin, string target, vector<string> words) {
    int answer = 0;
    
    vector<bool> used(words.size());
    
    queue<pair<string, int>> q;
    q.push({begin, 0});
    
    while(!q.empty()){
        auto [str, cnt] = q.front();
        q.pop();
        
        if(str == target){
            answer = cnt;
            break;
        }
        
        for(int i=0; i<(int)words.size(); i++){
            if(used[i]) continue;

            int diffcnt=0;
            for(int j=0; j<(int)words[i].size(); j++){
                if(words[i][j] != str[j]){
                    diffcnt++;
               }
            }
            if(diffcnt != 1) continue;
            
            q.push({words[i], cnt + 1});
            used[i] = true;
        }
    }
    
    return answer;
}