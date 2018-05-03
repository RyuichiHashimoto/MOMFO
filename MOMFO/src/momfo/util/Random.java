package momfo.util;

import momfo.util.MersenneTwister.MersenneTwisterFast;

/*
 *
 *
 */

public class Random {

	private static MersenneTwisterFast random = new MersenneTwisterFast(1995);

	public static void set_seed(int s){
		random = new MersenneTwisterFast(s);
	}


	public static double nextDoubleII(){
		return random.nextDoubleII();
	}

	public static double nextDoubleIE(){
		return random.nextDoubleIE();
	}
	public static double nextDoubleEI(){
		return random.nextDoubleEI();
	}
	public static double nextDoubleEE(){
		return random.nextDoubleEE();
	}

	public static int nextInt(){
		return random.nextInt();
	}

	public static int nextIntIE(int a){
		return random.nextInt(a);
	}

	public static int nextIntII(int a){
		return random.nextInt(a+1);
	}

	public static int nextIntIE(int a, int b ){
		return a + nextIntIE(b-a);
	}
	public static int nextIntII(int a, int b ){
		return a + nextIntIE(b -a + 1);
	}

	public static int nextInt(int a ){
		return random.nextInt(a);
	}

	public static boolean nextBoolean(){
		return random.nextBoolean();
	}

	public static boolean nextBoolean(double pro){
		return random.nextBoolean(pro);
	}






}
