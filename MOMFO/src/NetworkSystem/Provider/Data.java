package NetworkSystem.Provider;


/*
 * ネットワーク通信の転送データ(主に個体群または個体)クラス．
 * 実験結果を相手に返す時のクラス
 *
 *
 *
 */

public class Data<T> {

	String name = "";

	String Type;

	int CONDITION;

	boolean isEmpty;

	private final Object data;

	public Data(T d,int CONDITION){
		data =d;
	}
	
	public T get(){
		return (T)data;
	}

	public int getCondition(){
		return CONDITION;
	}
}



