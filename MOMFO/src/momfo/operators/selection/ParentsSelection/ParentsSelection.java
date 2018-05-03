package momfo.operators.selection.ParentsSelection;

import java.util.HashMap;

import momfo.operators.selection.Selection;
import momfo.util.Comparator.Comparator;

public abstract class ParentsSelection extends Selection{


	protected Comparator comparator_;

	public ParentsSelection(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public ParentsSelection(HashMap<String, Object> parameters,Comparator d) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
		comparator_ = d;
	}

	public void setComparator(Comparator d){
		comparator_ = d;
	}

}
