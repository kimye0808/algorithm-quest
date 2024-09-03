#include <iostream>
#include <queue>

using namespace std;

int t, m, n, k;
int x, y;
int board[2500][2500];
bool vis[2500][2500];
typedef pair<int, int> P;
queue<P> Q;
int dx[4] = { 1,0,-1,0 };
int dy[4] = { 0,-1,0,1 };
int main() {
	cin >> t;//test개수

	for (int i = 0; i < t; i++) {
		cin >> m >> n >> k;
		if (i) {
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < n; k++) {
					board[j][k] = 0;
					vis[j][k] = false;
				}
			}
		}


		for (int j = 0; j < k; j++) {
				cin >> x >> y;
				board[x][y] = 1;
		}
		int cnt = 0;
		for (int j = 0; j < m; j++) {
			for (int k = 0; k < n; k++) {
				if (board[j][k] == 1 && !vis[j][k]) {
					Q.push({ j,k });
					vis[j][k] = true;
					cnt++;
					while (!Q.empty()) {
						P tmp = Q.front(); Q.pop();
						for (int dir = 0; dir < 4; dir++) {
							int nx = tmp.first + dx[dir];
							int ny = tmp.second + dy[dir];

							if (nx < 0 || nx >= m || ny < 0 || ny >= n) continue;
							if (vis[nx][ny] || board[nx][ny] != 1) continue;

							vis[nx][ny] = true;
							Q.push({ nx,ny });

						}
						
					}
				}
			}
		}
		cout << cnt << '\n';
	}




}