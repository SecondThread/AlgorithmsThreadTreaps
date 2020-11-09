#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerGen(argc, argv, 1);
	// rnd.next(1, n)
	
	int n=atoi(argv[1]);
	cout<<n<<endl;
	for (int i=0; i<n; i++) {
		int l=rnd.next(1, n), r=rnd.next(1, n);
		cout<<l<<" "<<r<<endl;
	}

	return 0;
}
