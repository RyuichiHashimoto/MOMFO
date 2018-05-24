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

public class ObjectiveSpaceResult extends GAResult {
	public static final String DEFAULT_FILE_NAME = "population.mtd";
	protected boolean isMaximize;
	protected CharArrayWriter caWriter;
	protected CommandSetting st;
	boolean isMultitask;
	int offSet;
	List<String> FinalFun;
	List<String> InitialFun;
	int nOfTasks = 0;
	@Override
	public void build(CommandSetting s) throws NamingException, IOException, ReflectiveOperationException {
		this.solver = s.get(ParameterNames.SOLVER);
		st = s;
		isMultitask = s.getAsBool(ParameterNames.IS_MULTITASK);
		if (isMultitask) {
			nOfTasks = solver.getGA().getProblemSet().countProblem();
		} else {
			nOfTasks = 1;
		}

		FinalFun = new ArrayList<String>();
		InitialFun = new ArrayList<String> ();
		if(s.containsKey(ParameterNames.SEED_OFFSET)){
			offSet = s.getAsInt(ParameterNames.SEED_OFFSET);
		} else {
			offSet = 0;
		}
	}

	@Override
	public void afterInitialization() throws IOException, NameNotFoundException, NamingException {
		if(isMultitask){
			Population[] pops = solver.getPopulationSet();
			for(int t = 0; t < pops.length;t++) {
				InitialFun.add(pops[t].objectiveToStr());
			}
		}else {
			InitialFun.add(solver.getPopulation().objectiveToStr());
		}
	}

	@Override
	public void afterTrial() throws IOException, NameNotFoundException, NamingException {
		if(isMultitask){
			Population[] pops = solver.getPopulationSet();
			for(int t = 0; t < pops.length;t++) {
				FinalFun.add(pops[t].objectiveToStr());
			}
		}else {
			FinalFun.add(solver.getPopulation().objectiveToStr());
		}
	}


	private String getOutputFinName(int index) {
		return "FinalFUN"+"/" + "FinalFUN"+ (index)+".dat";
	}
	private String getOutputIniName(int index) {
		return "InitialFUN"+"/" + "InitialFUN"+ (index)+".dat";
	}

	private String getOutputFinName(int taskNumber,int index) {
		return "Task"+taskNumber+"/FinalFUN"+"/" + "FinalFUN"+ (index)+".dat";
	}
	private String getOutputIniName(int taskNumber, int index) {
		return "Task"+taskNumber+"/InitialFUN"+"/" + "InitialFUN"+ (index)+".dat";
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

	@Override
	public void save() throws IOException, NameNotFoundException, NamingException {
		int reps = InitialFun.size()/nOfTasks;
		String outputdir = "";
		for(int rep = 0; rep < reps;rep++) {
			for(int t = 0;t < nOfTasks;t++) {
				outputdir =  getOutputIniName(t+1,1+rep+offSet,isMultitask);
				Writer w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(outputdir);
				writer = new BufferedWriter(w);
				writer.write(InitialFun.get(rep*nOfTasks + t));
				writer.flush();

				outputdir =  getOutputFinName(t+1,1+rep+offSet,isMultitask);
				w = ((StreamProvider) solver.setting.get(ParameterNames.STREAM_PROVIDER)).getWriter(outputdir);
				writer = new BufferedWriter(w);
				writer.write(FinalFun.get(rep*nOfTasks + t));
				writer.flush();
			}
		}
	}


	private String getOutputFinName(CommandSetting st,int taskNumber ,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+ "/" +"Task"+(taskNumber+1)+"/FinalFUN"+"/" + "FinalFUN"+ (offset+1)+".dat";
	}
	private String getOutputIniName(CommandSetting st,int taskNumber ,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+ "/" +"Task"+(taskNumber+1)+"/InitialFUN"+"/" + "InitialFUN"+ (offset+1)+".dat";
	}

	private String getOutputIniName(CommandSetting st,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+"/InitialFUN"+"/" + "InitialFUN"+ (offset+1)+".dat";
	}

	private String getOutputFinName(CommandSetting st,int offset) throws NameNotFoundException {
		return st.getAsStr(ParameterNames.NAME_SPACE)+"/FinalFUN"+"/" + "FinalFUN"+ (offset+1)+".dat";
	}

	@Override
	public void save(CommandSetting s, Object... results) throws IOException, NamingException, ReflectiveOperationException {
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
	public Serializable getMemento() {
		int maxSize = FinalFun.size() + InitialFun.size();
		String[] ret = new String[maxSize];
		if(isMultitask) {
			for(int i = 0;i <InitialFun.size();i++){
				ret[i] = InitialFun.get(i);
			}
			for(int i = 0;i <FinalFun.size();i++){
				ret[i+InitialFun.size()] = FinalFun.get(i);
			}

		}else {
			for(int i =0;i<maxSize/2;i++) {
				ret[2*i] =  InitialFun.get(i);
				ret[2*i+1] = FinalFun.get(i);
			}
		}
		return ret;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	protected String getOutputName(CommandSetting s) throws NamingException{
		throw new NamingException("not implement yet");
//		return ret;
	}


}
