package service;

import dao.BillDAO;
import model.Bill;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class BillService {
    private BillDAO billDAO;
    private static final double RATE_PER_UNIT = 5.0;

    public BillService(Connection conn) {
        this.billDAO = new BillDAO(conn);
    }

    public boolean generateBill(int customerId, int unitsConsumed) throws SQLException {
        double amount = unitsConsumed * RATE_PER_UNIT;
        Bill bill = new Bill(0, customerId, unitsConsumed, amount, new Date());
        return billDAO.addBill(bill);
    }

    public boolean addBill(Bill bill) throws SQLException {
        return billDAO.addBill(bill);
    }

    public List<Bill> getBillsByCustomerId(int customerId) throws SQLException {
        return billDAO.getBillsByCustomerId(customerId);
    }

    public List<Bill> getAllBills() throws SQLException {
        return billDAO.getAllBills();
    }
} 