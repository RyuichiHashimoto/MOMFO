package lib.experiments.Exception.CommandException;

public class HeaderNotFoundException extends CommandException{

	public HeaderNotFoundException(){
		super("please add the header to commandList file");
	}

	public HeaderNotFoundException(String command){
		super(command);
	}
}
