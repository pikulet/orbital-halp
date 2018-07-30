package com.kayheenjoyce.halp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendingReg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Horizontal transition between activities
        overridePendingTransition(R.anim.enter, R.anim.exit);

        // Retrieve token from previous activity
        Bundle extras = getIntent().getExtras();
        String value = extras.getString("token");

        sendRegistration(value);
        proceedToWait();
    }

    /**
     * Sends the registration to the clinic
     */
    private void sendRegistration(String token) {
        // Retrieve file from internal storage
        String storageEntry = getContentsFromInternalStorage();

        // Parse file to JSON object
        JSONObject regEntry = parseStorageFileToJSON(storageEntry);

        // Re-package token into a JSON object
        RegistrationEntry myEntry = new RegistrationEntry(regEntry);
        myEntry.addEntry(RegistrationEntry.authenticationToken, token);

        // Request for and package the name and ID into JSON
        packageJSONWithTokenInfo(myEntry, token);


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
     * Gets the username and userID with the token and packages into the RegistrationEntry object
     * @param JSONEntry the object to be added to
     * @param token the token to access the username and userID
     */
    private void packageJSONWithTokenInfo(RegistrationEntry JSONEntry, String token) {
        // Send UserNameGetter into async task
        String nameLink = "https://ivle.nus.edu.sg/api/Lapi.svc/UserName_Get?APIKey=RZBCGrDddNHkXGG7Y9Q4Q&Token=" + token;
        AsyncTask userNameAsync = new JsonTask().execute(nameLink);

        // Send UserIDGetter into async task
        String idLink = "https://ivle.nus.edu.sg/api/Lapi.svc/UserID_Get?APIKey=RZBCGrDddNHkXGG7Y9Q4Q&Token=" + token;
        AsyncTask userIDAsync = new JsonTask().execute(idLink);


        try {
            // Get both username and userID once completed.
            String studentName = (String) userNameAsync.get();
            Log.d("response", studentName);
            String studentID = (String) userIDAsync.get();
            Log.d("response", studentID);

            // Add to the RegistrationEntry object
            JSONEntry.addEntry(RegistrationEntry.name, studentName);
            JSONEntry.addEntry(RegistrationEntry.studentID, studentID);
        } catch(Exception e) {
            Log.d("Problem with async", "something wrong");
        }


    }

    /**
     * This class does the Async Request for the userName and userID.
     */

    private class JsonTask extends AsyncTask<String, String, String> {

//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            pd = new ProgressDialog(WebViewActivity.this);
//            pd.setMessage("Please wait");
//            pd.setCancelable(false);
//            pd.show();
//        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
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
