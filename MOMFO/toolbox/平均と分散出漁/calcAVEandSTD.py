from GetFileName import*
import numpy as np;
import glob
import os

def getAveAndSTD(targetdir):
	FILES_ = glob.glob(targetdir+"/*.dat");
	#print(targetdir);
	data_ = np.array([]);
	for f in FILES_:
		if ("average" in f):
			continue;
		with open(f,"r") as fin:
			data = fin.readlines();
			last = data[len(data)-1].replace("\n","").split("	")[1];
#			print(last);
			data_ = np.append(data_,last);
	data_ = data_.astype(np.float32);
	return np.array([ [np.mean(data_),np.std(data_)] ]);

def SubscriptAVEandSTD(dir,targetdir):
    
    DIRECTORY = getAllFileName(dir,targetdir);
    for dir in DIRECTORY:
        print("FUN");
        ret =  getAveAndSTD(dir);
        print(ret);
        np.savetxt(dir + "\\AVE_STD.dat",ret,delimiter='\t');

if __name__ =="__main__":
#    print("delete")
#    DIRECTORY = getAllFileName("result","average.dat");    
#    for file in DIRECTORY:
#        os.remove(file);
    DIRECTORY = getAllFileName("result","AVE_STD.dat");    
    for file in DIRECTORY:
        os.remove(file);
#    print("FILES")
    SubscriptAVEandSTD("result","IGDHistory");
#    DIRECTORY = getAllFileName("result","average.dat");    
    #print("file")
#    for file in DIRECTORY:
    #    print("file");
#        Tolog(file,"   ");
    print("end");
    input();
