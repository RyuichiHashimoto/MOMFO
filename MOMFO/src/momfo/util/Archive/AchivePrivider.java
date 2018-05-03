package momfo.util.Archive;

public class AchivePrivider {

	public static SolutionArchive ArchiveSolution(String archiveName){

		if(archiveName.contentEquals("DominanceArchive")){
			return new DominanceArchive();
		}
		return null;
	}

}
