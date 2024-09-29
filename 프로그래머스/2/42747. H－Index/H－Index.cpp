#include <string>
#include <algorithm>
#include <vector>

using namespace std;

int solution(vector<int> citations) {
    int answer = 0;
    
    int left = 0;
    int right = 10000;
    sort(citations.begin(), citations.end());
    
    while(left <= right){
        int mid = (left + right)/2;
        
        int cnt = 0;
        int n = citations.size();
        for(int i=0; i < n; i++){
            if(mid <= citations[i]){
                cnt++;
            }
        }
        
        if(cnt >= mid){
            answer = mid;
            left = mid + 1;
        }else{
            right = mid - 1;
        }
    }
    
    return answer;
}