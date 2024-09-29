#include <string>
#include <vector>

using namespace std;

vector<int> solution(int brown, int yellow) {
    vector<int> answer;
    
    for(long long i = yellow; i * i >= yellow; i--){
        for(long long j = 1; j * j <= yellow; j++){
            if(i * j != yellow) continue;
            
            if(brown == 2 *i + 2 *j + 4){
                answer.push_back(i+2);
                answer.push_back(j+2);
                break;
            }
        }
    }
    
    return answer;
}