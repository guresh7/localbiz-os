import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductManager {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:sqlite:localbiz.db";

        String sql = """
            INSERT INTO products
            (product_name, category, price, quantity, supplier)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection con = DriverManager.getConnection(url);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            System.out.println("================================");
            System.out.println("       LOCALBIZ OS");
            System.out.println("        ADD PRODUCT");
            System.out.println("================================");

            System.out.print("Enter product name: ");
            String name = sc.nextLine();

            System.out.print("Enter category: ");
            String category = sc.nextLine();

            System.out.print("Enter price: ");
            double price = Double.parseDouble(sc.nextLine());

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());

            System.out.print("Enter supplier name: ");
            String supplier = sc.nextLine();

            if (name.isBlank() || category.isBlank() || supplier.isBlank()) {
                System.out.println("Error: Text fields cannot be empty.");
                return;
            }

            if (price < 0 || quantity < 0) {
                System.out.println("Error: Price and quantity cannot be negative.");
                return;
            }

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, quantity);
            ps.setString(5, supplier);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("\nProduct added successfully!");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Enter a valid price and quantity.");
        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        } 
            
        }
    }
