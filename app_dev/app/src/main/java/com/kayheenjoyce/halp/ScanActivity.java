package com.kayheenjoyce.halp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        overridePendingTransition(R.anim.enter, R.anim.exit);
//        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//        setContentView(mScannerView);                // Set the scanner view as the content view
        setContentView(R.layout.activity_scan);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
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
        Intent finalPage = new Intent(ScanActivity.this, RoomNumberAndNotes.class);
        startActivity(finalPage);
        onBackPressed();

        // If you would like to resume scanning, call this method below:
        //ScannerView.resumeCameraPreview(this);
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

