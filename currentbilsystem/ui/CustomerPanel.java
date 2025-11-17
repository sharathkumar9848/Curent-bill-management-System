package ui;

import model.User;
import model.Customer;
import model.Bill;
import service.CustomerService;
import service.BillService;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class CustomerPanel extends JPanel {
    private JLabel nameLabel, addressLabel, meterLabel, billLabel, amountLabel, dateLabel;
    private CustomerService customerService;
    private BillService billService;
    private User currentUser;
    private Connection connection;
    private MainFrame mainFrame;

    public CustomerPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        nameLabel = new JLabel();
        addressLabel = new JLabel();
        meterLabel = new JLabel();
        infoPanel.add(new JLabel("Profile Info:"));
        infoPanel.add(nameLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(meterLabel);
        infoPanel.add(Box.createVerticalStrut(20));
        infoPanel.add(new JLabel("Current Bill:"));
        billLabel = new JLabel();
        amountLabel = new JLabel();
        dateLabel = new JLabel();
        infoPanel.add(billLabel);
        infoPanel.add(amountLabel);
        infoPanel.add(dateLabel);
        add(infoPanel, BorderLayout.CENTER);
        JButton logoutButton = new JButton("Logout");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
        logoutButton.addActionListener(e -> mainFrame.showLogin());
    }

    public void setContext(User user, Connection connection) {
        this.currentUser = user;
        this.connection = connection;
        this.customerService = new CustomerService(connection);
        this.billService = new BillService(connection);
        refresh();
    }

    public void refresh() {
        if (currentUser == null) return;
        try {
            Optional<Customer> custOpt = customerService.getCustomerByUserId(currentUser.getId());
            if (custOpt.isPresent()) {
                Customer c = custOpt.get();
                nameLabel.setText("Name: " + c.getName());
                addressLabel.setText("Address: " + c.getAddress());
                meterLabel.setText("Meter Number: " + c.getMeterNumber());
                List<Bill> bills = billService.getBillsByCustomerId(c.getId());
                if (bills.isEmpty()) {
                    billLabel.setText("No bills found.");
                    amountLabel.setText("");
                    dateLabel.setText("");
                } else {
                    Bill b = bills.get(bills.size() - 1); // Latest bill
                    billLabel.setText("Units Consumed: " + b.getUnitsConsumed());
                    amountLabel.setText("Amount: ₹" + b.getAmount());
                    dateLabel.setText("Date: " + b.getBillDate());
                }
            } else {
                nameLabel.setText("Profile not found.");
                addressLabel.setText("");
                meterLabel.setText("");
                billLabel.setText("");
                amountLabel.setText("");
                dateLabel.setText("");
            }
        } catch (Exception e) {
            nameLabel.setText("Error loading profile: " + e.getMessage());
            addressLabel.setText("");
            meterLabel.setText("");
            billLabel.setText("");
            amountLabel.setText("");
            dateLabel.setText("");
        }
    }
} 