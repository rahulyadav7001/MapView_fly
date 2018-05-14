package com.example.flytta.flyttatestapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.flytta.flyttatestapp.R;
import com.example.flytta.flyttatestapp.dataobject.LocationDO;
import com.example.flytta.flyttatestapp.dataobject.LocationListDO;
import com.example.flytta.flyttatestapp.service.APIClient;
import com.example.flytta.flyttatestapp.service.APIInterface;
import com.example.flytta.flyttatestapp.utils.AppConstant;
import com.example.flytta.flyttatestapp.utils.NetworkUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private CoordinatorLayout cl_main;
    private APIInterface apiInterface;
    private ArrayList<LocationDO> locationArrayList = new ArrayList<>();
    private SupportMapFragment fm;
    private GoogleMap mGoogleMap;
    private ProgressDialog progressDoalog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setStatusBarColor();
        initializeControls();
        bindControl();
        getLatLongDetail();
    }
    private void initializeControls() {

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        cl_main = (CoordinatorLayout) findViewById(R.id.cl_main);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);
        apiInterface = APIClient.getClient().create(APIInterface.class);

        progressDoalog = new ProgressDialog(MapActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage(getString(R.string.PleaseWait));
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    private void bindControl() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            // Google Play Services are available
            // Getting reference to the SupportMapFragment of activity_main.xml
            // Getting GoogleMap object from the fragment
            fm.getMapAsync(this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void getLatLongDetail() {
        if (NetworkUtility.isNetworkAvailable(MapActivity.this)) {
            progressDoalog.show();
            Call<LocationListDO> call = apiInterface.getLocationsFromServer();
            call.enqueue(new Callback<LocationListDO>() {
                @Override
                public void onResponse(Call<LocationListDO> call, Response<LocationListDO> response) {
                    Log.d("url ", "" + call.request().url().toString());
                    Log.d("Service call :", "Success");
                    Log.d("", response.toString());
                    progressDoalog.dismiss();
                    locationArrayList = response.body();
                    showLoactionInMap(locationArrayList);
                }

                @Override
                public void onFailure(Call<LocationListDO> call, Throwable t) {
                    Log.e("Service call :", "failed");
                    progressDoalog.dismiss();
                }
            });
        } else {
            Toast.makeText(MapActivity.this, getResources().getString(R.string.ConnectionIssue), Toast.LENGTH_SHORT).show();
        }
    }

    public void showLoactionInMap(ArrayList<LocationDO> locationArrayList) {
        if (locationArrayList != null && locationArrayList.size() > 0) {
            for (int i = 0; i < locationArrayList.size(); i++) {
                LocationDO locationDO = locationArrayList.get(i);
                setMarker(locationDO.getLat(), locationDO.getLon(), "", locationDO.getServiceId());
            }
        } else {
            Toast.makeText(MapActivity.this, "No location found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setMarker(String lat, String lon, String title, String serviceId) {
        double lattiute, longitude;
        lattiute = Double.parseDouble(lat);
        longitude = Double.parseDouble(lon);
        LatLng placeLocation = new LatLng(lattiute, longitude);
        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(placeLocation).title(serviceId));
        marker.setTag(serviceId);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 12));
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
        getMenuInflater().inflate(R.menu.map, menu);
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
            showMessage();
        } else if (id == R.id.nav_gallery) {
            showMessage();
        } else if (id == R.id.nav_slideshow) {
            showMessage();
        } else if (id == R.id.nav_manage) {
            showMessage();
        } else if (id == R.id.nav_share) {
            showMessage();
        } else if (id == R.id.nav_send) {
            showMessage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showMessage() {
        Snackbar.make(cl_main, "Work in under progress", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void setStatusBarColor() {
        Window window = MapActivity.this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(MapActivity.this.getResources().getColor(R.color.statusBar_color));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String serviceId = (String) marker.getTag();
                Log.d("Clicked Service ID:", serviceId);
                Intent activtyIntent = new Intent(MapActivity.this, LocationDetailActivity.class);
                activtyIntent.putExtra(AppConstant.Extra_Key_ServiceID, serviceId);
                startActivity(activtyIntent);
                return false;
            }
        });
    }
}
