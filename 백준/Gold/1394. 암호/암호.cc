#include <bits/stdc++.h>
#include <unordered_map>
using namespace std;

#define DIV 900528;

/*
그냥 전부 해버리면 시간 초과 100P50만 해도 시간 초과

어차피 암호 알파벳 순서는 정해진 것을 이용
ak 의 경우
a의 위치 * 전체 후보 길이 + k의 위치

마치 52면
5 * 10 + 2  같은 느낌

암호 문자열의 길이에 따라 처리

bkr이면
b위치 * 전체길이 * 전체길이 + k위치 * 전체길이 + r위치
이때 알파벳 위치를 1-based로 해야 편하게 구할 수 있음

후보 알파벳이 순서대로가 아닐 수 있으므로
map 같은 것에 넣어도 될듯
*/
int main() {
	ios::sync_with_stdio(0);
	cin.tie(0);

	unordered_map<char, int> alphaIndex;

	string s;
	cin >> s;

	for (int idx = 0; idx < s.length(); idx++) {
		int num = idx + 1; // 1-based
		alphaIndex[s[idx]] = num;
	}

	string password;
	cin >> password;

	int len = password.length();
	// 마지막 문자 전까지는 전체 후보 길이 곱해줘야 함
	long long sum = 0;
	for (int idx = 0; idx < len; idx++) {
		sum = (sum * s.length() + alphaIndex[password[idx]]) % DIV;
	}

	/*
	int answerCnt = 0;
	// 마지막 문자 전까지는 전체 후보 길이 곱해줘야 함
	for (int idx = 0; idx < len - 1; idx++) {
		int size = len - 1 - idx;
		long long number = (long long)pow(alphaIndex[password[idx]], size) % DIV;
		answerCnt = (number * s.length()) % DIV;
	}
	// 마지막은 그냥 추가
	answerCnt = (answerCnt + alphaIndex[password[len - 1]]) % DIV;
	이러니까 long long 범위도 넘어선다고 함

	(((c1*n+c2)*n+c3)*n+c4)*n+c5 ... 이런 방식으로 계산해야 한다
	*/

	cout << sum;
}