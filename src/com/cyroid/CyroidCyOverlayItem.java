package com.cyroid;


import com.cyroid.data.CyroidLocation;
import com.cyroid.data.Stop;
import com.google.android.maps.OverlayItem;

/**
 * 
 * @author William
 * This class will be graphical represenation
 * for each stop.
 *
 */
public class CyroidCyOverlayItem extends OverlayItem {

	
	private CharSequence[] charSeq; //Char sequence - can be used to store times
	
	private boolean isCurrentPosition = false;//used to check if this is the stop or the user location
	
	private CyroidLocation myLocation;
	
	private Stop stop;
	
	/**
	 * Constructor for CyOverlayItem 
	 * @param point - latitude and longitude of each stop
	 * @param title - the name of the stop
	 * @param snippet - not used
	 */
	public CyroidCyOverlayItem(CyroidLocation point, String title, String snippet) {
		super(point.getGeoLocation(), title, snippet);
		setMyLocation(point);
	}
	
	/**
	 * Constructor for CyOverlayItem
	 * @param point - latitude and longitude of each stop
	 * @param title - name of the stop
	 * @param snippet - not used
	 * @param StopNames - initialize all the stop names
	 */
	public CyroidCyOverlayItem(Stop stop ,String snippet){
		super(stop.getLocation().getGeoLocation(),stop.getStopName(),snippet);
		this.stop = stop;
	}
	
	/**
	 * Used to set whether the current CyOverlayItem is the user graphical
	 * represenation or not
	 * @param set - true if is, false if it is a stop
	 */
	public void setCurrentPosition(boolean set){
		isCurrentPosition = set;
	}
	
	/**
	 * Returns whether a stop is a bus stop
	 * or a user location
	 * @return isCurrentPosition
	 */
	public boolean isCurrentPosition(){
		return isCurrentPosition;
	}
	
	
	/**
	 * Returns the sequence of times (Not Used)
	 * stopNames() replaces this method
	 * @return charSeq
	 */
	public CharSequence[] getCharItem(){
		return charSeq;
	}
	
	/**
	 * Sets the sequence of times (Not Used)
	 * 
	 * @param CharSeq the sequence to be set to
	 */
	public void setCharSeq(CharSequence[] CharSeq){
		charSeq = CharSeq;
	}
	
	
	public void setStop(Stop stop) {
		this.stop = stop;
	}

	public Stop getStop() {
		return stop;
	}

	public void setMyLocation(CyroidLocation myLocation) {
		this.myLocation = myLocation;
	}

	public CyroidLocation getMyLocation() {
		return myLocation;
	}
	
	
	
	
}
