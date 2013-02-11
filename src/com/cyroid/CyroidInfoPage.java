package com.cyroid;

import com.cyroid.data.CyroidApplicationV2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * @author Kevin Nennig
 * 
 * This is the info page in the application. It explains the 
 * specific functions of the applications
 */
public class CyroidInfoPage extends Activity{
	
	CyroidApplicationV2 cyApp;

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//Goes to the xml page that holds all of the data for the info page
		setContentView(R.layout.info_page);
		
		 cyApp = (CyroidApplicationV2) this.getApplication();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.info_inflator, menu);
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
			startActivity(new Intent(CyroidInfoPage.this, CyroidMainMenu.class));
			this.finish();
			return true;
			
		case R.id.maps:
			cyApp.getCyroidEngine().viewMaps(CyroidInfoPage.this, cyApp);
			return true;
			
		case R.id.about:
			startActivity(new Intent(CyroidInfoPage.this, CyroidAboutPage.class));//Changes to the AboutPage Activity
			this.finish();
			return true;
		case R.id.contactUs:
			final Intent eI = new Intent(android.content.Intent.ACTION_SEND);
	    	eI.setType("plain/text");
	    	eI.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email)});
	    	eI.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.fb_sbjct_info));
	    	eI.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.fb_body_info));
	    	startActivity(Intent.createChooser(eI, "Send mail..."));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	

	
}
