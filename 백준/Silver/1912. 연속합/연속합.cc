#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int n;
int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> n;
    vector<int> nums(n, 0);
    int sum = -1000, maxsum = -1000;
    for (int i = 0; i < n; i++) {
        cin >> nums[i];
        sum = max(sum + nums[i], nums[i]);
        maxsum = max(maxsum, sum);
    }
    cout << maxsum;
}