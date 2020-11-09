import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
/*
5 5
aaaaa
2 b 3
1 1 1
3 5 5
1 5 5
1 3 3

 */
public class PearTreapBF {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), q=fs.nextInt();
		char[] a=fs.next().toCharArray();
		PrintWriter out=new PrintWriter(System.out);
		query: for (int qq=0; qq<q; qq++) {
			int type=fs.nextInt();
			if (type==1) {
				//range delete
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				n=a.length;
				char[] newA=new char[n-(r-l+1)];
				for (int i=0; i<l; i++) newA[i]=a[i];
				for (int i=0; i<n-1-r; i++) newA[newA.length-1-i]=a[a.length-1-i];
				a=newA;
			}
			else if (type==2) {
				//point add
				char c=fs.next().charAt(0);
				int index=fs.nextInt()-1;
				n=a.length;
				char[] newA=new char[n+1];
				for (int i=0; i<index; i++) newA[i]=a[i];
				newA[index]=c;
				for (int i=index; i<n; i++) newA[i+1]=a[i];
				a=newA;
			}
			else if (type==3) {
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				n=a.length;
				while (l<r) {
					if (a[l]!=a[r]) {
						out.println("no");
						continue query;
					}
					l++;
					r--;
				}
				out.println("yes");
			}
			else throw null;
		}
		out.close();
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
