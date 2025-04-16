#include <bits/stdc++.h>

using namespace std;

/*
완탐은 말이 안되고 그냥 그리디에 가깝?
최대한 비슷한 숫자로 맞춰줘야 한다
그러면 
1. s를 n으로 나눈다 = p
2. 나머지 r가 0이면 그냥 p를 n번 곱하면 끝
2-1. r이 0이 아니면 p를 곱할때 r횟수만큼 p 대신 p+1 곱하면 된다
*/
vector<int> solution(int n, int s) {
    vector<int> answer;
    
    int p = s/n;
    int r = s%n;
    
    if(p == 0){
        answer.push_back(-1);
        return answer;
    }
    
    if(r==0){
        for(int cnt=0; cnt<n; cnt++){
            answer.push_back(p);
        }
    }else{
        for(int cnt=0; cnt<n; cnt++){
            int element = p;
            if(cnt >= n - r){
                element = p + 1;
            }
            answer.push_back(element);
        }
    }
    
    return answer;
}