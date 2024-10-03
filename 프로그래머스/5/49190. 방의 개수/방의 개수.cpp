#include <string>
#include <vector>
#include <set>
#include <tuple>

using namespace std;

int dx[] = {-1,-1,0,1,1,1,0,-1};
int dy[] = {0,1,1,1,0,-1,-1,-1};

// 간선 저장 안되어있고
// 정점 중복시
int solution(vector<int> arrows) {
    int answer = 0;

    set<pair<int,int>> p;//전체 정점 저장
    set<tuple<int,int,int,int>> e;//전체 간선 저장

    p.insert({0,0});
    int x= 0;
    int y =0;

    // 한 방향을 두 번 고려해야 
    // 모래시계처럼 x 같은 경우 해결 가능
    for(int i=0; i<(int)arrows.size(); i++){


        for(int j=0; j<2; j++){
            int nx = x + dx[arrows[i]];
            int ny = y + dy[arrows[i]];
            tuple<int, int, int, int> edge = make_tuple(x, y, nx, ny);
            tuple<int, int, int, int> reverseEdge = make_tuple(nx, ny, x, y);

            // 간선 존재 X, 정점 중복 O
            if (e.find(edge) == e.end() && e.find(reverseEdge) == e.end()) {
                if(p.find({nx,ny}) != p.end()){
                    answer++;
                }
                e.insert(make_tuple(x,y,nx,ny));
                e.insert(make_tuple(nx,ny,x,y));
            }

            p.insert({nx, ny});

            x = nx;
            y = ny;          
        }
    }


    return answer;
}