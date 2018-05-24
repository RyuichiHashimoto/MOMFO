package momfo.problems.MOMFOP.NTU;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.naming.NameNotFoundException;

import org.junit.jupiter.api.Test;

import lib.experiments.CommandSetting;
import lib.experiments.JMException;
import lib.lang.Generics;
import lib.math.BuildInRandom;
import momfo.core.ProblemSet;
import momfo.core.Solution;

class NTUProblemTest {

	@Test
	void test() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NameNotFoundException, ClassNotFoundException, JMException {

		String[] problemNameSet = {"CIHS","CIMS","CILS","PIHS","PIMS","PILS","NIHS","NIMS","NILS"};

		BuildInRandom random = new BuildInRandom(3);
		CommandSetting setting = new CommandSetting();
		for(int p = 0; p < problemNameSet.length;p++){
			String problemName = problemNameSet[p] ;
			ProblemSet problemSet = null;
			setting.putForce("Problem", "momfo.problems.MOMFOP.NTU." +problemName);
			System.out.println("");
			System.out.println(problemName + " is start");
			problemSet = (ProblemSet)Generics.getToClass("momfo.problems.MOMFOP.NTU." +problemName, "momfo.problems.MOMFOP.NTU.").getDeclaredConstructor(CommandSetting.class).newInstance(setting);
			Solution sol = new Solution(problemSet,0,random);
			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}
			problemSet.get(0).evaluate(sol);

			double[] vale = sol.getObjectives();
			System.out.print("Task1	is clear   ");

			if(!same(vale,hashTask1.get(problemName))) {
				fail(problemName+" Task1 is wrong");
			}

			System.out.print("Task2	is clear");

			sol = new Solution(problemSet.get(1),random);

			for(int val = 0 ; val < sol.getNumberOfVariables();val++){
				sol.setValue(val, 0.3);
			}

			problemSet.get(1).evaluate(sol);

			vale = sol.getObjectives();
			if(!same(vale,hashTask2.get(problemName))) {
				System.out.println(vale[0] + "	" + hashTask2.get(problemName)[0]);
				System.out.println(vale[1] + "	" + hashTask2.get(problemName)[1]);

				fail(problemName+" Task2 is wrong");
			}
			System.out.println();

		}
		System.out.println();
		System.out.println("all clear");
	}



	public static final HashMap<String,double[] > hashTask1 = new HashMap<String,double[]>(){
		{
			double[] a = new double[2];
			a[0]=69855.80250289223;a[1]=35593.3091700802;
			put("CIHS",a.clone());
			a[0]=0.3;a[1]=28872.9999968829;
			put("CIMS",a.clone());
			a[0]=330.5114505191884;a[1]=168.4039953888451;
			put("CILS",a.clone());
			a[0]=0.3;a[1]=78247.63670582567;
			put("PIHS",a.clone());
			a[0]=29.66282876017543;a[1]=15.113966157304652;
			put("PIMS",a.clone());
			a[0]=6.14794501689981;a[1]=3.1325344482029096;
			put("PILS",a.clone());
			a[0]=4.7692951420991745E9;a[1]=2.430077251049677E9;
			put("NIHS",a.clone());
			a = new double[3];
			a[0]=6997510.9197403835;a[1]=3565409.8967228928;a[2]=4001553.0750133186;
			put("NIMS",a.clone());
			a[0]=8.891597237855192;a[1]=4.530495079341588;a[2]=5.084693497018429;
			put("NILS",a.clone());

		}
	};


	public static final HashMap<String,double[] > hashTask2 = new HashMap<String,double[]>(){
		{
			double[] b = new double[2];
			b[0]=0.3;b[1]=360.99975069252076;
			put("CIHS",b.clone());
			b[0]=33.04743198214657;b[1]=16.83850763533979;
			put("CIMS",b.clone());
			b[0]=0.3;b[1]=3.609327645229244;
			put("CILS",b.clone());
			b[0]=0.3;b[1]=78346.73801420101;
			put("PIHS",b.clone());
			b[0]=0.3;b[1]=3172.955074683656;
			put("PIMS",b.clone());
			b[0]=18.710499316339224;b[1]=9.533475574422944;
			put("PILS",b.clone());
			b[0]=0.3;b[1]=50054.308924529934;
			put("NIHS",b.clone());
			b[0]=0.3;b[1]=5361.441583213471;
			put("NIMS",b.clone());
			b[0]=0.3;b[1]=20.989003663483427;
			put("NILS",b.clone());
		}
	};


	private boolean same(double[] a, double[] b) {
		for(int i = 0; i < a.length;i++) {
			if(Math.abs(a[i]-b[i]) > 1.0E-14) {
				return false;
			}
		}
		return true;
	}

}
