package com.cyroid.data;


import java.util.ArrayList;


/**
 * 
 * @author William Tran
 * Represents a bus stop
 *
 */
public class Stop  {
	
	

	//A lat and lon location
	private CyroidLocation location;

	//unique stopID
	private String stopID;
	
	//stop name
	private String stopName;
	
	//the id that the stop belongs to
//	private String tripID;
	
	//the arrival time;
	private ArrayList<StopTrips> arrivalTime;


	
	/*
		  	
		0 - Regularly scheduled pickup
		1 - No pickup available
		2 - Must phone agency to arrange pickup
		3 - Must coordinate with driver to arrange pickup
	 */
	private String pickupType;
	
	//CONSTRUCTORS
	public Stop(CyroidLocation Location, String StopID, String StopName, String TripID, ArrayList<StopTrips> ArrivalTime,String PickUpType){
		location = Location;
		stopID = StopID;
		stopName = StopName;
		//tripID = TripID;
		arrivalTime = ArrivalTime;
		pickupType = PickUpType;
		arrivalTime = new ArrayList<StopTrips>();
	}
	
	public Stop() {
		location = new CyroidLocation();
		stopID = "";
		stopName = "";
		//tripID = "";
		arrivalTime = new ArrayList<StopTrips>();
	}
	
	public Stop(CyroidLocation Location, String StopID, String StopName){
		location = Location;
		stopID = StopID;
		stopName = StopName;
		arrivalTime = new ArrayList<StopTrips>();

	}
	
	public Stop(String StopID, String TripID){
		
		stopID = StopID;
		//tripID = TripID;
		
		arrivalTime = new ArrayList<StopTrips>();

	}
	
	public Stop(String StopID){
		stopID = StopID;
		arrivalTime = new ArrayList<StopTrips>();
	}
	
	
	//SETTERS AND GETTERS
	public void setLocation(CyroidLocation location) {
		this.location = location;
	}

	public CyroidLocation getLocation() {
		return location;
	}

	public void setStopID(String stopID) {
		this.stopID = stopID;
	}

	public String getStopID() {
		return stopID;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public String getStopName() {
		return stopName;
	}


	public void setArrivalTime(ArrayList<StopTrips> arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public ArrayList<StopTrips> getStopTrips() {
		return arrivalTime;
	}

	/*
	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getTripID() {
		return tripID;
	}
	*/

	public void setPickupType(String pickupType) {
		this.pickupType = pickupType;
	}

	public String getPickupType() {
		return pickupType;
	}


	
	
}
