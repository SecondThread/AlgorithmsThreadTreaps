import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class GrimTreaperSolution {

	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), q=fs.nextInt();
		int[] a=fs.readArray(n);
		Treap t=null;
		int index=0;
		for (int i:a) {
			Data data=new Data(i, (long)-1e18, 1, 0, i, 0, 1);
			t=Treap.merge(t, new Treap(data, index++));
		}
		PrintWriter out=new PrintWriter(System.out);
		for (int qq=0; qq<q; qq++) {
			int type=fs.nextInt();
			if (type==1) {
				//slice
				int l=fs.nextInt()-1, r=fs.nextInt()-1, h=fs.nextInt();
				if (l>r) throw null;
				Treap[] parts1=Treap.split(t, l);
				Treap[] parts2=Treap.split(parts1[1], r-l+1);
				Treap.prop(parts2[0]);
				minWith(h, parts2[0]);
				t=Treap.merge(parts1[0], Treap.merge(parts2[0], parts2[1]));
			}
			else if (type==2) {
				//translate l..r to right end
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				if (l>r) throw null;
				Treap[] parts1=Treap.split(t, l);
				Treap[] parts2=Treap.split(parts1[1], r-l+1);
				t=Treap.merge(parts1[0], Treap.merge(parts2[1], parts2[0]));
			}
			else if (type==3) {
				//range add
				int l=fs.nextInt()-1, r=fs.nextInt()-1, toAdd=fs.nextInt();
				if (l>r) throw null;
				Treap[] parts1=Treap.split(t, l);
				Treap[] parts2=Treap.split(parts1[1], r-l+1);
				Treap.prop(parts2[0]);
				rangeAdd(parts2[0].subtreeData, toAdd);
				t=Treap.merge(parts1[0], Treap.merge(parts2[0], parts2[1]));
			}
			else throw null;
			out.println(t.subtreeData.sum());
		}
		out.close();
	}
	
	static final Random rand=new Random(5);
	// lower priority on top, all methods return the new treap root
	// To add new seg-tree supported properties, edit recalc()
	// To add lazyprop values, edit recalc() and prop()

	// If you only add by merging, skip add() and rebalance()
	// If you don't need lazyprop, skip prop() and rangeAdd()
	static class Treap {
		int priority, id;
		Data data, subtreeData;
		Treap[] kids=new Treap[2];
		
		int subtreeSize;
		
		public Treap(Data data, int id) {
			this.data=data;
			if (this.data==null) throw null;
			this.id=id;
			priority=rand.nextInt();
			this.subtreeData=new Data(0, 0, 0, 0, 0, 0, 0); // fill in in recalc
			recalc(this);
		}
		
		//returns lefthalf, rightHalf
		//nInLeft is size of left treap, aka index of first thing in right treap
		static Treap[] split(Treap me, int nInLeft) {
			if (me==null) return new Treap[] {null, null};
			prop(me);
			if (size(me.kids[0])>=nInLeft) {
				Treap[] leftRes=split(me.kids[0], nInLeft);
				me.kids[0]=leftRes[1];
				recalc(me);
				return new Treap[] {leftRes[0], me};
			}
			else {
				nInLeft=nInLeft-size(me.kids[0])-1;
				Treap[] rightRes=split(me.kids[1], nInLeft);
				me.kids[1]=rightRes[0];
				recalc(me);
				return new Treap[] {me, rightRes[1]};
			}
		}
		
		static Treap merge(Treap l, Treap r) {
			if (l==null) return r;
			if (r==null) return l;
			prop(l); prop(r);
			if (l.priority<r.priority) {
				l.kids[1]=merge(l.kids[1], r);
				recalc(l);
				return l;
			}
			else {
				r.kids[0]=merge(l, r.kids[0]);
				recalc(r);
				return r;
			}
		}
		
		//MUST CALL PROP BEFORE RECALCING!
		static void recalc(Treap me) {
			if (me==null) return;
			if (me.subtreeData.toAdd != 0) throw null;
			me.subtreeSize=1;
			
			Data subtreeData=me.subtreeData, data=me.data;
			data.max=data.sum;
			
			subtreeData.max=data.max();
			subtreeData.maxCount=data.maxCount;
			subtreeData.secondMax=data.secondMax();
			subtreeData.secondMaxCount=data.secondMaxCount;
			subtreeData.sum=data.sum();
			subtreeData.toAdd=0;
			subtreeData.width=1;
			for (Treap t:me.kids) if (t!=null) subtreeData=GrimTreaperSolution.merge(subtreeData, t.subtreeData);
			me.subtreeData=subtreeData;
			for (Treap t:me.kids) if (t!=null) me.subtreeSize+=t.subtreeSize;
			
		}
		
		static void prop(Treap me) {
			if (me==null) return;
			
			if (me.subtreeData.toAdd!=0) {
				rangeAdd(me.data, me.subtreeData.toAdd);
				me.subtreeData.max+=me.subtreeData.toAdd;
				me.subtreeData.secondMax+=me.subtreeData.toAdd;
				for (Treap t:me.kids) if (t!=null) rangeAdd(t.subtreeData, me.subtreeData.toAdd);
				me.subtreeData.toAdd=0;
			}
			
			minWithSafe(me.data, me.subtreeData.max);
			for (Treap t:me.kids) if (t!=null) minWithSafe(t.subtreeData, me.subtreeData.max);
			//reset subtreedata max in recalc
			recalc(me);
		}
		
		static int size(Treap t) {
			return t==null?0:t.subtreeSize;
		}
		
		public String toString() {
			return "Node "+data.sum()+" subtreeData: "+subtreeData+" {"+kids[0]+"} {"+kids[1]+"}";
		}
	}
	
	static class Data {
		long max, secondMax;
		int maxCount, secondMaxCount;
		long sum;
		long toAdd;
		int width;
		
		public Data(long max, long secondMax, int maxCount, int secondMaxCount, long sum, long toAdd, int width) {
			this.max=max;
			this.secondMax=secondMax;
			this.maxCount=maxCount;
			this.secondMaxCount=secondMaxCount;
			this.sum=sum;
			this.width=width;
		}
		
		long max() {
			return max+toAdd;
		}
		
		long secondMax() {
			return secondMax+toAdd;
		}
		
		long sum() {
			return sum+toAdd*width;
		}
		
		public String toString() {
			return "Sum: "+sum+" width: "+width+" sum(): "+sum()+" max: "+max+" maxCount: "+maxCount+" toAdd: "+toAdd;
		}
	}
	
	//assumes that there is nothing more to prop!!
	static Data merge(Data lChild, Data rChild) {
		long a, b;
		int ac, bc;
		long max, secondMax;
		int maxCount, secondMaxCount;
		if (lChild.max()==rChild.max()) {
			max=lChild.max();
			maxCount=lChild.maxCount+rChild.maxCount;
			a=lChild.secondMax();
			b=rChild.secondMax();
			ac=lChild.secondMaxCount;
			bc=rChild.secondMaxCount;
		} else if (lChild.max()>rChild.max()) {
			max=lChild.max();
			maxCount=lChild.maxCount;
			a=lChild.secondMax();
			b=rChild.max();
			ac=lChild.secondMaxCount;
			bc=rChild.maxCount;
		} else {
			max=rChild.max();
			maxCount=rChild.maxCount;
			a=lChild.max();
			b=rChild.secondMax();
			ac=lChild.maxCount;
			bc=rChild.secondMaxCount;
		}
		if (a==b) {
			secondMax=a;
			secondMaxCount=ac+bc;
		} else if (a>b) {
			secondMax=a;
			secondMaxCount=ac;
		} else {
			secondMax=b;
			secondMaxCount=bc;
		}
		long sum=lChild.sum()+rChild.sum();
		int width=lChild.width+rChild.width;
		return new Data(max, secondMax, maxCount, secondMaxCount, sum, 0, width);
	}
	
	static void minWith(long minWith, Treap me) {
		if (me==null) return;
		Treap.prop(me);
		Data d=me.subtreeData;
		minWith-=d.toAdd;
		if (minWith>=d.max) {
			return;
		}
		if (minWith<=d.secondMax) {
			for (Treap t:me.kids) {
				minWith(minWith, t);
			}
			minWithSafe(me.data, minWith);
			Treap.recalc(me);
		}
		else {
			d.sum-=d.maxCount*(d.max-minWith);
			d.max=minWith;
		}
	}
	
	static void minWithSafe(Data d, long minWith) {
		minWith-=d.toAdd;
		if (minWith>=d.max) return;
		if (minWith<=d.secondMax) {
			throw null;
		}
		else {
			d.sum-=d.maxCount*(d.max-minWith);
			d.max=minWith;
		}
	}
	
	static void rangeAdd(Data d, long toAdd) {
		d.toAdd+=toAdd;
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
