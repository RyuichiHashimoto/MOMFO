package NetworkSystem.worker;

public interface Signal {


	int SUCCESS = 1;
	int NETWORKEXCEPTION = 2;
	int EXECUTEEXCEPTION = 3;
	int DEAD	= -1;
	public int getSignal();


}
