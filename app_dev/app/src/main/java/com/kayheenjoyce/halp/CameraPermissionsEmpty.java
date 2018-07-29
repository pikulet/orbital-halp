package com.kayheenjoyce.halp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CameraPermissionsEmpty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermissions();
    }

    /**
     * Proceeds to the permission-checking page.
     */
    private void checkCameraPermissions() {
        Intent reachedClinic = new Intent(this, CameraPermissions.class);
        startActivity(reachedClinic);
    }
}
