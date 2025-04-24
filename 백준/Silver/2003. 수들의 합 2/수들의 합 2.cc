#include <iostream>
#include <vector>

using namespace std;

/*
* 투포인터
* 일단 e 증가
* s, e 안의 합이 M보다 크다? s 증가
* M인 경우는 카운트
*/
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	int N, M;
	cin >> N >> M;

	vector<int> nums(N);
	for (int idx = 0; idx < N; idx++) {
		cin >> nums[idx];
	}

	int s = 0, e = 0;
	int sum = 0;
	int answer = 0;
	while (e < N) {
		sum += nums[e];
		e++;
		while (sum > M && s<=e) {
			sum -= nums[s];
			s++;
		}
		if (sum == M) {
			answer++;
		}
	}
	cout << answer;
}