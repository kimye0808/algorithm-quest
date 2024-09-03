#include <iostream>
#include <algorithm>
#include <vector>
#include <string>
using namespace std;

int n;
bool ban[10];
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	string s;
	cin >> s;
	
	int bancnt = 0;
	cin >> bancnt;
	int num = stoi(s);
	int result = abs(num - 100);
		for (int i = 0; i < bancnt; i++) {
			int tmp;
			cin >> tmp;
			ban[tmp] = true;
		}

		int gap = 1000000;
		int length = 1;
		int answer = 0;
		for (int i = 0; i <= 1000000; i++) {
			int tmp = i;
			int r = tmp;
			bool isnot = false;
			int cnt = 1;
			while (tmp) {

				r = tmp % 10;
				if (ban[r]) {
					isnot = true;
					break;
				}
				if (tmp / 10 == 0) break;
				tmp /= 10;
				cnt+=1;
			}
			if (isnot || ban[r]) {
				continue;
			}

			if (gap > abs(num - i)) {
				gap = abs(num - i);
				answer = i;
				length = cnt;
			}
		}
		result = min(result, length + gap);
		cout << result;
}