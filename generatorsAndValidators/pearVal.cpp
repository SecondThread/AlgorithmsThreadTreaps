#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerValidation(argc, argv);
	//inf.readInt(1, 10, "testCount");
	//inf.readSpace9);
	//inf.readEoln();
	//inf.readEof();
	int n=inf.readInt(1, 300000, "n");
	inf.readSpace();
	int q=inf.readInt(1, 300000, "q");
	inf.readEoln();
	string line=inf.readWord();
	inf.readEoln();
	ensure(n==line.size());
	for (int qq=0; qq<q; qq++) {
		int type=inf.readInt(1, 3, "qType");
		inf.readSpace();
		if (type==1) {
			ensure(n>0);
			int l=inf.readInt(1, n, "l");
			inf.readSpace();
			int r=inf.readInt(l, n, "r");
			n-=(r-l+1);
		}
		else if (type==2) {
			string line=inf.readWord();
			inf.readSpace();
			int posAt=inf.readInt(1, n+1, "pos");
			n++;
		}
		else if (type==3) {
			ensure(n>0);
			int l=inf.readInt(1, n, "l");
			inf.readSpace();
			int r=inf.readInt(l, n, "r");
		}
		inf.readEoln();
	}
	


	inf.readEof();
	return 0;
}
