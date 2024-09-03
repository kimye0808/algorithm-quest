#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/*
* MST : 그래프의 모든 노드 연결하면서 가중치 합이 최소가 되는 트리
* 
* MST 크루스칼 : 간선을 가중치가 작은 순서대로 정렬하고 Union Find 를 이용해서
* for문을 돌리면서 간선을 연결하는 알고리즘
* 프림과 마찬가지로 O(ElogE)
* 
* 프림 - 정점 , priority queue, BFS
* 크루스칼 - 간선, 오름차순 정렬, Union Find
*/

struct Edge {
	int from; // bfs돌리면서 to, cost만 필요한 프림과 다르게 for문 돌리면서 간선 정보 전부 필요
	int to;
	int cost;
	
	/*
	* 크루스칼에서는 프림과 다르게 
	* std::sort가 기본적으로 오름차순임
	* operator < 를 통해서 A < B를 비교하고 A를 우선시함
	* 따라서 여기서 cost가 작은 순서로 하고 싶기 때문에
	* 프림과 다르게 cost < other.cost로 설정함
	* 기본적으로 pq는 내림차순, sort는 오름차순임을 인지하고 어디에 우선순위를
	* 부여하는가 생각하면 될듯
	*/
	bool operator < (const Edge& other) const {
		return cost < other.cost;
	}
};

int r[10001];
int par[10001];

int Find(int x) {
	if (x != par[x]) {
		par[x] = Find(par[x]);
	}
	return par[x];
}

void Union(int a, int b) {
	a = Find(a);
	b = Find(b);

	if (a == b) return;
	if (r[a] < r[b]) swap(a, b);

	par[b] = a;
	if (r[a] == r[b]) {
		r[a]++;
	}
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	int v, e;
	cin >> v >> e;
	for (int i = 1; i <= v; i++) {
		par[i] = i;
	}
	
	vector<Edge> edge(e);

	for (int i = 0; i < e; i++) {
		int a, b, c;
		cin >> a >> b >> c;
		edge[i].from = a;
		edge[i].to = b;
		edge[i].cost = c;
	}

	// cost가 작은 순서대로 정렬
	sort(edge.begin(), edge.end());

	int result = 0;

	for (int i = 0; i < e; i++) {
		Edge e = edge[i];
		int x = Find(e.from);
		int y = Find(e.to);
		if (x != y) {
			Union(x, y);
			result += e.cost;
		}
	}

	cout << result;
}