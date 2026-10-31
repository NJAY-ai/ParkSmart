package com.parksmart.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.parksmart.model.ParkingLot;
import com.parksmart.model.SlotType;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * ===========================================================
 *  ParkSmart - Smart Parking Lot System
 *  GIVEN FILE - DO NOT CHANGE THIS FILE.
 *
 *  Run this class to start the app.
 * ===========================================================
 */
public class Main {

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception e) {
            System.out.println("Modern theme not loaded, using default look.");
        }

        ParkingLot lot = new ParkingLot("ParkSmart City Centre");

        // Starting layout of the car park
        lot.addSlot("S1", SlotType.SMALL);
        lot.addSlot("S2", SlotType.SMALL);
        lot.addSlot("S3", SlotType.SMALL);
        lot.addSlot("M1", SlotType.MEDIUM);
        lot.addSlot("M2", SlotType.MEDIUM);
        lot.addSlot("M3", SlotType.MEDIUM);
        lot.addSlot("M4", SlotType.MEDIUM);
        lot.addSlot("L1", SlotType.LARGE);
        lot.addSlot("L2", SlotType.LARGE);

        SwingUtilities.invokeLater(() -> new ParkSmartApp(lot).setVisible(true));
    }
}
