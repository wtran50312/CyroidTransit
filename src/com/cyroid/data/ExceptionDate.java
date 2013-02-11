package com.cyroid.data;


import java.util.Calendar;

public class ExceptionDate  {
	
	//unique service ID
	private String serviceID;
	//dates
	private Calendar dates;
	//exception type
	private boolean remove;
	
	//Construction
	public ExceptionDate(String ServiceID,Calendar Dates,boolean ExceptionType){
		serviceID = ServiceID;
		dates = Dates;
		remove = ExceptionType;
	}
	public ExceptionDate(){
		serviceID = "";
		dates = null;
		remove = false;
	}
	
	//Getter and setter
	public void setServiceID(String serviceID){
		this.serviceID = serviceID;
	}
	public String getServiceID(){
		return serviceID;
	}
	public void setDates(Calendar dates){
		this.dates = dates;
	}
	public Calendar getDates(){
		return dates;
	}
	public void setRemove(boolean exceptionType){
		this.remove = exceptionType;
	}
	public boolean getRemove(){
		return remove;
	}
}
