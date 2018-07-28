package com.kayheenjoyce.halp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.CancellationException;

/**
 * Encapsulates a registration entry sent to the clinic.
 * In the future, this class could have a conversion option to JSON.
 */
public class RegistrationEntry {

    /** Denotes the registration time. */
    private JSONObject entry = new JSONObject();

    /** Denotes keys for entries. */
    public static final String patientRegistrationTime = "REG_TIME";
    public static final String studentID = "STUDENT_ID";
    public static final String authenticationSuccess = "AUTHENTICAITON_SUCCESS";
    public static final String reminderSet = "REMINDER_SET";
    public static final String patientHasFever = "HAS_FEVER";
    public static final String patientHasCough = "HAS_COUGH";
    public static final String patientWasOverseas = "WAS_OVERSEAS";
    public static final String patientNotes = "ADDITIONAL_NOTES";
    public static final String consultationTime = "CONSULT_TIME";
    public static final String roomNumber = "ROOM_NUM";

    /**
     * Constructor for a new com.kayheenjoyce.halp.RegistrationEntry
     */
    public RegistrationEntry(Calendar registrationTime, Boolean hasFever, Boolean hasCough, Boolean wasOverseas, String additionalNotes) {
        try {
            this.entry.put(patientRegistrationTime, registrationTime.toString());
            this.entry.put(patientHasFever, hasFever.toString());
            this.entry.put(patientHasCough, hasCough.toString());
            this.entry.put(patientWasOverseas, wasOverseas.toString());
            this.entry.put(patientNotes, additionalNotes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor to modify a com.kayheenjoyce.halp.RegistrationEntry
     */
    public RegistrationEntry(JSONObject entry) {
        this.entry = entry;
    }

    /** Getter for the studentID. */
    public String getStudentID() {
        try {
            return (String) this.entry.get(studentID);
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to retrieve Student ID";
        }
    }

    /** Getter for the consultation time. */
    public GregorianCalendar getConsultationTime() {
        try {
            return (GregorianCalendar) this.entry.get(consultationTime);
        } catch (Exception e) {
            e.printStackTrace();

            Calendar now = Calendar.getInstance();
            now.add(Calendar.MINUTE, 34);
            return (GregorianCalendar) now;
        }
    }

    /** Getter for the room number. */
    public int getRoomNumber() {
        try {
            String value = this.entry.get(roomNumber).toString();
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 10;
        }
    }

    @Override
    public String toString() {
        return this.entry.toString();
    }
}