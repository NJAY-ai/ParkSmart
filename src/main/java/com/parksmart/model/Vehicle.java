package com.parksmart.model;

/**
 * Parent class for every vehicle (Car, Bike, Van).
 * It is abstract because the car park never parks a "general" vehicle.
 */
public abstract class Vehicle {

    // TODO: add the private fields a vehicle needs.

    /**
     * Creates a vehicle.
     * Rules: plate number and owner name cannot be empty -> throw IllegalArgumentException.
     *        Remove extra spaces. Save the plate number in capital letters.
     */
    public Vehicle(String plateNumber, String ownerName) {
        // TODO: write this constructor
    }

    public String getPlateNumber() {
        // TODO
        return null;
    }

    public String getOwnerName() {
        // TODO
        return null;
    }

    /** Returns "Car", "Bike" or "Van". */
    public abstract String getType();

    /** Returns the price for one hour of parking, in rupees. */
    public abstract double getHourlyRate();

    /** Returns the smallest slot size this vehicle needs. */
    public abstract SlotType getRequiredSlotType();

    /**
     * Returns the fee for the given number of charged hours.
     * Rule: hours less than 1 -> throw IllegalArgumentException.
     */
    public double calculateFee(long hours) {
        // TODO: write this method
        return 0;
    }

    @Override
    public String toString() {
        return getType() + " " + getPlateNumber() + " (" + getOwnerName() + ")";
    }
}
