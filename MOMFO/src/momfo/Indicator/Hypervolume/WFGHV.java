 package momfo.Indicator.Hypervolume;

import java.util.HashMap;

import lib.math.BuiltInRandom;
import momfo.core.Population;
import momfo.core.Solution;
import momfo.util.Front;
import momfo.util.JMException;
import momfo.util.POINT;
import momfo.util.ReferencePoint;
import momfo.util.Sort;


/*
 *********************************************
   *              !!!caution!!!              *　
 *Each point of Front in Objective space     *
 *used in this class must have non Dominated *
 *Relation with each other                   *
 *********************************************
 */

/*
　*
 * このパッケージのファイルと基底クラスのIndicatorがあればHyperVolumeは実行可能のはずなので，もし
 * このプログラムぜひ使いたいという人がいれば，そこの部分だけ切り取ればＯＫ
 *　このクラスのメイン関数を実行すると，HVの合わせに用いたファイルを読み込み算出する．
 *  コードを書いた私は，プログラミング大好きだけどそれほどスキルないので，実行速度が遅いでしょう
 *　なので，早くしたいならこのソースコードを勝手に変えてください．
 */

public class WFGHV extends HypervolumeCalculator {

	int nOfD_;



	public WFGHV(double[] ReferencePoint,BuiltInRandom random){
		super(ReferencePoint,random);
	}
	public WFGHV() {
		super(null);
	}




	public double HV2D(Population ind){
		return HV2D(new Front(ind));
	}


	private POINT limit(POINT r, POINT limited) {
		assert r.getDimension() >= nObj : "size of r is " + r.getDimension() + "	size of limited is" + limited.getDimension();
		assert r.getDimension() >= nObj : "size of r is " + r.getDimension() + "	size of limited is" + limited.getDimension();

		double[] retval = new double[r.getDimension()];
		for (int i = 0; i < nObj; i++) {
			retval[i] = Dominator.worse(r.get(i), limited.get(i));
		}
		return new POINT(retval);
	}






	//0:equal, 1:me dominates you , 2: you dominates me, 3:two points are nonDominated
	private Front wfgNonDominatedLimitSet(Front set, POINT limit ) throws JMException {
		Front retval = new Front(set);
		retval.clear();

		Front d = new Front(set);
		for(int i=0;i<set.size();i++){
			if(retval.size() == 0){
				retval.add(limit(d.get(i),limit));
				continue;
			}
			POINT newpoint = new POINT(limit(set.get(i), limit));
			for(int j=0;j<retval.size();j++){
				if(Dominator.executePoint(newpoint,retval.get(j) ))
					retval.remove(j--);
			}

			boolean flag = true;

			for(int j=0;j<retval.size();j++){
				if(Dominator.executePoint(retval.get(j), newpoint) || Dominator.samePoint( newpoint,retval.get(j))){
					flag = false;
					break;
				}
			}
			if(flag){
				retval.add(new POINT(newpoint));
			}
		}
		return retval;
	}


	public boolean checkDuplication(Front d,POINT t){
		int count = 0;

		for(int i=0;i < d.size(); i++){
			count = 0;
			for(int j=0;j<d.getDimension();j++){
				if(Math.abs(d.get(i).get(j) - t.get(j)) > 1.0E-14 ){
					break;
				} else {
					count++;
				}
			}
			if(count == d.getDimension()){
				return true;
			}
		}
		return false;
	}
	private void removeOutOfReferencePoint(Front d)  throws JMException {
		for(int i=0; i < d.size();i++){

			if(!(Dominator.execute(new Solution( d.get(i) ) ,new Solution (new POINT(referencePoint_) ) ) == 1 )){
				d.remove(i--);
			}
		}
	}

	public boolean checker(Front d,POINT test) throws JMException {
		for(int i=0;i<d.size();i++) {
			if( !Dominator.samePoint(d.get(i), test)  && !(Dominator.execute(new Solution(test), new Solution( d.get(i) ) )==1) ) {
				return false;
			}
		}

		return true;
	}

	public Front limitSet(Front d,POINT now) {
		Front ret = new Front(d);
		for(int i=0;i<d.size();i++) {
			for(int j=0;j<d.getDimension();j++) {
				POINT temp = d.get(i);
				ret.get(i).set(j,Dominator.worse(temp.get(j) ,now.get(j)));
			}

		}
		return ret;
	}


// this function is not used by calculating the HyperVolume;
// this Function is used in SMS-EmomfoA to calculating HperVolumeContribution
// wfgNonDominatedLimitSet_test(d,emp) これを訂正しよう
	public double exclhv(Front front ,int index_,double[] ref, boolean isMAX) throws JMException{
		nObj = front.getDimension();
		referencePoint_ =  new ReferencePoint(ref);
		Front d = new Front(front);

		if (!isMAX){
			d.getInverse();
			referencePoint_.setInverse();
		}
		setMAXProblem();

		double	retval;
		POINT emp = new POINT(d.get(index_));

		//参照点を優越していないと値は0
		if(!Dominator.executePoint(emp, referencePoint_) ) {
			return 0.0;
		}
		d.remove(index_);
		removeOutOfReferencePoint(d);
		d =	wfgNonDominatedLimitSet(d,emp);

		if(d.size() == 0) {
			retval = inclhv(emp);
		} else if (d.size() == 1){
			retval = inclhv(emp) - inclhv(d.get(0));
		}else {
			retval = inclhv(emp) - wfg(d,referencePoint_,true);
		}

		return retval < 0 ? 0:retval ;
	}

	public double HV2D(Front front){
		assert front.size() > 0 : "the size of Front is " + front.size();

		Sort.sortUsingObjevtiveForAscedingOrader(front, 0);
		double retval = 0;;

		retval = ((front.get(0).get(0) - referencePoint_.get(0)) *(front.get(0).get(1) - referencePoint_.get(1)));
		for(int i=1;i<front.size();i++){
			retval += ((front.get(i).get(0) - front.get(i-1).get(0))*(front.get(i).get(1) - referencePoint_.get(1)));
		}

		assert retval >= 0 : retval;

		return retval;
	}

	public double wfghv(Front a) throws JMException{
		assert a.size() > 0 : "the size of Front is " +  a.size();
		assert a.getDimension() >= nObj : "the Dimension of Front is "  + a.getDimension();

		Front ret = a;
		if(ret.size() == 1) return inclhv(ret.get(0));
		if(ret.size() == 2) return iex2(ret);

		if(nObj == 2) return HV2D(ret);
		nObj--;
		ret.Decreasing_Sort(nObj);
		double retval = 0;
		while(2 < ret.size()){
			POINT emp = ret.pop();
			retval += (emp.get(nObj) - referencePoint_.get(nObj)) * (inclhv(emp) - wfghv(wfgNonDominatedLimitSet(ret, emp)));
		}
		nObj++;
		if (ret.size() == 1){
			retval += inclhv(ret.get(0));
		} else if (ret.size() == 2) {
			retval += iex2(ret);
		} else {
			assert false : "Something is wrong";
		}

		return retval;
	}

	public double wfg(Front front_, double[] d,boolean isMAX) throws JMException{
		return wfg(front_,new ReferencePoint(d),isMAX);
	}

	public double wfg(Front front_, ReferencePoint d,boolean isMAX) throws JMException{
		assert front_.size() > 0 : "the size of Front is " + front_.size();
		assert front_.getDimension() > 1 : "the Objective of Front is "  + front_.size();
		assert front_.getDimension() == d.getDimension() : "the Objective of Front is "  + front_.getDimension()   + " and the referencePoint dimension is " + d.getDimension();

		nObj = d.getDimension();
		referencePoint_ =  new ReferencePoint(d);

		if (!isMAX){
			front_.getInverse();
			referencePoint_.setInverse();
		}
		setMAXProblem();

		removeOutOfReferencePoint(front_);
		if(front_.size() == 0) return 0;


		return wfghv(front_);
	}

	public void reset(Front d, ReferencePoint f){
		assert d.getDimension() == f.getDimension() : "the dimension of Front is " + d.getDimension() + " the dimension of ReferencePoint is " +f.getDimension();
		for(int i=0;i<d.size();i++){
			POINT em = d.get(i);

			for(int j=0;j<em.getDimension();j++){
				em.set(j,em.get(j) - f.get(j));
			}
		}
		for(int j=0;j<f.getDimension();j++){
			f.set(j,0);
		}
	}


	//true なら最大化，　false なら最小化
	public static void main(String[] args){
		System.out.print("start");
		tester.test(true,"greedyselection3.csv");
	}




	@Override
	public Object execute(Population ind, HashMap<String, Object> d) throws JMException {
		int[] perm = ind.sortObjectivereturnperm(0);
		Population ret = new Population(ind);
		return wfg(new Front(ret),referencePoint_,Dominator.get());
	}

	@Override
	public String getIndicatorName() {
		return "WFGHV";
	}


	public Object execute(Front d, Object d2) throws JMException{
		return wfg(new Front(d),referencePoint_,Dominator.get());
	}


}
