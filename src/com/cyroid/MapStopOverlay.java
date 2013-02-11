package com.cyroid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;

import com.cyroid.data.CyroidLocation;
import com.cyroid.data.Route;
import com.cyroid.data.Stop;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapStopOverlay extends Overlay {

	private Route r;

	public MapStopOverlay(Route r) {
		this.setR(r);

	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		ArrayList<Point> mpoints = new ArrayList<Point>();

		ArrayList<Stop> stops = new ArrayList<Stop>();

		for (String stopKey : r.getStops().keySet()) {
			Stop s = r.getStops().get(stopKey);
			stops.add(s);
		}
		
		
		Collections.sort(stops, new Comparator<Stop>() {

			@Override
			public int compare(Stop arg0, Stop arg1) {
				if (arg0.getStopID().compareTo(arg1.getStopID()) > 0) {
					return 1;
				} else if (arg0.getStopID().compareTo(arg1.getStopID()) < 0) {
					return -11;
				}
				return 0;
			}

		});
		
		

		for (Stop stop : stops) {

			Point tpoint = new Point();
			mapView.getProjection().toPixels(
					stop.getLocation().getGeoLocation(), tpoint);
			mpoints.add(tpoint);
		}

		Path path = new Path();

		// Create a path from the points

		path.moveTo(mpoints.get(0).x, mpoints.get(0).y);
		Point previousPoint = mpoints.get(0);
		CyroidLocation temp = new CyroidLocation();
		CyroidLocation temp2 = new CyroidLocation();
		ArrayList<Rect> regions = new ArrayList<Rect>();
		for (int i = 1; i < mpoints.size(); i++) {
			
			if (temp.getCyroidLocation(
					mapView.getProjection().fromPixels(previousPoint.x,
							previousPoint.y)).getDistanceInFeet(
					temp2.getCyroidLocation(mapView.getProjection().fromPixels(
							mpoints.get(i).x, mpoints.get(i).y))) < 9000){
				for (Rect r : regions){
				
					if ( regions.size() > 0 && !r.contains(mpoints.get(i).x,mpoints.get(i).y)){
						path.lineTo(mpoints.get(i).x, mpoints.get(i).y);
					}
				}
				regions.add(new Rect(previousPoint.x,previousPoint.y,mpoints.get(i).x,mpoints.get(i).y));
				previousPoint= mpoints.get(i);
				
			}
			else {
				
				path.moveTo(mpoints.get(i).x, mpoints.get(i).y);
				//regions.add(new Rect(previousPoint.x,previousPoint.y,mpoints.get(i).x,mpoints.get(i).y));
				previousPoint= mpoints.get(i);
				
			}
				

		}
		
		/*
		ArrayList<Point> alreadyDrawnTo = new ArrayList<Point>();
		for (int i = 0; i < mpoints.size(); i++) {
			path.moveTo(mpoints.get(i).x, mpoints.get(i).y);
			alreadyDrawnTo.add(mpoints.get(i));
			for (int j = 0; j < mpoints.size(); j++){
				if (temp.getCyroidLocation(
						mapView.getProjection().fromPixels(mpoints.get(i).x,
								mpoints.get(i).y)).getDistanceInFeet(
						temp2.getCyroidLocation(mapView.getProjection().fromPixels(
								mpoints.get(j).x, mpoints.get(j).y))) < 1500){
					if (!alreadyDrawnTo.contains(mpoints.get(j))){
						path.lineTo(mpoints.get(j).x, mpoints.get(j).y);
						path.moveTo(mpoints.get(j).x, mpoints.get(j).y);
					}
					
				
				}
			}
		
			

		}
		*/

		Paint paint = new Paint();
		try {
			paint.setColor(Color.parseColor("#" + r.getRouteColor()));
		} catch (NumberFormatException e) {
			paint.setColor(Color.BLACK);
		}
		paint.setStrokeWidth(4);
		paint.setStyle(Style.STROKE);
		// Draw to the map
		canvas.drawPath(path, paint);

		return true;
	}

	// SETTER AND GETTERS
	public void setR(Route r) {
		this.r = r;
	}

	public Route getR() {
		return r;
	}

}
