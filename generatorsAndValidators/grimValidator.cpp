#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerValidation();
	//inf.readInt(1, 10, "testCount");
	//inf.readSpace9);
	//inf.readEoln();
	//inf.readEof();
	int n=inf.readInt(1, 300000, "n");
	inf.readSpace();
	int q=inf.readInt(1, 300000, "q");
	inf.readEoln();
	for (int i=0; i<n; i++) {
		if (i!=0) inf.readSpace();
		inf.readInt(1, 1000000, "a_i");
	}
	inf.readEoln();

	for (int qq=0; qq<q; qq++) {
		int qType=inf.readInt(1, 3, "qType");
		inf.readSpace();
		int l=inf.readInt(1, n, "l");
		inf.readSpace();
		int r=inf.readInt(l, n, "r");
		if (qType==1) {
			inf.readSpace();
			int h=inf.readInt(1, 1000000000, "h");
		}
		else if (qType==2) {
		}
		else if (qType==3) {
			inf.readSpace();
			int x=inf.readInt(1, 1000000000, "x");
		}
		inf.readEoln();
	}

	inf.readEof();
	return 0;
}
