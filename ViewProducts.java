import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewProducts {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:localbiz.db";

        String sql = """
            SELECT product_id, product_name, category,
                   price, quantity, supplier
            FROM products
            ORDER BY product_id
            """;

        System.out.println("==========================================================================");
        System.out.println("                     LOCALBIZ OS");
        System.out.println("                    PRODUCT LIST");
        System.out.println("==========================================================================");

        try (
            Connection con = DriverManager.getConnection(url);

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            boolean found = false;

            System.out.printf(
                "%-5s %-18s %-14s %-10s %-10s %-15s%n",
                "ID", "Product", "Category",
                "Price", "Quantity", "Supplier"
            );

            System.out.println(
                "--------------------------------------------------------------------------"
            );

            while (rs.next()) {
                found = true;

                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String supplier = rs.getString("supplier");

                System.out.printf(
                    "%-5d %-18s %-14s ₹%-9.2f %-10d %-15s%n",
                    id, name, category, price, quantity, supplier
                );

                if (quantity < 5) {
                    System.out.println(
                        "   LOW STOCK ALERT: " + name
                        + " has only " + quantity + " units remaining!"
                    );
                }

                if (quantity == 0) {
                    System.out.println(
                        "  🔴 OUT OF STOCK: " + name
                    );
                }
            }

            if (!found) {
                System.out.println("No products found.");
            }

            System.out.println(
                "--------------------------------------------------------------------------"
            );

        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        }
    }
}