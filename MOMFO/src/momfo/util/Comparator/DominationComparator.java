package momfo.util.Comparator;

import javax.naming.NameNotFoundException;

import lib.experiments.CommandSetting;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.math.BuildInRandom;
import momfo.core.Solution;
import momfo.util.Front;
import momfo.util.JMException;
import momfo.util.POINT;

public class DominationComparator extends Comparator{

	public DominationComparator() {
		super();
	}
	
	public void build(CommandSetting st) throws NameNotFoundException, notFoundException {
		super.build(st);
	}
	
	public DominationComparator(boolean d,BuildInRandom random_){
		super(d,random_);
	}

	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution me = (Solution)one;
		Solution you = (Solution)two;

		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return	isDominanceForMIN(me,you);
		}
	}
	public boolean executePoint(POINT me, POINT you) throws JMException {
		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}
	}


	public boolean samePoint(POINT one, POINT two, int d){
		for(int i=0;i<d;i++){
			if(Math.abs(one.get(i) - two.get(i)) > 1.0E-14){
				return false;
			}
		}
		return true;
	}

	public boolean samePoint(POINT one, POINT two){
		assert one.getDimension() == two.getDimension() : "Dimension of First Point is " + one.getDimension() + "	that of Second Point is " + two.getDimension();
 		return samePoint(one,two,one.getDimension());
	}





	public int  isDominanced(Solution me, Solution you){
		assert (isMAX_ == true || isMAX_ == false ) : "you cannot set the Max or Min problem ";

		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}
	}


	public int isDominance(Solution me, Solution you){
		assert (isMAX_== true || isMAX_ == false ) : "you cannot set the Max or Min problem ";

		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}
	}




	// 大きいほうがいい
	public int isDominanceForMAX(Solution me , Solution you){
			int size = me.getNumberOfObjectives();
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) < you.getObjective(i) ){
					return -1;
				}
			}
			for(int i = 0;i< size;i++){
				if(me.getObjective(i) > you.getObjective(i) ){
					return 1;
				}
			}
			return 0;
	}



	//lower Objective value  is better than high Objective Value;
	public int isDominanceForMIN(Solution me , Solution you){
		int size = me.getNumberOfObjectives();
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) > you.getObjective(i) ){
				return -1;
			}
		}

		//
		for(int i = 0;i< size;i++){
			if(me.getObjective(i) < you.getObjective(i) ){
				return 1;
			}
		}
		return 0;
	}


	// 大きいほうがいい
	public boolean isDominanceForMAX(POINT me , POINT you){
			int size = me.getDimension();
			for(int i = 0;i< size;i++){
				if(me.get(i) < you.get(i) ){
					return false;
				}
			}

			//
			for(int i = 0;i< size;i++){
				if(me.get(i) > you.get(i) ){
					return true;
				}
			}
			return false;
	}

	//lower Objective value  is better than high Objective Value;
	public boolean isDominanceForMIN(POINT me , POINT you){

		int size = me.getDimension();
		for(int i = 0;i< size;i++){
			if(me.get(i) > you.get(i) ){
				return false;
			}
		}

		//
		for(int i = 0;i< size;i++){
			if(me.get(i) < you.get(i) ){
				return true;
			}
		}
		return false;
	}


	/*
	 *if  any POINT in Front d  dominates POINT me , this function return true;
	 */
	public boolean anydominance(POINT me, Front d){
		assert d.size()> 0 :"Front doesnt have the POINT";
		assert d.getDimension() == me.getDimension() :"Objective's dimension on Front and POINT have differenct";
		int size = d.getDimension();
		for(int i=0;i<d.size();i++){
			if (isDominance(d.get(i), me)){
				return true;
			}
		}
		return false;
	}

	public void eraseDominancedPOINT(POINT me, Front d){
		if (d.size() ==0)
			return;

		assert me.getDimension() == d.getDimension() :"two objective dimension are different";

		for(int i=0;i< d.size();i++){
			if (isDominance(me,d.get(i))){
				d.remove(i);
				i--;
			}
		}
	}

	public boolean isNondominanced(POINT me,POINT you){
		assert me.getDimension() == you.getDimension() : "the dimension of Objectives of two POINT are different,  one is " + me.getDimension() + " another is " + you.getDimension();

		if (isDominance(me,you))
			return false;

		if (isDominance(me,you))
			return false;

		return true;
	}

	public boolean isNondominance(POINT me,Front you){
		assert you.size() > 0 : "the Front doesn't have POINT";

		assert me.getDimension() == you.getDimension() : "the dimension of Objectives of Front and POINT are different,  one is " + me.getDimension() + " another is " + you.getDimension();

		for(int i=0;i<you.size();i++){
			if (!isNondominanced(me,you.get(i))){
				return false;
			}
		}

		return true;
	}


	public boolean isDominance(POINT me, POINT you){
		assert (me.getDimension() == you.getDimension()) : "two point have different dimenstion size";
		assert (isMAX_ == true || isMAX_ == false ) : "you have not yet set the Max or Min problem ";

		if (isMAX_){
			return isDominanceForMAX(me,you);
		} else {
			return isDominanceForMIN(me,you);
		}
	}




	public static boolean equal(POINT me, POINT you){
		assert me.getDimension() == you.getDimension() : "first POINT's Dimension is  " +  me.getDimension() +"second POINT's Dimension is  " +  you.getDimension();
		int size = me.getDimension();

		for(int i=0;i<size;i++){
			if(Math.abs(me.get(i) - you.get(i)) > 1.0E-14){
				return false;
			}
		}
		return true;
	}


   //0:equal, 1:me dominates you , 2: you dominates me, 3:two points are nonDominated
	public int isDominancedOrEqual(POINT me,POINT you){

		if(equal(me,you)) return 0;       	//two Point is same
		if(isDominance(me,you)) return 1;//Point1 is dominating Point2
		if(isDominance(you,me)) return 2;//Point1 is dominating Point1

		return 3; // two Point is nonDominated


	}


























}
