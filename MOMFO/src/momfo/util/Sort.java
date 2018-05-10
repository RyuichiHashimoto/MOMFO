package momfo.util;

import java.util.Random;

import lib.math.BuiltInRandom;
import lib.math.Permutation;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.NormalComparator;

/*
 *
 *
 *
 */


public class Sort {


	public static int[] random_sort(int[] Sort,BuiltInRandom random){
		int size = Sort.length;
		int[] list = new int[size];
		for(int i=0;i<size;i++){
			list[i] = Sort[i];
		}
		for(int i = size - 1;i > 0; i--){
			int j = random.nextIntIE(i+1);
			if (i != j ){
				int t = list[i];
				list[i]  = list[j];
				list[j]  = t;
			}
		}
		return list;
	}
/*
	public static void QuickSort(Object[] obj, Comparator comp,int start , int end) throws JMException{
		if(end - start <=0){
			return ;
		}
		int l = start; int r = end;
		int pipot = (end+start)/2;

		while(l<=r){
			while (comp.execute(obj[pipot],obj[l]) == 1){ l++;}
			while (comp.execute( obj[r],obj[pipot]) == 1){ r--;}


			if(l > r) break;
			Object d = obj[l];
			obj[l] = obj[r];
			obj[r] = d;
			l++; r--;

		}
		QuickSort(obj,comp,start,r);
		QuickSort(obj,comp,l,end);
	}
*/

	public static void QuickSort(Object[] obj,int[] perm, Comparator comp,int left , int right) throws JMException{
		 if (left>=right) {
	            return;
	        }


		int pivot = left;
	    int curleft = left, curright = right;

	    do {
            while(comp.execute( obj[perm[curleft]] , obj[perm[pivot]]) == 1 ) {  curleft++; }
            while(comp.execute( obj[perm[pivot]] , obj[perm[curright]]) == 1 ) { curright--; }
            if (curleft <=curright) {
                int temp = perm[curleft];
                perm[curleft] = perm[curright];
                perm[curright] = temp;
                curleft++; curright--;
            }
        }while(curleft<=curright);

	    QuickSort(obj,perm,comp ,left, curright);  // ピボットより左側をクイックソート
    	QuickSort(obj,perm,comp ,curleft, right); // ピボットより右側をクイックソート
	}
	public static void QuickSort(Object []  d,Comparator comparator ,int left, int right) throws JMException {
		 if (left>=right) {
	            return;
	        }

		  int pivot = left;
	        int curleft = left, curright = right;

	        do {
	            while(comparator.execute( d[curleft] , d[pivot]) == 1 ) {  curleft++; }
	            while(comparator.execute( d[pivot] , d[curright]) == 1 ) { curright--; }
	            if (curleft <=curright) {
	                Object temp = d[curleft];
	                d[curleft] = d[curright];
	                d[curright] = temp;
	                curleft++; curright--;
	            }
	        }while(curleft<=curright);

	    	QuickSort(d,comparator ,left, curright);  // ピボットより左側をクイックソート
	    	QuickSort(d,comparator ,curleft, right); // ピボットより右側をクイックソート
	 }

	//
	public static double[][] Decreasing_sort(double[][] asg, int key){
		assert asg.length >0 : "Decresing_sort in Decresingsort :: ret have no size";
		assert asg[0].length >key : "Decresing_sort in Decresingsort :: the double[] ret have no long array ";

		double[][] ret = asg.clone();

		for(int i=0;i<ret.length-1;i++){
			for(int j=i+1;j<ret.length;j++){
					if (ret [i][key] < ret [j][key]){

						Swap.Swap(ret[i],ret[j]);
					}
			}
		}
		return ret;
	}


/*
	public static void quick_sort(int[] perm,Object []  d,Comparator comparator ,int left, int right) throws JMException {
		 if (left>=right) {
	            return;
	        }

	        int p = (left+right)/2;
	        int l = left, r = right, tmp;

	        while(l<=r) {
	            while(comparator.execute( d[perm[l]] , d[perm[p]]) == 1 ) {  l++; }
	            while(comparator.execute( d[perm[r]] , d[perm[p]]) == -1 ) { r--; }
	            if (l<=r) {
	                tmp = perm[l]; perm[l] = perm[r]; perm[r] = tmp;
	                l++; r--;
	            }
	        }
	        quick_sort(perm,d,comparator ,left, r);  // ピボットより左側をクイックソート
	        quick_sort(perm,d,comparator ,l, right); // ピボットより右側をクイックソート
	 }
*/


	public static void QuickSort_(Double []  d,Comparator comparator ,int left, int right) throws JMException {
		if(left>right){
			return ;
		}

	        int pivot = left;
	        int curleft = left, curright = right;
	        double temp;

	        do {
	            while(d[curleft] < d[pivot]) {  curleft++; }
	            while(d[curright] > d[pivot]) { curright--; }

	            if (curleft<=curright){
	            	temp = d[curleft];
	            	d[curleft] = d[curright];
	            	d[curright] = temp;

	            	curleft++; curright--;
	            }
	        }while(curleft<=curright);


	        	QuickSort_(d,comparator ,left, curright);  // ピボットより左側をクイックソート
	    	   QuickSort_(d,comparator ,curleft, right); // ピボットより右側をクイックソート
	 }

/*
	 public static void main(String args[]){




	 }

*/

	public static void sort(Object[]  sort, Comparator d ) throws JMException{
		int size = sort.length;
		for(int i =0;i < size-	1 ;i ++){
			for(int j=1;j<size;j++){
				if(d.execute(sort[i] ,sort[j]) == 1){
					Object empty = sort[i];
					sort[i] = sort[j];
					sort[j] = empty;
				}
			}
		}
	}

	public static Double[] random_sort(double[] Sort,BuiltInRandom radom){
		int size = Sort.length;

		Double[] list = new Double[size];
		for(int i=0;i<size;i++){
			list[i] = Sort[i];
		}
		for(int i = size - 1;i > 0; i--){
			int j = radom.nextIntIE(i+1);
			if (i != j ){
				double t = list[i];
				list[i]  = list[j];
				list[j]  = t;
			}
		}
		return list;
	}

	public static Double[] random_sort(Double[] Sort,BuiltInRandom random){
		int size = Sort.length;

		Double[] list = new Double[size];
		for(int i=0;i<size;i++){
			list[i] = Sort[i];
		}
		for(int i = size - 1;i > 0; i--){
			int j = random.nextIntIE(i+1);
			if (i != j ){
				double t = list[i];
				list[i]  = list[j];
				list[j]  = t;
			}
		}
		return list;
	}

	private static boolean check(Double[] a,int[] perm,Double[]b){
		for(int i=0;i<a.length;i++){
			if( Math.abs(a[perm[i]] - b[i]) > 1.0E-14){
				return false;
			}
		}
		return true;
	}


	private static boolean check_quick_Sort(Object[] obj, Comparator comp,int left , int right) throws JMException {
		int pipot = (left + right)/2;

		int test = 0;
		boolean flag = false;
		for(int i=0;i<obj.length;i++){
			test =0;

//			if(i < pipot)
				if( comp.execute(obj[pipot], obj[i]) == 1){
					System.out.println("before");
				}

				if( comp.execute(obj[i], obj[pipot]) == 1){
					System.out.println("after");
//					flag = false;
//					System.out.println("after");
//					if(test == 1){
//						System.out.println("");
//					}
				}

		}
		return flag;
//		return flag;
	}

	public static void main(String[] args) throws JMException{

		//		Permutation.randomPermutation(ret);
	int numberOfTasks = 10;
	for(int t = 0;t<numberOfTasks;t++){
		BuiltInRandom random = new BuiltInRandom(); 
		Random.set_seed(t);
		int size_of_list = 200;
		Double[] answer = new Double[size_of_list];
		for(int i=0;i<size_of_list;i++){
			answer[i] = (Double)(i+1.0);//1.0/(i+1);
		}

		Double[] ret = random_sort(answer);

		int[] perm = new int[ret.length];

		Permutation.setPermutation(perm);
		Comparator comp = new NormalComparator(true);

		double d = ret[ret.length/2];
		System.out.println("敷居値"+ d);

//		QuickSort(ret,comp,0 , 199) ;
		QuickSort(ret,perm,comp,0 , 199) ;


		for(int i=0;i<ret.length;i++){
	//		System.out.println(ret[i]);
		}


		if(check(ret,perm,answer)){
			System.out.println("OK");
		} else {
			System.out.println("落第");
		}
	}
		/*



		for(int i=0;i<3;i++){
			System.out.print(a[i][0] + "  "+a[i][1] + "  "+a[i][2] + "\n");
		}


		for(int i=0;i<3;i++){
			System.out.print(ret[i][0] + "  "+ret[i][1] + "  "+ret[i][2] + "\n");
		}
	*/}


	public static void sortUsingObjevtiveForAscedingOrader(Population pop, int key){
		for(int i = 0;i<pop.size() - 1 ;i++){
			for(int j =i;j<pop.size();j++){
				if(pop.get(i).getObjective(key) >  pop.get(j).getObjective(key)){
					Solution d = new Solution(pop.get(i));
					pop.replace(i ,new Solution( pop.get(j)));
					pop.replace(j,d);
				}
			}
		}
	}

	public static void sortUsingObjevtiveForAscedingOrader(Front pop,int key){
		for(int i = 0;i<pop.size() - 1 ;i++){
			for(int j =i;j<pop.size();j++){
				if(pop.get(i).get(key) >  pop.get(j).get(key)){
					POINT d = new POINT(pop.get(i));
					pop.replace(i ,new POINT( pop.get(j)));
					pop.replace(j,d);
				}
			}
		}
	}
	public static int[] sortUsingObjevtiveForAscedingOraderperm(Front pop,int key){
		int[] perm = new int[pop.size()];
		Permutation.setPermutation(perm);


		for(int i = 0;i<pop.size() - 1 ;i++){
			for(int j =i;j<pop.size();j++){
				if(pop.get(perm[i]).get(key) >  pop.get(perm[j]).get(key)){
					int emp = perm[i];
					perm[i] = perm[j];
					perm[j] = emp;
				}
			}
		}
		return perm;
	}

	public static void sortUsingObjevtiveForAscedingOraderWithID(Population pop, int key){
		for(int i = 0;i<pop.size() - 1 ;i++){
			for(int j =i+1;j<pop.size();j++){
				if(pop.get(i).getObjective(key) >  pop.get(j).getObjective(key)){
					Solution d = new Solution(pop.get(i));
					pop.replace(i ,new Solution( pop.get(j)));
					pop.replace(j,d);
				} else if(  Math.abs( pop.get(i).getObjective(key)  -  pop.get(j).getObjective(key) )< 1.0E-14 &&  pop.get(i).getID() > pop.get(j).getID()) {
					Solution d = new Solution(pop.get(i));
					pop.replace(i ,new Solution( pop.get(j)));
					pop.replace(j,d);
				}
			}
		}
	}

/*
	public static Population nonDominatedPopulation(Population a){

		Population ret = new Population ();
		for(int i=0;i<a.size();i++){
			boolean judge = true;
			Solution p = a.get(i);
			for(int j=0;j<a.size();j++){
				Solution q = a.get(j);
				if(Dominance.isDominanced(q, p)){
					judge = false;
				}
			}
			if(judge){
				ret.add(p);
			}
		}
		return ret;
	}









	public static Population  nonDominatedSort(Population pop, boolean isMAX){
		Dominance.set(isMAX);

		Population ret = new Population();
		for(int i=0;i<pop.size();i++){
			boolean judge = true;
			Solution p = pop.get(i);
			for(int j=0;j<pop.size();j++){
				Solution q = pop.get(j);
				if(Dominance.isDominanced(q, p)){
					judge = false;
				}
			}
			if(judge){
				ret.add(p);
			}
		}
		return ret;
	}

*/
}
