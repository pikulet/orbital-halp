package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CameraPermissions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_camera_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCameraPermissions();
    }


    /**
     * Checks if the user has granted this app permissions to use the camera
     * If not, ask for it.
     */
    private void checkCameraPermissions() {
        for (int i = 0; i < 3; i++) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        100);
            }
            proceedToScanQR();
        }
    }

    /**
     * Proceed to scan the QR code.
     */
    private void proceedToScanQR() {
        Intent reachedClinic = new Intent(this, ScanActivity.class);
        startActivity(reachedClinic);
    }
}
