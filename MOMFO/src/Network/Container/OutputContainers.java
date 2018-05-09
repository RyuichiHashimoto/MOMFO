package Network.Container;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lib.directory.DirectoryMaker;
import lib.io.FileConstants;

public class OutputContainers implements Serializable{

	List<String> outputDir;

	List<double[][]> Data;

	public OutputContainers(){
		outputDir = new ArrayList<String>();
		Data = new ArrayList<double[][]>();
	}

	public String outputToFile(String filePath, double[][] data){

		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;

		try {
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);

			for(int d=0;d < data.length;d++){
				for(int t = 0 ; t < data[d].length-1;t++){
					bw.write(data[d][t]+FileConstants.FILE_DEMILITER);
				}
				bw.write(  String.valueOf(data[d][data[d].length-1] ) );
				bw.newLine();
			}

			bw.close();

		} catch (FileNotFoundException e) {

			// args is the args remove the last element.
			String[] arg = filePath.split(FileConstants.FILEPATH_DEMILITER);

			String[]  args = new String[arg.length-1];
			for(int i= 0;i < args.length ;i++){
				args[i] = arg[i];
			}

			String dir = String.join(FileConstants.FILEPATH_DEMILITER, args);
			DirectoryMaker.Make(dir);
			return outputToFile(filePath,data);

		} catch (IOException e) {
			return FileConstants.FILE_OUTPUT_ERROR;
		}

		return FileConstants.FILE_OUTPUT_SUCCESS;

	}

	public void outputToFile(){
		for(int t = 0; t< outputDir.size();t++){
			outputToFile(outputDir.get(t), Data.get(t));
		}
	}


}
