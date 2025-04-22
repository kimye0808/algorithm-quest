#include <bits/stdc++.h>
using namespace std;

/*
최소 십자가 개수 그런 것도 아니어서 그냥 구현하면 된다
N*M 인 보드판 : 1-based

1. for row, for col, *이면 십자가를 만든다
1-1. 십자가 크기를 1씩 증가시키면서 확인한다
1-2. 만약 불가능하면 만들 수 있는 최대 십자가를 만든다
1-2-0. 십자가 만들때, 네 방향 모두 *여야 가능하다
1-2-1. 십자가 크기가 0이면 실패인데 일단 다음 좌표로 넘어간다
1-2-2. 십자가 크기가 1이상이면 성공이고 다음 좌표로 넘어간다
1-2-3. 만들어진 십자가들은 2차원 bool 에 체크한다
1-3. 최종적으로 bool flag에 체크된 십자가 보드와 원본 보드가 같은지 확인한다
1-3-1. 원본 보드의 *는 true, .은 false로 처리한다
*/
int N, M;
char board[101][101];

bool outOfBorder(int r, int  c) {
	return r < 1 || c < 1 || r > N || c > M;
}

bool makeSipja(int row, int col, vector<vector<bool>>& flagBoard, int& aptSize) {
	int DR[] = { 1, 0, -1, 0 };
	int DC[] = { 0, 1, 0, -1 };

	int size = 0;
	bool able = false;
	while (1) {
		size++;

		int cr = row;
		int cc = col;

		bool done = false;
		for (int dir = 0; dir < 4; dir++) {
			int nr = cr + size * DR[dir];
			int nc = cc + size * DC[dir];

			if (outOfBorder(nr, nc) || board[nr][nc] != '*') {
				done = true;
				break;
			}
		}
		if (done) break;

		able = true;
		for (int dir = 0; dir < 4; dir++) {
			int nr = cr + size * DR[dir];
			int nc = cc + size * DC[dir];

			flagBoard[nr][nc] = true;
		}
		// 적정 사이즈 갱신
		aptSize = size;
	}

	if (able) {
		flagBoard[row][col] = true;
		return true;
	}

	return false;
}

bool checkAbleSipja(vector<vector<bool>> flagBoard) {
	for (int row = 1; row <= N; row++) {
		for (int col = 1; col <= M; col++) {
			if ((flagBoard[row][col] && board[row][col] == '*') ||
				(!flagBoard[row][col] && board[row][col] == '.')) continue;
			else return false;
		}
	}
	return true;
}

void simulate() {
	vector<tuple<int, int, int>> poses; // 십자가 중심 좌표 모음
	vector<vector<bool>> flagBoard(N + 1, vector<bool>(M + 1, false));

	for (int row = 1; row <= N; row++) {
		for (int col = 1; col <= M; col++) {
			if (board[row][col] == '*') {
				int size = 0;
				if (makeSipja(row, col, flagBoard, size)) {
					poses.push_back({ row, col, size });
				}
			}
		}
	}

	// 십자가 맞는지 체크
	bool result = checkAbleSipja(flagBoard);
	if (result) {
		cout << poses.size() << '\n';
		for (tuple<int, int, int> pose : poses) {
			cout << get<0>(pose) << ' ' << get<1>(pose) << ' ' << get<2>(pose) << '\n';
		}
	}
	else {
		cout << -1 << '\n';
	}
}


void init() {
	cin >> N >> M;

	for (int row = 1; row <= N; row++) {
		string line;
		cin >> line;

		for (int col = 1; col <= M; col++) {
			board[row][col] = line[col - 1];
		}
	}
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	init();

	simulate();
}