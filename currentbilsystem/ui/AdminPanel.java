package ui;

import service.CustomerService;
import model.Customer;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import service.BillService;
import model.Bill;

public class AdminPanel extends JPanel {
    private JTable customerTable;
    private CustomerService customerService;
    private CustomerTableModel customerTableModel;
    private BillService billService;
    private BillTableModel billTableModel;
    private JTable billTable;
    private MainFrame mainFrame;

    public AdminPanel(Connection connection, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        customerService = new CustomerService(connection);
        billService = new BillService(connection);
        JTabbedPane tabbedPane = new JTabbedPane();

        // Customers Tab
        JPanel customersPanel = new JPanel(new BorderLayout());
        customerTableModel = new CustomerTableModel();
        customerTable = new JTable((javax.swing.table.TableModel) customerTableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
        customersPanel.add(scrollPane, BorderLayout.CENTER);

        // Add/Edit/Delete buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        customersPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add customer dialog
        addButton.addActionListener(e -> {
            CustomerFormDialog dialog = new CustomerFormDialog(null);
            int result = JOptionPane.showConfirmDialog(this, dialog, "Add Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Customer c = dialog.getCustomer();
                    if (c != null) {
                        customerService.addCustomer(c);
                        loadCustomers();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error adding customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Edit customer dialog
        editButton.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                Customer selected = customerTableModel.getCustomerAt(row);
                CustomerFormDialog dialog = new CustomerFormDialog(selected);
                int result = JOptionPane.showConfirmDialog(this, dialog, "Edit Customer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        Customer updated = dialog.getCustomer();
                        updated.setId(selected.getId());
                        customerService.updateCustomer(updated);
                        loadCustomers();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error editing customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to edit.");
            }
        });

        // Delete customer
        deleteButton.addActionListener(e -> {
            int row = customerTable.getSelectedRow();
            if (row >= 0) {
                Customer selected = customerTableModel.getCustomerAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete customer '" + selected.getName() + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        customerService.deleteCustomer(selected.getId());
                        loadCustomers();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a customer to delete.");
            }
        });

        tabbedPane.addTab("Customers", customersPanel);

        // Bills Tab
        JPanel billsPanel = new JPanel(new BorderLayout());
        billTableModel = new BillTableModel();
        billTable = new JTable((javax.swing.table.TableModel) billTableModel);
        JScrollPane billScrollPane = new JScrollPane(billTable);
        billsPanel.add(billScrollPane, BorderLayout.CENTER);
        JButton generateBillButton = new JButton("Generate Bill");
        JPanel billButtonPanel = new JPanel();
        billButtonPanel.add(generateBillButton);
        billsPanel.add(billButtonPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Bills", billsPanel);

        // Load bills
        loadBills();

        // Generate Bill dialog
        generateBillButton.addActionListener(e -> {
            java.util.List<Customer> customers = null;
            try { customers = customerService.getAllCustomers(); } catch (Exception ex) { customers = new java.util.ArrayList<>(); }
            BillFormDialog dialog = new BillFormDialog(customers);
            int result = JOptionPane.showConfirmDialog(this, dialog, "Generate Bill", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    Bill bill = dialog.getBill();
                    if (bill != null) {
                        billService.addBill(bill);
                        loadBills();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error generating bill: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
        logoutButton.addActionListener(e -> mainFrame.showLogin());

        loadCustomers();
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            customerTableModel.setCustomers(customers);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBills() {
        try {
            java.util.List<Bill> bills = billService.getAllBills();
            java.util.List<Customer> customers = customerService.getAllCustomers();
            billTableModel.setBills(bills, customers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load bills: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// Table model for customers
class CustomerTableModel extends AbstractTableModel {
    private String[] columns = {"ID", "Name", "Address", "Meter Number", "User ID"};
    private java.util.List<model.Customer> customers = new java.util.ArrayList<>();

    public void setCustomers(java.util.List<model.Customer> customers) {
        this.customers = customers;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        model.Customer c = customers.get(row);
        switch (col) {
            case 0: return c.getId();
            case 1: return c.getName();
            case 2: return c.getAddress();
            case 3: return c.getMeterNumber();
            case 4: return c.getUserId();
            default: return null;
        }
    }

    public Customer getCustomerAt(int row) {
        return customers.get(row);
    }
}

class CustomerFormDialog extends JPanel {
    private JTextField nameField = new JTextField(15);
    private JTextField addressField = new JTextField(15);
    private JTextField meterField = new JTextField(15);
    private JTextField userIdField = new JTextField(5);

    public CustomerFormDialog(Customer c) {
        setLayout(new GridLayout(4, 2, 5, 5));
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Meter Number:")); add(meterField);
        add(new JLabel("User ID:")); add(userIdField);
        if (c != null) {
            nameField.setText(c.getName());
            addressField.setText(c.getAddress());
            meterField.setText(c.getMeterNumber());
            userIdField.setText(String.valueOf(c.getUserId()));
        }
    }
    public Customer getCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String meter = meterField.getText();
        int userId = Integer.parseInt(userIdField.getText());
        return new Customer(0, name, address, meter, userId);
    }
}

// Add BillTableModel and BillFormDialog inner classes
class BillTableModel extends javax.swing.table.AbstractTableModel {
    private String[] columns = {"ID", "Customer", "Units", "Amount", "Date"};
    private java.util.List<model.Bill> bills = new java.util.ArrayList<>();
    private java.util.Map<Integer, String> customerNameMap = new java.util.HashMap<>();
    public void setBills(java.util.List<model.Bill> bills, java.util.List<model.Customer> customers) {
        this.bills = bills;
        customerNameMap.clear();
        for (model.Customer c : customers) {
            customerNameMap.put(c.getId(), c.getName());
        }
        fireTableDataChanged();
    }
    @Override public int getRowCount() { return bills.size(); }
    @Override public int getColumnCount() { return columns.length; }
    @Override public String getColumnName(int col) { return columns[col]; }
    @Override public Object getValueAt(int row, int col) {
        model.Bill b = bills.get(row);
        switch (col) {
            case 0: return b.getId();
            case 1: return customerNameMap.getOrDefault(b.getCustomerId(), String.valueOf(b.getCustomerId()));
            case 2: return b.getUnitsConsumed();
            case 3: return b.getAmount();
            case 4: return b.getBillDate();
            default: return null;
        }
    }
}
class BillFormDialog extends JPanel {
    private static final double RATE_PER_UNIT = 5.0;
    private JComboBox<Customer> customerBox;
    private JTextField unitsField = new JTextField(5);
    private JTextField amountField = new JTextField(7);
    private JTextField dateField = new JTextField(10); // yyyy-mm-dd
    private java.util.List<Customer> customers;
    public BillFormDialog(java.util.List<Customer> customers) {
        this.customers = customers;
        setLayout(new GridLayout(4, 2, 5, 5));
        customerBox = new JComboBox<>(customers.toArray(new Customer[0]));
        add(new JLabel("Customer:")); add(customerBox);
        add(new JLabel("Units Consumed:")); add(unitsField);
        add(new JLabel("Amount:")); add(amountField);
        add(new JLabel("Bill Date (yyyy-mm-dd):")); add(dateField);
        // Default date to today
        dateField.setText(java.time.LocalDate.now().toString());
        // Auto-calc amount when units change
        unitsField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { calcAmount(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { calcAmount(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { calcAmount(); }
            private void calcAmount() {
                try {
                    int units = Integer.parseInt(unitsField.getText());
                    amountField.setText(String.valueOf(units * RATE_PER_UNIT));
                } catch (Exception ex) {
                    amountField.setText("");
                }
            }
        });
    }
    public Bill getBill() {
        Customer c = (Customer) customerBox.getSelectedItem();
        int customerId = c.getId();
        int units = Integer.parseInt(unitsField.getText());
        double amount = Double.parseDouble(amountField.getText());
        java.sql.Date date = java.sql.Date.valueOf(dateField.getText());
        return new Bill(0, customerId, units, amount, date);
    }
    @Override
    public String toString() { return "BillFormDialog"; }
} 