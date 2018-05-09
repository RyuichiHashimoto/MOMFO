package lib.experiments.Exception.CommandSetting;

public class CannotConvertException extends CommandSettingException{

	public CannotConvertException(){
		super("cannot convert");
	}

	public CannotConvertException(String massage){
		super(massage);
	}
}
