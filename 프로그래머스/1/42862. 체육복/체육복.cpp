#include <string>
#include <vector>

using namespace std;

int solution(int n, vector<int> lost, vector<int> reserve) {
    int answer = 0;
    
    vector<int> rest(n+1, 1);
    
    for(int i=0; i < (int)lost.size(); i++){
        rest[lost[i]] = 0;
    }
    for(int i=0; i < (int)reserve.size(); i++){
        if(rest[reserve[i]] == 0){
            rest[reserve[i]] = 1;
        }else{
            rest[reserve[i]] = 2;
        }
    }
    
    // rest 2 0 1 0 2 
    for(int i = 1; i <= n; i++){
        int left = i - 1;
        int right = i + 1;
        
        if(rest[i] == 0){
            if(i > 1 && rest[left] == 2){
                rest[i] = 1;
                rest[left] = 1;
            }
            else if(i < n && rest[right] == 2){
                rest[i] = 1;
                rest[right] = 1;
            }
        }
    }
    
    int cnt = 0;
    for(int i=1; i<=n; i++){
        if(rest[i] >= 1){
            cnt++;
        }
    }
    
    answer = cnt;
    return answer;
}