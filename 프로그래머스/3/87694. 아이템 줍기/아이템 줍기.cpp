#include <string>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

int dx[] = {1, 0, -1, 0};
int dy[] = {0, 1, 0, -1};

int maps[101][101];

int solution(vector<vector<int>> rec, int cx, int cy, int ix, int iy) {

    // 모든 좌표를 2배 확장하여 정확한 경계를 표현 <- 억까 ㄷ자 모양 때문에 자세히 안나와서
    // 미리 갈 수 있는 경로를 마킹한다
    for (auto r : rec) {
        int x1 = r[0] * 2;
        int y1 = r[1] * 2;
        int x2 = r[2] * 2;
        int y2 = r[3] * 2;

        // 사각형의 테두리만을 1로 마킹
        for (int i = x1; i <= x2; ++i) {
            if (maps[i][y1] != 2) maps[i][y1] = 1;
            if (maps[i][y2] != 2) maps[i][y2] = 1;
        }
        for (int i = y1; i <= y2; ++i) {
            if (maps[x1][i] != 2) maps[x1][i] = 1;
            if (maps[x2][i] != 2) maps[x2][i] = 1;
        }

        // 사각형 내부를 2로 마킹
        for (int i = x1 + 1; i < x2; ++i) {
            for (int j = y1 + 1; j < y2; ++j) {
                maps[i][j] = 2;
            }
        }
    }

    queue<pair<int, int>> q;
    q.push({cx * 2, cy * 2});
    vector<vector<int>> d(101, vector<int>(101, 0));
    d[cx * 2][cy * 2] = 1;

    while (!q.empty()) {
        int x = q.front().first;
        int y = q.front().second;
        q.pop();

        if (x == ix * 2 && y == iy * 2) {
            return d[x][y] / 2;
        }

        for (int i = 0; i < 4; ++i) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if (nx >= 0 && ny >= 0 && nx <= 100 && ny <= 100 
                && !d[nx][ny] && maps[nx][ny] == 1) {
                d[nx][ny] = d[x][y] + 1;
                q.push({nx, ny});
            }
        }
    }
}
