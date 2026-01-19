import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

/**
 * VISIONARY BANKING OS v9.0 - COMPLETE EDITION
 * Features: Login -> Dashboard flow, premium animations, and Account Deletion.
 */
public class BankGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Bank bank;
    private Account currentAccount;
    
    // Components to update dynamically
    private JLabel balDisplay;
    private JTextArea terminalLog;
    private JLabel operatorTag;
    private double displayedBalance = 0;

    // --- VISIONARY UI TOKENS ---
    private final Color APP_BG = new Color(5, 5, 7);
    private final Color INDIGO_GLOW = new Color(99, 102, 241);
    private final Color PANEL_BG = new Color(15, 15, 20);
    private final Color SIDEBAR_BG = new Color(10, 10, 15);
    private final Color CARD_BG = new Color(25, 25, 30);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_MUTE = new Color(113, 113, 122);
    private final Color SUCCESS_GREEN = new Color(34, 197, 94);
    private final Color ERROR_RED = new Color(239, 68, 68);

    private final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 36);
    private final Font FONT_NAV = new Font("SansSerif", Font.BOLD, 14);
    private final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 10);
    private final Font FONT_VALUE = new Font("Monospaced", Font.BOLD, 56);

    public BankGUI() {
        bank = new Bank();
        setTitle("Visionary Universal Banking Terminal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(APP_BG);

        // Build Phases
        mainPanel.add(createLandingNode(), "LANDING");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "LANDING");
        setVisible(true);
    }

    // --- ANIMATION UTILS ---
    private void animateBalance(double target) {
        Timer timer = new Timer(20, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Math.abs(displayedBalance - target) < 0.1) {
                    displayedBalance = target;
                    timer.stop();
                } else {
                    displayedBalance += (target - displayedBalance) * 0.15;
                }
                balDisplay.setText("$" + String.format("%.2f", displayedBalance));
            }
        });
        timer.start();
    }

    // --- PHASE 1: LANDING / AUTH ---
    private JPanel createLandingNode() {
        JPanel landing = new JPanel(new GridBagLayout());
        landing.setBackground(APP_BG);

        AnimatedPanel identityCard = new AnimatedPanel();
        identityCard.setPreferredSize(new Dimension(500, 600));
        identityCard.setBackground(PANEL_BG);
        identityCard.setLayout(new BoxLayout(identityCard, BoxLayout.Y_AXIS));
        identityCard.setBorder(new CompoundBorder(
            new LineBorder(new Color(99, 102, 241, 50), 1),
            new EmptyBorder(60, 60, 60, 60)
        ));

        JLabel logo = new JLabel("🏦 VISIONARY");
        logo.setFont(FONT_TITLE);
        logo.setForeground(TEXT_WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("UNIVERSAL BANKING SYSTEM");
        sub.setFont(new Font("SansSerif", Font.BOLD, 10));
        sub.setForeground(INDIGO_GLOW);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField idInput = createStudioInput("ACCOUNT ID");
        JPasswordField pinInput = createStudioPasswordInput("PASSWORD");
        JTextField nameInput = createStudioInput("FULL NAME");
        nameInput.setVisible(false);

        JButton actionBtn = createAnimatedButton("AUTHORIZE ACCESS", INDIGO_GLOW);
        JButton toggleBtn = createGhostButton("CREATE NEW ACCOUNT");

        actionBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idInput.getText());
                String pin = new String(pinInput.getPassword());

                if (nameInput.isVisible()) {
                    String name = nameInput.getText();
                    if (name.isEmpty() || pin.length() < 4) {
                        showToast("ERROR: INVALID DETAILS", ERROR_RED);
                        return;
                    }
                    if (bank.findAccount(id) != null) {
                        showToast("ERROR: ID EXISTS", ERROR_RED);
                        return;
                    }
                    bank.addAccount(new Account(name, id, 0, pin));
                    showToast("Account created successfully.", SUCCESS_GREEN);
                    nameInput.setVisible(false);
                    actionBtn.setText("AUTHORIZE ACCESS");
                    toggleBtn.setText("CREATE NEW ACCOUNT");
                } else {
                    Account acc = bank.findAccount(id);
                    if (acc != null && acc.checkPassword(pin)) {
                        currentAccount = acc;
                        displayedBalance = acc.getBalance();
                        mainPanel.add(createDashboardOS(), "DASHBOARD");
                        cardLayout.show(mainPanel, "DASHBOARD");
                    } else {
                        showToast("ERROR: ACCESS DENIED", ERROR_RED);
                    }
                }
            } catch (Exception ex) {
                showToast("ERROR: INVALID ID", ERROR_RED);
            }
        });

        toggleBtn.addActionListener(e -> {
            boolean isLogin = actionBtn.getText().equals("AUTHORIZE ACCESS");
            nameInput.setVisible(isLogin);
            actionBtn.setText(isLogin ? "CREATE ACCOUNT" : "AUTHORIZE ACCESS");
            toggleBtn.setText(isLogin ? "ALREADY HAVE AN ACCOUNT? LOGIN" : "CREATE NEW ACCOUNT");
            identityCard.revalidate();
        });

        identityCard.add(logo);
        identityCard.add(Box.createVerticalStrut(5));
        identityCard.add(sub);
        identityCard.add(Box.createVerticalStrut(60));
        identityCard.add(idInput);
        identityCard.add(Box.createVerticalStrut(20));
        identityCard.add(nameInput);
        identityCard.add(Box.createVerticalStrut(20));
        identityCard.add(pinInput);
        identityCard.add(Box.createVerticalStrut(40));
        identityCard.add(actionBtn);
        identityCard.add(Box.createVerticalStrut(20));
        identityCard.add(toggleBtn);

        landing.add(identityCard);
        return landing;
    }

    // --- PHASE 2: FULL PAGE DASHBOARD OS ---
    private JPanel createDashboardOS() {
        JPanel os = new JPanel(new BorderLayout());
        os.setBackground(APP_BG);

        // 1. Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 0, 1, new Color(255,255,255,10)),
            new EmptyBorder(40, 25, 40, 25)
        ));

        JLabel sideLogo = new JLabel("⚡ VISIONARY");
        sideLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        sideLogo.setForeground(TEXT_WHITE);

        operatorTag = new JLabel("USER: " + currentAccount.getName().toUpperCase());
        operatorTag.setFont(new Font("SansSerif", Font.BOLD, 10));
        operatorTag.setForeground(INDIGO_GLOW);

        sidebar.add(sideLogo);
        sidebar.add(operatorTag);
        sidebar.add(Box.createVerticalStrut(60));

        CardLayout internalLayout = new CardLayout();
        JPanel workbench = new JPanel(internalLayout);
        workbench.setOpaque(false);

        JButton dashBtn = createNavButton("DASHBOARD");
        JButton logBtn = createNavButton("HISTORY");
        JButton delBtn = createNavButton("DELETE ACCOUNT");
        delBtn.setForeground(ERROR_RED.brighter());

        dashBtn.addActionListener(e -> internalLayout.show(workbench, "VIEW_DASH"));
        logBtn.addActionListener(e -> {
            refreshHistory();
            internalLayout.show(workbench, "VIEW_LOGS");
        });
        
        delBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "ARE YOU ABSOLUTELY SURE? \nThis will permanently remove your account and all assets.", 
                "DANGER ZONE", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String pwd = showInputDialog("Enter password to confirm deletion:");
                if (pwd != null && currentAccount.checkPassword(pwd)) {
                    bank.removeAccount(currentAccount.getId());
                    showToast("Account successfully deleted.", SUCCESS_GREEN);
                    cardLayout.show(mainPanel, "LANDING");
                } else if (pwd != null) {
                    showToast("Incorrect password. Deletion aborted.", ERROR_RED);
                }
            }
        });

        sidebar.add(dashBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(logBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(delBtn);

        sidebar.add(Box.createVerticalGlue());
        JButton logout = createAnimatedButton("DISCONNECT", ERROR_RED);
        logout.addActionListener(e -> cardLayout.show(mainPanel, "LANDING"));
        sidebar.add(logout);

        // Sub-Views
        workbench.add(createSummaryPanel(), "VIEW_DASH");
        workbench.add(createHistoryPanel(), "VIEW_LOGS");

        os.add(sidebar, BorderLayout.WEST);
        os.add(workbench, BorderLayout.CENTER);

        return os;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(30, 30));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("ACCOUNT OVERVIEW");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        header.add(title, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout(30,30));
        center.setOpaque(false);

        JPanel balCard = new AnimatedPanel();
        balCard.setBackground(INDIGO_GLOW);
        balCard.setPreferredSize(new Dimension(0, 250));
        balCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        JLabel balLabel = new JLabel("CURRENT BALANCE");
        balLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        balLabel.setForeground(new Color(255,255,255,180));
        
        balDisplay = new JLabel("$" + String.format("%.2f", currentAccount.getBalance()));
        balDisplay.setFont(FONT_VALUE);
        balDisplay.setForeground(Color.WHITE);
        animateBalance(currentAccount.getBalance());
        
        balCard.add(balLabel, BorderLayout.NORTH);
        balCard.add(balDisplay, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(1, 3, 20, 0));
        actions.setOpaque(false);
        actions.setPreferredSize(new Dimension(0, 100));

        JButton dep = createAnimatedButton("📥 DEPOSIT", SUCCESS_GREEN);
        JButton wit = createAnimatedButton("📤 WITHDRAW", new Color(245, 158, 11));
        JButton tra = createAnimatedButton("💱 TRANSFER", new Color(139, 92, 246));

        dep.addActionListener(e -> {
            String val = showInputDialog("Amount to deposit:");
            if (val != null) {
                String pwd = showInputDialog("Enter password to confirm deposit:");
                if (pwd != null && currentAccount.checkPassword(pwd)) {
                    try {
                        double amt = Double.parseDouble(val);
                        currentAccount.deposit(amt);
                        bank.saveAccounts();
                        animateBalance(currentAccount.getBalance());
                    } catch (Exception ex) { showToast("INVALID AMOUNT", ERROR_RED); }
                } else if (pwd != null) {
                    showToast("INCORRECT PASSWORD", ERROR_RED);
                }
            }
        });

        wit.addActionListener(e -> {
            String val = showInputDialog("Amount to withdraw:");
            if (val != null) {
                String pwd = showInputDialog("Enter password to confirm withdrawal:");
                if (pwd != null && currentAccount.checkPassword(pwd)) {
                    try {
                        double amt = Double.parseDouble(val);
                        if (currentAccount.withdraw(amt)) {
                            bank.saveAccounts();
                            animateBalance(currentAccount.getBalance());
                        } else { showToast("INSUFFICIENT FUNDS", ERROR_RED); }
                    } catch (Exception ex) { showToast("INVALID AMOUNT", ERROR_RED); }
                } else if (pwd != null) {
                    showToast("INCORRECT PASSWORD", ERROR_RED);
                }
            }
        });

        tra.addActionListener(e -> {
            String target = showInputDialog("Recipient ID:");
            String amtStr = showInputDialog("Amount to transfer:");
            if (target != null && amtStr != null) {
                String pwd = showInputDialog("Enter password to confirm transfer:");
                if (pwd != null && currentAccount.checkPassword(pwd)) {
                    try {
                        int toId = Integer.parseInt(target);
                        double amt = Double.parseDouble(amtStr);
                        if (bank.transfer(currentAccount.getId(), toId, amt)) {
                            animateBalance(currentAccount.getBalance());
                        } else { showToast("TRANSFER FAILED", ERROR_RED); }
                    } catch (Exception ex) { showToast("ERROR", ERROR_RED); }
                } else if (pwd != null) {
                    showToast("INCORRECT PASSWORD", ERROR_RED);
                }
            }
        });

        actions.add(dep); actions.add(wit); actions.add(tra);

        center.add(balCard, BorderLayout.NORTH);
        center.add(actions, BorderLayout.CENTER);

        panel.add(header, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(30, 30));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel title = new JLabel("TRANSACTION HISTORY");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_WHITE);
        panel.add(title, BorderLayout.NORTH);

        terminalLog = new JTextArea();
        terminalLog.setEditable(false);
        terminalLog.setBackground(CARD_BG);
        terminalLog.setForeground(TEXT_WHITE);
        terminalLog.setFont(new Font("Monospaced", Font.PLAIN, 16));
        terminalLog.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JScrollPane scroll = new JScrollPane(terminalLog);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new LineBorder(new Color(255,255,255,10), 1));
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshHistory() {
        StringBuilder sb = new StringBuilder();
        for (String s : currentAccount.getTransactionHistory()) {
            sb.append("• ").append(s).append("\n\n");
        }
        terminalLog.setText(sb.toString());
    }

    // --- ANIMATED COMPONENTS ---

    private JButton createAnimatedButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            private float hoverAlpha = 0f;
            private Timer timer;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { startAnimate(true); }
                    public void mouseExited(MouseEvent e) { startAnimate(false); }
                });
            }
            private void startAnimate(boolean in) {
                if (timer != null) timer.stop();
                timer = new Timer(15, e -> {
                    hoverAlpha += in ? 0.1f : -0.1f;
                    if (hoverAlpha >= 1f) { hoverAlpha = 1f; timer.stop(); }
                    if (hoverAlpha <= 0f) { hoverAlpha = 0f; timer.stop(); }
                    repaint();
                });
                timer.start();
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(baseColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 255, 255, (int)(hoverAlpha * 40)));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_NAV);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(15, 25, 15, 25));
        return btn;
    }

    private class AnimatedPanel extends JPanel {
        private float pulse = 0f;
        private boolean direction = true;
        public AnimatedPanel() {
            setOpaque(false);
            new Timer(50, e -> {
                pulse += direction ? 0.01f : -0.01f;
                if (pulse >= 0.2f) direction = false;
                if (pulse <= 0f) direction = true;
                repaint();
            }).start();
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.setColor(new Color(INDIGO_GLOW.getRed(), INDIGO_GLOW.getGreen(), INDIGO_GLOW.getBlue(), (int)(pulse * 100)));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
        }
    }

    private JTextField createStudioInput(String placeholder) {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        f.setBackground(CARD_BG);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setFont(new Font("SansSerif", Font.BOLD, 14));
        f.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(TEXT_MUTE, 1), placeholder,
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL, TEXT_MUTE
        ));
        return f;
    }

    private JPasswordField createStudioPasswordInput(String placeholder) {
        JPasswordField f = new JPasswordField();
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        f.setBackground(CARD_BG);
        f.setForeground(TEXT_WHITE);
        f.setCaretColor(TEXT_WHITE);
        f.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(TEXT_MUTE, 1), placeholder,
            TitledBorder.LEFT, TitledBorder.TOP, FONT_LABEL, TEXT_MUTE
        ));
        return f;
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(TEXT_MUTE);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(FONT_NAV);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(TEXT_WHITE); }
            public void mouseExited(MouseEvent e) { btn.setForeground(TEXT_MUTE); }
        });
        return btn;
    }

    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 10));
        btn.setForeground(TEXT_MUTE);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void showToast(String msg, Color bg) {
        UIManager.put("OptionPane.background", PANEL_BG);
        UIManager.put("Panel.background", PANEL_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
        JOptionPane.showMessageDialog(this, msg, "SYSTEM", JOptionPane.PLAIN_MESSAGE);
    }

    private String showInputDialog(String msg) {
        UIManager.put("OptionPane.background", PANEL_BG);
        UIManager.put("Panel.background", PANEL_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_WHITE);
        return JOptionPane.showInputDialog(this, msg, "INPUT", JOptionPane.QUESTION_MESSAGE);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        new BankGUI();
    }
}
