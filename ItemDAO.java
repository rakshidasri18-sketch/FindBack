import java.sql.*;
import java.util.ArrayList;

public class ItemDAO {

    // Save a lost or found item
    public static void saveItem(
            String type, String name, String category,
            String color, String location, String description)
            throws SQLException {

        String table = getTable(type);

        String sql = "INSERT INTO " + table
                + " (name, category, color, location, description)"
                + " VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, category);
            ps.setString(3, color);
            ps.setString(4, location);
            ps.setString(5, description);

            ps.executeUpdate();
        }
    }

    // Retrieve items with IDs and status
    public static ArrayList<String[]> getItemsWithStatus(String type)
            throws SQLException {

        String table = getTable(type);
        ArrayList<String[]> items = new ArrayList<>();

        String statusColumn = type.equals("lost")
                ? "status" : "'FOUND' AS status";

        String sql = "SELECT id, name, category, color, "
                + "location, description, " + statusColumn
                + " FROM " + table + " ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("color"),
                    rs.getString("location"),
                    rs.getString("description"),
                    rs.getString("status")
                });
            }
        }

        return items;
    }

    // Existing compatibility method
    public static ArrayList<String[]> getItems(String type)
            throws SQLException {

        ArrayList<String[]> items = new ArrayList<>();

        for (String[] row : getItemsWithStatus(type)) {
            items.add(new String[]{
                row[1], row[2], row[3], row[4], row[5]
            });
        }

        return items;
    }

    // Update status of a lost item
    public static boolean updateLostItemStatus(
            int id, String newStatus) throws SQLException {

        if (!newStatus.equals("MATCHED")
                && !newStatus.equals("RETURNED")) {
            throw new IllegalArgumentException("Invalid status");
        }

        String expectedStatus = newStatus.equals("MATCHED")
                ? "LOST" : "MATCHED";

        String sql = "UPDATE lost_items SET status = ? "
                + "WHERE id = ? AND status = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, id);
            ps.setString(3, expectedStatus);

            return ps.executeUpdate() == 1;
        }
    }

    // Get total number of lost or found items
    public static int getItemCount(String type) throws SQLException {

        String table = getTable(type);

        String sql = "SELECT COUNT(*) FROM " + table;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // Get number of matched or returned items
    public static int getStatusCount(String status)
            throws SQLException {

        if (!status.equals("MATCHED")
                && !status.equals("RETURNED")) {
            throw new IllegalArgumentException("Invalid status");
        }

        String sql = "SELECT COUNT(*) FROM lost_items "
                + "WHERE UPPER(status) = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    // Get database table name safely
    private static String getTable(String type) {

        if ("lost".equals(type)) {
            return "lost_items";
        }

        if ("found".equals(type)) {
            return "found_items";
        }

        throw new IllegalArgumentException("Invalid item type");
    }
}