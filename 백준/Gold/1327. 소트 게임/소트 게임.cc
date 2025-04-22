#include <bits/stdc++.h>
using namespace std;
/*
* 어려웠음
* 그냥 처음에 어떻게 하지 하다가
* 순열을 구해보면 되겠다 싶어서
* 찾아보다가
* prev_permutation 이용해서 
* 일일히 다음 순열 구해서 원래와 K만큼 뒤집혀있는지 체크하고
* 카운트하겠다 했는데
* 뭔가 오류가 있었음
* 
* 모르겠어서 찾아보니 
* BFS에 순열 상태 넣어서 처리함
* 시간이 안될줄 알았는데 어케 되네
* 시간 : 8!(상태공간) * (8 - K + 1)(상태마다 연산)
* 인데 중복체크도 하므로 더 줄어들 수도 있음
* 
* 몰랐던 것:
* 1. void reverse(iterbegin, iterend)
* 
* 놓쳤던 것:
* 0. 최소 방법 수(최단 경로, 최소 상태 전이 수) -> BFS
* 1. vector를 bfs 상태로 처리
*/

int N, K;
vector<int> nums;

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N >> K;
	
	nums.resize(N, 0);
	for (int idx = 0; idx < N; idx++) {
		cin >> nums[idx];
	}

	vector<int> sorted(nums);
	sort(sorted.begin(), sorted.end());

	queue<pair<vector<int>, int>> q;
	set<vector<int>> set;
	q.push({ nums, 0 });
	set.insert(nums);

	int answer = -1;
	while (!q.empty()) {
		auto state = q.front(); q.pop();
		vector<int> now = state.first;
		int cnt = state.second;

		if (now == sorted) {
			answer = cnt;
			break;
		}

		for (int idx = 0; idx <= N - K; idx++) {
			vector<int> next(now);
			reverse(next.begin() + idx, next.begin() + idx + K);

			if (set.count(next)) continue;

			set.insert(next);
			q.push({ next, cnt + 1 });
		}
	}

	cout << answer;
}