package momfo.operators.selection.ParentsSelection;

import momfo.operators.selection.Selection;
import momfo.util.Comparator.Comparator;

public abstract class ParentsSelection extends Selection{

	protected Comparator comparator_;

	public void setComparator(Comparator d){
		comparator_ = d;
	}

}
