#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerValidation(argc, argv);
	//inf.readInt(1, 10, "testCount");
	//inf.readSpace9);
	//inf.readEoln();
	//inf.readEof();
	vector<int> works(1000000, 0);
	int q=inf.readInt(1, 500000, "q");
	inf.readEoln();
	for (int qq=1; qq<=q; qq++) {
		int type=inf.readInt(1, 4, "type");
		inf.readSpace();
		if (type==1) {
			int val=inf.readInt(0, 100000, "val");
			works[qq]=1;
		}
		else if (type==2) {
			int y=inf.readInt(0, q, "y");
			inf.readSpace();
			int z=inf.readInt(0, q, "z");
			ensure(works[y] == 1);
			ensure(works[z] == 1);
		}
		else if (type==3) {
			int y=inf.readInt(0, q, "y");
			inf.readSpace();
			int z=inf.readInt(1, 1000000000, "z");
			ensure(works[y] == 1);
		}
		else if (type==4) {
			int y=inf.readInt(0, q, "y");
			ensure(works[y] == 1);
		}
		inf.readEoln();
	}

	inf.readEof();
	return 0;
}
