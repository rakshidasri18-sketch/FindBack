
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/findback_db";

    private static final String USER = "root";

    public static Connection getConnection()
            throws SQLException {

        String password = System.getenv("FINDBACK_DB_PASSWORD");

        if (password == null || password.isEmpty()) {
            throw new SQLException(
                "FINDBACK_DB_PASSWORD is not configured."
            );
        }

        return DriverManager.getConnection(
            URL, USER, password
        );
    }

    public static void main(String[] args) {

        try (Connection con = getConnection()) {
            System.out.println(
                "FindBack connected to MySQL successfully!"
            );

        } catch (SQLException e) {
            System.out.println(
                "Database connection failed: "
                + e.getMessage()
            );
        }
    }
}
