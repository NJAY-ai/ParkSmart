package com.parksmart.model;

import com.parksmart.exception.ParkingException;
import com.parksmart.exception.TicketNotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The whole car park: its slots, the tickets of parked vehicles,
 * the history of finished tickets and the total earnings.
 */
public class ParkingLot {

    // TODO: add the private fields the car park needs.
    //       Choose suitable Java collections (List, Map ...).

    /** Rule: name cannot be empty -> throw IllegalArgumentException. */
    public ParkingLot(String name) {
        // TODO: write this constructor
    }

    public String getName() {
        // TODO
        return null;
    }

    /**
     * Adds a new empty slot.
     * Rule: two slots cannot have the same ID (ignore capital/small letters)
     *       -> throw IllegalArgumentException.
     */
    public void addSlot(String slotId, SlotType type) {
        // TODO: write this method
    }

    /**
     * Parks a vehicle and returns its new ticket.
     * Rules:
     *  - The same plate number is already parked -> throw VehicleAlreadyParkedException.
     *  - Choose the SMALLEST free slot that fits the vehicle.
     *    If two slots have the same size, use the one that was added first.
     *  - No free slot fits -> throw ParkingFullException.
     *  - Ticket IDs are "T1001", "T1002", "T1003" ...
     */
    public Ticket parkVehicle(Vehicle vehicle, LocalDateTime entryTime) throws ParkingException {
        // TODO: write this method
        throw new UnsupportedOperationException("parkVehicle() is not written yet.");
    }

    /**
     * The vehicle leaves. Returns the fee to pay.
     * Rules:
     *  - No active ticket with this ID (ignore capital/small letters) -> throw TicketNotFoundException.
     *  - Work out the charged hours, then the fee for this vehicle.
     *  - Free the slot, close the ticket, move it to the history, add the fee to the earnings.
     */
    public double exitVehicle(String ticketId, LocalDateTime exitTime) throws TicketNotFoundException {
        // TODO: write this method
        throw new UnsupportedOperationException("exitVehicle() is not written yet.");
    }

    /**
     * Number of hours to charge.
     * Rules: round UP to the next full hour, minimum 1 hour.
     *        59 minutes = 1 hour, 60 minutes = 1 hour, 61 minutes = 2 hours.
     *        Exit time before entry time -> throw IllegalArgumentException.
     */
    public static long calculateChargedHours(LocalDateTime entryTime, LocalDateTime exitTime) {
        // TODO: write this method
        throw new UnsupportedOperationException("calculateChargedHours() is not written yet.");
    }

    /**
     * Finds the active ticket of a parked vehicle (ignore capital/small letters).
     * Rule: not found -> throw TicketNotFoundException.
     */
    public Ticket findTicketByPlate(String plateNumber) throws TicketNotFoundException {
        // TODO: write this method
        throw new UnsupportedOperationException("findTicketByPlate() is not written yet.");
    }

    /** All slots, in the order they were added. */
    public List<ParkingSlot> getAllSlots() {
        // TODO
        return new ArrayList<>();
    }

    /** Only the free slots. */
    public List<ParkingSlot> getAvailableSlots() {
        // TODO
        return new ArrayList<>();
    }

    public int getAvailableSlotCount() {
        // TODO
        return 0;
    }

    /** Tickets of vehicles that are parked right now. */
    public List<Ticket> getActiveTickets() {
        // TODO
        return new ArrayList<>();
    }

    /** Closed tickets, NEWEST exit time first. */
    public List<Ticket> getCompletedTickets() {
        // TODO
        return new ArrayList<>();
    }

    /** Total of all fees paid so far. */
    public double getTotalEarnings() {
        // TODO
        return 0;
    }

    /**
     * BONUS TASK: save the completed tickets to a CSV file that opens in Excel.
     * If you skip the bonus, leave this method as it is.
     */
    public void exportHistoryToCsv(String filePath) throws IOException {
        throw new UnsupportedOperationException("CSV export is not written yet.");
    }
}
