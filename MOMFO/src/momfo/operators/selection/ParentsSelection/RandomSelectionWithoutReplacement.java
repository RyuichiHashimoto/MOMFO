package momfo.operators.selection.ParentsSelection;

import java.util.HashMap;

import momfo.core.Population;


/*
 * 非復元抽出
 *
 */

public class RandomSelectionWithoutReplacement extends ParentsSelection {
	public RandomSelectionWithoutReplacement(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	public Object execute(Object object) {
		Population population = (Population) object;
		assert population.size() >1 : "population size is " + population.size();
		int[] perm = new int[2];
		perm[0] = random.nextIntII(0, population.size() - 1);
/*		perm[1] = Random.nextIntII(0, population.size() - 1);
		while ((perm[1] == perm[0])) {
			perm[1] = Random.nextIntII(0, population.size() - 1);
		}
*/		return perm[0];
	} // Execute


}
