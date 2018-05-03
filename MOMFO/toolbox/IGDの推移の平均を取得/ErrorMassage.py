#coding: UTF-8

import glob;
import sys


def Assertion(bool,str):
	if(not bool):
		ErrorMassage(str);


def ErrorMassage(str):
	sys.stderr.write(str);
	sys.stderr.write('\ninput any key to finish this program\n');		
	input();
	sys.exit(-1);
