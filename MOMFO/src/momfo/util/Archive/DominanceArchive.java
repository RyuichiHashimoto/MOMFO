package momfo.util.Archive;

import lib.math.Distance;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;

/*
 *
 *
 *
 *
 */

public class DominanceArchive extends SolutionArchive{


	public DominanceArchive(Population pop) {
		super(pop);
	}

	public DominanceArchive(){
		super();
		archiveName = "DominanceArchive";
	}

	@Override
	public void addSolution(Solution sol){
		solutionArchive.add(sol);
	}

	@Override
	public void eraseSolution(int index) {

	}

	@Override
	public Population selectSolution(int retsize) throws JMException {
		Population ret = new Population(solutionArchive.size());
		for(int p = 0; p < ret.size();p++){
			ret.add(solutionArchive.get(p));
		}

		Distance.calcCrowdingDistance(ret);

//		Sort.QuickSort(ret.get(), new CrowdingDistanceComparator(false), 0, ret.size()-1);

		return ret;
	}

	public static void main(String[] args) throws JMException, ClassNotFoundException {
		SolutionArchive solArchive = new DominanceArchive();

		Solution sol = new Solution(2);
		sol.setObjective(0, 3);
		sol.setObjective(1, 2);
		solArchive.addSolution(new Solution(sol));

		sol.setObjective(0, 4);
		sol.setObjective(1, 3);
		solArchive.addSolution(new Solution(sol));

		sol.setObjective(0, 5);
		sol.setObjective(1, 4);
		solArchive.addSolution(new Solution(sol));

		Population ret = solArchive.selectSolution(2);

//		ret.subscriptObjective();




//		if (solArchive.isObjectiveDuplication(sol)){
//			System.out.println("Sucsess");
//		} else {
//			System.out.println("false");
//		}
	}

}
