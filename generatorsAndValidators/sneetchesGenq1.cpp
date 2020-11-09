#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerGen(argc, argv, 1);
	// rnd.next(1, n)
	
	int n=atoi(argv[1]);
	cout<<n<<" "<<n<<endl;
	for (int i=0; i<n; i++)
		cout<<rnd.next(0, 1);
	cout<<endl;
	for (int qq=0; qq<n; qq++) {
		int qType=rnd.next(1, 1);
		int l=rnd.next(1, n);
		int r=rnd.next(1, n);
		if (l>r) swap(l, r);
		cout<<qType<<" "<<l<<" "<<r<<endl;
	}


	return 0;
}
