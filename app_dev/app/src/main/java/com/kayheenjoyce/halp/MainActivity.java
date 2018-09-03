package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Internal storage filename
    public static String fileName = "currentRegEntry";

    // The current opening status of the clinic
    private static boolean clinicIsOpen;
    private static String CLINIC_OPEN;      // Directly accessible strings
    private static String CLINIC_CLOSED;    // Directly accessible strings

    // The current estimated waiting time at the clinic. Provided by the clinic.
    protected static int estimatedWaitingTime = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // this is to close the app
        if(getIntent().getExtras() != null
                && getIntent().getExtras().getBoolean("CloseApp", false)) {
            finish();
        }

        initialiseFields(); // setup the first page
    }

    @Override
    protected void onResume() { //lock the phone then open it again
        super.onResume();
        updateClinicStatus(); // Updates to see if the clinic is opened or closed.
        updateWaitingTime(); // Updates the clinic's estimated waiting time.
    }

    private void initialiseFields() {
        CLINIC_OPEN = getString(R.string.main_clinic_status_open);
        CLINIC_CLOSED = getString(R.string.main_clinic_status_closed);
        clinicIsOpen = true;
    }

    /**
     * Updates the clinic status, if it is opened or not.
     *      Currently, the clinic opening hours are set as:
     *      Monday to Wednesday: 8.30am - 6.00pm
     *      Thursday: 8.30am - 5.00pm
     *      Friday: 8.30am - 5.30pm
     *      We are closed on Saturday, Sunday & Public Holidays
     *      Clinic is closed for lunch from 12.30pm - 1.30pm
     *      Please note that last registration is 30 minutes prior to lunch or closing hours.
     *
     * In the future, this could be done by simply running an API call to the clinic
     */
    protected void updateClinicStatus() {
        // Retrieves the date and time, checking if this is within the clinic's opening hours.
        Calendar currentCalendar = Calendar.getInstance();

        // Opening time
        Calendar openingTime = Calendar.getInstance();
        openingTime.set(Calendar.HOUR_OF_DAY, 8);
        openingTime.set(Calendar.MINUTE, 30);

        // Closing time
        Calendar closingTime = Calendar.getInstance();
        closingTime.set(Calendar.HOUR_OF_DAY, 18);
        closingTime.set(Calendar.MINUTE, 0);

        // Lunch time starts
        Calendar lunchStartTime = Calendar.getInstance();
        lunchStartTime.set(Calendar.HOUR_OF_DAY, 12);
        lunchStartTime.set(Calendar.MINUTE, 29);

        // Lunch time ends
        Calendar lunchEndTime = Calendar.getInstance();
        lunchEndTime.set(Calendar.HOUR_OF_DAY, 13);
        lunchEndTime.set(Calendar.MINUTE, 31);

        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY ||
            dayOfWeek == Calendar.SUNDAY) {
            // The clinic is not open on weekends
            clinicIsOpen = false;
        } else if (currentCalendar.before(openingTime)) {
            // The clinic has fixed opening hours
            clinicIsOpen = false;
        } else if (currentCalendar.after(lunchStartTime) && currentCalendar.before(lunchEndTime)) {
            // The clinic is closed for lunch
            clinicIsOpen = false;
        } else {
            clinicIsOpen = true;
        }

        String currentClinicStatus = clinicIsOpen ? CLINIC_OPEN : CLINIC_CLOSED;

        // Updates the displayed text
        TextView tView = findViewById(R.id.main_clinicStatus);
        tView.setText(currentClinicStatus);
    }

    /**
     * Updates the clinic's waiting time.
     * Currently, this is done by generating a pseudorandom number from 45 to 60.
     */
    protected void updateWaitingTime() {
        Random rand = new Random();
        int randomWaitingTime = rand.nextInt(16) + 45;
        MainActivity.estimatedWaitingTime = randomWaitingTime;
        String waitTimeValue = String.valueOf(MainActivity.estimatedWaitingTime);

        if(!clinicIsOpen) {
            waitTimeValue = "-";
        }

        // Updates the displayed text
        TextView tView = findViewById(R.id.main_estWaitTime);
        tView.setText(waitTimeValue);
    }

    /**
     * Starts a registration process by going to the next activity.
     */
    public void startRegistration(View view) {
        Intent startReg = new Intent(this, Registration.class);
        startActivity(startReg);
    }
}
