package com.cyroid;



import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.cyroid.data.CyroidApplicationV2;
import com.cyroid.data.CyroidLocation;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

/**
 * This activity will display specified data into Google map.
 * 
 * @author William Tran
 * 
 */
public class CyroidMapViewer extends MapActivity {
	

	private MapView mapView;// The layout/look of the map

	private MapController mc;// Used to control the map



	private CyroidApplicationV2 cyApp;// Used to access global variables and
									// objects


	private CyroidLocation myLocat;// used to store user location
	
	private MapStopOverlay mapStopOverlay;


	private int zoomLevel = 13;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cymapviewer);
		cyApp = ((CyroidApplicationV2) getApplicationContext());
		
		//set text view to display route
		TextView routeView = (TextView) this.findViewById(R.id.mapViewerRouteName);
		routeView.setText(cyApp.getCyroidEngine().getRouteSelected().getLong_Route_Name());
		try {
		routeView.setTextColor(Color.parseColor("#"+ cyApp.getCyroidEngine().getRouteSelected().getRouteColor()));
		} catch (NumberFormatException e){
			routeView.setTextColor(Color.WHITE);
		}
			
		
		
		
		mapStopOverlay = new MapStopOverlay(cyApp.getCyroidEngine().getRouteSelected());
		
		cyApp.getMyLocat().setLatLon(42.023397,-93.625638);
		myLocat = cyApp.getMyLocat();
		
		//Log.v("MY LOCATION ", "LAT : "+ cyApp.getMyLocat().getLat() +"  LON "+ cyApp.getMyLocat().getLon());
		
		zoomLevel= 13;

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

	
	
		//ops = cyApp.getCyroidEngine().getAllMapStops(this, 500000000, myLocat);
		
		

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
		
		mapView.getOverlays().clear();
		mapView.getOverlays().add(mapStopOverlay);
	



	

	}

	

	/**
	 * Checks which the previous menu so it may know which menu to return to
	 * when the back button is pressed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			

				Intent intent = new Intent(CyroidMapViewer.this, CyroidRouteSelection.class);

				startActivity(intent);
				this.finish();
				return true;
			
		}// keycode back

		return super.onKeyDown(keyCode, event);
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
		
	
		
		
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mainMenu:
			startActivity(new Intent(CyroidMapViewer.this, CyroidMainMenu.class));
			this.finish();
			return true;
		case R.id.about:
			startActivity(new Intent(CyroidMapViewer.this, CyroidAboutPage.class));// Changes to
			return true;
		case R.id.info:
			startActivity(new Intent(CyroidMapViewer.this, CyroidInfoPage.class));
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
	



}
