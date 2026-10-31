package com.parksmart.exception;

/** Thrown when: No active ticket for this ID or plate number. */
public class TicketNotFoundException extends ParkingException {

    public TicketNotFoundException(String message) {
        // TODO: pass the message to the parent class
        super(null);
    }
}
