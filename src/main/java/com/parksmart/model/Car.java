package com.parksmart.model;

/** A car: Rs. 100 per hour, needs a MEDIUM slot. */
public class Car extends Vehicle {

    public Car(String plateNumber, String ownerName) {
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
