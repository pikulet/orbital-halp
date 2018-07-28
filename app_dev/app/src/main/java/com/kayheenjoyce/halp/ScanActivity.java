package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_scan);

        checkCameraPermissions(); // checks if camera permissions have been granted

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this); // programmatically initialise the scanner view
        contentFrame.addView(mScannerView); // set the scanner view as the content view
    }


    /**
     * Checks if the user has granted this app permissions to use the camera
     * If not, ask for it.
     */
    private void checkCameraPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    100);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        ScanQR.tvresult.setText(rawResult.getText());

        // results of the qr code scanning
        String result = rawResult.getText();
        String pw = "33C07E7CBA240011B526A0573EDD4927A8971D95513D58C005F9F3295ED9E71E"; // hash of actual password kjaoyyhceeeyne

        // compare the 2 strings
        boolean same = result.equals(pw);


        if(same) { // if correct start the next activity
            Intent finalPage = new Intent(ScanActivity.this, RoomNumberAndNotes.class);
            startActivity(finalPage);
        } else { // show the try again toast
            showTryAgainToast();
        }

        // If you would like to resume scanning, call this method below:
        //ScannerView.resumeCameraPreview(this);
    }

    // Show this toast if the two texts do not match
    public void showTryAgainToast() {
        Context context = getApplicationContext();
        String text = getString(R.string.qr_fail_toast);
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 200);
        toast.show();

        // recreating the activity, will call onsavedinstancestate() and onrestoreinstancestate() method
        // not sure if applicable here
        recreate();
    }

    // Back button calls this method
    public void back(View view) {
        onBackPressed();
    }

    // Skip button calls this method, to change it to something once we decide to.
    public void skip(View view) {
        Intent finalPage = new Intent(ScanActivity.this, RoomNumberAndNotes.class);
        startActivity(finalPage);
    }

}

