import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;
/*
10
1 67247
2 1 1
1 56336
3 1 1
2 1 3
4 1
1 14815
4 1
3 3 1
4 1
 */
public class TrickOrTreapSolution {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int Q=fs.nextInt();
		Treap[] treaps=new Treap[Q];
		PrintWriter out=new PrintWriter(System.out);
		for (int qq=0; qq<Q; qq++) {
			int type=fs.nextInt();
			if (type==1) {
				//new treap
				int val=fs.nextInt();
				treaps[qq]=new Treap(val);
			}
			else if (type==2) {
				//merge
				Treap t1=treaps[fs.nextInt()-1], t2=treaps[fs.nextInt()-1];
				if (t1==null || t2==null) throw null;
				t1=Treap.goUp(t1);
				t2=Treap.goUp(t2);
				if (t1==t2) continue;
				Treap.merge(t1, t2);
			}
			else if (type==3) {
				//split
				Treap t1=treaps[fs.nextInt()-1];
				t1=Treap.goUp(t1);
				if (t1 == null) throw null;
				int splitSize=fs.nextInt();
				if (t1.subtreeSize<=splitSize)
					continue;
				Treap.split(t1, splitSize);
			}
			else {
				Treap t1=treaps[fs.nextInt()-1];
				if (t1==null) throw null;
				Treap root=Treap.goUp(t1);
				out.println(root.sum);
			}
		}
		out.close();
	}
	
	static final Random rand=new Random(5);
	// lower priority on top, all methods return the new treap root
	// To add new seg-tree supported properties, edit recalc()
	// To add lazyprop values, edit recalc() and prop()

	// If you don't need lazyprop, skip prop() and rangeAdd()
	static class Treap {
		int data, priority;
		Treap[] kids=new Treap[2];
		Treap par;
		
		int subtreeSize;
		long sum;
		int toProp;
		
		public Treap(int data) {
			this.data=data;
			priority=rand.nextInt();
			recalc(this);
		}
		
		//returns lefthalf, rightHalf
		//nInLeft is size of left treap, aka index of first thing in right treap
		static Treap[] split(Treap me, int nInLeft) {
			if (me==null) return new Treap[] {null, null};
			prop(me);
			if (size(me.kids[0])>=nInLeft) {
				
				Treap[] leftRes=split(me.kids[0], nInLeft);
				if (me.kids[0] != null) me.kids[0].par=null;
				me.kids[0]=leftRes[1];
				if (me.kids[0] != null) me.kids[0].par=me;
				recalc(me);
				return new Treap[] {leftRes[0], me};
			}
			else {
				nInLeft=nInLeft-size(me.kids[0])-1;
				Treap[] rightRes=split(me.kids[1], nInLeft);
				if (me.kids[1] != null) me.kids[1].par=null;
				me.kids[1]=rightRes[0];
				if (me.kids[1] != null) me.kids[1].par=me;
				recalc(me);
				return new Treap[] {me, rightRes[1]};
			}
		}
		
		static Treap merge(Treap l, Treap r) {
			if (l==null) return r;
			if (r==null) return l;
			prop(l); prop(r);
			if (l.priority<r.priority) {
				if (l.kids[1]!=null) l.kids[1].par=null;
				l.kids[1]=merge(l.kids[1], r);
				if (l.kids[1]!=null) l.kids[1].par=l;
				recalc(l);
				return l;
			}
			else {
				if (r.kids[0]!=null) r.kids[0].par=null;
				r.kids[0]=merge(l, r.kids[0]);
				if (r.kids[0]!=null) r.kids[0].par=r;
				recalc(r);
				return r;
			}
		}
		
		static void recalc(Treap me) {
			if (me==null) return;
			me.subtreeSize=1;
			me.sum=me.data+me.toProp*size(me);
			for (Treap t:me.kids) if (t!=null) me.subtreeSize+=t.subtreeSize;
			for (Treap t:me.kids) if (t!=null) me.sum+=t.sum+t.toProp*size(t);
		}
		
		static void prop(Treap me) {
			if (me==null) return;
			if (me.toProp==0) return;
			for (Treap t:me.kids) if (t!=null) t.toProp+=me.toProp;
			me.data+=me.toProp;
			me.toProp=0;
			recalc(me);
		}
		
		static Treap rangeAdd(Treap t, int l, int r, int toAdd) {
			Treap[] a=split(t, l);
			Treap[] b=split(a[1], r-l+1);
			b[0].toProp+=toAdd;
			return merge(a[0], merge(b[0], b[1]));
		}
		
		static Treap goUp(Treap t) {
			if (t==null || t.par==null) return t;
			return goUp(t.par);
		}
		
		static int size(Treap t) {
			return t==null?0:t.subtreeSize;
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
