package momfo.util.Archive;

import java.util.ArrayList;
import java.util.List;

import lib.experiments.JMException;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.WeightedVector;
import momfo.util.Comparator.MOEADComparator.NormalMOEADComapator;
import momfo.util.ScalarzingFunction.ScalarzingFunction;
import momfo.util.ScalarzingFunction.TchebycheffForMin;

public class MOEADArchive extends SolutionArchive {

	@Override
	public void addSolution(Solution sol) {
		solutionArchive.add(sol);
	}

	@Override
	public void eraseSolution(int index) {

	}

	public double[] getReferencePoint(List<Solution> pop, int Objective) throws ClassNotFoundException, JMException {
		Solution a = pop.get(0);
		double[] referencePoint = new double[Objective];
		for (int obj = 0; obj < Objective; obj++) {
			referencePoint[obj] = a.getObjective(obj);
		}
		for (int p = 1; p < pop.size(); p++) {
			for (int obj = 0; obj < Objective; obj++) {
				if (referencePoint[obj] > pop.get(p).getObjective(obj)) {
					referencePoint[obj] = pop.get(p).getObjective(obj);
				}
			}
		} // for
		return referencePoint;
	}

	public Population selectSolution(int Objective, int Division) throws JMException, ClassNotFoundException {
 		double[][] weightVector = WeightedVector.getWeightedVector(Objective, Division);
		double[] referencePoint = getReferencePoint(solutionArchive, Objective);
		int populationSize = weightVector.length;
		List<Integer> POPINDEX = new ArrayList<Integer>();

		Population ret = new Population(populationSize);
		ScalarzingFunction ScalarzingFunction_ = new TchebycheffForMin();
//		NormalMOEADComapator comparator = new NormalMOEADComapator(null, ScalarzingFunction_);
		NormalMOEADComapator comparator = null;
		
		comparator.setRefernecePoint(referencePoint);

		for (int p = 0; p < populationSize; p++) {
			int bestIndex = 0;

			Solution bestSol = solutionArchive.get(bestIndex);
			comparator.setWeightedVector(weightVector[p]);

			for (int q = 1; q < solutionArchive.size(); q++) {
				if (comparator.execute(solutionArchive.get(q), bestSol) == 1) {
					bestSol = solutionArchive.get(q);
					bestIndex = q;
				}
			}
			if(!POPINDEX.contains(bestIndex))
				POPINDEX.add(bestIndex);

			ret.add(bestSol);
		}

//		System.out.println("-----------------------------");
//		System.out.println("Before_ArchiveSize	\t" +calcArchiveSize() + "	");
		int beforeArhiveSize = calcArchiveSize();

		int counter = 0;

		List<Integer> list = new ArrayList<Integer>();


		for(int q = beforeArhiveSize-1; q >= 0; q--){
			if (!POPINDEX.contains(q)){
				solutionArchive.remove(q);
			}
		}

//		System.out.println("POPINDEXSIZE\t\t\t" + POPINDEX.size());
//		System.out.println("After_ArchiveSize	\t" +calcArchiveSize() + "	");


		return ret;
	}

	public Population selectSolution(int retsize) throws JMException, ClassNotFoundException {

		Population ret = null;
		if (retsize == 100) {
			int before = calcArchiveSize();
			ret = selectSolution(2, 99);
			//System.out.println(ret.size()+"	"+before + "	" + calcArchiveSize());
		} else if (retsize == 120) {
			ret = selectSolution(3, 14);
			System.out.println(ret.size() + "	" + retsize);
		} else {
			System.err.println("----------------fault-----------------");
			System.err.println("----------------fault-----------------");
			System.err.println("----------------fault-----------------");
		}
		return ret;
	}

	public static void main(String[] args) throws JMException, ClassNotFoundException {
		MOEADArchive archive = new MOEADArchive();





		archive.selectSolution(120);

	}
}
