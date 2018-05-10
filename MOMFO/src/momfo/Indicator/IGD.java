package momfo.Indicator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import lib.io.input.FileReader;
import lib.math.BuiltInRandom;
import lib.math.Distance;
import momfo.core.Population;
import momfo.util.JMException;
import momfo.util.Ranking.NDSRanking;



/*
 * this inplement is unconfirmed , So before use this inmpement , We must confirm this Implement
 *
 *
 */
public class IGD  extends Indicator{


	public static double IGD(Population d,double[][] ref){
		return IGD.CalcIGD(d.getAllObjectives(), ref);
	}
	public static double CalcIGD(double[][]  population , double[][] referencePoint){

		double ret = 0;
		int refSize = referencePoint.length;

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<population.length;pop++){
					min = Math.min(min,dist(population[pop],referencePoint[i]));
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;

	}
	public static double CalcIGD(double[][]  objective ,boolean[] check ,double[][] referencePoint){

		double ret = 0;
		int refSize = referencePoint.length;

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
				if(check[pop])
					min = Math.min(min,dist(objective[pop],referencePoint[i]));
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;

	}


	public static double CalcNormalizeIGD(double[][]  objective , double[][] referencePoint,double[] maxValue,double[] minValue){
		double[][] normalizeObjective = new double[objective.length][objective[0].length];
		double ret = 0;
		int refSize = referencePoint.length;

		for(int pop = 0;pop<objective.length;pop++){
			for(int o = 0; o < objective[0].length;o++){
				normalizeObjective[pop][o] = (objective[pop][o] - minValue[o])/(maxValue[o] - minValue[o]);
			}
		}
		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
					min = Math.min(min,dist(normalizeObjective[pop],referencePoint[i]));
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;

	}

	public static double CalcNormalizeIGD(double[][]  objective, boolean[] checker , double[][] referencePoint,double[] maxValue,double[] minValue){
		double[][] normalizeObjective = new double[objective.length][referencePoint[0].length];
		double ret = 0;
		int refSize = referencePoint.length;

		for(int pop = 0;pop<objective.length;pop++){
			for(int o = 0;o<referencePoint[0].length ;o++){
				normalizeObjective[pop][o] = (objective[pop][o] - minValue[o])/(maxValue[o] - minValue[o]);
			}
//			System.out.println(referencePoint[pop][0]*referencePoint[pop][0] + referencePoint[pop][1]*referencePoint[pop][1]);
		}

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<objective.length;pop++){
				if(checker[pop]){
					min = Math.min(min,dist(normalizeObjective[pop],referencePoint[i]));
				} else {
//					System.out.println("test");
				}
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;

	}
	private static double dist(double[] a,double[] b){
		double ret = 0;
		for(int i=0;i<a.length;i++){
			ret += Math.pow(a[i]-b[i],2d);
		}

		return Math.sqrt(ret);
	}

	public double IGD(double[][]  objective , double[][] referencePoint){

		int numberOfReferencePoint = referencePoint.length;
		double sum = 0;
		double min = 1.0E30;

		for (int i=0;i<referencePoint.length;i++){
			min = Distance.calc(objective[0], referencePoint[i]);

			for (int j=0;j<objective.length;j++){
				if (min > Distance.calc(objective[j], referencePoint[i])){
					min = Distance.calc(objective[j], referencePoint[i]);
				}
			}
			sum += min;
		}

		return sum/numberOfReferencePoint;
	}

	@Override
	public Object execute(Population ind, HashMap<String, Object> d) throws JMException {
		assert d.containsKey("referencePoint") : "execute in IGD: there are no referecenPoint in the HashMap";
		assert false :"未検証につき使用注意";
		System.out.println("this inplement is unconfirmed , So before use this inmpement , We must confirm this Implement");

		double[][] referencePoint = (double[][])d.get("referencePoint");

		double[][] Objectives = ind.getAllObjectives();

		return IGD(Objectives,referencePoint);
	}

	@Override
	public String getIndicatorName() {
		return "IGD";
	}
	public static double CalcNormalizeIGD_To_NonDominated(double[][] objective, boolean[] checker,
		double[][] referencePoint, double[] maxValue, double[] minValue,BuiltInRandom random) {
		double ret = 0;
		int refSize = referencePoint.length;
		int obj = referencePoint[0].length;

		NDSRanking rank = new NDSRanking(false,random);

		int counter = 0;
		for(int i = 0;i <checker.length;i++){
			if(checker[i]) counter++;
		}
		double[][] checkedObj = new double[counter][obj];
		counter = 0;


		for(int i = 0;i <checker.length;i++){
			if(checker[i]) {
				for(int o = 0; o < obj;o++){
					checkedObj[counter][o] = objective[i][o];
				}
				counter++;
			}
		}
		rank.setPop(new Population(checkedObj));
		rank.Ranking();

		double[][] NonDominated = rank.get(0).getAllObjectives();

		double[][] normalizeObjective = new double[NonDominated.length][obj];


		for(int pop = 0;pop<NonDominated.length;pop++){
			for(int o = 0;o<NonDominated[0].length ;o++){
				normalizeObjective[pop][o] = (NonDominated[pop][o] - minValue[o])/(maxValue[o] - minValue[o]);
			}
//			System.out.println(referencePoint[pop][0]*referencePoint[pop][0] + referencePoint[pop][1]*referencePoint[pop][1]);
		}

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<NonDominated.length;pop++){
					min = Math.min(min,dist(normalizeObjective[pop],referencePoint[i]));
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;
	}
	public static double CalcNormalizeIGD_To_NonDominated(double[][] objective, double[][] referencePoint,
			double[] maxValue, double[] minValue,BuiltInRandom random) {
		double ret = 0;
		int refSize = referencePoint.length;

		NDSRanking rank = new NDSRanking(false,random);
		rank.setPop(new Population(objective));
		rank.Ranking();

		double[][] NonDominated = rank.get(0).getAllObjectives();
		double[][] normalizeObjective = new double[NonDominated.length][referencePoint[0].length];


		for(int pop = 0;pop<NonDominated.length;pop++){
			for(int o = 0;o<NonDominated[0].length ;o++){
				normalizeObjective[pop][o] = (NonDominated[pop][o] - minValue[o])/(maxValue[o] - minValue[o]);
			}
//			System.out.println(referencePoint[pop][0]*referencePoint[pop][0] + referencePoint[pop][1]*referencePoint[pop][1]);
		}

		for(int i = 0;i <refSize;i++){
			double min = Double.POSITIVE_INFINITY;
			for(int pop = 0;pop<NonDominated.length;pop++){
					min = Math.min(min,dist(normalizeObjective[pop],referencePoint[i]));
			}
			ret += min*min;
		}
		return Math.sqrt(ret)/refSize;
	}


	public static void main(String[] args) throws FileNotFoundException, IOException{
		//double[][] TaskFUN_1 = fileReader.FileReading("Data/IGDConfigtion/FinalFUN_task1.dat"," ");
		double[][] TaskFUN_2 = FileReader.FileReading("Data/IGDConfigtion/FinalFUN_task2.dat"," ");

		IGDRef.AddRefFiles("Data/PF/concave.pf");

		double[][] refdata = IGDRef.getRefs(0);

		double d= CalcNormalizeIGD_To_NonDominated(TaskFUN_2,refdata,IGDRef.getMaxValue(0),IGDRef.getMinValue(0),new BuiltInRandom(3));
//		double d= CalcNormalizeIGD(TaskFUN_2,refdata,IGDRef.getMaxValue(0),IGDRef.getMinValue(0));



		for(int i = 0;i <TaskFUN_2.length;i++){
			System.out.println(d);
		}


	}

}
