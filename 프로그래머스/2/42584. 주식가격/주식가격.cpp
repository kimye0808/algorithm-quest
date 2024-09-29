#include <string>
#include <vector>
#include <stack>

using namespace std;

vector<int> solution(vector<int> prices) {
    vector<int> answer(prices.size());

    stack<pair<int,int>> chart;

    for(int i=0; i<(int)prices.size();i++){

        while(!chart.empty()){
            auto [index, val] = chart.top();

            if(val > prices[i]){
                chart.pop();
                answer[index] = i - index;
            }else{
                break;
            }
        }

        chart.push({i, prices[i]});
    }

    while(!chart.empty()){
        auto [index, val] = chart.top();
        chart.pop();
        answer[index] = prices.size() - 1 - index;
    }


    return answer;
}