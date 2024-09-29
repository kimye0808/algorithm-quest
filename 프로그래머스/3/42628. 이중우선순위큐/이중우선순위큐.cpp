#include <string>
#include <vector>
#include <queue>
#include <string>
#include <unordered_map>

using namespace std;

vector<int> solution(vector<string> operations) {
    vector<int> answer;

    priority_queue<int, vector<int>, greater<int>> minpq;
    priority_queue<int> maxpq;
    unordered_map<int, int> valid;

    for(int i=0; i<(int)operations.size(); i++){
        char order = operations[i][0];

        string num = operations[i].substr(2);
        int n = stoi(num);

        switch(order){
            case 'I':
                minpq.push(n);
                maxpq.push(n);
                valid[n]++;
                break;
            case 'D':
                if(n == 1){
                    while(!maxpq.empty()){
                        int maxv = maxpq.top();
                        maxpq.pop();

                        if(valid[maxv]){
                            valid[maxv]--;
                            break;
                        }
                    }
                }else if(n == -1){
                    while(!minpq.empty()){
                        int minv = minpq.top();
                        minpq.pop();

                        if(valid[minv]){
                            valid[minv]--;
                            break;
                        }
                    }     
                }
        }
    }
    while(!minpq.empty() && valid[minpq.top()] == 0){
        minpq.pop();
    }     
    while(!maxpq.empty() && valid[maxpq.top()] == 0){
        maxpq.pop();
    }     
    
    if (!maxpq.empty()) {
        answer.push_back(maxpq.top());
    } else {
        answer.push_back(0);
    }

    if (!minpq.empty()) {
        answer.push_back(minpq.top());
    } else {
        answer.push_back(0);
    }

    return answer;
}