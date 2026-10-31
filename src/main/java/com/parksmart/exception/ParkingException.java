package com.parksmart.exception;

/**
 * Parent of every ParkSmart business-rule error.
 * The UI shows the message of this exception to the user in a pop-up.
 */
public class ParkingException extends Exception {

    public ParkingException(String message) {
        // TODO: pass the message to the parent class
    }
}
