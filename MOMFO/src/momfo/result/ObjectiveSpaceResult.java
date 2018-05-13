package momfo.result;

import java.io.IOException;

import lib.io.FileConstants;

public class ObjectiveSpaceResult extends PopulationResult {
	@Override
	public void afterTrial() throws IOException {
		
		if(!solver.getGA().isMultitask()) {
			writer.write(solver.getGA().getPopulation().objectiveToStr());
			writer.write(FileConstants.NEWLINE_DEMILITER);
		}
	}
}
