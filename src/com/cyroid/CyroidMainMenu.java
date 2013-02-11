package com.cyroid;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cyroid.data.CyroidApplicationV2;
import com.cyroid.control.CyroidGPS;

/**
 * @author William Tran
 * @author Alex Wesenberg
 * @author Kevin Nennig
 * 
 *         The MainMenu class is the Activity representing the Main Menu screen
 *         of the Cyroid Application.
 */
public class CyroidMainMenu extends Activity {

	// cyApp saves the current state of the Application
	private CyroidApplicationV2 cyApp;

	private CyroidGPS GPS;

	private boolean inBrowseRoutes;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		cyApp = ((CyroidApplicationV2) getApplicationContext());// gets the
																// current
																// application
																// state

		cyApp.getCyroidEngine().init(this);
		/*
		 * //first time no internet handler if
		 * (!cyApp.getCyroidEngine().haveNetworkConnection(this) &&
		 * !ConfigHandler.configFileExists()) { Log.v("TAGGGGGGGGGG",
		 * "!cyApp.getCyroidEngine().haveNetworkConnection(this) && !ConfigHandler.configFileExists()"
		 * ); AlertDialog.Builder builder = new AlertDialog.Builder( this);
		 * builder.setMessage(
		 * "Internet Is Required For First Time Use or Updating")
		 * .setCancelable(false) .setPositiveButton("OK", new
		 * DialogInterface.OnClickListener() { public void onClick(
		 * DialogInterface dialog, int id) { ConfigHandler.deleteConfigFile();
		 * CyroidMainMenu.this.finish();
		 * 
		 * 
		 * } }); AlertDialog alert = builder.create(); alert.show();
		 * cyApp.setCreateMainMenu(false); //has internet and first time } else
		 * if (cyApp.getCyroidEngine().haveNetworkConnection(this) &&
		 * !ConfigHandler.configFileExists()) {
		 * cyApp.getCyroidEngine().init(this); Log.v("TAGGGGGG",
		 * "cyApp.getCyroidEngine().haveNetworkConnection(this)");
		 * cyApp.setCreateMainMenu(false); } else if (
		 * cyApp.isCreateMainMenu()){ Log.v("TAGGGGGG",
		 * "ELSEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
		 * cyApp.setCreateMainMenu(false);
		 * 
		 * cyApp.getCyroidEngine().getAndVerifyCacheData(this); }
		 */

		/*
		 * This is a button listener for the FindABus button, and listens for a
		 * user click on this button, and takes appropriate action
		 */
		((Button) findViewById(R.id.FindABus))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {

						cyApp.setSelection(CyroidApplicationV2.inRouteSelection);

						Intent changeToRouteScreen = new Intent(
								CyroidMainMenu.this, CyroidRouteSelection.class);
						
						startActivity(changeToRouteScreen);// Switches activity
															// to the
															// RouteSelection
															// class

					}
				});
		/*
		 * This is a button listener for the CallInformation button, which
		 * listens for user input and take the appropriate action when a user
		 * action is detected.
		 */
		((Button) findViewById(R.id.CallInformation))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {

						Intent call = new Intent(Intent.ACTION_VIEW, Uri
								.parse("tel:"
										+ cyApp.getCyroidEngine().getData()
												.getAgency().getPhone()));// Sets
																			// the
																			// phone
																			// number
																			// to
																			// call
						call.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(call);// Starts a new call activity onthe
											// phone

					}
				});
		/*
		 * This is a button listener for the BrowseRoutes button, which listens
		 * for user input and takes the appropriate action when a user action is
		 * detected.
		 */
		((Button) findViewById(R.id.BrowseRoutes))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {
						/*
						 * settings = getSharedPreferences(PREFS_NAME,0);
						 * SharedPreferences.Editor editor = settings.edit();
						 * cyApp.setInBrowseRoutes(true); Intent
						 * changeToBrowseRoutes = new Intent(MainMenu.this,
						 * BrowseRoutes.class);
						 * startActivity(changeToBrowseRoutes);//Changes the
						 * activity to the BrowseRoutes class
						 * MainMenu.this.finish();
						 */

						cyApp.setSelection(CyroidApplicationV2.inBrowseRoutes);

						Intent changeToRouteScreen = new Intent(
								CyroidMainMenu.this, CyroidRouteSelection.class);
						
						startActivity(changeToRouteScreen);// Switches activity
															// to the
															// RouteSelection
															// class

					}
				});

		/*
		 * This is a button listener for the BusMap button, which listens for
		 * user input and takes the appropriate action when a user action is
		 * detected.
		 */
		((Button) findViewById(R.id.BusMap))
				.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {

						final CharSequence[] items = { "Main Map", "Route Maps" };
						// Creates a dialog that will have Listview and MapView
						// Depending on which one is selected, it will change to
						// that activity
						AlertDialog.Builder builder = new AlertDialog.Builder(
								CyroidMainMenu.this);
						builder.setTitle("Choose Map View");
						builder.setSingleChoiceItems(items, -1,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int item) {
										if (item == 0) {
											dialog.dismiss();

											
											File pdf = new File("/sdcard/cyroid/AreaBusMap.pdf");
											//File pdf = new File ("Android/data/data/com.cyroid/AreaBusMap.pdf");
											Intent pdfViewerIntent = new Intent(
													Intent.ACTION_VIEW);
											
											if (pdf.exists()) {
												Log.v("PATH EXISTS", "EXIST EXIST EXIST");
												Uri path = Uri
														.fromFile(pdf);
												Log.v("PATH TEST",""+path.getPath());
												pdfViewerIntent.setDataAndType(path,"application/pdf");
												pdfViewerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											
											}
											try {
											
													startActivity(pdfViewerIntent);
												
											} catch (ActivityNotFoundException e) {
												Toast.makeText(
														CyroidMainMenu.this,
														"No Application Available to View PDF",
														Toast.LENGTH_LONG)
														.show();
											}
											
											//CyroidMainMenu.this.startActivity(new Intent(CyroidMainMenu.this,PdfViewer.class));
										} else if (item == 1) {
											cyApp.setSelection(CyroidApplicationV2.inMapViewSelection);
											dialog.dismiss();
											startActivity(new Intent(
													CyroidMainMenu.this,
													CyroidRouteSelection.class));

										}
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

					}
				});

		/* this needs to go in the inflator!!! */
		/*
		 * ((Button) findViewById(R.id.feedback)) .setOnClickListener(new
		 * Button.OnClickListener() { public void onClick(View arg0) { final
		 * Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		 * emailIntent.setType("plain/text");
		 * emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new
		 * String[]{"cyroidtransit@googlegroups.com"});
		 * emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		 * "Cyroid Feedback");
		 * emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
		 * "Please tell us something!");
		 * startActivity(Intent.createChooser(emailIntent, "Send mail...")); }
		 * });
		 */
	}

	/*
	 * This method is a listener for the back button. Whenever the user presses
	 * the back button, it will load MainMenu.class.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			return false;
			

		}// keycode back
		return super.onKeyDown(keyCode, event);

	}

	
	/*
	 * This method is used to inflate the options menu on the bottom of the
	 * screen when the menu button is pressed on an Android device.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu_inflator, menu);
		return true;

	}

	/*
	 * This method is a listener which determines which(if any) buttons on the
	 * menu inflator that the user has selected. Depending on which button is
	 * pressed, it performs a certain action.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle item selection
		switch (item.getItemId()) {

		case R.id.settings:
			startActivity(new Intent(CyroidMainMenu.this,
					CyroidSettingsMenu.class));// Changes to the SettingsMenu
												// Activity
			return true;

		case R.id.maps:
			cyApp.getCyroidEngine().viewMaps(CyroidMainMenu.this, cyApp);
			return true;

		case R.id.about:
			startActivity(new Intent(CyroidMainMenu.this, CyroidAboutPage.class));// Changes
																					// to
																					// the
																					// AboutPage
																					// Activity
																					// return
			return true;
		case R.id.info:
			startActivity(new Intent(CyroidMainMenu.this, CyroidInfoPage.class));// Changes
																					// to
																					// the
																					// AboutPage
																					// Activity
																					// return
																					// true;
			return true;

		case R.id.contactUs:
			final Intent eI = new Intent(android.content.Intent.ACTION_SEND);
			eI.setType("plain/text");
			eI.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { getString(R.string.feedback_email) });
			eI.putExtra(android.content.Intent.EXTRA_SUBJECT,
					getString(R.string.fb_sbjct_mainmenu));
			eI.putExtra(android.content.Intent.EXTRA_TEXT,
					getString(R.string.fb_body_mainmenu));
			startActivity(Intent.createChooser(eI, "Send mail..."));

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	/**
	 * 
	 */
	@Override
	public void onDestroy(){
		super.onDestroy();
		 
		/*
		if (cyApp.getCyroidEngine().getData() == null){
			cyApp.getCyroidEngine().setInMainMenu(false);
			
		}
		*/
		
		
		
	}

	/**
	 * Disable gps ADD MORE IMPLEMENTATION
	 * 
	 */
	@Override
	public void onPause() {
		super.onPause();

		if (!inBrowseRoutes) {
			if (GPS != null) {
				if (GPS.getGPSStatus() == true)
					GPS.setEnable(false);
			}
		}
	}

}