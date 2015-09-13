package com.glitterlabs.caixia.util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.parse.ParseGeoPoint;

/**
 * Created by mohinish on 8/28/15.
 */
public class GpsHelper extends Service implements LocationListener {
    public ParseGeoPoint point;
    private LocationManager locationManager;
    private Context mcontext;

    public GpsHelper(LocationManager locationManager, Context context) {
        this.mcontext = context;
        this.locationManager = locationManager;
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 2000, this);
    }


    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

        Toast.makeText(mcontext, msg, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onProviderDisabled(String provider) {

        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(mcontext, "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(mcontext, "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
