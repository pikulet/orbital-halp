package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

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
    protected void onResume() {
        super.onResume();

        // Constantly reminds user to be there at clinic 15 minutes before actual time
        showComeEarlyToast();

        // Reminds user to set notification if not set already, the handler staggers the pop up
        // so they don't both appear at the same time, making screen less cluttered
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showReminderToast();
            }
        }, 3500);

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
        if (!SettingNotification.isTimeSet()) {
            Context context = getApplicationContext();
            String text = getString(R.string.wait_toast_notification);
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.RIGHT | Gravity.TOP, 36, 36);
            toast.show();
        }
    }

    /**
     * Sets a phone alarm. The user can select X minutes before their appointment.
     * Alarm is set for COUNTDOWN TIME - X minutes later.
     */
    public void setReminder(View view) {
        Intent intent = new Intent(this, SettingNotification.class);
        startActivity(intent);
    }

    /**
     * Proceeds to scan the QR code.
     */
    public void reachedClinic(View view) {
        Intent reachedClinic = new Intent(this, ScanQR.class);
        startActivity(reachedClinic);
    }

}
