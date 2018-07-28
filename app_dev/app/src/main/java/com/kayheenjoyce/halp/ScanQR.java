package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


// this class not in used anymore, leaving it here for debugging purposes if needed
public class ScanQR extends AppCompatActivity {

    public static TextView tvresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        overridePendingTransition(R.anim.enter, R.anim.exit);

        // stores the qr return value for debugging
        tvresult = (TextView) findViewById(R.id.instructions);

        // the actual scan qr code button
        Button btn = (Button) findViewById(R.id.scanQR);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanQR.this, ScanActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    // can remove this and the button once we know that the qr code scanning to room num and notes works.
    public void finishScan(View view) {
        Intent finishScan = new Intent(this, RoomNumberAndNotes.class);
        startActivity(finishScan);
    }
}
