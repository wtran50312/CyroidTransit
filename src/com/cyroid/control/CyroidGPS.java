package com.cyroid.control;

import com.cyroid.data.CyroidLocation;

import android.app.Activity;
import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.location.LocationManager;
import android.os.IBinder;


/**
 * This class will initialize the GPS and provide methods to get user location
 * along with turning GPS on/off
 * 
 * @author William
 * 
 */
public class CyroidGPS extends Service {

	/**
	 * Location Listener for GPS
	 */
	private GPSLocationListener listLoc;

	/**
	 * Keeps track of GPS status (on/off)
	 */
	private boolean on = false;

	/**
	 * The Activity that uses GPS
	 */
	private Activity act;

	/**
	 * Location Manager retrieves data from actual GPS device
	 */
	private LocationManager myLocationManager = null;

	/**
	 * Keeps track of location
	 */
	private CyroidLocation myLocation = null;
	
	 String provider = "";
	
	/**
	 * Constructor for GPS
	 * Will initialize GPS location to -1000.0, -1000.0
	 * and initialize a GPS Location Listener for the GPS
	 * @param Act - the activity that uses the gps
	 */
	public CyroidGPS(Activity Act){
		act = Act;
		myLocation = new CyroidLocation(-1000.0, -1000.0, "Current Location");

		listLoc = new GPSLocationListener(myLocation);
		
		
		this.myLocationManager = (LocationManager) act
				.getSystemService(Context.LOCATION_SERVICE);
/*
		myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				500, 25, listLoc);
		on = true;
		*/
		/*
		 Criteria criteria = new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_FINE);
		    
		    
		    myLocationManager = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
		    provider = myLocationManager.getBestProvider(criteria, true);
		    
		    if(provider == null){
		        provider = LocationManager.GPS_PROVIDER;
		    }
		    if(!myLocationManager.isProviderEnabled(provider)){
		        myLocationManager.setTestProviderEnabled(provider, true);
		    }
		  
		    
		    myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					500, 25, listLoc);
			on = true;
		      */
		
		
	}

	/**
	 * Sets gps to on or off
	 * 
	 * @param set
	 *            - true to set on - false to set off
	 */
	public void setEnable(boolean set) {
		if (on != set){
			on = set;
			if (!on) {
				myLocationManager.removeUpdates(listLoc);
				
			} else if (on) {
				
				myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,25, listLoc);
			}
		
		}
	}// setEnable

	/**
	 * Returns the current location of GPS returns -1000 for latitude and
	 * longitude if GPS has not received a coordinate If it has, it will return
	 * actual coordinates
	 * 
	 * @return the current location of the device
	 */
	public CyroidLocation getGPSLocation() {
		if ((myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)) != null) {
			myLocation.setLatLon(myLocationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER)
					.getLatitude(), myLocationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER).getLongitude());
		}
		return myLocation;
	}
	
	/**
	 * Sets location for GPS
	 * 
	 * @param Latitude the value to set the latitude to
	 * @param Longitude the value to set the longitude to
	 */
	public void setLocation(double Latitude, double Longitude){
		myLocation.setLatLon(Latitude, Longitude);
		
	}

	/**
	 * Returns the location manager that manages the GPS
	 * 
	 * @return myLocationManager
	 */
	public LocationManager getLocationManager() {
		return myLocationManager;
	}
	
	public boolean getGPSStatus(){
		return on;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	




}// end of class