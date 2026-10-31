package com.parksmart.ui;

import com.parksmart.exception.ParkingException;
import com.parksmart.model.Bike;
import com.parksmart.model.Car;
import com.parksmart.model.ParkingLot;
import com.parksmart.model.ParkingSlot;
import com.parksmart.model.SlotType;
import com.parksmart.model.Ticket;
import com.parksmart.model.Van;
import com.parksmart.model.Vehicle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * ===========================================================
 *  ParkSmart - main window.
 *  GIVEN FILE - DO NOT CHANGE THIS FILE.
 *
 *  This window only shows data and collects input.
 *  All the real work is done by YOUR classes in the
 *  "model" and "exception" packages.
 * ===========================================================
 */
public class ParkSmartApp extends JFrame {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Color BRAND = new Color(0x1F6FEB);
    private static final Color GOOD = new Color(0x1A7F37);
    private static final Color BAD = new Color(0xCF222E);

    private final ParkingLot lot;

    // Dashboard numbers
    private final JLabel totalValue = new JLabel("0");
    private final JLabel freeValue = new JLabel("0");
    private final JLabel parkedValue = new JLabel("0");
    private final JLabel earningsValue = new JLabel("Rs. 0.00");

    // Tables
    private final DefaultTableModel slotModel = readOnlyModel("Slot", "Size", "Status", "Vehicle");
    private final DefaultTableModel activeModel = readOnlyModel("Ticket", "Plate", "Type", "Owner", "Slot", "Entry time");
    private final DefaultTableModel historyModel = readOnlyModel("Ticket", "Plate", "Type", "Slot", "Entry", "Exit", "Fee (Rs.)");

    public ParkSmartApp(ParkingLot lot) {
        this.lot = lot;
        setTitle("ParkSmart - " + safe(lot.getName()));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 640);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Park Vehicle", buildParkTab());
        tabs.addTab("Exit & Payment", buildExitTab());
        tabs.addTab("Slots", buildSlotsTab());
        tabs.addTab("Active Tickets", tablePanel(activeModel, null));
        tabs.addTab("History", buildHistoryTab());
        tabs.setBorder(new EmptyBorder(8, 12, 12, 12));
        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);
        refreshAll();
    }

    // ---------------------------------------------------------------- header

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BRAND);
        header.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("ParkSmart");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel(safe(lot.getName()) + "  |  Smart Parking Lot System");
        sub.setForeground(new Color(0xDDE8FF));

        JPanel names = new JPanel(new GridLayout(2, 1));
        names.setOpaque(false);
        names.add(title);
        names.add(sub);
        header.add(names, BorderLayout.WEST);

        JPanel cards = new JPanel(new GridLayout(1, 4, 10, 0));
        cards.setOpaque(false);
        cards.add(statCard("Total slots", totalValue));
        cards.add(statCard("Free", freeValue));
        cards.add(statCard("Parked", parkedValue));
        cards.add(statCard("Earnings", earningsValue));
        header.add(cards, BorderLayout.EAST);
        return header;
    }

    private JPanel statCard(String label, JLabel value) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(6, 14, 6, 14));
        JLabel l = new JLabel(label);
        l.setForeground(Color.GRAY);
        value.setFont(value.getFont().deriveFont(Font.BOLD, 18f));
        card.add(l);
        card.add(value);
        return card;
    }

    // ---------------------------------------------------------------- park tab

    private JPanel buildParkTab() {
        JComboBox<String> type = new JComboBox<>(new String[]{"Car", "Bike", "Van"});
        JTextField plate = new JTextField(16);
        JTextField owner = new JTextField(16);
        JTextField entry = new JTextField(LocalDateTime.now().format(TIME), 16);
        JButton nowBtn = new JButton("Now");
        nowBtn.addActionListener(e -> entry.setText(LocalDateTime.now().format(TIME)));

        JButton park = primaryButton("Park Vehicle");
        JLabel result = new JLabel(" ");

        park.addActionListener(e -> {
            try {
                Vehicle v;
                String t = (String) type.getSelectedItem();
                if ("Bike".equals(t)) {
                    v = new Bike(plate.getText(), owner.getText());
                } else if ("Van".equals(t)) {
                    v = new Van(plate.getText(), owner.getText());
                } else {
                    v = new Car(plate.getText(), owner.getText());
                }
                Ticket ticket = lot.parkVehicle(v, parseTime(entry.getText()));
                result.setForeground(GOOD);
                result.setText("Parked. Ticket " + ticket.getTicketId() + "  ->  slot "
                        + ticket.getSlot().getSlotId() + "  (" + v.getType() + ", Rs. "
                        + money(v.getHourlyRate()) + " per hour)");
                plate.setText("");
                owner.setText("");
                refreshAll();
            } catch (ParkingException ex) {
                result.setForeground(BAD);
                result.setText(ex.getMessage());
                warn(ex.getMessage());
            } catch (RuntimeException ex) {
                result.setForeground(BAD);
                result.setText(ex.getMessage());
                showProblem(ex);
            }
        });

        JPanel form = formPanel();
        addRow(form, 0, "Vehicle type", type);
        addRow(form, 1, "Plate number", plate);
        addRow(form, 2, "Owner name", owner);
        addRow(form, 3, "Entry time", rowWith(entry, nowBtn));
        addRow(form, 4, "", park);
        addRow(form, 5, "", result);
        return wrap(form, "Rates: Bike Rs. 50/h, Car Rs. 100/h, Van Rs. 150/h + Rs. 100 service charge. "
                + "Time format: yyyy-MM-dd HH:mm");
    }

    // ---------------------------------------------------------------- exit tab

    private JPanel buildExitTab() {
        JTextField ticketId = new JTextField(12);
        JTextField plate = new JTextField(12);
        JButton find = new JButton("Find ticket");
        JTextField exit = new JTextField(LocalDateTime.now().format(TIME), 16);
        JButton nowBtn = new JButton("Now");
        JButton plus30 = new JButton("+30 min");
        JButton plus1h = new JButton("+1 hour");
        JButton pay = primaryButton("Exit & Pay");
        JLabel result = new JLabel(" ");

        nowBtn.addActionListener(e -> exit.setText(LocalDateTime.now().format(TIME)));
        plus30.addActionListener(e -> shiftTime(exit, 30));
        plus1h.addActionListener(e -> shiftTime(exit, 60));

        find.addActionListener(e -> {
            try {
                Ticket t = lot.findTicketByPlate(plate.getText());
                ticketId.setText(t.getTicketId());
                result.setForeground(GOOD);
                result.setText("Found " + t.getTicketId() + " in slot " + t.getSlot().getSlotId()
                        + ", parked since " + t.getEntryTime().format(TIME));
            } catch (ParkingException ex) {
                result.setForeground(BAD);
                result.setText(ex.getMessage());
            } catch (RuntimeException ex) {
                showProblem(ex);
            }
        });

        pay.addActionListener(e -> {
            try {
                double fee = lot.exitVehicle(ticketId.getText(), parseTime(exit.getText()));
                result.setForeground(GOOD);
                result.setText("Ticket " + ticketId.getText().trim().toUpperCase()
                        + " closed. Amount to pay: Rs. " + money(fee));
                JOptionPane.showMessageDialog(this, "Amount to pay: Rs. " + money(fee),
                        "Payment", JOptionPane.INFORMATION_MESSAGE);
                ticketId.setText("");
                plate.setText("");
                refreshAll();
            } catch (ParkingException ex) {
                result.setForeground(BAD);
                result.setText(ex.getMessage());
                warn(ex.getMessage());
            } catch (RuntimeException ex) {
                result.setForeground(BAD);
                result.setText(ex.getMessage());
                showProblem(ex);
            }
        });

        JPanel form = formPanel();
        addRow(form, 0, "Find by plate", rowWith(plate, find));
        addRow(form, 1, "Ticket ID", ticketId);
        addRow(form, 2, "Exit time", rowWith(exit, nowBtn, plus30, plus1h));
        addRow(form, 3, "", pay);
        addRow(form, 4, "", result);
        return wrap(form, "Hours are rounded up. Any stay under one hour is charged as one hour.");
    }

    // ---------------------------------------------------------------- slots tab

    private JPanel buildSlotsTab() {
        JTextField id = new JTextField(8);
        JComboBox<SlotType> size = new JComboBox<>(SlotType.values());
        JButton add = primaryButton("Add Slot");
        add.addActionListener(e -> {
            try {
                lot.addSlot(id.getText(), (SlotType) size.getSelectedItem());
                id.setText("");
                refreshAll();
            } catch (RuntimeException ex) {
                showProblem(ex);
            }
        });
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.add(new JLabel("New slot ID"));
        bar.add(id);
        bar.add(new JLabel("Size"));
        bar.add(size);
        bar.add(add);
        return tablePanel(slotModel, bar);
    }

    // ---------------------------------------------------------------- history tab

    private JPanel buildHistoryTab() {
        JButton export = new JButton("Export to CSV (bonus)");
        export.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new java.io.File("parking-history.csv"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return;
            }
            try {
                lot.exportHistoryToCsv(chooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, "History saved to " + chooser.getSelectedFile().getName());
            } catch (UnsupportedOperationException ex) {
                JOptionPane.showMessageDialog(this, "The CSV export bonus task is not done yet.");
            } catch (Exception ex) {
                showProblem(ex);
            }
        });
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bar.add(new JLabel("Newest exit first"));
        bar.add(export);
        return tablePanel(historyModel, bar);
    }

    // ---------------------------------------------------------------- refresh

    private void refreshAll() {
        try {
            List<ParkingSlot> slots = orEmpty(lot.getAllSlots());
            List<Ticket> active = orEmpty(lot.getActiveTickets());
            totalValue.setText(String.valueOf(slots.size()));
            freeValue.setText(String.valueOf(lot.getAvailableSlotCount()));
            parkedValue.setText(String.valueOf(active.size()));
            earningsValue.setText("Rs. " + money(lot.getTotalEarnings()));

            slotModel.setRowCount(0);
            for (ParkingSlot s : slots) {
                Vehicle v = s.getParkedVehicle();
                slotModel.addRow(new Object[]{s.getSlotId(), s.getSlotType(),
                        s.isAvailable() ? "Free" : "Taken",
                        v == null ? "-" : v.getType() + " " + v.getPlateNumber()});
            }

            activeModel.setRowCount(0);
            for (Ticket t : active) {
                Vehicle v = t.getVehicle();
                activeModel.addRow(new Object[]{t.getTicketId(), v.getPlateNumber(), v.getType(),
                        v.getOwnerName(), t.getSlot().getSlotId(), t.getEntryTime().format(TIME)});
            }

            historyModel.setRowCount(0);
            for (Ticket t : orEmpty(lot.getCompletedTickets())) {
                Vehicle v = t.getVehicle();
                historyModel.addRow(new Object[]{t.getTicketId(), v.getPlateNumber(), v.getType(),
                        t.getSlot().getSlotId(), t.getEntryTime().format(TIME),
                        t.getExitTime() == null ? "-" : t.getExitTime().format(TIME), money(t.getFee())});
            }
        } catch (RuntimeException ex) {
            showProblem(ex);
        }
    }

    // ---------------------------------------------------------------- helpers

    private static <T> List<T> orEmpty(List<T> list) {
        return list == null ? List.of() : list;
    }

    private static String safe(String s) {
        return s == null ? "(no name)" : s;
    }

    private static String money(double value) {
        return String.format("%,.2f", value);
    }

    private LocalDateTime parseTime(String text) {
        try {
            return LocalDateTime.parse(text.trim(), TIME);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Time must look like 2026-11-02 09:30");
        }
    }

    private void shiftTime(JTextField field, int minutes) {
        try {
            field.setText(parseTime(field.getText()).plusMinutes(minutes).format(TIME));
        } catch (IllegalArgumentException ex) {
            showProblem(ex);
        }
    }

    private void warn(String message) {
        JOptionPane.showMessageDialog(this, message, "ParkSmart", JOptionPane.WARNING_MESSAGE);
    }

    private void showProblem(Exception ex) {
        String msg = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        JOptionPane.showMessageDialog(this, msg, "Problem", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel tablePanel(DefaultTableModel model, JComponent topBar) {
        JTable table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new StatusRenderer());
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBorder(new EmptyBorder(12, 12, 12, 12));
        if (topBar != null) {
            p.add(topBar, BorderLayout.NORTH);
        }
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    private static JPanel formPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        return form;
    }

    private static void addRow(JPanel form, int row, String label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        form.add(new JLabel(label), c);
        c.gridx = 1;
        form.add(field, c);
    }

    private static JPanel rowWith(JComponent... parts) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        for (JComponent part : parts) {
            p.add(part);
        }
        return p;
    }

    private static JPanel wrap(JPanel form, String note) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(form, BorderLayout.NORTH);
        JLabel n = new JLabel(note);
        n.setForeground(Color.GRAY);
        n.setBorder(new EmptyBorder(0, 26, 16, 16));
        p.add(n, BorderLayout.SOUTH);
        return p;
    }

    private static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BRAND);
        b.setForeground(Color.WHITE);
        b.setFont(b.getFont().deriveFont(Font.BOLD));
        return b;
    }

    /** Colours the "Free" / "Taken" words in the slots table. */
    private static class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean sel,
                                                       boolean focus, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, value, sel, focus, row, col);
            if (!sel) {
                if ("Free".equals(value)) {
                    c.setForeground(GOOD);
                } else if ("Taken".equals(value)) {
                    c.setForeground(BAD);
                } else {
                    c.setForeground(t.getForeground());
                }
            }
            return c;
        }
    }
}
