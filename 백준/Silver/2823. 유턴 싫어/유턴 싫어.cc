#include <bits/stdc++.h>
using namespace std;

typedef vector<vector<vector<bool>>> vvi3;
typedef vector<vector<bool>> vvi;

/*
* 그냥 bfs 돌리면 된다 생각했는데
* 돌다 보면 중복된 길을 갈 수가 있다
* 근데 유턴은 이전에 왔던 방향의 반대 방향으로는
* 못 가는 거니까 
* 3면이 못가는 상황에서 막다른 길이 발생한다
* bfs 상태에만 이전 방향 넣어주면 될듯
*/
int R, C;
bool outOfBorder(int r, int c) {
	return r < 0 || c < 0 || r >= R || c >= C;
}
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> R >> C;

	vector<vector<char>> board(R, vector<char>(C));

	// 상 좌 우 하
	int DR[] = { 1, 0, 0, -1 };
	int DC[] = { 0, -1, 1, 0 };

	for (int r = 0; r < R; r++) {
		for (int c = 0; c < C; c++) {
			cin >> board[r][c];
		}
	}

	for (int row = 0; row < R; row++) {
		for (int col = 0; col < C; col++) {
			if (board[row][col] == '.') {
				queue<tuple<int, int, int>> q;
				vvi3 vis(R, vvi(C, vector<bool>(4, false)));

				q.push({ row,col,-1 });

				bool cannotgo = false;
				while (!q.empty()) {
					tuple<int, int, int> now = q.front(); q.pop();
					int r = get<0>(now);
					int c = get<1>(now);
					int nowDir = get<2>(now);

					/*
					* 원점으로 돌아와도 모든 방향 길을 체크해야 되기 때문에
					* 해당 들어오는 방향의 상태 전이만 막고 계속 진행
					*/
					if (r == row && c == col && nowDir != -1) {
						continue;
					}

					int blockedCnt = 0;
					for (int dir = 0; dir < 4; dir++) {
						// 항상 반대길은 패스
						if (dir == 3 - nowDir) continue;
						int nr = r + DR[dir];
						int nc = c + DC[dir];

						if (outOfBorder(nr, nc) || board[nr][nc] == 'X') {
							blockedCnt++;
							continue;
						}
						
						if (vis[nr][nc][dir]) continue;
						vis[nr][nc][dir] = true;

						q.push(make_tuple(nr, nc, dir));
					}
					if (blockedCnt == 3) {
						cannotgo = true;
						break;
					}
				}
				if (cannotgo) {
					cout << 1;
					return 0;
				}
			}
		}
	}
	cout << 0;
}