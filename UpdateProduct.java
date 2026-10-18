import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UpdateProduct {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String url = "jdbc:sqlite:localbiz.db";

        System.out.println("==================================");
        System.out.println("          LOCALBIZ OS");
        System.out.println("          UPDATE PRODUCT");
        System.out.println("==================================");

        try {
            System.out.print("Enter Product ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter new product name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter new category: ");
            String category = sc.nextLine().trim();

            System.out.print("Enter new price: ");
            double price = Double.parseDouble(sc.nextLine().trim());

            System.out.print("Enter new quantity: ");
            int quantity = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Enter new supplier name: ");
            String supplier = sc.nextLine().trim();

            if (name.isBlank() || category.isBlank() || supplier.isBlank()) {
                System.out.println("Error: Text fields cannot be empty.");
                return;
            }

            if (!Double.isFinite(price) || price < 0 || quantity < 0) {
                System.out.println("Error: Price and quantity must be valid non-negative values.");
                return;
            }

            String sql = """
                UPDATE products
                SET product_name = ?,
                    category = ?,
                    price = ?,
                    quantity = ?,
                    supplier = ?
                WHERE product_id = ?
                """;

            try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement ps = con.prepareStatement(sql)
            ) {
                ps.setString(1, name);
                ps.setString(2, category);
                ps.setDouble(3, price);
                ps.setInt(4, quantity);
                ps.setString(5, supplier);
                ps.setInt(6, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("\nProduct updated successfully!");
                } else {
                    System.out.println("\nNo product found with ID " + id + ".");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Enter a valid numeric ID, price, and quantity.");
        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        }
    
        }
    }
