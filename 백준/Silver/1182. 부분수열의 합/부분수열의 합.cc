#include <iostream>
using namespace std;

int n, s, cnt;
int input[20];

void func(int sum, int k) {
	if (k == n) {
		if (sum == s) {
			cnt++;
		}
		return;
	}

	func(sum, k + 1);
	func(sum + input[k], k + 1);
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> n >> s;
	for (int i = 0; i < n; i++) {
		cin >> input[i];
	}
	func(0, 0);
	if (!s) cnt--;
	cout << cnt;
}