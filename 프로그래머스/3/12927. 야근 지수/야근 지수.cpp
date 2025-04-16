#include <bits/stdc++.h>

using namespace std;

/*
최대한 가장 작은데 비슷하게 작게 만들어야 함
그리디인가 했는데 그냥 가장 큰거에서 1씩 빼면 될 듯
priority queue 이용
n이 1백만이라서 시간이 될까?
*/
long long solution(int n, vector<int> works) {
    long long answer = 0;
    
    priority_queue<int> pq(works.begin(), works.end());
    
    while(n--){
        int now = pq.top(); pq.pop();
        if(now == 0) break;
        now -= 1;
        pq.push(now);
    }
    
    while(!pq.empty()){
        int now = pq.top(); pq.pop();
        answer += (long long)now * now;
    }
    return answer;
}