#include <bits/stdc++.h>
using namespace std;

pair<int,int> memo[41];

pair<int,int> fibo(int n) {
	if (memo[n] != make_pair(0,0)) {
		return memo[n];
	}
	if (n == 0) {
		return { 1,0 };
	}
	else if (n == 1) {
		return{ 0, 1 };
	}

	pair<int, int> result1 = fibo(n - 1);
	pair<int, int> result2 = fibo(n - 2);

	return memo[n] = { result1.first + result2.first, result1.second + result2.second };
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	int T;
	cin >> T;

	for (int idx = 0; idx < T; idx++) {
		int N;
		cin >> N;
		
		pair<int,int> result = fibo(N);

		cout << result.first << ' ' << result.second << '\n';
	}
}