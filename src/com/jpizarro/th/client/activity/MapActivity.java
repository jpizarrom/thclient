package com.jpizarro.th.client.activity;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.ResourceProxyImpl;
import org.andnav.osm.constants.OpenStreetMapConstants;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemTapListener;
import org.andnav.osm.views.util.OpenStreetMapRendererInfo;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.service.to.response.InGameUserInfoTO;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MapActivity extends Activity  implements OpenStreetMapConstants{
	// ===========================================================
	// Constants
	// ===========================================================
	
	protected static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;
	
	public static final int SEND_MESSAGE_REQUEST_CODE = 0;

	private static final int MENU_MY_LOCATION = Menu.FIRST;
	private static final int MENU_MAP_MODE = MENU_MY_LOCATION + 1;
	private static final int MENU_SAMPLES = MENU_MAP_MODE + 1;
	private static final int MENU_ABOUT = MENU_SAMPLES + 1;
	private static final int MENU_ABANDON = MENU_ABOUT + 1;
	
	private static final int USER_TAPPED_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SharedPreferences mPrefs;
	private OpenStreetMapView mOsmv;
	
	private TableLayout userTappedTable;
	
	private String tappedUser;
	
	// Overlays	
	private MyLocationOverlay mLocationOverlay;
	
	private ArrayList<OpenStreetMapViewOverlayItem> users;
	private OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem> mUsersOverlay;
	
	private List<HintOverlayItem> hints;
	private List<HintOverlayItem> hideHints;
	private OpenStreetMapViewItemizedOverlay<HintOverlayItem> mHintsOverlay;
	private OpenStreetMapViewItemizedOverlay<HintOverlayItem> mHideHintsOverlay;

	
	private ResourceProxy mResourceProxy;
	private SampleLocationListener mLocationListener;
	private LocationManager mLocationManager;

	private GenericGameResponseTO genericGameResponseTO;
	
	private StartGameTask startGameTask = new StartGameTask();
	private UpdateLocationTask updateLocationTask = new UpdateLocationTask();
	
	private User user;
		
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
        this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);
        
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
//    	if ( this.mLocationOverlay != null )
//	    	if(mPrefs.getBoolean(PREFS_SHOW_LOCATION, false))
//	    		this.mLocationOverlay.enableMyLocation();
//	    	this.mLocationOverlay.followLocation(mPrefs.getBoolean(PREFS_FOLLOW_LOCATION, true));
    	
    	user = (User)getIntent().getExtras().getSerializable("user");
    	
    	launchStartGameThread();
    }
    
    private void doStartGame() {
    	
    	/* MyLocationOverlay */
        {
        	if(this.mLocationOverlay == null){
		        this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(), this.mOsmv, mResourceProxy);
		        this.mOsmv.getOverlays().add(this.mLocationOverlay);
        	}
        }
        
        {
        	if( this.users == null )
	        	users = new ArrayList<OpenStreetMapViewOverlayItem>();
        	
        	if( this.mUsersOverlay == null ){
		        /* OnTapListener for the Markers, shows a simple Toast. */
		        this.mUsersOverlay = new OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem>(this, users, 
		        	this.getResources().getDrawable(R.drawable.person_red),
		        	null,
		        	new OpenStreetMapViewItemizedOverlay.OnItemTapListener<OpenStreetMapViewOverlayItem>(){
						@Override
						public boolean onItemTap(int index, OpenStreetMapViewOverlayItem item) {
//							Toast.makeText(MapActivity.this, "User '" + item.mTitle + "' (index=" + index + ") got tapped", Toast.LENGTH_LONG).show();
							try {
								tappedUser = item.mTitle;
								showDialog(USER_TAPPED_DIALOG_ID);
							} catch (Exception e) {
								CommonDialogs.errorMessage = e.getLocalizedMessage();
								showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
							}
							return true;
						}
			        }, 
			        mResourceProxy);
		        this.mOsmv.getOverlays().add(this.mUsersOverlay);
	        }
        }
        
        {
        	if( this.hints == null )
        		hints = new ArrayList<HintOverlayItem>();
        	
        	if( this.mHintsOverlay == null ){
		        /* OnTapListener for the Markers, shows a simple Toast. */
		        this.mHintsOverlay = new OpenStreetMapViewItemizedOverlay<HintOverlayItem>(this, hints,
		        	null,
		        	null,
		        	new OpenStreetMapViewItemizedOverlay.OnItemTapListener<HintOverlayItem>(){
						@Override
						public boolean onItemTap(int index, HintOverlayItem item) {
							Toast.makeText(MapActivity.this, item.toString(), Toast.LENGTH_LONG).show();
							return true; // We 'handled' this event.
						}
		        	}, 
		        	mResourceProxy);
		        this.mOsmv.getOverlays().add(this.mHintsOverlay);
	        }
        }
        
        {
        	if( this.hideHints == null )
        		hideHints = new ArrayList<HintOverlayItem>();
        	
        	if( this.mHideHintsOverlay == null ){
		        /* OnTapListener for the Markers, shows a simple Toast. */
		        this.mHideHintsOverlay = new OpenStreetMapViewItemizedOverlay<HintOverlayItem>(this, hideHints,
		        	null,
		        	null,
		        	new OpenStreetMapViewItemizedOverlay.OnItemTapListener<HintOverlayItem>(){
						@Override
						public boolean onItemTap(int index, HintOverlayItem item) {
							Toast.makeText(MapActivity.this, "Hide "+item.toString(), Toast.LENGTH_LONG).show();
							return true; // We 'handled' this event.
						}
		        	}, 
		        	mResourceProxy);
		        this.mOsmv.getOverlays().add(this.mHideHintsOverlay);
	        }
        }
        
        update();    	
    }
	private void launchStartGameThread() {
		startGameTask.setLogin(user.getUserName());
		Thread startGameThread = new Thread(null, startGameTask, "StartGame");
		startGameThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
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


	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case USER_TAPPED_DIALOG_ID:
			userTappedTable = new TableLayout(this);
        	fillTappedTable();
        	dialog.setContentView(userTappedTable);
        	return;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d = CommonDialogs.createDialog(id, this);
		if (d != null)
			return d;
		
		switch(id) {
			case USER_TAPPED_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle("User Info");
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
		}
		return null;
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
		if (genericGameResponseTO.getInGameUserInfoTOs().size() != 0) {
			for( InGameUserInfoTO in : genericGameResponseTO.getInGameUserInfoTOs() ){
				 users.add(new OpenStreetMapViewOverlayItem( in.getUsername(), "SampleDescription", 
						 new GeoPoint(in.getLatitude(), in.getLongitude())));
			}
		}
		if (genericGameResponseTO.getHints().size() != 0) {
			for( Hint in : genericGameResponseTO.getHints() ){
				 hints.add(new HintOverlayItem(in.getId(), in.getName(), in.getDescription(), 
						 new GeoPoint(in.getLatitude(), in.getLongitude())));
			}
		}
		if (genericGameResponseTO.getHideHints().size() != 0) {
			for( Hint in : genericGameResponseTO.getHideHints() ){
				 hideHints.add(new HintOverlayItem(in.getId(), in.getName(), in.getDescription(), 
						 new GeoPoint(in.getLatitude(), in.getLongitude())));
			}
		}
		
//		checkIncomingMessages();
		
		this.mOsmv.invalidate();

	}
	private void fillTappedTable() {
		userTappedTable.removeAllViews();
		TableRow tr;
		
		TextView tvName;
		
//		Toast.makeText(MapActivity.this, "User '" + tappedInfoTitle+" got tapped", Toast.LENGTH_LONG).show();

		InGameUserInfoTO p = genericGameResponseTO.getInGamePlayerInfoTO(
				tappedUser);
		
		tvName = new TextView(this);
		tvName.setText("Username :\t" + tappedUser );
		tvName.setWidth(260);
        tr = new TableRow(this);
        tr.addView(tvName);
        tr.setGravity(Gravity.CENTER);
        userTappedTable.addView(tr);
        
        Button bSendMessage;
        bSendMessage = new Button(this);
        bSendMessage.setText("Send Message");
        bSendMessage.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MapActivity.this, SendMessageActivity.class);
	        	i.putExtra("receiverUser", tappedUser);
	        	startActivityForResult(i, SEND_MESSAGE_REQUEST_CODE);
	        	try {
	        		dismissDialog(USER_TAPPED_DIALOG_ID);
	        	} catch (Exception e) {
	        		
	        	}
			}
		});
        tr = new TableRow(this);
        bSendMessage.setWidth(LayoutParams.FILL_PARENT);
        tr.addView(bSendMessage);
        tr.setGravity(Gravity.CENTER);
        userTappedTable.addView(tr);

		
		
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
	
	private class HintOverlayItem extends OpenStreetMapViewOverlayItem{
		private long id;
		public HintOverlayItem(long id, String aTitle, String aDescription,
				GeoPoint aGeoPoint) {
			super(aTitle, aDescription, aGeoPoint);
			this.id = id;
		}	

		public HintOverlayItem(String aTitle, String aDescription,
				GeoPoint aGeoPoint) {
			super(aTitle, aDescription, aGeoPoint);
			// TODO Auto-generated constructor stub
		}

		@Override
		public String toString() {
			return "HintOverlayItem [id=" + id + "," + " mTitle=" + mTitle +
					", mDescription=" + mDescription  + "]";
		}
		
	}
	
	private class StartGameHandler extends Handler {

		public StartGameHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			try {
				dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			} catch (Exception e) {
				
			}
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
	        		CommonDialogs.errorMessage = e.getMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
	        	GenericGameResponseTO sOCGRTO2 = 
				(GenericGameResponseTO)msg.getData().getSerializable("sOCGRTO");
			if (sOCGRTO2 != null) {
				genericGameResponseTO = sOCGRTO2;
				doStartGame();
			}
		}
		
	}
	private class StartGameTask implements Runnable {

		String login;
		HttpGameServiceImpl gameService;
		
		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		StartGameTask() {
			gameService = new HttpGameServiceImpl();
		}
		
		public void run() {
			
			StartGameHandler handler = new StartGameHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO sOCGRTO = 
					gameService.startOrContinueGame(login);
				data.putSerializable("sOCGRTO", sOCGRTO);
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
