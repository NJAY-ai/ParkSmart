package com.parksmart.model;

/** One parking space. It can hold one vehicle at a time. */
public class ParkingSlot {

    // TODO: add the private fields a slot needs.

    /**
     * Creates an empty slot.
     * Rules: slot ID cannot be empty and slot type cannot be null -> throw IllegalArgumentException.
     *        Save the slot ID in capital letters.
     */
    public ParkingSlot(String slotId, SlotType slotType) {
        // TODO: write this constructor
    }

    public String getSlotId() {
        // TODO
        return null;
    }

    public SlotType getSlotType() {
        // TODO
        return null;
    }

    /** True when no vehicle is parked here. */
    public boolean isAvailable() {
        // TODO
        return false;
    }

    /** The vehicle parked here, or null if the slot is free. */
    public Vehicle getParkedVehicle() {
        // TODO
        return null;
    }

    /**
     * Puts a vehicle in this slot.
     * Rule: if the slot is already taken -> throw IllegalStateException.
     */
    public void park(Vehicle vehicle) {
        // TODO: write this method
    }

    /** Empties the slot and returns the vehicle that was in it. */
    public Vehicle removeVehicle() {
        // TODO: write this method
        return null;
    }
}
