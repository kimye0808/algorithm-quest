#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

typedef pair<int,int> P;

struct cmp{// 걸리는 시간이 작은거
    bool operator()(P& a, P& b){
        return a.first > b.first; // 부등호 방향 주의
    }
};

int solution(vector<vector<int>> jobs) {
    int answer = 0;
    
    priority_queue<P, vector<P>, cmp> q;
    
    // first가 작은 순서대로, 같으면 second가 작은 순서대로
    sort(jobs.begin(), jobs.end());
    
    int ji = 0;
    int n = jobs.size();
    int time = 0;
    while(ji < n || !q.empty()){
        // 작업 중이면 대기큐에 넣음
        while(ji < n && time >= jobs[ji][0]){
            q.push({jobs[ji][1], jobs[ji][0]});
            ji++;
        }
        
        // 대기 큐에 존재하면
        if(!q.empty()){
            auto [d, s] = q.top();
            q.pop();
            
            time += d;
            answer += time - s;
        }else{
            time = jobs[ji][0];
        }
    }
    
    return answer/n;
}