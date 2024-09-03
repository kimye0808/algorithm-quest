#include <iostream>
#include <deque>
#include <algorithm>
#include <string>
using namespace std;

typedef pair<int, int> P;
typedef tuple<int, int, int> T;

int n, m;
int board[101][101];
bool vis[101][101];
int dx[4] = { 1,0,-1,0 };
int dy[4] = { 0,-1,0,1 };

void bfs() {
	deque<T> q;
	q.push_back({ 1,1,0 });

	while (!q.empty()) {
		int x = get<0>(q.front());
		int y = get<1>(q.front());
		int d = get<2>(q.front());

		q.pop_front();
		if (vis[x][y]) continue;
		vis[x][y] = true;

		if (x == n && y == m) {
			int result = d;
			for (auto it = q.begin(); it < q.end(); it++) {
				T tp = *it;
				int tx = get<0>(tp);
				int ty = get<1>(tp);
				int td = get<2>(tp);
				if (tx == n && td == m) {
					if (td < result) {
						result = td;
					}
				}
			}
			cout << result << '\n';
			return;
		}

		for (int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];

			if (nx < 1 || ny < 1 || nx > n || ny > m) continue;
			if (board[nx][ny] == 1) {
				q.push_back({ nx,ny,d + 1 });
			}
			else if (!board[nx][ny]) {
				q.push_front({ nx,ny,d });
			}
		}
	}
}
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> m >> n;
	cin.ignore();
	for (int i = 1; i <= n; i++) {
		string s;
		getline(cin, s);
		for (int j = 0; j < m; j++) {
			board[i][j+1] = s[j]-'0';
		}
	}
	bfs();
}