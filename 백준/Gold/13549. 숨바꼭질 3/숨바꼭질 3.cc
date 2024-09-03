#include <iostream>
#include <vector>
#include <deque>
using namespace std;
typedef pair<int, int> P;

int n, k;
bool vis[1000001];
int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n >> k;

	deque<P> dq;
	dq.push_back({ n, 0 });

	while (!dq.empty()) {
		int x = dq.front().first;
		int t = dq.front().second;
		dq.pop_front();

		if (vis[x]) continue;
		vis[x] = true;

		if (x == k) {
			cout << t;
			break;
		}

		int xmp2 = x * 2;
		if (0 <= xmp2 && xmp2 <= 1000000 && !vis[xmp2]) {
			dq.push_front({ xmp2, t });
		}

		int xp1 = x + 1;
		if (0 <= xp1 && xp1 <= 1000000 && !vis[xp1]) {
			dq.push_back({ xp1, t + 1 });
		}
		int xm1 = x - 1;
		if (0 <= xm1 && xm1 <= 1000000 && !vis[xm1]) {
			dq.push_back({ xm1, t + 1 });
		}
	}
}