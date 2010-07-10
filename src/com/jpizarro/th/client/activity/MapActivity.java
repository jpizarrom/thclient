package com.jpizarro.th.client.activity;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.ResourceProxyImpl;
import org.andnav.osm.constants.OpenStreetMapConstants;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.util.OpenStreetMapRendererInfo;

import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MapActivity extends Activity  implements OpenStreetMapConstants{
	// ===========================================================
	// Constants
	// ===========================================================
	
	protected static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;

	private static final int MENU_MY_LOCATION = Menu.FIRST;
	private static final int MENU_MAP_MODE = MENU_MY_LOCATION + 1;
	private static final int MENU_SAMPLES = MENU_MAP_MODE + 1;
	private static final int MENU_ABOUT = MENU_SAMPLES + 1;
	private static final int MENU_ABANDON = MENU_ABOUT + 1;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SharedPreferences mPrefs;
	private OpenStreetMapView mOsmv;
	private MyLocationOverlay mLocationOverlay;
	private ResourceProxy mResourceProxy;
	private SampleLocationListener mLocationListener;
	private LocationManager mLocationManager;

	private GenericGameResponseTO genericGameResponseTO;
	
	private UpdateLocationTask updateLocationTask = new UpdateLocationTask();
	
	User user;

	// ===========================================================
	// Constructors
	// ===========================================================
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
    	mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        final RelativeLayout rl = new RelativeLayout(this);
        
        this.mOsmv = new OpenStreetMapView(this, OpenStreetMapRendererInfo.values()[mPrefs.getInt(PREFS_RENDERER, OpenStreetMapRendererInfo.MAPNIK.ordinal())]);
        this.mOsmv.setResourceProxy(mResourceProxy);
        this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(), this.mOsmv, mResourceProxy);
        this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);
        this.mOsmv.getOverlays().add(this.mLocationOverlay);
        rl.addView(this.mOsmv, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        this.setContentView(rl);
    
    	mOsmv.getController().setZoom(mPrefs.getInt(PREFS_ZOOM_LEVEL, 1));
    	mOsmv.scrollTo(mPrefs.getInt(PREFS_SCROLL_X, 0), mPrefs.getInt(PREFS_SCROLL_Y, 0));
    	
    	
    	
    	// register location listener
		initLocation();
    }
        
    @Override
    protected void onPause() {
    	SharedPreferences.Editor edit = mPrefs.edit();
    	edit.putInt(PREFS_RENDERER, mOsmv.getRenderer().ordinal());
    	edit.putInt(PREFS_SCROLL_X, mOsmv.getScrollX());
    	edit.putInt(PREFS_SCROLL_Y, mOsmv.getScrollY());
    	edit.putInt(PREFS_ZOOM_LEVEL, mOsmv.getZoomLevel());
    	edit.putBoolean(PREFS_SHOW_LOCATION, mLocationOverlay.isMyLocationEnabled());
    	edit.putBoolean(PREFS_FOLLOW_LOCATION, mLocationOverlay.isLocationFollowEnabled());
    	edit.commit();

    	this.mLocationOverlay.disableMyLocation();
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	mOsmv.setRenderer(OpenStreetMapRendererInfo.values()[mPrefs.getInt(PREFS_RENDERER, OpenStreetMapRendererInfo.MAPNIK.ordinal())]);
    	if(mPrefs.getBoolean(PREFS_SHOW_LOCATION, false))
    		this.mLocationOverlay.enableMyLocation();
    	this.mLocationOverlay.followLocation(mPrefs.getBoolean(PREFS_FOLLOW_LOCATION, true));
    	
    	user = (User)getIntent().getExtras().getSerializable("user");
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MY_LOCATION, Menu.NONE, "my_location");
		menu.add(0, MENU_ABANDON, Menu.NONE, "Abandon Game");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case MENU_MY_LOCATION:
			this.mLocationOverlay.followLocation(true);
    		this.mLocationOverlay.enableMyLocation();
    		Location lastFix = this.mLocationOverlay.getLastFix();
    		if (lastFix != null)
    			this.mOsmv.getController().setCenter(new GeoPoint(lastFix));
			return true;
		}
		return false;
	}
	
	private LocationManager getLocationManager() {
		if(this.mLocationManager == null)
			this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return this.mLocationManager; 
	}

	private void initLocation() {
		this.mLocationListener = new SampleLocationListener();
		getLocationManager().requestLocationUpdates(PROVIDER_NAME, 2000, 20, this.mLocationListener);
	}
	
	private void doUpdateLocation() {
		update();
	}
	
	private void launchUpdateLocationThread(int latitude, int longitude) {
		updateLocationTask.setLatitude(latitude);
		updateLocationTask.setLongitude(longitude);
		Thread updateLocationThread = new Thread(null, updateLocationTask, "UpdateLocationGame");
		updateLocationThread.start();
	}
	
	private void update() {

	}
	
	private class SampleLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location loc) {
			user.setLatitude((new GeoPoint(loc)).getLatitudeE6() );
			user.setLongitude((new GeoPoint(loc)).getLongitudeE6() );
			launchUpdateLocationThread(user.getLatitude(),
					user.getLongitude());
			
		}

		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class UpdateLocationHandler extends Handler {

		public UpdateLocationHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				CommonDialogs.errorMessage = sE.getMessage();
	        	if (sE.getCode() == ServerException.INSTANCE_NOT_FOUND_CODE)
					showDialog(CommonDialogs.CONNECTION_LOST_DIALOG_ID);
				else
					showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        	return;
	        }
        	Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		CommonDialogs.errorMessage = e.getLocalizedMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
	        GenericGameResponseTO gGRTO2 = 
				(GenericGameResponseTO)msg.getData().getSerializable("gGRTO");
			if (gGRTO2 != null) {
				genericGameResponseTO = gGRTO2;
				doUpdateLocation();
			}
		}
	}
	
	private class UpdateLocationTask implements Runnable {

		int latitude, longitude;
		HttpGameServiceImpl gameService;
		
		public int getLatitude() {
			return latitude;
		}

		public void setLatitude(int latitude) {
			this.latitude = latitude;
		}

		public int getLongitude() {
			return longitude;
		}

		public void setLongitude(int longitude) {
			this.longitude = longitude;
		}

		UpdateLocationTask() {
			gameService = new HttpGameServiceImpl();
		}

		public void run() {

			UpdateLocationHandler handler = 
				new UpdateLocationHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO gGRTO = 
					gameService.updateLocation(
							latitude, longitude);
				data.putSerializable("gGRTO", gGRTO);
				msg.setData(data);
				handler.sendMessage(msg);
				
			} catch (ServerException e) {
	        	data.putSerializable("ServerException", e);
	        	msg.setData(data);
				handler.sendMessage(msg);
	        } catch (Exception e) {
	        	data.putSerializable("Exception", e);
	        	msg.setData(data);
				handler.sendMessage(msg);
	        }
		}
	}
}
