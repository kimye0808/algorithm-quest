#include <string>
#include <set>
#include <vector>

using namespace std;

#define MAX 10000000

bool notprime[MAX];
void erachae(){
    notprime[0] = notprime[1] = true;
    for(int i=2; i * i <= MAX; i++){
        if(notprime[i]) continue;
        for(int j = i * i; j < MAX; j += i){
            notprime[j] = true;
        }
    }
}

set<int> s;
int sizes;
void bt(int n, int k, string num, vector<bool>& vis, string& numbers){
    if(n==k){
        if(!num.empty()){
            int nn = stoi(num);

            if(nn < MAX && !notprime[nn]){
                s.insert(nn);
            }
        }

        return;
    }

    for(int i=0; i < sizes; i++){
        if(vis[i]) continue;

        vis[i] = true;
        bt(n+1, k, num + numbers[i], vis, numbers);
        vis[i] = false;
    }
}

int solution(string numbers) {
    int answer = 0;

    erachae();

    sizes = (int)numbers.length();

    for(int i=1; i<= sizes; i++){
        vector<bool> vis(sizes, false);
        bt(0, i, "", vis, numbers);
    }

    answer = s.size();

    return answer;
}