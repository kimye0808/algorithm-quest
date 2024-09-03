#include <iostream>
#include <vector>
using namespace std;

typedef long long ll;

pair<ll,ll> sol(ll t, ll n) {
	ll totcnt = 0; // t 전까지의 개수
	ll tcnt = 0; // t의 개수
	for (ll i = 1; i <= n; i++) {
		ll rowcnt = min(n, t / i);
		if (i * rowcnt == t) {
			rowcnt -= 1;
			tcnt += 1;
		}
		totcnt += rowcnt;
	}

	return { totcnt, tcnt };
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	ll n, k;
	cin >> n >> k;

	ll left = 1;
	ll right = n*n;

	ll ans = 0;
	while (left <= right) {
		ll mid = (left + right) / 2;// 예상 숫자

		auto [totcnt, tcnt] = sol(mid, n);

		if (totcnt <= k && k<=totcnt+tcnt) {
			ans = mid;
			right = mid - 1;
		}
		else if(totcnt > k ){
			right = mid - 1;

		}
		else {
			left = mid + 1;
		}
	}

	cout << ans;
}