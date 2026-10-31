package com.parksmart.model;

/**
 * A van: Rs. 150 per hour, needs a LARGE slot.
 * Vans also pay a one-time service charge of Rs. 100 on top of the hourly fee.
 */
public class Van extends Vehicle {

    public Van(String plateNumber, String ownerName) {
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

    // TODO: the fee for a van is different. Add the method that makes this happen.
}
