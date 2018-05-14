package momfo.problems.SOP;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Function {

	double[][] rotationMatrix_;

	double[]	shiftMatrix_;

	double[] UpperLimit;
	double[] LowerLimit;

	int numberOfDistanceVariables_;

	public int NumberOfDistanceVariables_(){;
		return  numberOfDistanceVariables_;
	}

	public void rotationFileReading(String name){
		rotationMatrix_ = new double[numberOfDistanceVariables_][numberOfDistanceVariables_];
	try(BufferedReader br = new BufferedReader(new FileReader(name))){
		String line;
		String[] S;
		int counter=0;

		while(( line = br.readLine())!= null){
			S  = line.split("	");
			for(int i=0;i<S.length;i++){
				rotationMatrix_[counter][i] =  Double.parseDouble(S[i]);
			}
			counter++;
		}
		line = null;
		if(counter != numberOfDistanceVariables_){
				IOException e = new IOException();
				e.printStackTrace();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void ShiftFileReading(String name){
		shiftMatrix_ = new double[numberOfDistanceVariables_];
	try(BufferedReader br = new BufferedReader(new FileReader(name))){
		String line;
		String[] S;
		int counter=0;

		while(( line = br.readLine())!= null){
			shiftMatrix_[counter] =  Double.parseDouble(line);
			counter++;
		}
		line = null;
		if(counter != numberOfDistanceVariables_){
				IOException e = new IOException();
				e.printStackTrace();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public void setRotationMatrix(double[][] d){
		rotationMatrix_ = d;
	}
	public void setShiftMatrix(double[]d){
		shiftMatrix_ = d;
	}



	abstract public double evaluate(double[] solution);






}
