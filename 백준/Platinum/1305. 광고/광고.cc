#include <iostream>
#include <vector>
#include <string>
using namespace std;

vector<int> prepro(string p) {
	int m = p.size();
	vector<int> pi(m);
	pi[0] = 0;

	int j = 0;
	for (int i = 1; i < m; i++) {
		while (j > 0 && p[i] != p[j]) j = pi[j - 1];
		if (p[i] == p[j]) {
			pi[i] = j + 1;
			j += 1;
		}
		else {
			pi[i] = 0;
		}
	}
	return pi;
}


int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	int n;
	cin >> n;
	cin.ignore();

	string s;
	getline(cin, s);

	auto pi = prepro(s);
	// prefix + something + suffix
	// prefix의 내용 = suffix의 내용
	// 패턴의 최소 길이 = prefix + something의 길이
	// 또는 something + suffix의 길이

	
	cout << n - pi[n - 1];

}