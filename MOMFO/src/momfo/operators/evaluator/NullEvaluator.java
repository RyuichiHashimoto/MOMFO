package momfo.operators.evaluator;

public class NullEvaluator extends Evaluator{

	@Override
	public void evaluate(Object d) {
		flag = false;
	}

	public NullEvaluator() {

	}

}
