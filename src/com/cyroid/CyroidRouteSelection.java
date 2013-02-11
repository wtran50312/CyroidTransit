package com.cyroid;



import java.util.ArrayList;

import com.cyroid.control.CyroidGPS;
import com.cyroid.data.CyroidApplicationV2;
import com.cyroid.data.Route;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity is responsible for finding the current available routes and
 * displaying them to the users. It is also responsible for listening for user
 * input corresponding to which route they wish to select.
 * 
 * @author William Tran MODIFIED
 * @author Alex Wesenberg
 * 
 */
public class CyroidRouteSelection extends Activity {

	private CyroidApplicationV2 cyApp; // Represents the current state of the
										// entire Application
	private ArrayList<Route> routes; // String array representing the list of
										// bus routes presented to the user

	final Runnable runPost = new Runnable() {
		public void run() {
			// This ArrayList is used to store the routes selected by the user
			postBackground();
		}
	};

	/*
	 * This method is executed whenever the RouteSelection activity is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_layout);

		cyApp = (CyroidApplicationV2) getApplicationContext();// instantiates
																// cyApp with
																// the
		// current
		// application state
		// cyApp.initGPS(this);//initializes a GPS object reference to be used
		// throughout the applicaiton
	
		// reset gps locaiton
		if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection ) {
			
			if ( CyroidApplicationV2.getGPS() == null){
				CyroidApplicationV2.initGPS(CyroidRouteSelection.this);
			}
			
			CyroidApplicationV2.getGPS().setEnable(true);
			
		}
		
		

		// CyroidApplication.getGPS().setEnable(true);//enables GPS for use by
		// the activity

		final Handler handle = new Handler();
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Loading. Please wait...", true);// displays a progressbar and
													// dialogue to the user

		new Thread() {
			@Override
			public void run() {
				dialog.show();
				doInBackground();
				dialog.dismiss();
				handle.post(runPost);

			}
		}.start();

	}// onCreate

	public void doInBackground() {
		routes = cyApp.getCyroidEngine().getRouteList();
	}

	private void postBackground() {
		
		
		
		

		// Instantiates ListView to be used for displaying routes
		ListView lv = (ListView) findViewById(R.id.RouteList);
		RouteAdapter routeAdapter = new RouteAdapter(this, R.layout.routerow,
				routes);
		// Sets adapter to display correct routes
		lv.setAdapter(routeAdapter);
		lv.setTextFilterEnabled(true);
		
		
		

		/*
		 * This is a listener for the ListView which displays the list of routes
		 * to the user. It allows the user to click on, and select one route to
		 * retrieve more information for.
		 */
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				cyApp.getCyroidEngine().setRouteSelected(routes.get(position));

				Intent routeSelection = new Intent(CyroidRouteSelection.this,
						CyroidMapActivity.class);
				
				if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection) {
				
					if ( (CyroidApplicationV2.getGPS() != null) & (CyroidApplicationV2.getGPS().getGPSLocation().getLat() != -1000.0)) {

						CyroidApplicationV2.getGPS().getGPSLocation();// Retreives
																		// the
																		// current
																		// GPS
																		// location

						CyroidApplicationV2.getGPS().setEnable(false);// Disables
																		// GPS
																		// Listener
																		// after
																		// receiving
																		// the
																		// coordinate
																		// to
																		// save
																		// battery
																		// life.
						startActivity(routeSelection);
				
						finish();

					} else {

						Toast.makeText(getApplicationContext(),
								"Please turn on GPS or retry outdoors",
								Toast.LENGTH_SHORT).show();
						// CyroidApplicationV2.getGPS().setEnable(true);
						if (!CyroidApplicationV2
								.getGPS()
								.getLocationManager()
								.isProviderEnabled(LocationManager.GPS_PROVIDER))
							startActivityForResult(
									new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
									0);
					}
				} else if (cyApp.getSelection() == CyroidApplicationV2.inBrowseRoutes){
					// in brose routes;
				
					final CharSequence[] items = {"ListView", "MapView"};
					//Creates a dialog that will have Listview and MapView
					//Depending on which one is selected, it will change to that activity
					AlertDialog.Builder builder = new AlertDialog.Builder(CyroidRouteSelection.this);
					builder.setTitle("Choose a view");
					builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int item) {
					        if (item == 0){
					        	dialog.dismiss();
					         
					        	startActivity(new Intent(CyroidRouteSelection.this,CyroidStopList.class));
								finish();

					        }
					        else if (item == 1) {
					        	
								dialog.dismiss();
								startActivity(new Intent(CyroidRouteSelection.this,CyroidMapActivity.class));
								finish();

					        }
					    }
					});
					AlertDialog alert = builder.create();
					alert.show();
					
				} else if (cyApp.getSelection() == CyroidApplicationV2.inMapViewSelection){
					startActivity( new Intent(CyroidRouteSelection.this,
							CyroidMapViewer.class));
							finish();
				}

			
			}

		});
	}

	/*
	 * This method acts as an inflater for the Options Menu. When the user
	 * presses the menu button on their Android device, the correct XML layout
	 * is shown on the bottom of the screen.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.other_menu_inflator, menu);// inflates
															// other_menu_inflator.xml
		return true;

	}

	/*
	 * This method acts as a listener for the optionsMenu. It listens for button
	 * input on the inflated options menu, and performs the corresponding
	 * action.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		CyroidGPS GPS = CyroidApplicationV2.getGPS();
		if (GPS != null) {
			if (GPS.getGPSStatus()) {
				GPS.setEnable(false);
			}
		}

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mainMenu:
			startActivity(new Intent(CyroidRouteSelection.this,
					CyroidMainMenu.class));// Pressing this button returns to
											// the main

			return true;
		case R.id.about:
			startActivity(new Intent(CyroidRouteSelection.this,
					CyroidAboutPage.class));// Changes
			// to
			// the
			// AboutPage
			// Activity
			return true;
			
		case R.id.maps :
				cyApp.getCyroidEngine().viewMaps(CyroidRouteSelection.this, cyApp);
				
			 return true;
			 
		case R.id.info:
			startActivity(new Intent(CyroidRouteSelection.this,
					CyroidInfoPage.class));// Changes
											// to
											// the
											// AboutPage
											// Activity
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	/*
	 * This method is a listener for the back button. Whenever the user presses
	 * the back button, it will load MainMenu.class.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection) {
				CyroidGPS GPS = CyroidApplicationV2.getGPS();
				
				if (GPS != null) {
					if (GPS.getGPSStatus()) {
						GPS.setLocation(-1000.0, -1000.0);
						GPS.setEnable(false);
					}
				}
			}
			Intent i = new Intent(CyroidRouteSelection.this,
					CyroidMainMenu.class);
			i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(new Intent(CyroidRouteSelection.this,
					CyroidMainMenu.class));

		}// keycode back
		return super.onKeyDown(keyCode, event);

	}

	/**
	 * Disable gps ADD MORE IMPLEMENTATION
	 * 
	 */
	 @Override 
	 public void onPause() { 
		 CyroidGPS GPS = CyroidApplicationV2.getGPS(); 
	 			if (GPS != null){ 
	 				if (GPS.getGPSStatus()){ 
	 					GPS.setEnable(false); 
	 					} 
	 				}
	  
	            super.onPause(); 
	     }
	 
	 @Override
	 public void onResume() {
		 super.onResume();
		 
		 if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection){
			 CyroidApplicationV2.getGPS().setEnable(true);
		 }
	 }

	private class RouteAdapter extends ArrayAdapter<Route> {

		private ArrayList<Route> routes;

		public RouteAdapter(Context context, int textViewResourceId,
				ArrayList<Route> objects) {
			super(context, textViewResourceId, objects);
			routes = objects;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.routerow, null);

			}
			Route r = routes.get(position);
			if (r != null) {
				TextView colorView = (TextView) v.findViewById(R.id.routeColor);
				TextView nameView = (TextView) v.findViewById(R.id.routeName);

				if (colorView != null) {
					try {
						colorView.setBackgroundColor(Color.parseColor("#"
								+ r.getRouteColor()));
						colorView.setText("");
					} catch (NumberFormatException e) {
						colorView.setBackgroundColor(0);
						colorView.setText("");
					}

				}
				if (nameView != null) {
					nameView.setText("#" + r.getShort_Route_Name() + " "
							+ r.getLong_Route_Name());
				}
			}
			return v;
		}
		/*
		 * @Override protected void onPause() { if (GPS.getGPSStatus() == true)
		 * GPS.setEnable(false); super.onPause(); }
		 */
	}

}
