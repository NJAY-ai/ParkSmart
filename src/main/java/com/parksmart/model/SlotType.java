package com.parksmart.model;

/**
 * Size of a parking slot, ordered from smallest to largest.
 */
public enum SlotType {
    SMALL, MEDIUM, LARGE;

    /**
     * Returns true if THIS slot size can hold a vehicle that needs the "required" size.
     * A slot can hold a vehicle of its own size or smaller.
     * Example: LARGE.canFit(SMALL) is true, SMALL.canFit(LARGE) is false.
     */
    public boolean canFit(SlotType required) {
        // TODO: write this method
        return false;
    }
}
