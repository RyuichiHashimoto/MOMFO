package lib.lang;

public class NotVerifiedYet extends Exception{
	private static final long serialVersionUID = 1L;
	public NotVerifiedYet() {
		super();
	}
	public NotVerifiedYet(String massage){
		super(massage);
	}
	
	public NotVerifiedYet(Object d,String massage) {
		super(d.getClass().getName()+":" +massage);
	}
	
	public NotVerifiedYet(Object d) {
		super(d.getClass().getName());
	}

}
