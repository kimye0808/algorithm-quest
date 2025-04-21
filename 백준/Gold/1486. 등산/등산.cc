#include <bits/stdc++.h>
using namespace std;

#define vi vector<int>
#define vvi vector<vector<int>>
#define vvi3 vector<vector<vector<int>>>

/*
* 'A'-'Z' = 0-25
* 'a'-'z' = 26-51
* 높 -> 같 or 낮 = 1초
* 낮 -> 높 = 높이 차이 제곱
* 0,0에서 시작
* 바로 인접한 정수 좌표 중 높이 차이가 T보다 크지 않은 곳으로만 다닐 수 있음
* D보다 크지 않은 시간 동안 올라갈 수 있는 최대 높이?
*/
/*
* 각 좌표마다 네 방향 시간을 미리 계산
* 상 우 하 좌 , 없으면 0
* int spent[][][4] 저장
* 
* 시작 점 0,0 고정, 좌표별 cost가 다 다르므로
* bfs보다는 다익스트라로 좌표별 최소 시간 거리 다 계산한 이후에
* D이하인 것 중에서 높이가 최대인 것을 구하면 될 것 같다
* 다익스트라 = ElogE(또는 ElogV)인데 대충 25*25*4 가 E니까 넉넉함
* 
* 인데 다시 내려가는 것 까지 고려해줘야 함
* 그냥 올라가면 끝인줄
* 
* 그러면 다시 다익스트라
* 대신에 
* decSpent[][][4]는 spent[][][4] 저장 방식과 완전 별개
* 
*/
int DR[] = { 1, 0, -1, 0 };
int DC[] = { 0, 1, 0, -1 };

int N, M; // 가로 세로
int T; // 높이 차이 제한
int D; // D 시간 내에
vvi3 spent;
vvi3 decSpent;
vvi board;
vvi timeMap;
vvi decTimeMap;

void init() {
	cin >> N >> M >> T >> D;

	spent.resize(N, vvi(M, vi(4)));
	decSpent.resize(N, vvi(M, vi(4)));
	board.resize(N, vi(M));
	timeMap.resize(N, vi(M, INT_MAX));
	decTimeMap.resize(N, vi(M, INT_MAX));

	for (int row = 0; row < N; row++) {
		string line;
		cin >> line;
		for (int col = 0; col < M; col++) {
			// 26 - 51
			if (line[col] >= 'a' && line[col] <= 'z') {
				board[row][col] = line[col] - 'a' + 26;
			}
			else if (line[col] >= 'A' && line[col] <= 'Z') { // 0 - 25
				board[row][col] = line[col] - 'A';
			}
		}
	}
}

bool outOfBorder(int r, int c) {
	return r < 0 || c < 0 || r >= N || c >= M;
}

void preCalcTime() {
	for (int row = 0; row < N; row++) {
		for (int col = 0; col < M; col++) {
			for (int dir = 0; dir < 4; dir++) {
				int nr = row + DR[dir];
				int nc = col + DC[dir];

				if (outOfBorder(nr, nc)) continue;

				int fromHeight = board[row][col];
				int toHeight = board[nr][nc];
				int diff = abs(fromHeight - toHeight);

				// 올라가는 시간 (row,col) -> (nr,nc)
				if (diff <= T) {
					spent[row][col][dir] = (fromHeight >= toHeight) ? 1 : (toHeight - fromHeight) * (toHeight - fromHeight);
				}
				else {
					spent[row][col][dir] = 0;  // 이동 불가
				}

				// 내려오는 시간 (nr,nc) -> (row,col)
				if (diff <= T) {
					decSpent[row][col][dir] = (toHeight >= fromHeight) ? 1 : (fromHeight - toHeight) * (fromHeight - toHeight);
				}
				else {
					decSpent[row][col][dir] = 0;  // 이동 불가
				}
			}
		}
	}
}

struct cmp {
	bool operator()(vector<int>& a, vector<int>& b) {
		return a[0] > b[0]; // 이게 오름차순
	}
};

void calcTimeClimb() {
	priority_queue<vector<int>, vector<vector<int>>, cmp> q;
	q.push({ 0, 0, 0 }); // 거리, 좌표
	timeMap[0][0] = 0;
	
	while (!q.empty()) {
		vector<int> top = q.top(); q.pop();
		int d = top[0], x = top[1], y = top[2];

		if (timeMap[x][y] < d) {
			continue;
		}

		for (int dir = 0; dir < 4; dir++) {
			int nx = x + DR[dir];
			int ny = y + DC[dir];

			if (outOfBorder(nx, ny)) continue;
			if (spent[x][y][dir] == 0) continue; // 높이 차가 불가능

			if (timeMap[nx][ny] > d + spent[x][y][dir]) {
				timeMap[nx][ny] = d + spent[x][y][dir];
				q.push({timeMap[nx][ny], nx, ny});
			}
		}
	}
}

void calcTimeDec() {
	priority_queue<vector<int>, vector<vector<int>>, cmp> q;
	q.push({ 0, 0, 0 }); // 거리, 좌표
	decTimeMap[0][0] = 0;

	while (!q.empty()) {
		vector<int> top = q.top(); q.pop();
		int d = top[0], x = top[1], y = top[2];

		if (decTimeMap[x][y] < d) {
			continue;
		}

		for (int dir = 0; dir < 4; dir++) {
			int nx = x + DR[dir];
			int ny = y + DC[dir];

			if (outOfBorder(nx, ny)) continue;
			if (decSpent[x][y][dir] == 0) continue; // 높이 차가 불가능

			if (decTimeMap[nx][ny] > d + decSpent[x][y][dir]) {
				decTimeMap[nx][ny] = d + decSpent[x][y][dir];
				q.push({decTimeMap[nx][ny], nx, ny});
			}
		}
	}
}

int findHighest() {
	int maxHeight = 0;
	for (int row = 0; row < N; row++) {
		for (int col = 0; col < M; col++) {
			if (timeMap[row][col] == INT_MAX || decTimeMap[row][col] == INT_MAX) continue;
			if (timeMap[row][col] + decTimeMap[row][col] <= D) {
				//cout << row << ' ' << col << ' ' << timeMap[row][col] + decTimeMap[row][col] << '\n';
				if (maxHeight < board[row][col]) {
					maxHeight = board[row][col];
				}
			}
		}
	}
	return maxHeight;
}


int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	// 초기화 + 입력
	init();

	// 높이 차이 미리 계산
	preCalcTime();

	// 다익스트라로 출발점 부터 모든 점 시간 계산
	calcTimeClimb();

	calcTimeDec();

	// 가장 높은 높이 구하기
	int answer = findHighest();

	cout << answer;
}