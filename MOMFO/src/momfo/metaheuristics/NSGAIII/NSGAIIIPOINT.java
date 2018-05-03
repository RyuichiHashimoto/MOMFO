package momfo.metaheuristics.NSGAIII;

import momfo.util.POINT;

public class NSGAIIIPOINT extends POINT{


	private double distance;

	private int ReferenceIndex;

	public NSGAIIIPOINT(NSGAIIIPOINT nsgaiiipoint) {
		super(nsgaiiipoint);
		distance = nsgaiiipoint.getDistance();
		ReferenceIndex = nsgaiiipoint.getIndex();
	}
	public NSGAIIIPOINT(double[] ds) {
		super(ds);
		distance = Double.POSITIVE_INFINITY;
		ReferenceIndex = -1;
	}
	public NSGAIIIPOINT() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public double getDistance(){
		return distance;
	}
	public int getIndex(){
		return ReferenceIndex;
	}

	public void setIndex(int d){
		ReferenceIndex = d;
	}
	public void setDistance(double d){
		distance = d;
	}



}
