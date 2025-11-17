package service;

import dao.CustomerDAO;
import model.Customer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private CustomerDAO customerDAO;

    public CustomerService(Connection conn) {
        this.customerDAO = new CustomerDAO(conn);
    }

    public boolean addCustomer(Customer customer) throws SQLException {
        return customerDAO.addCustomer(customer);
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        return customerDAO.updateCustomer(customer);
    }

    public boolean deleteCustomer(int id) throws SQLException {
        return customerDAO.deleteCustomer(id);
    }

    public Optional<Customer> getCustomerById(int id) throws SQLException {
        return customerDAO.getCustomerById(id);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }

    public Optional<Customer> getCustomerByUserId(int userId) throws SQLException {
        return customerDAO.getCustomerByUserId(userId);
    }
} 