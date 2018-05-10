package lib.math;


/**
 * Class providing int [] permutations
 */
public class Permutation {

	/**
	 * Return a permutation vector between the 0 and (length - 1)
	 */


	public static int[]	setPermutation(int[] list){
		for(int i=0 ;i<list.length;i++){
				list[i] = i;
		}
		return list;
	}


	public static int[]  randomPermutation(int[] list, int size,BuildInRandom Random){
		int[] 	array = new int[size];

		for(int i = 0;i < size ;i++){
			array[i] =  i;
		}

		int r;
		int temp;

		for(int i=size - 1;i>0;i--){
			r = Random.nextIntII(0,size-1);
			temp = array[i];
			array[i]= array[r];
			array[r]= temp;
		}
		for(int i = 0;i < size ;i++){
			list[i] =  array[i];
		}


		return list ;
	}
	public static int[]  randomPermutation(int[] list,BuildInRandom random){
		int size = list.length;
		int[] 	array = new int[size];

		for(int i = 0;i < size ;i++){
			array[i] =  i;
		}

		int r;
		int temp;

		for(int i=size - 1;i>0;i--){
			r = random.nextIntIE(0,size);
			temp = array[i];
			array[i]= array[r];
			array[r]= temp;
		}
		for(int i = 0;i < size ;i++){
			list[i] =  array[i];
		}


		return list ;
	}




 	public int[] intPermutation(int length,BuildInRandom Random) {
		int[] aux = new int[length];
		int[] result = new int[length];

		// First, create an array from 0 to length - 1. We call them result
		// Also is needed to create an random array of size length
		for (int i = 0; i < length; i++) {
			result[i] = i;
			aux[i] = Random.nextInt(length);
		} // for

		// Sort the random array with effect in result, and then we obtain a
		// permutation array between 0 and length - 1
		for (int i = 0; i < length; i++) {
			for (int j = i + 1; j < length; j++) {
				if (aux[i] > aux[j]) {
					int tmp;
					tmp = aux[i];
					aux[i] = aux[j];
					aux[j] = tmp;
					tmp = result[i];
					result[i] = result[j];
					result[j] = tmp;
				} // if
			} // for
		} // for
		return result;
	}
}