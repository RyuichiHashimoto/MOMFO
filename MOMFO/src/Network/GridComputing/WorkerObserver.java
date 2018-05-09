package Network.GridComputing;

import java.util.Observable;
import java.util.Observer;

/**
 * Observer to watch the events of the WorkerThread.
 * All the events reported by the WorkerThread are
 * defined in SereverEvent.
 *
 * @author Hiroyuki Masuda
 */
abstract public class WorkerObserver implements Observer {

	@Override
	public void update(Observable o, Object event) {
		// double dispatch; call method corresponding to the event
		((ServerEvent) event).handle(this, (WorkerThread) o);
	}

	abstract void taskStarted(WorkerThread thread);
	abstract void taskFinished(WorkerThread thread);
	abstract void exceptionThrown(WorkerThread thread);
	abstract void slaveFinished(WorkerThread thread);
	abstract void slaveDead(WorkerThread thread);
}

enum ServerEvent {
	TASK_START, TASK_DONE, TASK_THROW_EXCPTION,
	SLAVE_FINISHED, SLAVE_DEAD;

	public void handle(Observer ovserver, WorkerThread worker) {
		// call corresponding method
		WorkerObserver wo = (WorkerObserver) ovserver;
		switch (this) {
		case TASK_START:
			wo.taskStarted(worker);
			break;
		case TASK_DONE:
			wo.taskFinished(worker);
			break;
		case TASK_THROW_EXCPTION:
			wo.exceptionThrown(worker);
			break;
		case SLAVE_FINISHED:
			wo.slaveFinished(worker);
			break;
		case SLAVE_DEAD:
			wo.slaveDead(worker);
			break;
		default:
			throw new AssertionError(this);
		}
	}
}
