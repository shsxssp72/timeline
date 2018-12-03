#include <iostream>
#include <algorithm>
#include "SpecializedForTimeline.h"
using namespace std;


int main()
{

	SpecializedForTimeline instance;

	ofstream fout;
	fout.open("output.sql");
	auto backup=cout.rdbuf();
	cout.rdbuf(fout.rdbuf());
	cout<<"USE timeline;"<<endl;
	instance.UserInfo(5);
	instance.Content(20);
	cout.rdbuf(backup);
	fout.close();
	
	
	return 0;
}