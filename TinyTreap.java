import java.util.Random;

public class TinyTreap {

	static final Random rand=new Random(5);
	// lower priority on top, all methods return the new treap root
	// To add new seg-tree supported properties, edit recalc()
	// To add lazyprop values, edit recalc() and prop()

	// If you only add by merging, skip add() and recalc()
	// If you don't need lazyprop, skip prop() and rangeAdd()
	static class Treap {
		int data, priority;
		Treap[] kids=new Treap[2];
		
		int subtreeSize, sum, toProp;
		
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
		
		static int size(Treap t) {
			return t==null?0:t.subtreeSize;
		}
	}

}
