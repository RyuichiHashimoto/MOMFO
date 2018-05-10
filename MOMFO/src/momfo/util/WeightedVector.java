package momfo.util;

import lib.math.Calculator;

/*
 *  this class is for the MOEA/D implementation
 *  Mainly used for the generate the WeightedVector
 * this cluss may be used for the MOMFEA (now 2017/5/17)
 *
 */
public class WeightedVector {

	double[] weightedVector_;

	public int getDimension(){return weightedVector_.length;};

	public WeightedVector(WeightedVector d ){
		int dime =  d.getDimension();
		weightedVector_ = new double[dime];
		for(int i=0;i<dime;i++){
			weightedVector_[i] = d.get(i);
		}
	}

	public WeightedVector(double[] d ){
		int dime =  d.length;
		weightedVector_ = new double[dime];
		for(int i=0;i<dime;i++){
			weightedVector_[i] = d[i];
		}
	}


	public double get(int i){
		assert i < getDimension() : "WeightedVector:: the index is wrong , the size of WeightedVector is" + getDimension() + "and the index is " + i;
		return weightedVector_[i];
	}

	public double[] get(){
		return weightedVector_;
	}



	public static double[][] getWeightedVector(int numberOfObjectives, int numberOfdivition){
		int SizeOfPopulation = Calculator.conbination( numberOfdivition + numberOfObjectives  -1,  numberOfObjectives - 1);

		double[][] ret = new double[SizeOfPopulation][numberOfObjectives];
		double[] a = new double[numberOfObjectives];
 		int counter = 0;
 		int size = Calculator.pow(  numberOfdivition + 1, numberOfObjectives);


 		for(int i = 0;i<size;i++){
			int moto = i;
			int sum = 0;
			for(int k = 0;k<numberOfObjectives;k++){
				a[k] = moto%(numberOfdivition + 1);
				sum  =  sum + moto% (numberOfdivition + 1);
				moto = moto / (numberOfdivition + 1);
			}
			if (sum == numberOfdivition){
				for(int k = 0;k<numberOfObjectives;k++){
					ret[counter][k] = a[k] / numberOfdivition;
				}
				counter++;
			}
		}

		return ret;
	}

	public static double[][] conbine(double[][] a, double [][] b ){
		int asize = a.length;
		int bsize = b.length;
		int eachSize = a[0].length;

		double  [][] ret = new double[asize + bsize][eachSize];

		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[i].length;j++){
				ret[i][j] = a[i][j];
			}
		}
		for(int i=0;i<b.length;i++){
			for(int j=0;j<b[i].length;j++){
				ret[i+a.length][j] = b[i][j];
			}
		}	return ret;
	}


	public static void main(String[] args){
		double [][] a = getWeightedVector(10, 3);


		double[][] weight1 = getWeightedVector(10,2);
		double[][] weight2 = getWeightedVector(10,3);
		a = conbine(weight1,weight2);
		for(int i=0;i<a.length;i++){
			for(int j= 0 ;j<a[i].length;j++ ){
				System.out.print(a[i][j] +  " ");
			}
			System.out.println("");
	}


//		System.out.print(a.length+  " ");

	}

	public static double[][] getInnerWeightVector(int numberOfObjectives, int numberOfdivition){
		double[][] ret = getWeightedVector(numberOfObjectives, numberOfdivition);
		
		getinnerWeightVector(ret);
		
		return ret;
	}

	public static void getinnerWeightVector(double[][] weightinner) {
		assert weightinner.length >0 : "the weighted Size is " + weightinner.length;
		assert weightinner[0].length > 1: "the size of top weighed vector is "  + weightinner[0].length;

		int dimension = weightinner[0].length;

		for(int i=0;i<weightinner.length;i++){
			for(int n=0;n<dimension;n++){
				weightinner[i][n] = (weightinner[i][n] + 1d/dimension)/2;
			}
		}
	}

}
