package com.sharepay.wifi.activity.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;

import com.sharepay.wifi.R;
import com.sharepay.wifi.base.BaseActivity;
import com.sharepay.wifi.module.main.MainFragment;
import com.sharepay.wifi.module.main.MainPresenter;
import com.sharepay.wifi.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fl_main_container);
        if (CommonUtil.checkIsNull(mainFragment)) {
            mainFragment = MainFragment.getInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_main_container, mainFragment);
            transaction.commit();
        }
        new MainPresenter(MainActivity.this, mainFragment);
    }

    private void checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED || (permissions.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            } else {
            }
            break;
        }
    }
}
