import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class SneetchesAndSpeechesSolution {
	
	public static void main(String[] args) {
		FastScanner fs=new FastScanner();
		int n=fs.nextInt(), q=fs.nextInt();
		char[] line=fs.next().toCharArray();
		int[] a=new int[n];
		for (int i=0; i<n; i++) a[i]=line[i]-'0';
		Treap t=null;
		for (int i:a) {
			Data data=new Data();
			if (i==0) {
				data.zeroPrefix=data.zeroSuffix=data.nZeros=1;
			}
			else {
				data.onePrefix=data.oneSuffix=data.nOnes=1;
			}
			data.width=1;
			data.biggestRange=1;
			t=Treap.merge(t, new Treap(data));
		}
		PrintWriter out=new PrintWriter(System.out);
		for (int qq=0; qq<q; qq++) {
			int type=fs.nextInt(), l=fs.nextInt()-1, r=fs.nextInt()-1;
			if (l>r) throw null;
			Treap[] parts1=Treap.split(t, l);
			Treap[] parts2=Treap.split(parts1[1], r-l+1);
			Treap middle=parts2[0];
			if (type==1) {
				//range ^= 1
				middle.subtreeData.invertMe();
			}
			else if (type==2) {
				//range mirror
				middle.subtreeData.mirrorMe();
			}
			else if (type==3) {
				//range sort
				int nZeros=middle.subtreeData.nZeros();
				Treap[] middleParts=Treap.split(middle, nZeros);
				if (middleParts[0] != null) middleParts[0].subtreeData.rangeSetZero();
				if (middleParts[1] != null) middleParts[1].subtreeData.rangeSetOne();
				middle=Treap.merge(middleParts[0], middleParts[1]);
			}
			else throw null;
			t=Treap.merge(parts1[0], Treap.merge(middle, parts2[1]));
			if (t.subtreeSize != n) throw null;
			out.println(t.subtreeData.biggestRange());
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
		int priority;
		Data data, subtreeData;
		Treap[] kids=new Treap[2];
		
		int subtreeSize;
		
		public Treap(Data data) {
			this.data=data;
			priority=rand.nextInt();
			this.subtreeData=new Data(); // fill in in recalc
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
			if (me.subtreeData.rangeSetOneProp || me.subtreeData.rangeSetZeroProp || me.subtreeData.invertProp || me.subtreeData.mirrorProp) throw null;
			me.subtreeSize=1;
			
			Data subtreeData=me.subtreeData, data=me.data;
			subtreeData.nZeros=data.nZeros();
			subtreeData.nOnes=data.nOnes();
			subtreeData.zeroPrefix=data.zeroPrefix();
			subtreeData.zeroSuffix=data.zeroSuffix();
			subtreeData.onePrefix=data.onePrefix();
			subtreeData.oneSuffix=data.oneSuffix();
			subtreeData.biggestRange=data.biggestRange();
			subtreeData.width=data.width;
			if (data.width!=1) throw null;
			if (me.kids[0]!=null) subtreeData=SneetchesAndSpeechesSolution.merge(me.kids[0].subtreeData, subtreeData);
			if (me.kids[1]!=null) subtreeData=SneetchesAndSpeechesSolution.merge(subtreeData, me.kids[1].subtreeData);
			me.subtreeData=subtreeData;
			for (Treap t:me.kids) {
				me.subtreeSize+=size(t);
			}
			if (me.subtreeSize != me.subtreeData.width) throw null;
		}
		
		static void prop(Treap me) {
			if (me==null) return;
			
			if (me.subtreeData.invertProp) {
				me.subtreeData.invertProp=false;
				me.data.invertMe();
				for (Treap t:me.kids) if (t!=null) t.subtreeData.invertMe();
			}
			
			if (me.subtreeData.rangeSetOneProp) {
				me.subtreeData.rangeSetOneProp=false;
				me.data.rangeSetOne();
				for (Treap t:me.kids) if (t!=null) t.subtreeData.rangeSetOne();
			}
			
			if (me.subtreeData.rangeSetZeroProp) {
				me.subtreeData.rangeSetZeroProp=false;
				me.data.rangeSetZero();
				for (Treap t:me.kids) if (t!=null) t.subtreeData.rangeSetZero();
			}
			
			if (me.subtreeData.mirrorProp) {
				me.subtreeData.mirrorProp=false;
				me.data.mirrorMe();
				Treap temp=me.kids[0];
				me.kids[0]=me.kids[1];
				me.kids[1]=temp;
				for (Treap t:me.kids) if (t!=null) t.subtreeData.mirrorMe();
			}
			
			recalc(me);
		}
		
		static int size(Treap t) {
			return t==null?0:t.subtreeSize;
		}
	}
	
	static class Data {
		//aggregate values (prop not applied yet)
		int nZeros, nOnes, width, zeroPrefix, zeroSuffix, onePrefix, oneSuffix, biggestRange;
		
		//prop values
		boolean rangeSetZeroProp;
		boolean rangeSetOneProp; //only one of those can be true
		boolean invertProp; 
		boolean mirrorProp;
		
		void mirrorMe() {
			mirrorProp^=true;
		}
		
		void invertMe() {
			if (rangeSetZeroProp || rangeSetOneProp) {
				if (rangeSetZeroProp) rangeSetOne();
				else rangeSetZero();
			}
			else {
				invertProp^=true;
			}
		}
		
		void rangeSetZero() {
			invertProp=false;
			mirrorProp=false;
			rangeSetZeroProp=true;
			rangeSetOneProp=false;
		}
		
		void rangeSetOne() {
			invertProp=false;
			mirrorProp=false;
			rangeSetZeroProp=false;
			rangeSetOneProp=true;
		}
		
		int nZeros() {
			if (rangeSetZeroProp) return width;
			if (rangeSetOneProp) return 0;
			if (invertProp) return nOnes;
			return nZeros;
		}
		
		int nOnes() {
			if (rangeSetZeroProp) return 0;
			if (rangeSetOneProp) return width;
			if (invertProp) return nZeros;
			return nOnes;
		}
		
		int zeroPrefix() {
			if (rangeSetZeroProp) return width;
			if (rangeSetOneProp) return 0;
			if (mirrorProp) {
				if (invertProp) return oneSuffix;
				else return zeroSuffix;
			}
			else {
				if (invertProp) return onePrefix;
				else return zeroPrefix;
			}
		}
		
		int onePrefix() {
			if (rangeSetZeroProp) return 0;
			if (rangeSetOneProp) return width;
			if (mirrorProp) {
				if (invertProp) return zeroSuffix;
				else return oneSuffix;
			}
			else {
				if (invertProp) return zeroPrefix;
				else return onePrefix;
			}
		}
		
		int zeroSuffix() {
			if (rangeSetZeroProp) return width;
			if (rangeSetOneProp) return 0;
			if (mirrorProp) {
				if (invertProp) return onePrefix;
				else return zeroPrefix;
			}
			else {
				if (invertProp) return oneSuffix;
				else return zeroSuffix;
			}
		}
		
		int oneSuffix() {
			if (rangeSetZeroProp) return 0;
			if (rangeSetOneProp) return width;
			if (mirrorProp) {
				if (invertProp) return zeroPrefix;
				else return onePrefix;
			}
			else {
				if (invertProp) return zeroSuffix;
				else return oneSuffix;
			}
		}
		
		int biggestRange() {
			if (rangeSetZeroProp) return width;
			if (rangeSetOneProp) return width;
			//don't care about mirror or invert
			return biggestRange;
		}
	}
	
	
	
	//assumes that there is nothing more to prop!!
	static Data merge(Data lChild, Data rChild) {
		
		Data toReturn=new Data();
		//need to set nZeros, nOnes, width, zeroPrefix, zeroSuffix, onePrefix, oneSuffix, biggestRange;
		toReturn.nZeros=lChild.nZeros()+rChild.nZeros();
		toReturn.nOnes=lChild.nOnes()+rChild.nOnes();
		toReturn.width=lChild.width+rChild.width;
		
		if (lChild.zeroPrefix() == lChild.width) toReturn.zeroPrefix=lChild.zeroPrefix() + rChild.zeroPrefix();
		else toReturn.zeroPrefix = lChild.zeroPrefix();
	
		if (lChild.onePrefix() == lChild.width) toReturn.onePrefix=lChild.onePrefix() + rChild.onePrefix();
		else toReturn.onePrefix = lChild.onePrefix();
		
		if (rChild.zeroSuffix() == rChild.width) toReturn.zeroSuffix=rChild.zeroSuffix() + lChild.zeroSuffix();
		else toReturn.zeroSuffix = rChild.zeroSuffix();
		
		if (rChild.oneSuffix() == rChild.width) toReturn.oneSuffix=rChild.oneSuffix() + lChild.oneSuffix();
		else toReturn.oneSuffix = rChild.oneSuffix();

		toReturn.biggestRange=Math.max(lChild.biggestRange(), rChild.biggestRange());
		toReturn.biggestRange=Math.max(toReturn.biggestRange, lChild.zeroSuffix()+rChild.zeroPrefix());
		toReturn.biggestRange=Math.max(toReturn.biggestRange, lChild.oneSuffix()+rChild.onePrefix());
		
		return toReturn;
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
