package momfo.operators.evaluator;

import java.io.IOException;
import java.io.Writer;

import Network.GridComputing.StreamProvider;

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

	@Override
	public void save(StreamProvider streamProvider) throws IOException {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
