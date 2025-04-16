#include <bits/stdc++.h>

using namespace std;

/*
cpp 문자열 처리에 대한 이해와 메소드 아냐 묻는 문제인듯
근데 잘 몰라 

toupper, tolower
알아두기

stringstream 공부할겸 활용해볼려 했는데 입력값이 공백문자가 연속으로 가능해서
문제가 너무 복잡해짐 그래서 그냥 순회 방식으로 해결
*/
string solution(string s) {
    string answer = "";
    
    bool blank=true;
    for(int sidx=0; sidx<s.length(); sidx++){
        char c = s[sidx];
        if(c == ' '){
            blank = true;
            answer += " ";
            continue;
        }
        
        if(blank){
            s[sidx] = toupper(s[sidx]);
            blank = false;
            answer += s[sidx];
        }else{
            s[sidx] = tolower(s[sidx]);
            answer += s[sidx];
        }
    }
    return answer;
}