package com.kayheenjoyce.halp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AlarmPermissions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_alarm_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkAlarmPermissions();
    }

    /**
     * Checks if the user has granted this app permissions to set an alarm
     * If not, ask for it.
     */
    private void checkAlarmPermissions() {
        if (checkSelfPermission(Manifest.permission.SET_ALARM)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SET_ALARM},
                    105);
            checkAlarmPermissions();
        }
        //onBackPressed();
    }
}
