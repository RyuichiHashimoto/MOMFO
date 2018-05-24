package momfo.util.Ranking;

import java.util.ArrayList;
import java.util.List;

import lib.experiments.JMException;
import lib.math.BuildInRandom;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.Comparator.DominationComparator;


/*
 *
 *
 *
 *
 */

/***************************************************************************************************************
 * pleese read "An Efficient Approach to Non-Dominated Sorting for Evolutionary Multi-Objective-Optimization"
 * 																										   *
 ***************************************************************************************************************
 */
public class C_NDSRanking {
	String name;

	List<Population> object = new ArrayList<Population>();

	Population pop;

	boolean isMAX_;

	BuildInRandom random ;

	public C_NDSRanking(boolean s,BuildInRandom random_){
		isMAX_ = s;
		random = random_;
		comparator_ = new DominationComparator(s,random);
	}

	DominationComparator comparator_;

	public C_NDSRanking() {
		name = "ranking";
	}

	public C_NDSRanking(Population d) {
		name = "ranking";
		pop = d;
	}

	public int size() {
		return object.size();
	}

	public Population get(int key) {
		assert (object.size() > key) : "Population size is " + object.size() + "	key is " + key;
		return object.get(key);
	}

	public void setPop(Population pop_) {
		pop = pop_;
	}

	public String getName() {
		return name;
	}
	public int getworstrank(){
		return object.size();
	}

	int CompareTwoSolution_NDS(Solution one, Solution two, boolean isMAX_) {
	if (!one.getFeasible() && two.getFeasible()) {
		return -1;
	}
	else if (one.getFeasible() && !two.getFeasible()) {

		return 1;
	}
	else if (one.getFeasible() && two.getFeasible()) {

		try {
			return comparator_.execute(one, two);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	} else if (!one.getFeasible() && !two.getFeasible()){

		if (one.getViolation() > two.getViolation()) {
			return -1;
		}
		else if (one.getViolation() < two.getViolation()) {
			return 1;
		}
		else {
			return 0;
		}
	} else {
		System.out.println("error");
		assert false : "in C_NDSRanking";
	}
	return -1100000;
}

	public void Ranking() {
		List<Integer> Sp_ = new ArrayList<Integer>();
		List<Integer> F_i= new ArrayList<Integer>();

		int size = pop.size();

		int []  Np_ = new  int[size];
		List<List<Integer>> Sp_all = new ArrayList<List<Integer>>();
		List<List<Integer>> F_all = new ArrayList<List<Integer>>();

		for (int p = 0; p < size; p++) {
			Sp_.clear();
			Np_[p] = 0;
			for (int q = 0; q < size; q++) {
				if (CompareTwoSolution_NDS(pop.get(p), pop.get(q), isMAX_) == 1) {
					Sp_.add(q);
				}
				else if (CompareTwoSolution_NDS(pop.get(q), pop.get(p), isMAX_) == 1) {
					Np_[p] = Np_[p] + 1;
				}
			}
			if (Np_[p] == 0) {
				pop.get(p).setRank(1);
				F_i.add(p);
			}
			Sp_all.add(new ArrayList<Integer>(Sp_));
		}

		int i = 1;
		List<Integer> Q = new ArrayList<Integer>();
		F_all.add(F_i);
		while (F_i.size() != 0) {
			Q.clear();

			for (int j = 0; j < F_i.size(); j++) {
				int p = F_i.get(j);
				List<Integer> Sp = (Sp_all.get(p));
				for (int k = 0; k < Sp.size(); k++) {
					int q = Sp.get(k);
					Np_[q] = Np_[q] - 1;
					if (Np_[q] == 0) {
						pop.get(q).setRank(i + 1);
						Q.add(q);
					}
				}
			}
			i = i + 1;
			F_i = new ArrayList<Integer>(Q);
			F_all.add(F_i);
		}
		object.clear();

		for (int k = 0; k < F_all.size(); k++) {
			Population d = new Population(F_all.get(k).size());
			List<Integer> ind = F_all.get(k);
			for (int ja = 0; ja < ind.size(); ja++) {
				d.add(pop.get(ind.get(ja)));
			}
			if (ind.size() != 0) {
				object.add(d);
			}
		}
	};


	public void Ranking(int t) {
		List<Integer> Sp_ = new ArrayList<Integer>();
		List<Integer> F_i= new ArrayList<Integer>();

		int size = pop.size();

		int []  Np_ = new  int[size];
		List<List<Integer>> Sp_all = new ArrayList<List<Integer>>();
		List<List<Integer>> F_all = new ArrayList<List<Integer>>();

		Population taskPop = new Population(pop.size());

		for(int p = 0; p < pop.size();p++){
			if(pop.get(p).getSkillFactor()==t)
				taskPop.add(pop.get(p));

		}
		size = taskPop.size();
		for (int p = 0; p < size; p++) {
			Sp_.clear();
			Np_[p] = 0;
			for (int q = 0; q < size; q++) {
				if (CompareTwoSolution_NDS(taskPop.get(p), taskPop.get(q), isMAX_) == 1) {
					Sp_.add(q);
				}
				else if (CompareTwoSolution_NDS(taskPop.get(q), taskPop.get(p), isMAX_) == 1) {
					Np_[p] = Np_[p] + 1;
				}
			}
			if (Np_[p] == 0) {
				taskPop.get(p).setRank(1);
				F_i.add(p);
			}
			Sp_all.add(new ArrayList<Integer>(Sp_));
		}

		int i = 1;
		List<Integer> Q = new ArrayList<Integer>();
		F_all.add(F_i);
		while (F_i.size() != 0) {
			Q.clear();

			for (int j = 0; j < F_i.size(); j++) {
				int p = F_i.get(j);
				List<Integer> Sp = (Sp_all.get(p));
				for (int k = 0; k < Sp.size(); k++) {
					int q = Sp.get(k);
					Np_[q] = Np_[q] - 1;
					if (Np_[q] == 0) {
						pop.get(q).setRank(i + 1);
						Q.add(q);
					}
				}
			}
			i = i + 1;
			F_i = new ArrayList<Integer>(Q);
			F_all.add(F_i);
		}
		object.clear();

		for (int k = 0; k < F_all.size(); k++) {
			Population d = new Population(F_all.get(k).size());
			List<Integer> ind = F_all.get(k);
			for (int ja = 0; ja < ind.size(); ja++) {
				d.add(taskPop.get(ind.get(ja)));
			}
			if (ind.size() != 0) {
				object.add(d);
			}
		}
	};


	public static void main(String[] args) {
		test d = new test();
		d.setConfig();
	}

	public void subscriontPop(Population a) {
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i).getRank() + "			" + a.get(i).getCrowdDistance_());
		}
	}

}
