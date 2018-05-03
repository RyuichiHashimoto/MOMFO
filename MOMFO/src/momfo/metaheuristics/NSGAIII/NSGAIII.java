package momfo.metaheuristics.NSGAIII;

import java.util.ArrayList;
import java.util.List;

import momfo.core.Algorithm;
import momfo.core.Operator;
import momfo.core.Population;
import momfo.core.Problem;
import momfo.core.Solution;
import momfo.operators.selection.Selection;
import momfo.operators.selection.ParentsSelection.RandomSelectionWithReplacement;
import momfo.util.JMException;
import momfo.util.Random;
import momfo.util.WeightedVector;
import momfo.util.Ranking.NDSRanking;




public class NSGAIII extends Algorithm{

	private int populationSize_;
	private Population population_;
	private Population  offSpring_;
	private Population  merge_;
	double[][] ReferencePoint_;
	private NSGAIIIFront front;
	private int[] nichiCount; //　サイズ : 参照点個数．　参照点において，自分に割り当てられた個体数をカウント

//	private double[] d; //各個体に対し，その個体と最も近い参照点との距離
//	private int[] P_index;///各個体に対し，その個体と最も近い参照点番号を格納　つまり，配列サイズは個体数，出力範囲は最大で参照点の数
//	private int[] nichingCountter;

	private double [][] extremePoint;
	private double[] IdealPoint;
	private boolean outNormal_;
	Selection selection_;

	int generation;

	int numberOfParents_;

	Solution[] indArray_;
	String functionType_;
	int evaluations_;

	int numberofObjectives_;
	int numberOfDivision_;
	boolean isInnerWeightVector_;
	int InnerWeightVectorDivision_;
	/**
	 * Operators
	 */


	int which = 0; // test用変数
	//0	don't use thea archive
	//1  use the archiive
	//2 use only the Ideal archive


	Operator crossover_;
	Operator mutation_;
	int maxEvaluations_;

	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;
	int time;
	boolean isMaxProblem_;

	boolean isNorm;
	String directoryname;
	public NSGAIII(Problem problem) {
		super(problem);
	} // DMOEA





	@Override
	public Population execute() throws JMException, ClassNotFoundException {

		setting();


		initPopulation();


		setReferencePoint();

		//file output information about population
		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/InitialFUN/InitiaFUN" + time + ".dat");


		boolean flag = true;
		do {
			// flas is true means that reaching the limit of the evaluations

			flag = makeOffSpring();

			merge_ = new Population(populationSize_ *2);

			merge_.merge(population_);

			merge_.merge(offSpring_);

			// selection the next Genereation to populaiton
			GotoNextGeneration();
		} while(flag);

	//	if(outNormal_)population_.Normalization();
		//file output information about population
		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/FinalFUN/FinalFUN" + time + ".dat");

		return null;
	}


	private void GotoNextGeneration() {

		NDSRanking Ranking = new NDSRanking(isMAX_);

		Ranking.setPop(merge_);
		Ranking.Ranking(); // nonDominated sorting

		Population temp_pop = new Population(populationSize_*2);

		population_ = new Population(populationSize_);

		int rank = -1;

		// select the Rank 1 ～Rank n Solution such that n is the minimum satisfing that the temp size is bigger than population Size
		do {
			temp_pop.merge(Ranking.get(++rank));
		} while(temp_pop.size() < populationSize_);


		if (temp_pop.size() == populationSize_){
			population_ = temp_pop;
			return;
		}

		assert temp_pop.size() > populationSize_ : "isOK";

		//select the temp Solutions expect for the worst rank Solutions
		for(int i = 0 ; i<rank;i++){
			population_.merge(Ranking.get(i));
		}
		// this is capacity.
		int capa = populationSize_ - population_.size();
		int lastFrontSize = Ranking.get(rank).size();


		assert population_.size() < populationSize_ : population_.size() +"	個体群サイズ";
		assert temp_pop.size() == lastFrontSize + population_.size(): "";

		front = new NSGAIIIFront(temp_pop);

		if(isNorm){
			if(which == 0){
				new HyperPlane(front).Normalization();//現世代のNadia Idealを使用
			} else if (which == 1) {
				new HyperPlane(front,ReferencePoint_,IdealPoint,extremePoint).Normalization();//両方ともアーカイブを使用

			} else if (which == 2){
				new HyperPlane(front,ReferencePoint_,IdealPoint).Normalization();//Idealのみアーカイブを使用
			}
		}
		Associate();

		nitchCount(lastFrontSize);

		NSGAIIIFront lastfront = new NSGAIIIFront();

		int size_ = front.size();
		for(int i=0;i<lastFrontSize;i++){
			lastfront.add(front.get(size_ - lastFrontSize + i));
		}

		/*for(int i=0;i<lastfront.size();i++){
			double ret = 0;
			for(int j=0;j<3;j++){
				ret += lastfront.get(i).get(j);
			}
		}
*/
		Niching(capa,lastfront,Ranking.get(rank));		//ここで次世代個体群形成の終了．
		assert population_.size() == populationSize_ : "個体群サイズが異なる	"  +population_.size() + "	"	+ populationSize_ ;

	}


	public void subscript(){
		for(int i=0;i<ReferencePoint_.length;i++){
			for(int j=0;j<ReferencePoint_[i].length;j++){
				System.out.print(ReferencePoint_[i][j] + "	");
			}
			System.out.println();
		}
	}

	private void nitchCount( int size) {
		nichiCount = new int[ReferencePoint_.length];

		for(int i=0;i<front.size() - size;i++){
			nichiCount[front.get(i).getIndex()]++;
		}
	}


	private void Niching(int K,NSGAIIIFront lastFront_,Population lastFrontpop){
		assert lastFrontpop.size() == lastFront_.size(): "Front size が異なる";

		lastFront_.assignID();
		boolean []  avairableRefFlag = new boolean[ReferencePoint_.length];

		for(int i = 0;i<K;){
			List<Integer> nichi_minNumberSet = new ArrayList<Integer>();
			for(int re=0;re<ReferencePoint_.length;re++){
				if(avairableRefFlag[re]){
				} else	if(nichi_minNumberSet.size() == 0){
					nichi_minNumberSet.add(re);
					continue;
				} else if (nichiCount[nichi_minNumberSet.get(0)] > nichiCount[re]){
					nichi_minNumberSet.clear();
					nichi_minNumberSet.add(re);
				} else if(nichiCount[nichi_minNumberSet.get(0)] == nichiCount[re]) {
					nichi_minNumberSet.add(re);
				}
			}

			//nichiカウントが同じ参照点が複数あるならランダムに取り出す．
			int nichi_minNumber = nichi_minNumberSet.get(Random.nextIntIE(0, nichi_minNumberSet.size()));

			List<Integer> nichi_minNumberSet_ = new ArrayList<Integer>();

			//その参照点の周辺の個体を集める．
			for(int ddd= 0; ddd < lastFront_.size() ;ddd++){
				if( nichi_minNumber == lastFront_.get(ddd).getIndex() ) {
					nichi_minNumberSet_.add(ddd);
				};
			}

			if(nichi_minNumberSet_.size() != 0){
				int index = -1;
				if(nichiCount[nichi_minNumber] == 0){
					Double min = Double.POSITIVE_INFINITY;
					for(int a=0;a<nichi_minNumberSet_.size() ; a++){
						int ind = nichi_minNumberSet_.get(a);
						double m = lastFront_.get(ind).getDistance();
						if(min > m){
							min = m;
							index = ind;
						}
					}
				} else {
					index = Random.nextIntIE(nichi_minNumberSet_.size());
				}
				population_.add(lastFrontpop.get(index));
				lastFront_.remove(index);
				lastFrontpop.remove(index);
				nichiCount[nichi_minNumber]++;
				i++;
			} else {
				avairableRefFlag[nichi_minNumber] = true;
			}
		}
	}

	private void Associate(){
		assert front.size() != 0 : "popsize が " + front.size() ;
		int size = front.size();
		for(int popnum=0;popnum<size;popnum++){
			double min = Double.POSITIVE_INFINITY;
			int minIndex_ = -1;
			double[] nowPoint  = front.get(popnum).get();
			for(int refnum = 0;refnum < ReferencePoint_.length; refnum++){
				double temp =  calcDistance(nowPoint, ReferencePoint_[refnum]);
				if(min > temp){
					min =  temp;
					minIndex_ = refnum;
				}
			}
			front.get(popnum).setDistance(min);
			front.get(popnum).setIndex(minIndex_);
		}
	}

	private double calcDistance(double[] Point, double [] ref){

		double s1 = 0;
		double s2 = 0;
		double refsize = 0;
		for(int i=0;i<Point.length;i++){
			s1 += Point[i]*Point[i];
			s2 += ref[i]*Point[i];
			refsize += ref[i]*ref[i];
		}
		s1 = Math.sqrt(s1);
		refsize = Math.sqrt(refsize);
		return  s1 - s2/refsize;
	}



	private void initPopulation() throws JMException, ClassNotFoundException {
		population_ = new Population(populationSize_);
 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			problem_.repair(newSolution,null);
			evaluations_++;
			problem_.evaluate(newSolution);
		 	population_.add(newSolution);
 		}
	}

	public void setReferencePoint() throws JMException{

		double[][] weight,weight_one;

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
	}



	public boolean makeOffSpring() throws JMException{
		offSpring_ = new Population(populationSize_);
		selection_ = new RandomSelectionWithReplacement(null);

		for(int i=0;i < populationSize_/2;i++){
			Solution[] parents = new Solution[numberOfParents_];
			Solution[] offSpring;

			int one =(int) selection_.execute(population_);// perm[i]; //(int) selection_.execute(population_);
			int two =(int) selection_.execute(population_);// perm[i]; //(int) selection_.execute(population_);


			// two parents must be differentl
			while((two = (int) selection_.execute(population_)) == one);


			parents[0] = population_.get(one);
			parents[1] = population_.get(two);
			offSpring = (Solution[]) crossover_.execute(parents);
			mutation_.execute(offSpring[0]);
			problem_.repair(offSpring[0],null);
			problem_.evaluate(offSpring[0]);
			offSpring_.add(offSpring[0]);
			evaluations_++;

			if(evaluations_ == maxEvaluations_){
				return false;
			}

			mutation_.execute(offSpring[1]);
			problem_.repair(offSpring[1],null);
			problem_.evaluate(offSpring[1]);
			offSpring_.add(offSpring[1]);
			evaluations_++;

			if(evaluations_ == maxEvaluations_){
				return false;
			}

		}
		return true;
	}

	void setting(){


		evaluations_  = 0;
		maxEvaluations_ = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		isMaxProblem_    = ((boolean)this.getInputParameter("ismax"));
		numberOfParents_= ((Integer) this.getInputParameter("numberOfParents")).intValue();
		offSpring_ = new Population();
		numberOfDivision_    = ((Integer)this.getInputParameter("numberOfDivision"));
		numberofObjectives_    = ((Integer)this.getInputParameter("numberOfObjectives"));
		time = ((Integer) this.getInputParameter("times")).intValue();

		directoryname = ((String) this.getInputParameter("DirectoryName"));
		generation=0;
		time = ((Integer) this.getInputParameter("times")).intValue();

		populationSize_ = ((Integer)this.getInputParameter("populationSize"));
		crossover_ = operators_.get("crossover"); // default: DE crossover
		mutation_ = operators_.get("mutation"); // default: polynomial mutation
//		outNormal_ = ((boolean) this.getInputParameter("outputNormal"));

		InnerWeightVectorDivision_ = ((Integer)this.getInputParameter("InnerWeightDivision"));
		isInnerWeightVector_ = ((InnerWeightVectorDivision_ > 0));


		isNorm = ((boolean) this.getInputParameter("IsNorm"));
		IdealPoint = new double[numberofObjectives_];
		extremePoint = new double[numberofObjectives_][numberofObjectives_];

		for(int i=0;i<numberofObjectives_;i++){
			IdealPoint[i] = Double.POSITIVE_INFINITY;
			for(int j=0;j<numberofObjectives_;j++){
				extremePoint[i][j] = Double.POSITIVE_INFINITY;
			}
		}
	}


}
