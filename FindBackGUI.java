
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class FindBackGUI extends JFrame {

    private final Color NAVY = new Color(22, 59, 112);
    private final Color BLUE = new Color(48, 104, 190);
    private final Color GREEN = new Color(35, 145, 105);
    private final Color BACKGROUND = new Color(237, 243, 250);

    private JLabel lostCountLabel;
    private JLabel foundCountLabel;
    private JLabel matchCountLabel;

    static class Item {
        int id;
        String name, category, color, location, description, status;

        Item(String[] data) {
            id = Integer.parseInt(data[0]);
            name = data[1];
            category = data[2];
            color = data[3];
            location = data[4];
            description = data[5];
            status = data[6];
        }
    }

    public FindBackGUI() {
        setTitle("FindBack - Smart Lost and Found Matcher");
        setSize(950, 680);
        setMinimumSize(new Dimension(750, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(0, 20));
        main.setBackground(BACKGROUND);
        main.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 25, 25)
        );

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 5));
        header.setBackground(NAVY);
        header.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title = new JLabel(
            "FindBack - Smart Lost & Found"
        );
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Report, match, track and recover lost belongings"
        );
        subtitle.setForeground(new Color(215, 230, 255));

        header.add(title);
        header.add(subtitle);
        main.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
        stats.setOpaque(false);

        lostCountLabel = new JLabel("0");
        foundCountLabel = new JLabel("0");
        matchCountLabel = new JLabel("0");

        stats.add(createStatCard("Lost Items", lostCountLabel));
        stats.add(createStatCard("Found Items", foundCountLabel));
        stats.add(createStatCard("Potential Matches", matchCountLabel));

        center.add(stats, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(4, 2, 15, 15));
        actions.setOpaque(false);

        JButton lostButton = createButton("Report Lost Item", BLUE);
        JButton foundButton = createButton("Report Found Item", GREEN);
        JButton matchButton = createButton("Search for Matches", NAVY);
        JButton viewButton = createButton("View All Items", BLUE);
        JButton statusButton = createButton("Manage Item Status", GREEN);
        JButton refreshButton = createButton("Refresh Dashboard", NAVY);
        JButton exitButton = createButton(
            "Exit Application", new Color(170, 60, 65)
        );

        actions.add(lostButton);
        actions.add(foundButton);
        actions.add(matchButton);
        actions.add(viewButton);
        actions.add(statusButton);
        actions.add(refreshButton);
        actions.add(exitButton);
        actions.add(new JLabel(""));

        center.add(actions, BorderLayout.CENTER);

        JLabel footer = new JLabel(
            "FindBack | Java Swing + JDBC + MySQL",
            SwingConstants.CENTER
        );
        footer.setForeground(new Color(90, 105, 125));
        center.add(footer, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);
        add(main);

        lostButton.addActionListener(e -> registerItem(true));
        foundButton.addActionListener(e -> registerItem(false));
        matchButton.addActionListener(e -> searchMatches());
        viewButton.addActionListener(e -> viewAllItems());
        statusButton.addActionListener(e -> manageStatus());
        refreshButton.addActionListener(e -> refreshDashboard());
        exitButton.addActionListener(e -> dispose());

        refreshDashboard();
    }

    private JPanel createStatCard(String label, JLabel value) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        value.setFont(new Font("Arial", Font.BOLD, 32));
        value.setForeground(NAVY);
        value.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel text = new JLabel(label, SwingConstants.CENTER);
        text.setForeground(new Color(85, 100, 120));

        card.add(value, BorderLayout.CENTER);
        card.add(text, BorderLayout.SOUTH);
        return card;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private ArrayList<Item> loadItems(String type)
            throws SQLException {

        ArrayList<Item> items = new ArrayList<>();

        for (String[] row : ItemDAO.getItemsWithStatus(type)) {
            items.add(new Item(row));
        }

        return items;
    }

    private void registerItem(boolean isLost) {
        JTextField name = new JTextField();
        JTextField category = new JTextField();
        JTextField color = new JTextField();
        JTextField location = new JTextField();
        JTextField description = new JTextField();

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.add(new JLabel("Item Name:"));
        form.add(name);
        form.add(new JLabel("Category:"));
        form.add(category);
        form.add(new JLabel("Color:"));
        form.add(color);
        form.add(new JLabel("Location:"));
        form.add(location);
        form.add(new JLabel("Description:"));
        form.add(description);

        int result = JOptionPane.showConfirmDialog(
            this, form,
            isLost ? "Report Lost Item" : "Report Found Item",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        JTextField[] fields = {
            name, category, color, location, description
        };

        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Please fill all fields!"
                );
                return;
            }
        }

        try {
            ItemDAO.saveItem(
                isLost ? "lost" : "found",
                name.getText().trim(),
                category.getText().trim(),
                color.getText().trim(),
                location.getText().trim(),
                description.getText().trim()
            );

            JOptionPane.showMessageDialog(
                this, "Item saved successfully!"
            );
            refreshDashboard();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private int calculateMatch(Item lost, Item found) {
        int score = 0;

        if (lost.name.equalsIgnoreCase(found.name)) score += 30;
        if (lost.category.equalsIgnoreCase(found.category)) score += 20;
        if (lost.color.equalsIgnoreCase(found.color)) score += 20;
        if (lost.location.equalsIgnoreCase(found.location)) score += 20;
        if (lost.description.equalsIgnoreCase(found.description)) score += 10;

        return score;
    }

    private void refreshDashboard() {
        try {
            ArrayList<Item> lost = loadItems("lost");
            ArrayList<Item> found = loadItems("found");

            lostCountLabel.setText(String.valueOf(lost.size()));
            foundCountLabel.setText(String.valueOf(found.size()));

            int matches = 0;
            for (Item l : lost) {
                if (l.status.equals("RETURNED")) continue;

                for (Item f : found) {
                    if (calculateMatch(l, f) >= 50) matches++;
                }
            }

            matchCountLabel.setText(String.valueOf(matches));

        } catch (Exception ex) {
            lostCountLabel.setText("--");
            foundCountLabel.setText("--");
            matchCountLabel.setText("--");
            showError(ex);
        }
    }

    private void searchMatches() {
        try {
            ArrayList<Item> lost = loadItems("lost");
            ArrayList<Item> found = loadItems("found");

            String[] columns = {
                "Lost ID", "Lost Item", "Found Item",
                "Location", "Score", "Level", "Status"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            for (Item l : lost) {
                if (l.status.equals("RETURNED")) continue;

                for (Item f : found) {
                    int score = calculateMatch(l, f);

                    if (score >= 50) {
                        model.addRow(new Object[]{
                            l.id, l.name, f.name,
                            f.location, score + "%",
                            score >= 80 ? "HIGH" : "MODERATE",
                            l.status
                        });
                    }
                }
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this, "No potential matches found."
                );
                return;
            }

            showTable(model, "FindBack - Potential Matches");

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void viewAllItems() {
        try {
            String[] columns = {
                "ID", "Type", "Name", "Category",
                "Color", "Location", "Description", "Status"
            };

            DefaultTableModel model = new DefaultTableModel(columns, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            for (Item item : loadItems("lost")) {
                model.addRow(new Object[]{
                    item.id, "LOST", item.name, item.category,
                    item.color, item.location,
                    item.description, item.status
                });
            }

            for (Item item : loadItems("found")) {
                model.addRow(new Object[]{
                    item.id, "FOUND", item.name, item.category,
                    item.color, item.location,
                    item.description, item.status
                });
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this, "No registered items found."
                );
                return;
            }

            showTable(model, "FindBack - All Registered Items");

        } catch (Exception ex) {
            showError(ex);
        }
    }

    // Manage lost item status
    private void manageStatus() {
        try {
            ArrayList<Item> lost = loadItems("lost");

            if (lost.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "No lost items available."
                );
                return;
            }

            String[] options = new String[lost.size()];

            for (int i = 0; i < lost.size(); i++) {
                Item item = lost.get(i);
                options[i] = "ID " + item.id + " - "
                        + item.name + " [" + item.status + "]";
            }

            String selected = (String) JOptionPane.showInputDialog(
                this,
                "Select a lost item:",
                "Manage Item Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (selected == null) return;

            Item chosen = null;

            for (int i = 0; i < options.length; i++) {
                if (options[i].equals(selected)) {
                    chosen = lost.get(i);
                    break;
                }
            }

            if (chosen == null) return;

            String newStatus;

            if (chosen.status.equals("LOST")) {
                newStatus = "MATCHED";

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Have you verified a matching found item for "
                    + chosen.name + "?\nMark as MATCHED?",
                    "Confirm Match",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) return;

            } else if (chosen.status.equals("MATCHED")) {
                newStatus = "RETURNED";

                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Has " + chosen.name
                    + " actually been returned to its owner?",
                    "Confirm Return",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) return;

            } else {
                JOptionPane.showMessageDialog(
                    this, "This item is already RETURNED."
                );
                return;
            }

            boolean updated = ItemDAO.updateLostItemStatus(
                chosen.id, newStatus
            );

            if (updated) {
                JOptionPane.showMessageDialog(
                    this,
                    "Item status updated to " + newStatus + "!"
                );
                refreshDashboard();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Status was not updated. Refresh and try again."
                );
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showTable(DefaultTableModel model, String title) {
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(850, 350));

        JOptionPane.showMessageDialog(
            this, scroll, title,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(
            this,
            "Database error: " + ex.getMessage(),
            "FindBack Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
            () -> new FindBackGUI().setVisible(true)
        );
    }
}
