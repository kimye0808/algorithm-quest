import java.io.*;
import java.util.*;

class Main{
	static int N;
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		List<Integer> li = new ArrayList<>();
		
		N = Integer.parseInt(br.readLine().trim());
		
		for(int r=0; r< N; r++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int c=0; c< N; c++) {
				int num = Integer.parseInt(st.nextToken());
				
				li.add(num);
			}
		}
		
		Collections.sort(li, new Comparator<Integer>() {
			public int compare(Integer a, Integer b) {
				return Integer.compare(b,a);
			}
		});
		
		System.out.println(li.get(N-1));
	}
}