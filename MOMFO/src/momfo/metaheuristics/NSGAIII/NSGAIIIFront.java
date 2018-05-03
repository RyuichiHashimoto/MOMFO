package momfo.metaheuristics.NSGAIII;

import java.util.ArrayList;
import java.util.List;

import momfo.core.Population;
import momfo.util.JMException;
import momfo.util.Permutation;
import momfo.util.ReferencePoint;
import momfo.util.Comparator.Comparator;
import momfo.util.Comparator.DominationComparator;


/*
 * this implement needs For the Hypervolume calculation
 *all NSGAIIIFront must be non Dominated Solution set
 */
public class NSGAIIIFront {

	private int nOfD_ = -1;

	private List<NSGAIIIPOINT>  NSGAIIIFront_;

	public NSGAIIIFront(){
		NSGAIIIFront_ = new ArrayList<NSGAIIIPOINT>();
	}
	public double[][] get(){
		double[][] ret = new double[NSGAIIIFront_.size()][nOfD_];
		for(int i=0;i<NSGAIIIFront_.size();i++){
			for(int j=0;j<NSGAIIIFront_.size();j++){
				ret[i][j] = NSGAIIIFront_.get(i).get(j);
			}
		}
		return ret;
	}


	public NSGAIIIFront(double[][] Font){
		assert Font.length > 0 : "initialither in NSGAIIIFront ::  argment has no size ";
		assert Font[0].length > 0 : "initialither in NSGAIIIFront ::  argment  haa no size ";
//		assert isOK(Font) : "initialther in NSGAIIIFront :: all Objective Demention of argment NSGAIIIFront are not same ";

		nOfD_ = Font[0].length;
		NSGAIIIFront_ = new  ArrayList<NSGAIIIPOINT>();

		for(int i=0;i<Font.length; i++){
			NSGAIIIFront_.add(new NSGAIIIPOINT (Font[i]));
		}
	}

	public NSGAIIIFront(int t,int s){
		NSGAIIIPOINT d = new NSGAIIIPOINT();
		NSGAIIIFront_ = new ArrayList<NSGAIIIPOINT>();
		NSGAIIIFront_.add(d);
		nOfD_ = s;

	}

	public NSGAIIIFront(NSGAIIIPOINT[] Font){
		assert Font.length > 0 : "initialither in NSGAIIIFront ::  argment has no size ";
		assert Font[0].getDimension() > 0 : "initialither in NSGAIIIFront ::  argment  has no size ";
		assert isOK(Font) : "initialther in NSGAIIIFront :: all Objective Demention of argment NSGAIIIFront are not same ";

		nOfD_ = Font[0].getDimension();
		NSGAIIIFront_ = new  ArrayList<NSGAIIIPOINT>();

		for(int i=0;i<Font.length; i++){
			NSGAIIIFront_.add(new NSGAIIIPOINT (Font[i]));
		}
	}


	public void assignID(){
		for(int i=0;i< NSGAIIIFront_.size();i++){
			NSGAIIIFront_.get(i).setID(i);
		}
	}

	public NSGAIIIFront(Population d){
		this(d.getAllObjectives());
	}

	public NSGAIIIFront(double[][] Font, double[] ref, boolean isMAX){
		this(new NSGAIIIFront(Font),new ReferencePoint(ref),isMAX);
	}

	public NSGAIIIFront(NSGAIIIFront Font, double[] ref, boolean isMAX){
		this(Font,new ReferencePoint(ref),isMAX);
	}

	public NSGAIIIFront(double[][] Font, ReferencePoint ref, boolean isMAX){
		this(new NSGAIIIFront(Font),ref,isMAX);
	}

	public NSGAIIIFront(Population Font, ReferencePoint ref, boolean isMAX){
		this(new NSGAIIIFront(Font) , ref, isMAX);
	}

	// this implement return the NSGAIIIFront , all  NSGAIIIPOINT in NSGAIIIFront dominates ref;
	public NSGAIIIFront(NSGAIIIFront Font, ReferencePoint ref, boolean isMAX){
		assert Font.size() > 0 :"NSGAIIIFront have no size";
		assert Font.getDimension() == ref.getDimension() :"NSGAIIIFront and ReferencePoint have difference size";

		nOfD_ = Font.getDimension();
		NSGAIIIFront_ = new  ArrayList<NSGAIIIPOINT>();
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
					NSGAIIIFront_.add(new NSGAIIIPOINT(Font.get(i)));
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
					NSGAIIIFront_.add(new NSGAIIIPOINT (Font.get(i)));
				}
			}
		}

	}


	public NSGAIIIFront(NSGAIIIFront Font){
		NSGAIIIFront_ = new  ArrayList<NSGAIIIPOINT>();
		nOfD_ = Font.getDimension();
		for(int i=0;i<Font.size(); i++){
			NSGAIIIFront_.add(new NSGAIIIPOINT (Font.get(i)));
		}
	}

	public void remove(int key){
		NSGAIIIFront_.remove(key);
	}

	public void replace(int key, double[] a){
		replace(key, new NSGAIIIPOINT(a));
	}
	public void replace(int key, NSGAIIIPOINT a){
		assert NSGAIIIFront_.size() > key : "the index is " + key + "	NSGAIIIFront size is " + NSGAIIIFront_.size();
		NSGAIIIFront_.remove(key);
		NSGAIIIFront_.add(key,new NSGAIIIPOINT(a));
	}

	public void set(int key,double[] ret){
		set(key, new NSGAIIIPOINT (ret));
	}
	public void set(int key,NSGAIIIPOINT ret){
		NSGAIIIFront_.add(key,new NSGAIIIPOINT(ret));
	}
	public void add(double[] a){
		add(new NSGAIIIPOINT(a));
	}

	public void add(NSGAIIIPOINT  a){
		assert  (nOfD_ == -1)||(a.getDimension() == nOfD_) : "lenth of array and dimension of NSGAIIIFront is different";
		nOfD_ = nOfD_ == -1 ? a.getDimension() : nOfD_;
		NSGAIIIFront_.add(new NSGAIIIPOINT(a));
	}



	public void clear(){
		nOfD_ = -1;
		NSGAIIIFront_.clear();
	}

	public int getDimension(){
		return nOfD_;
	}

	public static void main(String[] args){
		double[][] ret = {{4,100}, {4,7},{100,3}};

		double[] d = {1,1};
		NSGAIIIFront fonrt = new NSGAIIIFront(ret);

		fonrt.subscript();
		fonrt.Normization(true);
		fonrt.subscript();
	}


	public void Decreasing_Sort(int key){
		assert nOfD_ > key :"NSGAIIIFront  Decrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"NSGAIIIFront  Decrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<NSGAIIIFront_.size()- 1;i++){
				for (int j=i+1;j<NSGAIIIFront_.size();j++){
						if (NSGAIIIFront_.get(i).get(key) < NSGAIIIFront_.get(j).get(key) ){
								Swap(i,j);
						}
				}
			}
	}

	public void Decreasing_Sort(int[] perm,int key){
		Permutation.setPermutation(perm);

		assert nOfD_ > key :"NSGAIIIFront  Decrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"NSGAIIIFront  Decrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<NSGAIIIFront_.size()- 1;i++){
				for (int j=i+1;j<NSGAIIIFront_.size();j++){
						if (NSGAIIIFront_.get(perm[i]).get(key) < NSGAIIIFront_.get(perm[j]).get(key) ){
							int empty = perm[i];
							perm[i] = perm[j];
							perm[j] = empty;
						}
				}
			}
	}



	public void Increasing_Sort(int[] perm,int key){
		Permutation.setPermutation(perm);

		assert nOfD_ > key :"NSGAIIIFront  Incrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"NSGAIIIFront  Incrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<NSGAIIIFront_.size()- 1;i++){
				for (int j=i;j<NSGAIIIFront_.size();j++){
						if (NSGAIIIFront_.get(perm[i]).get(key) > NSGAIIIFront_.get(perm[j]).get(key) ){
								Swap(i,j);
						}
				}
			}
	}
	public void Increasing_Sort(int key){
		assert nOfD_ > key :"NSGAIIIFront  Incrasing_sort ::  the index is wrong  nOfD_ is " + nOfD_ + " , key is " +key;
		assert 0 	<= key :"NSGAIIIFront  Incrasing_sort ::  the index is wrong  key is "   + key;

			for (int i=0;i<NSGAIIIFront_.size()- 1;i++){
				for (int j=i+1;j<NSGAIIIFront_.size();j++){
						if (NSGAIIIFront_.get(i).get(key) > NSGAIIIFront_.get(j).get(key) ){
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
		assert d.length == this.getDimension() : "the Dimension of  Ideal NSGAIIIPOINT is  " + d.length + " the dimension of NSGAIIIFront  is " + this.size();

		return d;
	}

	public double[] getIdeal(boolean isMAX){
		return getNadia(!isMAX);
	}

	public void Normization(boolean isMAX){
		double[] NadiaNSGAIIIPOINT = this.getNadia(isMAX);
		double[] IdealNSGAIIIPOINT = this.getIdeal(isMAX);



		for(int i=0;i< this.size();i++){
			double[] NSGAIIIPOINT_ = NSGAIIIFront_.get(i).get();
			for(int j=0;j<this.getDimension();j++){

				NSGAIIIPOINT_[j] = (NSGAIIIPOINT_[j] - NadiaNSGAIIIPOINT[j])/ (IdealNSGAIIIPOINT[j] - NadiaNSGAIIIPOINT[j]);

			}
		}
	}





	public NSGAIIIPOINT get(int key){
		return NSGAIIIFront_.get(key);
	}

	public int size(){
		return NSGAIIIFront_.size();
	}


	public  NSGAIIIPOINT pop(){
		NSGAIIIPOINT a = new NSGAIIIPOINT(NSGAIIIFront_.get(NSGAIIIFront_.size() - 1));
		NSGAIIIFront_.remove(NSGAIIIFront_.size() - 1);
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
	private boolean isOK(NSGAIIIFront ret){
		assert ret.size() > 0 :" isOK(NSGAIIIFront) in NSGAIIIFront :: NSGAIIIFront has no size ";
		assert ret.get(0).getDimension() > 0 :" isOK(NSGAIIIFront) in NSGAIIIFront :: top NSGAIIIPOINT of NSGAIIIFront has no size of Objective ";


		int size = ret.getDimension();

		for (int i=0;i<size;i++){
			if(ret.get(i).getDimension() != size)
				return false;
		}
		return true;
	}
	public void sort(Comparator d) throws JMException{
		for(int i=0;i<NSGAIIIFront_.size();i++){
			for(int j=0;j<NSGAIIIFront_.size();j++){
				if( d.execute(NSGAIIIFront_.get(i), NSGAIIIFront_.get(i)) == 1 );
					NSGAIIIPOINT ss = new NSGAIIIPOINT(NSGAIIIFront_.get(i));
					NSGAIIIFront_.set(i, new NSGAIIIPOINT(NSGAIIIFront_.get(j)));
					NSGAIIIFront_.set(j, new NSGAIIIPOINT(ss));
			}
		}


	}
	private boolean isOK(NSGAIIIPOINT[] ret){
		assert ret.length > 0 :" isOK(NSGAIIIFront) in NSGAIIIFront :: NSGAIIIFront has no size ";
		assert ret[0].getDimension() > 0 :" isOK(NSGAIIIFront) in NSGAIIIFront :: top NSGAIIIPOINT of NSGAIIIFront has no size of Objective ";


		int size = ret[0].getDimension();

		for (int i=0;i<size;i++){
			if(ret[i].getDimension() != size)
				return false;
		}
		return true;
	}

	private void Swap(int key_one, int key_two){
			NSGAIIIPOINT empty = new NSGAIIIPOINT (NSGAIIIFront_.get(key_one));
			NSGAIIIFront_.set(key_one, new NSGAIIIPOINT (NSGAIIIFront_.get(key_two)));
			NSGAIIIFront_.set(key_two, new NSGAIIIPOINT (empty));
	}

	public void subscript(){
		for(int i=0;i<size();i++){
			for(int j=0;j<nOfD_;j++){
				System.out.print(NSGAIIIFront_.get(i).get(j)  +"\t");
			}
			System.out.println();
		}
	}



	public void setNumberOfNSGAIIIPOINTs(int i) {
		// TODO 自動生成されたメソッド・スタブ

	}

	public void getInverse() {
		for(int i=0;i<this.size();i++){
			NSGAIIIFront_.get(i).getInverse();
		}

	}


}







