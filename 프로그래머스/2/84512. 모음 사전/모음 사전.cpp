#include <string>
#include <vector>

using namespace std;

string alpha[] = {"A", "E", "I", "O", "U"};
int result = 0;
int cnt = 0;

void bt(int n, string made, string& target){
    if(made == target){
        result = cnt;
        return;
    }
    
    if(n==5){
        return;
    }
    
    for(int i=0; i<5; i++){
        cnt++;
        bt(n+1, made + alpha[i], target);
    }
}

int solution(string word) {
    int answer = 0;
    
    bt(0, "", word);
    
    answer = result;
    return answer;
}