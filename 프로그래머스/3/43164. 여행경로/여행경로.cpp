#include <string>
#include <algorithm>
#include <vector>
#include <string>
#include <unordered_map>

using namespace std;

int n;
unordered_map<string, vector<pair<string, bool>>> um;

bool dfs(string s, vector<string>& answer){
    if(answer.size() == n+1){
        return true;
    } 

    for(int i=0; i<(int)um[s].size(); i++){
        auto& [to, vis] = um[s][i]; // auto [to, vis] 하면 값 복사라 vis 반영 안되니까 주의
        if(!vis){
            answer.push_back(to);
            vis = true;
            if(dfs(to, answer)){
                return true;                
            }
            vis = false;
            answer.pop_back();
        }
    }
    
    return false;
}

using namespace std;

vector<string> solution(vector<vector<string>> tickets) {
    vector<string> answer;

    n = tickets.size();
    // "icn", {{atl,false},{jfk, false}}
    for(int i=0; i<(int)tickets.size(); i++){
        um[tickets[i][0]].push_back({tickets[i][1], false});
    }

    for (auto& it : um) {
        sort(it.second.begin(), it.second.end());
    }

    answer.push_back("ICN");
    dfs("ICN", answer);

    return answer;
}