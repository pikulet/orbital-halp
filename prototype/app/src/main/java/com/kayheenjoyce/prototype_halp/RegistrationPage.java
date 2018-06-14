package com.kayheenjoyce.prototype_halp;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class RegistrationPage extends AppCompatActivity {

    protected boolean wasOverseas = false; // Has the patient been overseas in the last 14 days?
    protected boolean hasFever = false;    // Does the patient have a fever?
    protected boolean hasCough = false;    // Does the patient have a cough?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
    }

}
