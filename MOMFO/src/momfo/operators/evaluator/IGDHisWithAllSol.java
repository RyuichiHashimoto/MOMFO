package momfo.operators.evaluator;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import lib.experiments.Exception.CommandSetting.notFoundException;
import lib.util.StringUtility;
import momfo.Indicator.IGDCalclator;
import momfo.Indicator.IGDRef;
import momfo.Indicator.IGDWithAllSol;
import momfo.core.Population;
import momfo.core.ProblemSet;
import momfo.problems.MOMFOP.NTU.CIHS;
import momfo.util.JMException;

/*
 * This class assumes that the class of evaluation in base class object is List<Double>
 *
 */

public class IGDHisWithAllSol extends Evaluator {

	IGDRef[] IGDReference;

	IGDCalclator[] IGDCalclator;

	String[] FileName;

	int nOfTasks;
	public String getIGDFileName(int key) {return FileName[key];};

	public IGDHisWithAllSol() {

	}

	public IGDRef getIGDRef(int key){
		return IGDReference[key];
	}

	@Override
	public void build(CommandSetting s) throws NameNotFoundException, JMException, NamingException,
			ReflectiveOperationException, IOException, notFoundException {
		 
		if(isMultitask){
			ProblemSet p = (s.get(ParameterNames.PROBLEM_SET));
			nOfTasks = p.countProblem();
			IGDReference = new IGDRef[nOfTasks];
			FileName = new String[nOfTasks];
			IGDCalclator = new IGDCalclator[nOfTasks];
			
			for(int t =0 ; t < nOfTasks;t++) {
				FileName[t] = p.get(t).getIGDRefFile();				
				IGDReference[t] = new IGDRef(FileName[t]);
				IGDCalclator[t] = new IGDWithAllSol();
				IGDCalclator[t].build(s);				
			}			
		} else {
			ProblemSet p = (s.get(ParameterNames.PROBLEM_SET));
						
			nOfTasks = 1;
			int taskNumber = s.getAsInt(ParameterNames.TASK_NUMBER);
			
			IGDReference = new IGDRef[nOfTasks];
			FileName = new String[nOfTasks];
			IGDCalclator = new IGDCalclator[nOfTasks];
			
			FileName[0] = p.get(taskNumber).getIGDRefFile();				
			IGDReference[0] = new IGDRef(FileName[0]);
			IGDCalclator[0] = new IGDWithAllSol();
			IGDCalclator[0].build(s);							
		}
		
	}

		
	@Override
	public void evaluate(Object d) {		
		if(isMultitask){
			flag = true;		
			Population pop = (Population)d;
			double[][] obj = pop.getAllObjectives();
			((List<Double>)((evaluation)[0])).add(IGDCalclator[0].calcNormalizeIGD(obj,IGDReference[0]));
		} else if (isMultitask){
			Population[] pops = (Population[])d;
			for(int i = 0;i < pops.length;i++) {
				flag = true;		
				Population pop = (Population)pops[i];
				((List<Double>)((evaluation[i]))).add(IGDCalclator[i].calcNormalizeIGD(pop.getAllObjectives(),IGDReference[i]));		
			}			
		}			
	}
	
	public static void main(String[] args) throws IOException, NameNotFoundException, notFoundException, JMException, NamingException, ReflectiveOperationException {
		IGDHisWithAllSol igd = new IGDHisWithAllSol();
		CommandSetting setting = new CommandSetting();
		ProblemSet p = new CIHS(setting);

		System.out.println(igd.getClassName());		
	}

	@Override
	public void save(Writer writer) throws IOException { 		

		if(isMultitask){
			for(int t =0;t < nOfTasks;t++) {
							
				List<Double> ret = (List<Double>) evaluation[t];				
				for(int i =0 ;i<ret.size();i++){
					writer.write(StringUtility.toStringWithIndex(  (Double [])ret.toArray()));
				}		
			}
			
		}
	}

	@Override
	public void initialize(){
		evaluation = new Object[nOfTasks];
		for(int i = 0;i < evaluation.length;i++) {
			evaluation[i] = new ArrayList<Double>();
		}		  
	}
	
	
	
}
