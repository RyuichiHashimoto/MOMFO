package momfo.util;

/*
 * this class is for the NSGAIII
 *逆行列は計算できるけど，制度が良くないため，変更する必要ありかも，
 *doubloe型の行列ではなく，分数型の行列の方が精度がいいかもしれないため，検討するべきかも
 *
 */


//正方行列のサイズが
//10だと誤差は5.82867*10E-16
//20だと誤差は3.906*E-15
//1000だと1675.37となり
//NSGAIIには使用する行列のサイズは目的数と同じため，たかだか20であるから問題なしであろう
//


public class MATRIX {

	private double[][] matrix;

	int size;
	int subsize;

	public int getSize(){
		return size;
	}
	public int getSubSize(){return subsize;}

	public MATRIX(){
		matrix = new double[0][0];
	}

	public MATRIX(double[][] d){
		size = d.length;
		subsize = d[0].length;
		matrix = new double[size][subsize];
		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] = d[i][j];
			}
		}
	}

	public MATRIX(MATRIX d){
		size = d.getSize();
		subsize = d.getSubSize();
		matrix = new double[size][subsize];

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] = d.get(i,j);
			}
		}
	}

	public MATRIX(Front d){
		subsize = d.getDimension();
		size = d.size();
		matrix = new double[size][subsize];

		for(int i=0;i<size;i++){
			POINT ss = d.get(i);
			for(int j=0;j<subsize;j++){
				matrix[i][j] = ss.get(j);
			}
		}
	}
	public MATRIX(POINT[] d){
		assert d.length != 0;
		subsize = d.length;
		size = d[subsize-1].getDimension();
		matrix = new double[size][subsize];

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] = d[i].get(j);
			}
		}
	}


	public MATRIX(int size_){
		subsize = size_;
		size = size_;
		matrix = new double[size][subsize];
		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				if(i == j )	matrix[i][j] = 1;
				else matrix[i][j] = 0;
			}
		}
	}

	public MATRIX(int one, int two, int dwo){
		subsize = two;
		size = one;
		matrix = new double[size][subsize];

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] = 1;
			}
		}



	}

	public double[] get(int d){
		return matrix[d];
	}

	public double get(int s,int key){
		assert  s < size ;
		assert key < subsize ;
		return matrix[s][key];
	}

	public double[][] get(){
		return matrix;
	}


	public void subscript(){
		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				System.out.print(get(i,j) + "	");
			}
			System.out.println();
		}
		System.out.println();

	}

	public void add(MATRIX two){
		assert (size == two.getSize()) : "matrix add :: 引き数の行列サイズがおかしい"  + size + "	" + two.getSize();
		assert (subsize == two.getSubSize()) : "matrix add :: 引き数の行列サイズがおかしい"  + subsize + "	" + two.getSubSize();

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] += two.get(i, j);
			}
		}
	}

	public void set(double[][] d){
		matrix = d;
	}

	public void subtraction(MATRIX two){
		assert (size == two.getSize()) : "matrix add :: 引き数の行列サイズがおかしい"  + size + "	" + two.getSize();
		assert (subsize == two.getSubSize()) : "matrix add :: 引き数の行列サイズがおかしい"  + subsize + "	" + two.getSubSize();

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize;j++){
				matrix[i][j] -= two.get(i, j);
			}
		}
	}

	public void exchange(int one, int two){
		double[] o = matrix[one];
		matrix[one] = matrix[two];
		matrix[two] = o;
	}

	public void mul(MATRIX two){
		assert two.getSize() == subsize : "行列積ができない"  + "	" + subsize + "	" + two.getSize();

		int subsize_ = two.getSubSize();
		double[][] ret = new  double[size][subsize_];

		for(int i=0;i<size;i++){
			for(int j=0;j<subsize_;j++){
				ret[i][j] = 0;
				for(int k=0;k<subsize;k++){
					ret[i][j] += matrix[i][k]*two.get(k, j);
				}
			}
		}
		subsize = subsize_;
		matrix = ret;
	}


	//行基本変形をする
	private void transformbaserow(int one, int two, double mag){
		assert one < size : "First index is wrong";
		assert two < size : "Second index is wrong";

		for(int i = 0;i<subsize;i++){
			matrix[two][i] += matrix[one][i]*mag;
		}
	}


	//true means there exists Inverse matrix, false means not exists
	//Nan になるときありなので使用禁止　20170824
	public boolean  Inverse(){
		assert size == subsize : "this matrix is not a square matrix";
		double[][] unit = new double[size][size];
		double[][] matrix_ = new double[size][size];
		//単位行列とこの行列の複製版の生成．
		//we use this deepcopy matrix because if we cannot make Inversed Matrix on the way to make, we cannnot recover the mateix before
		for(int i=0;i<size;i++){
			for(int j = 0;j<size;j++){
				if (i == j) unit[i][j] = 1.0; else unit[i][j] = 0.0;
				matrix_[i][j] = matrix[i][j];
			}
		}

		for(int i = 0; i < size;i++){
			//対格要素の絶対値が最大となるような行を基準にして逆行列の計算を行う．
			//そうしないと誤差が大きくなる．単純な問題ならこれでもOKだが，
			//NSGAIIIで使用する場合，誤差が大きくなりうる可能性がある．
			double max = -1;
			int index = -1;

			for(int j = i;j<size;j++){

				if(max < Math.abs(matrix_[i][j])){
					max = Math.abs(matrix_[i][j]);
					index = j;
				}
			}
			if(index == -1){
				System.out.println(size);
				for(int j = 0;j<size;j++){
					System.out.println(matrix_[i][j]);
				}
			}
			double [] o = matrix_[i];
			matrix_[i] = matrix_[index];
			matrix_[index] = o;
			o = unit[i];
			unit[i] = unit[index];
			unit[index] = o;

			if(max < 1.0E-14) return false;

			double temp =  matrix_[i][i];
			for(int k=0;k<size;k++){
				matrix_[i][k] /= temp;
				unit[i][k] /= temp;
			}

			for(int j = 0;j<size;j++){
				if(i == j) continue;
				double d =-1*matrix_[j][i]/matrix_[i][i];
				for(int k = 0;k < subsize;k++){
					unit[j][k] += d*unit[i][k];
					matrix_[j][k] += d*matrix_[i][k];
				}
			}
		}
/*
		for(int i=0;i<size;i++){
			for(int j = 0;j<size;j++){
				System.out.print(matrix_[i][j] + "	");
			}
			System.out.println();

		}
*/
		matrix = unit;//matrix_;
		return true;
	}



	public static void main(String[] args){


		int size = 4; int subsize = size;

		POINT[] d = new POINT[size];

		double[][]  one = new double[size][subsize];
		double[][]  two = new double[size][1];

		for(int i = 0;i < size;i++){
			two[i][0] = 1;
		}

		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				one[i][j] = Math.sqrt(2d/(size+1))*Math.sin((i+1)*(j+1)*Math.PI/(size+1));
//				one[i][j] = i+2*j;
			}
			d[i] = new POINT(one[i]);
		}
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
//				one[i][j] = i+2*j;
				one[i][j] = Math.sqrt(2d/(size+1))*Math.sin((i+1)*(j+1)*Math.PI/(size+1));
			}
		}

		MATRIX vise = new MATRIX(two);

		MATRIX matOne = new MATRIX(d);
//		matOne.subscript();
//		matOne.subscript();
		MATRIX mattwo = new MATRIX(one);
		MATRIX mattwo_inv = new MATRIX(one);
		mattwo.subscript();
		mattwo_inv.Inverse();
		System.out.println(mattwo_inv.getSize() + "	" + mattwo_inv.getSubSize());
		mattwo_inv.mul(mattwo);
		mattwo_inv.subscript();
		MATRIX UNIT = new MATRIX(size);
//		MATRIX E = new MATRIX(10);
//		matOne.subscript();
		if(!matOne.Inverse()){;
			System.out.print("失敗");
		}
//		matOne.subscript();
//		mattwo.subscript();

//		mattwo.mul(matOne);
//		matOne.mul(vise);
//		matOne.mul(matTwo);
//		matOne.subscript();
//		matOne.subscript();


		double max = Double.NEGATIVE_INFINITY;

		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				max = Math.max(max, Math.abs(mattwo.get(i,j) -UNIT.get(i,j)  ));
			}
		}

	//	mattwo.subscript();

		System.out.println();
		System.out.println("Error value : "+max);

		//		E.subscript();
	}
	public static double[] calc(double[][] rotationMatrix_, double[] x) {
		return null;
	}




}

