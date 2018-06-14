package com.kayheenjoyce.prototype_halp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // The current opening status of the clinic
    protected static boolean clinicIsOpen = false;

    // The current estimated waiting time at the clinic. Provided by the clinic.
    protected static int estimatedWaitingTime = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateClinicStatus(); // Updates to see if the clinic is opened or closed.
        updateWaitingTime(); // Updates the clinic's estimated waiting time.
    }

    /**
     * Updates the clinic status, if it is opened or not.
     */
    protected void updateClinicStatus() {

    }

    /**
     * Updates the clinic's waiting time.
     * Currently, this is done by generating a pseudorandom number from 45 to 60.
     */
    protected void updateWaitingTime() {
        Random rand = new Random();
        int randomWaitingTime = rand.nextInt(16) + 45;
        MainActivity.estimatedWaitingTime = randomWaitingTime;

        // Updates the displayed text
        TextView tView = findViewById(R.id.main_estWaitTime);
        tView.setText(String.valueOf(MainActivity.estimatedWaitingTime));
    }

}
