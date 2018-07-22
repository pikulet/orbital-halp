package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.Toast;

import java.util.ArrayList;
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

        // Retrieve the RegistrationEntry

        // Retrieve the time of the consultation from the registration entry
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

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    /**
     * Sets a phone alarm. The user can select X minutes before their appointment.
     * Alarm is set for COUNTDOWN TIME - X minutes later.
     */
    public void setReminder(View view) {

        // Future expansion: modify reminder timings

        if (!reminderSet) {
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
                            createNotificationIn(reminderMinutes);
                            dialog.dismiss();
                        }

                    })
                    .create();

            alertDialog.show();
        }
    }

    /**
     * Helper method to create a drop-down list of possible reminder times, based on the countdown.
     * Lots of errors in this method.
     */
    private ArrayAdapter<Integer> calculateReminderTimes() {

        int countDown = this.countDown;

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
    private void createNotificationIn(int minutesSelected) {

        // Calculates the time that the notification should be set
        GregorianCalendar currentTime = (GregorianCalendar)GregorianCalendar.getInstance();
        currentTime.add(GregorianCalendar.MINUTE, minutesSelected);

        // Set a phone notification based on the minutes selected and the current time.
        // TODO

        // Inform the user that the reminder has been set
        showReminderSetToast(minutesSelected);
    }

    /**
     * Proceeds to scan the QR code.
     */
    public void reachedClinic(View view) {
        Intent reachedClinic = new Intent(this, ScanQR.class);
        startActivity(reachedClinic);
    }

}
