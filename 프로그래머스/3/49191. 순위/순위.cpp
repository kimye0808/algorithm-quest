#include <string>
#include <vector>

using namespace std;

// 처음에 우선순위라고 생각해서 위상정렬로 풀려 했는데
// 이 문제는 DAG가 아닌듯
// cycle이 발생 가능함 1->2->3->1


// 모든 결과 값의 거리(관계) 업데이트 -> 플로이드
int solution(int n, vector<vector<int>> results) {
    int answer = 0;

    vector<vector<bool>> win(n+1, vector<bool>(n+1, false));

    for(auto& result: results){
        win[result[0]][result[1]] = true;
    }

    // i -승리-> k -승리-> j
    for(int k=1; k<=n; k++){
        for(int i=1; i<=n; i++){
            for(int j=1; j<=n; j++){
                if(win[i][k] && win[k][j]){
                    win[i][j] = true;
                }   
            }
        }
    }

    for(int i=1; i<=n; i++){//1번 ~  n번 노드
        int cnt=0;

        for(int j=1; j<=n; j++){
            if(win[i][j] || win[j][i]){
                cnt++;
            }
        }
        if(cnt == n-1){
            answer++;
        }
    }

    return answer;
}