package com.cyroid.data;

/**
 * 
 * @author William Tran
 * 
 * This class represents multiple stops
 *
 */
public class Trip  {
	
	//The routeID for the trip
	private String routeID;
	
	//The text on the sign
	private String trip_HeadSign;
	
	//The service id
	private String serviceID;
	
	//The trip id UNIQUE
	private String tripID;
	

	
	//CONSTRUCTORS
	public Trip( String RouteID, String Trip_HeadSign,String ServiceID,String TripID){
		routeID = RouteID;
		trip_HeadSign = Trip_HeadSign;
		serviceID = ServiceID;
		tripID = TripID;
		
	}
	
	public Trip(){
		routeID = "";
		trip_HeadSign = "";
		serviceID = "";
		tripID = "";
	}
	
	//GETTER AND SETTERS
	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setTrip_HeadSign(String trip_HeadSign) {
		this.trip_HeadSign = trip_HeadSign;
	}

	public String getTrip_HeadSign() {
		return trip_HeadSign;
	}

	public void setService_ID(String serviceID) {
		this.serviceID = serviceID;
	}

	public String getService_ID() {
		return serviceID;
	}

	public void setTripID(String tripID) {
		this.tripID = tripID;
	}

	public String getTripID() {
		return tripID;
	}

	

}
