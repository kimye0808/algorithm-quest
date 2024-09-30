#include <string>
#include <vector>

using namespace std;

vector<int> nums;
int t;
int cnt;
void bt(int n, int k, int sum){
    if(n == k){
        if(sum == t){
            cnt++;
        }
        return;
    }
    
    bt(n+1, k, sum+nums[n]);
    bt(n+1, k, sum-nums[n]);
}

int solution(vector<int> numbers, int target) {
    int answer = 0;
    nums = numbers;
    t = target;
    
    bt(0, numbers.size(), 0);
    
    answer = cnt;
    return answer;
}