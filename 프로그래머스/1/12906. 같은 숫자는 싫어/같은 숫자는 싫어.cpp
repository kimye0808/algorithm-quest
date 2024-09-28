#include <vector>
#include <deque>
#include <iostream>

using namespace std;

vector<int> solution(vector<int> arr) 
{
    vector<int> answer;

    deque<int> s;
    
    for(int i=0; i<(int)arr.size(); i++){
        if(!s.empty()){
            int tp = s.back();
            
            if(tp == arr[i]){
                continue;
            }
        }
        s.push_back(arr[i]);
    }
    
    while(!s.empty()){
        answer.push_back(s.front());
        s.pop_front();
    }

    return answer;
}