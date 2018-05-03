package momfo.util.Archive;

import java.util.ArrayList;
import java.util.List;

import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.JMException;

public abstract class SolutionArchive {

	protected List<Solution> solutionArchive;
	protected String archiveName;

	protected int designableSolutionSize;

	public SolutionArchive(Population pop) {
		solutionArchive = new ArrayList<Solution>();
		for (int p = 0; p < pop.size(); p++) {
			solutionArchive.add(new Solution(pop.get(p)));
		}
	}

	public SolutionArchive() {
		solutionArchive = new ArrayList<Solution>();
	}

	public int calcArchiveSize() {
		return solutionArchive.size();
	}

	public abstract void addSolution(Solution sol);

	public abstract void eraseSolution(int index);

	public abstract Population selectSolution(int retsize) throws JMException, ClassNotFoundException;

	public boolean isObjectiveDuplication(Solution _CandidateSol) {

		for (int p = 0; p < solutionArchive.size(); p++) {
			Solution checkerSol = solutionArchive.get(p);
			int o = 0;
			for (o = 0; o < checkerSol.getNumberOfObjectives(); o++) {
				if (Math.abs(_CandidateSol.getObjective(o) - checkerSol.getObjective(o)) > 1.0E-14) {
					break;
				}
			}
			if (o == checkerSol.getNumberOfObjectives()) {
				return true;
			}
		}

		return false;
	}

	public String getAchiveName() {
		return archiveName;
	}

	public void claer() {
		solutionArchive.clear();
	}


}
