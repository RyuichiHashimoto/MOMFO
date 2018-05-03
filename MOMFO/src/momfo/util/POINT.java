package momfo.util;

import momfo.core.Solution;

public class POINT {
	double[] POINT_;

	int dimension_;

	int ID_;

	public POINT(){

	}

	public void Print() {
		for(int i=0;i<dimension_;i++) {
			System.out.print(POINT_[i] +" ");
		}
		System.out.println();;
	}

	public  POINT(double[] d){
		assert d.length > 0 : "initilzer in ReferencePoint :: argment has no size";
		dimension_ = d.length;
		POINT_ = d.clone();
	}

	public POINT(int dimension){
		POINT_ = new double[dimension];
		dimension_ = dimension;
	}



	public POINT(POINT a){
		dimension_ = a.getDimension();
		POINT_ = a.get().clone();
	}



	public POINT(Solution d) {
		assert d.getNumberOfObjectives() > 2 :"the Dimension of Objective is "  + d.getNumberOfObjectives();
		int size = d.getNumberOfObjectives();
		POINT_ = new double[size];

		dimension_ = size;
		for(int i=0;i<size;i++){
			POINT_[i] = d.getObjective(i);
		}

	}


	public double get(int key){
		return POINT_[key];
	}

	public double[] get(){
		return POINT_;
	}
	public int getDimension(){
		return dimension_;
	}

	public int getID(){
		return ID_;
	}
	public  void setID(int key){
		ID_ = key;
	}


// this Function is Deeocopy
	public void  set(double[] a){
			assert a.length == POINT_.length :" argment dimension are different";
			POINT_ = a.clone();
	}
	public void  set(int key, double a){
		assert key < POINT_.length :" argment dimension are different";
		POINT_[key] = a;
	}


	public void getInverse() {
		for(int i=0;i< dimension_;i++){
			POINT_[i] = POINT_[i] * -1;
		}
	}
}
