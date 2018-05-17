package momfo.operators.evaluator;

import java.io.Writer;

public class NullEvaluator extends Evaluator{

	@Override
	public void evaluate(Object d) {
		flag = false;
	}

	public NullEvaluator() {
		flag = false;
	}
	
	public void save(Writer st){
		return;
	}

	@Override
	public void initialize() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
