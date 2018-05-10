package momfo.util.Dominator;

import java.util.ArrayList;
import java.util.List;

import lib.math.BuildInRandom;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;

public class nonDominatedSort extends Dominance{

	public nonDominatedSort(double[] ref,boolean isMax_,BuildInRandom random_){
		super(ref,isMax_,random_);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	int worstRank_;


	public Population[] fastNondominatedSort(Population pop) throws JMException{
		int[] perm = pop.sortObjectivereturnperm(0);
		List< List<Integer> > PopSet = new ArrayList< List<Integer> >();
		List<Integer> nowlist = new ArrayList<Integer>();


		int size = pop.size();

		nowlist.add(perm[0]);
		PopSet.add(nowlist);

		for(int i=0;i<size;i++){
			int n = perm[i];
			Solution nowsol = pop.get(i);

		}




		return null;


	}

	public static void main(String[] args){


	}

	public Population setConfig() throws JMException{
/*		Population ret = new Population();
		Solution a = new Solution(2);
		System.out.println();
		comparator = new SMSEmomfoAComparatorContribution(null);
		comparator.setIsMAX();;
		//a.setObjective(0, 18);
		//a.setObjective(1, 18);
		//ret.add(new Solution(a));
		a.setObjective(0, 12);
		a.setObjective(1, 18);
		ret.add(new Solution(a));
		a.setObjective(0, 16);
		a.setObjective(1, 16);
		ret.add(new Solution(a));
		a.setObjective(0, 18);
		a.setObjective(1, 12);
		ret.add(new Solution(a));
		a.setObjective(0,10);
		a.setObjective(1, 16);
		ret.add(new Solution(a));
		a.setObjective(0,14 );
		a.setObjective(1, 14);
		ret.add(new Solution(a));
		a.setObjective(0,15 );
		a.setObjective(1, 9);
		ret.add(new Solution(a));
		a.setObjective(0,18 );
		a.setObjective(1, 6);
		ret.add(new Solution(a));
		a.setObjective(0,4 );
		a.setObjective(1, 14);
		ret.add(new Solution(a));
		a.setObjective(0,9 );
		a.setObjective(1, 13);
		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 11);
		ret.add(new Solution(a));
		a.setObjective(0,16 );
		a.setObjective(1, 4);
		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 12);
		ret.add(new Solution(a));
		a.setObjective(0,8 );
		a.setObjective(1, 10);
		ret.add(new Solution(a));
		a.setObjective(0,12 );
		a.setObjective(1, 8);
		ret.add(new Solution(a));
		a.setObjective(0,14 );
		a.setObjective(1, 4);
		ret.add(new Solution(a));
		a.setObjective(0,16 );
		a.setObjective(1, 0);
		ret.add(new Solution(a));
		a.setObjective(0,2 );
		a.setObjective(1, 8);
		ret.add(new Solution(a));
		a.setObjective(0, 6 );
		a.setObjective(1, 6);
		ret.add(new Solution(a));
		a.setObjective(0,11 );
		a.setObjective(1, 1);
		ret.add(new Solution(a));
		a.setObjective(0,10 );
		a.setObjective(1, 2);
		ret.add(new Solution(a));

		ret.AssignID();

		double [] referencePoint  = new double[2];
		referencePoint[0] = 1000;		referencePoint[1] = 1000;

		fastNondominatedSort(ret);
*/	return null;
	}


	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
