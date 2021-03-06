package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.provider.AlarmClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WaitingClinic extends AppCompatActivity {

    private JSONObject entry; // assume that this is stored inside the phone
    private boolean reminderSet = false;
    private int countdownTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_waiting_clinic);

        // Retrieve the RegistrationEntry
        this.entry = getRegEntry();

        // Set countdown value
        TextView waitCountdownTime = findViewById(R.id.wait_time);
        countdownTime = this.getSharedPreferences("X", MODE_PRIVATE).getInt("CountDown",0); // account for stored
        if (countdownTime == 0) {
            countdownTime = getCountDownTime();
        } else { // presence of stored value
            long milliLeft = getSharedPreferences("X", MODE_PRIVATE).getLong("TimeLast", 0) - GregorianCalendar.getInstance().getTimeInMillis();
            int milliInMin = (int)milliLeft/1000/60;
            countdownTime -= milliInMin;
        }
        waitCountdownTime.setText(String.valueOf(countdownTime));

        // Reminds user to be there at clinic 15 minutes before actual time
        showComeEarlyToast();

        // Reminds user to set notification if not set already, the handler staggers the pop up
        // so they don't both appear at the same time, making screen less cluttered
        if (!wasReminderSet()) {
            showReminderToast();
        }
    }

    @Override
    public void onBackPressed() {
        // User can no longer go back in the app
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update the waiting time countdown
        updateWaitingTime();
    }

    /**
     * Retrieves the registration entry from local storage.
     */
    private JSONObject getRegEntry() {
        FileInputStream inputStream;
        String result;

        try {

            // Opens the file
            inputStream = openFileInput("currentRegEntry");
            int inputStreamLength = inputStream.available();
            int[] fileContents = new int[inputStreamLength];

            // Reads from the file
            for (int index = 0; index < inputStreamLength; index ++) {
                fileContents[index] = inputStream.read();
            }

            // Convert result to String
            result = java.util.Arrays.toString(fileContents);

        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return parseStorageFileToJSON(result);
    }

    /**
     * Converts the storage string to a JSON entry.
     */
    private JSONObject parseStorageFileToJSON(String storageEntry) {
        try {
            return new JSONObject(storageEntry);
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    /**
     * Updates the waiting time every 30 seconds.
     */
    private void updateWaitingTime() {
        final Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                // Checks if countdown is above 0
                if (updateWaitingTimeText() > 0) {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
    }

    /**
     * Updates the displayed waiting time.
     */
    private int updateWaitingTimeText() {
        TextView waitTimeDisplay = findViewById(R.id.wait_time);

        int currentValueInt = getCurrentCountDownValue(); // read current value from view
        int newValueInt = currentValueInt - 1; // Subtract 1 from countdown value
        String newTime = String.valueOf(newValueInt);

        waitTimeDisplay.setText(newTime);

        int textColour = Color.BLACK;

        // Colour checking
        if (newValueInt <= 45 && newValueInt > 20) {
            textColour = ContextCompat.getColor(this, R.color.colorSecondaryMedium);
        } else if (newValueInt <= 20) {
            textColour = ContextCompat.getColor(this, R.color.colorSecondaryDark);
        }

        waitTimeDisplay.setTextColor(textColour);

        return newValueInt;
    }

    /**
     * Re-calculates the countdown timer left.
     */
    private int getCountDownTime() {
        return new Random().nextInt(30) + 30;
    }

    /**
     * Retrieves the current countdown value
     */
    private int getCurrentCountDownValue() {
        TextView waitTimeDisplay = findViewById(R.id.wait_time);

        String currentValueString = String.valueOf(waitTimeDisplay.getText());
        return Integer.parseInt(currentValueString);
    }

    /**
     * Checks if a phone alarm has been set.
     */
    private boolean wasReminderSet() {
        reminderSet = this.getSharedPreferences("X", MODE_PRIVATE).getBoolean("Remind",false);
        return this.reminderSet;
    }

    /**
     * Displays the toast notification to come early.
     * Displayed upon resuming the page of the clinic waiting.
     */
    private void showComeEarlyToast() {
        Context context = getApplicationContext();
        String text = getString(R.string.wait_toast_come_early);
        int duration = Toast.LENGTH_SHORT;

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
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 36, 36);
        toast.show();
    }

    /**
     * Displays the toast notification that the reminder has been set.
     */
    private void showReminderSetToast(Integer reminderMinutes) {
        Context context = getApplicationContext();
        String text = getString(R.string.wait_reminder_set_before) + " "
                + reminderMinutes.toString() + " "
                + getString(R.string.wait_reminder_set_after);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    /**
     * Sets a phone alarm. The user can select X minutes before their appointment.
     * Alarm is set for COUNTDOWN TIME - X minutes later.
     */
    public void setReminder(View view) {

        if (!wasReminderSet()) {
            // Creates the drop-down timing list based on the countdown
            final ArrayAdapter<Integer> timingsAdapter = calculateReminderTimes();

            AlertDialog alertDialog = new AlertDialog.Builder(WaitingClinic.this)
                    // Set title
                    .setTitle(getString(R.string.wait_reminder_remind_me_xx))
                    // Set cancellable
                    .setCancelable(true)
                    // Set adapter
                    .setAdapter(timingsAdapter, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int itemSelected) {
                            int reminderMinutes = timingsAdapter.getItem(itemSelected);
                            dialog.dismiss();
                            setAlarmIn(reminderMinutes);
                        }

                    })
                    .create();

            reminderSet = true;

            alertDialog.show();
        }
    }

    /**
     * Helper method to create a drop-down list of possible reminder times, based on the countdown.
     * Lots of errors in this method.
     */
    private ArrayAdapter<Integer> calculateReminderTimes() {

        int countDown = getCurrentCountDownValue();

        // Add timings to the arraylist in intervals of five minutes, up to but not including the
        // time left on the countdown
        ArrayList<Integer> timings = new ArrayList<>();
        for (Integer i = 15; i < countDown; i += 5) {
            timings.add(i);
        }

        // Create an array adapter to be converted to the Alert Dialog
        ArrayAdapter<Integer> timingsAdapter
                = new ArrayAdapter<>(this, R.layout.wait_reminder_basic_text_layout, timings);

        return timingsAdapter;
    }

    /**
     * User presses the timing on the alert dialog, setting an alarm.
     * @param minutesSelected User wants to be reminder x minutes before their consultation
     */
    private void setAlarmIn(int minutesSelected) {

        // Calculates the time that the notification should be set
        GregorianCalendar currentTime = (GregorianCalendar)GregorianCalendar.getInstance();
        currentTime.add(GregorianCalendar.MINUTE, minutesSelected);

        // Set a phone alarm based on the minutes selected and the current time.
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, R.string.wait_reminder_alarm_msg);
        intent.putExtra(AlarmClock.EXTRA_HOUR, currentTime.get(Calendar.HOUR_OF_DAY));
        intent.putExtra(AlarmClock.EXTRA_MINUTES, currentTime.get(Calendar.MINUTE));
        startActivity(intent);
    }

    /**
     * Proceeds to scan the QR code.
     */
    public void reachedClinic(View view) {
        Intent reachedClinic = new Intent(this, ScanActivity.class);
        startActivity(reachedClinic);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // deals with the time left
        TextView waitTimeDisplay = findViewById(R.id.wait_time);
        int currentValueInt = getCurrentCountDownValue(); // read current value from view
        editor.putInt("CountDown",currentValueInt);

        // deals with the current time when closed
        Long currentTime = GregorianCalendar.getInstance().getTimeInMillis(); // store the current system time.
        editor.putLong("TimeLast", currentTime);

        // deals with whether reminder has been set
        editor.putBoolean("Remind", reminderSet); // store whether reminder set
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

}
