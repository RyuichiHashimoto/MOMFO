package momfo.util.Comparator;

import java.io.Serializable;
import java.util.HashMap;

import lib.math.BuiltInRandom;
import momfo.util.JMException;


//isMAX is true if problem is max problem　
public abstract class Comparator implements Serializable {

	public  boolean isMAX_;

	protected HashMap<String , Object> parameters_;

	final protected BuiltInRandom random;


	public void setIsMAX(){
		isMAX_ = true;
	}
	public void setIsMIN(){
		isMAX_ = false;
	}

	public void setIs(boolean tet){
		isMAX_ = tet;
	}

	public boolean get(){
		return isMAX_;
	}

	public boolean compare(double a, double b){
		return (isMAX_ == (a > b));
	}

	public double better(double a,double b){
		if (isMAX_){
			if (a > b ){
				return a;
			} else {
				return b;
			}
		} else {
			if (a > b ){
				return b;
			} else {
				return a;
			}
		}
	}


	public double worse(double a, double b){
		if (isMAX_){
			if (a > b ){
				return b;
			} else {
				return a;
			}
		} else {
			if (a > b ){
				return a;
			} else {
				return b;
			}
		}
	}
	abstract public int execute(Object one, Object two)  throws JMException;


	 public Comparator(HashMap<String , Object> parameters) throws JMException{

		 if(!parameters.containsKey("RandomGenerator"))
			 throw new JMException("not found RandomGenerator");
		 
		 if(! (parameters.get("RandomGenerator") instanceof BuiltInRandom))
			 throw new JMException("not found RandomGenerator");
		 
		 random = (BuiltInRandom) parameters.get("RandomGenerator");
		 
		 parameters_ = parameters;
	}
	 public Comparator(boolean is,BuiltInRandom random_) {
		 isMAX_ = is;		 
		 random = random_;
	 }

	  public void setInputParameter(String name, Object object) {
		if (parameters_ == null) {
			parameters_ = new HashMap<String, Object>();
		}
		parameters_.put(name, object);
	}

	public Object getInputParameter(String name) {
		return parameters_.get(name);
	}

	String comparatorName_;


	public String getComparatorName(){return comparatorName_;};
}
