package momfo.util;

/*
 * this implement is used for the Swap
 *
 *
 */

public class Swap {




	public static void Swap(double[] a , double[] b){
		assert (a.length == b.length) : "Swap in Swap Class :: two Argment must have same array length";
		for (int i=0;i<a.length;i++){
			double empty = b[i];
			b[i] = a[i];
			a[i] = empty;
		}
	}



	public static void main(String args[]){
		double [] oen = {1.4, 4.2};
		double [] two = {3.1, 1.215};

		Swap(oen, two);
		System.out.println(oen[0] + "	" +oen[1] );
		System.out.println(two[0] + "	" +two[1] );

	}
}
