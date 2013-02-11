package com.cyroid;


import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cyroid.data.CyroidApplicationV2;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * 
 * @author William This class will hold all the overlay items
 * Meaning this class will contain all the gui representation
 * of the stops
 * 
 */
public class CyroidCyOverLayItems extends ItemizedOverlay {

	// Holds CyOverlayItems
	private ArrayList<CyroidCyOverlayItem> mOverlays = new ArrayList<CyroidCyOverlayItem>();

	// Context of application
	private Context mContext;
	
	private CyroidApplicationV2 cyApp;

	boolean isCurrentPosition = false;

	/**
	 * Constructor
	 * 
	 * @param defaultMarker
	 */
	public CyroidCyOverLayItems(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();

	}

	/**
	 * Constructor with context
	 * 
	 * @param defaultMarker
	 * @param context
	 */
	public CyroidCyOverLayItems(Drawable defaultMarker, Context context,
			boolean CurrentPosition) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		cyApp = ((CyroidApplicationV2) context.getApplicationContext());
		isCurrentPosition = CurrentPosition;
		populate();

	}
	
	public ArrayList<CyroidCyOverlayItem> getMOverlays (){
		return mOverlays;
	}

	/**
	 * Adds a overlay item to the list of overlays
	 * 
	 * @param overlay
	 */
	public void addOverlay(CyroidCyOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();

	}

	/**
	 * Removes overlay item
	 */
	public void removeOverlay() {
		mOverlays.remove(0);
	}

	/**
	 * Returns an overlay item at index
	 * 
	 * @param i
	 *            - the index of overlay requested
	 * @return the overlay at index
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	
	/**
	 * This method will add the times to an overlay representing
	 * a bus stop on the map. It will then figure out which bus time is the
	 * next available time, and scroll to that time. If no times are after the current
	 * time, then the list starts from the beginning.
	 */
	@Override
	protected boolean onTap(int index) {

		CyroidCyOverlayItem item = mOverlays.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		
		ListView lv = new ListView(mContext);

		if (item.isCurrentPosition() == false) {
			
			
			
			ArrayList<String> toDisplay = new ArrayList<String>();
		
			
			toDisplay = cyApp.getCyroidEngine().getTimesForStop(item.getStop());
			
			lv.setAdapter(new ArrayAdapter<String>(mContext,R.layout.time_layout,  toDisplay));
			lv.setSelection(cyApp.getCyroidEngine().getNearestTimePos());

			dialog.setView(lv);
			
			

		} else if (item.isCurrentPosition() == true) {
			CharSequence[] tempChar = new CharSequence[2];
			tempChar[0] = "Latitude: " + item.getMyLocation().getLat();
			tempChar[1] = "Longitude: " + item.getMyLocation().getLon();
			item.setCharSeq(tempChar);
			dialog.setItems(item.getCharItem(), null);

		}

		dialog.setTitle(item.getTitle());

		dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// dialog.setCancelable(true);
		dialog.show();
		return true;
	}

	/**
	 * Returns the number of overlay items in list
	 */
	@Override
	public int size() {
		return mOverlays.size();
	}
	


}
