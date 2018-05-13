package momfo.result;

import java.io.IOException;

import mo.solutionset.Solutionset;

public class ObjectiveSpaceResult extends PopulationResult {
	@Override
	public void afterTrial() throws IOException {
		writer.write(Solutionset.toText(solver.getPopulation().getUniqueNonDominated(), isMaximize));
		writer.write("\n");
	}
}
