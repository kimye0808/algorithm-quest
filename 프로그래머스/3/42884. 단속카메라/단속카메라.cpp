#include <string>
#include <algorithm>
#include <vector>

using namespace std;

// 차 진출 지점에 카메라 다는 것이 최적
// 카메라 단 곳이 다음 진입 지점보다 왼쪽이면 카메라 달기
int solution(vector<vector<int>> routes) {
    int answer = 0;
    
    sort(routes.begin(), routes.end(), [](vector<int> a, vector<int> b){
       return a[1] < b[1]; 
    });
    
    int lastpos = -30001;
    
    for(int i=0; i<(int)routes.size(); i++){
        if(lastpos < routes[i][0]){
            answer++;
            lastpos = routes[i][1];
        }
    }
    
    return answer;
}