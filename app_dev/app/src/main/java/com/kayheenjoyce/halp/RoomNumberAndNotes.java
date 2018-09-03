package com.kayheenjoyce.halp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class RoomNumberAndNotes extends AppCompatActivity {

    EditText EditText1;
    int roomNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_room_number_and_notes);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save("Note1.txt");
            }
        });

        EditText1 = (EditText) findViewById(R.id.EditText1);
        EditText1.setText(Open("Note1.txt"));

        // retrieve stored value if present
        roomNum = this.getSharedPreferences("X", MODE_PRIVATE).getInt("RoomNum",0);

        if(roomNum == 0) {
            roomNum = getRoomNumber();
        }
        updateRoomNumber(roomNum);
    }

    /**
     * Updates the room number text box.
     */
    private void updateRoomNumber(int roomNum) {

        TextView roomNumView = (TextView) findViewById(R.id.room_roomNum);

        // Concatenates values to form the new display text.
        String roomNumDisplayBack = String.valueOf(roomNum);
        String roomNumDisplayFront = String.valueOf(roomNumView.getText());
        String roomNumDisplay = roomNumDisplayFront + " " + roomNumDisplayBack;

        // Set the text
        roomNumView.setText(roomNumDisplay);
    }

    /**
     * Retrieves the room number and appends to the textbox.
     * Currently uses a random number generator.
     */
    private int getRoomNumber() {
        Random rand = new Random();
        int randomWaitingTime = rand.nextInt(15) + 1;

        return randomWaitingTime;
    }

    public void Save(String fileName) {
        try {
            OutputStreamWriter out =
                    new OutputStreamWriter(openFileOutput(fileName, 0));
            out.write(EditText1.getText().toString());
            out.close();
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public String Open(String fileName) {
        String content = "";
        if (FileExists(fileName)) {
            try {
                InputStream in = openFileInput(fileName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n");
                    } in .close();
                    content = buf.toString();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        return content;
    }

    public boolean FileExists(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    // this button clears the notes taken for the doctor, so that the next time
    // the user goes to see the doctor, the notes will be clear again.
    // method also closes the app once the button is clicked
    public void completed(View view) {

        this.deleteFile("Note1.txt");
        this.deleteFile(MainActivity.fileName);

        // this block of code results in the app being closed after the im done button is clicked
        Intent CloseInt = new Intent(getApplicationContext(), Dispatcher.class);
        CloseInt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        CloseInt.putExtra("CloseApp", true);
        startActivity(CloseInt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", ScanActivity.class.getName());
        editor.apply();

        // Horizontal transition between activities
        overridePendingTransition(R.anim.reverse_enter, R.anim.reverse_exit);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("try", "pause called");

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("RoomNum",roomNum);
        editor.putString("lastActivity", getClass().getName());
        editor.apply();
    }

}

