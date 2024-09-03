#include <iostream>
#include <string>
#include <vector>
using namespace std;
typedef unsigned long ul;

int n, k;
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n >> k;
	cin.ignore();
	int bw[50] = { 0 };
	for (int i = 0; i < n; i++) {
		string s;
		getline(cin, s);
		for (int j = 4; j < s.length()-4; j++) {
			if (s[j] == 'a' 
				|| s[j] == 'n'
				|| s[j] == 't' 
				|| s[j] == 'i' 
				|| s[j] == 'c') continue;
			int bit = s[j] - 'a';
			if (bit > 19) bit -= 5;
			else if (bit > 13) bit -= 4;
			else if (bit > 8) bit -= 3;
			else if (bit > 2) bit -= 2;
			else if (bit > 0) bit -= 1;
			bw[i] |= (1 << bit);
		}
	}


	int maxcnt = 0;
	for (ul i = 0; i < (1 << 21); i++) {
		int cnt = 0;
		for (int j = 0; j < n; j++) {
			if ((i & bw[j]) == bw[j]) {
				cnt++;
			}
		}
		int tmp = i;
		int onecnt = 0;
		while (tmp) {
			if (tmp & 1) {
				onecnt++;
			}
			tmp = (tmp >> 1);
		}
		if (onecnt <= k - 5) {
			if (maxcnt < cnt) maxcnt = cnt;
		}
	}

	cout << maxcnt;
}