package momfo.util;

public class ReferencePoint extends POINT {

	public  ReferencePoint(double[] d){
		dimension_ = d.length;
		POINT_ = d.clone();
	}

	public ReferencePoint(ReferencePoint a){
		super(a);
	}

	public void setInverse(){
		for(int i=0;i< dimension_;i++){
			POINT_[i] = POINT_[i] * - 1;
		}

	}

	public double[] getArray() {
		double[] ret = new double[dimension_];
		for(int i = 0;i<dimension_;i++) {
			ret[i] = POINT_[i];
		}
		return ret;
	}

	public POINT getPOINT() {
		return new POINT(getArray());
	}

}
