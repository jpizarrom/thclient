package com.jpizarro.th.client.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.andnav.osm.ResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.OverlayItem.HotspotPlace;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.GameService;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.osm.OpenStreetMapConstants;
import com.jpizarro.th.client.util.CustomAPP;
import com.jpizarro.th.lib.game.entity.GoalTO;
import com.jpizarro.th.lib.game.entity.HintTO;
import com.jpizarro.th.lib.game.entity.PlaceTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.UserTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;
import com.jpizarro.th.lib.game.entity.response.InGameUserInfoTO;

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
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

public class MapActivity extends Activity implements OpenStreetMapConstants{
	protected static final Logger LOG = Logger.getLogger(GenericGameResponseTO.class.getCanonicalName());
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
	private static final int MENU_LAST_ID = MENU_UPDATE + 1; // Always set to last unused id
	
	private static final int USER_TAPPED_USER_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID;
	
	private static final int USER_TAPPED_HINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 1;
	private static final int USER_TAPPED_HIDEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 2;
	private static final int USER_TAPPED_USERSEEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 3;
	private static final int USER_TAPPED_TEAMSEEHINT_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID + 4;
	
	private static final int USER_TAPPED_HIDEGOAL_DIALOG_ID = USER_TAPPED_TEAMSEEHINT_DIALOG_ID + 1;
	private static final int USER_TAPPED_USERSEEGOAL_DIALOG_ID = USER_TAPPED_HIDEGOAL_DIALOG_ID + 1;
	private static final int USER_TAPPED_GOAL_DIALOG_ID = USER_TAPPED_USERSEEGOAL_DIALOG_ID + 1;
	
	private int METERS_TO_SEE = 100;
	private int METERS_TO_TAKE = 50;
	private int MIN_DISTANCE = 5;
	private boolean SHOW_HIDE = false;
	private boolean SHOW_LOAD_DIALOG = false;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private SharedPreferences mPrefs;
	private MapView mOsmv;
	
//	private TableLayout userTappedTable, userTappedPlaceTable;
	
	private String tappedUser;
	private int tappedIdx;
	
	// Overlays	
	private MyLocationOverlay mLocationOverlay;
	
	private ArrayList<OverlayItem> users;
	private ItemizedOverlay<OverlayItem> mUsersOverlay;
	
	private List<HintOverlayItem> hints;
	private THItemizedIconOverlay<HintOverlayItem> mHintsOverlay;
	private Drawable mMarker1, mMarker2, mMarker3, mMarker4;

	
	private ResourceProxy mResourceProxy;
	private SampleLocationListener mLocationListener;
	private LocationManager mLocationManager;

	private GenericGameResponseTO genericGameResponseTO;
	
	private StartGameTask startGameTask;
	private UpdateLocationTask updateLocationTask;
	private TakePlaceTask takePlaceTask;
	
	private UserTO user;
	private TeamTO team;
	Location curLoc = null;
	Location selPlace = null;
		
	// ===========================================================
	// Constructors
	// ===========================================================
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences settings = PreferenceManager
		.getDefaultSharedPreferences(getApplicationContext());
        METERS_TO_SEE = Integer.valueOf(settings.getString("meters_to_see", "100"));
        SHOW_HIDE = settings.getBoolean("show_hide", false);
        MIN_DISTANCE = Integer.valueOf(settings.getString("min_distance", "5"));
        SHOW_LOAD_DIALOG = settings.getBoolean("show_load_dialog", false);

        
    	startGameTask = new StartGameTask();
    	updateLocationTask = new UpdateLocationTask();
    	takePlaceTask = new TakePlaceTask();

        mResourceProxy = new ResourceProxyImpl(getApplicationContext());
        
    	mPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        final RelativeLayout rl = new RelativeLayout(this);
        
        this.mOsmv = new MapView(this, 256, mResourceProxy);
//        this.mOsmv.setResourceProxy(mResourceProxy);
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
//    	getLocationManager().removeUpdates(mLocationListener);
    	
    	SharedPreferences.Editor edit = mPrefs.edit();
    	edit.putString(PREFS_RENDERER, mOsmv.getTileProvider().getTileSource().name());
    	edit.putInt(PREFS_SCROLL_X, mOsmv.getScrollX());
    	edit.putInt(PREFS_SCROLL_Y, mOsmv.getScrollY());
    	edit.putInt(PREFS_ZOOM_LEVEL, mOsmv.getZoomLevel());
    	edit.putBoolean(PREFS_SHOW_LOCATION, mLocationOverlay.isMyLocationEnabled());
    	edit.putBoolean(PREFS_FOLLOW_LOCATION, mLocationOverlay.isFollowLocationEnabled());
    	edit.commit();

    	disableLocation();
    	this.mLocationOverlay.disableMyLocation();
    	this.mLocationOverlay.disableCompass();
    	
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	final String tileSourceName = mPrefs.getString(PREFS_RENDERER, TileSourceFactory.DEFAULT_TILE_SOURCE.name());
		try {
			final ITileSource tileSource = TileSourceFactory.getTileSource(tileSourceName);
			mOsmv.setTileSource(tileSource);
		} catch (final IllegalArgumentException ignore) {
		}
    	
    	/* MyLocationOverlay */
        {
        	if(this.mLocationOverlay == null){
		        this.mLocationOverlay = new MyLocationOverlay(this.getBaseContext(), this.mOsmv, mResourceProxy)
		        {
		        	private static final boolean DEBUGMODE = true;
		        	
		        	@Override
					public void draw(Canvas c, MapView osmv, boolean arg2) {
						// TODO Auto-generated method stub
						super.draw(c, osmv, arg2);
						onDrawFinished(c, osmv);
					}

//					@Override
		        	protected void onDrawFinished(Canvas c, MapView osmv) {
		        		if(getMyLocation() != null) {
	                        final Projection pj = osmv.getProjection();
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

		        
		        final Handler handler = new Handler();
                mLocationOverlay.runOnFirstFix(new Runnable() {
                        @Override
                        public void run() {
                                handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                                Toast.makeText(getApplicationContext(),
                                                                "runOnFirstFix ",
                                                                Toast.LENGTH_LONG).show();
                                                curLoc = MapActivity.this.mLocationOverlay.getLastFix();
                                                launchStartGameThread();
                                        }
                                });
                        }
                });
        	}
        }
//		if (mPrefs.getBoolean(PREFS_SHOW_LOCATION, false)) {
//			this.mLocationOverlay.enableMyLocation();
//		}
		if (mPrefs.getBoolean(PREFS_SHOW_COMPASS, false)) {
			this.mLocationOverlay.enableCompass();
		}
    	if ( this.mLocationOverlay != null ){
	    	if(mPrefs.getBoolean(PREFS_SHOW_LOCATION, false)){
	    		this.mLocationOverlay.enableMyLocation();
	    	}

//	    	this.mLocationOverlay.followLocation(mPrefs.getBoolean(PREFS_FOLLOW_LOCATION, true));
	    }
   	
    	user = (UserTO)getIntent().getExtras().getSerializable("user");
    	team = (TeamTO)getIntent().getExtras().getSerializable("team");
    	
        // register location listener
//		initLocation();
    	enableLocation();

//    	launchStartGameThread();
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
    	
//        {
//        	if( this.users == null )
//	        	users = new ArrayList<OverlayItem>();
//        	
//        	if( this.mUsersOverlay == null ){
//		        /* OnTapListener for the Markers, shows a simple Toast. */
//		        this.mUsersOverlay = new ItemizedOverlay<OverlayItem>(this, users, 
////		        	this.getResources().getDrawable(R.drawable.person_red),
//		        	null,
//		        	null,
//		        	new ItemizedOverlay.OnItemGestureListener<OverlayItem>(){
//
//						public boolean onItemTap(int index, OverlayItem item) {
////							Toast.makeText(MapActivity.this, "User '" + item.mTitle + "' (index=" + index + ") got tapped", Toast.LENGTH_LONG).show();
//							try {
//								tappedUser = item.mTitle;
//								showDialog(USER_TAPPED_USER_DIALOG_ID);
//							} catch (Exception e) {
//								CommonDialogs.errorMessage = e.getLocalizedMessage();
//								showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
//							}
//							return true;
//						}
//
//						@Override
//						public boolean onItemLongPress(int arg0,
//								OverlayItem arg1) {
//							// TODO Auto-generated method stub
//							return false;
//						}
//
//						@Override
//						public boolean onItemSingleTapUp(int arg0,
//								OverlayItem arg1) {
//							// TODO Auto-generated method stub
//							return false;
//						}
//			        }, 
//			        mResourceProxy);
//		        this.mOsmv.getOverlays().add(this.mUsersOverlay);
//	        }
//        }
        
//        {
        	if( this.hints == null )
        		hints = new ArrayList<HintOverlayItem>();
//        	
        	if( this.mHintsOverlay == null ){
		        /* OnTapListener for the Markers, shows a simple Toast. */
		        this.mHintsOverlay = new THItemizedIconOverlay<HintOverlayItem>(
//		        	this,
		        	hints,
//		        	mMarker4,
//		        	null,
//		        	null,
		        	new ItemizedIconOverlay.OnItemGestureListener<HintOverlayItem>(){
//
		        		@Override
						public boolean onItemSingleTapUp(int index, HintOverlayItem item) {
		        			LOG.info("onItemSingleTapUp "+item.type);
							try {
								tappedIdx = index;
								switch(item.type){
								case HintOverlayItem.TEAM_HAVE:
//									showDialog(USER_TAPPED_HINT_DIALOG_ID);
									if (item.getPlace().getType().equals(HintTO.TYPE))
										showDialog(MapActivity.USER_TAPPED_HINT_DIALOG_ID);
									else
										showDialog(MapActivity.USER_TAPPED_GOAL_DIALOG_ID);
									break;
								case HintOverlayItem.TEAM_SEE:
//									showDialog(MapActivity.USER_TAPPED_TEAMSEEHINT_DIALOG_ID);
									if (item.getPlace().getType().equals(HintTO.TYPE))
										showDialog(MapActivity.USER_TAPPED_TEAMSEEHINT_DIALOG_ID);
									else
										showDialog(MapActivity.USER_TAPPED_TEAMSEEHINT_DIALOG_ID);
									break;
								case HintOverlayItem.USER_SEE:
//									showDialog(MapActivity.USER_TAPPED_USERSEEHINT_DIALOG_ID);
									if (item.getPlace().getType().equals(HintTO.TYPE))
										showDialog(MapActivity.USER_TAPPED_USERSEEHINT_DIALOG_ID);
									else
										showDialog(MapActivity.USER_TAPPED_USERSEEGOAL_DIALOG_ID);
									break;
//								case HintOverlayItem.ITEM_GOAL:
//									break;
								case HintOverlayItem.HIDE:
									if (item.getPlace().getType().equals(HintTO.TYPE))
										showDialog(MapActivity.USER_TAPPED_HIDEHINT_DIALOG_ID);
									else
										showDialog(MapActivity.USER_TAPPED_HIDEGOAL_DIALOG_ID);
								default:
//									showDialog(MapActivity.USER_TAPPED_HIDEHINT_DIALOG_ID);
								}
//								
							} catch (Exception e) {
								CommonDialogs.errorMessage = e.getLocalizedMessage();
								showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
							}
							return true;
						}

						@Override
						public boolean onItemLongPress(int index, HintOverlayItem item) {
							// TODO Auto-generated method stub
							return false;
						}

		        	}, 
		        	mResourceProxy){
		        	@Override
//		        	protected void onDrawItem(final Canvas c, final int index, final Point curScreenCoords) {
		        	protected void onDrawItem(final Canvas canvas, final HintOverlayItem item, final Point curScreenCoords) {
		        		final HotspotPlace hotspot = item.getMarkerHotspot();
		        		Drawable m;
//		        		final HotspotPlace hotspot = item.getMarkerHotspot();
//		        		final int left = curScreenCoords.x - this..mMarkerHotSpot.x;
//		        		final int right = left + this.mMarkerWidth;
//		        		final int top = curScreenCoords.y - this.mMarkerHotSpot.y;
//		        		final int bottom = top + this.mMarkerHeight;
//		        		
//		        		HintOverlayItem item = mItemList.get(index);
		        		switch(item.type){
//						case HintOverlayItem.ITEM_TEAM_HAVE:
//							m = mMarker1;
//							break;
//						case HintOverlayItem.ITEM_TEAM_SEE:
//							m = mMarker2;
//							break;
//						case HintOverlayItem.ITEM_USER_SEE:
//							m = mMarker3;
//							break;
//						case HintOverlayItem.ITEM_GOAL:
//							m = mMarker4;
//							break;
						case HintOverlayItem.HIDE:
							if (!SHOW_HIDE)
								return;
						default:
							m = this.mDefaultMarker;
						}
//		        		super.onDrawItem(canvas, item, curScreenCoords);
		        		m = boundToHotspot(m, hotspot);
//////		        		m.setBounds(left, top, right, bottom);
////		        		m..getDrawable().draw(canvas);
//		        		// draw it
//		                Overlay.drawAt(canvas, m, curScreenCoords.x, curScreenCoords.y, false);
		        		drawAt(canvas, m, curScreenCoords.x, curScreenCoords.y, false);
		        		
		        	}

		        };
		        this.mOsmv.getOverlays().add(this.mHintsOverlay);
	        }
//        }
        
        update();
        
     // register location listener
//		initLocation();
    }
	private void launchStartGameThread() {
		if (SHOW_LOAD_DIALOG)
			showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
		startGameTask.setLogin(String.valueOf(user.getUserId()));
		Thread startGameThread = new Thread(null, startGameTask, "StartGame");
		startGameThread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_MY_LOCATION, Menu.NONE, R.string.my_location);
		menu.add(0, MENU_ABANDON, Menu.NONE, R.string.abandon_game);
		menu.add(0, MENU_UPDATE, Menu.NONE, R.string.update);
		// Put overlay items next
		mOsmv.getOverlayManager().onCreateOptionsMenu(menu, MENU_LAST_ID, mOsmv);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(final Menu pMenu) {
		mOsmv.getOverlayManager().onPrepareOptionsMenu(pMenu, MENU_LAST_ID, mOsmv);
		return super.onPrepareOptionsMenu(pMenu);
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
		default:
			return mOsmv.getOverlayManager().onOptionsItemSelected(item, MENU_LAST_ID,
					mOsmv);
//			return false;
		}
	}


	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id) {
		case USER_TAPPED_USER_DIALOG_ID:
//			userTappedTable = new TableLayout(this);
        	fillTappedUserTable(dialog);
//        	dialog.setContentView(userTappedTable);
        	return;
		case USER_TAPPED_GOAL_DIALOG_ID:
		case USER_TAPPED_USERSEEGOAL_DIALOG_ID:
			
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
	        
			case USER_TAPPED_GOAL_DIALOG_ID:
			case USER_TAPPED_HINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	d.setTitle(R.string.info_hint);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
	        	
			case USER_TAPPED_HIDEGOAL_DIALOG_ID:
			case USER_TAPPED_HIDEHINT_DIALOG_ID:
	        	d = new Dialog(this);
	        	if (id == USER_TAPPED_HIDEHINT_DIALOG_ID)
	        		d.setTitle(R.string.info_hidehint);
	        	else
	        		d.setTitle(R.string.info_hidegoal);
	        	d.setCanceledOnTouchOutside(true);
	        	return d;
	        	
			case USER_TAPPED_USERSEEGOAL_DIALOG_ID:
			case USER_TAPPED_USERSEEHINT_DIALOG_ID:
	        	d = new Dialog(this);
//	        	d.setTitle(R.string.info_hint);
	        	if (id == USER_TAPPED_USERSEEHINT_DIALOG_ID)
	        		d.setTitle(R.string.info_hint);
	        	else
	        		d.setTitle(R.string.info_goal);
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
	@Override
	public boolean onTrackballEvent(final MotionEvent event) {
		return this.mOsmv.onTrackballEvent(event);
	}
	private LocationManager getLocationManager() {
		if(this.mLocationManager == null)
			this.mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return this.mLocationManager; 
	}

	private void enableLocation() {
//		mMyLocationEnabled = true;
		this.mLocationListener = new SampleLocationListener();
		getLocationManager().requestLocationUpdates(PROVIDER_NAME, 3000, MIN_DISTANCE, this.mLocationListener);
	}
	private void disableLocation() {
//		mMyLocationEnabled = false;
		if (this.mLocationListener!=null)
			getLocationManager().removeUpdates(this.mLocationListener);
		
	}
	
	private void doUpdateLocation() {
		update();
	}
	
	private void launchUpdateLocationThread() {
		launchUpdateLocationThread(
				(new GeoPoint(curLoc)).getLatitudeE6() ,
				(new GeoPoint(curLoc)).getLongitudeE6()
				);
		
	}
	private void launchUpdateLocationThread(Location loc) {
		curLoc = loc;
		launchUpdateLocationThread();
		
	}
	private void launchUpdateLocationThread(int latitude, int longitude) {
		LOG.info("launchUpdateLocationThread");
		if (!updateLocationTask.onUpdate){
			updateLocationTask.onUpdate = true;
			updateLocationTask.setLatitude(latitude);
			updateLocationTask.setLongitude(longitude);
			Thread updateLocationThread = new Thread(null, updateLocationTask, "UpdateLocationGame");
			updateLocationThread.start();
		}
	}
	
	private void update() {
		if (curLoc == null)
			return;
		if ( genericGameResponseTO.isHasFinished() ){
			Toast.makeText(getApplicationContext(),
                    "genericGameResponseTO.isHasFinished()",
                    Toast.LENGTH_LONG).show();
			this.disableLocation();
			return;
			}

		GeoPoint gp = new GeoPoint(curLoc);
		
		LOG.info("-----------------------------");
		if (genericGameResponseTO.getInGameUserInfoTOs().size() != 0) {
			for( InGameUserInfoTO in : genericGameResponseTO.getInGameUserInfoTOs() ){
//				 users.add(new OverlayItem( in.getUsername(), "SampleDescription", 
//						 new GeoPoint(in.getLatitude(), in.getLongitude())));
			}
		}
//		hints.clear();
		this.mHintsOverlay.removeAllItems();
		
		System.out.println("genericGameResponseTO.getGoals().size(): "+genericGameResponseTO.getGoals().size());
		if (genericGameResponseTO.getGoals() != null && genericGameResponseTO.getGoals().size() > 0) {
			for( GoalTO in : genericGameResponseTO.getGoals() ){
				GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
				int distanceTo = g.distanceTo(new GeoPoint(gp.getLatitudeE6(), gp.getLongitudeE6()));
				LOG.info("distanceTo" +distanceTo);
				
				int type = HintOverlayItem.HIDE;
				if ( distanceTo < METERS_TO_SEE )
					type = HintOverlayItem.USER_SEE;
//				if ( distanceTo < METERS_TO_TAKE )
//					type = HintOverlayItem.USER_SEE;
				this.mHintsOverlay.addItem(new HintOverlayItem(in, type));
			}
		}
//
		System.out.println("genericGameResponseTO.getHints().size(): "+genericGameResponseTO.getHints().size());
		if (genericGameResponseTO.getHints().size() != 0) {
			for( HintTO in : genericGameResponseTO.getHints() ){
//				GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
				this.mHintsOverlay.addItem(new HintOverlayItem(in, HintOverlayItem.TEAM_HAVE));
			}
		}
		System.out.println("genericGameResponseTO.getHideHints().size(): "+genericGameResponseTO.getHideHints().size());
		if (genericGameResponseTO.getHideHints().size() != 0) {
			for( HintTO in : genericGameResponseTO.getHideHints() ){
				GeoPoint g = new GeoPoint(in.getLatitude(), in.getLongitude());
				int distanceTo = g.distanceTo(new GeoPoint(gp.getLatitudeE6(), gp.getLongitudeE6()));
				LOG.info("distanceTo" +distanceTo);
				
				int type = HintOverlayItem.HIDE;
				if ( distanceTo < METERS_TO_SEE )
					type = HintOverlayItem.USER_SEE;
//				GeoPoint point = new GeoPoint(in.getLatitude(), in.getLongitude());
//				System.out.println("point: "+point);
				this.mHintsOverlay.addItem(new HintOverlayItem(in, type));
				
			}
		}
//		
//		if (genericGameResponseTO.getUserSeeHintTOList().size() != 0) {
//			for( HintTO in : genericGameResponseTO.getUserSeeHintTOList() ){
//				hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
//						 new GeoPoint(in.getLatitude(), in.getLongitude()), HintOverlayItem.ITEM_USER_SEE));
//			}
//		}
//		
//		if (genericGameResponseTO.getTeamSeeHintTOList().size() != 0) {
//			for( HintTO in : genericGameResponseTO.getTeamSeeHintTOList() ){
//				hints.add(new HintOverlayItem(in.getPlaceId(), in.getName(), in.getDescription(), 
//						 new GeoPoint(in.getLatitude(), in.getLongitude()), HintOverlayItem.ITEM_TEAM_SEE));
//			}
//		}
		
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
	
	private void fillTappedPlaceTable(Dialog d, final int userTappedHintDialogId) {
		// TODO Auto-generated method stub
		TextView tv;
		
//		Toast.makeText(MapActivity.this, "User '" + tappedInfoTitle+" got tapped", Toast.LENGTH_LONG).show();

//		InGameUserInfoTO p = genericGameResponseTO.getInGamePlayerInfoTO(
//				tappedUser);
		switch(userTappedHintDialogId){
		case USER_TAPPED_GOAL_DIALOG_ID:
		case USER_TAPPED_HINT_DIALOG_ID:
			d.setContentView(R.layout.dialog_show_hint);
			if( this.hints !=null && tappedIdx < this.hints.size()  ){
				HintOverlayItem h = this.hints.get(tappedIdx);
				
				tv = (TextView) d.findViewById(R.id.dsh_name);
				tv.setText(h.mTitle );
				tv.setText( String.valueOf(h.getPlace().getPlaceId()) +" : "+h.mTitle);
				
				tv = (TextView) d.findViewById(R.id.dsh_desc);
				tv.setText(h.mDescription );
			}
			break;

		case USER_TAPPED_USERSEEGOAL_DIALOG_ID:
		case USER_TAPPED_USERSEEHINT_DIALOG_ID:
			d.setContentView(R.layout.dialog_take_hint);
			Button bAct;
			bAct = (Button)d.findViewById(R.id.dth_bt_take);
			if (userTappedHintDialogId == USER_TAPPED_USERSEEHINT_DIALOG_ID)
				bAct.setText(R.string.take_hint);
			else
				bAct.setText(R.string.take_goal);
			
			
			bAct.setOnClickListener(new android.view.View.OnClickListener() {

				public void onClick(View v) {
					try {
						dismissDialog(userTappedHintDialogId);
						launchTakePlaceThread(tappedIdx);
					} catch (Exception e) {}
				}
			});
			break;
		}
		
	}
	
	private void launchTakePlaceThread(int tappedIdx2) {
		// TODO Auto-generated method stub
//		if (SHOW_LOAD_DIALOG)
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
		takePlaceTask.setPlaceId(hints.get(tappedIdx2).id);
		Thread takePlaceThread = new Thread(null, takePlaceTask, "StartGame");
		takePlaceThread.start();		
	}

	private class SampleLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			launchUpdateLocationThread(loc);
//			curLoc = loc;
//			user.setLatitude((new GeoPoint(loc)).getLatitudeE6() );
//			user.setLongitude((new GeoPoint(loc)).getLongitudeE6() );
//			launchUpdateLocationThread((new GeoPoint(loc)).getLatitudeE6() ,
//					(new GeoPoint(loc)).getLongitudeE6());
			
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
	
	private class HintOverlayItem extends OverlayItem implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8292145846143000436L;
		private long id;
		private PlaceTO place;
		
		private static final int TEAM_HAVE = 0;
		private static final int USER_SEE = 1;
		private static final int TEAM_SEE = 2;
		
		private static final int USER_CAN_TAKE = 3;
//		private static final int ITEM_GOAL = 3;
		private static final int HIDE = Integer.MIN_VALUE;
		
		private int type;
		
		public HintOverlayItem(long id, String aTitle, String aDescription,
				GeoPoint aGeoPoint) {
			super(aTitle, aDescription, aGeoPoint);
			this.id = id;
			this.type = HIDE;
		}	
		
		public HintOverlayItem(long id, String aTitle, String aDescription,
				GeoPoint aGeoPoint, int type) {
			super(aTitle, aDescription, aGeoPoint);
			this.id = id;
			this.type = type;
		}
		public HintOverlayItem(PlaceTO place, int type) {
			this(place.getPlaceId(), place.getName(), place.getDescription(),
					new GeoPoint(place.getLatitude(), place.getLongitude()), type);
			this.place = place;
		}

//		public HintOverlayItem(String aTitle, String aDescription,
//				GeoPoint aGeoPoint) {
//			super(aTitle, aDescription, aGeoPoint);
//			// TODO Auto-generated constructor stub
//		}

		public PlaceTO getPlace() {
			return place;
		}

		public int getType() {
			return type;
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
				if (SHOW_LOAD_DIALOG) dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
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
		GameService gameService;
		
		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		StartGameTask() {
			gameService = CustomAPP.getGameService(MapActivity.this.getApplicationContext());
		}
		
		public void run() {
			
			StartGameHandler handler = new StartGameHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO sOCGRTO = 
//					gameService.startOrContinueGame(login);
					gameService.startOrContinueGame(team.getGameId(),user.getUserId(), team.getTeamId());
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
	        updateLocationTask.onUpdate = false;
	        launchStartGameThread();
			if (gGRTO2 != null) {
//				genericGameResponseTO = gGRTO2;
				doUpdateLocation();
			}
		}
	}
	private class UpdateLocationTask implements Runnable {

		int latitude, longitude;
		boolean onUpdate = false;
		GameService gameService;
		
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
			gameService = CustomAPP.getGameService(MapActivity.this.getApplicationContext());
		}

		public void run() {

			UpdateLocationHandler handler = 
				new UpdateLocationHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO gGRTO = 
					gameService.updateLocation(
							user.getUserId(),
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
	        	if (gGRTO2 == null){
	        		CommonDialogs.errorMessage = "gGRTO2 == null";
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
	        		
				if (gGRTO2 != null) {
					genericGameResponseTO = gGRTO2;
					doTakePlace();
				}
		}
	}
	private class TakePlaceTask implements Runnable {

		long placeId;
		int latitude, longitude;
		GameService gameService;
		
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
			gameService = CustomAPP.getGameService(MapActivity.this.getApplicationContext());
		}

		public void run() {

			TakePlaceHandler handler = 
				new TakePlaceHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				GenericGameResponseTO gGRTO = 
					gameService.takePlace(user.getUserId(), placeId, latitude, longitude, team.getGameId(), team.getTeamId());
				
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
	
	public class THItemizedIconOverlay<Item extends OverlayItem> extends ItemizedIconOverlay<Item> {

		public THItemizedIconOverlay(
				List<Item> pList,
				org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<Item> pOnItemGestureListener,
				ResourceProxy pResourceProxy) {
			super(pList, pOnItemGestureListener, pResourceProxy);
			// TODO Auto-generated constructor stub
		}
        public void removeAllItems() {
            removeAllItems(true);
	    }
	
	    public void removeAllItems(boolean withPopulate) {
	            mItemList.clear();
	            if (withPopulate) {
	                    populate();
	            }
	    }
		
	}
}
