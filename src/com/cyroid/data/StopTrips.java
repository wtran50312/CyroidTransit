package com.cyroid.data;


import java.util.Calendar;

/**
 * 
 * @author William Tran
 * A stop will contain multiple of these
 *
 */
public class StopTrips {
	
	private String tripID;
	
	private Calendar time = Calendar.getInstance();
	
	public StopTrips(String TripID, Calendar Time) {
		tripID = TripID;
		setTime(Time);
	}
	public StopTrips() {

	}
	
	
	//GETTER AND SETTERS

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getTripID() {
		return tripID;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public Calendar getTime() {
		return time;
	}
	
	

}
