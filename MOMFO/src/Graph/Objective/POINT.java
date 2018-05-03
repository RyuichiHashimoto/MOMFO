package Graph.Objective;

public abstract class POINT {

	private int nDims;

	public int getDim(){
		return nDims;
	}
	public abstract double[] getPoint();
	public abstract double getX();
	public abstract double getY();
	public abstract double getZ();
	
	public abstract void setX(double x);
	public abstract void setY(double y);
	public abstract void setZ(double z);
	
}
