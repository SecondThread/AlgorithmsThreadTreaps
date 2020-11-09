import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
10
1 6
2 1 1
1 5
3 1 1
2 1 3
4 1
1 1
4 1
3 3 1
4 1

9
1 6
1 5
3 1 1
2 1 2
4 1
1 1
4 1
3 2 1
4 1

5
1 6
1 5
2 1 2
3 2 1
4 1
 */
public class TrickOrTreapBF {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int q=fs.nextInt();
		ArrayList<ArrayList<Item>> lists=new ArrayList<>();
		for (int qq=0; qq<q; qq++) {
			int type=fs.nextInt();
			if (type==1) {
				int value=fs.nextInt();
				ArrayList<Item> toAdd=new ArrayList<>();
				toAdd.add(new Item(qq+1, value));
				lists.add(toAdd);
			}
			else if (type==2) {
				 int n1=fs.nextInt(), n2=fs.nextInt();
				 int g1=getGroup(lists, n1), g2=getGroup(lists, n2);
				 if (g1!=g2) {
					 ArrayList<Item> toRemove=lists.remove(g2);
					 if (g1>g2) g1--;
					 lists.get(g1).addAll(toRemove);
				 }
			}
			else if (type==3) {
				int g1=getGroup(lists, fs.nextInt());
				int maxSize=fs.nextInt();
				ArrayList<Item> toSplit=lists.get(g1);
				if (toSplit.size()>maxSize) {
					ArrayList<Item> prefix=new ArrayList<>(), suffix=new ArrayList<>();
					for (Item i:toSplit) if (prefix.size()<maxSize) prefix.add(i); else suffix.add(i);
					lists.remove(toSplit);
					lists.add(prefix);
					lists.add(suffix);
				}
			}
			else if (type==4) {
				int g1=getGroup(lists, fs.nextInt());
				long ans=0;
				for (Item i:lists.get(g1)) {
					ans+=i.value;
				}
				System.out.println(ans);
			}
			else throw null;
		}
	}
	
	static int getGroup(ArrayList<ArrayList<Item>> lists, int key) {
		int index=0;
		for (ArrayList<Item> list:lists) if (contains(list, key)) return index; else index++;
		return -1;
	}
	
	static boolean contains(ArrayList<Item> list, int key) {
		for (Item i:list)if (i.key==key) return true;
		return false;
	}
	
	static class Item {
		int key, value;
		public Item(int key, int value) {
			this.key=key;
			this.value=value;
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
