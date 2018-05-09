package Network.Container;

import java.io.Serializable;

/*
 *
 *
 */

public class Container implements Serializable{

	private String Status;

	private String Type;

	private Object container;

	public Container(Object obj){
		container = obj;

		if(! (obj == null))
			Type = obj.getClass().getName();
		else
			Type = "null";
	}

	public String getType(){
		return Type;
	}
	public String getStatus(){
		return Status;
	}
	public void setStatus(String status_){
		Status = status_;
	}

	public Object get(){
		return container;
	}


	public static void main(String[] args) {
		int d = 4;
		Container con = new Container(d);

		System.out.println(con.getType());
		System.out.println(con.get());
	}


}
