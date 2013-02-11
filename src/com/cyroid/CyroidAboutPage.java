package com.cyroid;

import com.cyroid.data.CyroidApplicationV2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CyroidAboutPage extends Activity{
	
	CyroidApplicationV2 cyApp;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
			setContentView(R.layout.about_page);
		 cyApp = (CyroidApplicationV2) this.getApplication();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about_inflator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.mainMenu:
			startActivity(new Intent(CyroidAboutPage.this, CyroidMainMenu.class));
			this.finish();
			return true;
			
		case R.id.maps:
			cyApp.getCyroidEngine().viewMaps(CyroidAboutPage.this, cyApp);
			return true;
			
		case R.id.info:
			startActivity(new Intent(CyroidAboutPage.this, CyroidInfoPage.class));//Changes to the AboutPage Activity
			this.finish();
			return true;
		case R.id.contactUs:
			final Intent eI = new Intent(android.content.Intent.ACTION_SEND);
	    	eI.setType("plain/text");
	    	eI.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email)});
	    	eI.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.fb_sbjct_about));
	    	eI.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.fb_body_about));
	    	startActivity(Intent.createChooser(eI, "Send mail..."));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
