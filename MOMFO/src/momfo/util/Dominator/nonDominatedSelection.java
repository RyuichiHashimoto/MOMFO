package momfo.util.Dominator;

import momfo.core.Population;
import momfo.util.JMException;

public class nonDominatedSelection extends Dominance{


	public nonDominatedSelection(double[] ref) {
		super(ref);
		// TODO 自動生成されたコンストラクター・スタブ
	}
/*
	//目的関数空間上で重複個体ならtrue
	private static boolean samePoint(Solution d , Solution b){
		return samePoint(new POINT(d), new POINT(b));
	}

	private static boolean samePoint(POINT d , POINT b){
		for(int i=0;i<d.getDimension();i++){
			if(Math.abs(d.get(i) - b.get(i)) > 1.0E-14){
				return false;
			}
		}
		return true;
	}

	public static Population remomfoveSamePoint(Population d){
		for(int i=0;i<d.size()-1;i++){
			for(int j=i+1;j<d.size();j++){
				if( samePoint(d.get(i),d.get(j)) ){
					d.remomfove(j);
					j--;
				}
			}
		}
		return d;
	}

	public static Front remomfoveSamePoint(Front d){
		for(int i=0;i<d.size()-1;i++){
			for(int j=i+1;j<d.size();j++){
				if( samePoint(d.get(i),d.get(j)) ){
					d.remomfove(j);
					j--;
				}
			}
		}
		return d;
	}




	public static Front nonDominatedSelectionWithoutSamePoint(Front pop,boolean isMAX){
		isMAXProblem_ = isMAX;

		Front ret = new Front();
		int size  = pop.size();
		List<Integer> li = new ArrayList<Integer>();

		li.add(0);

		for(int p=1;p<size;p++){

			for(int j=0;j<li.size();j++){
				if(isDominance(pop.get(p), pop.get(li.get(j)))){
					li.remomfove(j);
					j--;
				}
			}

			boolean d = true;
			if (li.size() != 0){
				for(int j=0;j<li.size();j++){
					int a = isDominancedOrEqual(pop.get(li.get(j)),pop.get(p));
					if(a == 0 ||a == 1){
						d = false;
					}
				}
			}
			if(d)
				li.add(p);
		}
		for(int i=0;i<li.size();i++){
			ret.add(pop.get(li.get(i)));
		}
		return ret;

	}

	public static Front nonDominatedSelection(Front pop,boolean isMAX){
		isMAXProblem_ = isMAX;

		Front ret = new Front();
		int size  = pop.size();
		List<Integer> li = new ArrayList<Integer>();

		li.add(0);

		for(int p=1;p<size;p++){

			for(int j=0;j<li.size();j++){
				if(isDominance(pop.get(p), pop.get(li.get(j)))){
					li.remomfove(j);
					j--;
				}
			}

			boolean d = true;
			if (li.size() != 0){
				for(int j=0;j<li.size();j++){
					if(isDominance(pop.get(li.get(j)),pop.get(p))){
						d = false;
					}
				}
			}
			if(d)
				li.add(p);
		}
		for(int i=0;i<li.size();i++){
			ret.add(pop.get(li.get(i)));
		}
		return ret;
	}


	public static Population nonDominatedSelectionWithoutSamePoint(Population pop,boolean isMAX){
		return remomfoveSamePoint(nonDominatedSelection(pop, isMAX));
	}

	public static Population nonDominatedSelection(Population pop,boolean isMAX){
		isMAXProblem_ = isMAX;

		Population ret = new Population();
		int size  = pop.size();
		List<Integer> li = new ArrayList<Integer>();

		if (li.size() == 0){
			li.add(0);
		}

		for(int p=1;p<size;p++){

			for(int j=0;j<li.size();j++){
				if(isDominance(pop.get(p), pop.get(li.get(j)))){
					li.remomfove(j);
					j--;
				}
			}

			boolean d = true;
			if (li.size() != 0){
				for(int j=0;j<li.size();j++){
					if(isDominance(pop.get(li.get(j)),pop.get(p))){
						d = false;
					}
				}
			}
			if(d)
				li.add(p);
		}
		for(int i=0;i<li.size();i++){
			ret.add(pop.get(li.get(i)));
		}
		return ret;
	}

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
*/

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
