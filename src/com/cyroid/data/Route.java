package com.cyroid.data;


import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 
 * @author William Tran
 * This class represents a route of a bus stop which will contain multiple trips
 * 
 *
 */
public class Route {
	
	

	//HashMap of trips - KEY is tripID
	private LinkedHashMap<String, Trip> trips;
	
	//long route name
	private String long_Route_Name;
	
	//short route name
	private String short_Route_Name;
	
	/* 
	 	0 - Tram, Streetcar, Light rail. Any light rail or street level system within a metropolitan area.
		1 - Subway, Metro. Any underground rail system within a metropolitan area.
		2 - Rail. Used for intercity or long-distance travel.
		3 - Bus. Used for short- and long-distance bus routes.
		4 - Ferry. Used for short- and long-distance boat service.
		5 - Cable car. Used for street-level cable cars where the cable runs beneath the car.
		6 - Gondola, Suspended cable car. Typically used for aerial cable cars where the car is suspended from the cable.
		7 - Funicular. Any rail system designed for steep inclines
	 */
	private int routeType;
	
	//color for route
	private String routeColor;
	
	//route id UNIQUE
	private String routeID;
	
	private LinkedHashMap<String, Stop> stops;

	//CONSTRUCTORS
	public Route( String Long_Route_Name, String Short_Route_Name, int RouteType, String RouteColor,String RouteID){
		trips = new LinkedHashMap<String,Trip>();
		long_Route_Name = Long_Route_Name;
		short_Route_Name = Short_Route_Name;
		routeType = RouteType;
		routeColor = RouteColor;
		routeID = RouteID;
		setStops(new  LinkedHashMap<String,Stop>());
	}
	
	public Route(){
		trips = new LinkedHashMap<String,Trip>();
		long_Route_Name = "";
		short_Route_Name = "";
		routeType = -1;
		routeColor = "";
		routeID = "";
		stops = new  LinkedHashMap<String,Stop>();
	}
	
	
	//UTIL METHODS
	public boolean containsTrip(String TripID){
		boolean hasTrip = false;
		for (String tripid : trips.keySet()){
			if (tripid.equals(TripID)){
				hasTrip = true;
			}
			
		}
		
		return hasTrip;
	}
	
	//GETTER AND SETTERS
	public void setTrips(LinkedHashMap<String, Trip> trips) {
		this.trips = trips;
	}

	public HashMap<String, Trip> getTrips() {
		return trips;
	}

	public void setLong_Route_Name(String long_Route_Name) {
		this.long_Route_Name = long_Route_Name;
	}

	public String getLong_Route_Name() {
		return long_Route_Name;
	}

	public void setShort_Route_Name(String short_Route_Name) {
		this.short_Route_Name = short_Route_Name;
	}

	public String getShort_Route_Name() {
		return short_Route_Name;
	}

	public void setRouteType(int routeType) {
		this.routeType = routeType;
	}

	public int getRouteType() {
		return routeType;
	}

	public void setRouteColor(String routeColor) {
		this.routeColor = routeColor;
	}

	public String getRouteColor() {
		return routeColor;
	}

	public void setRouteID(String routeID) {
		this.routeID = routeID;
	}

	public String getRouteID() {
		return routeID;
	}

	public void setStops(LinkedHashMap<String, Stop> stops) {
		this.stops = stops;
	}

	public LinkedHashMap<String, Stop> getStops() {
		return stops;
	}

	


}
