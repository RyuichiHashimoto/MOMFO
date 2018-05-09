package lib.directory;

import java.io.File;

public class DirectoryMaker {

	public static void Make(String Derectory){
		String[] Directoryes = Derectory.split("/");

		String name = "";
		for(int i = 0;i< Directoryes.length;i++){
			name += Directoryes[i];
			File newDir = new File(name);
			newDir.mkdir();
			name += "/";
		}
	}


}
