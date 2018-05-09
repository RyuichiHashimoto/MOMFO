package lib.experiments.Exception.CommandSetting;

public class notFoundException extends CommandSettingException {

	public notFoundException(){
		super("the key is not found exception");
	}

	public notFoundException(String massage){
		super(massage);
	}

}
