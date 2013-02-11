

package com.cyroid.data;

import java.io.Serializable;

/**
 * This class represents a bus agency
 * @author William Tran
 *
 */
public class Agency implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//phone number
	private String phone;	

	//website
	private String website;
	
	//timezone
	private String timeZone;
	
	//language
	private String lang;
	
	//the name of the agency
	private String agencyID;
	
	

	
	//CONSTRUCTORS
	public Agency(String Phone, String Website,String TimeZone, String Lang, String AgencyID){
		phone = Phone;
		website = Website;
		timeZone = TimeZone;
		lang = Lang;
		agencyID = AgencyID;
	}
	
	public Agency(){
		phone = "";
		website= "";
		timeZone = "";
		lang = "";
		agencyID = "";
	}
	
	
	//SETTER AND GETTER METHODS
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWebsite() {
		return website;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setAgencyID(String agencyID) {
		this.agencyID = agencyID;
	}

	public String getAgencyID() {
		return agencyID;
	}
	
	
	
}
