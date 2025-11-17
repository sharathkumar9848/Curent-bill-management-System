package dao;

import model.Bill;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillDAO {
    private Connection conn;

    public BillDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean addBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (customer_id, units_consumed, amount, bill_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bill.getCustomerId());
            stmt.setInt(2, bill.getUnitsConsumed());
            stmt.setDouble(3, bill.getAmount());
            stmt.setDate(4, new java.sql.Date(bill.getBillDate().getTime()));
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Bill> getBillsByCustomerId(int customerId) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE customer_id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bills.add(new Bill(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("units_consumed"),
                    rs.getDouble("amount"),
                    rs.getDate("bill_date")
                ));
            }
        }
        return bills;
    }

    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bills.add(new Bill(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getInt("units_consumed"),
                    rs.getDouble("amount"),
                    rs.getDate("bill_date")
                ));
            }
        }
        return bills;
    }
} 