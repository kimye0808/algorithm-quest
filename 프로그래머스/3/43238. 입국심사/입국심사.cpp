#include <string>
#include <algorithm>
#include <climits>
#include <vector>

using namespace std;

long long solution(int n, vector<int> times) {
    long long answer = 0;
    
    long long left = 0;
    long long right = (long long)n * (*max_element(times.begin(), times.end()));
    
    // mid 는 심사가 마치는데 걸리는 시간
    while(left <= right){
        long long mid = (left+right)/2;
        
        long long cnt =0;
        for(int i=0; i<(int)times.size(); i++){
            cnt += (mid / times[i]);
        }
        
        if(n <= cnt){
            answer = mid;
            right = mid -1;
        }else{
            left = mid + 1;
        }
    }
    return answer;
}