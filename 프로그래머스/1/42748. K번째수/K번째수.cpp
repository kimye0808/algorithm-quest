#include <string>
#include <vector>
#include <algorithm>

using namespace std;

vector<int> solution(vector<int> array, vector<vector<int>> commands) {
    vector<int> answer;
    
    for(int i=0; i<(int)commands.size(); i++){
        int start = commands[i][0];
        int end = commands[i][1];
        int target = commands[i][2];
        
        vector<int> tmp(array.begin() + start -1, array.begin() + end); // end-1 아님
        
        sort(tmp.begin(), tmp.end());
        
        answer.push_back(tmp[target-1]);
    }
    
    
    return answer;
}