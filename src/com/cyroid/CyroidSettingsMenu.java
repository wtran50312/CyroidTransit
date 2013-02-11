package com.cyroid;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cyroid.control.ConfigHandler;
import com.cyroid.data.CyroidApplicationV2;


/**
 * @author Alex Wesenberg && William Tran ( Modified)
 * 
 * The SettingsMenu Activity is a screen in which the user can set various options about the
 * application such as the stop search radius and the mode in which the application runs.
 */
public class CyroidSettingsMenu extends Activity {
	

	private Button applyButton ;
	private Spinner intervalSpinner;
	private EditText edittext ;
	
	private CyroidApplicationV2 cyApp;		
	String timeInterval[] = {"3 Months","6 Months"};


	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		
		 cyApp = (CyroidApplicationV2) this.getApplication();
	
	
			
		/*
		 * Setup Button to handle updating now
		 * 
		 */
		((Button) this.findViewById(R.id.updateButton)).setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (cyApp.getCyroidEngine().haveNetworkConnection(CyroidSettingsMenu.this)) {
			    
			    	  cyApp.getCyroidEngine().updateData(CyroidSettingsMenu.this);
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							CyroidSettingsMenu.this);
					builder.setMessage(
							"Internet Is Required For First Time Use or Updating")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int id) {
											
											CyroidSettingsMenu.this.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
											
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
				}
				
			
				
			}
			
		});
		
		
		/*
		 * Setup Button To apply changed settings
		 * 
		 */
		(applyButton = (Button) this.findViewById(R.id.applySettings)).setOnClickListener( new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				
			
				
				
				int radius = Integer.parseInt(edittext.getText().toString());
				
				cyApp.getCyroidEngine().getData().getConfig().setRadius(radius);
				
				cyApp.getCyroidEngine().getData().getConfig().setUpdateInterval(intervalSpinner.getSelectedItemPosition());
				
				//cyApp.getCyroidEngine().cacheCyroidData(CyroidSettingsMenu.this);
				
				ConfigHandler.updateConfig(cyApp.getCyroidEngine().getData());
				
				Toast.makeText(CyroidSettingsMenu.this, "Settings Saved", Toast.LENGTH_LONG).show();
				
				
				intervalSpinner.setSelection(cyApp.getCyroidEngine().getData().getConfig().getUpdateInterval());
				edittext.setText(""+cyApp.getCyroidEngine().getData().getConfig().getRadius());
				
				
				
			}	
			
		});
		
		
		
		/*
		 * Sets up update interval for user
		 */
		   intervalSpinner = (Spinner) findViewById(R.id.intervalSpinner);
			 String[] spinnerSelection = this.getResources().getStringArray(R.array.spinner_array);
		    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		            this, R.array.spinner_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    intervalSpinner.setAdapter(adapter);
		    
		    Log.v("CONFIG POSITION : ", "CONFIG POSITION : " + cyApp.getCyroidEngine().getData().getConfig().getUpdateInterval());
		   
		   
		    intervalSpinner.setSelection(cyApp.getCyroidEngine().getData().getConfig().getUpdateInterval());
		    intervalSpinner.setOnItemSelectedListener( new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int pos, long id) {
				
						cyApp.getCyroidEngine().getData().getConfig().setUpdateInterval(pos);
			
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
		    	
		    });
		    
		    
		    
		    
		
		
		/*
		 * This object is a field which allows the user to type in the stopRadius they wish to use
		 * with the application.
		 */
		edittext = (EditText) findViewById(R.id.edittext);
		edittext.setInputType(InputMethodManager.HIDE_IMPLICIT_ONLY);
		edittext.setText(""+cyApp.getCyroidEngine().getData().getConfig().getRadius());
		edittext.setOnKeyListener(new OnKeyListener() {
			
			 boolean successMessage;
			 int stopRadius;
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		          // Perform action on key press
		        	String stopRadiusString = edittext.getText().toString();
		        	try{
		        		int garbageInteger=Integer.parseInt(stopRadiusString);
		        		if(garbageInteger<25||garbageInteger>5000) throw new NumberFormatException();//Doesn't allow the user to have a value outside of the specified range
		        		successMessage=true;
		        		stopRadius=Integer.parseInt(stopRadiusString);
		        	}
		        	catch(NumberFormatException e)
		        	{
		        		Toast.makeText(CyroidSettingsMenu.this, "Please enter a value between 25 and 5000 feet", Toast.LENGTH_SHORT).show();
		        		successMessage=false;
		        		edittext.setText("2000");
		        	}
		        		if(successMessage==true){
		        			Toast.makeText(CyroidSettingsMenu.this, "Remember to hit apply", Toast.LENGTH_LONG).show();
		        			edittext.setText(((Integer)stopRadius).toString());
		        			
		        			cyApp.getCyroidEngine().getData().getConfig().setRadius(stopRadius);

		        		}
		          return true;
		        }
		        return false;
		    }
		});
		
		/*
		 * This ToggleButton is used to decide between displaying Major Time stops only, 
		 * and displaying both major and minor time stops to the user.
		 
		 togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
		togglebutton.setChecked(!cyApp.getCyroidEngine().getData().getConfig().isMajorStops());
		togglebutton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		        if (togglebutton.isChecked()) {//If the button is already clicked
		            Toast.makeText(CyroidSettingsMenu.this, "All Stops Displayed", Toast.LENGTH_SHORT).show();
		          
        		    //Shows the user both Major and Minor stops for a route.
        		  cyApp.getCyroidEngine().getData().getConfig().setMajorStops(false);
		            
		        } else {
		            Toast.makeText(CyroidSettingsMenu.this, "Only Time Stops Displayed", Toast.LENGTH_SHORT).show();
		       
        		  //Shows the user only the major stops for a route.
        		   cyApp.getCyroidEngine().getData().getConfig().setMajorStops(true);
		        }
		    }
		});
		*/
		
		((Button) findViewById(R.id.resetSettings))
		.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
			
				cyApp.getCyroidEngine().getData().getConfig().setUpdateInterval(0);
				cyApp.getCyroidEngine().getData().getConfig().setRadius(2000);
				//cyApp.getCyroidEngine().getData().getConfig().setMajorStops(false);
				
				edittext.setText("2000");
				//togglebutton.setChecked(false);
				Toast.makeText(CyroidSettingsMenu.this, "All Settings Returned to Default Values", Toast.LENGTH_LONG).show();
			}
		});
		   
	  
		
	}
	
	/*
	 * This method is used to inflate the options menu on the bottom of the screen when the menu
	 * button is pressed on an Android device.
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_inflator, menu);
        
        return true;
    }
    
	/*
	 * This method is a listener which determines which(if any) buttons on the menu inflator
	 * that the user has selected. Depending on which button is pressed, it performs a certain
	 * action.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.mainMenu:
        	finish();
            return true;
        case R.id.maps:
        	cyApp.getCyroidEngine().viewMaps(CyroidSettingsMenu.this, cyApp);
        	return true;
        case R.id.about:
			startActivity(new Intent(CyroidSettingsMenu.this, CyroidAboutPage.class));//Changes to the AboutPage Activity
			return true;
        case R.id.info:
			startActivity(new Intent(CyroidSettingsMenu.this, CyroidInfoPage.class));//Changes to the AboutPage Activity
			return true;
        case R.id.contactUs:
			final Intent eI = new Intent(android.content.Intent.ACTION_SEND);
	    	eI.setType("plain/text");
	    	eI.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email)});
	    	eI.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.fb_sbjct_settings));
	    	eI.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.fb_body_settings));
	    	startActivity(Intent.createChooser(eI, "Send mail..."));
			return true;
			
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    public void onNothingSelected(AdapterView parent) {
      // Do nothing.
    }
    

    
}


