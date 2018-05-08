package momfo.util;

import momfo.util.MersenneTwister.MersenneTwisterFast;

public class BuiltInRandom{
	private MersenneTwisterFast Random = new MersenneTwisterFast(1995);

	public void set_seed(int s){
		Random = new MersenneTwisterFast(s);
	}


	public double nextDoubleII(){
		return Random.nextDoubleII();
	}

	public double nextDoubleIE(){
		return Random.nextDoubleIE();
	}
	public double nextDoubleEI(){
		return Random.nextDoubleEI();
	}
	public double nextDoubleEE(){
		return Random.nextDoubleEE();
	}

	public int nextInt(){
		return Random.nextInt();
	}

	public int nextIntIE(int a){
		return Random.nextInt(a);
	}

	public int nextIntII(int a){
		return Random.nextInt(a+1);
	}

	public int nextIntIE(int a, int b ){
		return a + nextIntIE(b-a);
	}
	public int nextIntII(int a, int b ){
		return a + nextIntIE(b -a + 1);
	}

	public int nextInt(int a ){
		return Random.nextInt(a);
	}

	public boolean nextBoolean(){
		return Random.nextBoolean();
	}

	public boolean nextBoolean(double pro){
		return Random.nextBoolean(pro);
	}
}
