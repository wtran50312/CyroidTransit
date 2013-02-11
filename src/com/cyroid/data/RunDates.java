package com.cyroid.data;

import java.util.LinkedHashMap;

/**
 * RunDates is a class manager that handles the getting / setting of run dates data 
 * through a linked hashmap of Strings to RunDays. 
 * String represents bus route id (???)
 * RunDay represents the week running days (???)
 * @author Van Nguyen
 */
public class RunDates  {

	
	/**
	 * hash map of run dates
	 */
	private LinkedHashMap<String, RunDay> runDates; 
	
	/**
	 * Construction
	 */
	public RunDates(){
		runDates = new LinkedHashMap<String,RunDay>();
	}
	
	/**
	 * Setter and Getter
	 */
	
	/**
	 * Set the rundates. Overwrites the current rundates.
	 * @param runDates
	 */
	public void setRunDates(LinkedHashMap<String, RunDay> runDates){
		this.runDates = runDates;
	}
	
	/**
	 * Gets the run dates linked hash map.
	 * @return the run dates.
	 */
	public LinkedHashMap<String, RunDay> getRunDates(){
		return runDates;
	}
}
