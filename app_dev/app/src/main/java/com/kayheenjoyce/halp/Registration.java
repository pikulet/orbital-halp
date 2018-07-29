package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class Registration extends AppCompatActivity {

    protected HashMap<View, Boolean> checkedStates; // Maps the views to their toggle states
    protected List<View> registrationOptions;       // A list of all registration options

    // Directly accessible drawables
    protected static Drawable button_up;            // Button up drawable
    protected static Drawable button_down;          // Button down drawable

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

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
        registrationOptions.add(findViewById(R.id.reg_others));

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
            checkedStates.put(buttonView, false);
            buttonView.setBackground(button_up);
        } else {
            // Button is not selected, select it
            checkedStates.put(buttonView, true);
            buttonView.setBackground(button_down);

            // If it's the overseas button, send a toast message
            View overseasButton = findViewById(R.id.reg_overseas_button);
            if (buttonView.equals(overseasButton)) {
                showOverseasToast();
            }
        }
    }

    /**
     * Displays the toast notification that the reminder has been set.
     */
    private void showOverseasToast() {
        Context context = getApplicationContext();
        String text = getString(R.string.reg_toast_overseas);

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 750);
        toast.show();
    }

    /**
     * Creates a registration entry based on the information supplied by the user.
     */
    private RegistrationEntry createRegEntry() {
        boolean hasFever = checkedStates.get(findViewById(R.id.reg_fever_button));
        boolean hasCough = checkedStates.get(findViewById(R.id.reg_fever_button));
        boolean wasOverseas = checkedStates.get(findViewById(R.id.reg_fever_button));

        EditText additionalNotesVIew = (EditText) findViewById(R.id.reg_others);
        String additionalNotes = additionalNotesVIew.getText().toString();

        Calendar currentTime = Calendar.getInstance();

        return new RegistrationEntry(currentTime, hasFever, hasCough, wasOverseas, additionalNotes);
    }

    /**
     * Adds the registration entry to the local storage.
     * @param entry The RegistrationEntry created by the user, without the OPEN ID Authentication Token
     */
    private void addEntryToStorage(RegistrationEntry entry) {
        String filename = "currentRegEntry";
        String fileContents = entry.toString();
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a browser view for the user to sign in to OPEN ID and verify their registration
     */
    private void getAuthenticationToken() {
//        String requestSite = "https://openid.nus.edu.sg/auth/index/?openid.ns=http://specs.openid.net/auth/2.0&openid.ns.ax=http://openid.net/srv/ax/1.0&openid.ax.mode=fetch_request&openid.ax.required=email,firstname,lastname&openid.ax.type.email=http://axschema.org/contact/email&openid.ax.type.firstname=http://axschema.org/namePerson/first&openid.ax.type.lastname=http://axschema.org/namePerson/last&openid.mode=checkid_setup&openid.ns.sreg=http://openid.net/extensions/sreg/1.1&openid.sreg.required=email,firstname,lastname&openid.sreg.optional=nickname&openid.claimed_id=http://specs.openid.net/auth/2.0/identifier_select&openid.identity=http://specs.openid.net/auth/2.0/identifier_select&openid.realm=halp://complete&openid.return_to=halp://complete";
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("halp://complete"));
//        startActivity(browserIntent);

        Intent sendReg = new Intent(this,WebViewActivity.class);
        startActivity(sendReg);

    }

    /**
     * Lets the user sign in to NUS OPENID, and sends a registration to UHC.
     * In return, the clinic will return a com.kayheenjoyce.halp.Registration.RegistrationEntry object.
     */
    public void sendRegistration(View buttonView) {
        // Create a new registration object using the states of the buttons and the "Others" section
        RegistrationEntry regEntry = createRegEntry();

        // Save entry to local storage
        addEntryToStorage(regEntry);

        // Redirect user to retrieve NUS Open ID Authentication Token
        getAuthenticationToken();
    }

}
