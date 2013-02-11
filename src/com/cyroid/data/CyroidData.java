package com.cyroid.data;

import java.util.LinkedHashMap;

/**
 * 
 * @author William Tran
 * 
 * This class holds all the data cyroid will need to operate
 * Routes, Agency, CyroidConfig, ExceptionDates, RunDates
 *
 */
public class CyroidData {
	
	//All the routes on bus
	private LinkedHashMap<String,Route> routes;
	
	//Agency Info
	private Agency agency;
	
	//Config Info
	private CyroidConfig config;
	
	//Exception dates
	private ExceptionDates exceptionDates;
	
	//
	private RunDates datesRunning;

	//CONSTRUCTORS
	public CyroidData() {
		setRoutes(new LinkedHashMap<String,Route>());
		agency = new Agency();
		config = new CyroidConfig();
		exceptionDates = new ExceptionDates();
		datesRunning = new RunDates(); 
		
	}

	//GETTER AND SETTERS
	

	public void setAgency(Agency agency) {
		this.agency = agency;
	}

	public Agency getAgency() {
		return agency;
	}

	public void setConfig(CyroidConfig config) {
		this.config = config;
	}

	public CyroidConfig getConfig() {
		return config;
	}

	public void setExceptionDates(ExceptionDates exceptionDates) {
		this.exceptionDates = exceptionDates;
	}

	public ExceptionDates getExceptionDates() {
		return exceptionDates;
	}

	public void setDatesRunning(RunDates datesRunning) {
		this.datesRunning = datesRunning;
	}

	public RunDates getDatesRunning() {
		return datesRunning;
	}

	public void setRoutes(LinkedHashMap<String,Route> routes) {
		this.routes = routes;
	}

	public LinkedHashMap<String,Route> getRoutes() {
		return routes;
	}
}
