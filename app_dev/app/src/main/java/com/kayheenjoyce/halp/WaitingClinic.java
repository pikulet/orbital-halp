package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class WaitingClinic extends AppCompatActivity {

    // The waiting countdown time, rounded up to the nearest minute
    protected int countDown = 60;

    // Whether a reminder has been set
    protected boolean reminderSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_waiting_clinic);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Retrieve the time of the consultation from local storage.
        // TODO
        //
        // Recalculate and convert the consultation time to a waiting time
        // TODO

        // Constantly reminds user to be there at clinic 15 minutes before actual time
        showComeEarlyToast();

        // Check from local storage if the reminder has been set
        // TODO

        // Reminds user to set notification if not set already, the handler staggers the pop up
        // so they don't both appear at the same time, making screen less cluttered
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!reminderSet) {
                    showReminderToast();
                }
            }
        }, 2000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    /**
     * Displays the toast notification to come early.
     * Displayed upon resuming the page of the clinic waiting.
     */
    private void showComeEarlyToast() {
        Context context = getApplicationContext();
        String text = getString(R.string.wait_toast_come_early);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    /**
     * Displays the toast notification to set a reminder.
     * Only displayed when a reminder has not been set by the user.
     */
    private void showReminderToast() {
        Context context = getApplicationContext();
        String text = getString(R.string.wait_toast_notification);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 36, 36);
        toast.show();
    }

    /**
     * Sets a phone alarm. The user can select X minutes before their appointment.
     * Alarm is set for COUNTDOWN TIME - X minutes later.
     */
    public void setReminder(View view) {

        calculateReminderTimes();   // Creates the drop-down timing list based on the countdown

        AlertDialog alertDialog = new AlertDialog.Builder(WaitingClinic.this)
                // Set title
                .setTitle(getString(R.string.wait_reminder_remind_me_xx))
                // Set cancellable
                .setCancelable(true)
                // Set view
                .setView(R.layout.wait_reminder_alert_dialog)
                .create();

        alertDialog.show();

        // Update on Firebase that the reminder has been set
        // TODO
    }

    /**
     * Helper method to create a drop-down list of possible reminder times, based on the countdown.
     * Lots of errors in this method.
     */
    private void calculateReminderTimes() {

        int countDown = this.countDown;

        // Add timings to the arraylist in intervals of five minutes, up to but not including the
        // time left on the countdown
        ArrayList<String> timings = new ArrayList<>();
        for (Integer i = 0; i < countDown; i += 5) {
            timings.add(i.toString());
        }

        // Create an array adapter to be converted to the Spinner
        ArrayAdapter<String> timingsAdapter
                = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, timings);


        // Set the spinner adapter
        ListView timingList = findViewById(R.id.wait_reminder_list);
        timingList.setAdapter(timingsAdapter);

    }

    /**
     * User presses the confirm button on the alert dialog, setting an alarm.
     */
    public void reminderTimeConfirmed(View view) {
        int minutesSelected = 20; // E.g. user chose to remind 20 minutes before consultation
        // TODO

        // Calculates the time that the notification should be set
        GregorianCalendar currentTime = (GregorianCalendar)GregorianCalendar.getInstance();
        currentTime.add(GregorianCalendar.MINUTE, minutesSelected);

        // Set a phone notification based on the minutes selected and the current time.
        // TODO
    }

    /**
     * Proceeds to scan the QR code.
     */
    public void reachedClinic(View view) {
        Intent reachedClinic = new Intent(this, ScanQR.class);
        startActivity(reachedClinic);
    }

}
