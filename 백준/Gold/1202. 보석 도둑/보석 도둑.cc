#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

struct DiaBag{
	int w;
	int v;
	int isBag;
};

int n, k;

bool cmp(DiaBag a, DiaBag b) {
	return a.w < b.w || ((a.w==b.w)&&(a.isBag<b.isBag));//가방이면 더 뒤로 배치
	// a, b, c, 가방1, d, e, 가방2 ...구조
}

int main() {
	ios::sync_with_stdio(0); cin.tie(0); cout.tie(0);
	cin >> n >> k;
	vector<DiaBag> db(n+k);//dia + bag

	for (int i = 0; i < n; i++) {
		cin >> db[i].w >> db[i].v;
	}
	for (int i = n; i < n+k; i++) {
		cin >> db[i].w;
		db[i].isBag = 1;
	}

	sort(db.begin(), db.end(), cmp);


	priority_queue<int> pq;

	long long sum = 0;
	for (auto i : db) {
		if (!i.isBag) {
			pq.push(i.v);
		}
		else {
			if (!pq.empty()) {
				sum += pq.top();//무게가 가장 큰거 리턴
				pq.pop();
			}
		}
	}

	cout << sum;
}
