package com.jpizarro.th.client.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.ResourceProxyImpl;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.OpenStreetMapView.OpenStreetMapViewProjection;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapRendererFactory;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.model.service.to.response.InGameUserInfoTO;
import com.jpizarro.th.client.osm.OpenStreetMapConstants;
import com.jpizarro.th.lib.game.entity.GoalTO;
import com.jpizarro.th.lib.game.entity.HintTO;
import com.jpizarro.th.lib.game.entity.UserTO;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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
	private static final int MENU_UPDATE = MENU_ABANDON + 1;
	
	private static final int USER_TAPPED_USER_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID;
	private static final int USER_TAPPED_HINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 1;
	private static final int USER_TAPPED_HIDEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 2;
	private static final int USER_TAPPED_USERSEEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 3;
	private static final int USER_TAPPED_TEAMSEEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 4;
	
	private static final int METERS_TO_SEE = 150;
	private static final int METERS_TO_TAKE = 50;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SharedPreferences mPrefs;
	private OpenStreetMapView mOsmv;
	
//	private TableLayout userTappedTable, userTappedPlaceTable;
	
	private String tappedUser;
	private int tappedIdx;
	
	// Overlays	
	private MyLocationOverlay mLocationOverlay;
	
	private ArrayList<OpenStreetMapViewOverlayItem> users;
	private OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem> mUsersOverlay;
	
	private List<HintOverlayItem> hints;
	private OpenStreetMapViewItemizedOverlay<HintOverlayItem> mHintsOverlay;
	private Drawable mMarker1, mMarker2, mMarker3, mMarker4;

	
	private ResourceProxy mResourceProxy;
	private SampleLocationListener mLocationListener;
	private LocationManager mLocationManager;

	private GenericGameResponseTO genericGameResponseTO;
	
	private StartGameTask startGameTask = new StartGameTask();
	private UpdateLocationTask updateLocationTask = new UpdateLocationTask();
	private TakePlaceTask takePlaceTask = new TakePlaceTask();
	
	private UserTO user;
		
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
        
        this.mOsmv = new OpenStreetMapView(this, OpenStreetMapRendererFactory.MAPNIK);
        this.mOsmv.setResourceProxy(mResourceProxy);
        this.mOsmv.setBuiltInZoomControls(true);
        this.mOsmv.setMultiTouchControls(true);
        
        rl.addView(this.mOsmv, new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
                
        this.setContentView(rl);
    
    	mOsmv.getController().setZoom(mPrefs.getInt(PREFS_ZOOM_LEVEL, 1));
    	mOsmv.scrollTo(mPrefs.getInt(PREFS_SCROLL_X, 0), mPrefs.getInt(PREFS_SCROLL_Y, 0));
    	
    	// register location listener
//		initLocation();
    }
        
    @Override
    protected void onPause() {
    	SharedPreferences.Editor edit = mPrefs.edit();
    	edit.putString(PREFS_RENDERER, mOsmv.getRenderer().name());
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
    	final String rendererName = mPrefs.getString(PREFS_RENDERER, OpenStreetMapRendererFactory.MAPNIK.name());
        // TODO this will go wrong if you use a renderer that the factory doesn't know about
        final IOpenStreetMapRendererInfo renderer = OpenStreetMapRendererFactory.getRenderer(rendererName);
    	mOsmv.setRenderer(renderer);
    	
    	/* MyLocationOverlay */
        {
        	if(this.mLocationOverlay == null){
		        this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(), this.mOsmv, mResourceProxy)
		        {
		        	@Override
		        	protected void onDrawFinished(Canvas c, OpenStreetMapView osmv) {
		        		if(getMyLocation() != null) {
		        			final OpenStreetMapViewProjection pj = osmv.getProjection();
		        			Point mMapCoords = new Point();
		        			pj.toMapPixels(getMyLocation(), mMapCoords);
		        			final float radius = pj.metersToEquatorPixels(METERS_TO_SEE);
		        			
		        			this.mCirclePaint.setAlpha(50);
		        			this.mCirclePaint.setStyle(Style.STROKE);
		        			c.drawCircle(mMapCoords.x, mMapCoords.y, radius, this.mCirclePaint);
		        		}
		        	}
		        };
		        this.mOsmv.getOverlays().add(this.mLocationOverlay);
        	}
        }
    	if ( this.mLocationOverlay != null ){
	    	if(mPrefs.getBoolean(PREFS_SHOW_LOCATION, false))
	    		this.mLocationOverlay.enableMyLocation();

	    	this.mLocationOverlay.followLocation(mPrefs.getBoolean(PREFS_FOLLOW_LOCATION, true));
	    }
        // register location listener
		initLocation();
    	
    	user = (UserTO)getIntent().getExtras().getSerializable("user");

    	launchStartGameThread();
    }
    
    private void doStartGame() {
    	/* Drawable */
    	if ( this.mMarker1 == null )
    		this.mMarker1 = this.getResources().getDrawable(R.drawable.marker_black);
    	
    	if ( this.mMarker2 == null )
    		this.mMarker2 = this.getResources().getDrawable(R.drawable.marker_red);
    	
    	if ( this.mMarker3 == null )
    		this.mMarker3 = this.getResources().getDrawable(R.drawable.marker_yellow);
    	
    	if ( this.mMarker4 == null )
    		this.mMarker4 = this.getResources().getDrawable(R.drawable.marker_blue);
    	
        {
        	if( this.users == null )
	        	users = new ArrayList<OpenStreetMapViewOverlayItem>();
        	
        	if( this.mUsersOverlay == null ){
		        /* OnTapListener for the Markers, shows a simple Toast. */
		        this.mUsersOverlay = new OpenStreetMapViewItemizedOverlay<OpenStreetMapViewOverlayItem>(this, users, 
		        	this.getResources().getDrawable(R.drawable.person_red),
		        	null,
		        	new OpenStreetMapViewItemizedOverlay.OnItemTapListener<OpenStreetMapViewOverlayItem>(){

						public boolean onItemTap(int index, OpenStreetMapViewOverlayItem item) {
//							Toast.makeText(MapActivity.this, "User '" + item.mTitle + "' (index=" + index + ") got tapped", Toast.LENGTH_LONG).show();
							try {
								tappedUser = item.mTitle;
								showDialog(USER_TAPPED_USER_DIALOG_ID);
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

						public boolean onItemTap(int index, HintOverlayItem item) {
							try {
								tappedIdx = index;
								switch(item.type){
								case HintOverlayItem.ITEM_TEAM_HAVE:
									showDialog(USER_TAPPED_HINT_DIALOG_ID);
									break;
								case HintOverlayItem.ITEM_TEAM_SEE:
									showDialog(MapActivity.USER_TAPPED_TEAMSEEHINT_DIALOG_ID);
									break;
								case HintOverlayItem.ITEM_USER_SEE:
									showDialog(MapActivity.USER_TAPPED_USERSEEHINT_DIALOG_ID);
									break;
								case HintOverlayItem.ITEM_GOAL:
									break;
								case HintOverlayItem.ITEM_HIDE:
								default:
//									showDialog(MapActivity.USER_TAPPED_HIDEHINT_DIALOG_ID);
								}
								
							} catch (Exception e) {
								CommonDialogs.errorMessage = e.getLocalizedMessage();
								showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
							}
							return true;
						}
		        	}, 
		        	mResourceProxy){
		        	protected void onDrawItem(final Canvas c, final int index, final Point curScreenCoords) {
		        		Drawable m = this.mMarker;
		        		final int left = curScreenCoords.x - this.mMarkerHotSpot.x;
		        		final int right = left + this.mMarkerWidth;
		        		final int top = curScreenCoords.y - this.mMarkerHotSpot.y;
		        		final int bottom = top + this.mMarkerHeight;
		        		
		        		HintOverlayItem item = mItemList.get(index);
		        		switch(item.type){
						case HintOverlayItem.ITEM_TEAM_HAVE:
							m = mMarker1;
							break;
						case HintOverlayItem.ITEM_TEAM_SEE:
							m = mMarker2;
							break;
						case HintOverlayItem.ITEM_USER_SEE:
							m = mMarker3;
							break;
						case HintOverlayItem.ITEM_GOAL:
							m = mMarker4;
							break;
						case HintOverlayItem.ITEM_HIDE:
//							return;
						default:
							m = this.mMarker;
						}

		        		m.setBounds(left, top, right, bottom);
		        		m.draw(c);
		        	}
		        };
		        this.mOsmv.getOverlays().add(this.mHintsOverlay);
	        }
        }
        
        update();
        
     // register location listener
//		initLocation();
    }
	private void launchStartGameThread() {
		startGameTask.setLogin(user.getUsername());
		Thread startGameThread = new Thread(null, startGameTask, "StartGame");
		startGameThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MY_LOCATION, Menu.NONE, R.string.my_location);
		menu.add(0, MENU_ABANDON, Menu.NONE, R.string.abandon_game);
		menu.add(0, MENU_UPDATE, Menu.NONE, R.string.update);
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
		case MENU_UPDATE:
			this.launchStartGameThread();
			return true;
		}
		return false;
	}


	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case USER_TAPPED_USER_DIALOG_ID:
//			userTappedTable = new TableLayout(this);
        	fillTappedUserTable(dialog);
//        	dialog.setContentView(userTappedTable);
        	return;
		case USER_TAPPED_HINT_DIALOG_ID:
		case USER_TAPPED_USERSEEHINT_DIALOG_ID:
//			userTappedPlaceTable = new TableLayout(this);
        	fillTappedPlaceTable(dialog, id);
//        	dialog.setContentView(userTappedPlaceTable);
        	return;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d = CommonDialogs.createDialog(id, this);
		if (d != null)
			return d;
		
		switch(id) {
			case USER_TAPPED_USER_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_user);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
			case USER_TAPPED_HINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_hint);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
			case USER_TAPPED_HIDEHINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_hint);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
			case USER_TAPPED_USERSEEHINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_hint);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
			case USER_TAPPED_TEAMSEEHINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_hint);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode) {
		case SEND_MESSAGE_REQUEST_CODE:
			switch(resultCode) {
			case RESULT_OK:
				Toast.makeText(this, R.string.message_sent, Toast.LENGTH_LONG).show();
				break;
			}
			break;
		}
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
		hints.clear();
		
		if (genericGameResponseTO.getGoal() != null) {
			GoalTO in = genericGameResponseTO.getGoal() ;
			GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
			int type = HintOverlayItem.ITEM_HIDE;
			if ( g.distanceTo(new GeoPoint(user.getLatitude(), user.getLongitude())) < METERS_TO_SEE )
				type = HintOverlayItem.ITEM_USER_SEE;
			if ( g.distanceTo(new GeoPoint(user.getLatitude(), user.getLongitude())) < METERS_TO_TAKE )
				type = HintOverlayItem.ITEM_GOAL;
			hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
					g, type));
		}

		if (genericGameResponseTO.getHints().size() != 0) {
			for( HintTO in : genericGameResponseTO.getHints() ){
				GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
				 hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
						g, HintOverlayItem.ITEM_TEAM_HAVE));
			}
		}
		
		if (genericGameResponseTO.getHideHints().size() != 0) {
			for( HintTO in : genericGameResponseTO.getHideHints() ){
				GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
				int type = HintOverlayItem.ITEM_HIDE;
				if ( g.distanceTo(new GeoPoint(user.getLatitude(), user.getLongitude())) < METERS_TO_SEE )
					type = HintOverlayItem.ITEM_USER_SEE;
				hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
						 new GeoPoint(in.getLatitude(), in.getLongitude()), type));
			}
		}
		
		if (genericGameResponseTO.getUserSeeHintTOList().size() != 0) {
			for( HintTO in : genericGameResponseTO.getUserSeeHintTOList() ){
				hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
						 new GeoPoint(in.getLatitude(), in.getLongitude()), HintOverlayItem.ITEM_USER_SEE));
			}
		}
		
		if (genericGameResponseTO.getTeamSeeHintTOList().size() != 0) {
			for( HintTO in : genericGameResponseTO.getTeamSeeHintTOList() ){
				hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
						 new GeoPoint(in.getLatitude(), in.getLongitude()), HintOverlayItem.ITEM_TEAM_SEE));
			}
		}
		
//		checkIncomingMessages();
		
		this.mOsmv.invalidate();

	}
	private void fillTappedUserTable(Dialog dialog) {
		dialog.setContentView(R.layout.dialog_show_user);
		TextView tv;
		Button bt;
		
//		Toast.makeText(MapActivity.this, "User '" + tappedInfoTitle+" got tapped", Toast.LENGTH_LONG).show();

//		InGameUserInfoTO p = genericGameResponseTO.getInGamePlayerInfoTO(
//				tappedUser);
		
		tv = (TextView) dialog.findViewById(R.id.dsu_tv_username);
		tv.setText( tappedUser );
        
        bt = (Button) dialog.findViewById(R.id.dsu_bt_sendmsg);
        bt.setOnClickListener(new android.view.View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(MapActivity.this, SendMessageActivity.class);
	        	i.putExtra("receiverUser", tappedUser);
	        	startActivityForResult(i, SEND_MESSAGE_REQUEST_CODE);
	        	try {
	        		dismissDialog(USER_TAPPED_USER_DIALOG_ID);
	        	} catch (Exception e) {
	        		
	        	}
			}
		});
        
	}
	
	private void fillTappedPlaceTable(Dialog d, int userTappedHintDialogId) {
		// TODO Auto-generated method stub
		TextView tv;
		
//		Toast.makeText(MapActivity.this, "User '" + tappedInfoTitle+" got tapped", Toast.LENGTH_LONG).show();

//		InGameUserInfoTO p = genericGameResponseTO.getInGamePlayerInfoTO(
//				tappedUser);
		switch(userTappedHintDialogId){
		case USER_TAPPED_HINT_DIALOG_ID:
			d.setContentView(R.layout.dialog_show_hint);
			HintOverlayItem h = this.hints.get(tappedIdx);
			
			tv = (TextView) d.findViewById(R.id.dsh_name);
			tv.setText(h.mTitle );
			
			tv = (TextView) d.findViewById(R.id.dsh_desc);
			tv.setText(h.mDescription );
			break;

		case USER_TAPPED_USERSEEHINT_DIALOG_ID:
			d.setContentView(R.layout.dialog_take_hint);
			Button bAct;
			bAct = (Button)d.findViewById(R.id.dth_bt_take);
			bAct.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View v) {
					try {
						launchTakePlaceThread(tappedIdx);
						dismissDialog(USER_TAPPED_USERSEEHINT_DIALOG_ID);
					} catch (Exception e) {}
				}
			});
			break;
		}
		
	}
	
	private void launchTakePlaceThread(int tappedIdx2) {
		// TODO Auto-generated method stub
		takePlaceTask.setPlaceId(hints.get(tappedIdx2).id);
		Thread takePlaceThread = new Thread(null, takePlaceTask, "StartGame");
		takePlaceThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
		
	}

	private class SampleLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			user.setLatitude((new GeoPoint(loc)).getLatitudeE6() );
			user.setLongitude((new GeoPoint(loc)).getLongitudeE6() );
			launchUpdateLocationThread(user.getLatitude(),
					user.getLongitude());
			
		}

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class HintOverlayItem extends OpenStreetMapViewOverlayItem implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8292145846143000436L;
		private long id;
		
		private static final int ITEM_TEAM_HAVE = 0;
		private static final int ITEM_USER_SEE = 1;
		private static final int ITEM_TEAM_SEE = 2;
		private static final int ITEM_GOAL = 3;
		private static final int ITEM_HIDE = Integer.MIN_VALUE;
		
		private int type;
		
		public HintOverlayItem(long id, String aTitle, String aDescription,
				GeoPoint aGeoPoint) {
			super(aTitle, aDescription, aGeoPoint);
			this.id = id;
			this.type = ITEM_HIDE;
		}	
		
		public HintOverlayItem(long id, String aTitle, String aDescription,
				GeoPoint aGeoPoint, int type) {
			super(aTitle, aDescription, aGeoPoint);
			this.id = id;
			this.type = type;
		}

//		public HintOverlayItem(String aTitle, String aDescription,
//				GeoPoint aGeoPoint) {
//			super(aTitle, aDescription, aGeoPoint);
//			// TODO Auto-generated constructor stub
//		}

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
	
	private class TakePlaceHandler extends Handler {

		public TakePlaceHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			try {
				dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			} catch (Exception e) {}
			
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
					doTakePlace();
				}
		}
	}
	private class TakePlaceTask implements Runnable {

		long placeId;
		int latitude, longitude;
		HttpGameServiceImpl gameService;
		
		public long getPlaceId() {
			return placeId;
		}

		public void setPlaceId(long placeId) {
			this.placeId = placeId;
		}

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

		TakePlaceTask() {
			gameService = new HttpGameServiceImpl();
		}

		public void run() {

			TakePlaceHandler handler = 
				new TakePlaceHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO gGRTO = 
					gameService.takePlace(placeId, latitude, longitude);
				
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
	public void doTakePlace() {
		// TODO Auto-generated method stub
		update();
		
	}
}
