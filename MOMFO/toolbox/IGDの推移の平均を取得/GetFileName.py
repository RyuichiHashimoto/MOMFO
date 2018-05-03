#coding: UTF-8

import glob;
from ErrorMassage import *
import os;
from copy import deepcopy

def getDatFileName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	return glob.glob(dir+"/*.dat");
def getCSVFileName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	return glob.glob(dir+"/*.csv");
def getMTDFileName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	return glob.glob(dir+"/*.mtd");	
def getTSVFileName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	return glob.glob(dir+"/*.tsv");

def getALLFileName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	return glob.glob(dir+"/*");


# true maens variables'dir' is file name, false means variables'dir' is DirectoryName  
def FileJudge(dir):
	return not os.path.isdir(dir);

def getSubDirectoryName(dir):
	Assertion(os.path.exists(dir), "there are not " + dir+" Directory ");	
	Files = glob.glob(dir+"/*");
	ret = [];
	if len(Files) == 0:
		return ret;

	for file in Files:
		if not FileJudge(file):
			ret.append(file);

	return ret;



def getAllDirectoryName(dir,target):	
	return glob.iglob(dir+'/**/' + target, recursive=True);

def getAllFileName(dir,target):
	return glob.iglob(dir+'/**/' + target, recursive=True);

def getAllSpecificFileName(dir,target):
	return glob.iglob(dir+'/**/*.' + target, recursive=True);

