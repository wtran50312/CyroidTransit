package com.cyroid.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;

import android.util.Log;

import com.cyroid.data.Agency;
import com.cyroid.data.CyroidData;
import com.cyroid.data.CyroidLocation;
import com.cyroid.data.ExceptionDate;
import com.cyroid.data.Route;
import com.cyroid.data.RunDay;
import com.cyroid.data.Stop;
import com.cyroid.data.StopTrips;
import com.cyroid.data.Trip;

/**
 * 
 * @author William Tran Class to parse text files and create tables out of them.
 * 
 */
public class GTFSParser {

	
	
	final static String path = "data/data/com.cyroid/gtfs/";

	final String files[] = { "agency.txt", "calendar.txt",
			"calendar_dates.txt", "routes.txt", "stop_times.txt", "stops.txt",
			"trips.txt" };

	static BufferedReader input;
	
	static FileReader fReader;



	// CONSTRUCTORS
	public GTFSParser() {

	}

	/**
	 * Create all required tables
	 */
	public static void parseAllFiles(CyroidData data) {
		parseAgency(data);
		Log.v("DONE", "DONE WITH AGENCY");
		parseCalendar(data);
		Log.v("DONE", "DONE WITH CALENDAR");
		parseCalendarDates(data);
		Log.v("DONE", "DONE WITH DATES");
		parseRoutes(data);
		Log.v("DONE", "DONE WITH ROUTES");
		parseTrips(data);
		Log.v("DONE", "DONE WITH TRIPS");
		long start = System.currentTimeMillis();
		parseStopsAndTimes(data);
		Log.v("DONE", "DONE WITH Stops");
		long end = System.currentTimeMillis();
		System.out.println("Took:  "+(end-start));
	}
	
	public static void parseSecondTime(CyroidData data){
		
		parseAgency(data);
		Log.v("DONE", "DONE WITH AGENCY");
		parseCalendar(data);
		Log.v("DONE", "DONE WITH CALENDAR");
		parseCalendarDates(data);
		Log.v("DONE", "DONE WITH DATES");
		CyroidOptimizeDataHandler.parseOptimizeFile(data);
	}

	private static void parseAgency(CyroidData data) {

		File agencyFile = new File(path + "agency.txt");

		try {
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(agencyFile)));
					*/
			fReader = new FileReader(agencyFile);
			input = new BufferedReader(fReader);

			// move off first line
			input.readLine();

			// grab all csv
			String line[] = input.readLine().split(",", -1);
			
			//fReader.close();
			//input.close();

			// String Phone, String Website,String TimeZone, String Lang, String
			// AgencyID
			Agency agency = new Agency(line[0], line[2],
					line[5], line[6], line[3]);
			// close scanner

			data.setAgency(agency);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

	}



	/**
	 * Parses calendar_dates.txt and creates CalendarDates table
	 */
	private static void parseCalendarDates(CyroidData data) {

		File calendarDatesFile = new File(path + "calendar_dates.txt");

		try {
			
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(calendarDatesFile)));
					*/
			fReader = new FileReader(calendarDatesFile);
			input = new BufferedReader(fReader);

			// move off first line
			input.readLine();

			String readLine;
			String line[];
			
			ExceptionDate exceptionDate;
			
			boolean remove;
			while ((readLine = input.readLine()) != null) {

				line = readLine.split(",", -1);
				Calendar date = Calendar.getInstance();
				// parse out line[1] and use itinitialize a calendar object
				date.set(Integer.parseInt(line[1].substring(0, 4)),
						Integer.parseInt(line[1].substring(4, 6)) - 1,
						Integer.parseInt(line[1].substring(6, 8)));

				if (Integer.parseInt(line[2].trim()) == 1) {
					remove = false;
				} else {
					remove = true;
				}

				// created exception date
				exceptionDate = new ExceptionDate(line[0], date,
						remove);

				// put into list of exceptionDates
				data.getExceptionDates().getExceptionDates().add(exceptionDate);
			
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void parseCalendar(CyroidData data) {

		File calendarFile = new File(path + "calendar.txt");

		try {
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(calendarFile)));
					*/
			fReader = new FileReader(calendarFile);
			input = new BufferedReader(fReader);
			
			// move off first line
			input.readLine();

			String readLine;
			String line[];
			
			
		
			RunDay dateRunning;
			
			while ((readLine = input.readLine()) != null) {

				line = readLine.split(",", -1);
				
				Calendar dateStart = Calendar.getInstance();
				Calendar dateEnd = Calendar.getInstance();
				


				// parse out line[1] and use initialize a calendar object
				dateStart.set(Integer.parseInt(line[1].substring(0, 4)),
						Integer.parseInt(line[1].substring(4, 6)) - 1,
						Integer.parseInt(line[1].substring(6, 8)));

				dateEnd.set(Integer.parseInt(line[2].substring(0, 4)),
						Integer.parseInt(line[2].substring(4, 6)) - 1,
						Integer.parseInt(line[2].substring(6, 8)));
				boolean week[] = new boolean[7];
				// initialize days of weeks running
				for (int i = 3; i < line.length; i++) {
					if (Integer.parseInt(line[i]) == 1) {
						week[i - 3] = true;
					} else {
						week[i - 3] = false;
					}

				}

				// String ServiceID, Calendar StartDate, Calendar EndDate,
				// boolean [] Week
			 dateRunning = new RunDay(line[0], dateStart, dateEnd,
						week);

				// put into list of running dates
				data.getDatesRunning().getRunDates().put(line[0], dateRunning);
				
			

			}



		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Parses routes.txt
	 */
	private static void parseRoutes(CyroidData data) {

		try {
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(path + "routes.txt"))));
					*/
			fReader = new FileReader(new File(path + "routes.txt"));
			input = new BufferedReader(fReader);

			// move off first line
			input.readLine();

			String line;
			String csvLine[];

			
			while ((line = input.readLine()) != null) {

				// grab all csv
				csvLine = line.split(",", -1);

				// String Long_Route_Name, String Short_Route_Name, int
				// RouteType, String RouteColor,String RouteID

				Route route = new Route(csvLine[0], csvLine[8],
						Integer.parseInt(csvLine[1]), csvLine[3],
						csvLine[5]);

				// put into routes
				data.getRoutes().put(csvLine[5], route);

			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Parses stops.txt
	 */
	private static void parseStopsAndTimes(CyroidData data) {

		File stopsFile = new File(path + "stops.txt");
		File stopTimesFile = new File(path + "stop_times.txt");
		try {
			
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(stopsFile)));
					*/
			
			fReader = new FileReader(stopsFile);
			input = new BufferedReader(fReader);
			

			input.readLine();

			String line;
			String csvLine[];

			// create necesssary objects and variables
			Stop stop = null;
			CyroidLocation location;
			double lat;
			double lon;
			// ContentValues values;
			while ((line = input.readLine()) != null) {

				csvLine = line.split(",", -1);

				// get all the data out
				lat = Double.parseDouble(csvLine[0]);
				lon = Double.parseDouble(csvLine[2]);

				location = new CyroidLocation(lat, lon);

				// add into stops into each route object's stops
				
				for (String routeKey : data.getRoutes().keySet()) {
					stop = new Stop(location, csvLine[3].trim(), csvLine[7].trim());
					
					if (!data.getRoutes().get(routeKey).getStops().containsKey(stop.getStopID())){
						data.getRoutes().get(routeKey).getStops().put(stop.getStopID(), stop);
					}
		
				}
				
				

			}
	
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(stopTimesFile)));
					*/
			//fReader.close();
			//input.close();
			
			
			fReader = new FileReader(stopTimesFile);
			input = new BufferedReader(fReader);
			

			input.readLine();
			
			
			Calendar savedTime = null;
			StopTrips stopTrips;
			String time[];
			String line2;
			String csvLine2[];
			String stopKey;
			String tripID;
			// Stops stops;
			// Stop stop;
			while ((line2 = input.readLine()) != null) {

				csvLine2 = line2.split(",", -1);

				// setup time
				time = csvLine2[1].split(":");
				stopKey = csvLine2[3];
				tripID = csvLine2[0];

				// set time , set tripID, stop sequence,
				// MAKE SURE TO CHECK FOR NO TIMES
				
				if (csvLine2[1].length() > 5) {
					Calendar arrivalTime = Calendar.getInstance();
					arrivalTime.set(Calendar.HOUR_OF_DAY,
							(Integer.parseInt(time[0])%24));
					arrivalTime.set(Calendar.MINUTE, Integer.parseInt(time[1]));
					arrivalTime.set(Calendar.SECOND, Integer.parseInt(time[2]));

					// get saved time
					savedTime = arrivalTime;

					stopTrips = new StopTrips(tripID, arrivalTime);
					
					Route r;
					for (String routeKey : data.getRoutes()
							.keySet()) {
						r = data.getRoutes().get(routeKey);
						//if (r.containsTrip(stopTrips.getTripID())) {
						
							// Log.v("Blue norht check", "added stop trip");
						if (r.getTrips().containsKey(stopTrips.getTripID())){
							data.getRoutes().get(routeKey)
									.getStops().get(stopKey)
									.getStopTrips().add(stopTrips);
							break;
						
						}
					//	r = null;
					}
					
					//stopTrips = null;
				} else {

					stopTrips = new StopTrips(tripID, savedTime);
					
					Route r;
					for (String routeKey : data.getRoutes()
							.keySet()) {
							r = data.getRoutes().get(routeKey);
						if (r.getTrips().containsKey(stopTrips.getTripID())){
							data.getRoutes().get(routeKey)
									.getStops().get(stopKey)
									.getStopTrips().add(stopTrips);
							break;
						}
						//r = null;
						
					}
					
					//stopTrips = null;
				}// arrival time = "";
				

				// tripID

			}// end of reading through stop_times
			fReader.close();
			input.close();
			//stopTimesFile = null;

			// loop through stops and delete any stops that don't have any
			// trips
			
			
			for (String routeKey2 : data.getRoutes().keySet()) {

			
				Iterator<String> iter = data.getRoutes()
						.get(routeKey2).getStops().keySet()
						.iterator();
				Stop s2;
				String stopKey2;
				while (iter.hasNext()) {
					stopKey2 = iter.next();
					s2 = data.getRoutes().get(routeKey2)
							.getStops().get(stopKey2);
					
					if (s2.getStopTrips().size() == 0) {
					//	Log.v("Stop Name : " + s2.getStopName(), "Size : "
						//		+ s2.getStopTrips().size());
						iter.remove();
					}
					//s2 = null;
				}

			}
			

		} catch (IOException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Parses trips.txt and creates Trips table
	 */
	private static void parseTrips(CyroidData data) {

		File tripsFile = new File(path + "trips.txt");

		try {
			
			/*
			input = new BufferedReader(new InputStreamReader(
					new FileInputStream(tripsFile)));
					*/
			fReader = new FileReader(tripsFile);
			input = new BufferedReader(fReader);

			// move off first line
			input.readLine();

			String csvLine[];
			String line;

			Trip trip;

			while ((line = input.readLine()) != null) {

				csvLine = line.split(",", -1);

				// Create trip object
				// String RouteID, String Trip_HeadSign,String ServiceID,String
				// TripID
				trip = new Trip(csvLine[1], csvLine[3],
						csvLine[5], csvLine[6]);
				
				//Log.v("Route ID "+ trip.getRouteID(),"tripID : "+trip.getTripID());
				// add trip object to correct route
				data.getRoutes().get(trip.getRouteID()).getTrips()
						.put(trip.getTripID(), trip);
				
				//trip = null;

			}

			// close scanner
			fReader.close();
			input.close();
			//tripsFile = null;

			// delete gtfs file
			// tripsFile.delete();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
