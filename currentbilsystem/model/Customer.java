package model;

public class Customer {
    private int id;
    private String name;
    private String address;
    private String meterNumber;
    private int userId;

    public Customer() {}

    public Customer(int id, String name, String address, String meterNumber, int userId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.meterNumber = meterNumber;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
} 