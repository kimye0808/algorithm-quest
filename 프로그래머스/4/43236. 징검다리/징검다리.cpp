#include <string>
#include <algorithm>
#include <vector>

using namespace std;

/*
어떤 값을 기준으로 그 값이 가능하거나 불가능한지를 판단할 수 있을 때, 이분 탐색
*/
int solution(int distance, vector<int> rocks, int n) {
    int answer = 0;
    
    sort(rocks.begin(), rocks.end());
    
    int left = 0;
    int right = distance;
    
    // mid는 바위 사이 간격 중 최소
    while(left<=right){
        int mid = (left+right)/2;
        
        int rcnt=0;//제거한 바위 수
        int lastrock = 0;
        for(int i=0; i<(int)rocks.size(); i++){
            int d = rocks[i] - lastrock;
            if(d >= mid){
                lastrock = rocks[i];
            }else{
                rcnt++;
            }
        }
        int finald = distance - lastrock;
        if(finald < mid){
            rcnt++;
        }
        
        if(rcnt <= n){
            left = mid + 1;
            answer = mid;
        }else{
            right = mid -1;
        }
    }
    return answer;
}