#include <iostream>
#include <list>
#include <string>
using namespace std;

int m;
list<char> li;
string s;
int main() {
    ios::sync_with_stdio(0);
    cin.tie(0);
    cin >> s;
    for (auto c : s) {
        li.push_back(c);
    }
    cin >> m;
    list<char>::iterator it = li.end();
    for (int i = 0; i < m; i++) {
        char a, b;
        cin >> a;
        switch (a) {
        case 'L':
            if (it != li.begin()) it--;
            break;
        case 'D':
            if (it != li.end()) it++;
            break;
        case 'B':
            if (it != li.begin()) {
                it--;
                it = li.erase(it);
            }
            break;
        case 'P':
            cin >> b;
            li.insert(it, b);
            break;
        }
    }

    for (auto c : li) {
        cout << c;
    }
}