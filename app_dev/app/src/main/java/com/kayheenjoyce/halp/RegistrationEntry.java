package com.kayheenjoyce.halp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

/**
 * Encapsulates a registration entry sent to the clinic.
 * In the future, this class could have a conversion option to JSON.
 */
public class RegistrationEntry {

    public JSONObject entry = new JSONObject();

    /** Denotes fields needed to send a registration. */
    private String studentID; // The NUS OPEN ID of the student.

    private boolean authenticationSuccess; // indicated the successful authentication of the user

    /** Denotes fields returned by the registration process. */
    private GregorianCalendar consultationTime;  // The timing of the consultation based on clinic estimates.
    private int roomNumber;         // The room number of the consultation

    /** Denotes if a reminder has been set for this registration entry. */
    private boolean reminderHasBeenSet;

    /**
     * Constructor for a new com.kayheenjoyce.halp.RegistrationEntry
     */
    public RegistrationEntry(Boolean hasFever, Boolean hasCough, Boolean wasOverseas, String additionalNotes) {
        try {
            this.entry.put("HAS_FEVER", hasFever.toString());
            this.entry.put("HAS_COUGH", hasCough.toString());
            this.entry.put("WAS_OVERSEAS", wasOverseas.toString());
            this.entry.put("ADDITIONAL_NOTES", additionalNotes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /** Getter for the studentID. */
    public String getStudentID() { return this.studentID; }

    /** Getter for the consultation time. */
    public GregorianCalendar getConsultationTime() {
        return this.consultationTime;
    }

    /** Getter for the room number. */
    public int getRoomNumber() {
        return this.roomNumber;
    }

    /** Getter for the entry. */
    public JSONObject getEntry() { return this.entry; }
}