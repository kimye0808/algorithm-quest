#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
using namespace std;

/*
* dijkstra 한 정점에서 다른 정점들 까지 최단거리
* 두 개의 정점을 지나는 최단 경로
* 1-dijk->v1-dijk->v2-dijk->n 또는
* 1-dijk->v2-dijk->v1-dijk->n 
*/
struct Edge {
    int to;
    int cost;
    Edge(int t, int c) : to(t), cost(c) {}
};

int n, e;
vector<Edge> adj[801];

typedef pair<long long, int> P;

long long dijk(int start, int target) {
    vector<long long> dist(n + 1, LLONG_MAX);
    dist[start] = 0;

    priority_queue<P, vector<P>, greater<P>> q;
    q.push({ 0, start });

    while (!q.empty()) {
        auto [d, u] = q.top();
        q.pop();

        if (d > dist[u]) continue;

        for (auto& edge : adj[u]) {
            int v = edge.to;
            int cost = edge.cost;
            if (dist[v] > dist[u] + cost) {
                dist[v] = dist[u] + cost;
                q.push({ dist[v], v });
            }
        }
    }

    return dist[target];
}

int main() {
    ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
    cin >> n >> e;

    for (int i = 0; i < e; i++) {
        int a, b, c;
        cin >> a >> b >> c;

        adj[a].push_back(Edge(b, c));
        adj[b].push_back(Edge(a, c));
    }

    int v1, v2;
    cin >> v1 >> v2;

    // 1 -> v1 -> v2 -> n
    long long sv1 = dijk(1, v1);
    long long v1v2 = dijk(v1, v2);
    long long v2n = dijk(v2, n);

    long long route1 = (sv1 == LLONG_MAX || v1v2 == LLONG_MAX || v2n == LLONG_MAX) ? LLONG_MAX : sv1 + v1v2 + v2n;

    // 1 -> v2 -> v1 -> n
    long long sv2 = dijk(1, v2);
    long long v2v1 = dijk(v2, v1);
    long long v1n = dijk(v1, n);

    long long route2 = (sv2 == LLONG_MAX || v2v1 == LLONG_MAX || v1n == LLONG_MAX) ? LLONG_MAX : sv2 + v2v1 + v1n;

    long long result = min(route1, route2);

    if (result == LLONG_MAX) {
        cout << -1;
    }
    else {
        cout << result;
    }
}