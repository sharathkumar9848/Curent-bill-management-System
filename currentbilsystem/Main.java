import model.User;
import model.Customer;
import model.Bill;
import service.AuthService;
import service.CustomerService;
import service.BillService;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ui.MainFrame().setVisible(true);
            }
        });
    }

    private static void adminMenu(CustomerService customerService, BillService billService) throws SQLException {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Edit Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Generate Bill");
            System.out.println("5. View All Customers");
            System.out.println("6. View All Bills");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Meter Number: ");
                String meter = scanner.nextLine();
                System.out.print("User ID (from users table): ");
                int userId = Integer.parseInt(scanner.nextLine());
                Customer customer = new Customer(0, name, address, meter, userId);
                if (customerService.addCustomer(customer)) {
                    System.out.println("Customer added.");
                } else {
                    System.out.println("Failed to add customer.");
                }
            } else if (choice == 2) {
                System.out.print("Customer ID to edit: ");
                int id = Integer.parseInt(scanner.nextLine());
                Optional<Customer> custOpt = customerService.getCustomerById(id);
                if (custOpt.isPresent()) {
                    Customer customer = custOpt.get();
                    System.out.print("New Name (" + customer.getName() + "): ");
                    String name = scanner.nextLine();
                    System.out.print("New Address (" + customer.getAddress() + "): ");
                    String address = scanner.nextLine();
                    System.out.print("New Meter Number (" + customer.getMeterNumber() + "): ");
                    String meter = scanner.nextLine();
                    customer.setName(name.isEmpty() ? customer.getName() : name);
                    customer.setAddress(address.isEmpty() ? customer.getAddress() : address);
                    customer.setMeterNumber(meter.isEmpty() ? customer.getMeterNumber() : meter);
                    if (customerService.updateCustomer(customer)) {
                        System.out.println("Customer updated.");
                    } else {
                        System.out.println("Failed to update customer.");
                    }
                } else {
                    System.out.println("Customer not found.");
                }
            } else if (choice == 3) {
                System.out.print("Customer ID to delete: ");
                int id = Integer.parseInt(scanner.nextLine());
                if (customerService.deleteCustomer(id)) {
                    System.out.println("Customer deleted.");
                } else {
                    System.out.println("Failed to delete customer.");
                }
            } else if (choice == 4) {
                System.out.print("Customer ID for bill: ");
                int customerId = Integer.parseInt(scanner.nextLine());
                System.out.print("Units Consumed: ");
                int units = Integer.parseInt(scanner.nextLine());
                if (billService.generateBill(customerId, units)) {
                    System.out.println("Bill generated.");
                } else {
                    System.out.println("Failed to generate bill.");
                }
            } else if (choice == 5) {
                List<Customer> customers = customerService.getAllCustomers();
                System.out.println("\n--- Customers ---");
                for (Customer c : customers) {
                    System.out.println("ID: " + c.getId() + ", Name: " + c.getName() + ", Address: " + c.getAddress() + ", Meter: " + c.getMeterNumber() + ", UserID: " + c.getUserId());
                }
            } else if (choice == 6) {
                List<Bill> bills = billService.getAllBills();
                System.out.println("\n--- Bills ---");
                for (Bill b : bills) {
                    System.out.println("ID: " + b.getId() + ", CustomerID: " + b.getCustomerId() + ", Units: " + b.getUnitsConsumed() + ", Amount: ₹" + b.getAmount() + ", Date: " + b.getBillDate());
                }
            } else if (choice == 7) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void customerMenu(User user, CustomerService customerService, BillService billService) throws SQLException {
        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. View Current Bill");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 1) {
                Optional<Customer> custOpt = customerService.getCustomerByUserId(user.getId());
                if (custOpt.isPresent()) {
                    Customer c = custOpt.get();
                    System.out.println("Name: " + c.getName());
                    System.out.println("Address: " + c.getAddress());
                    System.out.println("Meter Number: " + c.getMeterNumber());
                } else {
                    System.out.println("Profile not found.");
                }
            } else if (choice == 2) {
                Optional<Customer> custOpt = customerService.getCustomerByUserId(user.getId());
                if (custOpt.isPresent()) {
                    Customer c = custOpt.get();
                    List<Bill> bills = billService.getBillsByCustomerId(c.getId());
                    if (bills.isEmpty()) {
                        System.out.println("No bills found.");
                    } else {
                        Bill b = bills.get(bills.size() - 1); // Latest bill
                        System.out.println("Units Consumed: " + b.getUnitsConsumed());
                        System.out.println("Amount: ₹" + b.getAmount());
                        System.out.println("Date: " + b.getBillDate());
                    }
                } else {
                    System.out.println("Profile not found.");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}
