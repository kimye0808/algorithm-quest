#include <bits/stdc++.h>
using namespace std;
/*
바람은 상,하,좌,우 4방향으로 분다
에어컨 위치 좌표도 바람
바람이 있는 곳의 개수

바람이 불면 쭉 감 못갈 때까지
물건 1 : 상 <-> 하 이동 가능, 좌우는 반대 방향으로 튕김(근데 예시 보니 정지 처리해도 될듯?)
물건 2 : 좌 <-> 우 이동 가능, 상하는 반대 방향으로 튕김
물건 3 : 해당 좌표로 가는 방향: 우->상, 하->좌, 상->우, 좌->하 로 방향 꺾임
물건 4 : 해당 좌표로 가는 방향: 우->하, 상->좌, 하->우, 좌->상 로 방향 꺾임

9는 에어컨
1~4는 물건 종류
0은 빈공간

에어컨 + 방문한 좌표 개수


이렇게 했는데 시간초과 발생
4 4
3 0 0 4
9 0 0 0
0 0 0 0
4 0 0 3
이런 상황에서 무한으로 회전
어떻게 막을 수 있을까?
9가 나오면 멈추면 된다 <- 어차피 멈춘 지점의 9에서도 4방향 바람 나오기 떄문
*/
const int UP = 0, RIGHT = 1, LEFT = 2, DOWN = 3;
int DR[] = { -1, 0, 0, 1 }; // 상, 우, 좌, 하
int DC[] = { 0, 1,-1, 0 };

int N, M;
vector<vector<int>> board;
vector<vector<bool>> winds; // 바람체크 보드판
vector<vector<int>> aircons; // 에어컨 좌표들

bool outOfBorder(int r, int c) {
	return r < 0 || c < 0 || r >= N || c >= M;
}

int getCollisionDir(int r, int c, int dir) {
	int objKind = board[r][c];
	switch (objKind) {
	case 1:
		if (dir == UP || dir == DOWN) {
			return dir;
		}
		break;
	case 2:
		if (dir == LEFT || dir == RIGHT) {
			return dir;
		}
		break;
	case 3:
		if (dir == RIGHT) return UP;
		if (dir == DOWN) return LEFT;
		if (dir == UP) return RIGHT;
		if (dir == LEFT) return DOWN;
		break;
	case 4:
		if (dir == RIGHT) return DOWN;
		if (dir == UP) return LEFT;
		if (dir == DOWN) return RIGHT;
		if (dir == LEFT) return UP;
		break;
	}
	return -1; // 방향 이동 불가
}

void blow4Dirs(int x, int y) {
	// 첫 4방향 바람 쏘기
	for (int dir = 0; dir < 4; dir++) {
		int nx = x + DR[dir];
		int ny = y + DC[dir];

		if (outOfBorder(nx, ny)) continue;

		// 바람 처리
		winds[nx][ny] = true;

		// 이제 처음 4방향에서 쭉 확장
		queue<tuple<int, int, int>> q;
		q.push({ nx, ny, dir });

		while (!q.empty()) {
			tuple<int, int, int> nowState = q.front(); q.pop();
			int nowx = get<0>(nowState);
			int nowy = get<1>(nowState);
			int nowDir = get<2>(nowState);

			if (board[nowx][nowy] == 9) break;

			if (board[nowx][nowy] >= 1 && board[nowx][nowy] <= 4) {
				int nextDir = getCollisionDir(nowx, nowy, nowDir);
				if (nextDir == -1) continue;

				int newx = nowx + DR[nextDir];
				int newy = nowy + DC[nextDir];

				if (outOfBorder(newx, newy)) continue;

				winds[newx][newy] = true;

				q.push({ newx,newy,nextDir });
			}
			else {
				// 물건이 아니면 가던 방향 쭉 간다
				int newx = nowx + DR[nowDir];
				int newy = nowy + DC[nowDir];

				if (outOfBorder(newx, newy)) continue;

				winds[newx][newy] = true;

				q.push({ newx,newy,nowDir });
			}
		}
	}
}

int getResult() {
	int cnt = 0;
	for (int r = 0; r < N; r++) {
		for (int c = 0; c < M; c++) {
			if (winds[r][c]) cnt++;
		}
	}
	return cnt;
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N >> M;

	board.assign(N, vector<int>(M, 0));
	winds.assign(N, vector<bool>(M, false));

	for (int r = 0; r < N; r++) {
		for (int c = 0; c < M; c++) {
			cin >> board[r][c];
			if (board[r][c] == 9) {
				winds[r][c] = true;
				aircons.push_back({ r,c });
			}
		}
	}

	for (vector<int> airconPos : aircons) {
		int x = airconPos[0];
		int y = airconPos[1];

		blow4Dirs(x, y);
	}

	// 최종 바람 개수 세기
	int answer = getResult();
	cout << answer;
}