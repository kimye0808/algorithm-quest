#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>
using namespace std;
typedef long long ll;
typedef tuple<ll, int, int> T;//dist, node, kcnt

const ll INF = LLONG_MAX;

struct Edge {
	int to, cost;
	Edge(int t, int c) :to(t), cost(c) {};
};

vector<Edge> adj[10001];
bool check[10001][21];
ll d[10001][21];

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	int n, m, k;
	cin >> n >> m >> k;

	for (int i = 0; i < m; i++) {
		int a, b, c;
		cin >> a >> b >> c;

		adj[a].push_back(Edge(b, c));
		adj[b].push_back(Edge(a, c));
	}

	for (int i = 1; i <= n; i++) {
		for (int j = 0; j <= k; j++) {
			d[i][j] = INF;
		}
	}

	for (int j = 0; j <= k; j++) {
		d[1][j] = 0;
	}

	priority_queue<T, vector<T>, greater<T>> q;

	q.push({ 0, 1, 0 });

	while (!q.empty()) {
		auto [dist, node, kcnt] = q.top();
		q.pop();

		if (check[node][kcnt]) continue;
		check[node][kcnt] = true;

		//cout << dist << ' ' << node << ' ' << kcnt << '\n';

		for (auto& edge : adj[node]) {
			int to = edge.to;
			int cost = edge.cost;

			if (kcnt + 1 <= k) {
				if (d[to][kcnt+1] > d[node][kcnt]) {
					d[to][kcnt+1] = d[node][kcnt];

					q.push({ d[to][kcnt+1], to, kcnt + 1});
				}
			}

			if (d[to][kcnt] > d[node][kcnt] + cost) {
				d[to][kcnt] = d[node][kcnt] + cost;

				q.push({ d[to][kcnt], to, kcnt });
			}
		}
	}
	// N번 도시에 도달하는 여러 경로 중 최소 시간 찾기
	ll result = *min_element(d[n], d[n] + k + 1);
	//for (int i = 0; i <= k; i++) {
	//	cout << "tlqkf: " << d[n][i] << '\n';
	//}

	//// INF 여부 확인
	//if (result == INF) {
	//	cout << "tlqkf" << endl;  // 도달할 수 없는 경우
	//}
	//else {
		cout << result << endl;
	//}

}