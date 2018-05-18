package momfo.util;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.SolverEvent;
import Network.SolverResult;
import momfo.result.GAResult;

public enum GAEvent implements SolverEvent {
	BEFORE_TRIAL {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException {
			if (result instanceof GAResult) ((GAResult) result).beforeTrial();
		}
	},
	AFTER_INTITIALIZATION {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException, NameNotFoundException, NamingException {
			if (result instanceof GAResult) ((GAResult) result).afterInitialization();
		}
	},
	AFTER_GENERATION {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException {
			if (result instanceof GAResult) ((GAResult) result).afterGeneration();
		}
	},
	AFTER_TRIAL {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException, NameNotFoundException, NamingException {
			if (result instanceof GAResult) ((GAResult) result).afterTrial();
		}
	},
}
