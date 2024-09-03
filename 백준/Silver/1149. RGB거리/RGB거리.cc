#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
using namespace std;

int N;
int main() {
	cin >> N;
	vector<vector<int>> arr(N + 1, vector<int>(3));
	vector<vector<int>> s(N + 1, vector<int>(3));

	for (int i = 1; i <= N; i++) {
		for (int j = 0; j < 3; j++) {
			cin >> s[i][j];
		}
	}

	arr[1][0] = s[1][0];
	arr[1][1] = s[1][1];
	arr[1][2] = s[1][2];

	for (int i = 2; i <= N; i++) {
		arr[i][0] = min(arr[i - 1][1], arr[i - 1][2]) + s[i][0];
		arr[i][1] = min(arr[i - 1][0], arr[i - 1][2]) + s[i][1];
		arr[i][2] = min(arr[i - 1][0], arr[i - 1][1]) + s[i][2];
	}

	cout << min({ arr[N][0], arr[N][1], arr[N][2] });
}