package com.kayheenjoyce.halp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WaitingClinic extends AppCompatActivity {

    // The timing left in the countdown
    protected static int countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_waiting_clinic);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    /**
     * Sets a phone alarm. The user can select X minutes before their appointment.
     * Alarm is set for COUNTDOWN TIME - X minutes later.
     */
    public void setReminder() {

    }

    /**
     * Proceeds to scan the QR code.
     */
    public void reachedClinic(View view) {
        Intent reachedClinic = new Intent(this, ScanQR.class);
        startActivity(reachedClinic);
    }

}
