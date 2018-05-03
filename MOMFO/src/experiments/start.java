package experiments;
import static org.kohsuke.args4j.ExampleMode.*;

import java.util.List;

import javax.naming.NameNotFoundException;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
/*
 * ここで指定することによりプログラムを実行できるようにする．
 * 増田さんが使用していたargs4j が便利そうなのでそれを利用できるか検討する．
 *
 * 現段階の案として，いちいち変更が必要なものに関して，settingに書くよりここに書いた方がいいものはここに書く．
 * 一回の試行で一つの問題を解く;
 *
 *
 */

import momfo.core.AlgorithmMain;
import momfo.main.AlgorithmMainFactory;
import momfo.util.JMException;

/*
 * きれいにかきましょう．
 *　SMSEmomfoAに使用されるrefについては，後でやる・
 * 引き数は全てString型で読み込む
 * 　Boolean型はYesならTrue,NoならFalseと表現する
 *  refは注意必要
 *  ,,各試行の参照点の実験設定の区切り, で各問題に対する区切り
 *
 */

public class start {

	@Option(name="-ga", aliases= "--GeneticAlgorithm", required=true, usage="Specify the populationSize")
	private String geneticAlgorithmName;
	@Option(name="-nt", aliases= "--numberOfTasks", required=true, usage="Specify the number of tasks")
	private int  numberOfTasks;
	@Option(name="-sepa", aliases= "--separate", required=false, usage="Specify the number of tasks")
	private boolean  separate;

	@Option(name="-nowTrial", aliases= "---nowTrial", required=false, usage="Specify the nowTrial")
	private int nowTrial = 0;
	@Option(name="-p", aliases= "--Problem Name", required=true, usage="set the Problem Name")
	private String problenName;
	@Option(name="-max", aliases= "--MaxProblem", required=false, metaVar="<MaxProblem>", usage="Specify the the maximum Problem or minimum Problem: default assume min Problem")
	private String IsMax = null;
	@Option(name="-norm", aliases= "--norm", required=false, metaVar="<ReferencePoint>", usage="you ")
	private String IsNorm = null;
	@Option(name="-on", aliases= "--output Normalization", required=false, metaVar="<momfoEAD Alphar>", usage="you set whether Objectives of Population are Normalized or not, Default false")
	private boolean OutNorm = false;
	@Option(name="-ps", aliases= "--PopulationSize", required=false, metaVar="<populationSize>", usage="Specify the populationSize")
	private String populationSize = null;
	@Option(name="-co", aliases= "--Crossover", required=false, metaVar="<Crossover>", usage="Specify the Crossover")
	private String crossoverName = null;
	@Option(name="-cop", aliases= "--CrossoverProbability", required=false, metaVar="<CrossovePror>", usage="Specify the CrossoverProbability")
	private String CrossoverProbablity = null;
	@Option(name="-cod", aliases= "--CrossoverDistribution", required=false, metaVar="<CrossoverDistribution>", usage="Specify the CrossoverDistribution")
	private String CrossoverDistribution = null;
	@Option(name="-mt", aliases= "--Mutation", required=false, metaVar="<Mutation>", usage="Specify the Mutation")
	private String MutationName = null;
	@Option(name="-mtp", aliases= "--MutationProbablity", required=false, metaVar="<mutationProbablity>", usage="Specify the Mutation Probablity")
	private String MutationProbablity =  null;
	@Option(name="-mtd", aliases= "--MutationDistribution", required=false, metaVar="<MutationDistribution>", usage="Specify the Mutation Probablity")
	private String MutationDistribution = null;
	@Option(name="-rep", aliases= "--repeat time", required=false, metaVar="<Repeat time>", usage="Specify the the Iteration")
	private String reps = null;
	@Option(name="-div", aliases= "--Division", required=false, metaVar="<Division>", usage="Specify the division in momfoEA/D or NSGAIII")
	private String  Division = null;
	@Option(name="-indiv", aliases= "--Innerdivision", required=false, metaVar="<InnerDivision>", usage="Specify the inner division in momfoEA/D or NSGA-III")
	private String InnerDivision = null;
	@Option(name="-ref", aliases= "--ReferencePoint", required=false, metaVar="<ReferencePoint>", usage="Specify the ReferencePoint in SMS-EmomfoA")
	private String ref = null;
	@Option(name="-eva", aliases= "--MaxEvaluations", required=false, metaVar="<MaxEvaluations>", usage="Specify --MaxEvaluations")
	private String maxEvaluations_ = null;
	@Option(name="-seed", aliases= "--seed", required=false, metaVar="<Seed >", usage="Specify the seed of random number")
	private int seed_ = 1;
	@Option(name="-sf", aliases= "--ScalarFunction", required=false, metaVar="<ScalarFunction>", usage="Specify the ScalarFunction")
	private String ScalarFunction = null;
	@Option(name="-alpha", aliases= "--momfoEAD Alpha", required=false, usage="Specify momfoEAD Alphar, changing the magnification of the distance to the idealpoint")
	private String alphar = null;
	@Option(name="-mn", aliases= "--mating Neighborhood", required=false, usage="Specify momfoEAD mating neighborhood")
	private String matingNeighborhood = null;
	@Option(name="-rn", aliases= "--replace Neighborhood", required=false, usage="Specify momfoEAD replace neighborhood")
	private String ReplaceNeighborhood = null;
	@Option(name="-ep", aliases= "--Epsilon", required=false, usage="Specify the Epsilon on NormalizeObjectives")
	private String epsiron = null;
	@Option(name="-dir", aliases= "--Directory", required=false, usage="Specify the Directory storing Directory")
	private String Directory = null;
	@Option(name="-PBITheta", aliases= "--PBITheta", required=false, usage="Specify the parameter of the scalarzing function PBI used in momfoEAD")
	private String PBITheta = null;
	@Option(name="-rmp", aliases= "--random mating probablity", required=false, usage="Specify the parameter of the scalarzing function PBI used in momfoEAD")
	private String rmp = null;

	@Argument
	private List<String> arguments;

	public static void main(String[] args) throws NameNotFoundException, ClassNotFoundException, JMException {
		 new start().domain(args);
	}

	public void domain(String[] args) throws NameNotFoundException, ClassNotFoundException, JMException {
	      CmdLineParser parser = new CmdLineParser(this);
	        // if you have a wider console, you could increase the value;
	        // here 80 is also the default
	        parser.setUsageWidth(80);

	        try {
	        	parser.parseArgument(args);
	        	if (arguments != null) throw new IllegalArgumentException("No arg is given. ");
	        } catch( CmdLineException e ) {
	            System.err.println(e.getMessage());
	            System.err.println("java test [options...] arguments...");
	            // print the list of available options
	            parser.printUsage(System.err);
	            System.err.println();
	            // print option sample. This is useful some time
	            System.err.println("  Example: java SampleMain"+parser.printExample(ALL));
	            return;
	        }
	        if(separate){
	        	Separate();
	        } else {
	        	Multitasking();
	        }

	}
	private void Multitasking() throws JMException, ClassNotFoundException, NameNotFoundException {
		Setting hashmap  = new Setting();
        AlgorithmMain algorithm;

        //コマンドラインによる実験設定の追加
        add(hashmap);

        //ファイル読み込みからの実験設定の追加
        hashmap.experiment_setting("setting/" + geneticAlgorithmName + ".st");

        // ファイル読み込みの追加
        hashmap.add("TaskNumber",0);
        algorithm = AlgorithmMainFactory.getAlgorithmMain(geneticAlgorithmName, hashmap);
        algorithm.runMultitask(nowTrial);


	}

	public void Separate() throws JMException, ClassNotFoundException, NameNotFoundException{
        Setting[] hashmap  = new Setting[numberOfTasks];
        AlgorithmMain[] algorithm = new AlgorithmMain[numberOfTasks];
        //設定クラスの初期化
        for (int t= 0; t < numberOfTasks;t++){
        	hashmap[t]  = new Setting();
        }

        //コマンドラインによる実験設定の追加
        add(hashmap);

        //ファイル読み込みからの実験設定の追加
        for (int t= 0; t < numberOfTasks;t++){
	        hashmap[t].experiment_setting("setting/" + geneticAlgorithmName + ".st");
        }

        // ファイル読み込みの追加
        for (int t= 0; t < numberOfTasks;t++){
        	
        	hashmap[t].add("TaskNumber",t);
        	algorithm[t] = AlgorithmMainFactory.getAlgorithmMain(geneticAlgorithmName, hashmap[t]);
        	algorithm[t].run(nowTrial);
        }
	}


	public void addHashmap(Setting[] hashmap,String title, String name){
		if(!name.contains(",")){
			for(int i=0;i<numberOfTasks;i++){
				hashmap[i].add(title, name);
			}
		} else {
			String[] each = name.split(",");
			if(each.length != hashmap.length){
				System.err.print("設定名とファイル名の変更");
			}
			for(int s = 0; s < each.length; s++){
				hashmap[s].add(title, each[s]);
			}
		}
	}
	public void addHashmap_boolean(Setting[] hashmap,String title, String name){
		if(name == null){
			for(int t= 0 ;t <numberOfTasks;t++){
				hashmap[t].add("titel",false);
			}
			return ;
		}
		if(!name.contains(",")){
			for(int i=0;i<numberOfTasks;i++){
				hashmap[i].add(title, name);
			}
		} else {
			String[] each = name.split(",");
			if(each.length != hashmap.length){
				System.err.print("設定名とファイル名の変更");
			}
			for(int s = 0; s < each.length; s++){
				if(each[s].contains("N")){
					hashmap[s].add(title, false);
				} else if (each[s].contains("Y")){
					hashmap[s].add(title, true);
				}
			}
		}
	}

	public void add(Setting[] hashmap) throws JMException{
        addHashmap(hashmap,"Problem",problenName);
        addHashmap(hashmap,"Seed", String.valueOf(seed_));
        if (maxEvaluations_ != null) addHashmap(hashmap,"maxEvaluations",maxEvaluations_);
        System.out.println(maxEvaluations_ + "	");

        addHashmap_boolean(hashmap,"IsMax", IsMax);
        addHashmap_boolean(hashmap,"IsNorm", IsNorm);


        if(epsiron != null)  addHashmap(hashmap,"epsilon", epsiron);
        if(populationSize != null) addHashmap(hashmap,"populationSize", populationSize);
        if(crossoverName != null) addHashmap(hashmap,"CrossoverName", crossoverName);
        if(CrossoverProbablity != null) addHashmap(hashmap,"CrossoverProbability", CrossoverProbablity);
        if(CrossoverDistribution != null) addHashmap(hashmap,"CrossoverDistribution", CrossoverDistribution);
        if(MutationName != null) addHashmap(hashmap,"MutationName", MutationName);
        if(MutationProbablity != null) addHashmap(hashmap,"MutationProbablity", MutationProbablity);
        if(MutationDistribution != null) addHashmap(hashmap,"MutationDistribution", MutationDistribution);
        if(reps != null) addHashmap(hashmap,"NumberOfTrial", reps);
        if(Division != null) addHashmap(hashmap,"Division", Division);
        if(InnerDivision != null) addHashmap(hashmap,"InnerWeightDivision",InnerDivision);
        if(ref != null) addHashmap(hashmap,"ref", ref);
        if(PBITheta != null) addHashmap(hashmap,"PBITheta", PBITheta);

        if(ref != null) addHashmap(hashmap,"ref", ref);
        if(IsMax == null){
        	IsMax = "N";
        }


        //スカラー化関数の最大最少と問題を照合している．
        if(ScalarFunction != null) {
        	if(!ScalarFunction.contains(",") && !IsMax.contains(",")){
            	if(IsMax.contains("Y"))
            		addHashmap(hashmap,"ScalarFunction", ScalarFunction);
            	else
            		addHashmap(hashmap,"ScalarFunction", ScalarFunction + "Formin");
        	}else if(!ScalarFunction.contains(",") && IsMax.contains(",")){
            	for(int t= 0; t < numberOfTasks;t++){
            		String test = ScalarFunction;
            		if(IsMax.contains("N")){
            			test = test + "Formin";
            		}
            		hashmap[t].add("ScalarFunction", test);
            	}
        	} else if (ScalarFunction.contains(",") && !IsMax.contains(",")){
        		String[] test  = ScalarFunction.split(",");
        		for(int t = 0 ; t < test.length;t++){
        			if(IsMax.contains("N")){
        				test[t] = test[t] + "Formin";
        			}
        			hashmap[t].add("ScalarFunction", test[t]);
        		}
        	}else if (ScalarFunction.contains(",") && IsMax.contains(",")){
        		String[] SFs = ScalarFunction.split(",");
        		String[] isMAXs = IsMax.split(",");
        		if(SFs.length != isMAXs.length || SFs.length != numberOfTasks){
        			System.err.println("Error Massage");
        		}
        		for(int t =  0 ; t < numberOfTasks;t++){
        			if(isMAXs[t].contains("N") ||isMAXs[t].contains("n")){
        				SFs[t] = SFs[t] +"Formin";
        			}
        			hashmap[t].add("ScalarFunction",SFs[t]);
        		}

        	}
        }


        if(alphar != null) addHashmap(hashmap,"alpha", alphar);
        if(matingNeighborhood != null) addHashmap(hashmap,"matingNeighborhood", matingNeighborhood);
        if(ReplaceNeighborhood !=  null) addHashmap(hashmap,"ReplaceNeighborhood", ReplaceNeighborhood);
       // if(Directory != null) hashmap.add("ResultDirectory", Directory);

	}

	public void add(Setting hashmap) throws JMException{
        hashmap.add("Problem", problenName);
        hashmap.add("Seed", String.valueOf(seed_));

        if (maxEvaluations_ != null) hashmap.add("maxEvaluations",maxEvaluations_);
        System.out.println(maxEvaluations_ + "	");

        if(IsMax != null){
        	if(IsMax.contains(",")){
        		String[] d =IsMax.split(",");

        		for(int t = 0; t < d.length;t++){
	        		if(d[t].contains("Y") || d[t].contains("y")){
		        		hashmap.add("IsMax"+ String.valueOf(t+1), true);
		        	} else if(d[t].contains("N") || d[t].contains("n")){
		        		hashmap.add("IsMax"+ String.valueOf(t+1), false);
		        	}
        		}
        	} else {
	        	for(int t=0;t<numberOfTasks;t++){
			        if(IsMax.contains("Y") ||IsMax.contains("y") ){
			        	hashmap.add("IsMax"+String.valueOf(t+1), true);
			        } else if(IsMax.contains("N") ||IsMax.contains("n") ){
			        	hashmap.add("IsMax"+String.valueOf(t+1), false);
			        } else {
			        	System.out.println("Miss: in add(Setting) in Experiments\\start.java");
			        }
	        	}
        	}
        }


        if(IsNorm != null){
        	if(IsNorm.contains("Y") || IsNorm.contains("y") ){
        		hashmap.add(IsNorm, true);
        	} else if (IsNorm.contains("N") ||IsNorm.contains("n")  ){
        		hashmap.add(IsNorm, false);
        	}
        }

        if(epsiron != null)  hashmap.add("epsilon", epsiron);
        if(populationSize != null) hashmap.add("populationSize", populationSize);
        if(crossoverName != null) hashmap.add("CrossoverName", crossoverName);
        if(CrossoverProbablity != null) hashmap.add("CrossoverProbablity", CrossoverProbablity);
        if(CrossoverDistribution != null) hashmap.add("CrossoverDistribution", CrossoverDistribution);
        if(MutationName != null) hashmap.add("MutationName", MutationName);
        if(MutationProbablity != null) hashmap.add("MutationProbability", MutationProbablity);
        if(MutationDistribution != null) hashmap.add("MutationDistribution", MutationDistribution);
        if(reps != null) hashmap.add("NumberOfTrial", reps);
        if(Division != null) hashmap.add("Division", Division);
        if(InnerDivision != null) hashmap.add("InnerWeightDivision",InnerDivision);
        if(ref != null) hashmap.add("ref", ref);
        if(PBITheta != null) hashmap.add("PBITheta", PBITheta);
        if(rmp != null) hashmap.add("rmp", rmp);

//        if(ref != null) addHashmap(hashmap,"ref", ref);


        //スカラー化関数の最大最少と問題を照合している．
        if(ScalarFunction != null) {
        	if(!ScalarFunction.contains(",") && !IsMax.contains(",")){
            	if(IsMax.contains("Y") ||IsMax.contains("y") )
            		hashmap.add("ScalarFunction", ScalarFunction);
            	else if (IsMax.contains("N")||IsMax.contains("n"))
            		hashmap.add("ScalarFunction", ScalarFunction + "Formin");
        	} else if(ScalarFunction.contains(",") &&  !IsMax.contains(",")){
        		String[] sf = ScalarFunction.split(",");
        		if(IsMax.contains("y") || IsMax.contains("Y"))
	            	hashmap.add("ScalarFunction", ScalarFunction);
	            else if(IsMax.contains("N") || IsMax.contains("n") ){
	            	String[] function = ScalarFunction.split(",");
	            	String name = "";
	            	for(int t = 0; t< numberOfTasks-1;t++){
	            		name += function[t] + "Formin,";
	            	}
	            	name += function[numberOfTasks-1] + "Formin";
	            	hashmap.add("ScalarFunction",name);
	            }
        	} else {


        	}

        }


        if(alphar != null) hashmap.add("alpha", alphar);
        if(matingNeighborhood != null) hashmap.add("matingNeighborhood", matingNeighborhood);
        if(ReplaceNeighborhood !=  null) hashmap.add("ReplaceNeighborhood", ReplaceNeighborhood);
       // if(Directory != null) hashmap.add("ResultDirectory", Directory);

	}



}
