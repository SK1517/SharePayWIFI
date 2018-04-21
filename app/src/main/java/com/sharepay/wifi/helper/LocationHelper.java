package com.sharepay.wifi.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.sharepay.wifi.module.wifiShare.WifiShareFragment;

public class LocationHelper {

    private final String TAG = "LocationHelper ";

    private WifiShareFragment.LocationCallBack mLocationCallBack;

    public LocationHelper(WifiShareFragment.LocationCallBack callBack) {
        mLocationCallBack = callBack;
    }

    public void location(Context context) {
        LogHelper.releaseLog(TAG + "location");
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);// 低精度，如果设置为高精度，依然获取不了location。
            criteria.setAltitudeRequired(false);// 不要求海拔
            criteria.setBearingRequired(false);// 不要求方位
            criteria.setCostAllowed(true);// 允许有花费
            criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗

            // 从可用的位置提供器中，匹配以上标准的最佳提供器
            String locationProvider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context.getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            Location location = locationManager.getLastKnownLocation(locationProvider);
            LogHelper.releaseLog(TAG + "onCreate: " + (location == null) + "..");
            if (location != null) {
                LogHelper.releaseLog(TAG + "onCreate: location");
                // 不为空,显示地理位置经纬度
                showLocation(location);
            }
            // 监视地理位置变化
            locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        } catch (Exception e) {
            LogHelper.errorLog(TAG + "location Exception! error:" + e.getMessage());
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogHelper.releaseLog(TAG + "onProviderEnabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogHelper.releaseLog(TAG + "onProviderDisabled: " + provider + ".." + Thread.currentThread().getName());
        }

        @Override
        public void onLocationChanged(Location location) {
            LogHelper.releaseLog(TAG + "onLocationChanged: " + ".." + Thread.currentThread().getName());
            // 如果位置发生变化,重新显示
            showLocation(location);
        }
    };

    private void showLocation(Location location) {
        // latitude 纬度
        // longitude 经度
        LogHelper.releaseLog(TAG + "location success" + " latitude:" + location.getLatitude() + " longitude:" + location.getLongitude());
        if (null != mLocationCallBack) {
            mLocationCallBack.setLocation(location);
        }
    }
}