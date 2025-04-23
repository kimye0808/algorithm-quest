#include <bits/stdc++.h>
using namespace std;

typedef long long ll;
/*
1-9 = 1자리씩 9개
10-99 = 2자리씩 90개
100-999 = 3자리씩 900개
1000-9999 = 4자리씩 9000개
...
N은 10억 번째의 숫자

예를 들어서 12 중에 1의 위치
1-9, 10 11 1 -> 14
14 - 9 = 5
5 / 2 = 2, 5 % 2 = 1
10 + 2 - 1 = 11 의 끝, 11다음 숫자 12 에서 나머지 1번째, 1
15면
15 - 9 = 6
6 / 2 = 3, 6 % 2 = 0
10 + 3 - 1 = 12

102에서 1 찾기
1-9, 10-99 , 100, 101, 1 => 9 + 180 + 7 = 196
196 - 9 = 187
187 - 180 = 7
7 / 3 = 2, 7 % 3 = 1 => 100 + 2 - 1 ekdma + 1 , 1번째
8 / 3 = 2, 8 % 3 = 2 => 100 + 2 - 1 다음 + 1 , 2번째 0
9 / 3 = 3, 9 % 3 = 0 => 100 + 3 - 1 , 나머지가 0이면 그대로, 3번째 0

N 이 9 이하? 종료
N 이 1 * 9보다 커-> N -= 1 * 9 , 9는 90으로
N 이 2 * 90보다 커 -> N -= 2 * 90, 90은 900으로
N 이 3 * 900 이하 -> N == 124 가정, 실제 N은 100 + 124/3 - 1 = 140 중에서 124%3 = 1이고
1 2 4 면 각각 1 2 0 가리키
*/

int N;
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N;

	ll base = 1;
	ll NINE = 9;
	
	bool changed = false;
	while (N > base * NINE) {
		N -= base * NINE;
		base += 1;
		NINE *= 10;
	}

	int p = N / base;
	int r = N % base;
	int targetNum = 0;
	int targetIdx = 0; 

	char answer;
	if (r != 0) {
		int startNum = (int)pow(10, base - 1); // 100
		targetNum = startNum + p;
		targetIdx = r - 1;
		// cout << targetIdx << '\n';

		string number = to_string(targetNum);
		answer = number[targetIdx];

		// cout << startNum << '\n';

	}
	else {
		int startNum = (int)pow(10, base - 1);
		targetNum = startNum + p - 1;
		targetIdx = base - 1;
		// cout << targetIdx << '\n';

		string number = to_string(targetNum);
		answer = number[targetIdx];

		// cout << startNum << '\n';
	}
	// cout << targetNum << '\n';
	cout << answer;
}