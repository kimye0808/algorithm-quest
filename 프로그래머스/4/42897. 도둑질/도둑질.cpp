#include <string>
#include <algorithm>
#include <vector>

using namespace std;

int solution(vector<int> money) {
    int n = money.size();
    if (n == 3) {
        return max({money[0], money[1], money[2]});
    }
    
    // dp[i] i번째까지 고려했을때 최댓값, 
    // dp[i]는 
    // i번째 요소를 더할지(dp[i-2]: i-2까지 최댓값에서 더할지)
    // i번재 요소를 안더할지(dp[i-1]: i-1까지 최댓값을 그대로)
    vector<int> dp1(n, 0);
    vector<int> dp2(n, 0);
    
    // 0번째 집을 포함하지 않는 경우
    dp1[1] = money[1];
    for (int i = 2; i < n; i++) {
        dp1[i] = max(dp1[i-1], dp1[i-2] + money[i]);
    }
    
    // 0번째 집을 포함하는 경우
    dp2[0] = money[0];
    dp2[1] = max(money[0], money[1]);
    for (int i = 2; i < n - 1; i++) {
        dp2[i] = max(dp2[i-1], dp2[i-2] + money[i]);
    }
    
    return max(dp1[n-1], dp2[n-2]);
}
