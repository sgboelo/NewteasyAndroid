package com.SmartTech.teasyNew.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import com.SmartTech.teasyNew.PermissionException;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by muddvayne on 12/06/2018.
 */
public class DeviceInfo implements Serializable {

    private String imei;

    private String simNumber;

    private String androidID;

    private transient Location location;

    private transient LocationManager locationManager;

    private transient LocationListener locationListener;

    private DeviceInfo(String imei, String simNumber, String androidID) {
        this.imei = imei;
        this.simNumber = simNumber;
        this.androidID = androidID;
    }

    @SuppressLint("MissingPermission")
    public static DeviceInfo newInstance(Context context) throws PermissionException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                throw new PermissionException("READ_PHONE_STATE has not been granted");
            }
        }
        else {
            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {

                throw new PermissionException("READ_PHONE_NUMBERS has not been granted");
            }
        }

        String imei = null;
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            imei = tm.getDeviceId();
        }

        String simNumber = tm.getLine1Number();
        String androidID = getUniqueID();
        DeviceInfo deviceInfo = new DeviceInfo(imei, simNumber, androidID);
        deviceInfo.updateLocation(context);

        return deviceInfo;
    }

    private static String getUniqueID() {
        String id = null;
        MediaDrm drm = null;

        try {
            UUID uuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
            drm = new MediaDrm(uuid);
            byte[] b = drm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
            id = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (UnsupportedSchemeException e) {
            e.printStackTrace();
        }
        finally {
            if(drm != null) {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    drm.close();
                }
                else {
                    drm.release();
                }
            }
        }

        return id;
    }

    private void updateLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager != null) {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, getLocationListener(), Looper.getMainLooper());
                }
            }
        }
    }

    public void startLocationUpdate(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if(locationManager != null) {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 10, 1, getLocationListener()
                    );
                }
            }
        }
    }

    public void stopLocationUpdate() {
        if(locationManager != null) {
            locationManager.removeUpdates(getLocationListener());
        }
    }

    private LocationListener getLocationListener() {
        if(locationListener != null) {
            return locationListener;
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                DeviceInfo.this.location = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        return locationListener;
    }

    public String getImei() {
        return imei;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public String getAndroidID() {
        return androidID;
    }

    public String getLocationCoordinates() {
        if(location == null) {
            return null;
        }

        return location.getLatitude() + "," + location.getLongitude();
    }
}
