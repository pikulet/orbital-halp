package com.kayheenjoyce.halp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Registration extends AppCompatActivity {

    protected HashMap<View, Boolean> checkedStates; // Maps the views to their toggle states
    protected List<View> registrationOptions;       // A list of all registration options
    // TODO: can add more narrowing bounds

    // Directly accessible drawables
    protected static Drawable button_up;            // Button up drawable
    protected static Drawable button_down;          // Button down drawable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_registration);
        initialiseFields();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    /**
     *  Helper method to initialise values on creation of activity, namely:
     *  1. Button Drawables
     *  2. Registration Options
     *  3. Checked States of Registration Options
     */
    private void initialiseFields() {
        button_up = getDrawable(R.drawable.button_up_secondary);
        button_down = getDrawable(R.drawable.button_down_secondary);

        registrationOptions = new ArrayList<View>();
        // Adds registration options
        registrationOptions.add(findViewById(R.id.reg_overseas_button));
        registrationOptions.add(findViewById(R.id.reg_fever_button));
        registrationOptions.add(findViewById(R.id.reg_cough_button));

        checkedStates = new HashMap<>();
        // Initialise the check states to be false
        for (View option : registrationOptions) {
            checkedStates.put(option, false);
        }
    }

    /**
     * Toggles the registration option button.
     * Changes the button state recorded, and drawable background.
     * @param buttonView The registration optional view being toggled.
     */
    public void toggleButton(View buttonView) {
        boolean isChecked = checkedStates.get(buttonView);

        if (isChecked) {
            // Button is already selected, de-select it
            checkedStates.replace(buttonView, false);
            buttonView.setBackground(button_up);
        } else {
            // Button is not selected, select it
            checkedStates.put(buttonView, true);
            buttonView.setBackground(button_down);
        }
    }

    /**
     * Lets the user sign in to NUS OPENID, and sends a registration to UHC.
     * In return, the clinic will return the
     *  (1) Waiting Time
     *  (2) Room Number
     */
    public void sendRegistration(View buttonView) {
        // TODO

        // User keys in NUS authentication details and confirms sign-in

        // Clinic received a registration and returns the consultation details.

        // For now, this will pretend that the registration is done.
    }
}
