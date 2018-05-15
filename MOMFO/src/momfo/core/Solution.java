package momfo.core;

import lib.math.BuildInRandom;
import momfo.util.POINT;


// Solution type  is　1 mean  that Solution Type is digit　
// Solution Type is 2 mean that Solution Type is Real
public class Solution implements Cloneable{

	Problem problem_;

	ProblemSet problemSet_;

	int multitaskID_;

	private final BuildInRandom random;

	public int getMultitaskID(){
		return multitaskID_;
	}
	public void setMultitaskiID(int key){
		multitaskID_ = key;
	}



	public ProblemSet getProblemSet(){
		if(problemSet_ != null){
			return problemSet_;
		}
		return null;
	}

	public void setProblemSet(ProblemSet d){
		problemSet_ = d;
	}

	double[]	value_;

	int numberOfVariables_;

	double[] objective_;
	int numberOftasks;
	int numberOfObjectives_;

	double[]	upperlimit_;
	double[]	lowerlimit_;

	int SolutionType_;

	int skillfactor_;
	double scalarfitness;

	double[] constrain_;
	boolean feasibleSolution_;
	int numberOfinfeasibleconstraint_;
	int numberOfConstrain_ = 0;
	double totalConstrain_;


	double crowedDistance_;
	int 	DominationCount_;
	int 	DominatedRank_;

	int[] factorialrank;

	public int getSkillFactor(){
		return skillfactor_;
	}


	public void setSkillFactor(int key){
		skillfactor_ = key;
	}


	public int getDominatedRank(){
		return DominatedRank_;
	}

	public void setDominatedRank(int a ){
		DominatedRank_ = a;
	}

	int rank_;

	int ID_;

	public void setRank(int rank){
			rank_ = rank;
	}

	public int getRank(){
		return rank_;
	}

	public double[] getConstrain(){
		return constrain_;
	}
	public double getConstrain(int key){
		assert key < constrain_.length : "the length is " + constrain_.length + "	key is " + key;
		return constrain_[key];
	}
	public void setConstrain(int key,double arg){
		assert key < constrain_.length : "the length is " + constrain_.length + "	key is " + key;
		constrain_[key] = arg;
	}

	public void setConstrain(double[] arg){
		assert arg.length == constrain_.length : "constrain length is " + constrain_.length + "	 arg length is " + arg.length;
		int size = arg.length;
		for(int i=0;i<size;i++){
			constrain_[i] = arg[i];
		}
	}
	public void calctotalCalc(){
		totalConstrain_ =   0;

		for (int i=0;i<constrain_.length;i++){
			totalConstrain_ += constrain_[i];
		}

		if( Math.abs(totalConstrain_) < 1.0E-14 ) {
			feasibleSolution_ = true;
		} else {
			feasibleSolution_ = false;
		}
	}
	public double getViolation(){return totalConstrain_;};

	public boolean getFeasible(){
		return feasibleSolution_;
	}


	public int getNumberOfConstraint(){
		return numberOfConstrain_;
	}
	public int getnumberOfinfeasibleConstraint(){
		return numberOfinfeasibleconstraint_;
	}


	public double getCrowdDistance_(){
		return crowedDistance_;
	}

	public void setCrowedDistance(double a){
		crowedDistance_ = a;
	}

	public int getDominationCount(){
		return DominationCount_;
	}

	public void addDominationcount(){
			DominationCount_++;
	}

	public int setDominationCount(int a){
		return a;
	}

	public int getSolutionType(){
		return SolutionType_;
	}

	public void setObjective(int key,double a){
		objective_[key] = a;
	}

	public void remake(){
		if(SolutionType_ == 1){
			ramakeWithInt();
		} else if (SolutionType_ == 2){
			remakeWithDouble();
		} else {
			assert false;
		}
	}

	public void ramakeWithInt(){
		for(int i = 0;i<numberOfVariables_;i++){
			value_[i] = random.nextIntII((int)Math.round(upperlimit_[i] - lowerlimit_[i])) +  lowerlimit_[i];
		}
	}

	public void remakeWithDouble(){
		for(int i = 0;i<numberOfVariables_;i++){
			value_[i] = lowerlimit_[i] + random.nextDoubleII()*(upperlimit_[i]-lowerlimit_[i]);
		}
	}


	public double getValue(int key){
		return value_[key];
	}
	public double[] getValue(){
		return value_;
	}

	public int getNumberOftasks(){
		return 		numberOftasks;
	}

	public Solution(ProblemSet a, int numberOfTasks,BuildInRandom random_){
		numberOfVariables_ = a.getMaxDimension();
		problemSet_ = a;
		problem_ = a.get(numberOfTasks);
		value_ = new double[numberOfVariables_];
		random = random_;
		factorialrank = new int[a.countProblem()];


		numberOfObjectives_ = a.getMaxDimensionOfObjective();
		numberOfConstrain_ = a.getMaxNumberOfConstrain();
		numberOftasks = a.countProblem();

		scalarfitness = Double.MIN_VALUE;
		upperlimit_ = new double[numberOfVariables_];
		lowerlimit_ = new double[numberOfVariables_];
		NumberOfDominatedSolution_ = 0;
		skillfactor_ = numberOfTasks;
		totalConstrain_ = 0;
		constrain_ = new double[numberOfConstrain_];
		feasibleSolution_ = true ;
		numberOfinfeasibleconstraint_ = 0;

		factorialrank = new int[problemSet_.countProblem()];
		for(int t = 0; t< factorialrank.length;t++){
			factorialrank[t] = Integer.MAX_VALUE;
		}

		for(int i=0;i<numberOfConstrain_ ;i++){
			constrain_[i] = 0d;
		}
		objective_ = new double[numberOfObjectives_];
		for(int t = 0; t< numberOfObjectives_;t++){
			objective_[t] = Double.MAX_VALUE;
		}
		
		for(int i = 0;i<numberOfVariables_;i++){
			upperlimit_[i] = 1.0;//a.getUpperlimit_(i);
			lowerlimit_[i] = 0.0;//a.getLowerlimit_(i);
		}

		SolutionType_ = 2;
		ID_  = -1;
//		remake();
	}

	public Solution(Problem a,BuildInRandom random_){
		
		problemSet_ = null;
		problem_ = a;
		numberOftasks =1;
		random = random_;
 		scalarfitness = Double.MIN_VALUE;
 		value_ = new double[a.getNumberOfVariables()];
		numberOfVariables_ = a.getNumberOfVariables();
		numberOfObjectives_ = a.getNumberOfObjectives();
		upperlimit_ = new double[numberOfVariables_];
		lowerlimit_ = new double[numberOfVariables_];
		NumberOfDominatedSolution_ = 0;
		skillfactor_ = -1;
		numberOfConstrain_ = a.getNumberOfConstrain();
		totalConstrain_ = 0;
		constrain_ = new double[numberOfConstrain_];
		feasibleSolution_ = true ;
		numberOfinfeasibleconstraint_ = 0;

		for(int i=0;i<numberOfConstrain_ ;i++){
			constrain_[i] = 0d;
		}
		
		for(int i = 0;i<numberOfVariables_;i++){
			upperlimit_[i] = 1.0;//a.getUpperlimit_(i);
			lowerlimit_[i] = 0.0;//a.getLowerlimit_(i);
		}
		
		objective_ = new double[numberOfObjectives_];
		SolutionType_ = a.getSolutionType_();
		ID_  = -1;
//		remake();
	}

	public Solution(int numberOfObjective){
		objective_ = new double[numberOfObjective];
		numberOfObjectives_ = numberOfObjective;
		constrain_ = new double[numberOfObjective];
		numberOfConstrain_ = numberOfObjective;
		random  = null;

	}
	public void setFeasible(boolean d){
		feasibleSolution_ = d;
	}
	int NumberOfDominatedSolution_;

	public int getNumberOfDominatedSolution(){return NumberOfDominatedSolution_;};
	public void setNumberOfDominatedSolution_(int d){NumberOfDominatedSolution_ = d;}


	public void setID(int a){
		ID_ = a;
	}

	public int getID(){
		return ID_;
	}

	public void SubscriptObjective(){
		for(int i=0;i<numberOfObjectives_;i++){
			System.out.print(objective_[i]+"	");
		}
		System.out.println();
	}

	public double gettotalConstrain(){
		return totalConstrain_;
	}

	public Solution(POINT a,BuildInRandom random_){
		random = random_;
		rank_ = Integer.MAX_VALUE;
		value_ = null;
		numberOfVariables_ = 0;
		numberOfObjectives_ = a.getDimension();

		numberOfinfeasibleconstraint_ = Integer.MAX_VALUE;
		totalConstrain_ = Double.MAX_VALUE;
		feasibleSolution_ = false;
		numberOfConstrain_ = Integer.MAX_VALUE;
		constrain_ = null;

		crowedDistance_ = Double.NEGATIVE_INFINITY;
		upperlimit_ = null;
		lowerlimit_ = null;
		objective_ = new double[numberOfObjectives_];

		for(int i = 0; i< numberOfObjectives_;i++){
			objective_[i]= a.get(i);
		}
		DominatedRank_ = Integer.MAX_VALUE;


	}
	public Solution clone() {
		return new Solution(this);
	}
	
	public Solution(POINT a){
		random = null;
		rank_ = Integer.MAX_VALUE;
		value_ = null;
		numberOfVariables_ = 0;
		numberOfObjectives_ = a.getDimension();

		numberOfinfeasibleconstraint_ = Integer.MAX_VALUE;
		totalConstrain_ = Double.MAX_VALUE;
		feasibleSolution_ = false;
		numberOfConstrain_ = Integer.MAX_VALUE;
		constrain_ = null;

		crowedDistance_ = Double.NEGATIVE_INFINITY;
		upperlimit_ = null;
		lowerlimit_ = null;
		objective_ = new double[numberOfObjectives_];

		for(int i = 0; i< numberOfObjectives_;i++){
			objective_[i]= a.get(i);
		}
		DominatedRank_ = Integer.MAX_VALUE;


	}

	public BuildInRandom getRandomGenerator(){
		return random;
	}

	public Solution(Solution a){
		skillfactor_ = a.getSkillFactor();
		rank_ = a.getRank();
		random = a.getRandomGenerator();
		value_ = new double[a.getNumberOfVariables()];
		numberOfVariables_ = a.getNumberOfVariables();
		numberOfObjectives_ = a.getNumberOfObjectives();
		for(int i = 0; i < numberOfVariables_;i++){
			value_[i] = a.getValue(i);
		}

		scalarfitness =  a.getScalarFitness();
		numberOfinfeasibleconstraint_ = a.getnumberOfinfeasibleConstraint();
		NumberOfDominatedSolution_ = a.getNumberOfDominatedSolution();
		totalConstrain_ = a.gettotalConstrain();
		feasibleSolution_ = a.getFeasible();
		numberOfConstrain_ = a.getNumberOfConstraint();
		constrain_ = new double[numberOfConstrain_];
		for(int i=0;i<numberOfConstrain_;i++){
			constrain_[i] = a.getConstrain(i);
		}

		numberOftasks =a.getNumberOftasks();

		factorialrank = new int[numberOftasks];
		if(numberOftasks != 1){
			for(int t = 0; t< numberOftasks;t++){
				factorialrank[t] = a.getFactorialRank(t);
			}
		}
		factorialrank = new int[numberOftasks];
		if(numberOftasks != 1){
			for(int t = 0; t< numberOftasks;t++){
				factorialrank[t] = a.getFactorialRank(t);
			}
		}
		crowedDistance_ = a.getCrowdDistance_();



		upperlimit_ = new double[a.getNumberOfVariables()];
		lowerlimit_ = new double[a.getNumberOfVariables()];
		objective_ = new double[numberOfObjectives_];
		for(int i = 0;i < numberOfVariables_; i++){
			upperlimit_[i] = a.getUpperlimit(i);
			lowerlimit_[i] = a.getLowerlimit(i);
		}
		ID_ = a.getID();
		multitaskID_ =a.getMultitaskID();
		for(int i = 0; i< numberOfObjectives_;i++){
			objective_[i]= a.getObjective(i);
		}
		DominatedRank_ = a.getDominatedRank();
	}
	public int getFactorialRank(int key){
		return factorialrank[key];
	}

	public double getObjective(int key){
		assert numberOfObjectives_ >=  key: "the key is wrong, size of Objectives is " + numberOfObjectives_ + " and the key is  " + key;
		return objective_[key];
	}

	public double[] getObjectives(){
		return objective_;
	}

	public double getUpperlimit(int key){
		return upperlimit_[key];
	}
	public double getLowerlimit(int key){
		return lowerlimit_[key];
	}


	public int getNumberOfVariables(){
		return numberOfVariables_;
	}

	public int getNumberOfObjectives(){
		return numberOfObjectives_;
	}

	public void setValue(int i, double d) {
		value_[i] = d;
	}

	public void setInverseObjective() {
		for(int i=0;i<numberOfObjectives_;i++){
			objective_[i] = objective_[i] * -1;
		}
	}

	public double getScalarFitness() {
		return scalarfitness;
	}
	public void setFactorialRank(int taskID, int rank) {
		factorialrank[taskID] = rank;
	}
	public void setScalarFitness(double d) {
		scalarfitness = d;
	}
	public void rescaleObjectives(int numberOfObjectives) {
		numberOfObjectives_ = numberOfObjectives;
		objective_ = new double[numberOfObjectives_];

		for(int i=0;i<numberOfObjectives_;i++){
			objective_[i] = objective_[i] * -1;
		}


	}


}
