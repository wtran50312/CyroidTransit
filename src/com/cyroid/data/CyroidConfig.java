package com.cyroid.data;

import java.util.Calendar;

/**
 * 
 * @author William Tran
 * 
 * Class to hold cyroid config data
 *
 */
public class CyroidConfig  {




	//stores the date the app was last updated
	private Calendar lastUpdated;
	

	/*
	 * Stores the update interval
	 * 0 - 3 months
	 * 1 - 6 months
	 * DEFAULTS TO 1
	 */
	private int updateInterval;
	
	
	private int radius;
	
	
	//CONSTRUCTORS
	public CyroidConfig() {
		lastUpdated = Calendar.getInstance();
		updateInterval = 0;
		radius = 2000;
	}
	
	public CyroidConfig(Calendar LastUpdated, int UpdateInterval, int Radius) {
		lastUpdated = LastUpdated;
		updateInterval = UpdateInterval;
		setRadius(Radius);
	}
	
	
	//GETTER AND SETTERS
	public void setLastUpdated(Calendar lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Calendar getLastUpdated() {
		return lastUpdated;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}
	
	
}
