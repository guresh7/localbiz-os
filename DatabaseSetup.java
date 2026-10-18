import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:localbiz.db";

        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                product_id INTEGER PRIMARY KEY AUTOINCREMENT,
                product_name TEXT NOT NULL,
                category TEXT NOT NULL,
                price REAL NOT NULL CHECK(price >= 0),
                quantity INTEGER NOT NULL CHECK(quantity >= 0),
                supplier TEXT NOT NULL
            );
            """;

        try (
            Connection connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement()
        ) {
            statement.execute(sql);

            System.out.println("================================");
            System.out.println("       LOCALBIZ OS");
            System.out.println("================================");
            System.out.println("Products table created successfully!");

        } catch (SQLException e) {
            System.out.println("Database setup failed!");
            e.printStackTrace();
        }
    }
}