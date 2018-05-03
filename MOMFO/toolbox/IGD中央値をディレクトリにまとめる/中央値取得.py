#!/usr/bin/env python
# -*- coding: utf-8 -*-

import glob
import numpy as np;
from subprocess import call
import os;
import shutil

#######Global変数#######
########################
MainDIR = "ParatoData";
Tasks = ["Task1","Task2"]; ##タスク番号
PROBLEMSET = ["CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"];
########################
########################


def getAllDirectoryName(dir,target):	
    return glob.iglob(dir+'/**/' + target, recursive=True);
"""
中央値の番号を返す
arg1:　IGDHistoryのファイルパス
return: IGD中央値の番号を返す

"""

def getMedian(targetdir):
    FILES_ = glob.glob(targetdir+"/*.dat");
    data_ = np.array([]);
    index = []
    for f in FILES_:
        if ( "STD" in f):
            continue;
        if ( "average" in f):
            continue;
        
        index.append( f.split("\\")[-1].split("IGD")[1].split(".")[0] );
        last = 0;
        with open(f,"r") as fin:
#            print(f);
            data = fin.readlines();
            last = data[len(data)-1].replace("\n","").split("\t")[1];
            data_ = np.append(data_,last);
#            print(last);
#            input();
    
    data_ = data_.astype(np.float32);
    median = np.median(data_);    
    count = -1;
    for i in data_:
        count = count + 1;
        if (median == i):
            print(str(median) + "    " + str(index[count]));
            break;
    return index[count];
    
#    return index[count];

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

def filesubscript(targetfile):
    FILES = glob.glob(targetfile + "/*");

    index = getMedian(targetfile);	

    print(file)
    FINALFUN = file.replace("IGDHistory","FinalFUN");
    FINALFUN = FINALFUN + "\\FinalFUN" + str(index) + ".dat";
    PFDATA = "Gnuplot" + "\\PF\\"+getPFDetaSet(FINALFUN)+".pf";
    OUTPUTFILE = FINALFUN.replace("FinalFUN" + str(index) + ".dat","Graph.emf");

#   plot(FINALFUN,PFDATA,OUTPUTFILE);

def Make_Directory():
   
#   ["CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"];　##問題名

    if (not os.path.exists(MainDIR)):
        os.mkdir(MainDIR );

    for prob in PROBLEMSET:
        if(not os.path.exists(MainDIR + "\\" + prob)):
            os.mkdir(MainDIR + "\\" + prob);

        for t in Tasks:
            if(not os.path.exists(MainDIR + "\\" + prob+"\\" + t)):
                os.mkdir(MainDIR + "\\" + prob+"\\"+t);
    

##かえり値
def getDetailInformation(file):
    problem = "null";
    for p in PROBLEMSET:
        if(p in file):
            problem = p;
            break;
    task = "null";
    for t in Tasks:
        if(t in file):
            task = t;
 

    return problem,task

def CopyToFile(targetfile,algorithmname):
    FILES = glob.glob(targetfile + "/*");

    index = getMedian(targetfile);	
    
    print(file)
    FINALFUN = targetfile.replace("IGDHistory","FinalFUN");
    FINALFUN = FINALFUN + "\\FinalFUN" + str(index) + ".dat";
    PFDATA = "Gnuplot" + "\\PF\\"+getPFDetaSet(FINALFUN)+".pf";
    OUTPUTFILE = FINALFUN.replace("FinalFUN" + str(index) + ".dat","Graph.emf");
    p,task = getDetailInformation(file);
    shutil.copy2(FINALFUN,MainDIR + "\\" + p+"\\"+task+"\\"+algorithmname+".dat");
    print(FINALFUN)    
    print(MainDIR + "\\" + p+"\\"+task+"\\"+algorithmname+".dat");


    

if __name__ == "__main__":
    DIRECTORY = "IGDHistory"; #評価指標値が入っているもの
    
    Algorithm = "PBIFormin5_0"; #取得するIGDの最終値
    
    FILES = getAllDirectoryName("result",DIRECTORY); ## ファイル全取得
    Make_Directory();
    for file in FILES:
        if ( not Algorithm in file):
            continue;
#        filesubscript(file);        
        CopyToFile(file,Algorithm);
#        input();
    