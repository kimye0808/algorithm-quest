#include <bits/stdc++.h>
using namespace std;
/*
* BNP : 살 수 있는 만큼 다 삼 사기만 함
* TIMING : 3일 연속 증가(3일 전에서 고려) 하면 전부 사고
* TIMING : 3일 연속 감소(3일 전에서 고려) 하면 전부 팜
* 
* 최종 연산 이후에 총 자산 비교
* 
* 그냥 하라는 대로 하면 되는 문제
* 날짜가 적으므로 그냥 시간 고려 안해도 된다
* 연속 증가, 감소만 유의하면 될 듯
* 3일 연속이 겹칠 수도 있음만 주의하자
*/
int TOTMONEY;
int bmoney; // BNP 남은 돈
int tmoney; // TIMING 남은 돈
int bcnt; // BNP 주식수
int tcnt; // TIMING 주식수

int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	cin >> TOTMONEY;
	bmoney = TOTMONEY;
	tmoney = TOTMONEY;

	int inputPrice[15]; // 1-based
	for (int day = 1; day <= 14; day++) {
		cin >> inputPrice[day];
	}

	for (int day = 1; day <= 14; day++) {
		// BNP
		if (bmoney > 0) {
			int p = bmoney / inputPrice[day];
			int r = bmoney % inputPrice[day];

			bmoney = r;
			bcnt += p;
		}

		if (tmoney > 0) {
			// 3일 연속 증가인지 체크
			bool incThree = true;
			for (int past = day - 3; past < day; past++) {
				if (past < 1 || inputPrice[past] >= inputPrice[past + 1]) {
					incThree = false;
					break;
				}
			}
			if (incThree) {
				int p = tmoney / inputPrice[day];
				int r = tmoney % inputPrice[day];

				tmoney = r;
				tcnt += p;
			}

			// 3일 연속 감소인지 체크
			bool decThree = true;
			for (int past = day - 3; past < day; past++) {
				if (past < 1 || inputPrice[past] <= inputPrice[past + 1]) {
					decThree = false;
					break;
				}
			}
			if (decThree) {
				int p = tmoney / inputPrice[day];
				int r = tmoney % inputPrice[day];

				tmoney = r;
				tcnt += p;
			}
		}
	}

	// 최종 자산 비교
	bmoney = bmoney + inputPrice[14] * bcnt;
	tmoney = tmoney + inputPrice[14] * tcnt;
	
	if (bmoney > tmoney) {
		cout << "BNP";
		return 0;
	}
	else if (bmoney < tmoney) {
		cout << "TIMING";
		return 0;
	}
	else {
		cout << "SAMESAME";
		return 0;
	}
}