import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DeleteProduct {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String url = "jdbc:sqlite:localbiz.db";

        System.out.println("==================================");
        System.out.println("          LOCALBIZ OS");
        System.out.println("          DELETE PRODUCT");
        System.out.println("==================================");

        try {
            System.out.print("Enter Product ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            if (id <= 0) {
                System.out.println("Error: Product ID must be positive.");
                return;
            }

            String sql = "DELETE FROM products WHERE product_id = ?";

            try (
                Connection con = DriverManager.getConnection(url);
                PreparedStatement ps = con.prepareStatement(sql)
            ) {
                ps.setInt(1, id);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Product deleted successfully!");
                } else {
                    System.out.println("No product found with ID " + id + ".");
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Enter a valid numeric Product ID.");
        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        }
        }
    }
