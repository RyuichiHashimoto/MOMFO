package lib.directory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryGetter {

	public static List<String> getSubDirectory(String directory){
		List<String> ret = new ArrayList<String>();
			
		File directoryFile = new File(directory);
	
		if (!directoryFile.exists()) {
			System.out.println("error");
			return new ArrayList<String>();
		}
		
		File [] subFiles = directoryFile.listFiles();

		if(subFiles == null) {
			return new ArrayList<String>();
		}
		
		for (File file : subFiles) {
			System.out.println(file.getPath());
		}
		
		return ret;
	}
	public static void main(String[] args) {
		List<String> dirs = getSubDirectory("dfasdfaf");
		
		for(String dir : dirs) {
			System.out.println(dir);
		}
	}
	
}
