package com.infratrans.taxi.onboard.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.infratrans.taxi.onboard.CurrentLocation;

public class LocationService extends Service implements LocationListener,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {

	private GoogleApiClient googleApiClient;

	private boolean LOGGER = false;
	public static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.infratrans.rescue.service.location_tracker";

	Handler mHandler;

	private boolean gpsEnabled = false;

	private String speed = "";
	private String latitude = "";
	private String longitude = "";

	private static final int UPDATE_ADDRESS = 1;
	private static final int UPDATE_LATLNG = 2;


	@Override
	public void onDestroy() {

		super.onDestroy();
		googleApiClient.disconnect();
	}

	@Override
	public void onCreate() {

		super.onCreate();

		googleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		prepareingLocation();
	}



	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		super.onStartCommand(intent, flags, startId);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {

			Intent intent_broadcast = new Intent(BROADCAST_ACTION);
			intent_broadcast.putExtra("opensetting", true);
			LocationService.this.sendBroadcast(intent_broadcast);

		} else {

			googleApiClient.connect();

			//useCoarseFineProviders();
		}

		return START_STICKY;
	}

	@SuppressLint("NewApi")
	private void prepareingLocation() {

		mHandler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
					case UPDATE_ADDRESS:
						//Global.printLog(LOGGER, "mAddress", (String) msg.obj);
						sendBroadcast(latitude, longitude, speed);
						break;
					case UPDATE_LATLNG:
						//Global.printLog(LOGGER, "mLatLng", (String) msg.obj);
						break;
				}
			}
		};

	}

	private void enableLocationSettings() {

		Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		//Add by Beat
		if (location != null) {
			CurrentLocation.currentLocation = location;
			///////////////////////////////////////////////////
			updateUILocation(location);
		}
	}

	private void updateUILocation(Location location) {

		speed = String.valueOf(location.getSpeed());
		latitude = String.valueOf(location.getLatitude());
		longitude = String.valueOf(location.getLongitude());
		sendBroadcast(latitude, longitude, speed);

		Message.obtain(mHandler, UPDATE_LATLNG, location.getLatitude() + ", " + location.getLongitude()).sendToTarget();

	}

	@Override
	public void onConnected(Bundle bundle) {
		LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
		if(locationAvailability != null) {
			LocationRequest locationRequest = new LocationRequest()
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
					.setInterval(500)
					.setFastestInterval(500);
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}


	private void sendBroadcast(String latitude, String longitude, String speed) {

		Intent intent = new Intent(BROADCAST_ACTION);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		intent.putExtra("speed", speed);
		LocationService.this.sendBroadcast(intent);
	}

}
