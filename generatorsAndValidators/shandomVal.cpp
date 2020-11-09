#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerValidation(argc, argv);
	//inf.readInt(1, 10, "testCount");
	//inf.readSpace9);
	//inf.readEoln();
	//inf.readEof();
	int n=inf.readInt(1, 500000, "n");
	inf.readEoln();
	for (int i=0; i<n; i++) {
		inf.readInt(1, n, "a");
		inf.readSpace();
		inf.readInt(1, n, "b");
		inf.readEoln();
	}

	inf.readEof();
	return 0;
}
