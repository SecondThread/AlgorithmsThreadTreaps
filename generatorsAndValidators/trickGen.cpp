#include "testlib.h"
#include <iostream>

using namespace std;

int main(int argc, char* argv[]) {
	registerGen(argc, argv, 1);
	// rnd.next(1, n)
	
	int q=atoi(argv[1]);
	cout<<q<<endl;
	vector<int> nodes;
	for (int qq=0; qq<q; qq++) {
		int type=rnd.next(1, 4);
		if (nodes.size()==0) type=1;
		cout<<type<<" ";
		if (type==1) {
			int val=rnd.next(0, 100000);
			nodes.push_back(qq+1);
			cout<<val<<endl;
		}
		else if (type==2) {
			int node1=nodes[rnd.next(0, (int)nodes.size()-1)], node2=nodes[rnd.next(0, (int)nodes.size()-1)];
			cout<<node1<<" "<<node2<<endl;
		}
		else if (type==3) {
			int node1=nodes[rnd.next(0, (int)nodes.size()-1)];
			int size=min(rnd.next(1, qq+1), rnd.next(1, qq+1));
			cout<<node1<<" "<<size<<endl;
		}
		else if (type==4) {
			int node1=nodes[rnd.next(0, (int)nodes.size()-1)];
			cout<<node1<<endl;
		}
	}


	return 0;
}
