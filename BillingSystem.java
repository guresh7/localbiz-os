import java.sql.*;
import java.util.*;

public class BillingSystem {

    private static final String URL = "jdbc:sqlite:localbiz.db";
    private static final int LOW_STOCK_LIMIT = 5;

    static class BillItem {
        int id;
        String name;
        int quantity;
        double price;

        BillItem(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        double getTotal() {
            return price * quantity;
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<BillItem> items = new ArrayList<>();

        System.out.println("========================================");
        System.out.println("             LOCALBIZ OS");
        System.out.println("             BILLING SYSTEM");
        System.out.println("========================================");

        try (Connection con = DriverManager.getConnection(URL)) {

            con.setAutoCommit(false);

            try {
                while (true) {
                    System.out.print("\nEnter Product ID (0 to finish): ");
                    int id = Integer.parseInt(sc.nextLine().trim());

                    if (id == 0) {
                        break;
                    }

                    if (id < 0) {
                        System.out.println("Invalid Product ID.");
                        continue;
                    }

                    System.out.print("Enter quantity: ");
                    int quantity = Integer.parseInt(sc.nextLine().trim());

                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than zero.");
                        continue;
                    }

                    String sql = """
                        SELECT product_name, price, quantity
                        FROM products
                        WHERE product_id = ?
                        """;

                    try (PreparedStatement ps = con.prepareStatement(sql)) {

                        ps.setInt(1, id);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                System.out.println("Product not found.");
                                continue;
                            }

                            String name = rs.getString("product_name");
                            double price = rs.getDouble("price");
                            int stock = rs.getInt("quantity");

                            int alreadyAdded = 0;

                            for (BillItem item : items) {
                                if (item.id == id) {
                                    alreadyAdded += item.quantity;
                                }
                            }

                            if (quantity + alreadyAdded > stock) {
                                System.out.println(
                                    "Insufficient stock! Available: "
                                    + (stock - alreadyAdded)
                                );
                                continue;
                            }

                            items.add(
                                new BillItem(id, name, quantity, price)
                            );

                            System.out.println(
                                name + " added to bill successfully."
                            );
                        }
                    }
                }

                if (items.isEmpty()) {
                    con.rollback();
                    System.out.println("\nNo products added. Bill cancelled.");
                    return;
                }

                // Deduct stock for every product.
                String updateSql = """
                    UPDATE products
                    SET quantity = quantity - ?
                    WHERE product_id = ? AND quantity >= ?
                    """;

                for (BillItem item : items) {
                    try (PreparedStatement ps =
                             con.prepareStatement(updateSql)) {

                        ps.setInt(1, item.quantity);
                        ps.setInt(2, item.id);
                        ps.setInt(3, item.quantity);

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                "Stock changed for product: " + item.name
                            );
                        }
                    }
                }

                // Commit only after all stock updates succeed.
                con.commit();

                // Display the bill.
                String billNumber =
                    "BILL-" + System.currentTimeMillis();

                System.out.println("\n========================================================");
                System.out.println("                     SALES BILL");
                System.out.println("========================================================");
                System.out.println("Bill Number: " + billNumber);
                System.out.println("--------------------------------------------------------");
                System.out.printf(
                    "%-20s %8s %12s %12s%n",
                    "Product", "Qty", "Price", "Total"
                );
                System.out.println("--------------------------------------------------------");

                double grandTotal = 0;

                for (BillItem item : items) {
                    double total = item.getTotal();
                    grandTotal += total;

                    System.out.printf(
                        "%-20s %8d %12.2f %12.2f%n"
                        ,
                        item.name, item.quantity, item.price, total
                    );
                }

                System.out.println("--------------------------------------------------------");
                System.out.printf(
                    "GRAND TOTAL: Rs. %.2f%n",
                    grandTotal
                );
                System.out.println("=======================================================");
                System.out.println("Thankyou for shopping with us!");

                // Display remaining stock and alerts.
                System.out.println("\nSTOCK STATUS");
                System.out.println("--------------------------------------------------------");

                String stockSql =
                    "SELECT product_name, quantity FROM products "
                    + "WHERE product_id = ?";

                for (BillItem item : items) {
                    try (PreparedStatement ps =
                             con.prepareStatement(stockSql)) {

                        ps.setInt(1, item.id);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int remaining = rs.getInt("quantity");

                                System.out.println(
                                    item.name + " - Remaining: " + remaining
                                );

                                if (remaining == 0) {
                                    System.out.println(
                                        "OUT OF STOCK: " + item.name
                                    );
                                } else if (remaining < LOW_STOCK_LIMIT) {
                                    System.out.println(
                                        "LOW STOCK ALERT: " + item.name
                                        + " has only " + remaining
                                        + " units remaining!"
                                    );
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                con.rollback();
                System.out.println("\nBilling failed. Stock changes rolled back.");
                System.out.println("Reason: " + e.getMessage());
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}