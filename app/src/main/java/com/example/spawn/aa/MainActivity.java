package com.example.spawn.aa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.IOrientationConsumer;
import org.osmdroid.views.overlay.compass.IOrientationProvider;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "osmdroid_test------------";
    private Context ctx;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private CompassOverlay mCompassOverlay;
    private GroundOverlay mBlueDot = null;
    private ScaleBarOverlay mScaleBarOverlay;
    private RotationGestureOverlay mRotationGestureOverlay;

    private class MyCompassOverlay extends CompassOverlay {


        public MyCompassOverlay(Context context, IOrientationProvider orientationProvider, MapView mapView) {
            super(context, orientationProvider, mapView);
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onOrientationChanged(float orientation,
                                         IOrientationProvider source) {
            Log.i(TAG, "指南针方向: " + orientation+"aa");
        }
    }

//    private GroundOverlay mBlueDot = null;
    private boolean mCameraPositionNeedsUpdating = true; // update on first location

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



         ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        map = findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);

        //开启内置放大缩小控件
        map.setBuiltInZoomControls(true);



//        GroundOverlay myGroundOverlay = new GroundOverlay();
//        myGroundOverlay.setPosition(new GeoPoint(45.787, 126.685));
//        myGroundOverlay.setImage(getResources().getDrawable(R.drawable.circle).mutate());
//        myGroundOverlay.setDimensions(2000.0f);
//        myGroundOverlay.setTransparency(0.5f);
//        map.getOverlays().add(myGroundOverlay);

        //屏幕中心设置
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        GeoPoint startPoint = new GeoPoint(45.787, 126.685);
        mapController.setCenter(startPoint);

        //指南针
        mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

        //输出指南针方向值
        MyCompassOverlay myCompassOverlay = new MyCompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        myCompassOverlay.enableCompass();


        //比例尺
        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
//play around with these values to get the location on screen in the right place for your applicatio
        mScaleBarOverlay.setScaleBarOffset(100, 10);
        map.getOverlays().add(mScaleBarOverlay);

        //屏幕双指旋转
        mRotationGestureOverlay = new RotationGestureOverlay(ctx, map);
        mRotationGestureOverlay.setEnabled(true);
        map.getOverlays().add(mRotationGestureOverlay);

        //开启双指放缩
        map.setMultiTouchControls(true);
//        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
//        GeoPoint geoPoint = new GeoPoint(48.8583, 2.2944);
//
//        if (mBlueDot == null) {
//
//            mBlueDot = new GroundOverlay();
//            mBlueDot.setImage(getResources().getDrawable(R.drawable.circle));
//            mBlueDot.setTransparency(0.5f);
//
//        } else {
//            // move existing markers position to received location
//            //mBlueDot.setPosition(geoPoint);
//            map.getOverlays().remove(mBlueDot);
//        }
//        mBlueDot.setPosition(geoPoint);
//        //mBlueDot.setDimensions(20, 20);
//
//        // add to top
//        map.getOverlays().add(mBlueDot);
//
//        // our camera position needs updating if location has significantly changed
//
//        if (mCameraPositionNeedsUpdating) {
//            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
//            map.getController().setCenter(geoPoint);
//            mCameraPositionNeedsUpdating = false;
//        }

        map.invalidate();

        //查看指南针方向
//        Log.i(TAG, "指南针方向: " + mCompassOverlay.getOrientation()+"aa");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //定位控件
//        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
//        mLocationOverlay.enableMyLocation();
//        map.getOverlays().add(mLocationOverlay);

    }
}
