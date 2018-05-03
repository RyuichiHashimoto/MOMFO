package momfo.metaheuristics.parallelsmsemoaigd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.operators.selection.ParentsSelection.ParentsSelection;
import momfo.operators.selection.ParentsSelection.RandomSelectionWithoutReplacement;
import momfo.util.Front;
import momfo.util.JMException;
import momfo.util.Random;
import momfo.util.Comparator.SMSEMOAComparator.SMSEMOAComparator;
import momfo.util.Comparator.SMSEMOAComparator.SMSEMOASelectionComparator;
import momfo.util.Indicator.Hypervolume.WFGHV;
import momfo.util.Ranking.NDSRanking;

/*
 * 並列に実行を行う．
 *
 *
 */

public class ParallelSMSEMOAIGD extends Algorithm{

	public ParallelSMSEMOAIGD(Problem problem) {
		super(problem);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	WFGHV WFGHV_;

	List<Integer>  Sp_;
	int[]	Np_;
	List<List<Integer> > F_all;
	List<List<Integer> > Sp_all  ;
	private  boolean  isNorm = false;
	private int maxEvaluation_ ;
	int[] rank_;

	double[][] ReferencePoint_;


	int numberofObjectives_;

	int numberOfDivision_;

	boolean isInnerWeightVector_;

	int InnerWeightVectorDivision_;

	int numberOfParents_;

	private SMSEMOAComparator comparator;

	private int populationSize_;

	NDSRanking ranking;

	int evaluations_;

	int nCPU_;

	HashMap parameters;
	Operator crossover_;
	Operator mutation_;

	ParentsSelection ParentSelection_;

	private Population population_;

	private Population Merge_;

	String directoryname;
	boolean  isMaxProblem_ ;

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		setting();
		HashMap parameters = new HashMap();
		int time = ((Integer) this.getInputParameter("times")).intValue();

		setReferencePoint();
		initPopulation();


		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/InitialFUN/InitiaFUN" + time + ".dat");

		Sp_all = new  ArrayList<List <Integer>>();
		F_all = new  ArrayList<List <Integer>>();

		System.out.print(maxEvaluation_ + " ");
		do {

			makeNextGeneration();

			GotoNextGeneration();

		}while(	evaluations_ <maxEvaluation_);
		
		System.out.print(evaluations_+ "");
		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/FinalFUN/FinalFUN" + time + ".dat");
		
		return population_;
	}


	public int[] ParentsSelection(int[] d){
		assert numberOfParents_ == d.length: "permutation size is wrong permutation size is " + d.length   + "	" + "numberOfparents is " + numberOfParents_;

		for(int i=0;i<numberOfParents_;i++){
			d[i] = Random.nextIntIE(populationSize_);
		}
		return d;
	}

	public void GotoNextGeneration() throws JMException{

		Merge_.AssignID();

		ranking = new NDSRanking(isMaxProblem_);
		ranking.setPop(Merge_);
		ranking.Ranking();

		Population empty = ranking.get(ranking.getworstrank()-1);
		int number = -1;
		if(empty.size() == 1){
			number = empty.get(0).getID();
		}  else  {
			Front no = new Front(empty);
			if(isNorm) {
				System.out.println("test");

				if(!isMaxProblem_){
					assert false : "まだ　未設定";
				} else {
					assert false : "まだ　未設定";
				}
			}
			double[] empt =IGDCalclator.calclate(no,ReferencePoint_,nCPU_);
			List<Integer> list = IGDCalclator.getLowestContribution(empt);
			int d = Random.nextIntIE(list.size());
			number = empty.get( list.get(d) ).getID();
		}
		
		Merge_.remove(number);
		population_ = new Population(Merge_);
		Merge_ = null;
		
		assert (population_.size() == populationSize_): "now pop size is " + population_.size() +" but hope " + populationSize_;
	}

	public void setReferencePoint() throws JMException{

		/*
		 * 		double[][] weight,weight_one;

		weight_one = WeightedVector.getWeightedVector(numberofObjectives_,numberOfDivision_);

		if (isInnerWeightVector_){
			double[][] weightinner = WeightedVector.getWeightedVector(numberofObjectives_,InnerWeightVectorDivision_);
			WeightedVector.getinnerWeightVector(weightinner);
			weight = 	WeightedVector.conbine(weight_one, weightinner);

		} else {
			weight = weight_one;
		}
		ReferencePoint_ = weight;
		System.out.println(ReferencePoint_.length);
*/
		ReferencePoint_ = FileReading.Reading(numberofObjectives_);

	}

	public void initPopulation() throws JMException, ClassNotFoundException {

 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			evaluations_++;
			problem_.repair(newSolution,null);
			problem_.evaluate(newSolution);
		 	population_.add(newSolution);
		} // for
	} // initPopulatiown
 // initPopulation

	public void makeNextGeneration() throws JMException {
		Merge_ = new Population(populationSize_ + 1);
		Merge_.merge(population_);

		Solution   child;
		Solution[] parents   = new Solution[2];

		int[] perm  = (int[]) ParentSelection_.execute(population_);
		parents[0] = population_.get(perm[0]);
		parents[1] = population_.get(perm[1]);

		child = (Solution) crossover_.execute(parents);
		mutation_.execute(child);
		problem_.repair(child ,null);

		problem_.evaluate(child);
		evaluations_ = evaluations_ + 1;
		Merge_.add(new Solution(child));

	}

	public void setting(){
		numberOfParents_ = 2;
		comparator = new SMSEMOASelectionComparator(null);
		evaluations_ = 0;
		populationSize_ = ((Integer)this.getInputParameter("populationSize"));
		maxEvaluation_ = ((Integer)this.getInputParameter("maxEvaluations"));
		population_ = new Population(populationSize_);

		ParentSelection_ = new RandomSelectionWithoutReplacement(null);
		ParentSelection_.setComparator(comparator);
		isMaxProblem_    = ((boolean)this.getInputParameter("ismax"));
		comparator.setIs(isMaxProblem_);

		crossover_ = operators_.get("crossover");
		mutation_ = operators_.get("mutation");
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		isNorm = ((boolean) this.getInputParameter("Norm"));
		numberofObjectives_    = ((Integer)this.getInputParameter("numberOfObjectives"));
		nCPU_ = ((Integer)this.getInputParameter("nCPU"));
		System.out.println(nCPU_);
	}

}
