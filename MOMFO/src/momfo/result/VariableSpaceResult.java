package momfo.result;


import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import Network.GridComputing.StreamProvider;
import lib.experiments.CommandSetting;
import lib.experiments.ParameterNames;
import momfo.core.Population;

public class VariableSpaceResult extends GAResult {
	public static final String DEFAULT_FILE_NAME = "population.mtd";
	protected boolean isMaximize;
	protected CharArrayWriter caWriter;
	protected CommandSetting st;

	int nOfTrial = 0;
	int offSet;
	boolean isMultitask;
	List<String> FinalVar;
	List<String> InitialVar;
	int nOfTasks;
	@Override
	public void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException {
		this.solver = s.get(ParameterNames.SOLVER);
		st = s;
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);

		FinalVar = new ArrayList<String>();
		InitialVar = new ArrayList<String> ();
		if (isMultitask) {
			nOfTasks = solver.getGA().getProblemSet().countProblem();
		} else {
			nOfTasks = 1;
		}


		if(s.containsKey(ParameterNames.SEED_OFFSET)){
			offSet = s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}
	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
		if(isMultitask){
			Population[] pops = solver.getPopulationSet();
			for(int t = 0; t < pops.length;t++) {
				FinalVar.add(pops[t].variableToStr());
			}
		}else {
			FinalVar.add(solver.getPopulation().variableToStr());
		}
	}


	@Override
	public void afterInitialization() throws IOException, NameNotFoundException, NamingException {
		nOfTrial++;
		if(isMultitask){
			Population[] pops = solver.getPopulationSet();
			for(int t = 0; t < pops.length;t++) {
				InitialVar.add(pops[t].variableToStr());
			}
		}else {
			InitialVar.add(solver.getPopulation().variableToStr());
		}
	}

	private String getOutputFinName(int index) {
		return "FinalVAR"+"/" + "FinalVAR"+ (index)+".dat";
	}
	private String getOutputIniName(int index) {
		return "InitialVAR"+"/" + "InitialVAR"+ (index)+".dat";
	}

	private String getOutputFinName(int taskNumber,int index) {
		return "Task"+taskNumber+"/FinalVAR"+"/" + "FinalVAR"+ (index)+".dat";
	}
	private String getOutputIniName(int taskNumber, int index) {
		return "Task"+taskNumber+"/InitialVAR"+"/" + "InitialVAR"+ (index)+".dat";
	}

	private String getOutputIniName(int taskNumber, int index, boolean isMultitask_){
		if(isMultitask_) {
			return getOutputIniName(taskNumber,index);
		} else {
			return getOutputIniName(index);
		}
	}
	private String getOutputFinName(int taskNumber, int index, boolean isMultitask_){
		if(isMultitask_) {
			return getOutputFinName(taskNumber,index);
		} else {
			return getOutputFinName(index);
		}
	}

	protected String getOutputIniName(CommandSetting s,int t) throws NamingException{
		String ret = "out.dat";
		boolean isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		if(isMultitask){
			throw new NamingException("not implement yet");
		} else {
			ret = "InitialVAR" + "/" + "InitialVAR" + (1 + t)+".dat";
		}
		return s.getAsStr(ParameterNames.NAME_SPACE)+ "/" + ret;
	}

	protected String getOutputFinName(CommandSetting s,int t) throws NamingException{
		String ret = "out.dat";
		boolean isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		if(isMultitask){
			throw new NamingException("not implement yet");
		} else {
			ret = "FinalVAR" + "/" + "FinalVAR" + (1 + t)+".dat";
		}
		return s.getAsStr(ParameterNames.NAME_SPACE)+ "/" +ret;
	}


	/*these methods are used in GridComputing*/
	private String getOutputFinName(CommandSetting st,int taskNumber ,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+ "/" +"Task"+(taskNumber+1)+"/FinalVAR"+"/" + "FinalVAR"+ (offset+1)+".dat";
	}
	private String getOutputIniName(CommandSetting st,int taskNumber ,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+ "/" +"Task"+(taskNumber+1)+"/InitialVAR"+"/" + "InitialVAR"+ (offset+1)+".dat";
	}

	/*
	 * this method is used when this program is started from Runsetting.java.
	 */
	@Override
	public void save() throws IOException, NameNotFoundException, NamingException {
		int reps = InitialVar.size()/nOfTasks;
		String outputdir = "";
		for(int rep = 0; rep < reps;rep++) {
			for(int t = 0;t < nOfTasks;t++) {
				outputdir =  getOutputIniName(t+1,1+rep+offSet,isMultitask);
				Writer w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(outputdir);
				writer = new BufferedWriter(w);
				writer.write(InitialVar.get(rep*nOfTasks + t));
				writer.flush();

				outputdir =  getOutputFinName(t+1,1+rep+offSet,isMultitask);
				w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(outputdir);
				writer = new BufferedWriter(w);
				writer.write(FinalVar.get(rep*nOfTasks + t));
				writer.flush();
			}

		}
	}

	/*
	 * this method is used when Grid Computing is used.
	 */
	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException{
		StreamProvider sp = s.get(ParameterNames.STREAM_PROVIDER);
		String[] funData = ((String[])results[0]);
		boolean isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);

		if(s.containsKey(ParameterNames.SEED_OFFSET)) {
			offSet =s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}

		if(isMultitask){

			Writer w = null;
			int nOftasks = s.getAsInt(ParameterNames.N_OF_TASKS);

			for(int i = 0;i < funData.length/(2*nOftasks);i++) {
				for(int t= 0; t < nOftasks;t++) {
					w = new BufferedWriter(sp.getWriter(getOutputIniName(s,t,i+offSet)));
					w.write(funData[i*nOftasks + t]);
					w.close();
				}
			}

			for(int i = 0;i < funData.length/(2*nOftasks);i++) {
				for(int t= 0; t < nOftasks;t++) {
					w = new BufferedWriter(sp.getWriter(getOutputFinName(s,t,i+offSet)));
					w.write(funData[funData.length/2 + i*nOftasks + t]);
					w.close();
				}
			}

		}else{
			throw new IOException("not implement yet");

//			Writer w = null;
//			for(int i = 0;i < funData.length/2;i++){
//				w = new BufferedWriter(sp.getWriter(getOutputIniName(s,i+offSet)));
//				w.write(funData[2*i]);
//				w.close();
//				w = new BufferedWriter(sp.getWriter(getOutputFinName(s,i+offSet)));
//				w.write(funData[2*i+1]);
//				w.close();
//			}
		}
	}



	@Override
	public Serializable getMemento(){
		int maxSize = FinalVar.size() + InitialVar.size();
		String[] ret = new String[maxSize];
		for(int i =0;i<maxSize/2;i++) {
			ret[2*i] =  InitialVar.get(i);
			ret[2*i+1] = FinalVar.get(i);
		}
		return ret;
	}

	@Override
	public void close() throws IOException {
		if(writer != null)writer.close();

		InitialVar.clear();
		FinalVar.clear();
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException {
		return null;
	}


}
