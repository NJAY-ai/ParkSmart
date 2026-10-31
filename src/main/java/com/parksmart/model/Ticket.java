package com.parksmart.model;

import java.time.LocalDateTime;

/** A parking ticket. It is "active" until the vehicle leaves. */
public class Ticket {

    // TODO: add the private fields a ticket needs.

    public Ticket(String ticketId, Vehicle vehicle, ParkingSlot slot, LocalDateTime entryTime) {
        // TODO: write this constructor
    }

    public String getTicketId() {
        // TODO
        return null;
    }

    public Vehicle getVehicle() {
        // TODO
        return null;
    }

    public ParkingSlot getSlot() {
        // TODO
        return null;
    }

    public LocalDateTime getEntryTime() {
        // TODO
        return null;
    }

    /** Null while the vehicle is still parked. */
    public LocalDateTime getExitTime() {
        // TODO
        return null;
    }

    /** 0 while the vehicle is still parked. */
    public double getFee() {
        // TODO
        return 0;
    }

    /** True until the ticket is closed. */
    public boolean isActive() {
        // TODO
        return false;
    }

    /**
     * Closes the ticket when the vehicle leaves.
     * Rule: if the ticket is already closed -> throw IllegalStateException.
     */
    public void close(LocalDateTime exitTime, double fee) {
        // TODO: write this method
    }
}
