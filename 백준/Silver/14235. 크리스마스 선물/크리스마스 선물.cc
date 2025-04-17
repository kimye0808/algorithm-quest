#include <bits/stdc++.h>
using namespace std;

/*
원래 내림차순인데 그냥 priority queue 정렬 연습용으로 다시 함
*/
struct cmp {
	bool operator()(int a, int b) {
		return b > a;
	}
};

int main() {
	int N;
	cin >> N;

	priority_queue<int, vector<int>, cmp> pq;
	
	for (int vidx = 0; vidx < N; vidx++) {
		int giftCnt;
		cin >> giftCnt;

		if (!giftCnt) {
			if (!pq.empty()) {
				cout << pq.top() << '\n';
				pq.pop();
			}
			else {
				cout << -1 << '\n';
			}
			continue;
		}

		for (int idx = 0; idx < giftCnt; idx++) {
			int val;
			cin >> val;
			
			pq.push(val);
		}
	}
}