#include <bits/stdc++.h>

using namespace std;

/*
백트래킹인가 싶었는데
A, B의 길이가 너무 크다 10만
구하는 것은 얻을 수 있는 최대 승점
작은 것을 작은 걸로 처리할때 제일 승점 쌓기 좋음
A작은 것을 최대한 B 작은 것들로 처리 반복하면 최대 승점을 알 수 있음
1. A 오름차순 정렬
2. B 도 오름차순 정렬
3. A 순회하면서, B중에서 작으면서 A보다 큰 것을 선택
3-1. 안되면 다음 B의 요소로 넘어가서 선택 반복
3-2. B 다 돌았는데도 없으면 종료 , A 다 돌아도 종료
*/
int solution(vector<int> A, vector<int> B) {
    sort(A.begin(), A.end());
    sort(B.begin(), B.end());
    
    int bidx=0;
    int score = 0;
    for(int aidx=0; aidx < A.size(); aidx++){
        if(bidx >= B.size()) return score;
        
        if(A[aidx] < B[bidx]){
            score++;
            bidx++;
        }else{
            while(A[aidx] >= B[bidx]){
                bidx++;
                if(bidx >= B.size()) return score;
            }
            score++;
            bidx++;
        }
    }
    return score;
}