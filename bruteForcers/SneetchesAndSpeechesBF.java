import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/*
8 2
00000000
1 1 3
1 5 5

8 1
00000000
1 4 5
 */
public class SneetchesAndSpeechesBF {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), q=fs.nextInt();
		char[] line=fs.next().toCharArray();
		int[] a=new int[n];
		for (int i=0; i<n; i++) a[i]=line[i]-'0';
		for (int qq=0; qq<q; qq++) {
			int type=fs.nextInt();
			int l=fs.nextInt()-1, r=fs.nextInt()-1;
			if (type==1) {
				for (int i=l; i<=r; i++) a[i]^=1;
			}
			else if (type==2) {
				int[] orig=new int[r-l+1];
				for (int i=0; i<orig.length; i++) orig[i]=a[i+l];
				for (int i=0; i<orig.length; i++) a[i+l]=orig[orig.length-1-i];
			}
			else if (type==3) {
				int[] orig=new int[r-l+1];
				for (int i=0; i<orig.length; i++) orig[i]=a[i+l];
				Arrays.sort(orig);
				for (int i=0; i<orig.length; i++) a[i+l]=orig[i];
			}
			else throw null;
			
			int best=0;
			int cur=0;
			int last=a[0];
			for (int i:a) {
				if (i!=last)
					cur=0;
				last=i;
				cur++;
				best=Math.max(best, cur);
			}
			System.out.println(best);
		}
	}
	
	static class FastScanner {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st=new StringTokenizer("");
		public String next() {
			while (!st.hasMoreElements())
				try {
					st=new StringTokenizer(br.readLine());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return st.nextToken();
		}
		
		int nextInt() {
			return Integer.parseInt(next());
		}
		
		int[] readArray(int n) {
			int[] a=new int[n];
			for (int i=0; i<n; i++) a[i]=nextInt();
			return a;
		}
	}

}
