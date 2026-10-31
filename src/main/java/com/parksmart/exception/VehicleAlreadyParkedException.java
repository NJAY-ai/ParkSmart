package com.parksmart.exception;

/** Thrown when: The same plate number is already parked. */
public class VehicleAlreadyParkedException extends ParkingException {

    public VehicleAlreadyParkedException(String message) {
        // TODO: pass the message to the parent class
        super(null);
    }
}
