import glob
import sys
import os;


def getAllFileName(str,target):
	return glob.iglob(str+'/**/' + target, recursive=True);
	

def replacement(path):
    print("start")
    FILES = getAllFileName(path,"*.java");
  #  path = sys.argv[1]
    for p in FILES:
        print(p);
        if '.java' not in p:
           continue#
        print(p)
#
        cls = p.split('.')[0]#
#
        # 書き換え
        f_input = open(p)
        LINES = [];

        for line in f_input:
            LINES.append(line.replace("\\\\", "/"));
        f_input.close()
        
        f_output = open(p, 'w')#
        for line in LINES:
            f_output.write(line)#


        f_output.close()

if __name__ == '__main__':
    replacement("src")
    print("end")
    input();