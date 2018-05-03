package Graph.Objective;

public class POINT2D extends POINT{

	double x;
	double y;
	double z;

	@Override
	public double[] getPoint() {
		double[] ret = {x,y};
		return ret;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		// TODO 自動生成されたメソッド・スタブ
		return y;
	}

	@Override
	public double getZ() {
		return 0;
	}

	@Override
	public void setX(double tempX) {
		x = tempX;
	}

	@Override
	public void setY(double y) {
	}

	@Override
	public void setZ(double z) {
//		throws
	}




}
