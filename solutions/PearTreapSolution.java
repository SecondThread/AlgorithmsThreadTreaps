import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class PearTreapSolution {

	public static void main(String[] args) throws FileNotFoundException {
		FastScanner fs=new FastScanner();
		long time=System.currentTimeMillis();
		PrintWriter out=new PrintWriter(new File("Output.out"));
		int n=fs.nextInt(), q=fs.nextInt();
		char[] line=fs.next().toCharArray();
		Treap t=null, tRev=null;
		for (char c:line) t=Treap.merge(t, new Treap(c));
		for (char c:line) tRev=Treap.merge(new Treap(c), tRev);
		for (int qq=0; qq<q; qq++) {
			System.err.println(qq);
			int type=fs.nextInt();
			if (type==1) {
				if (t.subtreeSize==0) throw null;
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				if (l>r) throw null;
				n=t.subtreeSize;
				t=rangeDelete(t, l, r);
				tRev=rangeDelete(tRev, n-1-r, n-1-l);
			}
			else if (type==2) {
				char toInsert=fs.next().charAt(0);
				if (toInsert>'z'||toInsert<'a') throw null;
				int pos=fs.nextInt()-1;
				if (pos<0||pos>t.subtreeSize) throw null;
				Treap insert1=new Treap(toInsert), insert2=new Treap(toInsert);
				n=t.subtreeSize;
				t=treapInsert(t, insert1, pos);
				tRev=treapInsert(tRev, insert2, n-pos);
			}
			else if (type==3) {
				if (t.subtreeSize==0) throw null;
				int l=fs.nextInt()-1, r=fs.nextInt()-1;
				if (l>r) throw null;
				n=t.subtreeSize;
				HarmeyerHash forwards=rangeHash(t, l, r);
				t=returnedTreap;
				HarmeyerHash backwards=rangeHash(tRev, n-r-1, n-l-1);
				tRev=returnedTreap;
				if (forwards.equals(backwards)) {
					out.println("yes");
				}
				else {
					out.println("no");
				}
			}
			else throw null;
		}
		
		out.println();
		out.close();
		System.err.println(System.currentTimeMillis()-time);
	}
	
	static Treap rangeDelete(Treap t, int l, int r) {
		Treap[] parts1=Treap.split(t, l);
		Treap[] parts2=Treap.split(parts1[1], r-l+1);
		return Treap.merge(parts1[0], parts2[1]);
	}
	
	static Treap treapInsert(Treap t, Treap toInsert, int pos) {
		Treap[] parts=Treap.split(t, pos);
		return Treap.merge(parts[0], Treap.merge(toInsert, parts[1]));
	}
	
	static Treap returnedTreap;
	static HarmeyerHash rangeHash(Treap t, int l, int r) {
		Treap[] parts1=Treap.split(t, l);
		Treap[] parts2=Treap.split(parts1[1], r-l+1);
		HarmeyerHash res=parts2[0].rangeHash;
		returnedTreap=Treap.merge(parts1[0], Treap.merge(parts2[0], parts2[1]));
		return res;
	}
	
	static final Random rand=new Random(5);
	// lower priority on top, all methods return the new treap root
	// To add new seg-tree supported properties, edit recalc()
	// To add lazyprop values, edit recalc() and prop()

	// If you don't need lazyprop, skip prop() and rangeAdd()
	static class Treap {
		HarmeyerHash data;
		int priority;
		Treap[] kids=new Treap[2];

		int subtreeSize;
		HarmeyerHash rangeHash;
		
		public Treap(char c) {
			data=new HarmeyerHash();
			data.add(c);
			priority=rand.nextInt();
			recalc(this);
		}
		
		//returns lefthalf, rightHalf
		//nInLeft is size of left treap, aka index of first thing in right treap
		static Treap[] split(Treap me, int nInLeft) {
			if (me==null) return new Treap[] {null, null};
			if (size(me.kids[0])>=nInLeft) {
				if (me==me.kids[0]) throw null;
				Treap[] leftRes=split(me.kids[0], nInLeft);
				if (leftRes[1]==me) throw null;
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
			if (r==l) throw null;
			if (l.priority<r.priority) {
				l.kids[1]=merge(l.kids[1], r);
				recalc(l);
				return l;
			}
			else {
				if (r==r.kids[0]) throw null;
				r.kids[0]=merge(l, r.kids[0]);
				if (r==r.kids[0]) throw null;
				recalc(r);
				return r;
			}
		}
		
		static void recalc(Treap me) {
			if (me==null) return;
			me.subtreeSize=1;
			me.rangeHash=new HarmeyerHash();
			for (Treap t:me.kids) if (t!=null) me.subtreeSize+=t.subtreeSize;
			if (me.kids[0]!=null) me.rangeHash.append(me.kids[0].rangeHash);
			me.rangeHash.append(me.data);
			if (me.kids[1]!=null) me.rangeHash.append(me.kids[1].rangeHash);
		}
		
		static int size(Treap t) {
			return t==null?0:t.subtreeSize;
		}
	}
	
	static class HarmeyerHash implements Comparable<HarmeyerHash> {
		static final long m1=8675309, m2=1_000_000_007;
		long v1=0, v2=0; int l=0;	
		static final long s1=257, s2=619;
		static long[] s1Pow, s2Pow;
		static boolean precomped=false;
		
		void add(char o) {
			v1=(v1*s1+o)%m1;
			v2=(v2*s2+o)%m2;
			l++;
		}
		
		public int compareTo(HarmeyerHash o) {
			if (v1!=o.v1)
				return Long.compare(v1, o.v1);
				return Long.compare(v2, o.v2);
		}
		
		public boolean equals(Object o) {
			return compareTo((HarmeyerHash)o)==0;
		}

		public int hashCode() {
			return (int)v1;
		}
		
		public HarmeyerHash clone() {
			HarmeyerHash toReturn=new HarmeyerHash();
			toReturn.v1=v1;
			toReturn.v2=v2;
			toReturn.l=l;
			return toReturn;
		}

		static void precomp() {
			if (precomped) return;
			precomped=true;
			s1Pow=new long[1000_000];
			s2Pow=new long[1000_000];
			s1Pow[0]=s2Pow[0]=1;
			for (int i=1; i<s1Pow.length; i++)
				s1Pow[i]=(s1Pow[i-1]*s1)%m1;
			for (int i=1; i<s2Pow.length; i++)
				s2Pow[i]=(s2Pow[i-1]*s2)%m2;
		}

		//need fastPow if o can be longer than 10^6
		void append(HarmeyerHash o) {
			precomp();
			v1=(v1*s1Pow[o.l]+o.v1)%m1;
			v2=(v2*s2Pow[o.l]+o.v2)%m2;
			l+=o.l;
		}
		
		public static HarmeyerHash[] getPrefixHashes(char[] word) {
			precomp();
			int n=word.length;
			HarmeyerHash[] toReturn=new HarmeyerHash[n+1];
			toReturn[0]=new HarmeyerHash();
			for (int i=1; i<=n; i++) {
				toReturn[i]=toReturn[i-1].clone();
				toReturn[i].add(word[i-1]);
			}
			return toReturn;
		}
		
		//inclusive, exclusive
		public static HarmeyerHash substringHash(HarmeyerHash[] prefixHashes, int from, int to) {
			if (from==to)
				return new HarmeyerHash();
			HarmeyerHash old=prefixHashes[to].clone(), toSub=prefixHashes[from];
			int diff=to-from;
			old.v1=(old.v1-(toSub.v1*s1Pow[diff])%m1+m1)%m1;
			old.v2=(old.v2-(toSub.v2*s2Pow[diff])%m2+m2)%m2;
			old.l=to-from;
			return old;
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
