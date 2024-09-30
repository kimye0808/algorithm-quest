#include <iostream>
#include <string>
#include <algorithm>

using namespace std;

int solution(string name) {
    int answer = 0;
    int n = name.size();
    int move = n - 1; // 끝까지 쭉 가는 경우

    for (int i = 0; i < n; i++) {
        // 위아래로 알파벳 맞추는 횟수 계산 (A에서 가까운 쪽을 선택)
        answer += min(name[i] - 'A', 'Z' - name[i] + 1);
        
        // 좌우 이동을 고려한 최소 이동 횟수 계산
        int next = i + 1;
        // A가 연속된 구간 찾기
        while (next < n && name[next] == 'A') next++;
        
        // i + n - next: i까지 이동 + i이후 바로나오는 연속 A구간 제외한 구간 이동 횟수
        // min(i, n - next): i는 정방향에서 왼쪽으로 되돌아갈때
        // n-next는 역방향 시작해서 오른쪽으로 되돌아가는 것
        move = min(move, i + n - next + min(i, n - next));
    }
    
    // 총 이동 횟수 = 알파벳 변경 횟수 + 좌우 이동 횟수
    answer += move;
    return answer;
}

