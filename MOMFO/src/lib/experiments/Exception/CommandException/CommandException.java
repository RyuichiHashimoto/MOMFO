package lib.experiments.Exception.CommandException;

public class CommandException extends Exception{

	public CommandException(){
		super("Please follow the format");
	}
	public CommandException(String massage){
		super(massage);
	}

	
}
