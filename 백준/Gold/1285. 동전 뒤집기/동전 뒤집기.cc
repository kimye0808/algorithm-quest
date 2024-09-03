#include <iostream>
#include <algorithm>
#include <vector>
#include <string>
using namespace std;

char turn(char x) {
	if (x == 'T') {
		return 'H';
	}
	else if (x == 'H') {
		return 'T';
	}
}


int n;

//비트마스크 이용 
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n; cin.ignore();
	vector<vector<char>> table(n, vector<char>(n));
	for (int i = 0; i < n; i++) {
		string s;
		getline(cin, s);
		for (int j = 0; j < n; j++) {
			table[i][j] = s[j];
		}
	}
	
	int minn = 401;

	// 000..0부터 11..11까지 모든 조합
	// (해당하는 행 뒤집지 말지)
	for (int i = 0; i < (1 << n); i++) {
		int totalcnt = 0;
		for (int j = 0; j < n; j++) {// column
			int cnt = 0;
			for (int k = 0; k < n; k++) {// 뒤집힌 row가 있으면 뒤집음
				int rowcol = table[k][j];
				if (i & (1 << k)) {
					rowcol = turn(table[k][j]);
				}
				if (rowcol == 'T') {
					cnt++;
				}
			}
			cnt = min(cnt, n - cnt);
			totalcnt += cnt;
		}
		if (minn > totalcnt) minn = totalcnt;
	}

	cout << minn;
}