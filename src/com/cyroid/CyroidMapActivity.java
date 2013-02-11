package com.cyroid;



import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.cyroid.control.CyroidGPS;
import com.cyroid.data.CyroidApplicationV2;
import com.cyroid.data.CyroidLocation;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;


/**
 * This activity will display specified data into Google map.
 * 
 * @author William Tran
 * 
 */
public class CyroidMapActivity extends MapActivity {
	/**
	 * Name of the shared preferences
	 */
	public static final String PREFS_NAME = "MyPrefsFile";

	private MapView mapView;// The layout/look of the map

	private MapController mc;// Used to control the map

	private CyroidGPS GPS;// GPS used to get user locations

	private CyroidApplicationV2 cyApp;// Used to access global variables and
									// objects

	private Drawable myLocationPin;// Icons used for user position and
											// stop


	private CyroidCyOverLayItems currentPosition;// holds user positions

	private CyroidCyOverlayItem myPosition;// graphical representation of user
										// position

	private CyroidCyOverLayItems stops; // holds all stops


	private List<Overlay> mapOverLay;// used to hold both stops and user
										// locations

	private CyroidLocation myLocat;// used to store user location

	private double stopRadius;// the radius of which stops should be displayed
	
	private int zoomLevel = 13;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cymap);
		cyApp = ((CyroidApplicationV2) getApplicationContext());
		
		
	

		// sets the icon for user and stop locations
		this.setLocationPinDrawable(R.drawable.ic_google_map_gps_pin);
		//this.setStopPinDrawable(R.drawable.ic_google_map_pin);
		
		//set text view to display route
		TextView routeView = (TextView) this.findViewById(R.id.mapRouteName);
		routeView.setText(cyApp.getCyroidEngine().getRouteSelected().getLong_Route_Name());
		try {
			routeView.setTextColor(Color.parseColor("#"+ cyApp.getCyroidEngine().getRouteSelected().getRouteColor()));
			} catch (NumberFormatException e){
				routeView.setTextColor(Color.WHITE);
			}
	
		// Will actually use gps to calculate nearest stops
		if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection) {
			GPS = CyroidApplicationV2.getGPS();
			myLocat = GPS.getGPSLocation();
			cyApp.setMyLocat(myLocat);
			zoomLevel = 17;
			stopRadius = cyApp.getCyroidEngine().getData().getConfig().getRadius();
		

		}
		// Otherwise user selected browse mode. Will display all stops.
		else if (cyApp.getSelection() == CyroidApplicationV2.inBrowseRoutes) {
			cyApp.getMyLocat().setLatLon(42.023397,-93.625638);
			myLocat = cyApp.getMyLocat();
			
			//Log.v("MY LOCATION ", "LAT : "+ cyApp.getTownCenterLat() +"  LON "+ cyApp.getTownCenterLon());
			
			
			zoomLevel= 13;

			stopRadius = 100000000.0;
	
		}

		// Used to handle any code after a thread has finished
		final Handler handle = new Handler();
		final Runnable run = new Runnable() {

			public void run() {
				runOnPost();
			}
		};

		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Loading. Please wait...", true);// displays a progress bar and
		// dialogue to the user

		// This thread will gather all the information required by the gui
		Thread mapThread = new Thread(new Runnable() {

			public void run() {
				try {
					dialog.show();// display progress bar
					runOnBackground();// gather all information from server
					dialog.dismiss();// when finished close progress bar
					handle.post(run);// execute gui code
				} catch (NullPointerException e) {

				}
			}
		});

		mapThread.start(); // start thread
	}

	/**
	 * Method to run any information gathering code such as calling the server
	 * to return information required by the gui.
	 */
	public void runOnBackground() {

	
		
		stops = cyApp.getCyroidEngine().getAllMapStops(this, (int) stopRadius, myLocat);
		
		

	}

	/**
	 * Initialize the gui by displaying all information from server in a useful
	 * way
	 */
	public void runOnPost() {

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
	
		
		mc = mapView.getController();

		mc.animateTo(myLocat.getGeoLocation());
		mc.setZoom(zoomLevel);

		mapOverLay = mapView.getOverlays();
		mapOverLay.clear();

		//create raduis overlay
		MyRadiusOverlay radiusOverlay = new MyRadiusOverlay(cyApp);
		
		// Set pictures to pins
		currentPosition = new CyroidCyOverLayItems(myLocationPin, this, true);
		//stops = new CyOverLayItems(stopPin, this, false);

		
		if(cyApp.getSelection() == CyroidApplicationV2.inRouteSelection) {
			myPosition = new CyroidCyOverlayItem(myLocat, myLocat.getDescription(),
					"Current Position");
			myPosition.setCurrentPosition(true);

			currentPosition.addOverlay(myPosition);

			mapOverLay.add(currentPosition);
			mapOverLay.add(stops);
			mapOverLay.add(radiusOverlay);
			mapView.invalidate();
			
			Log.v(" NOTTTT IN BROWSE ROUTES","NOTTTT IN BROWSE ROUTES 2");
		}
		/*
		if (eachStop.size() == 0) {
			Toast.makeText(getApplicationContext(), "No stops near you. Try a larger radius in settings.",
					Toast.LENGTH_LONG).show();
		}
		*/

		if (cyApp.getSelection() == CyroidApplicationV2.inBrowseRoutes) {
			Log.v("IN BROWSE ROUTES","IN BROWSE ROUTES 2");
			
			mapOverLay.add(stops);
			mapView.invalidate();

			
		
		}// eachStop != null

	}

	/*
	 * NOT USED AT THE MOMENT float startX = -1; float startY = -3; float endX =
	 * -4; float endY = -2;
	 * 
	 * public boolean dispatchTouchEvent(MotionEvent ev) {
	 * 
	 * if (cyApp.getSelectOwnLocation()) { int actionType = ev.getAction();
	 * 
	 * if (actionType == MotionEvent.ACTION_DOWN) { startX = ev.getX(); startY =
	 * ev.getY(); Toast.makeText(getApplicationContext(), "Moving",
	 * Toast.LENGTH_LONG); }
	 * 
	 * else if (actionType == MotionEvent.ACTION_UP) { endX = ev.getX(); endY =
	 * ev.getY();
	 * 
	 * if (startX == endX && startY == endY) {
	 * 
	 * Projection proj = mapView.getProjection(); GeoPoint loc =
	 * proj.fromPixels((int) ev.getX(), (int) ev.getY()); String x =
	 * Double.toString(loc.getLongitudeE6() / 1E6); String y =
	 * Double.toString(loc.getLatitudeE6() / 1E6);
	 * 
	 * CyOverlayItem temp = new CyOverlayItem(loc, "test", "test");
	 * currentPosition.addOverlay(temp); mapOverLay.add(currentPosition);
	 * 
	 * Toast toast = Toast.makeText(getApplicationContext(), "Lat: " + x +
	 * " Lon: " + y, Toast.LENGTH_LONG); toast.show(); }
	 * 
	 * }
	 * 
	 * } return super.dispatchTouchEvent(ev); }
	 */

	/**
	 * Checks which the previous menu so it may know which menu to return to
	 * when the back button is pressed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			

				Intent intent = new Intent(CyroidMapActivity.this, CyroidRouteSelection.class);

				startActivity(intent);
				//this.finish();
				return true;
			
		}// keycode back

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Disable gps ADD MORE IMPLEMENTATION
	 */
	@Override
	public void onPause() {
		super.onPause();

		if (cyApp.getSelection() == CyroidApplicationV2.inRouteSelection) {
			if (GPS != null){
				if (GPS.getGPSStatus() == true)
					GPS.setEnable(false);
				}
		}
	}

	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Initializes the menu gui
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.other_menu_inflator, menu);
		return true;
	}

	/**
	 * Adds more options and action handling to inflator (menu)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		GPS = CyroidApplicationV2.getGPS();
		if (GPS != null){
			if (GPS.getGPSStatus()){
				GPS.setEnable(false);
			}
		}
		
		
		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mainMenu:
			startActivity(new Intent(CyroidMapActivity.this, CyroidMainMenu.class));
			this.finish();
			return true;
			
		case R.id.maps:
			cyApp.getCyroidEngine().viewMaps(CyroidMapActivity.this, cyApp);
			return true;
			
		case R.id.about:
			startActivity(new Intent(CyroidMapActivity.this, CyroidAboutPage.class));// Changes to
			return true;
		case R.id.info:
			startActivity(new Intent(CyroidMapActivity.this, CyroidInfoPage.class));
			return true;
		case R.id.contactUs:
			final Intent eI = new Intent(android.content.Intent.ACTION_SEND);
			eI.setType("plain/text");
			eI.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { getString(R.string.feedback_email) });
			eI.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.fb_sbjct_general));
			eI.putExtra(android.content.Intent.EXTRA_TEXT,
					getString(R.string.fb_body_general));
			startActivity(Intent.createChooser(eI, "Send mail..."));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	
	/**
	 * Used to set the user pin for a drawable icon
	 * 
	 * @param Drawable
	 */
	public void setLocationPinDrawable(int Drawable) {
		myLocationPin = this.getResources().getDrawable(Drawable);
	}


}
