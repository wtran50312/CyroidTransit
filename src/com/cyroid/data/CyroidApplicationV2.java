package com.cyroid.data;

import com.cyroid.control.CyroidGPS;

import com.cyroid.control.CyroidEngine;

import android.app.Activity;
import android.app.Application;

public class CyroidApplicationV2 extends Application {

	CyroidEngine engine = new CyroidEngine();
	private static CyroidGPS GPS;
	//private boolean inBrowseRoutes = false;
	
	private boolean createMainMenu = true;
	
	private int mainMenuCounter = 0;
	
	private CyroidLocation myLocat = new CyroidLocation(42.023397,-93.625638,"My Location");
	
	private int selection = 0;
	
	public static final int inRouteSelection = 0;
	public static final int inBrowseRoutes = 1;
	public static final int inMapViewSelection = 2;
	
	public void initEngine(){
		engine = new CyroidEngine();
		
	
	}
	
	
	
	public CyroidEngine getCyroidEngine(){
		return engine;
	}

	/**
	 * Initializes the GPS for the application
	 * @param act Activity needed to initialize the GPS
	 */
	public static void initGPS(Activity act){
		GPS = new CyroidGPS(act);
		
	}
	
	/**
	 * Gets the current GPS of the phone
	 */
	public static CyroidGPS getGPS(){
		return GPS;
	}



	public void setCreateMainMenu(boolean createMainMenu) {
		this.createMainMenu = createMainMenu;
	}



	public boolean isCreateMainMenu() {
		return createMainMenu;
	}



	public void setSelection(int selection) {
		this.selection = selection;
	}



	public int getSelection() {
		return selection;
	}



	public void setMyLocat(CyroidLocation myLocat) {
		this.myLocat = myLocat;
	}



	public CyroidLocation getMyLocat() {
		return myLocat;
	}



	public void setMainMenuCounter(int mainMenuCounter) {
		this.mainMenuCounter = mainMenuCounter;
	}



	public int getMainMenuCounter() {
		return mainMenuCounter;
	}
	
	
}
