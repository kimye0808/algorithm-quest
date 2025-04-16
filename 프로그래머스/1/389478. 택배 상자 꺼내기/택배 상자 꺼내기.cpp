#include <bits/stdc++.h>
using namespace std;

/*
n 택배 상자 개수
w 가로로 놓는 상자 개수
num 꺼내려는 상자 번호
정답 : 꺼내는 상자 포함 해당 상자를 꺼내기 위해 필요한 상자 개수
*/
/*
n과 w가 작기 때문에
그냥 로우하게 배열 만들어서 해도 될 것 같다
1. n을 w로 나눈다
13 / 3 = 4
0~4 의 높이 배열 생성 가능
0~<3 가로 배열 생성 가능
2. 짝수행에는 왼->우, 홀수행에는 우->왼 채우기
4. 행 역순으로 순회하면서 카운트
4-1. 0이면 패스(카운트 제외)
*/
int solution(int MAX, int w, int targetNum) {
    int answer = 0;
    
    int h = MAX/w;
    if(MAX % w != 0){
        h = h+1;
    }
    vector<vector<int>> board(h, vector<int>(w, 0));
    
    int targetCol = 0;
    int num = 1;
    
    bool done = false;
    for(int row=0; row<h; row++){
        if(row % 2 == 0){
            for(int col=0; col<w; col++){
                if(num > MAX){
                    done = true;
                    break;
                }
                board[row][col] = num;
                if(num == targetNum){
                    targetCol = col;
                }
                num++;
            }  
        }else{
            for(int col=w-1; col>=0; col--){
                if(num > MAX){
                    done = true;
                    break;
                }
                board[row][col] = num;
                if(num == targetNum){
                    targetCol = col;
                }
                num++;        
            }
        }
        if(done) break;
    }
    
    for(int row = h-1; row >= 0; row--){
        if(board[row][targetCol] == 0) continue;
        
        answer++;
        if(board[row][targetCol] == targetNum){
            break;
        }
    }
    
    return answer;
}