package ui;

import service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Optional;
import model.User;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private JLabel messageLabel;
    private MainFrame mainFrame;
    private Connection connection;

    public LoginPanel(MainFrame mainFrame, Connection connection) {
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

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        messageLabel = new JLabel("");
        gbc.gridy = 3;
        add(messageLabel, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegister();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        AuthService authService = new AuthService(connection);
        try {
            Optional<User> userOpt = authService.login(username, password);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String role = user.getRole();
                if (role.equals("admin")) {
                    mainFrame.showAdminPanel();
                } else if (role.equals("customer")) {
                    mainFrame.showCustomerPanel(user);
                } else {
                    messageLabel.setText("Unknown role: " + role);
                }
            } else {
                messageLabel.setText("Invalid credentials");
            }
        } catch (java.sql.SQLException e) {
            messageLabel.setText("Database error: " + e.getMessage());
        }
    }
} 