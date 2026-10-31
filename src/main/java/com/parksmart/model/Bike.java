package com.parksmart.model;

/** A bike: Rs. 50 per hour, needs a SMALL slot. */
public class Bike extends Vehicle {

    public Bike(String plateNumber, String ownerName) {
        super(plateNumber, ownerName);
    }

    @Override
    public String getType() {
        // TODO
        return null;
    }

    
    @Override
    public double getHourlyRate() {
        // TODO
        return 0;
    }

    @Override
    public SlotType getRequiredSlotType() {
        // TODO
        return null;
    }
}
