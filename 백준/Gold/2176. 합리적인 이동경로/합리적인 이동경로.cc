#include <bits/stdc++.h>
using namespace std;

typedef pair<int, int> P;

/*
합리적인 이동경로가 아닌 경우
1. 사이클이 존재
2. 도달하지 못함
[1]
그냥 둘 다 dfs 로 방문 처리하면서
탐색하면 자연스럽게 처리 가능함
1에서 시작, 2에서 종료

그냥 백트래킹 식으로 해봤는데 시간초과
N! 수준이라고 함

[2]
그래서 dp + 백트래킹 접근

f(from) 는 from번 노드에서 2번 노드까지 가는 합리적인 경로의 개수
f(from) 는 (from에서 갈 수 있는 노드) to번 노드에서 2번 노드까지 가는 합리적인 경로 개수의 합

로만 하면 되는 줄 알았는데 이번엔 틀림
[1][2] 시도 까지는 그냥 간선의 비용이 왜 있는지 모르겠고 그냥 무시하고 했는데
아무래도 그게 문제라고 생각
합리적 : 이동할때 2(도착)노드에 가까워지며 이동
A번 노드 -> B 번 노드 이동할때
2번-A번 거리 > 2번-B번 거리 여야 한다는 소리인듯

문제 설명이 부족하다고 생각함
*/
int N, M; // N 은 노드 수, M 은 간선 수
vector<pair<int,int>> adj[1001]; 
vector<int> visPath(1001,0);

vector<int> dist(1001);

/*
도착점 2에서 각 노드까지 가는 거리 미리 계산
*/
void makeProperDist() {
	int start = 2;
	priority_queue<P, vector<P>, greater<P>> pq;
	pq.push({ 0, 2 });

	fill(dist.begin(), dist.end(), INT_MAX); // 연습용으로 함
	dist[start] = 0;

	while (!pq.empty()) {
		auto top = pq.top();
		pq.pop();

		int nowDist = top.first;
		int now = top.second;

		if (dist[now] < nowDist) {
			continue;
		}

		for (auto next : adj[now]) {
			int to = next.first;
			int cost = next.second;

			if (dist[to] > dist[now] + cost) {
				dist[to] = dist[now] + cost;
				pq.push({ dist[to], to });
			}
		}
	}
}


/*
합리적인 경로로 이동한다
f(from) 는 from번 노드에서 2번 노드까지 가는 합리적인 경로의 개수
f(from) 는 (from에서 갈 수 있는 노드) to번 노드에서 2번 노드까지 가는 합리적인 경로 개수의 합
*/
int go(int node, vector<bool>& vis) {
	if (visPath[node]) {
		return visPath[node];
	}

	if (node == 2) {
		visPath[node] = 1;
		return visPath[node];
	}

	int ableCnt = 0;
	for (auto next : adj[node]) {
		int to = next.first;
		if (!vis[to] && dist[to] < dist[node]) {
			vis[to] = true;

			ableCnt += go(to, vis);

			vis[to] = false;
		}
	}

	return visPath[node] = ableCnt;
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N >> M;

	for (int idx = 0; idx < M; idx++) {
		int A, B, C;
		cin >> A >> B >> C;

		adj[A].push_back({ B,C });
		adj[B].push_back({ A,C });
	}
	
	makeProperDist();

	vector<bool> vis(N + 1);

	vis[1] = true;
	go(1, vis);

	cout << visPath[1];
}