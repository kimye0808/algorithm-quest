#include <string>
#include <algorithm>
#include <vector>

using namespace std;

/*
    두 명 씩만 탈 수 있음
*/
int solution(vector<int> people, int limit) {
    int answer = 0;
    
    sort(people.begin(), people.end());
    int n = people.size();
    
    int light = 0;
    int heavy = n-1;
    
    while(light <= heavy){
        if(people[light] + people[heavy] <= limit){
            light++;
            heavy--;
        }else{
            heavy--;
        }
        answer++;
    }
    
    return answer;
}