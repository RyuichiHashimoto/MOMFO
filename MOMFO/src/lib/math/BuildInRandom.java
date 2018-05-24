package lib.math;

import java.io.Serializable;

public class BuildInRandom implements Serializable {
	private MersenneTwisterFast random = new MersenneTwisterFast(1995);

	int seed = 0;

	public void setSeed(int s){
		random.setSeed(s);

//		random = new MersenneTwisterFast(s);
	}

	public BuildInRandom(int seed){
		this.setSeed(seed);
	}



	public  double nextDoubleII(){
		return random.nextDoubleII();
	}

	public  double nextDoubleIE(){
		return random.nextDoubleIE();
	}
	public  double nextDoubleEI(){
		return random.nextDoubleEI();
	}
	public  double nextDoubleEE(){
		return random.nextDoubleEE();
	}

	public  int nextInt(){
		return random.nextInt();
	}

	public  int nextIntIE(int a){
		return random.nextInt(a);
	}

	public  int nextIntII(int a){
		return random.nextInt(a+1);
	}

	public  int nextIntIE(int a, int b ){
		return a + nextIntIE(b-a);
	}
	public  int nextIntII(int a, int b ){
		return a + nextIntIE(b -a + 1);
	}

	public  int nextInt(int a ){
		return random.nextInt(a);
	}

	public  boolean nextBoolean(){
		return random.nextBoolean();
	}

	public  boolean nextBoolean(double pro){
		return random.nextBoolean(pro);
	}



}
