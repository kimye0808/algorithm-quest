#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

const int INF = 1e9;


int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);

	vector<vector<int>> d(50, vector<int>(50, INF));

	int n;
	cin >> n;
	
	vector<string> a(n);

	for (int i = 0; i < n; i++) {
		cin >> a[i];
	}

	for (int i = 0; i < n; i++) {
		for (int j = 0; j < n; j++) {
			if (a[i][j] == 'Y' && a[j][i] == 'N') {
				d[i][j] = 1;
			}
		}
	}

	for (int k = 0; k < n; k++) {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (d[i][j] > d[i][k] + d[k][j]) {
					d[i][j] = d[i][k] + d[k][j];
				}
			}
		}
	}

	bool isvalid = true;
	for (int i = 0; i < n; i++) {
		if (d[i][i] != INF) {
			isvalid = false;
		}
	}

	cout << (isvalid ? "YES" : "NO");
}