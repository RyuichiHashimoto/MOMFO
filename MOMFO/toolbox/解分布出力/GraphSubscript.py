import numpy
import glob
import sys;
import openpyxl as px
from openpyxl.styles import Alignment
from subprocess import call
import os;
from openpyxl.styles import Border, Side
import string
import numpy as np
import glob 
import re

INDICATOR = "IGD";


def getAllDirectoryName(dir,target):
	return glob.iglob(dir+'/**/' + target, recursive=True);

def	worstIndex():
	return 1;

def getMedianIndex(targetfile):
	return 0;

def getPFDetaSet(filename):
	ret = "ret"
	CIRCLE = "circle";
	CONCAVE = "concave"
	CONVEX = "convex"
	SPHERE = "sphere"

	if("CIHS" in filename):
		if( "Task1" in filename):
			ret = CIRCLE;
		elif ("Task2" in filename):
			ret = CONCAVE;
	elif ("CIMS" in filename):
		if( "Task1" in filename):
			ret = CONCAVE;
		elif ("Task2" in filename):
			ret = CIRCLE;
	elif ("CILS" in filename):
		if( "Task1" in filename):
			ret = SPHERE;
		elif ("Task2" in filename):
			ret = CONVEX;
	elif ("PIHS" in filename):
		if( "Task1" in filename):
			ret = CONVEX;
		elif ("Task2" in filename):
			ret = CONVEX;
	elif ("PIMS" in filename):
		if( "Task1" in filename):
			ret = CIRCLE;
		elif ("Task2" in filename):
			ret = CONCAVE;
	elif ("PILS" in filename):
		if( "Task1" in filename):
			ret = CIRCLE;
		elif ("Task2" in filename):
			ret = CIRCLE;
	elif ("NIHS" in filename):
		if( "Task1" in filename):
			ret = CIRCLE;
		elif ("Task2" in filename):
			ret = CONVEX;
	elif ("NIMS" in filename):
		if( "Task1" in filename):
			ret = SPHERE;
		elif ("Task2" in filename):
			ret = CONCAVE;
	elif ("NILS" in filename):
		if( "Task1" in filename):
			ret = SPHERE;
		elif ("Task2" in filename):
			ret = CONCAVE;
			
	return ret;

def plot(targetfile,PFData,outputData):
	if ( (     (("NIMS") in targetfile) or  (("NILS")in targetfile) ) and "Task1" in targetfile):
		call("gnuplot -e \"DataFile=\'"+ targetfile+"\';PFData=\'" + PFData+"\';OutputFile=\'" + outputData + "graph.emf\'\" Gnuplot\\Sphere3d.plt");
	else:		
		call("gnuplot -e \"DataFile=\'"+ targetfile+"\';PFData=\'" + PFData+"\';OutputFile=\'" + outputData + "graph.emf\'\" Gnuplot\\Sphere2d.plt");

def getIndex(targetfile):
	return 10;

def filesubscript(targetfile):
	FILES = glob.glob(targetfile + "/*");

	index = getIndex(targetfile);	

	print(file)
	FINALFUN = file.replace("IGDHistory","FinalFUN");
	FINALFUN = FINALFUN + "\\FinalFUN" + str(index) + ".dat";
	PFDATA = "Gnuplot" + "\\PF\\"+getPFDetaSet(FINALFUN)+".pf";
	OUTPUTFILE = FINALFUN.replace("FinalFUN" + str(index) + ".dat","Graph.emf");

	plot(FINALFUN,PFDATA,OUTPUTFILE);


#	input();


if __name__ == "__main__":
	targetfilename = "IGDHistory";
	FILES = getAllDirectoryName("result",targetfilename);

	for file in FILES:
		filesubscript(file)
#		print(file)
	print("end")
	input();