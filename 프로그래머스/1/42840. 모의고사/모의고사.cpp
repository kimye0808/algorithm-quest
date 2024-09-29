#include <string>
#include <vector>

using namespace std;

vector<int> solution(vector<int> answers) {
    vector<int> answer;
    
    vector<int> one = {1, 2, 3, 4, 5};//5개
    vector<int> two = {2, 1, 2, 3, 2, 4, 2, 5};//8개
    vector<int> three = {3, 3, 1, 1, 2, 2, 4, 4, 5, 5};//10개
    
    int n = answers.size();
    
    vector<int> s(3, 0);
    for(int i=0; i<n; i++){
        s[0] += one[i%5] == answers[i] ? 1 : 0;
        s[1] += two[i%8] == answers[i] ? 1 : 0;
        s[2] += three[i%10] == answers[i] ? 1 : 0;
    }
    
    int maxn = 0;
    for(int i=0; i<3; i++){
        if(maxn < s[i]){
            maxn = s[i];
        }
    }
    
    for(int i=0; i<3; i++){
        if(maxn == s[i]){
            answer.push_back(i+1);
        }
    }
    
    return answer;
}