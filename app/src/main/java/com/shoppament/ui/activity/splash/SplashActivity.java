package com.shoppament.ui.activity.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.shoppament.R;
import com.shoppament.ui.activity.registration.RegistrationActivity;
import com.shoppament.ui.base.BaseActivity;
import com.shoppament.utils.AndroidPermissions;

public class SplashActivity extends BaseActivity {
    private AndroidPermissions mPermissions;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        openMainScreen();

//        mPermissions = new AndroidPermissions(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        );
//
//        if (mPermissions.checkPermissions()) {
//            openMainScreen();
//        } else {
//            mPermissions.requestPermissions(PERMISSIONS_REQUEST_CODE);
//        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissions.areAllRequiredPermissionsGranted(permissions, grantResults)) {
            openMainScreen();
        } else {
            Toast.makeText(getApplicationContext(),"Permissions Error ", Toast.LENGTH_LONG).show();
        }
    }

    private void openMainScreen() {
        Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.fade_out);
        finish();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doCreate() {

    }
}
