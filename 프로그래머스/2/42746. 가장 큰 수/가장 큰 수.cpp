#include <string>
#include <vector>
#include <algorithm>

using namespace std;

/*
1. std::sort에서의 비교 함수 (compare(a, b))
기준: compare(a, b)가 true이면, a가 b보다 앞에 위치해야 합니다.
결과: 오름차순 정렬을 원할 경우, compare(a, b)가 **a < b**일 때 true를 반환합니다. 즉, a가 b보다 작은 경우 a에 우선순위를 부여하는 것입니다.
2. priority_queue에서의 비교 함수 (operation(a, b))
기준: operation(a, b)가 true이면, b가 a보다 더 높은 우선순위를 가져야 합니다.
결과: 최소 힙을 만들고 싶다면 operation(a, b)가 **a > b**일 때 true를 반환합니다. 즉, a가 b보다 큰 경우 b에 우선순위를 부여하는 것입니다.
*/
bool compare(int a, int b){
    string as = to_string(a);
    string bs = to_string(b);
    
    return as + bs > bs + as;
}


string solution(vector<int> numbers) {
    string answer = "";
    
    sort(numbers.begin(), numbers.end(), compare);
    
    for(int i=0; i < (int)numbers.size(); i++){
        answer += to_string(numbers[i]);
    }
    
    if(answer[0] == '0'){
        return "0";
    }
    
    return answer;
}