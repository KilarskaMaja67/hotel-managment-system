import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

import Observer.HotelLogger;
import People.*;
import People.Employees.*;

public class HotelGUI extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private HotelSystemLogic logic;

    private boolean isDarkMode=true;

    private final Color DARK_BG = new Color(33, 33, 33);
    private final Color DARK_FG = new Color(230, 230, 230);
    private final Color DARK_FIELD_BG = new Color(50, 50, 50);

    private final Color LIGHT_BG_MAIN = new Color(240, 240, 240);
    private final Color LIGHT_BG_PANEL = new Color(255, 255, 255);
    private final Color LIGHT_FG = new Color(30, 30, 30);
    private final Color LIGHT_FIELD_BG = new Color(255, 255, 255);

    private final Color ACCENT_BLUE = new Color(72, 45, 179);

    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private static final String LOGIN_PANEL = "Login";
    private static final String REGISTER_PANEL = "Register";
    private static final String MANAGER_PANEL = "Manager";
    private static final String GUEST_PANEL = "Guest";
    private static final String RECEPTION_PANEL = "Reception";
    private static final String SECURITY_PANEL = "Security";
    private static final String MAID_PANEL = "Maid";
    private static final String MASSEUR_PANEL = "Masseur";

    public HotelGUI()
    {
        setupTheme();
        try{
            logic = new HotelSystemLogic();
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(this, "Database initialization error");
            System.exit(1);
        }

        setTitle("Hotel Management System");
        setSize(1000,750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout= new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(DARK_BG);

        mainPanel.add(createLoginPanel(), LOGIN_PANEL);
        mainPanel.add(createRegisterPanel(), REGISTER_PANEL);
        mainPanel.add(new JPanel(), MANAGER_PANEL);
        mainPanel.add(new JPanel(), GUEST_PANEL);
        mainPanel.add(new JPanel(), RECEPTION_PANEL);
        mainPanel.add(new JPanel(), SECURITY_PANEL);
        mainPanel.add(new JPanel(), MAID_PANEL);
        mainPanel.add(new JPanel(), MASSEUR_PANEL);

        add(mainPanel);
    }

    private void setupTheme() {
        Color bg = isDarkMode ? DARK_BG : LIGHT_BG_MAIN;
        Color panelBg = isDarkMode ? DARK_BG : LIGHT_BG_PANEL;
        Color fg = isDarkMode ? DARK_FG : LIGHT_FG;
        Color fieldBg = isDarkMode ? DARK_FIELD_BG : LIGHT_FIELD_BG;
        Color fieldFg = isDarkMode ? DARK_FG : LIGHT_FG;
        UIManager.put("Panel.background", panelBg);
        UIManager.put("Label.foreground", fg);
        UIManager.put("OptionPane.background", bg);
        UIManager.put("OptionPane.messageForeground", fg);

        UIManager.put("Button.background", fieldBg);
        UIManager.put("Button.foreground", fg);
        UIManager.put("Button.select", fieldBg);
        UIManager.put("Button.focus", new Color(0, 0, 0, 0));

        UIManager.put("TextField.background", fieldBg);
        UIManager.put("TextField.foreground", fieldFg);
        UIManager.put("TextField.caretForeground", fg);

        UIManager.put("PasswordField.background", fieldBg);
        UIManager.put("PasswordField.foreground", fieldFg);
        UIManager.put("PasswordField.caretForeground", fg);

        UIManager.put("TextArea.background", fieldBg);
        UIManager.put("TextArea.foreground", fieldFg);

        UIManager.put("ComboBox.background", fieldBg);
        UIManager.put("ComboBox.foreground", fg);
        UIManager.put("ComboBox.selectionBackground", ACCENT_BLUE);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);

        UIManager.put("TabbedPane.background", bg);
        UIManager.put("TabbedPane.foreground", fg);
        UIManager.put("TabbedPane.selected", fieldBg);
        UIManager.put("TabbedPane.contentAreaColor", fieldBg);
        UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
    }

    private void toggleTheme() {
        isDarkMode = !isDarkMode;
        setupTheme();

        Color newBg = isDarkMode ? DARK_BG : LIGHT_BG_MAIN;
        Color newPanelBg = isDarkMode ? DARK_BG : LIGHT_BG_PANEL;
        Color newFg = isDarkMode ? DARK_FG : LIGHT_FG;
        Color newFieldBg = isDarkMode ? DARK_FIELD_BG : LIGHT_FIELD_BG;
        Color newFieldFg = isDarkMode ? DARK_FG : LIGHT_FG;

        mainPanel.setBackground(newBg);
        getContentPane().setBackground(newBg);

        updateColorsRecursively(this, newBg, newPanelBg, newFg, newFieldBg, newFieldFg);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void updateColorsRecursively(Container container, Color bg, Color panelBg, Color fg, Color fieldBg, Color fieldFg) {
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(panelBg);
            }
            else if (c instanceof JLabel) {
                c.setForeground(fg);
            }
            else if (c instanceof JButton) {
                if (!c.getBackground().equals(ACCENT_BLUE)) {
                    c.setBackground(fieldBg);
                    c.setForeground(fg);
                }
            }
            else if (c instanceof javax.swing.text.JTextComponent) {
                c.setBackground(fieldBg);
                c.setForeground(fieldFg);
            }

            if (c instanceof Container) {
                updateColorsRecursively((Container) c, bg, panelBg, fg, fieldBg, fieldFg);
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(ACCENT_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addDatePlaceholder(JTextField field) {
        field.setText("YYYY-MM-DD");
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals("YYYY-MM-DD")) {
                    field.setText("");
                    field.setForeground(UIManager.getColor("TextField.foreground"));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText("YYYY-MM-DD");
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private JPanel createLoginPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton themeBtn = new JButton("☀/☾");
        themeBtn.setMargin(new Insets(2, 5, 2, 5));
        themeBtn.addActionListener(e -> toggleTheme());

        GridBagConstraints themeGbc = new GridBagConstraints();
        themeGbc.gridx = 2;
        themeGbc.gridy = 0;
        themeGbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(themeBtn, themeGbc);

        JLabel titleLabel = new JLabel("Welcome to the M4 Hotel System");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(ACCENT_BLUE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField loginField= new JTextField(20);
        JPasswordField passwordField=new JPasswordField(20);

        JButton loginButton = createStyledButton("Log in");

        JButton registerButton=new JButton("Create new Guest Account");
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(ACCENT_BLUE);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        panel.add(titleLabel, gbc);

        gbc.gridy=1; gbc.gridwidth = 1;
        panel.add(new JLabel("Login:"), gbc);
        gbc.gridx=1;
        panel.add(loginField, gbc);

        gbc.gridx=0;gbc.gridy=2;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx=1;
        panel.add(passwordField, gbc);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth =2;
        panel.add(loginButton, gbc);

        gbc.gridy=4;
        panel.add(registerButton, gbc);

        loginButton.addActionListener(e -> {
            String login = loginField.getText();
            String pass = new String(passwordField.getPassword());
            try{
                Person user = logic.handleLogin(login, pass);
                routeUserToPanel(user);
            } catch (Exception exception)
            {
                JOptionPane.showMessageDialog(this,"Error: "+exception.getMessage(),"Login error",JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(mainPanel,REGISTER_PANEL));

        return panel;
    }

    private JPanel createRegisterPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account");
        title.setFont(HEADER_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField nameField = new JTextField(15);
        JTextField surnameField = new JTextField(15);
        JTextField loginField = new JTextField(15);
        JPasswordField pass1Field = new JPasswordField(15);
        JPasswordField pass2Field = new JPasswordField(15);

        JButton confirmButton = createStyledButton("Register");
        JButton backButton = createStyledButton("Cancel");
        backButton.setBackground(UIManager.getColor("Button.background"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        panel.add(new JLabel("Name: "), gbc); gbc.gridx=1; panel.add(nameField, gbc);

        gbc.gridx=0; gbc.gridy = 2;
        panel.add(new JLabel("Surname: "), gbc); gbc.gridx=1; panel.add(surnameField, gbc);

        gbc.gridx=0; gbc.gridy = 3;
        panel.add(new JLabel("Login: "), gbc); gbc.gridx=1; panel.add(loginField, gbc);

        gbc.gridx=0; gbc.gridy = 4;
        panel.add(new JLabel("Password: "), gbc); gbc.gridx=1; panel.add(pass1Field, gbc);

        gbc.gridx=0; gbc.gridy = 5;
        panel.add(new JLabel("Repeat: "), gbc); gbc.gridx=1; panel.add(pass2Field, gbc);

        gbc.gridx=0; gbc.gridy = 6; gbc.gridwidth=2;
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.add(backButton);
        btnPanel.add(confirmButton);
        panel.add(btnPanel, gbc);

        confirmButton.addActionListener(e->{
            try{
                logic.handleRegistration(
                        nameField.getText(),surnameField.getText(),loginField.getText(),
                        new String(pass1Field.getPassword()), new String(pass2Field.getPassword())
                );
                JOptionPane.showMessageDialog(this, "Account created! You can now log in.");
                cardLayout.show(mainPanel,LOGIN_PANEL);
            } catch (Exception exception)
            {
                JOptionPane.showMessageDialog(this,exception.getMessage());
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel,LOGIN_PANEL));

        return panel;
    }

    private void routeUserToPanel(Person user)
    {
        if(logic.isFirstLogin(user))
        {
            handleFirstLoginChangePassword((Employee)user);
            return;
        }

        if(user instanceof Manager)
        {
            refreshManagerPanel((Manager)user);
            cardLayout.show(mainPanel,MANAGER_PANEL);
        }
        else if(user instanceof Guest)
        {
            refreshGuestPanel((Guest)user);
            cardLayout.show(mainPanel,GUEST_PANEL);
        }
        else if(user instanceof Recepcionist)
        {
            refreshReceptionistPanel((Recepcionist)user);
            cardLayout.show(mainPanel,RECEPTION_PANEL);
        }
        else if(user instanceof SecurityGuard)
        {
            refreshSecurityPanel((SecurityGuard)user);
            cardLayout.show(mainPanel,SECURITY_PANEL);
        }
        else if(user instanceof Maid)
        {
            refreshMaidPanel((Maid)user);
            cardLayout.show(mainPanel, MAID_PANEL);
        }
        else if(user instanceof Masseur)
        {
            refreshMasseurPanel((Masseur)user);
            cardLayout.show(mainPanel, MASSEUR_PANEL);
        }
        else
        {
            JOptionPane.showMessageDialog(this,"Panel for this role is WIP or Unknown.");
            logic.handleLogout();
        }
    }

    private void refreshManagerPanel(Manager manager) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Manager Panel: " + manager.getName());
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        header.setFont(HEADER_FONT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(header, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        JPanel employeesTab = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setOpaque(false);

        JLabel filterLabel = new JLabel("Filter by Role:");
        String[] filterRoles = {"All", "Manager", "Recepcionist", "SecurityGuard", "Maid", "Masseur"};
        JComboBox<String> roleFilterBox = new JComboBox<>(filterRoles);

        JButton filterBtn = createStyledButton("Filter");

        filterPanel.add(filterLabel);
        filterPanel.add(roleFilterBox);
        filterPanel.add(filterBtn);

        JTextArea employeeListArea = new JTextArea();
        employeeListArea.setEditable(false);
        employeeListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        employeeListArea.setMargin(new Insets(10, 10, 10, 10));

        filterBtn.addActionListener(e -> {
            String selectedRole = (String) roleFilterBox.getSelectedItem();
            String data = logic.displayEmployees(selectedRole);
            employeeListArea.setText(data);
        });

        employeeListArea.setText(logic.displayEmployees("All"));

        employeesTab.add(filterPanel, BorderLayout.NORTH);
        employeesTab.add(new JScrollPane(employeeListArea), BorderLayout.CENTER);


        JPanel hireTab = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel posLabel = new JLabel("Position:");
        String[] hireRoles = {"Manager", "Recepcionist", "SecurityGuard", "Maid", "Masseur"};
        JComboBox<String> positionBox = new JComboBox<>(hireRoles);
        ((JComponent) positionBox.getRenderer()).setOpaque(true);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        hireTab.add(posLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        hireTab.add(positionBox, gbc);

        String[] labels = {"Name:", "Surname:", "Login:", "Password:", "Rate (PLN):"};
        JTextField[] fields = {
                new JTextField(), new JTextField(), new JTextField(), new JTextField(), new JTextField(),
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.weightx = 0.3;
            hireTab.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1; gbc.weightx = 0.7;
            hireTab.add(fields[i], gbc);
        }

        JButton hireBtn = createStyledButton("Hire Employee");
        gbc.gridx = 0; gbc.gridy = labels.length+1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        hireTab.add(hireBtn, gbc);

        hireBtn.addActionListener(e -> {
            try {
                logic.handleHiring(
                        (String) positionBox.getSelectedItem(),
                        fields[0].getText(), fields[1].getText(), fields[2].getText(), fields[3].getText(),
                        Double.parseDouble(fields[4].getText())
                );
                JOptionPane.showMessageDialog(this, "Success! Employee hired.");
                for(int i=0; i<fields.length; i++) fields[i].setText("");
                employeeListArea.setText(logic.displayEmployees("All"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JPanel reportsTab = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setOpaque(false);

        JButton occReportBtn = new JButton("Occupancy Report");
        JButton cleanReportBtn = new JButton("Cleanliness Report");
        JTextArea reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setMargin(new Insets(10,10,10,10));

        String[] lastReportType = {null};

        occReportBtn.addActionListener(e -> {
            lastReportType[0] = "occupancy";
            reportArea.setText(logic.generateOccupancyReport());
        });
        cleanReportBtn.addActionListener(e -> {
            lastReportType[0] = "clean";
            reportArea.setText(logic.generateCleanlinessReport());
        });

        buttonsPanel.add(occReportBtn);
        buttonsPanel.add(cleanReportBtn);

        reportsTab.add(buttonsPanel, BorderLayout.NORTH);
        reportsTab.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        logic.addObserver(() -> SwingUtilities.invokeLater(() -> {
            if (lastReportType[0] != null) {
                if ("occupancy".equals(lastReportType[0])) {
                    reportArea.setText(logic.generateOccupancyReport());
                } else if ("clean".equals(lastReportType[0])) {
                    reportArea.setText(logic.generateCleanlinessReport());
                }
            }
        }));

        // PAYROLL TAB
        JPanel payrollTab = new JPanel(new GridBagLayout());
        GridBagConstraints pGbc = new GridBagConstraints();
        pGbc.insets = new Insets(10,10,10,10);
        pGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton payBtn = createStyledButton("Process Payroll & Close Month");

        JTextField addHoursLogin = new JTextField(10);
        addHoursLogin.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Employee Login", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JTextField addHoursAmount = new JTextField(5);
        addHoursAmount.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Hours to Add", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JButton addHoursBtn = new JButton("Add Hours");

        pGbc.gridx=0; pGbc.gridy=0; pGbc.gridwidth=2;
        payrollTab.add(payBtn, pGbc);

        pGbc.gridwidth=1; pGbc.gridy=1;
        payrollTab.add(addHoursLogin, pGbc);
        pGbc.gridx=1;
        payrollTab.add(addHoursAmount, pGbc);

        pGbc.gridx=0; pGbc.gridy=2; pGbc.gridwidth=2;
        payrollTab.add(addHoursBtn, pGbc);

        payBtn.addActionListener(e -> {
            String report = logic.processPayroll();
            JTextArea rArea = new JTextArea(report);
            rArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            rArea.setEditable(false);
            rArea.setRows(15);
            rArea.setColumns(50);
            JOptionPane.showMessageDialog(this, new JScrollPane(rArea), "Payroll Report", JOptionPane.INFORMATION_MESSAGE);
        });

        addHoursBtn.addActionListener(e -> {
            try {
                logic.addHoursToEmployee(addHoursLogin.getText(), Double.parseDouble(addHoursAmount.getText()));
                JOptionPane.showMessageDialog(this, "Hours added successfully.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        JPanel logsTab = new JPanel(new BorderLayout());
        logsTab.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(50, 205, 50));


        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        logsTab.add(logScroll, BorderLayout.CENTER);


        HotelLogger.addObserver(message -> {
            SwingUtilities.invokeLater(() -> {
                logArea.append(message + "\n");
                // Automatyczne przewijanie na sam dół
                logArea.setCaretPosition(logArea.getDocument().getLength());
            });
        });

        tabs.addTab("Employee List", employeesTab);
        tabs.addTab("Hire", hireTab);
        tabs.addTab("Reports", reportsTab);
        tabs.addTab("Payroll", payrollTab);
        tabs.addTab("System Logs", logsTab);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);

        mainPanel.add(panel, MANAGER_PANEL);
    }

    private void refreshGuestPanel(Guest guest) {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_BLUE);
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel nameLabel = new JLabel("Welcome, " + guest.getName());
        nameLabel.setFont(HEADER_FONT);
        nameLabel.setForeground(Color.WHITE);

        JLabel balanceLabel = new JLabel("Wallet: " + String.format("%.2f", guest.getWalletBalance()) + " PLN | Card: " + logic.getCardBalance(guest));
        balanceLabel.setFont(NORMAL_FONT);
        balanceLabel.setForeground(Color.WHITE);

        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(balanceLabel, BorderLayout.EAST);

        logic.addObserver(() -> {
            SwingUtilities.invokeLater(() -> {
                balanceLabel.setText("Wallet: " + String.format("%.2f", guest.getWalletBalance()) + " PLN | Card: " + logic.getCardBalance(guest));
            });
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(NORMAL_FONT);


        JPanel bookTab = new JPanel(new BorderLayout());
        JTextArea roomListArea = new JTextArea();
        roomListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        roomListArea.setEditable(false);

        JPanel topFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topFilter.setOpaque(false);


        JTextField maxPriceField = new JTextField(8);
        maxPriceField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Max Price", 0, 0, null, UIManager.getColor("TextField.foreground")));


        JTextField equipmentField = new JTextField(8);
        equipmentField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Equipment", 0, 0, null, UIManager.getColor("TextField.foreground")));

        String[] sortOptions = {"No Sort", "Price: Low to High", "Price: High to Low"};
        JComboBox<String> sortBox = new JComboBox<>(sortOptions);
        sortBox.setFont(new Font("Segoe UI", Font.BOLD, 14));


        JButton filterBtn = createStyledButton("Filter Offers");
        JButton showAvailBtn = createStyledButton("Check Availability (Dates)");

        topFilter.add(maxPriceField);
        topFilter.add(equipmentField);
        topFilter.add(sortBox);
        topFilter.add(filterBtn);

        JPanel bookingForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bookingForm.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] typeOptions = {"Standard","Family","Business", "VIP", "Suite"};
        JComboBox <String> typeBox = new JComboBox<> (typeOptions);
        typeBox.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField rStartField = new JTextField(10);
        rStartField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Check In", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JTextField rEndField = new JTextField(10);
        rEndField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Check Out", 0, 0, null, UIManager.getColor("TextField.foreground")));

        addDatePlaceholder(rStartField);
        addDatePlaceholder(rEndField);

        JButton bookBtn = createStyledButton("Book Room");

        bookingForm.add(showAvailBtn);
        bookingForm.add(typeBox);
        bookingForm.add(rStartField);
        bookingForm.add(rEndField);
        bookingForm.add(bookBtn);


        filterBtn.addActionListener(e -> {
            try {
                double price = 0;
                if (!maxPriceField.getText().isEmpty()) {
                    price = Double.parseDouble(maxPriceField.getText());
                }

                String equip = equipmentField.getText();

                int sortMode = sortBox.getSelectedIndex();

                String result = logic.filterRoomOffers(price, equip, sortMode);
                roomListArea.setText(result);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Price must be a number!");
            }
        });

        showAvailBtn.addActionListener(e ->{
            try {
                if(rStartField.getText().equals("YYYY-MM-DD") || rEndField.getText().equals("YYYY-MM-DD")){
                    JOptionPane.showMessageDialog(this, "Select dates to check availability.");
                    return;
                }
                roomListArea.setText(logic.getAvailableRoomsList(LocalDate.parse(rStartField.getText()), LocalDate.parse(rEndField.getText())));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.");
            }
        });

        bookBtn.addActionListener(e -> {
            try {
                String startText = rStartField.getText();
                String endText = rEndField.getText();
                String roomText = (String) typeBox.getSelectedItem();

                if (startText.equals("YYYY-MM-DD") || endText.equals("YYYY-MM-DD") || startText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter valid dates first.");
                    return;
                }

                String roomType =roomText;
                LocalDate startDate = LocalDate.parse(startText);
                LocalDate endDate = LocalDate.parse(endText);

                double estimatedPrice = logic.calculateReservationCost(roomType, startDate, endDate);

                int choice = JOptionPane.showConfirmDialog(
                        this,
                        "Estimated total price: " + estimatedPrice + " PLN\nDo you want to proceed?",
                        "Confirm Booking",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    String result = logic.makeReservation(guest, roomType, startDate, endDate);
                    JOptionPane.showMessageDialog(this, result);
                   // balanceLabel.setText("Balance: " + logic.getCardBalance(guest));
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        bookTab.add(topFilter, BorderLayout.NORTH);
        bookTab.add(new JScrollPane(roomListArea), BorderLayout.CENTER);
        bookTab.add(bookingForm, BorderLayout.SOUTH);


        JPanel myResTab = new JPanel(new BorderLayout());
        JTextArea myResArea = new JTextArea();
        myResArea.setEditable(false);
        myResArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        myResArea.setText(logic.getMyReservations(guest));

        logic.addObserver(() -> {
            SwingUtilities.invokeLater(() -> {
                myResArea.setText(logic.getMyReservations(guest));
            });
        });
        JPanel cancelPanel=new JPanel();
        cancelPanel.setOpaque(false);
        JTextField cancelIdField = new JTextField(8);
        cancelIdField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Res. ID", 0,0,null, UIManager.getColor("TextField.foreground")));
        JButton cancelBtn = createStyledButton("Cancel Reservation");
        cancelBtn.setForeground(new Color(228, 8, 8));

        cancelBtn.addActionListener(e -> {
            try {
                String res = logic.cancelReservation(guest, Integer.parseInt(cancelIdField.getText()));
                JOptionPane.showMessageDialog(this, res);
                myResArea.setText(logic.getMyReservations(guest));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter correct ID");
            }
        });

        cancelPanel.add(cancelIdField);
        cancelPanel.add(cancelBtn);

        myResTab.add(new JScrollPane(myResArea), BorderLayout.CENTER);
        myResTab.add(cancelPanel, BorderLayout.SOUTH);


        JPanel serviceTab = new JPanel(new GridBagLayout());
        GridBagConstraints sGbc = new GridBagConstraints();
        sGbc.gridx = 0; sGbc.fill = GridBagConstraints.HORIZONTAL;
        sGbc.insets = new Insets(10, 10, 10, 10);

        JLabel serviceLabel = new JLabel("Room service");
        serviceLabel.setFont(HEADER_FONT);
        serviceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints headerGbc = new GridBagConstraints();
        headerGbc.gridx = 0; headerGbc.gridy = 0; headerGbc.insets = new Insets(20, 0, 30, 0);
        serviceTab.add(serviceLabel, headerGbc);

        JButton openDoorBtn = createStyledButton("Open door (with card)");
        JButton breakfastBtn = createStyledButton("Order breakfast (50 PLN)");
        JButton spaQueueBtn = createStyledButton("Join Massage Queue (150 PLN)");

        serviceTab.add(openDoorBtn, sGbc);
        serviceTab.add(breakfastBtn, sGbc);
        serviceTab.add(spaQueueBtn, sGbc);

        openDoorBtn.addActionListener(e -> {
            String room = JOptionPane.showInputDialog("Enter room number:");
            try {
                if(room != null && logic.handleOpenDoor(guest, Integer.parseInt(room))) {
                    JOptionPane.showMessageDialog(this, "Doors open!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error. The card do not fit this room");
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        breakfastBtn.addActionListener(e -> {
            double price = 50.0;
            int confirm = JOptionPane.showConfirmDialog(this, "Charge " + price + " PLN to room?", "Confirm Order", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    logic.handleOrderService(guest, price, "Breakfast");
                    JOptionPane.showMessageDialog(this, "Ordered! Check balance.");
                    //balanceLabel.setText("Balance: " + logic.getCardBalance(guest));
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
            }
        });

        spaQueueBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Join the massage waiting list?\nYou will be charged 150 PLN when the masseur performs the service.",
                    "Confirm", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                logic.addToMassageQueue(guest.getLogin());
                JOptionPane.showMessageDialog(this, "You have been added to the queue!");
            }
        });


        JPanel actionsTab = new JPanel(new GridBagLayout());
        GridBagConstraints aGbc = new GridBagConstraints();
        aGbc.gridx = 0; aGbc.fill = GridBagConstraints.HORIZONTAL;
        aGbc.insets = new Insets(10, 10, 10, 10);

        JLabel checkInLabel = new JLabel("Self Check In");
        checkInLabel.setFont(HEADER_FONT);
        checkInLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints hGbc = new GridBagConstraints();
        headerGbc.gridx = 0; headerGbc.gridy = 0; headerGbc.insets = new Insets(20, 0, 30, 0);
        actionsTab.add(checkInLabel, hGbc);


        JTextField inIdField = new JTextField(10);
        inIdField.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Reservation ID", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JButton checkInBtn = createStyledButton("Check-in");

        actionsTab.add(inIdField, aGbc)
;
        actionsTab.add(checkInBtn, aGbc);
        checkInBtn.addActionListener(e -> {
            try {
                String result = logic.handleCheckInGuestVers(Integer.parseInt(inIdField.getText()));
                JOptionPane.showMessageDialog(this, result);
                inIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Error. ID must be a number!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        //portfel
        JPanel walletTab = new JPanel(new BorderLayout());
        walletTab.setBorder(new EmptyBorder(10,10,10,10));

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints wGbc = new GridBagConstraints();
        wGbc.insets = new Insets(5,5,5,5);

        JLabel wInfo = new JLabel("Add funds to Wallet");
        wInfo.setFont(HEADER_FONT);

        JPanel bankPanel = new JPanel();
        bankPanel.setBorder(BorderFactory.createTitledBorder("Bank Transfer"));
        JTextField amountField = new JTextField(10);
        JButton addBtn = createStyledButton("Add Funds");

        bankPanel.add(new JLabel("Amount: "));
        bankPanel.add(amountField);
        bankPanel.add(addBtn);

        JLabel noteLabel = new JLabel("<html><center>Note: Use Wallet for reservations.<br>Transfer to Card at Reception.</center></html>");
        noteLabel.setForeground(Color.GRAY);

        wGbc.gridx=0; wGbc.gridy=0; topPanel.add(wInfo, wGbc);
        wGbc.gridy=1; topPanel.add(bankPanel, wGbc);
        wGbc.gridy=2; topPanel.add(noteLabel, wGbc);

        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyArea.setText(guest.getHistoryString());

        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        walletTab.add(topPanel, BorderLayout.NORTH);
        walletTab.add(historyPanel, BorderLayout.CENTER);

        addBtn.addActionListener(e -> {
            try {
                String msg = logic.topUpGuestWallet(guest.getLogin(), Double.parseDouble(amountField.getText()));
                JOptionPane.showMessageDialog(this, msg);
                amountField.setText("");
                historyArea.setText(guest.getHistoryString());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        logic.addObserver(() -> SwingUtilities.invokeLater(() ->
                historyArea.setText(guest.getHistoryString())
        ));


        tabs.addTab("Book Room", bookTab);
        tabs.addTab("My wallet",walletTab);
        tabs.addTab("My Reservations", myResTab);
        tabs.addTab("Room Service", serviceTab);
        tabs.addTab("Check in", actionsTab);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);

        mainPanel.add(panel, GUEST_PANEL);
    }

    private void refreshReceptionistPanel(Recepcionist recepcionist) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Reception Panel: " + recepcionist.getName());
        header.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        header.setFont(HEADER_FONT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel actionsPanel = new JPanel(new GridBagLayout());
        actionsPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Operations", 0,0,null, UIManager.getColor("TextField.foreground")));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel resLabel = new JLabel("Reservation ID:");
        resLabel.setForeground(ACCENT_BLUE);
        JTextField inIdField = new JTextField(15);

        JButton checkInBtn = createStyledButton("Check-in Guest");
        JButton checkOutBtn = createStyledButton("Check-out Guest");
        JButton topUpBtn = createStyledButton("Top up Guest card");

        gbc.gridx = 0; gbc.gridy = 0;
        actionsPanel.add(resLabel, gbc);

        gbc.gridy = 1;
        actionsPanel.add(inIdField, gbc);

        gbc.gridy = 2;
        actionsPanel.add(checkInBtn, gbc);

        gbc.gridy = 3;
        actionsPanel.add(checkOutBtn, gbc);

        gbc.gridy = 4;
        actionsPanel.add(topUpBtn, gbc);

        JButton swapBtn = createStyledButton("Swap Room (Show Available)");
        gbc.gridy=5; actionsPanel.add(swapBtn, gbc);

        JButton blackBtn = createStyledButton("Blacklist Guest");
        blackBtn.setBackground(new Color(180, 50, 50));
        gbc.gridy=6; actionsPanel.add(blackBtn, gbc);

        gbc.gridy = 7; gbc.weighty = 1.0;
        actionsPanel.add(new JPanel(), gbc);

        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Guest List", 0,0,null,UIManager.getColor("TextField.foreground")));
        JTextArea guestListArea = new JTextArea();
        guestListArea.setEditable(false);
        guestListArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        guestListArea.setText(logic.displayGuests());
        listPanel.add(new JScrollPane(guestListArea), BorderLayout.CENTER);

        logic.addObserver(() -> SwingUtilities.invokeLater(() -> guestListArea.setText(logic.displayGuests())));

        contentPanel.add(actionsPanel);
        contentPanel.add(listPanel);
        panel.add(contentPanel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        panel.add(logoutBtn, BorderLayout.SOUTH);

        checkInBtn.addActionListener(e -> {
            try {
                String result = logic.handleCheckIn(Integer.parseInt(inIdField.getText()));
                JOptionPane.showMessageDialog(this, result);
                inIdField.setText("");
                guestListArea.setText(logic.displayGuests());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        checkOutBtn.addActionListener(e -> {
            try {
                String result = logic.handleCheckOut(Integer.parseInt(inIdField.getText()));
                JOptionPane.showMessageDialog(this, result);
                inIdField.setText("");
                guestListArea.setText(logic.displayGuests());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        topUpBtn.addActionListener(e -> {
            try {
                String amount = JOptionPane.showInputDialog("Enter amount:");
                JOptionPane.showMessageDialog(this, logic.topUpGuestCard(Integer.parseInt(inIdField.getText()), amount));
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        swapBtn.addActionListener(e -> {
            try {
                int resId = Integer.parseInt(inIdField.getText());
                String availableList = logic.getAvailableRoomsForSwap(resId);

                String newRoomStr = JOptionPane.showInputDialog(this,
                        availableList + "\n\nEnter new room number from list:",
                        "Swap Room Selection",
                        JOptionPane.QUESTION_MESSAGE);

                if (newRoomStr != null && !newRoomStr.isEmpty()) {
                    String res = logic.swapRoomForGuest(resId, Integer.parseInt(newRoomStr));
                    JOptionPane.showMessageDialog(this, res);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter valid Reservation ID first!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        blackBtn.addActionListener(e -> {
            String login = JOptionPane.showInputDialog("Enter Guest Login to BAN:");
            if(login != null && !login.isEmpty()) {
                try {
                    logic.addToBlacklist(login);
                    JOptionPane.showMessageDialog(this, "User " + login + " has been blacklisted.");
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        });

        mainPanel.add(panel, RECEPTION_PANEL);
    }

    private void refreshSecurityPanel(SecurityGuard guard) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("SECURITY: " + guard.getName(), SwingConstants.CENTER);
        header.setFont(HEADER_FONT);
        header.setBorder(new EmptyBorder(20,0,20,0));
        panel.add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel verifyBox = new JPanel(new FlowLayout());
        verifyBox.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Verify Person", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JTextField nameF = new JTextField(10); addDatePlaceholder(nameF); nameF.setText("Name");
        JTextField surF = new JTextField(10); addDatePlaceholder(surF); surF.setText("Surname");
        JButton verifyBtn = createStyledButton("Verify ID");

        verifyBox.add(nameF); verifyBox.add(surF); verifyBox.add(verifyBtn);

        JTextArea resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        resultArea.setBorder(new LineBorder(Color.GRAY));

        JButton banBtn = createStyledButton("ADD TO BLACKLIST");
        banBtn.setBackground(new Color(180, 50, 50));
        banBtn.setForeground(Color.WHITE);

        gbc.gridx=0; gbc.gridy=0;
        centerPanel.add(verifyBox, gbc);

        gbc.gridy=1;
        centerPanel.add(new JScrollPane(resultArea), gbc);

        gbc.gridy=2;
        centerPanel.add(banBtn, gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        panel.add(logoutBtn, BorderLayout.SOUTH);

        verifyBtn.addActionListener(e -> {
            resultArea.setText(logic.verifyPerson(nameF.getText(), surF.getText()));
        });

        banBtn.addActionListener(e -> {
            String login = JOptionPane.showInputDialog("Enter Login to BAN:");
            if(login != null && !login.isEmpty()) {
                try {
                    logic.addToBlacklist(login);
                    JOptionPane.showMessageDialog(this, "User " + login + " BANNED.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        mainPanel.add(panel, SECURITY_PANEL);
    }

    private void refreshMaidPanel(Maid maid) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Housekeeping Panel: " + maid.getName());
        header.setFont(HEADER_FONT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(new EmptyBorder(10,10,10,10));
        panel.add(header, BorderLayout.NORTH);

        JTextArea listArea = new JTextArea(logic.getMaidRoomList());
        listArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        listArea.setEditable(false);
        listArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        logic.addObserver(() -> SwingUtilities.invokeLater(() -> {
            listArea.setText(logic.getMaidRoomList());
        }));
        panel.add(new JScrollPane(listArea), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Actions", 0, 0, null, UIManager.getColor("TextField.foreground")));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField roomF = new JTextField(5);
        roomF.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Room No.", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JTextField costF = new JTextField(5);
        costF.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Minibar (PLN)", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JButton cleanBtn = createStyledButton("Mark Clean (+0.5h)");
        JButton defectBtn = createStyledButton("Report Defect");
        defectBtn.setBackground(new Color(180, 50, 50));
        JButton minibarBtn = createStyledButton("Charge Minibar");
        JButton logoutBtn = new JButton("Logout");

        gbc.gridx=0; gbc.gridy=0; actions.add(roomF, gbc);
        gbc.gridx=1; actions.add(cleanBtn, gbc);
        gbc.gridx=2; actions.add(defectBtn, gbc);

        gbc.gridx=0; gbc.gridy=1; actions.add(costF, gbc);
        gbc.gridx=1; gbc.gridwidth=2; actions.add(minibarBtn, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=3; actions.add(logoutBtn, gbc);

        cleanBtn.addActionListener(e -> {
            try {
                String res = logic.handleCleaningRoom(maid, Integer.parseInt(roomF.getText()));
                JOptionPane.showMessageDialog(this, res);
                listArea.setText(logic.getMaidRoomList());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        defectBtn.addActionListener(e -> {
            try {
                String res = logic.reportDefect(Integer.parseInt(roomF.getText()));
                JOptionPane.showMessageDialog(this, res);
                listArea.setText(logic.getMaidRoomList());
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        minibarBtn.addActionListener(e -> {
            try {
                String res = logic.refillMinibar(Integer.parseInt(roomF.getText()), Double.parseDouble(costF.getText()));
                JOptionPane.showMessageDialog(this, res);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        logoutBtn.addActionListener(e -> logout());

        panel.add(actions, BorderLayout.SOUTH);
        mainPanel.add(panel, MAID_PANEL);
    }

    private void refreshMasseurPanel(Masseur masseur) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Massage Panel: " + masseur.getName());
        header.setFont(HEADER_FONT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setBorder(new EmptyBorder(10,10,10,10));
        panel.add(header, BorderLayout.NORTH);

        JTextArea queueArea = new JTextArea(logic.getMassageQueue());
        queueArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        queueArea.setEditable(false);
        queueArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        panel.add(new JScrollPane(queueArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        bottom.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Service Control", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JTextField guestLogin = new JTextField(12);
        guestLogin.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Guest login", 0, 0, null, UIManager.getColor("TextField.foreground")));

        JButton doJob = createStyledButton("Perform Massage (+1h)");
        JButton refresh = createStyledButton("Refresh Queue");
        refresh.setBackground(new Color(100, 100, 100));
        JButton logout = createStyledButton("Logout");

        doJob.addActionListener(e -> {
            try {
                String res = logic.performMassage(masseur, guestLogin.getText());
                JOptionPane.showMessageDialog(this, res);
                queueArea.setText(logic.getMassageQueue());
                guestLogin.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        refresh.addActionListener(e -> queueArea.setText(logic.getMassageQueue()));
        logout.addActionListener(e -> logout());

        bottom.add(guestLogin);
        bottom.add(doJob);
        bottom.add(refresh);
        bottom.add(logout);

        panel.add(bottom, BorderLayout.SOUTH);
        mainPanel.add(panel, MASSEUR_PANEL);
    }

    private void handleFirstLoginChangePassword(Employee emp) {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        JPasswordField oldP = new JPasswordField();
        JPasswordField newP1 = new JPasswordField();
        JPasswordField newP2 = new JPasswordField();

        panel.add(new JLabel("Previous password:")); panel.add(oldP);
        panel.add(new JLabel("New password:")); panel.add(newP1);
        panel.add(new JLabel("Repeat new password:")); panel.add(newP2);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                logic.forcePasswordChange(emp, new String(newP1.getPassword()),
                        new String(newP2.getPassword()), new String(oldP.getPassword()));
                JOptionPane.showMessageDialog(this, "Password changed! Log in again.");
                cardLayout.show(mainPanel, LOGIN_PANEL);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                handleFirstLoginChangePassword(emp);
            }
        }
    }

    private void logout()
    {
        logic.handleLogout();
        cardLayout.show(mainPanel,LOGIN_PANEL);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            HotelGUI gui = new HotelGUI();
            gui.setVisible(true);
        });
    }
}
