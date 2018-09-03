package com.kayheenjoyce.halp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Dispatcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this is to close the app
        if (getIntent().getExtras() != null
                && getIntent().getExtras().getBoolean("CloseApp", false)) {
//            Intent CloseInt = new Intent(getApplicationContext(), MainActivity.class);
//            CloseInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            CloseInt.putExtra("CloseApp", true);
//            startActivity(CloseInt);
            getSharedPreferences("X", 0).edit().clear().apply();
            finish();
        } else {

            Class<?> activityClass;

            try {
                    SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
                    activityClass = Class.forName(
                            prefs.getString("lastActivity", MainActivity.class.getName()));
            } catch (ClassNotFoundException ex) {
                activityClass = MainActivity.class;
            }

            startActivity(new Intent(this, activityClass));
        }
    }
}
