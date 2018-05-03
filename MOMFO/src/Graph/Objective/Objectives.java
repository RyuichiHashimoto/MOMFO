package Graph.Objective;

import momfo.core.Population;

public class Objectives {

	private int objectiveDimention;

	private int objectiveSize;


	private double[][] Objectives;

	public Objectives(Population population){ 
		objectiveSize = population.size();
		objectiveDimention = population.getMaxDimOfObjective();
		Objectives = new double[objectiveSize][objectiveDimention];
	}

	public Objectives(Population population, int taskNumber){ 
		objectiveSize = population.size();
		int counter = 0;
		for(int  p  = 0;p < population.size();p++){
			if(population.get(p).getSkillFactor() == taskNumber){
				counter++;
				objectiveDimention = population.get(p).getNumberOfObjectives();
			}
		}
		Objectives = new double[counter][objectiveDimention];
		int iterator = 0;
		for(int  p  = 0;p < population.size();p++){
			if (population.get(p).getSkillFactor() == taskNumber){
				for(int o = 0;o < objectiveDimention;o++){
					Objectives[iterator++][o] = population.get(p).getObjective(o);
				}
			}
		}
	}

	public Objectives(double[][] objective){
		objectiveDimention = objective[0].length;
		objectiveSize = objective.length;
		Objectives = objective.clone();
	}
	
	public int getObjectiveDimention(){
		return objectiveDimention;
	}
	public int getObjectiveSize(){
		return objectiveSize;
	}
	public double[][] getObjective(){
		return Objectives;
	}
	public double[] getObjective(int index){
		return Objectives[index];
	}

	
}
