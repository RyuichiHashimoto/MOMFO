package momfo.operators.selection.ParentsSelection;

import java.util.HashMap;

import momfo.core.Population;

public class RandomSelectionWithReplacement extends ParentsSelection{
	public RandomSelectionWithReplacement(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	/**
	 * Performs the operation
	 *
	 * @param object
	 *            Object representing a SolutionSet.
	 * @return an object representing an array with the selected parents
	 */
	public Object execute(Object object) {
		Population population = (Population) object;
		int perm;

		perm = random.nextIntII(0, population.size() - 1);

		return perm;
	} // Execute


}
