#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerGen(argc, argv, 1);
	// rnd.next(1, n)
	
	int n=atoi(argv[1]);
	if (n==-1) {
		n=150000;
		int q=n;
		cout<<n<<" "<<q<<endl;
		for (int i=0; i<n; i++) {
			if (i!=0) cout<<" ";
			cout<<999999*(i&1)+1;
		}
		cout<<endl;
		for (int qq=0; qq<q; qq++) {
			cout<<"1 1 "<<n<<" "<<(1000000-qq)<<endl;
		}

	}
	else {
		int q=n;
		cout<<n<<" "<<q<<endl;
		for (int i=0; i<n; i++) {
			if (i!=0) cout<<" ";
			cout<<rnd.next(1, 1000000);
		}
		cout<<endl;
		for (int qq=0; qq<q; qq++) {
			int type=rnd.next(1, 3);
			cout<<type<<" ";
			int l=rnd.next(1, n), r=rnd.next(1, n);
			if (l>r) swap(l, r);
			if (type==1) {
				int h=rnd.next(1, 1000000000);
				if (n<20)
					h=rnd.next(1, 1000000);
				cout<<l<<" "<<r<<" "<<h<<endl;
			}
			else if (type==2) {
				cout<<l<<" "<<r<<endl;	
			}
			else if (type==3) {
				int x=rnd.next(1, 100000);
				cout<<l<<" "<<r<<" "<<x<<endl;
			}
			else return 1;
		}
	}

	return 0;
}
