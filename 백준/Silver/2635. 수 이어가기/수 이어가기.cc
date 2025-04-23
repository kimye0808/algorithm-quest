#include <bits/stdc++.h>
using namespace std;
/*
* 그냥 전부 하면
* N이 3만이라
* 시간 초과날수도 있다고 생각
* 
* 이분탐색으로 가능하지 않을까?
* 어차피 N 다음의 수만 정하면 다음 배열들은 정해진다 자연스럽게
* 아마 N까지는 안걸려도
* logN?
* 그러면 사실상 logN * logN 아님?
* 이분탐색한다고 가정, 어떻게 최대 지점을 구할 것인가?
* N이 100이고
* 60, 61, 62 , 63 을 예시로 해보면
* 배열이 길이가 같거나 크다 -> N 다음 수를 증가시킨다 -> 
* 배열 길이가 작아진다 -> N 다음 수를 감소시킨다
* 반복하면서 최댓값 갱신하다 보면 나오지 않을까?
* left<=right 동안에
* 
* => 라고 했는데 틀림
* 배열의 길이가 단조 증가/단조 감소 가 아니기 떄문
* 60(7) 61(7) 62(8) 63(6)
* 이분 탐색을 적용할려면 단조 증가/감소 여야 한다
* <- 일관된 패턴이 없으면 이분 탐색의 범위 좁히는 논리가 의미 없어지기 떄문
* 
* 그냥 완탐 때려도 될 것 같긴함
* 30000 잡아도 빼다보면 logN연산 걸릴듯
*/


int N; // 시작 숫자
vector<int> answer; // 정답 배열

vector<int> getArr(int nextBig) {
	int localN = N;
	vector<int> tmpArr;
	tmpArr.push_back(N);
	tmpArr.push_back(nextBig);
	while (1) {
		int result = localN - nextBig;
		if (result < 0) break;

		tmpArr.push_back(result);
		localN = nextBig;
		nextBig = result;
	}
	return tmpArr;
}

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> N;
	
	int maxLen = 0;
	for (int nextBig = N; nextBig >= 0; nextBig--) {
		vector<int> tmp = getArr(nextBig);
		if (maxLen < tmp.size()) {
			maxLen = tmp.size();
			answer = tmp;
		}
	}

	cout << maxLen << '\n';
	for (int idx = 0; idx < answer.size(); idx++) {
		cout << answer[idx] << ' ';
	}
}