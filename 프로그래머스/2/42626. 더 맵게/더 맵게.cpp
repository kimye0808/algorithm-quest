#include <string>
#include <vector>
#include <queue>

using namespace std;

int solution(vector<int> scoville, int K) {
    int answer = 0;

    priority_queue<int, vector<int>, greater<int>> q;
    for(int i=0; i<(int)scoville.size(); i++){
        q.push(scoville[i]);
    }
    
    while(1){
        if(!q.empty()){
            int mins = q.top();
            if(mins >= K){
                break;
            }else{
                q.pop();
                if(!q.empty()){
                    int mins2 = q.top();
                    q.pop();

                    int news = mins + (mins2 * 2);
                    q.push(news);
                    answer++;
                }else{
                    answer = -1;
                    break;
                }

            } 
        }

    }

    return answer;
}