package com.kayheenjoyce.halp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanQR extends AppCompatActivity {

    public static TextView tvresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

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
}
