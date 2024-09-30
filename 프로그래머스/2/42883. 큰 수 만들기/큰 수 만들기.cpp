#include <string>
#include <vector>

using namespace std;

string solution(string number, int k) {
    string answer = "";

    vector<char> stack;
    stack.push_back(number[0]);

    for (int i = 1; i < (int)number.length(); i++) {

        while (!stack.empty() && k > 0&& stack.back() - '0' < number[i] - '0') {
            stack.pop_back();
            k--;
        }

        stack.push_back(number[i]);
    }
    
    while (k > 0) {
        stack.pop_back();
        k--;
    }

    for (auto c : stack) {
        answer += c;
    }


    return answer;
}