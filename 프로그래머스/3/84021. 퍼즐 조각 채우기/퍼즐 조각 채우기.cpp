#include <string>
#include <queue>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

int dx[] = {1, -1, 0, 0};
int dy[] = {0, 0, 1, -1};

vector<vector<pair<int, int>>> dst; // 테이블 도형
vector<vector<pair<int, int>>> dsg; // 게임 보드 도형

void bfs(int i, int j, vector<vector<bool>>& visited, vector<vector<int>>& grid, vector<pair<int, int>>& shape) {
    queue<pair<int, int>> q;
    q.push({i, j});
    visited[i][j] = true;
    shape.push_back({0, 0}); // 시작점 (0,0) 상대적 위치로 저장

    while (!q.empty()) {
        auto [x, y] = q.front();
        q.pop();
        
        for (int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            
            if (nx < 0 || ny < 0 || nx >= grid.size() || ny >= grid[0].size()) continue;
            if (visited[nx][ny]) continue;
            if (grid[nx][ny] == grid[i][j]) { // 같은 값일 때만 탐색
                visited[nx][ny] = true;
                shape.push_back({nx - i, ny - j}); // 상대적 위치 저장
                q.push({nx, ny});
            }
        }
    }
}

void normalizeShape(vector<pair<int, int>>& shape) {
    pair<int, int> minp = {INT_MAX, INT_MAX};
    for (const auto& p : shape) {
        minp.first = min(minp.first, p.first);
        minp.second = min(minp.second, p.second);
    }

    for (auto& p : shape) {
        p.first -= minp.first;
        p.second -= minp.second;
    }

    sort(shape.begin(), shape.end());
}

vector<pair<int, int>> rotateShape(const vector<pair<int, int>>& shape) {
    vector<pair<int, int>> rotated;
    for (const auto& p : shape) {
        rotated.push_back({p.second, -p.first}); // 90도 회전
    }
    return rotated;
}

bool areShapesEqual(vector<pair<int, int>> shape1, vector<pair<int, int>> shape2) {
    if (shape1.size() != shape2.size()) return false;

    normalizeShape(shape1);  // 정규화
    normalizeShape(shape2);

    for (int rotation = 0; rotation < 4; rotation++) {
        if (rotation > 0) {
            shape1 = rotateShape(shape1);
            normalizeShape(shape1); // 회전 후 정규화
        }

        if (shape1 == shape2) return true;
    }

    return false;
}

int solution(vector<vector<int>> gb, vector<vector<int>> tb) {
    int answer = 0;

    int g = gb.size();
    int t = tb.size();

    vector<vector<bool>> visitedTable(t, vector<bool>(t, false));
    vector<vector<bool>> visitedGame(g, vector<bool>(g, false));

    // 테이블의 도형 찾기
    for (int i = 0; i < t; i++) {
        for (int j = 0; j < t; j++) {
            if (!visitedTable[i][j] && tb[i][j] == 1) {
                vector<pair<int, int>> shape;
                bfs(i, j, visitedTable, tb, shape);
                dst.push_back(shape);
            }
        }
    }

    // 게임 보드의 도형 찾기
    for (int i = 0; i < g; i++) {
        for (int j = 0; j < g; j++) {
            if (!visitedGame[i][j] && gb[i][j] == 0) {
                vector<pair<int, int>> shape;
                bfs(i, j, visitedGame, gb, shape);
                dsg.push_back(shape);
            }
        }
    }

    // 도형을 정규화하여 비교할 준비
    for (auto& shape : dst) {
        normalizeShape(shape);
    }
    for (auto& shape : dsg) {
        normalizeShape(shape);
    }

    vector<bool> used(dsg.size(), false); // 게임 보드에서 사용된 도형 표시

    for (const auto& shape1 : dst) {
        for (int i = 0; i < dsg.size(); i++) {
            if (!used[i] && areShapesEqual(shape1, dsg[i])) {
                answer += shape1.size(); // 도형 크기만큼 더함
                used[i] = true;
                break;
            }
        }
    }

    return answer;
}
