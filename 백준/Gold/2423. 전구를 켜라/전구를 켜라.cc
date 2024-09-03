#include <iostream>
#include <deque>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;
typedef tuple<int, int, int, int> T;

int dx[] = { 1, 0, -1, 0, 1, 1, -1, -1 };
int dy[] = { 0, -1, 0, 1, 1, -1, -1, 1 };
bool vis[500][500][2]; // n ,m, 1바뀌, 0 그대로
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	int n, m;
	cin >> n >> m;
	cin.ignore();

	vector<vector<int>> a(n, vector<int>(m));

	for (int i = 0; i < n; i++) {
		string s;
		getline(cin, s);
		for (int j = 0; j < m; j++) {
			if (s[j] == '/') {
				a[i][j] = 0;
			}
			else if (s[j] == '\\') {
				a[i][j] = 1;
			}
		}
	}

	deque<T> q;
	
	if (a[0][0] == 1) {
		q.push_back({ 0,0, a[0][0], 0 });//r, c, a[r][c], cnt
		vis[0][0][0] = true;
	}
	else {
		q.push_back({ 0,0, !a[0][0], 1 });
		vis[0][0][1] = true;
	}

	vector<int> cnts;
	while (!q.empty()) {
		auto [x, y, arc, cnt] = q.front();
		q.pop_front();

		if (x == n - 1 && y == m - 1 && arc == 1) {
			int mincnt = cnt;
			for (auto it = q.begin(); it != q.end(); it++) {
				auto [ix, iy, iarc, icnt] = *it;
				//cout <<"ic "<< icnt << '\n';
				if (mincnt > icnt) {
					mincnt = icnt;
				}
			}
			cout << mincnt;
			return 0;
		}
		
		//cout << x<<' '<<y<<' '<<cnt << '\n';
		for (int dir = 0; dir < 8; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];

			if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
			
			switch (dir) {
			case 0:
			case 1:
			case 2:
			case 3:
				if (arc == a[nx][ny]) {//바꿈
					if (vis[nx][ny][1]) continue;
					q.push_back({ nx,ny,!a[nx][ny], cnt + 1 });
					vis[nx][ny][1] = true;
				}
				else{
					if (vis[nx][ny][0]) continue;
					q.push_front({ nx,ny, a[nx][ny], cnt });
					vis[nx][ny][0] = true;
				}
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				if (arc == 1 && (dir == 5 || dir == 7)) continue;
				if (arc == 0 && (dir == 4 || dir == 6)) continue;
				if (arc != a[nx][ny]) {//바꿈
					if (vis[nx][ny][1]) continue;
					q.push_back({ nx,ny,!a[nx][ny], cnt + 1 });
					vis[nx][ny][1] = true;
				}
				else {
					if (vis[nx][ny][0]) continue;
					q.push_front({ nx,ny, a[nx][ny], cnt });
					vis[nx][ny][0] = true;
				}
				break;
			}
		}
	}

	cout << "NO SOLUTION";
}
