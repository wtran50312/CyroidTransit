package com.cyroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;

import com.cyroid.data.CyroidApplicationV2;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyRadiusOverlay extends Overlay {
	
	
	CyroidApplicationV2 cyApp;
	public MyRadiusOverlay(CyroidApplicationV2 CyApp){
		cyApp = CyApp;
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		
		Point userLocation = new Point();
		
		mapView.getProjection().toPixels(cyApp.getMyLocat().getGeoLocation(),userLocation);
		
	
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		paint.setAlpha(10);
		paint.setStyle(Style.FILL_AND_STROKE);
		
		float feetToMeter =   (cyApp.getCyroidEngine().getData().getConfig().getRadius()+1600) / (float) 3.2808399  ;
		float radius = mapView.getProjection().metersToEquatorPixels(feetToMeter);
		
		canvas.drawCircle(userLocation.x, userLocation.y, radius, paint);
		
		return true;
		
	}
	
}
