package momfo.util.Comparator;

import momfo.util.JMException;

public class NormalComparator extends Comparator{

	public NormalComparator(boolean is) {
		super(is);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		assert (one instanceof Double): "Object one is not Double";
		assert (one instanceof Double): "Object two is not Double";

		Double int_one = (Double)one;
		Double int_two = (Double)two;

		if( int_one < int_two ){
			return 1;
		} else if (int_one > int_two){
			return -1;
		}

		return 0;
	}

}
