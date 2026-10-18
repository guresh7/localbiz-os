import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StockManagement {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:localbiz.db";

        System.out.println("==================================");
        System.out.println("          LOCALBIZ OS");
        System.out.println("         STOCK MANAGEMENT");
        System.out.println("==================================");

       Scanner sc = new Scanner(System.in);
try {
            System.out.print("Enter Product ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter 1 to Add Stock or 2 to Remove Stock: ");
            int choice = Integer.parseInt(sc.nextLine().trim());

            if (id <= 0 || (choice != 1 && choice != 2)) {
                System.out.println("Invalid Product ID or choice.");
                return;
            }

            System.out.print("Enter quantity: ");
            int amount = Integer.parseInt(sc.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

            try (Connection con = DriverManager.getConnection(url)) {

                con.setAutoCommit(false);

                try {
                    String checkSql =
                        "SELECT product_name, quantity FROM products "
                        + "WHERE product_id = ?";

                    String productName;
                    int currentStock;

                    try (PreparedStatement ps =
                             con.prepareStatement(checkSql)) {

                        ps.setInt(1, id);

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                System.out.println("Product not found.");
                                con.rollback();
                                return;
                            }

                            productName = rs.getString("product_name");
                            currentStock = rs.getInt("quantity");
                        }
                    }

                    if (choice == 2 && amount > currentStock) {
                        System.out.println(
                            "Insufficient stock! Available: " + currentStock
                        );
                        con.rollback();
                        return;
                    }

                    String updateSql = choice == 1
                        ? "UPDATE products SET quantity = quantity + ? "
                          + "WHERE product_id = ?"
                        : "UPDATE products SET quantity = quantity - ? "
                          + "WHERE product_id = ? AND quantity >= ?";

                    try (PreparedStatement ps =
                             con.prepareStatement(updateSql)) {

                        ps.setInt(1, amount);
                        ps.setInt(2, id);

                        if (choice == 2) {
                            ps.setInt(3, amount);
                        }

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException("Stock update failed.");
                        }
                    }

                    con.commit();

                    int newStock = choice == 1
                        ? currentStock + amount
                        : currentStock - amount;

                    System.out.println("\nStock updated successfully!");
                    System.out.println("Product: " + productName);
                    System.out.println("Available stock: " + newStock);

                    if (newStock == 0) {
                        System.out.println("OUT OF STOCK!");
                    } else if (newStock < 5) {
                        System.out.println(
                            "LOW STOCK ALERT: Quantity is below 5!"
                        );
                    }

                } catch (SQLException e) {
                    con.rollback();
                    throw e;
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Please enter valid numeric values.");
        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        }
    }
}