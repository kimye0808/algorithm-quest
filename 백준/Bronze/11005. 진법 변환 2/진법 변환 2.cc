#include <iostream>
#include <vector>

using namespace std;

/*
* 10진법 -> B진법
* 10진법 수 N
* N을 B로 나눈 나머지
* 를 뒤에서부터 시작
* 26을 0이 될때까지 2로 계속 나눈 나머지
* 0 1 0 1 1
* 답은 역순: 1 1 0 1 0
* 
* 진법 수가 커지면 문자로 나타내야하기 때문에
* B<=36이므로 vector<int> 로 저장,
* 결과는 역순으로 읽기,
* K이면 K가 10이상이면 K - 10 + 'A'
* 
* char <-> int 변환에 주의해야함
*/
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	int N, B;
	cin >> N >> B;

	vector<int> namujis;

	while (N) {
		int r = N % B;
		namujis.push_back(r);
		N /= B;
	}

	for (int idx = namujis.size() - 1; idx >= 0; idx--) {
		int num = namujis[idx];
		if (num >= 10) {
			cout<< (char)(num - 10 + 'A');
		}
		else {
			cout << num;
		}
	}
}