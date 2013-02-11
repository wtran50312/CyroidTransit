package com.cyroid.data;

import java.util.Calendar;

public class RunDay  {
	
	//unique serviceID 
	private String serviceID;
	//starting date
	private Calendar startDate;
	//ending date
	private Calendar endDate;
	//day of the week
	private boolean [] week ;
	
	//Construction
	public RunDay (String ServiceID, Calendar StartDate, Calendar EndDate, boolean [] Week){
		serviceID = ServiceID;
		startDate = StartDate;
		endDate = EndDate;
		week = new boolean[7];
		week = Week;
	}
	public RunDay(){
		serviceID = "";
		startDate = null;
		endDate = null;
		week = new boolean[7] ;
		
	}
	
	//Setter and getter
	public void setServiceID(String serviceID){
		this.serviceID = serviceID;
	}
	public String getServiceID(){
		return serviceID;
	}
	public void setStartDate(Calendar startDate){
		this.startDate = startDate;
	}
	public Calendar getStartDate(){
		return startDate;
	}
	public void setEndDate(Calendar endDate){
		this.endDate = endDate;
	}
	public Calendar getEndDate(){
		return endDate;
	}
	public void setWeek(boolean [] week){
		this.week = week;
	}
	public boolean [] getWeek(){
		return week;
	}
}
