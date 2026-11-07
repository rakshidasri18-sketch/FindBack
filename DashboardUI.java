import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardUI extends JFrame {

    private final Color PURPLE = new Color(76, 53, 150);
    private final Color DARK_PURPLE = new Color(48, 34, 105);
    private final Color LIGHT_PURPLE = new Color(239, 235, 250);
    private final Color BACKGROUND = new Color(247, 247, 251);
    private final Color TEXT = new Color(45, 45, 65);
    private final Color MUTED = new Color(120, 120, 140);

    private FindBackGUI existingGUI;

    private JLabel lostValue;
    private JLabel foundValue;
    private JLabel matchedValue;
    private JLabel returnedValue;

    public DashboardUI() {

        setTitle("FindBack - Smart Lost & Found Matcher");
        setSize(1250, 780);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        main.add(createSidebar(), BorderLayout.WEST);
        main.add(createMainContent(), BorderLayout.CENTER);

        add(main);
        setVisible(true);

        loadStatistics();
    }

    // SIDEBAR
    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(245, 0));
        sidebar.setBackground(DARK_PURPLE);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(30, 18, 25, 18));

        JLabel logo = new JLabel("FindBack");
        logo.setFont(new Font("Arial", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("SMART LOST & FOUND");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 11));
        subtitle.setForeground(new Color(205, 195, 240));

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(6));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(45));

        addMenuButton(sidebar, "⌂   Dashboard", null);
        addMenuButton(sidebar, "＋   Report Lost Item",
                () -> openExistingFeature(1));
        addMenuButton(sidebar, "＋   Report Found Item",
                () -> openExistingFeature(2));
        addMenuButton(sidebar, "⌕   Search Matches",
                () -> openExistingFeature(3));
        addMenuButton(sidebar, "▤   View All Items",
                () -> openExistingFeature(4));
        addMenuButton(sidebar, "⚙   Manage Status",
                () -> openExistingFeature(5));
        addMenuButton(sidebar, "↻   Refresh Statistics",
                this::loadStatistics);

        sidebar.add(Box.createVerticalGlue());

        JButton exit = new JButton("↪   Exit");
        styleMenuButton(exit);
        exit.addActionListener(e -> dispose());

        sidebar.add(exit);

        return sidebar;
    }

    // SIDEBAR BUTTON
    private void addMenuButton(
            JPanel sidebar, String text, Runnable action) {

        JButton button = new JButton(text);
        styleMenuButton(button);

        if (action != null) {
            button.addActionListener(e -> action.run());
        } else {
            button.setBackground(new Color(85, 68, 155));
        }

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(10));
    }

    // SIDEBAR BUTTON STYLE
    private void styleMenuButton(JButton button) {

        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(DARK_PURPLE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(14, 12, 14, 8));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(85, 68, 155));
            }

            public void mouseExited(MouseEvent e) {
                if (!button.getText().contains("Dashboard")) {
                    button.setBackground(DARK_PURPLE);
                }
            }
        });
    }

    // MAIN CONTENT
    private JPanel createMainContent() {

        JPanel content = new JPanel(new BorderLayout(20, 20));
        content.setBackground(BACKGROUND);
        content.setBorder(new EmptyBorder(28, 30, 25, 30));

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel heading = new JLabel("Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 29));
        heading.setForeground(TEXT);

        JLabel welcomeUser = new JLabel("Welcome to FindBack  ");
        welcomeUser.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeUser.setForeground(MUTED);

        header.add(heading, BorderLayout.WEST);
        header.add(welcomeUser, BorderLayout.EAST);

        // CENTER
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // WELCOME BANNER
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(LIGHT_PURPLE);
        banner.setBorder(new EmptyBorder(25, 28, 25, 28));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 135));

        JPanel bannerText = new JPanel();
        bannerText.setOpaque(false);
        bannerText.setLayout(new BoxLayout(bannerText, BoxLayout.Y_AXIS));

        JLabel welcome = new JLabel("Welcome back to FindBack!");
        welcome.setFont(new Font("Arial", Font.BOLD, 25));
        welcome.setForeground(DARK_PURPLE);

        JLabel message = new JLabel(
                "A smarter way to reconnect lost belongings with their owners."
        );
        message.setFont(new Font("Arial", Font.PLAIN, 14));
        message.setForeground(MUTED);

        bannerText.add(welcome);
        bannerText.add(Box.createVerticalStrut(12));
        bannerText.add(message);

        banner.add(bannerText, BorderLayout.CENTER);

        center.add(banner);
        center.add(Box.createVerticalStrut(30));

        // STATISTICS HEADING
        JLabel overview = new JLabel("Overview");
        overview.setFont(new Font("Arial", Font.BOLD, 21));
        overview.setForeground(TEXT);

        center.add(overview);
        center.add(Box.createVerticalStrut(16));

        // STATISTICS CARDS
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 0));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));

        lostValue = new JLabel("0");
        foundValue = new JLabel("0");
        matchedValue = new JLabel("0");
        returnedValue = new JLabel("0");

        stats.add(createStatCard(
                "TOTAL LOST ITEMS", "📦", lostValue,
                new Color(105, 75, 190)));

        stats.add(createStatCard(
                "TOTAL FOUND ITEMS", "🔎", foundValue,
                new Color(40, 145, 180)));

        stats.add(createStatCard(
                "MATCHED ITEMS", "✓", matchedValue,
                new Color(50, 155, 110)));

        stats.add(createStatCard(
                "RETURNED ITEMS", "★", returnedValue,
                new Color(220, 145, 55)));

        center.add(stats);
        center.add(Box.createVerticalStrut(32));

        // QUICK ACTIONS
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Arial", Font.BOLD, 21));
        actionsTitle.setForeground(TEXT);

        center.add(actionsTitle);
        center.add(Box.createVerticalStrut(16));

        JPanel actions = new JPanel(new GridLayout(2, 2, 16, 16));
        actions.setOpaque(false);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 205));

        actions.add(createActionCard(
                "＋  Report Lost Item",
                "Register an item that you have lost.",
                () -> openExistingFeature(1)));

        actions.add(createActionCard(
                "＋  Report Found Item",
                "Register an item that you have found.",
                () -> openExistingFeature(2)));

        actions.add(createActionCard(
                "⌕  Search Matches",
                "Search for matching lost and found items.",
                () -> openExistingFeature(3)));

        actions.add(createActionCard(
                "▤  View All Items",
                "Browse all reported items.",
                () -> openExistingFeature(4)));

        center.add(actions);
        center.add(Box.createVerticalStrut(30));

        JLabel footer = new JLabel(
                "FindBack | Smart Lost & Found Matcher"
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setForeground(MUTED);

        center.add(footer);

        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setBackground(BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        content.add(header, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        return content;
    }

    // STATISTICS CARD
    private JPanel createStatCard(
            String title, String icon,
            JLabel value, Color accent) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(230, 228, 240)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 19));
        iconLabel.setForeground(accent);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 10));
        titleLabel.setForeground(MUTED);

        value.setFont(new Font("Arial", Font.BOLD, 27));
        value.setForeground(TEXT);

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(value);

        return card;
    }

    // QUICK ACTION CARD
    private JButton createActionCard(
            String title, String description, Runnable action) {

        JButton button = new JButton(
                "<html><div style='text-align:left;'>"
                + "<b>" + title + "</b><br><br>"
                + "<span style='font-size:11px;color:#777777;'>"
                + description
                + "</span></div></html>"
        );

        button.setFont(new Font("Arial", Font.PLAIN, 15));
        button.setForeground(DARK_PURPLE);
        button.setBackground(Color.WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 222, 240)),
                new EmptyBorder(18, 18, 18, 18)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_PURPLE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });

        button.addActionListener(e -> action.run());

        return button;
    }

    // LOAD DATABASE STATISTICS
    private void loadStatistics() {

        try {

            lostValue.setText(
                    String.valueOf(ItemDAO.getItemCount("lost")));

            foundValue.setText(
                    String.valueOf(ItemDAO.getItemCount("found")));

            matchedValue.setText(
                    String.valueOf(ItemDAO.getStatusCount("MATCHED")));

            returnedValue.setText(
                    String.valueOf(ItemDAO.getStatusCount("RETURNED")));

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load statistics:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // EXISTING GUI CONNECTION
    private FindBackGUI getExistingGUI() {

        if (existingGUI == null) {
            existingGUI = new FindBackGUI();
            existingGUI.setVisible(false);
        }

        return existingGUI;
    }

    // OPEN EXISTING FEATURES
    private void openExistingFeature(int feature) {

        try {

            FindBackGUI gui = getExistingGUI();

            switch (feature) {

                case 1:
                    gui.dashboardReportLost();
                    break;

                case 2:
                    gui.dashboardReportFound();
                    break;

                case 3:
                    gui.dashboardSearchMatches();
                    break;

                case 4:
                    gui.dashboardViewAllItems();
                    break;

                case 5:
                    gui.dashboardManageStatus();
                    break;
            }

            loadStatistics();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open feature:\n" + ex.getMessage(),
                    "FindBack Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new DashboardUI());

    }
}