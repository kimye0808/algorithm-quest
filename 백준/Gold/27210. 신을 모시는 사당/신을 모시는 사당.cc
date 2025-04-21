#include <bits/stdc++.h>
using namespace std;

/*
1912 연속합 문제와 유사
근데 처음에 못풀었음

N이 최대 10만개
투포인터로 접근했었는데
투포인터로는 해결하지 못하는 예외 케이스가 존재했다

f(idx) = V
idx 인덱스까지를 고려했을떄 최대 합 V
idx는 idx-1까지의 최대합에 현재 인덱스 넘버를 더한 값과 인덱스 넘버 값 중에 최댓값
f(idx) = max(f(idx-1) + num[idx] , num[idx])
*/
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	int N;
	cin >> N;

	vector<int> dol(N);
	vector<int> dpLR(N);
	vector<int> dpRL(N);

	// 돌 입력
	for (int idx = 0; idx < N; idx++) {
		cin >> dol[idx];
	}


	// 왼 -> 오 돌 카운팅 (1 개수에 우선순위 : 1개수 > 2개수)
	dpLR[0] = dol[0] == 1 ? 1 : -1;
	for (int idx = 1; idx < N; idx++) {
		int dolNum = 1;
		if (dol[idx] == 1) {
			dolNum = 1;
		}
		else {
			dolNum = -1;
		}

		// 어차피 1과 2는 자격이 동일하므로 1, -1로 처리
		dpLR[idx] = max(dpLR[idx - 1] + dolNum, dolNum);
	}

	// 오 -> 왼 돌 카운팅, 1과 2의 자격을 바꾼다 (2개수에 우선순위 : 2개수 > 1개수)
	dpRL[N - 1] = dol[N - 1] == 2 ? 1 : -1;
	for (int idx = N - 2; idx >= 0; idx--) {
		int dolNum = 1;
		if (dol[idx] == 1) {
			dolNum = -1;
		}
		else {
			dolNum = 1;
		}

		// 어차피 1과 2는 자격이 동일하므로 -1, 1로 처리
		dpRL[idx] = max(dpRL[idx + 1] + dolNum, dolNum);
	}

	int maxLR = *max_element(dpLR.begin(), dpLR.end());
	int maxRL = *max_element(dpRL.begin(), dpRL.end());

	cout << max(maxLR, maxRL);
}