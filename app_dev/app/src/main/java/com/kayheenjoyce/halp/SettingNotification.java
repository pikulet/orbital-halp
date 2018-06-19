package com.kayheenjoyce.halp;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class SettingNotification extends AppCompatActivity {

    // for querying if time set
    public static boolean isTimeSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_notification);

        //Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // let user input time in minutes which they want to be notified in
    public void setTime(View view) {
        isTimeSet = true;

        // set the time
        timeHasBeenSet();
    }
    // informing the user that the time is set
    public void timeHasBeenSet() {

        Context context = getApplicationContext();
        String text = "Timer set";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.LEFT|Gravity.BOTTOM, 400, 1200);
        toast.show();

        // let the thread sleep while the toast is still up, then runs the run() method when 2000ms up
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    public static boolean isTimeSet() {
        return isTimeSet;
    }
}
