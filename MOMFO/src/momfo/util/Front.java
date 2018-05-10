package momfo.util;

import java.util.ArrayList;
import java.util.List;

import lib.math.Permutation;
import momfo.core.Population;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.DominationComparator;


/*
 * this implement needs For the Hypervolume calculation
 *all Front must be non Dominated Solution set
 */
public class Front {

	private int nOfD_ = -1;

	private List<POINT>  Front_;

	public Front(){
		Front_ = new ArrayList<POINT>();
	}
	public double[][] get(){
		double[][] ret = new double[Front_.size()][nOfD_];
		for(int i=0;i<Front_.size();i++){
			for(int j=0;j<Front_.size();j++){
				ret[i][j] = Front_.get(i).get(j);
			}
		}
		return ret;
	}


	public Front(double[][] Font){
		assert Font.length > 0 : "initialither in Front ::  argment has no size ";
		assert Font[0].length > 0 : "initialither in Front ::  argment  haa no size ";
//		assert isOK(Font) : "initialther in Front :: all Objective Demention of argment Front are not same ";

		nOfD_ = Font[0].length;
		Front_ = new  ArrayList<POINT>();

		for(int i=0;i<Font.length; i++){
			Front_.add(new POINT (Font[i]));
		}
	}

	public Front(int t,int s){
		POINT d = new POINT();
		Front_ = new ArrayList<POINT>();
		Front_.add(d);
		nOfD_ = s;

	}

	public Front(POINT[] Font){
		assert Font.length > 0 : "initialither in Front ::  argment has no size ";
		assert Font[0].getDimension() > 0 : "initialither in Front ::  argment  has no size ";
		assert isOK(Font) : "initialther in Front :: all Objective Demention of argment Front are not same ";

		nOfD_ = Font[0].getDimension();
		Front_ = new  ArrayList<POINT>();

		for(int i=0;i<Font.length; i++){
			Front_.add(new POINT (Font[i]));
		}
	}


	public void assignID(){
		for(int i=0;i< Front_.size();i++){
			Front_.get(i).setID(i);
		}
	}

	public Front(Population d){
		this(d.getAllObjectives());
	}

	public Front(double[][] Font, double[] ref, boolean isMAX){
		this(new Front(Font),new ReferencePoint(ref),isMAX);
	}

	public Front(Front Font, double[] ref, boolean isMAX){
		this(Font,new ReferencePoint(ref),isMAX);
	}

	public Front(double[][] Font, ReferencePoint ref, boolean isMAX){
		this(new Front(Font),ref,isMAX);
	}

	public Front(Population Font, ReferencePoint ref, boolean isMAX){
		this(new Front(Font) , ref, isMAX);
	}

	// this implement return the Front , all  Point in Front dominates ref;
	public Front(Front Font, ReferencePoint ref, boolean isMAX){
		assert Font.size() > 0 :"Front have no size";
		assert Font.getDimension() == ref.getDimension() :"Front and ReferencePoint have difference size";

		nOfD_ = Font.getDimension();
		Front_ = new  ArrayList<POINT>();
		boolean isOK = true;
		if (isMAX){
			for (int i = 0 ;i < Font.size();i++){
				isOK = true;
				for (int j=0;j< Font.getDimension(); j++){
					if (Font.get(i).get(j) < ref.get(j)){
						isOK = false;
					}
				}
				if (isOK){
					Front_.add(new POINT(Font.get(i)));
				}
			}
		} else {
			for (int i = 0 ;i < Font.size();i++){
				isOK = true;
				for (int j=0;j< Font.getDimension(); j++){
					if (Font.get(i).get(j) > ref.get(j)){
						isOK = false;
					}
				}
				if (isOK){
					Front_.add(new POINT (Font.get(i)));
				}
			}
		}

	}


	public Front(Front Font){
		Front_ = new  ArrayList<POINT>();
		nOfD_ = Font.getDimension();
		for(int i=0;i<Font.size(); i++){
			Front_.add(new POINT (Font.get(i)));
		}
	}

	public void remove(int key){
		Front_.remove(key);
	}

	public void replace(int key, double[] a){
		replace(key, new POINT(a));
	}
	public void replace(int key, POINT a){
		assert Front_.size() > key : "the index is " + key + "	Front size is " + Front_.size();
		Front_.remove(key);
		Front_.add(key,new POINT(a));
	}

	public void set(int key,double[] ret){
		set(key, new POINT (ret));
	}
	public void set(int key,POINT ret){
		Front_.add(key,new POINT(ret));
	}
	public void add(double[] a){
		add(new POINT(a));
	}

	public void add(POINT  a){
		assert  (nOfD_ == -1)||(a.getDimension() == nOfD_) : "lenth of array and dimension of Front is different";
		nOfD_ = nOfD_ == -1 ? a.getDimension() : nOfD_;
		Front_.add(new POINT(a));
	}



	public void clear(){
		nOfD_ = -1;
		Front_.clear();
	}

	public int getDimension(){
		return nOfD_;
	}

	public static void main(String[] args){
		double[][] ret = {{4,100}, {4,7},{100,3}};

		double[] d = {1,1};
		Front fonrt = new Front(ret);

		fonrt.subscript();
		fonrt.Normization(true);
		fonrt.subscript();
	}


	public void Decreasing_Sort(int key){
		assert nOfD_ > key :"Front  Decrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"Front  Decrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<Front_.size()- 1;i++){
				for (int j=i+1;j<Front_.size();j++){
						if (Front_.get(i).get(key) < Front_.get(j).get(key) ){
								Swap(i,j);
						}
				}
			}
	}

	public void Decreasing_Sort(int[] perm,int key){
		Permutation.setPermutation(perm);

		assert nOfD_ > key :"Front  Decrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"Front  Decrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<Front_.size()- 1;i++){
				for (int j=i+1;j<Front_.size();j++){
						if (Front_.get(perm[i]).get(key) < Front_.get(perm[j]).get(key) ){
							int empty = perm[i];
							perm[i] = perm[j];
							perm[j] = empty;
						}
				}
			}
	}



	public void Increasing_Sort(int[] perm,int key){
		Permutation.setPermutation(perm);

		assert nOfD_ > key :"Front  Incrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"Front  Incrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<Front_.size()- 1;i++){
				for (int j=i;j<Front_.size();j++){
						if (Front_.get(perm[i]).get(key) > Front_.get(perm[j]).get(key) ){
								Swap(i,j);
						}
				}
			}
	}
	public void Increasing_Sort(int key){
		assert nOfD_ > key :"Front  Incrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"Front  Incrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<Front_.size()- 1;i++){
				for (int j=i+1;j<Front_.size();j++){
						if (Front_.get(i).get(key) > Front_.get(j).get(key) ){
								Swap(i,j);
						}
				}
			}
	}

	public double[] getNadia(boolean isMAX){
		double[] d = this.get(0).get().clone();

		Comparator comp = new DominationComparator(null); //DominaitonComaparatorでなくてもよい
		comp.setIs(isMAX);
		

		for(int i=0;i<this.size();i++){
			for(int j=0;j<this.getDimension();j++){
				d[j] = comp.worse( d[j] , this.get(i).get(j));
			}
		}
		assert d.length == this.getDimension() : "the Dimension of  Ideal POINT is  " + d.length + " the dimension of Front  is " + this.size();

		return d;
	}

	public double[] getIdeal(boolean isMAX){
		return getNadia(!isMAX);
	}

	public void Normization(boolean isMAX){
		double[] NadiaPOINT = this.getNadia(isMAX);
		double[] IdealPOINT = this.getIdeal(isMAX);

		for(int i=0;i< this.size();i++){
			double[] point_ = Front_.get(i).get();
			for(int j=0;j<this.getDimension();j++){

				point_[j] = (point_[j] - NadiaPOINT[j])/ (IdealPOINT[j] - NadiaPOINT[j]);

			}
		}
	}





	public POINT get(int key){
		return Front_.get(key);
	}

	public int size(){
		return Front_.size();
	}


	public  POINT pop(){
		POINT a = new POINT(Front_.get(Front_.size() - 1));
		Front_.remove(Front_.size() - 1);
		return a;
	}


	// this function return true if all Objectives have  the same demension;
	private boolean isOK(double[][] ret){
		assert ret.length > 0 : "arg size is " + ret.length;

		int size = ret.length;

		for (int i=0;i<size;i++){
			if(ret[i].length != size)
				return false;
		}
		return true;
	}
	private boolean isOK(Front ret){
		assert ret.size() > 0 :" isOK(Front) in Front :: Front has no size ";
		assert ret.get(0).getDimension() > 0 :" isOK(Front) in Front :: top POINT of Front has no size of Objective ";


		int size = ret.getDimension();

		for (int i=0;i<size;i++){
			if(ret.get(i).getDimension() != size)
				return false;
		}
		return true;
	}
	public void sort(Comparator d) throws JMException{
		for(int i=0;i<Front_.size();i++){
			for(int j=0;j<Front_.size();j++){
				if( d.execute(Front_.get(i), Front_.get(i)) == 1 );
					POINT ss = new POINT(Front_.get(i));
					Front_.set(i, new POINT(Front_.get(j)));
					Front_.set(j, new POINT(ss));
			}
		}


	}
	private boolean isOK(POINT[] ret){
		assert ret.length > 0 :" isOK(Front) in Front :: Front has no size ";
		assert ret[0].getDimension() > 0 :" isOK(Front) in Front :: top POINT of Front has no size of Objective ";


		int size = ret[0].getDimension();

		for (int i=0;i<size;i++){
			if(ret[i].getDimension() != size)
				return false;
		}
		return true;
	}

	private void Swap(int key_one, int key_two){
			POINT empty = new POINT (Front_.get(key_one));
			Front_.set(key_one, new POINT (Front_.get(key_two)));
			Front_.set(key_two, new POINT (empty));
	}

	public void subscript(){
		for(int i=0;i<size();i++){
			for(int j=0;j<nOfD_;j++){
				System.out.print(Front_.get(i).get(j)  +"     ");
			}
			System.out.println();
		}
	}



	public void setNumberOfPoints(int i) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void getInverse() {
		for(int i=0;i<this.size();i++){
			Front_.get(i).getInverse();
		}

	}


}







