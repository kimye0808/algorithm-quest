#include <string>
#include <unordered_map>
#include <vector>
#include <algorithm>

using namespace std;

vector<int> solution(vector<string> genres, vector<int> plays) {
    vector<int> answer;

    unordered_map < string, vector<pair<int, int>>> m;
    unordered_map<string, int> ms;

    for (int i = 0; i < (int)genres.size(); i++) {
        m[genres[i]].push_back({ i,plays[i] });//{classic, [{0, 500},{3, 150}]} 이런 형태로 저장
        ms[genres[i]] += plays[i];//genre합 계산
    }

    

    vector<pair<string, int>> msv(ms.begin(), ms.end());
    sort(msv.begin(), msv.end(), [](pair<string, int> p1, pair<string, int> p2) {
        return p1.second > p2.second;
        });

    for (int i = 0; i < (int)msv.size(); i++) {
        string gen = msv[i].first;
        
        // 합이 큰 장르 순서대로
        auto it = m.find(gen);
        if (it != m.end()) {
            vector<pair<int, int>> musics = (*it).second;
            sort(musics.begin(), musics.end(), [](pair<int, int> p1, pair<int, int> p2) {
                return p1.second > p2.second || (p1.second == p2.second && p1.first < p2.first);
                }
            );

            for (int i = 0; i < (int)musics.size(); i++) {
                if (i == 2) break;
                answer.push_back(musics[i].first);
            }
        }
    }


    return answer;
}