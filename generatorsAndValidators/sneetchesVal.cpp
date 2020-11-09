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

	string line=inf.readToken();
	ensure(line.size() == n);
	inf.readEoln();
	for (int qq=0; qq<q; qq++) {
		int qType=inf.readInt(1, 3, "qType");
		inf.readSpace();
		int l=inf.readInt(1, n, "l");
		inf.readSpace();
		int r=inf.readInt(l, n, "r");
		inf.readEoln();
	}

	inf.readEof();
	return 0;
}
