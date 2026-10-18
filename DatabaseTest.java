import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:localbiz.db";

        try (Connection connection = DriverManager.getConnection(url)) {

            System.out.println("================================");
            System.out.println("       LOCALBIZ OS");
            System.out.println("================================");
            System.out.println("SQLite connection successful!");
            System.out.println("Database created successfully!");

        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
