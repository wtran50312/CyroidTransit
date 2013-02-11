package com.cyroid.control;

import com.cyroid.data.CyroidLocation;

import android.location.Location;

import android.location.LocationListener;
import android.os.Bundle;

/**
 * Location listener for GPS
 * @author William
 *
 */
public class GPSLocationListener implements LocationListener {
	
	/**
	 * The location that the GPS listens to for GPS updates.
	 */
	private CyroidLocation myLocation;
	
	/**
	 * Default Constructor
	 * @param MyLocation the LocationDevice to listen for GPS
	 */
	public GPSLocationListener(CyroidLocation MyLocation) {
		myLocation = MyLocation;
	}
	
	
	/**
	 * 
	 */
	public void onLocationChanged(Location arg) {
		myLocation.setLatLon(arg.getLatitude(), arg.getLongitude());

	}

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}// end of inner class
