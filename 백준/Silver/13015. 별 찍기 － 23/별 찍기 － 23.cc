#include<bits/stdc++.h>
using namespace std;
/*
* 가운데는 반드시 *이 생김
* 높이는 2N-1
* 가로는 (N-1)*4 + 1 = 4N-3
* N이 최대 100이면
* 높이는 최대 199
* 가로는 397
* 대충 200 * 400 = 8만
* 8만 * 1바이트 = 8만바이트 
* 512MB = 512백만
* 
* 위치가 바뀌는 점이 4개인데
* 해당 부분의 가로를 변경시키면서
* 보드판에 *을 집어넣는 방식이 쉬울듯
* 
* 바뀌는 점 위치 = 0, N-1, 3N-3, 4N-4
* 행마다 0, N-1은 1씩 증가, 3N-3, 4N-4은 1씩 감소
* 행이 N-1일때까지(0부터) 해당 패턴
* 행이 N부터 2N-1까지 반대로
* 
* 이후, 첫행, 마지막 행은 
* 0부터 N-1까지 *, 3N-3부터 4N-4까지 *
*/

int N;
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N;

	vector<vector<char>> board(2*N-1, vector<char>(4*N-3, ' '));

	int L1 = 0, L2 = N - 1;
	int R1 = 3 * N - 3, R2 = 4 * N - 4;

	for (int row = 0; row <= N - 1; row++) {
		board[row][L1] = '*';
		board[row][L2] = '*';
		board[row][R1] = '*';
		board[row][R2] = '*';

		L1++;
		L2++;
		R1--;
		R2--;
	}
	L1--;
	L2--;
	R1++;
	R2++;
	L1--;
	L2--;
	R1++;
	R2++;
	for (int row = N; row < 2 * N - 1; row++) {
		board[row][L1] = '*';
		board[row][L2] = '*';
		board[row][R1] = '*';
		board[row][R2] = '*';

		L1--;
		L2--;
		R1++;
		R2++;
	}
	for (int col = 0; col <= N - 1; col++) {
		board[0][col] = '*';
		board[2 * N - 2][col] = '*';
	}
	for (int col = 3*N-3; col < 4*N - 3; col++) {
		board[0][col] = '*';
		board[2 * N - 2][col] = '*';
	}

	for (int row = 0; row < 2 * N - 1; row++) {
		int lastStar = -1; // 출력형식 억까방지
		for (int col = 4 * N - 4; col >= 0; col--) {
			if (board[row][col] == '*') {
				lastStar = col;
				break;
			}
		}
		for (int col = 0; col <= lastStar; col++) {
			cout << board[row][col];
		}
		cout << '\n';
	}
}