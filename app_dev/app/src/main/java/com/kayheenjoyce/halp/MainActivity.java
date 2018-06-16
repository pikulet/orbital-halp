package com.kayheenjoyce.halp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // The current opening status of the clinic
    protected static boolean clinicIsOpen = true;
    protected static String CLINIC_OPEN;
    protected static String CLINIC_CLOSED;

    // The current estimated waiting time at the clinic. Provided by the clinic.
    protected static int estimatedWaitingTime = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CLINIC_OPEN = getString(R.string.main_clinic_status_open);
        CLINIC_CLOSED = getString(R.string.main_clinic_status_closed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateClinicStatus(); // Updates to see if the clinic is opened or closed.
        updateWaitingTime(); // Updates the clinic's estimated waiting time.
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
        // TODO

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

        // Updates the displayed text
        TextView tView = findViewById(R.id.main_estWaitTime);
        tView.setText(String.valueOf(MainActivity.estimatedWaitingTime));
    }

    /**
     * Starts a registration process by going to the next activity.
     */
    public void startRegistration(View view) {
        Intent startReg = new Intent(this, Registration.class);
        startActivity(startReg);
    }
}
