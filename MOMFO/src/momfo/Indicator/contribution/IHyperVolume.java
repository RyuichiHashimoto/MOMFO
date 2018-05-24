package momfo.Indicator.contribution;

import java.util.ArrayList;
import java.util.List;

import lib.experiments.JMException;
import momfo.Indicator.Indicator;
import momfo.Indicator.Hypervolume.WFGHV;
import momfo.core.Solution;
import momfo.util.Front;
import momfo.util.POINT;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.DominationComparator;





/*
 *個体群Vに含まれる個体をvとすると貢献度は以下のように計算している
 *
 *Contribution(v) = inclHV(v) - HV(NonDominatedLimitSet(V/v , v ));
 *
 *V/v：個体vを取り除いた個体群
 *inclHV(v)：WFG(ハイパーボリューム)の提案論文参照
 *NonDominatedLimitSet(S,v)：個体群Sに属するすべての個体が個体vに優越されるように目的関数空間内の位置を変更したもの．
 *具体的にはWFG(ハイパーボリューム)の提案論文参照， 橋本がいるなら橋本に聞いて
 *
 */


public class IHyperVolume  extends Indicator{

	public static Comparator comparator_ = new DominationComparator(false, null);

	private static boolean isMAXproblem_;

	public static void  setMIN(){
		isMAXproblem_ =false;
		comparator_.setMin();
	}
	public static void setMAX(){
		isMAXproblem_ = true;
		comparator_.setMax();
	}

	public static void set(boolean arg){
		if (arg)
			setMAX();
		else
			setMIN();
	}



	// this variable is shallow copy
	public  static double[] referencePoint_;

	public static void setReferencePoint(double[] d){
		referencePoint_ = d;
	}

	public static Comparator getComparator(){return comparator_;}



	//これは参照点を優越できない点を除いていないから少し待つ必要があり．
	public static double[] IHV2D(Front d) throws JMException{
		assert d.size() > 0 : "the Size of Front is " + d.size();

	//	assert false : "実装まだ";

		int[]  perm = new int[d.size()];
		List<Integer> li = null;

//		li = outofReferencePoint(d);

		d.Decreasing_Sort(perm,0);

		double[] ret = new double[d.size()];

		ret[perm[0]] = Double.POSITIVE_INFINITY;
		ret[perm[d.size() - 1]] = Double.POSITIVE_INFINITY;

		for (int i = 1; i < d.size() - 1; i++){
			double r1 = d.get(perm[i + 1]).get(0) - d.get(perm[i]).get(0);
			double r2 = d.get(perm[i - 1]).get(1) - d.get(perm[i]).get(1);
			ret[perm[i]] = r1*r2;
		}

		// for
		//参照点を超えていない場合最悪地を付ける．
//		for(int i=0;i< li.size();i++){
//			ret[li.get(i)] = Double.NEGATIVE_INFINITY;
//		}

		return ret;
	}

	public static double[] IHV2D(double[][] d) throws JMException{
		return IHV2D(new Front(d));
	}

	public static List<Integer> outofReferencePoint(Front d) throws JMException{
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i < d.size();i++){
			if(!(comparator_.execute( new Solution (new POINT(referencePoint_) )  , new Solution( d.get(i) )) == 1) ){
				list.add(i);
			}
		}
		return list;
	}

	public static void removeOutOfReferencePoint(Front d)  throws JMException {
		for(int i=0; i < d.size();i++){
			if(!(comparator_.execute(new Solution( d.get(i) ) ,new Solution (new POINT(referencePoint_) ) ) ==1)){
				d.remove(i--);
			}
		}
	}




	/*//IHSOをやる
	public static double[] IHV3D(Front d) throws JMException{
		assert d.getDimension() == 3 : "the dimension of Front is " + d.getDimension();
		double[] ret = new double[d.size()];

		for(int i=0;i<d.size();i++){
			Front fr = new Front(d);
			ArrayList<Solution> sols = new ArrayList<Solution>();


			for(int j=0;j<d.size();j++){
				Solution AAA = new Solution(d.get(i));
				AAA.setID(j);
				sols.add(AAA);
			}

			IHSO ihso = new IHSO(sols, referencePoint_ , new Solution(d.get(i)));
			ret[i] = ihso.execute();//.exclhv(d,i,referencePoint_,comparator_.get());
			ihso = null;
		}

		return ret;

	}*/



	public static double[] IHV(Front d) throws JMException{
		assert d.size() > 1 : "the size of front is " + d.size();
		assert d.getDimension() > 1 : "the dimension of Front is " + d.getDimension();
		if(d.getDimension() == 2) return IHV2D(d);

		//if(d.getDimension() == 3) return IHV3D(d);

		return  IHVOver4D(d);
	}

	private static double[] IHVOver4D(Front d) throws JMException {
		double[] ret = new double[d.size()];
		WFGHV wfghv_ = new WFGHV(referencePoint_,null);
		// contribution v in S is calclated by the (HV(S) - HV(S/v))
		// HV(S) is same, So we calclated only HV(S/v)
		for(int i=0;i<d.size();i++){
			 wfghv_ = new WFGHV(referencePoint_,null);
			 ret[i] = wfghv_.exclhv(d,i,referencePoint_,comparator_.isMax());
		}
		return ret;
	}


//この実装方法は確かだけど絶対遅い　
//これは貢献度計算の大小関係は同じであるが，値はかならず同じとはかぎらない
/*		private static double[] IHVOver4D(Front d) throws JMException {
		double[] ret = new double[d.size()];
		WFGHV wfghv_ = new WFGHV(referencePoint_);


		// contribution v in S is calclated by the (HV(S) - HV(S/v))
		// HV(S) is same, So we calclated only HV(S/v)
		// これはSMS
		for(int i=0;i<d.size();i++){
			 wfghv_ = new WFGHV(referencePoint_);
			 Front test = new Front(d);
			 test.remove(i);
			 ret[i] = -1* wfghv_.wfg(test,referencePoint_,comparator_.get());
//			 wfghv_ = new WFGHV(referencePoint_);
//			 ret[i] += wfghv_.wfg(new Front(d), referencePoint_, comparator_.get());

//			 assert !(ret[i] <- 1.0E-14) : ret[i];
		}

		return ret;
	}
*/


	public static List<Integer> getLowestContribution(double[] empt) {
		List<Integer> ret = new ArrayList<Integer>();

		ret.add(0);
			for(int i=1;i<empt.length;i++){
			if(empt[ret.get(0)] > empt[i]){
				ret.clear();
				ret.add(i);
			} else if (Math.abs(empt[i] -empt[ret.get(0)]) < 1.0E-14){
				ret.add(i);
			}
		}
		return ret;
	}

	/*
	public static List<Integer> getLowestContribution(double [] contribution_){
		List<Integer> ret = new ArrayList<Integer>();

		ret.add(0);
		for(int i=1;i<contribution_.length;i++){
			if( contribution_[i] < ){
				min = contribution_[i];
				listContribution_.clear();
				listContribution_.add(i);
			} else 	if ( Math.abs(min - contribution_[i] ) < 1.0E-14 ){
				min = contribution_[i];
				listContribution_.add(i);
			}
		}
		return listContribution_;
	}
*/
	public static void main(String[] argv){
		System.out.println("start");

		//double base = 		tester.test(false,"DTLZ3OBJ.csv");
		double[] ret = new double[91];

		for(int i = 0;i < 91;i++){
			String name = "DTLZ3OBJ_" + String.valueOf(i) + ".csv";
			//ret[i] = base - tester.test(false,name);
			System.out.println(ret[i]);
		}



	}









	public List<Integer> execute(){

		return null;
	}

	@Override
	public String getIndicatorName() {
		return "IHyperVolume";
	}






}
