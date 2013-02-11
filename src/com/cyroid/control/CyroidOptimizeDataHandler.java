package com.cyroid.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Scanner;

import android.R.raw;
import android.os.Environment;
import android.util.Log;

import com.cyroid.data.CyroidData;
import com.cyroid.data.CyroidLocation;
import com.cyroid.data.Route;
import com.cyroid.data.RunDay;
import com.cyroid.data.Stop;
import com.cyroid.data.StopTrips;
import com.cyroid.data.Trip;

public class CyroidOptimizeDataHandler {

	public static void outputOptimizeFile(CyroidData data) {

		try {
			
			File cyroidDir = new File(Environment.getExternalStorageDirectory()
					.toString() + "/cyroid");

			if (!cyroidDir.exists())
				cyroidDir.mkdir();

			
			
			PrintWriter writer = new PrintWriter(new File(cyroidDir.getAbsolutePath()+"/CyroidOptFile.xml"));
			writer.println("<Routes>");
			for (String key : data.getRoutes().keySet()) {
				Route r = data.getRoutes().get(key);

				writer.println("<Route>");
				writer.println("<RouteID>_" + r.getRouteID() + "_</RouteID>");
				writer.println("<RouteLongName>_" + r.getLong_Route_Name()
						+ "_</RouteLongName>");
				writer.println("<RouteShortName>_" + r.getShort_Route_Name()
						+ "_</RouteShortName>");
				writer.println("<RouteColor>_" + r.getRouteColor()
						+ "_</RouteColor>");
				writer.println("<RouteType>_" + r.getRouteType()
						+ "_</RouteType>");
				writer.println("<RouteTrips>");

				for (String tripKey : r.getTrips().keySet()) {
					Trip trip = r.getTrips().get(tripKey);
					writer.println("<Trip>");
					writer.println("<TripRouteID>_" + trip.getRouteID()
							+ "_</TripRouteID>");
					writer.println("<TripServiceID>_" + trip.getService_ID()
							+ "_</TripServiceID>");
					writer.println("<TripHeadSign>_" + trip.getTrip_HeadSign()
							+ "_</TripHeadSign>");
					writer.println("<TripID>_" + trip.getTripID()
							+ "_</TripID>");
					writer.println("</Trip>");
				}

				writer.println("</RouteTrips>");

				writer.println("<Stops>");

				for (String stopKey : r.getStops().keySet()) {
					writer.println("<Stop>");
					Stop s = r.getStops().get(stopKey);
					writer.println("<StopLocation>_" + s.getLocation().getLat()
							+ "," + s.getLocation().getLon()
							+ "_</StopLocation>");
					writer.println("<StopID>_" + s.getStopID() + "_</StopID>");
					writer.println("<StopType>_" + s.getPickupType()
							+ "_</StopType>");
					writer.println("<StopName>_" + s.getStopName()
							+ "_</StopName>");

					writer.println("<StopTrips>");
					for (StopTrips sT : s.getStopTrips()) {
						writer.println("<StopTrip>");
						writer.println("<StopTripID>_" + sT.getTripID()
								+ "_</StopTripID>");
						writer.println("<StopTripTime>_"
								+ sT.getTime().get(Calendar.HOUR_OF_DAY) + ","
								+ sT.getTime().get(Calendar.MINUTE) + ","
								+ sT.getTime().get(Calendar.SECOND)
								+ "_</StopTripTime>");
						writer.println("</StopTrip>");
					}

					writer.println("</StopTrips>");

					writer.println("</Stop>");
				}
				writer.println("</Stops>");
				writer.println("</Route>");
			}

			writer.println("</Routes>");

			writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void parseOptimizeFile(CyroidData data) {

		try {
			//Scanner input = new Scanner(new FileInputStream(new File(
				//	"data/data/com.cyroid/CyroidOptFile.xml")));
			
			File cyroidDir = new File(Environment.getExternalStorageDirectory()
					.toString() + "/cyroid");

			if (!cyroidDir.exists())
				cyroidDir.mkdir();

			
			ArrayList<StopTrips> stopTrips ;
			StopTrips stopTrip;
			Stop stop ;
			Calendar time ;
			int hours;
			int mins;
			int secs;
			
			FileReader fReader = new FileReader(new File(cyroidDir.getAbsolutePath()+"/CyroidOptFile.xml"));
			
			Log.v("CYROIDOPTFILE.XML EXISTS",""+CyroidOptFileExist());
			
			BufferedReader input = new BufferedReader(fReader);
			String line;
			while ((line = input.readLine()) != null) {
	
				if (line.equals("<Routes>")) {
					
					
					while ((line = input.readLine()) != null && !line.contains("</Routes>")) {
						Route r;
						if (line.contains("<Route>")) {
							
							 r = new Route();
						
							
							while ( ((line = input.readLine()) != null) && (!line.contains("</Route>"))) {
								
								if (line.contains("<RouteID>")) {
									//set route id
									String routeID = line.split("_")[1];
									r.setRouteID(routeID);
									
								}					
								else if (line.contains("<RouteLongName>")) {
								
									
									String routeLongName = line.split("_")[1];
									r.setLong_Route_Name(routeLongName);
									
								}
								else if (line.contains("<RouteShortName>")) {
									String routeShortName = line.split("_")[1];
									r.setShort_Route_Name(routeShortName);
								}
								else if (line.contains("<RouteColor>")) {
									String routeColor = line.split("_")[1];
									r.setRouteColor(routeColor);
								}
								else if (line.contains("<RouteType>")) {
									int routeType = Integer.parseInt(line.split("_")[1]);
									r.setRouteType(routeType);
								}
								
								else if (line.contains("<RouteTrips>")) {
									
									
									while ( (line = input.readLine()) != null
											&& !line.contains("</RouteTrips>")) {
										
										
										if (line.contains("<Trip>")){
											Trip trip = new Trip();
											
											while ( (line = input.readLine()) != null && !line.contains("</Trip>")) {
												
												
												if (line.contains("<TripRouteID>")){
													String tripRouteID =  line.split("_")[1];
													trip.setRouteID(tripRouteID);
												}
												else if (line.contains("<TripServiceID>")){
													String tripServiceID = line.split("_")[1];
													trip.setService_ID(tripServiceID);
												}
												else if (line.contains("<TripHeadSign>")){
													String tripHeadSign = line.split("_")[1];
													trip.setTrip_HeadSign(tripHeadSign);
												}
												else if (line.contains("<TripID>")){
													String tripID = line.split("_")[1];
													trip.setTripID(tripID);
												}
												
											}//while (input.hasNextLine()&& !(line5 = input.nextLine()).equals("</Trip>"))
											r.getTrips().put(trip.getTripID(), trip);
										}//if (line4.contains("<Trip>"))
								
						
										
									}//input.hasNextLine()&&!(line2 = input.nextLine()).equals("</RoutesTrips>")
									
								}//if (line3.equals("<RouteTrips>")) 
								
								if (line.contains("<Stops>")){
									LinkedHashMap<String,Stop> stops = new LinkedHashMap<String,Stop>();
									
									while ( (line = input.readLine()) != null && !line.contains("</Stops>")){
										
										if (line.contains("<Stop>")){
											stop = new Stop();
											
											while ( (line = input.readLine()) != null && !line.contains("</Stop>")){
												if (line.contains("<StopLocation>")){
													String[] latLonSplit = line.split("_");
													String[] latLon = latLonSplit[1].split(",");
													CyroidLocation locat = new CyroidLocation(Double.parseDouble(latLon[0]),Double.parseDouble(latLon[1]));
													stop.setLocation(locat);
												}
												else if (line.contains("<StopID>")){
													String stopID = line.split("_")[1];
													stop.setStopID(stopID);
												}
												else if (line.contains("<StopType>")){
													String stopType = line.split("_")[1];
													stop.setPickupType(stopType);
												}
												else if (line.contains("<StopName>")){
													String stopName = line.split("_")[1];
													stop.setStopName(stopName);
												}
												else if (line.contains("<StopTrips>")){
													 stopTrips = new ArrayList<StopTrips>();
												
													while ( (line = input.readLine()) != null && !line.contains("</StopTrips>")){
													
														if (line.contains("<StopTrip>")){
															 stopTrip = new StopTrips();
															
															
															while ( (line = input.readLine()) != null && !line.contains("</StopTrip>")){
																
																if (line.contains("<StopTripID>")){
																	String stopTripID = line.split("_")[1];
																	stopTrip.setTripID(stopTripID);
																}
																else if (line.contains("<StopTripTime>")){
																	time = Calendar.getInstance();
																	String timeString[] = line.split("_");
																	String timeSplit[] = timeString[1].split(",");
																	hours = Integer.parseInt(timeSplit[0]);
																	mins = Integer.parseInt(timeSplit[1]);
																	secs = Integer.parseInt(timeSplit[2]);
																	time.set(Calendar.HOUR_OF_DAY, hours);
																	time.set(Calendar.MINUTE,mins);
																	time.set(Calendar.SECOND, secs);
																	stopTrip.setTime(time);
																}
															}//while (input.hasNextLine()&& !(line8 = input.nextLine()).equals("</StopTrip>"))
															
															stopTrips.add(stopTrip);
														}//if (line8.equals("<StopTrip>"))
										
													}//while (input.hasNextLine()&& !(line8 = input.nextLine()).equals("</StopTrips>"))
													
													stop.setArrivalTime(stopTrips);
												}//else if (line7.contains("<StopTrips> "))
												
												
												
											}//while (input.hasNextLine()&& !(line7 = input.nextLine()).equals("</Stop>"))
											
											stops.put(stop.getStopID(), stop);
										}//if (line6.equals("<Stop>"))
										
										
									}//while (input.hasNextLine()&& !(line6 = input.nextLine()).equals("</Stops>"))
									r.setStops(stops);
								}//if (line3.equals("<Stops>"))
								
						
							
							}// input.hasNextLine() && !(line3 =input.nextLine()).equals("</Route>")
							data.getRoutes().put(r.getRouteID(),r);	
							
						}//route
						
					}// input.hasNextLine() && !(line2 =
						// input.nextLine()).equals("</Routes>")
				
				}

			}
			System.out.println("Size : " +data.getRoutes().size());
			input.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e ){
			e.printStackTrace();
		}


	}

	public static boolean CyroidOptFileExist() {
		File cyroidDir = new File(Environment.getExternalStorageDirectory()
				.toString() + "/cyroid");

		if (!cyroidDir.exists())
			cyroidDir.mkdir();

		return new File(cyroidDir.getAbsolutePath()+"/CyroidOptFile.xml").exists();
	}

}
