//
// Created by 星落_月残 on 2018/12/2.
//

#ifndef DATAGENERATOR_SPECIALIZEDFORTIMELINE_H
#define DATAGENERATOR_SPECIALIZEDFORTIMELINE_H

#include <iostream>
#include <vector>
#include <ctime>
#include <random>
#include <fstream>
#include <sstream>
#include <map>
#include <algorithm>
#include "NameData.h"


using namespace std;

class SpecializedForTimeline
{
public:
	SpecializedForTimeline()
	{
		getRandom.seed(time(nullptr));
	}
	string getNumberString(int digit)
	{
		stringstream ss;
		long long j=1;
		for(int i=0;i<digit;i++)
			j*=10;
		ss<<getRandom()%j;
		return ss.str();
	}

	string getNumberArea(int bottom,int top,int digit)
	{
		stringstream ss;
		int gap=top-bottom;
		ss<<(getRandom()%gap+bottom);
		string result=ss.str();
		for(;result.length()<digit;)
			result="0"+result;
		return result;
	}
	string getCharacterString(int digit)
	{
		string result;
		for(int i=0;i<digit;i++)
			result+=Alphabeta[getRandom()%Alphabeta.size()];
		return result;
	}
	string getDateString(bool isNew)
	{
		string result,year,month,day;
		if(isNew)
			year="20";
		else
			year="19";
		year+=getNumberArea(0,100,2);
		month=getNumberArea(1,13,2);
		int month_num=stoi(month);
		switch(month_num)
		{
			case 2:
				day=getNumberArea(1,29,2);
				break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				day=getNumberArea(1,32,2);
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day=getNumberArea(1,31,2);
				break;
			default:
				day="01";
				break;
		}
		result=year+month+day;
		return result;
	}
	string getTimeString()
	{
		string result=getNumberArea(0,24,2)+getNumberArea(0,60,2)+getNumberArea(0,60,2);
		return result;
	}

	void makeUnique(vector<string> &input)
	{
		sort(input.begin(),input.end());
		auto new_end=unique(input.begin(),input.end());
		input.erase(new_end,input.end());
	}

	string getCharAndNumString(int digit)
	{
		string result;
		for(;result.size()<digit;)
		{
			result+=getCharacterString(getRandom()%2);
			result+=getNumberString(getRandom()%2);
		}
		result=result.substr(0,static_cast<unsigned long long int>(digit));
		return result;
	}

	void UserInfo(int number)
	{
		for(int i=0;i<number;i++)
		{
			user_id.push_back(to_string(i));
			string username=usernameList[getRandom()%usernameList.size()];
			string display_name=Lastname[getRandom()%Lastname.size()]
								+FirstName[getRandom()%FirstName.size()]
								+FirstName[getRandom()%FirstName.size()];
			string user_password=getCharAndNumString(10);
			string salt=getCharAndNumString(128);
			string last_login=getDateString(true)+getTimeString();
			cout<<"INSERT INTO UserInfo"<<endl<<"VALUES ('"<<i<<"','"<<username
				<<"','"<<display_name<<"','"<<user_password<<"','"<<salt<<"',"
				<<last_login<<");"<<endl;
		}
	}
	void Content(int number)
	{
		for(int i=0;i<number;i++)
		{
			string user_id_loc=user_id[getRandom()%user_id.size()];
			string publish_time=getDateString(true)+getTimeString();
			string content=contentList[getRandom()%contentList.size()];
			cout<<"INSERT INTO Content"<<endl<<"VALUES ('"<<i<<"','"<<user_id_loc
				<<"',"<<publish_time<<",'"<<content<<"');"<<endl;
		}
	}

private:
	vector<string> user_id;
	default_random_engine getRandom;
};


#endif //DATAGENERATOR_SPECIALIZEDFORTIMELINE_H
