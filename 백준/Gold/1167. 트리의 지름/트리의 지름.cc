#include <iostream>
#include <algorithm>
#include <vector>
using namespace std;
typedef pair<int, int> P;

int n;
vector<P> adj[100001];


void dfs(int x, bool vis[], int d[]) {
	vis[x] = true;

	for (auto i : adj[x]) {
		int to = i.first;
		int w = i.second;
		if (!vis[to]) {
			d[to] = d[x] + w;
			dfs(to, vis, d);
		}
	}
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n;
	for (int i = 1; i <= n; i++) {
		int a;
		cin >> a;
		int b;
		while (1) {
			cin >> b;
			if (b == -1) break;
			int w;
			cin >> w;
			adj[a].push_back({ b,w });//b는 to, w 는 weight
			adj[b].push_back({ a,w });
		}
	}

	// 한 점에서 최대 weight 합인 점 찾기
	int d[100001] = { 0 };
	bool vis[100001] = { 0 };

	dfs(1, vis, d);
	int maxnum = 1;
	int maxw = 0;
	for (int i = 1; i <= n; i++) {
		if (maxw < d[i]) {
			maxw = d[i];
			maxnum = i;
		}
	}

	// 위에서 구한 점에서 가장 먼 점 찾기
	fill(d, d + 100001, 0);
	fill(vis, vis + 100001, 0);
	dfs(maxnum, vis, d);
	maxnum = 1;
	maxw = 0;
	for (int i = 1; i <= n; i++) {
		if (maxw < d[i]) {
			maxw = d[i];
			maxnum = i;
		}
	}

	cout << maxw;
}