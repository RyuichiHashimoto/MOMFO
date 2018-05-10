package momfo.problems.SOP;

import lib.math.BuildInRandom;

public class rotationMatrix {



	public static double[][]  calc(double[][] left, double[][] right) {
		double[][] ret = new double[left.length][right[0].length];
		double sum = 0.0;

		for(int i =0 ;i<left.length;i++){
			for(int j = 0;j < right.length; j++){
				sum = 0.0;
				for(int k = 0;k< left[0].length;k++){
					sum += left[i][k]*right[k][j];
				}
				ret[i][j] = sum;
			}
		}
		return ret;
	}
	public static double[]  calc(double[][] left, double[] right) {
/*
		double[] ret = new double[left.length];
		double sum = 0.0;

		for(int i =0 ;i<left.length;i++){
			ret[i] = 0;
			for(int j = 0;j < right.length; j++){
				ret[i] += left[i][j]*right[j];
			}
		}
*/
		int len = right.length;
		double res[] = new double[len];

		for (int i = 0; i < len; i++) {
			double[] y = left[i];

			double sum = 0;
			for (int j = 0; j < len; j++)
				sum += right[j] * y[j];
			res[i] = sum;
		}

		return res;


	//	return ret;
	}
	public static double[]  calc(double[] right , double[][] left) {
		double[] ret = new double[left.length];
		double sum = 0.0;

		for(int i =0 ;i<left.length;i++){
			ret[i] = 0;
			for(int j = 0;j < right.length; j++){
				ret[i] += left[i][j]*right[j];
			}
		}
		return ret;
	}
	public static void Subscript(double[] a){
		for(int i=0;i< a.length;i++){
			System.out.print(a[i] +  "	");
		}
		System.out.print("\n");
	}
	public static void Subscript(double[][] a){
		for(int i=0;i< a.length;i++){
			for(int j = 0;j < a[i].length;j ++)
				System.out.print(a[i][j] +  "	");
			System.out.print("\n");
		}
	}


// make the unit Matrix;
	public static double[][] UnitMatrix(double[][] a,int size){
			 a = new double[size][size];
			for(int i =0 ;i< size;i++){
				for(int j =0 ;j< size;j++){
					if(i == j)
						a[i][j] = 1.0;
					else
						a[i][j] = 0.0;
				}

			}
			return  a;
	}

	public static double[][] Transpose(double[][] a){
		double ret[][] = new double[a[0].length][a.length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				ret[j][i] = a[i][j];
			}
		}
		return ret;
	}


	//make unit Matrix;
	public static double[][] UnitMatrix(int size){
		double[][] a = new double[size][size];
		for(int i =0 ;i< size;i++){
			for(int j =0 ;j< size;j++){
				if(i == j)
					a[i][j] = 1.0;
				else
					a[i][j] = 0.0;
			}

		}
		return  a;
	}





	// For Configuration
	public static void main(String[] args){

		double[][] mat = new double[10][10];

		double[] vect = new double[10];
		BuildInRandom Random  = new BuildInRandom(1995);
		
		for(int i=0;i< mat.length;i++){
			vect[i] = Random.nextIntIE(5);
			for(int j = 0;j < mat[i].length;j ++)
				if(i == j)
					mat[i][j] = Random.nextIntIE(5);
				else
					mat[i][j] = Random.nextIntIE(5);
		}

		Subscript(mat);
		System.out.print("\n");

		Subscript(vect);
		System.out.print("\n");

		System.out.print("\n");
		Subscript(calc(mat,vect));


		vect = calc(mat,vect);


			Subscript(vect);




	}

}
