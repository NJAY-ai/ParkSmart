package com.parksmart.exception;

/** Thrown when: No free slot fits the vehicle. */
public class ParkingFullException extends ParkingException {

    public ParkingFullException(String message) {
        // TODO: pass the message to the parent class
        super(null);
    }
}
