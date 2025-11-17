package ui;

import service.AuthService;
import service.CustomerService;
import model.User;
import model.Customer;
import java.util.Optional;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton registerButton, backButton;
    private JLabel messageLabel;
    private MainFrame mainFrame;
    private Connection connection;

    public RegisterPanel(MainFrame mainFrame, Connection connection) {
        this.mainFrame = mainFrame;
        this.connection = connection;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0; gbc.gridy = 0;
        add(userLabel, gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        add(passLabel, gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        JLabel roleLabel = new JLabel("Role:");
        gbc.gridx = 0; gbc.gridy = 2;
        add(roleLabel, gbc);
        roleBox = new JComboBox<>(new String[]{"admin", "customer"});
        gbc.gridx = 1;
        add(roleBox, gbc);

        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        messageLabel = new JLabel("");
        gbc.gridy = 4;
        add(messageLabel, gbc);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLogin();
            }
        });
    }

    private void handleRegister() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();
        AuthService authService = new AuthService(connection);
        try {
            boolean registered = authService.register(username, password, role);
            if (registered) {
                if (role.equals("customer")) {
                    // Get the new user to get their ID
                    Optional<User> userOpt = authService.login(username, password);
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        CustomerDetailsDialog dialog = new CustomerDetailsDialog();
                        int result = JOptionPane.showConfirmDialog(this, dialog, "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            String name = dialog.getName();
                            String address = dialog.getAddress();
                            String meter = dialog.getMeterNumber();
                            CustomerService customerService = new CustomerService(connection);
                            Customer customer = new Customer(0, name, address, meter, user.getId());
                            customerService.addCustomer(customer);
                        }
                    }
                }
                messageLabel.setText("Registration successful! Please login.");
                mainFrame.showLogin();
            } else {
                messageLabel.setText("Registration failed. Username may already exist.");
            }
        } catch (Exception e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }
}

class CustomerDetailsDialog extends JPanel {
    private JTextField nameField = new JTextField(15);
    private JTextField addressField = new JTextField(15);
    private JTextField meterField = new JTextField(15);
    public CustomerDetailsDialog() {
        setLayout(new GridLayout(3, 2, 5, 5));
        add(new JLabel("Name:")); add(nameField);
        add(new JLabel("Address:")); add(addressField);
        add(new JLabel("Meter Number:")); add(meterField);
    }
    public String getName() { return nameField.getText(); }
    public String getAddress() { return addressField.getText(); }
    public String getMeterNumber() { return meterField.getText(); }
} 