package com.cyroid;

import java.util.ArrayList;

import com.cyroid.control.CyroidGPS;
import com.cyroid.data.CyroidApplicationV2;
import com.cyroid.data.Stop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CyroidStopList extends Activity {

	private CyroidApplicationV2 cyApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stops_layout);

		cyApp = (CyroidApplicationV2) getApplicationContext();// instantiates
																// cyApp with
		final ArrayList<Stop> stopList = cyApp.getCyroidEngine().getStopList();
		
		//set text view to display route
		TextView routeView = (TextView) this.findViewById(R.id.stopSelectedRoute);
		routeView.setText(cyApp.getCyroidEngine().getRouteSelected().getLong_Route_Name());
		try {
			routeView.setTextColor(Color.parseColor("#"+ cyApp.getCyroidEngine().getRouteSelected().getRouteColor()));
			} catch (NumberFormatException e){
				routeView.setTextColor(Color.WHITE);
			}
		
		
		// Instantiates ListView to be used for displaying routes
		ListView lv = (ListView) findViewById(R.id.StopList);
		// Sets adapter to display correct routes
		StopAdapter stopAdapter = new StopAdapter(this, R.layout.time_layout,stopList);
		lv.setAdapter(stopAdapter);
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				AlertDialog.Builder dialog = new AlertDialog.Builder(CyroidStopList.this);
				
				ListView timeListView = new ListView(CyroidStopList.this);

			
					
					ArrayList<String> toDisplay = new ArrayList<String>();
				
					
					toDisplay = cyApp.getCyroidEngine().getTimesForStop(stopList.get(position));
					
					timeListView.setAdapter(new ArrayAdapter<String>(CyroidStopList.this,R.layout.time_layout,  toDisplay));
					timeListView.setSelection(cyApp.getCyroidEngine().getNearestTimePos());

					dialog.setView(timeListView);
					
					

			

				dialog.setTitle(stopList.get(position).getStopName());

				dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				// dialog.setCancelable(true);
				dialog.show();

				
			}
		});// listener
		
		
	}
	
	
	/**
	 * Checks which the previous menu so it may know which menu to return to
	 * when the back button is pressed
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			

				Intent intent = new Intent(CyroidStopList.this, CyroidRouteSelection.class);

				startActivity(intent);
				this.finish();
				return true;
			
		}// keycode back

		return super.onKeyDown(keyCode, event);
	}

	private class StopAdapter extends ArrayAdapter<Stop> {

		private ArrayList<Stop> stops;

		public StopAdapter(Context context, int textViewResourceId,
				ArrayList<Stop> objects) {
			super(context, textViewResourceId, objects);
			stops = objects;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) this.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.stoprow, null);

			}
			Stop s = stops.get(position);
			if (s != null) {

				TextView nameView = (TextView) v.findViewById(R.id.stopName);

				if (nameView != null) {
					nameView.setText("" + s.getStopName());
				}
			}
			return v;
		}

	}// end of private class
	
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
			startActivity(new Intent(CyroidStopList.this,
					CyroidMainMenu.class));// Pressing this button returns to
											// the main

			return true;
		case R.id.about:
			startActivity(new Intent(CyroidStopList.this,
					CyroidAboutPage.class));// Changes
			// to
			// the
			// AboutPage
			// Activity
			return true;
			
		case R.id.maps :
			cyApp.getCyroidEngine().viewMaps(CyroidStopList.this, cyApp);
			 return true;
			 
		case R.id.info:
			startActivity(new Intent(CyroidStopList.this,
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

}
