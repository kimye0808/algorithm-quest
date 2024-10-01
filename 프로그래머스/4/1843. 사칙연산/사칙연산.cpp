#include <vector>
#include <string>
#include <algorithm>
#include <climits>
using namespace std;

int solution(vector<string> arr) {
    int n = arr.size();
    
    /*
        dp[i][j][k]
        i번쨰 인덱스부터
        j번째 인덱스까지
        k가 1이면 최댓값, k가 0이면 최솟값
    */
    int dp[201][201][2];
    
    for (int i = 0; i < n; i += 2) {
        int num = stoi(arr[i]);
        dp[i][i][0] = num; // 최솟값
        dp[i][i][1] = num; // 최댓값
    }
    
    for (int len = 2; len < n; len += 2) {
        for (int i = 0; i < n - len; i += 2) {
            int j = i + len;
            
            dp[i][j][0] = INT_MAX;
            dp[i][j][1] = INT_MIN;

            for (int k = i + 1; k < j; k += 2) {
                string operatorChar = arr[k];
                if (operatorChar == "+") {
                    dp[i][j][0] = min(dp[i][j][0], dp[i][k-1][0] + dp[k+1][j][0]);
                    dp[i][j][1] = max(dp[i][j][1], dp[i][k-1][1] + dp[k+1][j][1]);
                } else if (operatorChar == "-") {
                    dp[i][j][0] = min(dp[i][j][0], dp[i][k-1][0] - dp[k+1][j][1]);
                    dp[i][j][1] = max(dp[i][j][1], dp[i][k-1][1] - dp[k+1][j][0]);
                }
            }
        }
    }
    
    return dp[0][n-1][1];
}
