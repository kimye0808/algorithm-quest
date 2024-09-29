#include <string>
#include <vector>
#include <algorithm>

using namespace std;

int maxcnt = 0;
vector<bool> vis;

void bt(int k, int cnt, vector<vector<int>>& dungeons) {
    
    maxcnt = max(maxcnt, cnt);

    for (int i = 0; i < (int)dungeons.size(); i++) {
        if (!vis[i]) {
            if (k >= dungeons[i][0]) {
                vis[i] = true;
                bt(k - dungeons[i][1], cnt + 1, dungeons);
                vis[i] = false;
            }
        }
    }
}

int solution(int k, vector<vector<int>> dungeons) {
    int answer = -1;
    vis = vector<bool>(dungeons.size(), false);
    bt(k, 0, dungeons);
    
    answer = maxcnt;
    
    return answer;
}