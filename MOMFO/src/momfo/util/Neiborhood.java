package momfo.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import lib.math.BuildInRandom;
import lib.math.Permutation;

public class Neiborhood {


	private  double[][]	weight_;

	private int[][]		neiborhood_;

	private double[][]	q_;

	int NumberOfWeightedVector_;

	//deep copy
	public Neiborhood(double[][] weight) {
		int oneSize = weight.length;
		int twoSize = weight[0].length;
		weight_ = weight;

		for(int i=0;i<oneSize;i++){
			for(int j=0;j<twoSize;j++){
				weight_[i][j] = weight[i][j];
			}
		}
	}
	public int[][] getNeiborhood(){
		return neiborhood_;
	}
	public double[][] getWeight(){
		return weight_;
	}


	public Neiborhood(){

	}

	//deepcopy
	public void setWeightedVector(double[][] weight){
		int oneSize = weight.length;
		int twoSize = weight[0].length;
		weight_ = new double[oneSize][twoSize];

		for(int i=0;i<oneSize;i++){
			for(int j=0;j<twoSize;j++){
				weight_[i][j] = weight[i][j];
			}
		}


	}

	public  void setNeiborhood(int siz,BuildInRandom random_) {

		int size = weight_.length;
		neiborhood_ = new int[size][];


		int[]	list = new int[size];
		double[] dist = new double[size];
		Permutation a = new Permutation();
		a.setPermutation(list);
		int temp;
		for(int k=0;k<size;k++){

			neiborhood_[k] = new int[siz];

			list = Permutation.setPermutation(list);
			for(int i=0;i<size;i++){
				dist[i] = Distance(weight_[k], weight_[i]);
			}

			for(int i=0;i<size;i++){
				for(int j=i+1;j<size;j++){
					if(dist[list[i]] > dist[list[j]]){
						temp = list[i];
						list[i] = list[j];
						list[j] = temp;
					}
				}
			}
					randamSortEach(list,dist,random_);
				for(int i = 0;i<siz;i++){
					neiborhood_[k][i] = list[i];
				}


		}
	}






	static int EqualityFinalNumber(int[] list,int[] value, int key){
		int counter = key;
		for(int i=key+1;i<list.length;i++){
			if(Math.abs(value[list[i]] - value[list[key]]) >  1.0E-14){
				return counter;
			}
			counter++;
		}
		return counter;
	}

	static int EqualityFinalNumber(int[] list,double[] value, int key){
		int counter=key;
		for(int i=key+1;i<list.length;i++){
			if(Math.abs(value[list[i]] - value[list[key]]) >  1.0E-14){
				return counter;
			}
			counter++;
		}
		return counter;
	}
	public static void main(String[] args){
		BuildInRandom random = new BuildInRandom(10);
		int list[] = new int[25];
		double value[] = new double[25];
		int ddd[] = new int[25];
		int ddd_list[] = new int[25];
		for(int i=0;i<25;i++){
			ddd_list[i] = i;
			ddd[i] = 10;
			value[i] = ((i+1)%5)*1.5;
			list[i]  = i;
		}
		System.out.print("\n");

		for(int i=0;i<25-1;i++){
			for(int j=i;j<25;j++){
				if(value[list[i]] > value[list[j]]){
					int a = list[i];
					list[i] = list[j];
					list[j] = a;
				}

				if(ddd[ddd_list[i]] > ddd[ddd_list[j]]){
					int a = ddd_list[i];
					ddd_list[i] = ddd_list[j];
					ddd_list[j] = a;
				}
			}
		}
		for(int i=0;i<25;i++){
			System.out.print(value[list[i]] + " ");
		}
		System.out.print("\n");
		for(int i=0;i<25;i++){
			System.out.print(list[i] + " ");
		}
		System.out.print("\n");

		randamSortEach(list,value,random);
		for(int i=0;i<25;i++){
			System.out.print(list[i] + " ");
		}
		System.out.print("\n");

		for(int i=0;i<25;i++){
			System.out.print(value[list[i]] + " ");
		}

		for(int i=0;i<25;i++){
	//		System.out.print(list[i] + " ");
		}


	}


	static void  randamSortEach(int[] list,double[] value,BuildInRandom random_){
		int[] empty;


		for(int i=0;i<list.length;i++){
			int Hiral = EqualityFinalNumber(list,value,i);
			empty = new int[Hiral - i + 1];
			for(int k = i;k<=Hiral;k++){
				empty[k-i] = list[k];
			}
			empty = Sort.random_sort(empty,random_);

			for(int k = i;k<=Hiral;k++){
				list[k]    = empty[k- i];
			}
			i = Hiral;
		}
	}

	public double Distance(double[] a,double[] b){
		double sum = 0;
		int size = a.length;
		double[] one = new double[size];
		double[] two = new double[size];


		for(int i=0;i<a.length;i++){
				one[i] = a[i];
				two[i] = b[i];

			sum += (one[i] - two[i])*(one[i] - two[i]);
		}
		return Math.sqrt(sum);
	}

	public void setNeiborhood(int[][] neiborhood){
		neiborhood_ = neiborhood;
	}
	public  void subscript_to_file() {
		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream("neiborhood_config + " + (100)+ ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for(int i=0;i<neiborhood_.length;i++){
				for(int j=0;j<neiborhood_[i].length;j++)
				bw.write(neiborhood_[i][j] + "	" );
				bw.newLine();

			}


			/* Close the file */
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}


	}

	//ファイル読み込み
	public void fileReading_weight_vector(String name, int NumberOfweightVector ) {
			NumberOfWeightedVector_ = NumberOfweightVector;
			weight_ = new double[NumberOfweightVector][];
		try(BufferedReader br = new BufferedReader(new FileReader(name))){
			String line;
			String[] S;
			int counter=0;

			while(( line = br.readLine())!= null){
				S  = line.split(" ");
				weight_[counter] = new double[S.length];
				for(int i=0;i<S.length;i++){
					weight_[counter][i] =  Double.parseDouble(S[i]);
				}
				S = null;
				line = null;
				counter++;
			}
			line = null;
			if(counter != NumberOfweightVector){
					IOException e = new IOException();
					e.printStackTrace();
				}
			} catch (IOException e){
				e.printStackTrace();
		}

	}



	public void config(){

		for(int i=0;i<weight_.length;i++){
			for(int j=0;j<weight_[i].length;j++){
				System.out.print(weight_[i][j] + "	");
			}
			System.out.print("\n");
		}
	}

	public void config_neiborhood(String name) throws IOException{
		File file = new File(name);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		for(int i=0;i<neiborhood_.length;i++){
			for(int j=0;j<neiborhood_[i].length;j++){
				pw.print(neiborhood_[i][j] + " ");
			}
			pw.println("\n");
		}
		for(int i=0;i<neiborhood_.length;i++){
			for(int j=0;j<neiborhood_[i].length;j++){
				System.out.print(neiborhood_[i][j] + " ");
			}
			System.out.print("\n");
		}
	}

}
