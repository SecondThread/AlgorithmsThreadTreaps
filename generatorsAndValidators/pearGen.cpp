#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerGen(argc, argv, 1);
	// rnd.next(1, n)
	
	int n=atoi(argv[1]);
	int diffChars=atoi(argv[2]);
	int maxDeleteRange=atoi(argv[3]);

	if (diffChars<1 || diffChars>26) return 1;
	cout<<n<<" "<<n<<endl;
	if (rnd.next(0, 1)) {
		for (int i=0; i<n; i++)
			cout<<'a';
	}
	else {
		for (int i=0; i<n; i++)
			cout<<(char)('a'+rnd.next(0, diffChars-1));
	}
	cout<<endl;
	int on=n;
	for (int qq=0; qq<on; qq++) {
		int type=rnd.next(1, 3);
		cout<<type<<" ";
		if (n==0) type=2;
		if (type==1) {
			//delete
			int l=rnd.next(1, n);
			int r=rnd.next(l, min(l+maxDeleteRange-1, n));
			n-=(r-l+1);
			cout<<l<<" "<<r<<endl;
		}
		else if (type==2) {
			n++;
			char c=(char)('a'+rnd.next(0, diffChars-1));
			int pos=rnd.next(1, n);
			cout<<c<<" "<<pos<<endl;
		}
		else if (type==3) {
			int l=rnd.next(1, n);
			int r=rnd.next(l, n);
			cout<<l<<" "<<r<<endl;
		}
	}


	return 0;
}
