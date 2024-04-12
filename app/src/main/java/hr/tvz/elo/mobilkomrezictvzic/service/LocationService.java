package hr.tvz.elo.mobilkomrezictvzic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import hr.tvz.elo.mobilkomrezictvzic.listener.LocationInfoListener;

public class LocationService extends Service {

    public static final String ACTION_LOCATION_BROADCAST = LocationService.class.getName() + "LocationBroadcast"
            , EXTRA_PROVIDER = "extra_provider"
            , EXTRA_LATITUDE = "extra_latitude"
            , EXTRA_LONGITUDE = "extra_longitude"
            , EXTRA_ALTITUDE = "extra_altitude"
            , EXTRA_SPEED = "extra_speed";
    private static final int MIN_TIME = 1000, MIN_DISTANCE = 1;
    private LocationManager locationManager;
    private Location currentLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getCurrentLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LocationService", "Location Service Started!");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
            }
            else {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                }
                else {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LocationService", "Location Service Stopped!");
        locationManager = null;
    }

    private final LocationInfoListener locationListener = new LocationInfoListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            currentLocation = location;
            sendBroadcastMessage(currentLocation);
        }
    };

    public void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (currentLocation == null) {
                currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (currentLocation == null) {
                    currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    sendBroadcastMessage(currentLocation);
                }
                else {
                    sendBroadcastMessage(currentLocation);
                }
            }
            else {
                sendBroadcastMessage(currentLocation);
            }
        }
    }

    private void sendBroadcastMessage(Location location) {
        if (location != null) {
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
            intent.putExtra(EXTRA_PROVIDER, location.getProvider());
            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
            intent.putExtra(EXTRA_ALTITUDE, location.getAltitude());
            intent.putExtra(EXTRA_SPEED, location.getSpeed());

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}
