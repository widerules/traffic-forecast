package vn.edu.hcmut.cse.trafficdirection.overlay;

import org.xml.sax.Parser;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import vn.edu.hcmut.cse.trafficdirection.main.MainActivity;
import vn.edu.hcmut.cse.trafficdirection.main.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Location;
import android.view.MotionEvent;
import android.widget.Toast;

public class MyPositionOverlay extends Overlay{
	private MainActivity  mainActivity;
	private MapView mapView;
	private boolean isDown = false;
	private long time = 0;
	/** Get the position location */
	public Location getLocation() {
		return location;
	}

	/** Set the position location */
	public void setLocation(Location location) {
		this.location = location;
	}

	Location location;

	public void GPXOverlay(MainActivity mainActivity, MapView mapView){
		this.mainActivity = mainActivity;
		this.mapView = mapView;
	}

	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		Double latitude=null;
		Double longitude=null;
		if (shadow == false) {
			if( location == null){
				
				latitude = 10.770579 * 1E6;
				longitude = 106.657963 * 1E6;
			}
			else
			{
				latitude = location.getLatitude() * 1E6;
				longitude = location.getLongitude() * 1E6;
			}
			GeoPoint geoPoint = new GeoPoint(latitude.intValue(),
					longitude.intValue());
			Point point = new Point();
			projection.toPixels(geoPoint, point);
			final float scale = mainActivity.getResources().getDisplayMetrics().density;
			int dip = (int) (8 * scale);
			Bitmap bmp1 = BitmapFactory.decodeResource(mainActivity.getResources(),
					R.drawable.startpoint);
			canvas.drawBitmap(bmp1, point.x - dip, point.y - dip, null);
		}
		super.draw(canvas, mapView, shadow);
	}

	public boolean onTap(GeoPoint point, MapView mapView) {
		return false;
	}
	
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		// ---when user lifts his finger---
		if(System.currentTimeMillis() - time > 1000 && isDown)
		{
			
			final Builder overlayDialog =new AlertDialog.Builder(mainActivity)
			.setIcon(R.drawable.alert_dialog_icon);
            overlayDialog.setTitle(R.string.alert_dialog_single_choice);
            overlayDialog.setSingleChoiceItems(R.array.select_dialog_items2, 0, new DialogInterface.OnClickListener() {
   
            	
                public void onClick(DialogInterface dialog, int whichButton) {
                	
                    /* User clicked on a radio button do some stuff */
                }
            })
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                	Toast.makeText(mainActivity.getApplicationContext(),"Build Route is Selected" +Integer.toString(whichButton),Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked No so do some stuff */
                }
            })
           .create();
            overlayDialog.show();
			
			isDown = false;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isDown = true;
			time = System.currentTimeMillis();
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			isDown = false;
		}
		return false;
	}
}