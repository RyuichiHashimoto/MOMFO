package momfo.metaheuristics.NSGAIII;

import Jama.Matrix;
import momfo.util.POINT;
public class HyperPlane {


	private final NSGAIIIFront NSGAIIIFront_;
	private       double[] IdealPoints;
	private 	   double[][] extremePoint_;
	private 	   double[] NadiaPoints_;
	private 	  double[] Intersection;
	int obj;

	public HyperPlane(NSGAIIIFront d) {

		NSGAIIIFront_ = d;
		obj = NSGAIIIFront_.getDimension();
		IdealPoints = new double[obj];
		Intersection = new double[obj];
		NadiaPoints_ = new double[obj];
		for(int i=0;i<IdealPoints.length;i++){
			NadiaPoints_[i] = Double.NEGATIVE_INFINITY;
		}
		for(int i=0;i<IdealPoints.length;i++){
			IdealPoints[i] = Double.POSITIVE_INFINITY;
		}

		extremePoint_ = new double[obj][obj];

		for(int i = 0; i < extremePoint_.length;i++){
			for(int j=0;j<extremePoint_[i].length;j++){
				extremePoint_[i][j] = Double.MAX_VALUE;
			}
		}
	}

	//アーカイブを使用するようにしているため絶対に参照コピー
	public HyperPlane(NSGAIIIFront d,double[][] weight,double[] Ideal, double[][] extremePoints_){
		obj = Ideal.length;

		Intersection = new double[obj];
		NadiaPoints_ = new double[obj];
		for(int i=0;i<obj;i++){
			NadiaPoints_[i] = Double.MIN_VALUE;
		}

		NSGAIIIFront_ = d;
		IdealPoints = Ideal;
		extremePoint_ = extremePoints_;
	}
	public HyperPlane(NSGAIIIFront d,double[][] weight,double[] Ideal){
		obj = Ideal.length;

		Intersection = new double[obj];
		NSGAIIIFront_ = d;
		NadiaPoints_ = new double[obj];
		for(int i=0;i<Ideal.length;i++){
			NadiaPoints_[i] = Double.MIN_VALUE;
		}

		IdealPoints = Ideal;
		extremePoint_ = new double[obj][obj];
		for(int i = 0; i < extremePoint_.length;i++){
			for(int j=0;j<extremePoint_[i].length;j++){
				extremePoint_[i][j] = Double.MAX_VALUE;
	}
		}

	}



/*
	public void Normalization(NSGAIIIFront NSGAIIIFront_){
		assert NSGAIIIFront_.size() != 0: "pop　size is " + NSGAIIIFront_;

		int Objectives = NSGAIIIFront_.get(0).getDimension();
		double[] ref = new double[Objectives];

		for(int j=0;j<Objectives;j++){
			ref[j] = 1.0E-6;
		}
		NSGAIIIFront hyp = new NSGAIIIFront();

		for(int obj= 0 ; obj< Objectives;obj++){
			ref[obj] = 1;
			if(obj!= 0){
				ref[obj-1] = 1.0E-6;
			}
			double min = Double.POSITIVE_INFINITY;

			for(int popNum =0;popNum < NSGAIIIFront_.size();popNum++){
				min = Math.min(NSGAIIIFront_.get(popNum).get(obj) , min);
			}
			double minASF = Double.POSITIVE_INFINITY;
			int index = -1;

			for(int popNum =0;popNum < NSGAIIIFront_.size();popNum++){
				POINT nowPOINT = NSGAIIIFront_.get(popNum);
				double temp = nowPOINT.get(obj);
				nowPOINT.set(obj, temp - min);
				double temp_asf = ASF.ASF(nowPOINT,ref);
				if(minASF > temp_asf){
					index = popNum;
					minASF= temp_asf;
				}
			}
			hyp.add( NSGAIIIFront_.get(index) );
		}

		double[] intersection = new double[Objectives];
		MATRIX inverse = new MATRIX(hyp);
		MATRIX  unitarray = new MATRIX(hyp.size(),1 ,1);

		if(inverse.Inverse()){
			inverse.mul(unitarray);
			for(int i = 0;i< Objectives;i++){
				intersection[i] = 1d/
						inverse.get(i, 0);
			}
		} else {
			for(int i = 0;i< Objectives;i++){
				intersection[i] = NSGAIIIFront_.get(i).get(i);
			}
		}

		for(int j=0;j<Objectives;j++){
			assert intersection[j] > 0 : "intersection がマイナス	" +intersection[j] ;
			for(int i=0;i<NSGAIIIFront_.size();i++){
				intersection[j] = intersection[j]<1.0E-6 ?1.0E-6 : intersection[j];
				POINT tempPOINT = NSGAIIIFront_.get(i);
				tempPOINT.set(j,tempPOINT.get(j)/intersection[j]);
			}
		}
	}
*/

	private void GetIdeal(NSGAIIIFront d){

		for(int j=0;j<d.getDimension();j++){
			IdealPoints[j] = Double.MAX_VALUE;
		}
		for(int i=0;i<d.size();i++){
			for(int j=0;j<d.getDimension();j++){
				IdealPoints[j] = Math.min(IdealPoints[j], d.get(i).get(j));
			}
		}
	}

	private void getNadiaPoint(NSGAIIIFront d){
		for(int i=0;i<d.size();i++){
			for(int j=0;j<d.getDimension();j++){
				NadiaPoints_[j] = Math.max(NadiaPoints_[j], d.get(i).get(j));
			}
		}
	}
	private double asfFunction(POINT sol, int j) {
		return asfFunction(sol.get(),j);
	}

	private void computeExtremePoints(NSGAIIIFront d){
		for (int j = 0; j < obj; j++) {
			int index = -1;
			double min = asfFunction(extremePoint_[j],j);
			for (int i = 0; i < d.size(); i++) {
				double asfValue = asfFunction_(d.get(i).get(), j);
				if (asfValue < min) {
					min = asfValue;
					index = i;
				}
			}
			if(index != -1)
			for (int k = 0; k < obj; k++)
				extremePoint_[j][k] = d.get(index).get(k);
		}
	}

	void computeIntercepts() {

		Intersection = new double[obj];
		double[][] temp = new double[obj][obj];
		for (int i = 0; i < obj; i++) {
			for (int j = 0; j < obj; j++) {
				double val = extremePoint_[i][j] - IdealPoints[j];
				temp[i][j] = val;
			}
		}

		Matrix EX = new Matrix(temp);
		if (EX.rank() == EX.getRowDimension()) {
			double[] u = new double[obj];
			for (int j = 0; j < obj; j++)
				u[j] = 1;

			Matrix UM = new Matrix(u, obj);
			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {

				double aj = 1.0 / AL.get(j, 0) + IdealPoints[j];

				if ((aj > IdealPoints[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					Intersection[j] = aj;
				else
					break;
			}
			if (j != obj) {
				for (int k = 0; k < obj; k++)
					Intersection[k] = NadiaPoints_[k];
			}

		} else {
			for (int k = 0; k < obj; k++)
				Intersection[k] = NadiaPoints_[k];
		}

	}

	private double asfFunction_(double[] ds, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;
		for (int i = 0; i < obj; i++) {
			double val = Math.abs(ds[i]) ;

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}

	private double asfFunction(double[] ds, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;
		for (int i = 0; i < obj; i++) {
			double val = ds[i] - IdealPoints[i];
			assert val >= 0 : val;

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}

	void normalizePopulation(NSGAIIIFront d) {
		for (int i = 0; i < d.size(); i++) {
			NSGAIIIPOINT sol = d.get(i);

			for (int j = 0; j < obj; j++) {
				double val = (sol.get(j) - IdealPoints[j])
						/ (Intersection[j] - IdealPoints[j]);
				sol.set(j, val);
			}
		}
	}

	public void Normalization(){
//		    GetIdeal(NSGAIIIFront_);
//			getNadiaPoint(NSGAIIIFront_);
//			computeExtremePoints(NSGAIIIFront_);
//			computeIntercepts();
//		normalizePopulation(NSGAIIIFront_);

		Normalization_();
//		subscript();

	}
	public void subscript(){
		for(int i=0;i<NSGAIIIFront_.size() ;i++){
			double ret = 0.0;
			for(int ia=0;ia<obj ;ia++){
				ret +=NSGAIIIFront_.get(i).get(ia);
			}
			System.out.println(ret);
		}
	}


	public void  getIdealPoint(){
		for(int i=0;i< obj;i++){
			double min =  IdealPoints[i];
			for(int popNum =0;popNum < NSGAIIIFront_.size();popNum++){
				min = Math.min(NSGAIIIFront_.get(popNum).get(i) , min);
			}
			IdealPoints[i] = min;
			assert !Double.isInfinite(min) : min;
		}
	}

	public void ShiftWithIdeal(){
		for(int popNum =0;popNum < NSGAIIIFront_.size();popNum++){
			POINT d = NSGAIIIFront_.get(popNum);
			for(int obje = 0 ; obje< obj;obje++){
				d.set(obje,d.get(obje)-IdealPoints[obje]);
				assert (d.get(obje) >=0):"test";
			}
		}

	}

	public void GetExtremePoint(){
		for(int obje = 0 ; obje< obj;obje++){
			int index = -1;
			double min = Double.MAX_VALUE;

			for(int popNum =0;popNum < NSGAIIIFront_.size();popNum++){
				double temp_asf = asfFunction(NSGAIIIFront_.get(popNum).get(),obje);
				assert temp_asf >= 0: temp_asf;
				if(min > temp_asf){
					index = popNum;
					min= temp_asf;
				}
			}
			if(index != -1){
				POINT d = NSGAIIIFront_.get(index);
				for(int i = 0;i <obj ;i++){
					extremePoint_[obje][i] = d.get(i);
				}
			} else {
				System.out.println("erroe");
			}
		}
	}


	public void getIntersectionPoints_()	{
		double ret[][] = new double[obj][obj];
			for(int i=0;i < extremePoint_.length ;i++){
				for(int aj=0;aj<extremePoint_.length;aj++){
					ret[i][aj] = extremePoint_[i][aj] - IdealPoints[aj];
				}
			}
	}
/*
	private void Normalization_() {
		assert NSGAIIIFront_.size() > 0: "pop　size is " + NSGAIIIFront_;

		getIdealPoint();

		GetExtremePoint();

		Matrix EX = new Matrix(extremePoint_);

		if(EX.rank() == EX.getRowDimension()){

			double [] u = new double[obj];
			for(int i=0;i<obj;i++){
				u[i] = 1d;
			}

			Matrix UM = new Matrix(u,obj);
			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {
				double aj = 1.0 / AL.get(j, 0) + IdealPoints[j];
				if ((aj > IdealPoints[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					Intersection[j] = aj;
				else
					break;
			}
			if (j != obj) {
				for(int i = 0;i< obj;i++){
					double max  = Double.NEGATIVE_INFINITY;
					for(int k = 0;k < NSGAIIIFront_.size();k++){
							max = Math.max(max,NSGAIIIFront_.get(k).get(i));
					}
					Intersection[i] = max;
				}
			}

		} else {
			for(int i = 0;i< obj;i++){
				double max  = Double.NEGATIVE_INFINITY;
				for(int j = 0;j < NSGAIIIFront_.size();j++){
						max = Math.max(max,NSGAIIIFront_.get(j).get(i));
				}
				Intersection[i] = max;
			}
		}
		for(int j=0;j<obj;j++){
			for(int i=0;i<NSGAIIIFront_.size();i++){
				POINT tempPOINT = NSGAIIIFront_.get(i);
				tempPOINT.set(j, ( (tempPOINT.get(j) - IdealPoints[j]))/(Intersection[j] - IdealPoints[j]));
			}
		}

	//	assert config() : "okasi";

		// TODO 自動生成されたメソッド・スタブ
	}
*/


	private void Normalization_() {
		assert NSGAIIIFront_.size() > 0: "pop　size is " + NSGAIIIFront_;

		getIdealPoint();

		GetExtremePoint();

		double[][] ret = new double[obj][obj];

		for(int i=0;i<ret.length;i++){
			for(int j=0;j<ret.length;j++){
				ret[i][j] = extremePoint_[i][j] - IdealPoints[j];
			}
		}

		Matrix EX = new Matrix(extremePoint_);

		if(EX.rank() == EX.getRowDimension()){

			double [] u = new double[obj];
			for(int i=0;i<obj;i++){
				u[i] = 1d;
			}

			Matrix UM = new Matrix(u,obj);
			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < obj; j++) {
				double aj = 1.0 / AL.get(j, 0) + IdealPoints[j];
				if ((aj > IdealPoints[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					Intersection[j] = aj;
				else
					break;
			}
			if (j != obj){
				for(int i = 0;i< obj;i++){
						double max  = Double.NEGATIVE_INFINITY;
						for(int js = 0;js < NSGAIIIFront_.size();js++){
								max = Math.max(max,NSGAIIIFront_.get(js).get(i));
						}
					Intersection[i] = max;
				}
			}
		} else {
				for(int i = 0;i< obj;i++){
					double max  = Double.NEGATIVE_INFINITY;
					for(int k = 0;k < NSGAIIIFront_.size();k++){
							max = Math.max(max,NSGAIIIFront_.get(k).get(i));
					}
					Intersection[i] = max;
				}
		}
		for(int i=0;i<NSGAIIIFront_.size();i++){
			POINT tempPOINT = NSGAIIIFront_.get(i);
			for(int j=0;j<obj;j++){
				tempPOINT.set(j, ( (tempPOINT.get(j) - IdealPoints[j]))/(Intersection[j] - IdealPoints[j]));
			}
		}
	//	assert config() : "okasi";
		// TODO 自動生成されたメソッド・スタブ
	}
	public void GetExtremePointFormax(){

		for(int i=0;i<obj;i++){
			double max = Double.NEGATIVE_INFINITY;
			int index = -1;
			for(int j=0;j<obj;j++){
				if(max < NSGAIIIFront_.get(j).get(i)){
					max = NSGAIIIFront_.get(j).get(i);
					index = j;
				}
			}
			for(int j=0;j<obj;j++){
				extremePoint_[i][j] = NSGAIIIFront_.get(index).get(j) - IdealPoints[j];
			}
		}


	}


}