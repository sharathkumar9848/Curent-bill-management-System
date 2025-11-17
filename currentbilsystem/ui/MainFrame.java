package ui;
import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import model.User;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private LoginPanel loginPanel;
    private AdminPanel adminPanel;
    private CustomerPanel customerPanel;
    private RegisterPanel registerPanel;
    private Connection connection;
    private User loggedInUser;

    public MainFrame() {
        setTitle("Current Bill Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        try {
            connection = DBConnection.getConnection(); // Use default package class directly
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, connection);
        adminPanel = new AdminPanel(connection, this);
        customerPanel = new CustomerPanel(this);
        registerPanel = new RegisterPanel(this, connection);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(customerPanel, "customer");
        mainPanel.add(registerPanel, "register");

        add(mainPanel);
        showLogin();
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "login");
    }

    public void showAdminPanel() {
        cardLayout.show(mainPanel, "admin");
    }

    public void showCustomerPanel(User user) {
        this.loggedInUser = user;
        customerPanel.setContext(user, connection);
        cardLayout.show(mainPanel, "customer");
    }

    public void showRegister() {
        cardLayout.show(mainPanel, "register");
    }
} 