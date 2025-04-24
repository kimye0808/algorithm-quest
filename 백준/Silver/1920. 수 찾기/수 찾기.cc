#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

typedef long long ll;
/*
* N이 10만, 
* 찾아야 하는 숫자는 10만개
* 그냥 탐색하면 시간 초과
* 
* 이분탐색시
* NlogN
*/

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	int N, M;

	// 주어진 숫자 배열
	cin >> N;
	vector<ll> nums(N);
	for (int idx = 0; idx < N; idx++) {
		cin >> nums[idx];
	}
	sort(nums.begin(), nums.end());

	cin >> M;
	for (int idx = 0; idx < M; idx++) {
		ll num;
		cin >> num;

		ll left = 0;
		ll right = N - 1;

		bool found = false;
		while (left <= right) {
			ll mid = left + (right - left) / 2;

			if (nums[mid] > num) {
				right = mid - 1;
			}
			else if (nums[mid] == num) {
				found = true;
				break;
			}
			else {
				left = mid + 1;
			}
		}
		cout << (found ? 1 : 0) << '\n';
	}
}