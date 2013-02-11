package com.cyroid.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.cyroid.CyroidCyOverLayItems;
import com.cyroid.CyroidCyOverlayItem;
import com.cyroid.CyroidMainMenu;
import com.cyroid.CyroidRouteSelection;
import com.cyroid.CyroidSettingsMenu;
import com.cyroid.R;
import com.cyroid.data.CyroidApplicationV2;
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
 * @author William Tran Class to run cyroid app
 * 
 */
public class CyroidEngine {

	// the main data
	private CyroidData data;

	// checks if in main menu
	private boolean inMainMenu;

	// holds route selected
	private Route routeSelected;

	//
	int nearestTimePos;

	// CONSTRUCTOR
	public CyroidEngine() {
		data = new CyroidData();
		inMainMenu = false;
		setRouteSelected(new Route());

		// initialize gtfsUpdator

	}

	/**
	 * Phase 1: (MAKE SURE TO CREATE MESSAGE IF FIRST TIME) Check if config file
	 * exist if not create it (First time) Since it is the first time parse
	 * config file and update CyroidConfig object Parse text files and create
	 * tables. If it is not the first time then update CYROIDDATA using table.
	 * 
	 */
	public void init(final Activity act) {

		String state = Environment.getExternalStorageState();
		boolean sdPresent = Environment.MEDIA_MOUNTED.equals(state);
		// makes sure sd is present
		if (!sdPresent) {
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setMessage("SD card is required.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									act.finish();

								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}
		// checks to make sure they have internet and first time
		if (!haveNetworkConnection(act) && !ConfigHandler.configFileExists()) {
			Log.v("TAGGGGGGGGGG",
					"!cyApp.getCyroidEngine().haveNetworkConnection(this) && !ConfigHandler.configFileExists()");
			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setMessage(
					"Internet Is Required For First Time Use or Updating")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

									act.finish();

								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			// has internet and first time
		} else if (haveNetworkConnection(act)
				&& !ConfigHandler.configFileExists()) {
			firstUpdateData(act);
			inMainMenu = true;
			Log.v("TAGGGGGG",
					"cyApp.getCyroidEngine().haveNetworkConnection(this)");
		} else if (ConfigHandler.configFileExists()
				&& data.getRoutes().size() == 0) {

			this.ParseGTFSData(act);
			// inMainMenu = true;
		}

	}

	/**
	 * Runs thread if user is getting onto application for the first time
	 * 
	 * @param act
	 */
	public void firstUpdateData(Activity act) {
		new GTFSFirstTimeUpdator(act).execute();
	}

	/**
	 * Runs thread when application needs updating whether manually or
	 * automatically
	 * 
	 * @param act
	 */
	public void updateData(Activity act) {
		new GTFSUpdator(act).execute();
	}

	/**
	 * Way to initialize objects
	 * 
	 * @param act
	 */
	public void ParseGTFSData(Activity act) {
		new GTFSParseData(act).execute();
	}

	public void finish() {

	}

	// METHODS TO BE USED IN GUI
	/**
	 * Method to return all routes
	 */
	public ArrayList<Route> getRouteList() {
		ArrayList<Route> routes = new ArrayList<Route>();
		for (String routeKey : data.getRoutes().keySet()) {
			routes.add(data.getRoutes().get(routeKey));
		}

		Collections.sort(routes, new Comparator<Route>() {

			@Override
			public int compare(Route arg0, Route arg1) {

				if (arg0.getShort_Route_Name().compareTo(
						arg1.getShort_Route_Name()) > 0) {
					return 1;
				} else if (arg0.getShort_Route_Name().compareTo(
						arg1.getShort_Route_Name()) < 0) {
					return -1;
				}

				return 0;
			}

		});

		return routes;

	}

	public ArrayList<Stop> getStopList() {
		ArrayList<Stop> stops = new ArrayList<Stop>();
		for (String stopKey : routeSelected.getStops().keySet()) {
			Stop s = routeSelected.getStops().get(stopKey);
			stops.add(s);
		}

		Collections.sort(stops, new Comparator<Stop>() {

			@Override
			public int compare(Stop arg0, Stop arg1) {

				if (arg0.getStopName().compareTo(arg1.getStopName()) > 0) {
					return 1;
				} else if (arg0.getStopName().compareTo(arg1.getStopName()) < 0) {
					return -1;
				}

				return 0;
			}

		});

		return stops;
	}

	/**
	 * Gets all the times
	 * 
	 * @param s
	 * @return
	 */
	public ArrayList<String> getTimesForStop(Stop s) {

		// FILTER OUT TIMES figure out which are running

		Trip trip;
		RunDay runDay;
		Calendar today = Calendar.getInstance();
		ArrayList<StopTrips> correctStopTrips = new ArrayList<StopTrips>();

		Log.v("Stop trips size in stop ", "Stop trip size : "
				+ s.getStopTrips().size());
		for (StopTrips sT : s.getStopTrips()) {
			trip = getRouteSelected().getTrips().get(sT.getTripID());
			runDay = getData().getDatesRunning().getRunDates()
					.get(trip.getService_ID());
			if (today.after(runDay.getStartDate())
					&& today.before(runDay.getEndDate())) {

				if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
						&& runDay.getWeek()[0]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)
						&& runDay.getWeek()[1]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)
						&& runDay.getWeek()[2]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)
						&& runDay.getWeek()[3]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
						&& runDay.getWeek()[4]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
						&& runDay.getWeek()[5]) {
					correctStopTrips.add(sT);
				} else if ((today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
						&& runDay.getWeek()[6]) {
					correctStopTrips.add(sT);
				}

			}// if in run date
		}// for each stop trips

		Log.v("CorrectStop Trip size: ", "Size is : " + correctStopTrips.size());
		// now loop through and remove / keep exceptions
		Trip trip2;
		Iterator<StopTrips> sT2Iter = correctStopTrips.iterator();
		RunDay runDay2;
		while (sT2Iter.hasNext()) {
			StopTrips sT2 = sT2Iter.next();
			trip2 = getRouteSelected().getTrips().get(sT2.getTripID());
			runDay2 = getData().getDatesRunning().getRunDates()
					.get(trip2.getService_ID());

			for (ExceptionDate e : getData().getExceptionDates()
					.getExceptionDates()) {
				// check serviceID
				if (trip2.getService_ID().equals(e.getServiceID())) {
				//	System.out
						//	.println("my service id " + trip2.getService_ID());
					//System.out.println("exception service id "
						//	+ e.getServiceID());
					// ! excetion type = remove
					//System.out.println("Today Year Month Day "+ today.get(Calendar.YEAR) + " | "+ today.get(Calendar.MONTH )+ " | "+ today.get(Calendar.DAY_OF_WEEK));
					//System.out.println("Exception Year Month Day "+ today.get(Calendar.YEAR) + " | "+ today.get(Calendar.MONTH) + " | "+ today.get(Calendar.DAY_OF_WEEK)));
					if ((today.get(Calendar.YEAR) == e.getDates().get(
							Calendar.YEAR))
							&& (today.get(Calendar.MONTH) == e.getDates().get(
									Calendar.MONTH))
							&& (today.get(Calendar.DAY_OF_MONTH) == e.getDates()
									.get(Calendar.DAY_OF_MONTH))
							&& runDay2.getWeek()[(today
									.get(Calendar.DAY_OF_WEEK) + 5) % 7]) {
						/*
						 * } System.out.println("E calendar year is  "+
						 * e.getDates().get(Calendar.YEAR));
						 * System.out.println("E calendar month is  "+
						 * e.getDates().get(Calendar.MONTH));
						 * System.out.println("E day of week  "+
						 * e.getDates().get(Calendar.DAY_OF_WEEK));
						 * System.out.println("today yar is " +
						 * today.get(Calendar.YEAR) );
						 * System.out.println("today month is "+
						 * today.get(Calendar.MONTH) );
						 * System.out.println("today day is " +
						 * today.get(Calendar.DAY_OF_WEEK));
						 */

						if (e.getRemove()) {
							Log.v("Removing : " + sT2.getTripID(),
									"time "
											+ DateFormat.getTimeInstance()
													.format(sT2.getTime()
															.getTime()));
							if (correctStopTrips.contains(sT2))
								sT2Iter.remove();

						}
					}

				}
			}
		}

		// short correctStopTrips
		Collections.sort(correctStopTrips, new Comparator<StopTrips>() {

			@Override
			public int compare(StopTrips arg0, StopTrips arg1) {
				if (((long) arg0.getTime().getTimeInMillis()) > ((long) arg1
						.getTime().getTimeInMillis())) {
					return 1;
				} else if (((long) arg0.getTime().getTimeInMillis()) < ((long) arg1
						.getTime().getTimeInMillis())) {
					return -1;
				}
				return 0;
			}

		});
		ArrayList<String> toDisplay = null;
		if (correctStopTrips.size() > 0) {
			toDisplay = new ArrayList<String>();

			toDisplay.add(DateFormat.getTimeInstance(DateFormat.SHORT).format(
					correctStopTrips.get(0).getTime().getTime()));
			Calendar timeNow = Calendar.getInstance();

			// gets the nearest position to your stop

			Calendar calendarPrevious = correctStopTrips.get(0).getTime();
			double minDifference = compareDates(timeNow, calendarPrevious);

			System.out.println("min difference : " + minDifference);
			// min difference
			nearestTimePos = 0;
			for (int i = 1; i < correctStopTrips.size(); i++) {

				// toDisplay.add(""+st.getTime().get(Calendar.HOUR)
				// +":"+st.getTime().get(Calendar.MINUTE)+" "+st.getTime().get(Calendar.AM_PM));
				/*
				 * if ((correctStopTrips.get(i).getTime()
				 * .get(Calendar.HOUR_OF_DAY) == previousTime
				 * .get(Calendar.HOUR_OF_DAY)) &&
				 * (correctStopTrips.get(i).getTime() .get(Calendar.MINUTE) ==
				 * previousTime .get(Calendar.MINUTE))) {
				 * correctStopTrips.remove(i);
				 * 
				 * } else {
				 */
				toDisplay.add(DateFormat.getTimeInstance(DateFormat.SHORT)
						.format(correctStopTrips.get(i).getTime().getTime()));

				double difference = compareDates(correctStopTrips.get(i)
						.getTime(), timeNow);

				if (minDifference > difference) {
					System.out.println(difference + " true");

					nearestTimePos = i;
					minDifference = difference;

				}

				// OLD ELSE }
			}
		}// size > 0

		// handle when no stops found
		if (toDisplay == null) {
			toDisplay = new ArrayList<String>();
			toDisplay.add("Not running today.");
		}

		return toDisplay;
	}// end of getCorrectStopTimes

	/**
	 * Returns stops pins for map
	 * 
	 * @param act
	 * @param Radius
	 * @param Location
	 * @return
	 */
	public CyroidCyOverLayItems getAllMapStops(Activity act, int Radius,
			CyroidLocation Location) {
		// create a pin
		Drawable stopPin = act.getResources().getDrawable(
				R.drawable.ic_google_map_pin);
		CyroidCyOverLayItems mapStops = new CyroidCyOverLayItems(stopPin, act,
				false);

		Log.v("SELECTED ROUTE",
				"SELECTED ROUTE ID " + routeSelected.getLong_Route_Name());
		for (String stopID : routeSelected.getStops().keySet()) {
			Stop s = routeSelected.getStops().get(stopID);

			if (s.getLocation().getDistanceInFeet(Location) < Radius) {

				// then addd
				CyroidCyOverlayItem mapStop = new CyroidCyOverlayItem(s, "");
				mapStops.addOverlay(mapStop);

			}

		}

		return mapStops;

	}

	public void viewMaps(final Activity from, final CyroidApplicationV2 cyApp) {

		final CharSequence[] items = { "Main Map", "Route Maps" };
		// Creates a dialog that will have Listview and MapView
		// Depending on which one is selected, it will change to
		// that activity
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setTitle("Choose Map View");
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							dialog.dismiss();

							File pdf = new File("/sdcard/cyroid/AreaBusMap.pdf");
							// File pdf = new File
							// ("Android/data/data/com.cyroid/AreaBusMap.pdf");
							Intent pdfViewerIntent = new Intent(
									Intent.ACTION_VIEW);

							if (pdf.exists()) {
								Log.v("PATH EXISTS", "EXIST EXIST EXIST");
								Uri path = Uri.fromFile(pdf);
								Log.v("PATH TEST", "" + path.getPath());
								pdfViewerIntent.setDataAndType(path,
										"application/pdf");
								pdfViewerIntent
										.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

							}
							try {

								from.startActivity(pdfViewerIntent);

							} catch (ActivityNotFoundException e) {
								Toast.makeText(from,
										"No Application Available to View PDF",
										Toast.LENGTH_LONG).show();
							}

							// CyroidMainMenu.this.startActivity(new
							// Intent(CyroidMainMenu.this,PdfViewer.class));
						} else if (item == 1) {
							cyApp.setSelection(CyroidApplicationV2.inMapViewSelection);
							dialog.dismiss();
							from.startActivity(new Intent(from,
									CyroidRouteSelection.class));

						}
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	// BACK END METHODS
	public void updateGTFS(final Activity act) {

		Unzip unzip = new Unzip();

		try {
			unzip.unzipFileHTTP(
					"http://www.cyride.com/ftp/gtf/current/google_transit.zip",
					act);

		} catch (NullPointerException e) {

			act.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// if (act.getCallingActivity() ){
					if (act instanceof CyroidMainMenu) {
						Log.v("Activity INSTANCE OF",
								"ACTIVITY IS INSTANCE OF CYROIDMAINMENU");

						AlertDialog.Builder builder = new AlertDialog.Builder(
								act);
						builder.setMessage(
								"Internet Is Required For First Time Use or Updating")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												ConfigHandler
														.deleteConfigFile();
												act.finish();

											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}// act instanceof CyroidMainMenu
					else if (act instanceof CyroidSettingsMenu) {
						Log.v("Activity INSTANCE OF",
								"ACTIVITY IS INSTANCE OF CYROIDMAINMENU");

						AlertDialog.Builder builder = new AlertDialog.Builder(
								act);
						builder.setMessage(
								"Internet Is Required For First Time Use or Updating")
								.setCancelable(false)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												act.startActivity(new Intent(
														Settings.ACTION_WIFI_SETTINGS));

											}
										});
						AlertDialog alert = builder.create();
						alert.show();
					}

				}

			});

		}

	}

	public void clearGtfsFiles() {
		if (new File("data/data/com.cyroid/gtfs").exists()) {
			File gtfsFolder[] = new File("data/data/com.cyroid/gtfs")
					.listFiles();

			for (File f : gtfsFolder) {
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}

	/**
	 * CHECK INTERNET CONNECTION
	 * 
	 * @param act
	 * @return
	 */
	public boolean haveNetworkConnection(Activity act) {
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) act
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					HaveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					HaveConnectedMobile = true;
		}
		return HaveConnectedWifi || HaveConnectedMobile;
	}

	/**
	 * CHECKS IF DATA NEEDS UPDATING
	 * 
	 * @param act
	 * @param data
	 */
	public boolean checkRequiredUpdate(final Activity act, CyroidData data,
			GTFSParseData parser) {

		long lastUpdated = data.getConfig().getLastUpdated().getTimeInMillis();
		long currentTime = System.currentTimeMillis();

		long updateInterval = 1;

		Log.v("CHECKING REQUIRED", "CHECKIGN REQUIRED UPDATE  LASTUPDATED : "
				+ lastUpdated);
		if (data.getConfig().getUpdateInterval() == 0) {
			// 3 months * 30 days * 24 hours * 60 mins * 60 secs * 1000 *ms
			updateInterval = (long) 3 * 30 * 24 * 60 * 60 * 1000;
		} else {
			updateInterval = (long) 6 * 30 * 24 * 60 * 60 * 1000;
		}
		long difference = Math.abs((long) (currentTime - lastUpdated));
		System.out.println("Difference : " + difference);
		System.out.println("UpdateInterval : " + ((long) updateInterval));

		if (((long) difference) >= ((long) updateInterval)) {
			if (haveNetworkConnection(act)) {

				// delete all old gtfs files

				clearGtfsFiles();

				updateGTFS(act);

				GTFSParser.parseAllFiles(data);

				// output optimize verions
				// CyroidOptimizeDataHandler.outputOptimizeFile(data);

				setData(data);
				// clearGtfsFiles();
				ConfigHandler.updateConfig(data);

				return true;
			} else {
				act.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								act);
						builder.setMessage(
								"Please Enable Internet Then Go To Settings To Manually Update or Cancel For Next Time")
								.setCancelable(true)
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {

												act.startActivity(new Intent(
														Settings.ACTION_WIFI_SETTINGS));

											}
										});
						AlertDialog alert = builder.create();
						alert.show();

					}

				});
			}

		}
		return false;

	}

	// download pdf
	public void downloadPDF(String url) {
		// InputStream in = null;
		// try {

		// trying external storage

		File cyroidDir = new File(Environment.getExternalStorageDirectory()
				.toString() + "/cyroid");

		if (!cyroidDir.exists())
			cyroidDir.mkdir();

		File pdfExt = new File("/sdcard/cyroid/AreaBusMap.pdf");

		try {

			if (!pdfExt.exists())
				pdfExt.createNewFile();

			URL Url = new URL(url);
			// HttpURLConnection c = (HttpURLConnection) u.openConnection();
			// c.setRequestMethod("GET");
			// c.setDoOutput(true);
			// c.connect();
			BufferedInputStream in = new BufferedInputStream(Url.openStream());
			FileOutputStream f = new FileOutputStream(pdfExt);

			byte[] buffer = new byte[1024];
			int len1 = 0;
			while ((len1 = in.read(buffer)) >= 0) {
				f.write(buffer, 0, len1);
			}
			Url.openStream().close();
			f.close();
			in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// CACHE OBJECT METHODS

	// UTIL METHODS

	public void printDataObject() {
		/*
		 * Log.v("CONFIG", "last updated" + data.getConfig().getLastUpdated());
		 * Log.v("CONFIG", "radius " + data.getConfig().getRadius());
		 * Log.v("Config", "update interval : " +
		 * data.getConfig().getUpdateInterval());
		 */
		Log.v("TEST ROUTE STOP", "Each ROUTE WITH STOPS");
		for (String routeKey : data.getRoutes().keySet()) {
			Route r = data.getRoutes().get(routeKey);
			if (r.getRouteID().contains("3N")) {
				Log.v("Route Name ", "@@@@@@@@@@@@@@@" + r.getLong_Route_Name());
				/*
				 * if (r.getTrips().containsKey("6B_T18")){
				 * Log.v("CONTAINS WRONG KEY", "KEY IS :"); }
				 */

				/*
				 * for (String tripID: r.getTrips().keySet()){ Trip t =
				 * r.getTrips().get(tripID); Log.v("ROUTE "+
				 * r.getRouteID(),"Trip ID "+ t.getTripID() + "trip route ID "+
				 * t.getRouteID()); }
				 */

				for (String stopKey : r.getStops().keySet()) {
					Stop s = r.getStops().get(stopKey);

					if (s.getStopID().contains("S0015")) {
						Log.v("STOP STOP STOP", "STOP ID : " + s.getStopID()
								+ " " + s.getStopName());
						Log.v("STOP SIZE: ", "STOP SIZE IS : "
								+ s.getStopTrips().size());
						for (StopTrips st : s.getStopTrips()) {
							Log.v("trip ID  " + st.getTripID(), "TIME :"
									+ st.getTime().toString());

						}
					}
				}

			}
			/*
			 * for (String stopKey : r.getStops().getStops().keySet()) { Stop s
			 * = r.getStops().getStops().get(stopKey); Log.v("Stops in Route" +
			 * r.getLong_Route_Name(), "		Stop : " + s.getStopName() + " "); //
			 * now check trips at each stop for each route; for (StopTrips st :
			 * s.getStopTrips()) { Log.v("	Stop Trips in Stop", "Stop Trip " +
			 * st.getTripID() + " " + st.getTime().toString()); } }
			 */}

	}

	private double compareDates(Calendar cal1, Calendar cal2) {
		double rating = 0;

		rating = rating
				+ Math.abs(cal1.get(Calendar.HOUR_OF_DAY)
						- cal2.get(Calendar.HOUR_OF_DAY));

		rating = rating
				+ ((Math.abs(cal1.get(Calendar.MINUTE)
						- cal2.get(Calendar.MINUTE))) / 100.0);

		return rating;
	}

	// GETTER AND SETTERS
	public void setData(CyroidData data) {
		this.data = data;
	}

	public CyroidData getData() {
		return data;
	}

	public void setInMainMenu(boolean inMainMenu) {
		this.inMainMenu = inMainMenu;
	}

	public boolean isInMainMenu() {
		return inMainMenu;
	}

	public void setRouteSelected(Route routeSelected) {
		this.routeSelected = routeSelected;
	}

	public Route getRouteSelected() {
		return routeSelected;
	}

	public void setNearestTimePos(int Pos) {
		nearestTimePos = Pos;
	}

	public int getNearestTimePos() {
		return nearestTimePos;
	}

	// private classes
	private class GTFSFirstTimeUpdator extends AsyncTask<Void, String, Void> {

		protected Activity act;
		private final ProgressDialog dialog;

		protected GTFSFirstTimeUpdator(Activity Act) {
			act = Act;
			this.dialog = new ProgressDialog(act);

		}

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Please Wait ...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			publishProgress("Deleting Old Files...");
			// delete all old gtfs files
			clearGtfsFiles();

			// download gtfs files
			// try {
			publishProgress("Updating Files...");
			updateGTFS(act);
			downloadPDF("http://www.cyride.com/Modules/ShowDocument.aspx?documentID=4673");

			ConfigHandler.generateConfig();

			publishProgress("Loading ...");
			GTFSParser.parseAllFiles(data);

			// output optmize part
			// CyroidOptimizeDataHandler.outputOptimizeFile(data);

			return null;

		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing())
				this.dialog.dismiss();

			// printDataObject();

		}

		@Override
		protected void onProgressUpdate(String... changed) {
			this.dialog.setMessage(changed[0]);
			Toast.makeText(act,
					"This is the first time so it may take longer...",
					Toast.LENGTH_LONG).show();
		}

	}// end of private class

	// private classes
	private class GTFSParseData extends AsyncTask<Void, String, Void> {

		protected Activity act;
		private final ProgressDialog dialog;

		protected GTFSParseData(Activity Act) {
			act = Act;
			this.dialog = new ProgressDialog(act);

		}

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Please Wait ...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			ConfigHandler.parseConfig(data);

			publishProgress("Downloading Updates ...");
			boolean check = checkRequiredUpdate(act, data, this);

			if (!check) {
				publishProgress("Loading ...");
				GTFSParser.parseAllFiles(data);

			}

			// GTFSParser.parseSecondTime(data);

			// setData(data);
			// clearGtfsFiles();

			return null;

		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing())
				this.dialog.dismiss();

		}

		@Override
		protected void onProgressUpdate(String... changed) {
			this.dialog.setMessage(changed[0]);
			Toast.makeText(act, "Please Wait", Toast.LENGTH_LONG).show();
		}

	}// end of private class

	private class GTFSUpdator extends AsyncTask<Void, String, Void> {

		protected Activity act;
		private final ProgressDialog dialog;

		protected GTFSUpdator(Activity Act) {
			act = Act;
			this.dialog = new ProgressDialog(act);

		}

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Please Wait ...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// ConfigHandler.updateConfig(data);
			publishProgress("Deleting Old Files...");
			// delete all old gtfs files
			clearGtfsFiles();

			// download gtfs files
			publishProgress("Updating Files...");
			updateGTFS(act);

			GTFSParser.parseAllFiles(data);

			// output optimize verions
			// CyroidOptimizeDataHandler.outputOptimizeFile(data);

			setData(data);
			// clearGtfsFiles();
			ConfigHandler.updateConfig(data);

			return null;

		}

		protected void onPostExecute(Void result) {
			if (this.dialog.isShowing())
				this.dialog.dismiss();

			// printDataObject();

		}

		@Override
		protected void onProgressUpdate(String... changed) {
			this.dialog.setMessage(changed[0]);
		}

	}// end of private class

	/**
	 * EVERYTHING BELOW THIS POINT IS NOT BEING USED ATM
	 * 
	 * 
	 * 
	 * public boolean cacheDataExists() { File file = new
	 * File("data/data/com.cyroid/CyroidData"); return file.exists(); }
	 * 
	 * public void deleteCacheData() { File file = new
	 * File("data/data/com.cyroid/CyroidData");
	 * 
	 * if (cacheDataExists()) { file.delete(); } } public void
	 * cacheCyroidData(Activity act) { new CacheCyroidData(act).execute(); }
	 * 
	 * public void getAndVerifyCacheData(Activity act) { new
	 * GetCyroidData(act).execute(); }
	 * 
	 * 
	 * 
	 * private class CacheCyroidData extends AsyncTask<Void, String, Void> {
	 * 
	 * protected Activity act; private final ProgressDialog dialog;
	 * 
	 * protected CacheCyroidData(Activity Act) { act = Act; this.dialog = new
	 * ProgressDialog(act);
	 * 
	 * }
	 * 
	 * // can use UI thread here protected void onPreExecute() {
	 * this.publishProgress("Saving Data ..."); this.dialog.show();
	 * 
	 * }
	 * 
	 * @Override protected Void doInBackground(Void... params) {
	 * 
	 *           CyroidEngine.this.saveObject(data, "data/data/com.cyroid/"); //
	 *           ConfigHandler.updateConfig(data);
	 * 
	 *           return null; }
	 * 
	 *           protected void onPostExecute(Void result) { //
	 *           printDataObject();
	 * 
	 *           if (this.dialog.isShowing()) this.dialog.dismiss();
	 * 
	 *           }
	 * @Override protected void onProgressUpdate(String... changed) {
	 *           this.dialog.setMessage(changed[0]); }
	 * 
	 *           }
	 * 
	 * 
	 *           private class GetCyroidData extends AsyncTask<Void, String,
	 *           Void> {
	 * 
	 *           protected Activity act; private final ProgressDialog dialog;
	 * 
	 *           protected GetCyroidData(Activity Act) { act = Act; this.dialog
	 *           = new ProgressDialog(act);
	 * 
	 *           }
	 * 
	 *           // can use UI thread here protected void onPreExecute() {
	 *           this.publishProgress("Getting Cached Object ...");
	 *           this.dialog.show();
	 * 
	 *           }
	 * @Override protected Void doInBackground(Void... params) {
	 * 
	 *           CyroidEngine.this
	 *           .setData(getCacheCyroidData("data/data/com.cyroid/")); //
	 *           ConfigHandler.parseConfig(CyroidEngine.this.getData());
	 * 
	 *           return null; }
	 * 
	 *           protected void onPostExecute(Void result) { //
	 *           printDataObject(); checkRequiredUpdate(act, data);
	 * 
	 *           if (this.dialog.isShowing()) this.dialog.dismiss();
	 * 
	 *           }
	 * @Override protected void onProgressUpdate(String... changed) {
	 *           this.dialog.setMessage(changed[0]); }
	 * 
	 *           }
	 */

}
