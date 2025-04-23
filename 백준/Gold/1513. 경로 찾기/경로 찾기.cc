#include <bits/stdc++.h>
using namespace std;

#define DIV 1000007

typedef vector<vector<vector<vector<int>>>> vi4;
typedef vector<vector<vector<int>>> vi3;
typedef vector<vector<int>> vi2;
typedef vector<int> vi;

/*
그냥 해서는 불가능
N,M,C는 50까지
오른쪽, 아래쪽 두 방향만 가능하므로 사실상 
일방향적인 상태전이 가능
dp 문제

상태를 어떻게 정의해야 할까 생각했는데
r,c, bitset 처럼 해서 마지막에 들렀던 오락실의 번호를
비트마스킹해서 표현할까 했는데
그러면 2^50이므로 너무 커짐 값이
그래서 사실 오락실 들를때 고려해야 할 것은 가장 최근에 들렀던 오락실의 번호이고
그러면 그것만 고려하자
f(r,c,num) = V
r,c 좌표까지 도달했을때 들렀던 가장 큰 오락실 번호가 num일때
최대 경로 수 = V라고 고려

근데 이것만으로는 부족한게 오락실을 몇 개 방문했는지도 고려해야 함
f(r,c,cnt,num) = V
r,c 좌표까지 도달했을때 들렀던 가장 큰 오락실 번호가 num이고 cnt개를 방문했을때
최대 경로 수 = V

만약 현재 오락실 보드판 값이 0보다 크면
nowNumber = board[r][c] 이고, 
현재 오락실 nowNumber 이하인 위, 왼 상태를 모두 고려,
cnt+1, nowNum 을 갱신해줘야 함

만약 현재 오락실 보드판 값이 0이면
똑같이 모두 갱신해줘야 함
cnt * num 만큼

사실상 반복문 r * c * cnt * num 
*/
int N, M, C;
vector<vector<int>> board;
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N >> M >> C;

	// board size 재조정
	board.resize(N+1);
	for (int idx = 1; idx <= N; idx++) {
		board[idx].resize(M+1);
	}

	// board에 입력
	for (int cidx = 1; cidx <= C; cidx++) {
		int x, y;
		cin >> x >> y;
		board[x][y] = cidx;
	}

	vi4 state(N + 1, vi3(M + 1, vi2(C + 1, vi(C + 1, 0))));

	if (board[1][1] > 0) {
		state[1][1][board[1][1]][1] = 1;
	}
	else {
		state[1][1][0][0] = 1;
	}

	for (int row = 1; row <= N; row++) {
		for (int col = 1; col <= M; col++){
			int now = board[row][col];
			if (now == 0) { // 오락실이 아닐때
				for (int last = 0; last <= C; last++) {
					for (int cnt = 0; cnt <= C; cnt++) {
						if (row - 1 >= 1) {
							state[row][col][last][cnt] += state[row - 1][col][last][cnt];
							state[row][col][last][cnt] %= DIV;
						}
						if (col - 1 >= 1) {
							state[row][col][last][cnt] += state[row][col - 1][last][cnt];
							state[row][col][last][cnt] %= DIV;
						}
					}
				}
			}
			else if (now > 0) { // 오락실일때
				for (int last = 0; last < now; last++) {
					for (int cnt = 0; cnt < C; cnt++) {
						if (row - 1 >= 1) {
							state[row][col][now][cnt + 1] += state[row - 1][col][last][cnt];
							state[row][col][now][cnt + 1] %= DIV;
						}
						if (col - 1 >= 1) {
							state[row][col][now][cnt + 1] += state[row][col - 1][last][cnt];
							state[row][col][now][cnt + 1] %= DIV;
						}
					}
				}
			}
		}
	}

	for (int cidx = 0; cidx <= C; cidx++) {
		int sum = 0;
		for (int last = 0; last <= C; last++) {
			sum += state[N][M][last][cidx];
			sum %= DIV;
		}
		cout << sum << ' ';
	}
}