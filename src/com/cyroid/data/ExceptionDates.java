package com.cyroid.data;



import java.util.LinkedList;

public class ExceptionDates  {

	// link list of exception date
	private LinkedList<ExceptionDate> exceptionDates;
	
	
	//Construction
	public ExceptionDates(){
		exceptionDates = new LinkedList<ExceptionDate>();
	}
	
	//SETTER and GETTER
	public void setExceptionDates(LinkedList<ExceptionDate> exceptionDates){
		this.exceptionDates = exceptionDates;
	}
	public LinkedList<ExceptionDate> getExceptionDates(){
		return exceptionDates;
	}
}
