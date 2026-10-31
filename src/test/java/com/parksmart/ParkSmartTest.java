package com.parksmart;

import com.parksmart.exception.ParkingException;
import com.parksmart.exception.ParkingFullException;
import com.parksmart.exception.TicketNotFoundException;
import com.parksmart.exception.VehicleAlreadyParkedException;
import com.parksmart.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ===========================================================
 *  "Check My Code" tests - GIVEN FILE - DO NOT CHANGE.
 *
 *  Right-click this file in IntelliJ and choose "Run".
 *  Green = that rule works. Red = read the message and fix your code.
 * ===========================================================
 */
class ParkSmartTest {

    private static final LocalDateTime NINE_AM = LocalDateTime.of(2026, 11, 2, 9, 0);

    private ParkingLot lot;

    @BeforeEach
    void setUp() {
        lot = new ParkingLot("Test Lot");
        lot.addSlot("S1", SlotType.SMALL);
        lot.addSlot("M1", SlotType.MEDIUM);
        lot.addSlot("M2", SlotType.MEDIUM);
        lot.addSlot("L1", SlotType.LARGE);
    }

    // ------------------------------------------------ Part 1: core OOP

    @Test
    @DisplayName("01. Vehicle is abstract")
    void vehicleIsAbstract() {
        assertTrue(Modifier.isAbstract(Vehicle.class.getModifiers()),
                "Vehicle should be an abstract class.");
    }

    @Test
    @DisplayName("02. Car, Bike and Van are all Vehicles")
    void subclassesExtendVehicle() {
        assertEquals(Vehicle.class, Car.class.getSuperclass());
        assertEquals(Vehicle.class, Bike.class.getSuperclass());
        assertEquals(Vehicle.class, Van.class.getSuperclass());
    }

    @Test
    @DisplayName("03. All fields are private (encapsulation)")
    void fieldsArePrivate() {
        for (Class<?> c : List.of(Vehicle.class, Car.class, Bike.class, Van.class,
                ParkingSlot.class, Ticket.class, ParkingLot.class)) {
            for (Field f : c.getDeclaredFields()) {
                boolean isConstant = Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers());
                if (!isConstant && !f.isSynthetic()) {
                    assertTrue(Modifier.isPrivate(f.getModifiers()),
                            c.getSimpleName() + "." + f.getName() + " should be private.");
                }
            }
        }
    }

    @Test
    @DisplayName("04. Each vehicle type has the right type name, rate and slot size")
    void vehicleDetails() {
        Vehicle car = new Car("CAB-1111", "Nimal");
        Vehicle bike = new Bike("BAA-2222", "Kamal");
        Vehicle van = new Van("PH-3333", "Sunil");

        assertEquals("Car", car.getType());
        assertEquals("Bike", bike.getType());
        assertEquals("Van", van.getType());

        assertEquals(100.0, car.getHourlyRate(), 0.001);
        assertEquals(50.0, bike.getHourlyRate(), 0.001);
        assertEquals(150.0, van.getHourlyRate(), 0.001);

        assertEquals(SlotType.MEDIUM, car.getRequiredSlotType());
        assertEquals(SlotType.SMALL, bike.getRequiredSlotType());
        assertEquals(SlotType.LARGE, van.getRequiredSlotType());
    }

    @Test
    @DisplayName("05. Fees use polymorphism (a Van adds Rs. 100 service charge)")
    void feesArePolymorphic() {
        Vehicle car = new Car("CAB-1111", "Nimal");
        Vehicle bike = new Bike("BAA-2222", "Kamal");
        Vehicle van = new Van("PH-3333", "Sunil");

        assertEquals(300.0, car.calculateFee(3), 0.001);
        assertEquals(100.0, bike.calculateFee(2), 0.001);
        assertEquals(400.0, van.calculateFee(2), 0.001);
    }

    @Test
    @DisplayName("06. Plate number is trimmed and stored in capitals")
    void plateIsCleaned() {
        Vehicle car = new Car("  cab-1234 ", "Nimal");
        assertEquals("CAB-1234", car.getPlateNumber());
    }

    @Test
    @DisplayName("07. Empty plate number is rejected")
    void emptyPlateRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Car("  ", "Nimal"));
    }

    // ------------------------------------------------ Part 2: collections and rules

    @Test
    @DisplayName("08. A duplicate slot ID is rejected")
    void duplicateSlotRejected() {
        assertThrows(IllegalArgumentException.class, () -> lot.addSlot("m1", SlotType.MEDIUM));
    }

    @Test
    @DisplayName("09. A new lot reports the right number of free slots")
    void freeSlotCount() {
        assertEquals(4, lot.getAllSlots().size());
        assertEquals(4, lot.getAvailableSlotCount());
    }

    @Test
    @DisplayName("10. Vehicles go to the smallest free slot that fits")
    void smallestFittingSlot() throws ParkingException {
        assertEquals("S1", lot.parkVehicle(new Bike("B-1", "A"), NINE_AM).getSlot().getSlotId());
        assertEquals("M1", lot.parkVehicle(new Car("C-1", "B"), NINE_AM).getSlot().getSlotId());
        // S1 is now taken, so the next bike moves up to a medium slot
        assertEquals("M2", lot.parkVehicle(new Bike("B-2", "C"), NINE_AM).getSlot().getSlotId());
        assertEquals(1, lot.getAvailableSlotCount());
    }

    @Test
    @DisplayName("11. Ticket IDs start at T1001 and go up by one")
    void ticketIds() throws ParkingException {
        assertEquals("T1001", lot.parkVehicle(new Car("C-1", "A"), NINE_AM).getTicketId());
        assertEquals("T1002", lot.parkVehicle(new Car("C-2", "B"), NINE_AM).getTicketId());
    }

    @Test
    @DisplayName("12. Find ticket by plate ignores capital and small letters")
    void findByPlate() throws ParkingException {
        Ticket t = lot.parkVehicle(new Car("CAB-9999", "A"), NINE_AM);
        assertEquals(t.getTicketId(), lot.findTicketByPlate("cab-9999").getTicketId());
    }

    @Test
    @DisplayName("13. Charged hours are rounded up, minimum one hour")
    void chargedHours() {
        assertEquals(1, ParkingLot.calculateChargedHours(NINE_AM, NINE_AM));
        assertEquals(1, ParkingLot.calculateChargedHours(NINE_AM, NINE_AM.plusMinutes(59)));
        assertEquals(1, ParkingLot.calculateChargedHours(NINE_AM, NINE_AM.plusMinutes(60)));
        assertEquals(2, ParkingLot.calculateChargedHours(NINE_AM, NINE_AM.plusMinutes(61)));
        assertEquals(3, ParkingLot.calculateChargedHours(NINE_AM, NINE_AM.plusHours(3)));
    }

    @Test
    @DisplayName("14. Exit time before entry time is rejected")
    void exitBeforeEntry() {
        assertThrows(IllegalArgumentException.class,
                () -> ParkingLot.calculateChargedHours(NINE_AM, NINE_AM.minusMinutes(1)));
    }

    @Test
    @DisplayName("15. Exit returns the fee, frees the slot and adds to earnings")
    void exitVehicle() throws ParkingException {
        Ticket t = lot.parkVehicle(new Van("PH-1", "A"), NINE_AM);
        double fee = lot.exitVehicle(t.getTicketId(), NINE_AM.plusMinutes(90));

        assertEquals(400.0, fee, 0.001);                 // 2 hours x 150 + 100
        assertTrue(t.getSlot().isAvailable());
        assertEquals(0, lot.getActiveTickets().size());
        assertEquals(1, lot.getCompletedTickets().size());
        assertEquals(400.0, lot.getTotalEarnings(), 0.001);
        assertFalse(t.isActive());
    }

    @Test
    @DisplayName("16. History shows the newest exit first")
    void historyOrder() throws ParkingException {
        Ticket a = lot.parkVehicle(new Car("C-1", "A"), NINE_AM);
        Ticket b = lot.parkVehicle(new Car("C-2", "B"), NINE_AM);
        lot.exitVehicle(a.getTicketId(), NINE_AM.plusHours(1));
        lot.exitVehicle(b.getTicketId(), NINE_AM.plusHours(5));

        List<Ticket> history = lot.getCompletedTickets();
        assertEquals(b.getTicketId(), history.get(0).getTicketId());
        assertEquals(a.getTicketId(), history.get(1).getTicketId());
    }

    // ------------------------------------------------ Part 3: custom exceptions

    @Test
    @DisplayName("17. All custom exceptions extend ParkingException")
    void exceptionFamily() {
        assertTrue(ParkingException.class.isAssignableFrom(ParkingFullException.class));
        assertTrue(ParkingException.class.isAssignableFrom(VehicleAlreadyParkedException.class));
        assertTrue(ParkingException.class.isAssignableFrom(TicketNotFoundException.class));
        assertTrue(Exception.class.isAssignableFrom(ParkingException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(ParkingException.class),
                "ParkingException should be a checked exception (extend Exception).");
    }

    @Test
    @DisplayName("18. No fitting slot throws ParkingFullException")
    void parkingFull() throws ParkingException {
        lot.parkVehicle(new Van("PH-1", "A"), NINE_AM);
        assertThrows(ParkingFullException.class, () -> lot.parkVehicle(new Van("PH-2", "B"), NINE_AM));
    }

    @Test
    @DisplayName("19. The same vehicle cannot park twice")
    void parkedTwice() throws ParkingException {
        lot.parkVehicle(new Car("CAB-1", "A"), NINE_AM);
        assertThrows(VehicleAlreadyParkedException.class,
                () -> lot.parkVehicle(new Car("cab-1", "A"), NINE_AM));
    }

    @Test
    @DisplayName("20. An unknown ticket or plate throws TicketNotFoundException")
    void unknownTicket() {
        assertThrows(TicketNotFoundException.class, () -> lot.exitVehicle("T9999", NINE_AM));
        assertThrows(TicketNotFoundException.class, () -> lot.findTicketByPlate("NOPE-1"));
    }
}
