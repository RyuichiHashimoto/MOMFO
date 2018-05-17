package Network;

import java.io.IOException;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

public interface SolverEvent {
	void notifyEvent(SolverResult<?> result) throws IOException, NameNotFoundException, NamingException;	
}

enum CommonSolverEvent implements SolverEvent {
	BEFORE_RUN {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException {
			result.beforeRun();
		}
	},
	AFTER_RUN {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException {
			result.afterRun();
		}
	},
	EXCEPTION_RISE {
		@Override
		public void notifyEvent(SolverResult<?> result) throws IOException {
			result.exceptionRise();
		}
	},
}