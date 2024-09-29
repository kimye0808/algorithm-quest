#include <string>
#include <queue>
#include <vector>

using namespace std;

int solution(int bridge_length, int weight, vector<int> truck_weights) {
    int answer = 0;

    queue<pair<int,int>> bgweight; //들어온 시각, 무게
    queue<int> waitings;

    for(int i=0; i< (int)truck_weights.size(); i++){
        waitings.push(truck_weights[i]);
    }

    int bgsum = 0;
    long long time = 0;
    while(!(waitings.empty() && bgweight.empty())){
        time++;

        if (!bgweight.empty()) {
            auto [t, w] = bgweight.front();
            if (t + bridge_length == time) {
                bgsum -= w;  
                bgweight.pop();  // 트럭 제거
            }
        }



        if(!waitings.empty()){
            int nxt = waitings.front();
            if(bgsum + nxt <= weight){
                waitings.pop();
                bgweight.push({time, nxt});
                bgsum += nxt;
            } 
        }
    }

    answer = time;
    return answer;
}