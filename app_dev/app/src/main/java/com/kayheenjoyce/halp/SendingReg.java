package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SendingReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);

        sendRegistration();
        proceedToWait();
    }

    /**
     * Sends the registration to the clinic
     */
    private void sendRegistration() {
        // Retrieve file from internal storage
        String storageEntry = getContentsFromInternalStorage();

        // Parse file to JSON object
        JSONObject regEntry = parseStorageFileToJSON(storageEntry);

        // Retrieve authentication token
        String authToken = "TEST_TOKEN";

        // Request for the name and ID
        String studentName = "TEST_NAME";
        String studentID = "TEST_ID";

        // Re-package everything into a JSON object
        RegistrationEntry myEntry = new RegistrationEntry(regEntry);
        myEntry.addEntry(RegistrationEntry.authenticationToken, authToken);
        myEntry.addEntry(RegistrationEntry.name, studentName);
        myEntry.addEntry(RegistrationEntry.studentID, studentID);

        // Re-save the file in internal storage
        addEntryToStorage(myEntry);

        // Send the actual registration

    }

    /**
     * Retrieves the JSON entry previously stored in the internal storage.
     */
    private String getContentsFromInternalStorage() {
        FileInputStream inputStream;

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
            return java.util.Arrays.toString(fileContents);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
     * Adds the registration entry to the local storage.
     * @param entry The RegistrationEntry created by the user, without the OPEN ID Authentication Token
     */
    public void addEntryToStorage(RegistrationEntry entry) {
        String filename = MainActivity.fileName;
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
     * Proceeds to the next waiting page.
     */
    private void proceedToWait() {
        Intent registerDone = new Intent(this, WaitingClinic.class);
        startActivity(registerDone);
    }

}
