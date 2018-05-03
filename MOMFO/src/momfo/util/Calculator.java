package momfo.util;

public class Calculator {

	public static int pow(int a,int b){
		int ret = 1;
		for(int i =0 ;i<b;i++){
			ret = ret*a;
		}
		return ret;
	}

	public static int Factorial(int n){
		int ret = 1;

		for(int i = 2;i<=n ;i++){
			ret = ret*i;
		}
		return ret;
	}

	public static int conbination(int n , int r){
		int ret = 1;
		for(int i = 1;i<= r;i++){
			ret =  ((n - i + 1)* ret )/i;
		}
		return ret;
	}



}
