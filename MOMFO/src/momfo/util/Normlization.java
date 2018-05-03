package momfo.util;

abstract public class Normlization{

	String name;

	boolean isMAX;

	Normlization(boolean s){
		isMAX = s;
		name = "Normlization";
	}

	Normlization(){
		isMAX = false;
		name = "Normlization";
	}

	public String getName(){
		return name;
	}


}
