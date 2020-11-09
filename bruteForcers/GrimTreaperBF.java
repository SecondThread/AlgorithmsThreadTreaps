import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
/*
10 10
3 1 2 3 5 2 8 1 6 6
2 7 9
1 7 7 4
2 7 10
2 3 6
2 4 8
1 1 4 6
1 4 5 2
2 5 10
3 2 5 5
2 8 10

10 6
10 10 10 10 10 10 10 10 10 10
1 7 7 4
2 3 6
1 1 4 6

10 6
10 10 10 10 10 10 4 10 10 10
2 3 6
1 1 4 6

 */
public class GrimTreaperBF {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), q=fs.nextInt();
		long[] a=fs.readLongArray(n);
		for (int qq=0; qq<q; qq++) {
//			System.out.println("Array: "+Arrays.toString(a));
			int type=fs.nextInt();
			if (type==1) {
				//cut l to r at h
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				long h=fs.nextLong();
				for (int i=l; i<=r; i++) a[i]=Math.min(a[i], h);
			}
			else if (type==2) {
				//move l to r to the end
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				long[] newA=new long[n];
				int size=r-l+1;
				for (int i=0; i<l; i++)
					newA[i]=a[i];
				for (int i=0; r+1+i<n; i++)
					newA[l+i]=a[r+1+i];
				for (int i=0; i<size; i++)
					newA[n-1-i]=a[r-i];
				a=newA;
			}
			else if (type==3) {
				// l to r += x
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				long x=fs.nextLong();
				for (int i=l; i<=r; i++) a[i]+=x;
			}
			else throw null;
			
			long sum=0;
			for (long l:a) {
				sum+=l;
			}
			System.out.println(sum);
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
		long[] readLongArray(int n) {
			long[] a=new long[n];
			for (int i=0; i<n; i++) a[i]=nextLong();
			return a;
		}
		long nextLong() {
			return Long.parseLong(next());
		}
	}

}
