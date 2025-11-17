package model;

import java.util.Date;

public class Bill {
    private int id;
    private int customerId;
    private int unitsConsumed;
    private double amount;
    private Date billDate;

    public Bill() {}

    public Bill(int id, int customerId, int unitsConsumed, double amount, Date billDate) {
        this.id = id;
        this.customerId = customerId;
        this.unitsConsumed = unitsConsumed;
        this.amount = amount;
        this.billDate = billDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getBillDate() { return billDate; }
    public void setBillDate(Date billDate) { this.billDate = billDate; }
} 