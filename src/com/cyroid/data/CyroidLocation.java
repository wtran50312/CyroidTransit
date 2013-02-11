package com.cyroid.data;



import com.google.android.maps.GeoPoint;

/**
 * 
 * @author William Tran
 * This class will represent a location
 *
 */
public class CyroidLocation  {
	

	private String description;
	
	private double lat;
	
	private double lon;

	
	//Constructor


	
	
	public CyroidLocation(double lat, double lon) {
		setDescription("");
		this.setLat(lat);
		this.setLon(lon);
	}


	public CyroidLocation(double d, double e, String string) {
		setDescription(string);
		this.setLat(d);
		this.setLon(e);
	}



	public CyroidLocation() {
		// TODO Auto-generated constructor stub
	}


	//Geo Location
	public GeoPoint getGeoLocation(){
	 return new GeoPoint((int)( getLat()*1e6),(int)(getLon() *1e6));
	}
	
	public CyroidLocation getCyroidLocation(GeoPoint geo){
		return new CyroidLocation( (double) geo.getLatitudeE6()/1E6, (double) geo.getLongitudeE6()/1E6);
	}
	//SETTER AND GETTER METHODS
	
	public double getDistanceInFeet( CyroidLocation Location2){
		
		double pk = (double) (180/3.14169);
		
		double a1 = getLat() / pk;
		  double a2 = getLon() / pk;
		  double b1 = Location2.getLat() / pk;
		  double b2 = Location2.getLon() / pk;

		  double t1 = Math.cos(a1)* Math.cos(a2)*
		     Math.cos(b1)*Math.cos(b2);
		  double t2 = Math.cos(a1)*Math.sin(a2)*
		     Math.cos(b1)*Math.sin(b2);
		  double t3 = Math.sin(a1)*Math.sin(b1);
		  double tt = Math.acos(t1 + t2 + t3);
		 //its in meters right now
		  double feet = 6366000*tt * 3.2808399;
		  
		  return feet ;
	}
	
	public void setLatLon(double Lat, double Lon){
		setLat(Lat);
		setLon(Lon);
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}
	
	
	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLon() {
		return lon;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getDescription() {
		return description;
	}
	
	
	
	
	
	
}
