import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SearchProduct {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String url = "jdbc:sqlite:localbiz.db";

        System.out.println("==================================");
        System.out.println("          LOCALBIZ OS");
        System.out.println("          SEARCH PRODUCT");
        System.out.println("==================================");
        System.out.println("1. Search by Product ID");
        System.out.println("2. Search by Product Name");
        System.out.print("Enter your choice: ");

        String choice = sc.nextLine().trim();

        String sql;

        if (choice.equals("1")) {
            sql = "SELECT * FROM products WHERE product_id = ?";

        } else if (choice.equals("2")) {
            sql = "SELECT * FROM products WHERE product_name LIKE ?";

        } else {
            System.out.println("Invalid choice!");
            sc.close();
            return;
        }

        try (
            Connection con = DriverManager.getConnection(url);
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (choice.equals("1")) {
                System.out.print("Enter Product ID: ");
                int id = Integer.parseInt(sc.nextLine().trim());
                ps.setInt(1, id);

            } else {
                System.out.print("Enter Product Name: ");
                String name = sc.nextLine().trim();
                ps.setString(1, "%" + name + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {

                System.out.println("\n--------------------------------------------------------------");
                System.out.printf(
                    "%-5s %-15s %-15s %-10s %-10s %-15s%n",
                    "ID", "NAME", "CATEGORY", "PRICE", "QUANTITY", "SUPPLIER"
                );
                System.out.println("--------------------------------------------------------------");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    System.out.printf(
                        "%-5d %-15s %-15s %-10.2f %-10d %-15s%n",
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getString("supplier")
                    );
                }

                if (!found) {
                    System.out.println("No matching product found.");
                }

                System.out.println("--------------------------------------------------------------");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid Product ID! Enter a number.");
        } catch (SQLException e) {
            System.out.println("Database error occurred.");
            e.printStackTrace();
        } 
        }
    }
