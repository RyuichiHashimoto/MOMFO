package momfo.util.Ranking;

import lib.math.BuiltInRandom;
import momfo.core.Population;
import momfo.core.Solution;

public class test {

	public Population setConfig(){
		Population ret = new Population(100);

		Solution a = new Solution(2);
		System.out.println();
		a.setFeasible(true);


		a.setObjective(0, 18);
		a.setObjective(1, 18);
		a.setConstrain(0, 1);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 18);
		a.setObjective(1, 18);
		a.setConstrain(0, 2);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 12);
		a.setObjective(1, 18);

		a.setConstrain(0, 3);
		a.calctotalCalc();
		ret.add(new Solution(a));
		a.setObjective(0, 16);
		a.setObjective(1, 16);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 18);
		a.setObjective(1, 12);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,10);
		a.setObjective(1, 16);
		a.setConstrain(0, 6);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,14 );
		a.setObjective(1, 14);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,15 );
		a.setObjective(1, 9);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();



		ret.add(new Solution(a));
		a.setObjective(0,18 );
		a.setObjective(1, 6);
		a.setConstrain(0, 9);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,4 );
		a.setObjective(1, 14);
		a.setConstrain(0, 10);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,9 );
		a.setObjective(1, 13);
		a.setConstrain(0, 11);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 11);
		a.setConstrain(0, 12);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,16 );
		a.setObjective(1, 4);
		a.setConstrain(0, 13);
		a.calctotalCalc();



		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 12);
		a.setConstrain(0, 14);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,8 );
		a.setObjective(1, 10);
		a.setConstrain(0, 15);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 8);
//		a.setConstrain(0, 16);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,14 );
		a.setObjective(1, 4);
//		a.setConstrain(0, 17);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,16 );
		a.setObjective(1, 0);
//		a.setConstrain(0, 0);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 8);
		//a.setConstrain(0, 0);
		//a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 6 );
		a.setObjective(1, 6);
		//a.setConstrain(0, 20);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,10 );
		a.setObjective(1, 2);
	//	a.setConstrain(0, 21);
	//	a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,19);
		a.setObjective(1,18);
		a.setSkillFactor(1);
	//	a.setConstrain(0, 21);
	//	a.calctotalCalc();

//		ret.add(new Solution(a));



		double[][] objectives =  {{10,2},{6,6},{2,8},{16,0},{14,4},{12,8},{8,10},{2,12},{16,4},{18,18},{18,18},{12,18},{16,16},{18,12},{10,16},{14,14},{15,9},{18,6},{4,14},{9,13},{12,11}};

		BuiltInRandom random = new BuiltInRandom(214);

		NDSRanking ranking = new NDSRanking(true,random);
		ret = new Population(objectives);
//		ranking.setPop(new Population(objectives));
		ranking.setPop(ret);
		ranking.Ranking();
		int worstrank = ranking.getworstrank();


		int k = 0;
		Population F;
		while(k < worstrank){
			F = ranking.get(k);
			CrowdingDistance(F);
			k = k + 1;
			for(int i=0;i<F.size();i++){
				System.out.println(F.get(i).getObjective(0) + "	" + F.get(i).getObjective(1) +"		"+  F.get(i).getRank()+"	" + F.get(i).getCrowdDistance_());
			}
			System.out.println();;
		}

		for(int i=0;i<ret.size();i++){
	//		System.out.println(ret.get(i).getObjective(0) + "	" + ret.get(i).getObjective(1) +"		"+  ret.get(i).getRank()+"	" + ret.get(i).getCrowdDistance_());
		}
	return ret;
	}

	public void CrowdingDistance(Population a){
		for(int i = 0;i< a.size();i++){
				a.get(i).setCrowedDistance(0.0);
		}
		Solution dd = a.get(0);
		int Objectives = dd.getNumberOfObjectives();

		double max,min;
		double em;
		for(int key = 0;key< Objectives; key++){
			 int [] e = a.sortObjectivereturnperm(key);
				a.get(e[0]).setCrowedDistance(1.0E25);
				a.get(e[a.size() - 1]).setCrowedDistance(1.0E25);
				max = a.get(e[a.size() - 1]).getObjective(key);
				min = a.get(e[0]).getObjective(key);
				if (max - min < 1.0E-14){
					continue;
				}
			for(int  n = 1;n < a.size() -1 ;n++){
					Solution sp = a.get(e[n]);
					em = sp.getCrowdDistance_();

					em = em + (a.get(e[n + 1]).getObjective(key)  - a.get(e[n - 1]).getObjective(key))/(max - min);

					sp.setCrowedDistance(em);
				}
			}
		}

	public void setConfigMultitask() {
		Population ret = new Population(100);

		Solution a = new Solution(2);
		System.out.println();
		a.setFeasible(true);
		double[][] d = {{18,18},{18,18},{12,18},{16,16},{18,12},{10,16},{14,14},{15,9}};

		a.setSkillFactor(0);
		a.setObjective(0, 18);
		a.setObjective(1, 18);
		a.setConstrain(0, 1);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 18);
		a.setObjective(1, 18);
		a.setConstrain(0, 2);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 12);
		a.setObjective(1, 18);
		a.setConstrain(0, 3);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0, 16);
		a.setObjective(1, 16);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();
		ret.add(new Solution(a));


		a.setObjective(0, 18);
		a.setObjective(1, 12);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,10);
		a.setObjective(1, 16);
		a.setConstrain(0, 6);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,14 );
		a.setObjective(1, 14);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,15 );
		a.setObjective(1, 9);
		a.setConstrain(0, 0.5);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,18 );
		a.setObjective(1, 6);
		a.setConstrain(0, 9);
		a.calctotalCalc();
		ret.add(new Solution(a));

		a.setObjective(0,4 );
		a.setObjective(1, 14);
		a.setConstrain(0, 10);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,9 );
		a.setObjective(1, 13);
		a.setConstrain(0, 11);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 11);
		a.setConstrain(0, 12);
		a.calctotalCalc();

		ret.add(new Solution(a));

		a.setObjective(0,13 );
		a.setObjective(1, 11);
	//	a.setConstrain(0, 21);
	//	a.calctotalCalc();
		a.setSkillFactor(1);
		ret.add(new Solution(a));
		a.setSkillFactor(0);

		a.setObjective(0,16 );
		a.setObjective(1, 4);
		a.setConstrain(0, 13);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 12);
		a.setConstrain(0, 14);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,8 );
		a.setObjective(1, 10);
		a.setConstrain(0, 15);
		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 8);
//		a.setConstrain(0, 16);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,14 );
		a.setObjective(1, 4);
//		a.setConstrain(0, 17);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,16 );
		a.setObjective(1, 0);
//		a.setConstrain(0, 0);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 8);
		//a.setConstrain(0, 0);
		//a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0, 6 );
		a.setObjective(1, 6);
		//a.setConstrain(0, 20);
//		a.calctotalCalc();

		ret.add(new Solution(a));
		a.setObjective(0,10 );
		a.setObjective(1, 2);
	//	a.setConstrain(0, 21);
	//	a.calctotalCalc();

		ret.add(new Solution(a));

		a.setObjective(0,18);
		a.setObjective(1,19);
	//	a.setConstrain(0, 21);
	//	a.calctotalCalc();
		a.setSkillFactor(1);
		ret.add(new Solution(a));
		a.setSkillFactor(0);

		BuiltInRandom random = new BuiltInRandom(214);
		NDSRanking ranking = new NDSRanking(true,random);

		ranking.setPop(ret);
		ranking.Ranking(0);
		int worstrank = ranking.getworstrank();


		int k = 0;
		Population F;
		while(k < worstrank){
			F = ranking.get(k);
			CrowdingDistance(F);
			k = k + 1;
			for(int i=0;i<F.size();i++){
				System.out.println(F.get(i).getObjective(0) + "	" + F.get(i).getObjective(1) +"		"+  F.get(i).getRank()+"	" + F.get(i).getCrowdDistance_());
			}
			System.out.println();;
		}

		for(int i=0;i<ret.size();i++){
	//		System.out.println(ret.get(i).getObjective(0) + "	" + ret.get(i).getObjective(1) +"		"+  ret.get(i).getRank()+"	" + ret.get(i).getCrowdDistance_());
		}
//	return ret;

	}
}
