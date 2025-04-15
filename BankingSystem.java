import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.net.URL; // For loading resources like images
import java.sql.*;

public class BankingSystem extends JFrame {

    // --- UI Styling Constants ---
    private static final Color PRIMARY_COLOR = new Color(0, 102, 204); // Professional Blue
    private static final Color SECONDARY_COLOR = new Color(240, 248, 255); // Light Alice Blue
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(50, 50, 50); // Dark Gray
    private static final Color BUTTON_COLOR = new Color(0, 153, 76); // Green for actions
    private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(200, 200, 200); // Light gray border

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TEXT_FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);

    // --- Panels ---
    // No need to declare them as instance variables if only created/used locally
    // JPanel customerRegistrationPanel, openAccountPanel, adminLoginPanel, transactionPanel, loanApplicationPanel, emiPaymentPanel, complaintPanel, fixedDepositPanel;

    // --- Database Credentials (Replace with your actual details) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/BankingLoanSystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Ajit@2002"; // Replace with your password


    // Constructor
    public BankingSystem() {
        setTitle("Apna Bank - Banking System"); // More specific title
        setSize(800, 700); // Increased size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add gaps between components
        getContentPane().setBackground(SECONDARY_COLOR); // Set background for the frame's content pane

        // --- Add Header ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- Create the Tabbed Pane ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(PRIMARY_COLOR);
        tabbedPane.setForeground(Color.WHITE);


        // --- Add Tabs with Icons (Placeholders) ---
        // You would replace "[ICON]" with actual ImageIcons
        // Example: new ImageIcon(getClass().getResource("/icons/customer.png"))
        tabbedPane.addTab("<html><body style='padding: 5px;'>Customer Registration</body></html>", null, createCustomerRegistrationPanel(), "Register a new customer");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Open Account</body></html>", null, createOpenAccountPanel(), "Open a new bank account");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Admin Login</body></html>", null, createAdminLoginPanel(), "Administrator login");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Deposit/Withdraw</body></html>", null, createTransactionPanel(), "Perform account transactions");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Loan Application</body></html>", null, createLoanApplicationPanel(), "Apply for a new loan");
        tabbedPane.addTab("<html><body style='padding: 5px;'>EMI Payment</body></html>", null, createEmiPaymentPanel(), "Pay loan installments");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Complaint Registration</body></html>", null, createComplaintPanel(), "Register a customer complaint");
        tabbedPane.addTab("<html><body style='padding: 5px;'>Fixed Deposit</body></html>", null, createFixedDepositPanel(), "Create a new fixed deposit");

        // Add padding around the tabbed pane content
        JPanel tabbedPaneHolder = new JPanel(new BorderLayout());
        tabbedPaneHolder.setBorder(new EmptyBorder(10, 10, 10, 10));
        tabbedPaneHolder.setBackground(SECONDARY_COLOR);
        tabbedPaneHolder.add(tabbedPane, BorderLayout.CENTER);

        // Add the tabbed pane holder to the frame
        add(tabbedPaneHolder, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // --- Helper Method to Create Styled Panel ---
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        // Add padding inside the panel
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true), // Subtle border
                BorderFactory.createEmptyBorder(25, 25, 25, 25) // Padding
        ));
        return panel;
    }

    // --- Helper Method to Style Labels ---
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    // --- Helper Method to Style Text Fields ---
    private JTextField styleTextField(JTextField textField) {
        textField.setFont(TEXT_FIELD_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5) // Padding inside text field
        ));
        textField.setColumns(15); // Suggest a default width
        return textField;
    }

    // --- Helper Method to Style Buttons ---
    private JButton styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFocusPainted(false); // Remove distracting focus border
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        button.setOpaque(true); // Necessary for background color on some LnFs
        button.setBorderPainted(false); // We use EmptyBorder for spacing

        // Optional: Add hover effect (changes background slightly)
        Color hoverColor = BUTTON_COLOR.brighter();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    // --- Create Header Panel ---
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0)); // Horizontal gap
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15)); // Padding

//        // --- Logo Placeholder ---
//        // Replace with actual ImageIcon loaded from a file/resource
//        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/logo.png"));
//        JLabel logoLabel = new JLabel(logoIcon);
//        logoLabel = new JLabel(" [LOGO] "); // Placeholder
//        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
//        logoLabel.setForeground(Color.WHITE);
//        headerPanel.add(logoLabel, BorderLayout.WEST);

        // --- Title ---
        JLabel titleLabel = new JLabel("ABC Bank", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add a dummy component on the right to balance the layout
//        headerPanel.add(Box.createHorizontalStrut(logoLabel.getPreferredSize().width), BorderLayout.EAST);

        return headerPanel;
    }


    // --- Database Connection Helper ---
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }


    // --- Create Customer Registration Panel ---
    private JPanel createCustomerRegistrationPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding around components
        gbc.anchor = GridBagConstraints.WEST; // Align labels to the left

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField nameField = styleTextField(new JTextField());
        panel.add(nameField, gbc);

        // Address
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Address:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField addressField = styleTextField(new JTextField());
        panel.add(addressField, gbc);

        // Phone
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField phoneField = styleTextField(new JTextField());
        panel.add(phoneField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField emailField = styleTextField(new JTextField());
        panel.add(emailField, gbc);

        // Register Button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton registerButton = styleButton(new JButton("Register Customer"));
        registerButton.addActionListener(e -> registerCustomer(nameField, addressField, phoneField, emailField));
        panel.add(registerButton, gbc);

        return panel;
    }

    // Register Customer in the database
    private void registerCustomer(JTextField nameField, JTextField addressField, JTextField phoneField, JTextField emailField) {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // Basic Validation
        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please fill in all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) { // Very basic email check
            JOptionPane.showMessageDialog(this, "⚠️ Please enter a valid email address.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = "INSERT INTO Customer (Name, Address, Phone, Email) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, name);
            pst.setString(2, address);
            pst.setString(3, phone);
            pst.setString(4, email);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ Customer Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Clear fields after successful registration
                nameField.setText("");
                addressField.setText("");
                phoneField.setText("");
                emailField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Registration Failed. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "❌ Registration Failed. Email or Phone might already exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create Open Account Panel
    private JPanel createOpenAccountPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Customer ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Customer ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField customerIdField = styleTextField(new JTextField());
        panel.add(customerIdField, gbc);

        // Initial Deposit
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Initial Deposit (>= 0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField initialDepositField = styleTextField(new JTextField());
        panel.add(initialDepositField, gbc);

        // Open Account Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton openAccountButton = styleButton(new JButton("Open Account"));
        openAccountButton.addActionListener(e -> openAccount(customerIdField, initialDepositField));
        panel.add(openAccountButton, gbc);

        return panel;
    }


    // Open Account in the database
    private void openAccount(JTextField customerIdField, JTextField initialDepositField) {
        int customerId;
        double initialDeposit;

        try {
            customerId = Integer.parseInt(customerIdField.getText().trim());
            initialDeposit = Double.parseDouble(initialDepositField.getText().trim());
            if (initialDeposit < 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Initial deposit cannot be negative.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for Customer ID and Initial Deposit.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }


        String query = "INSERT INTO Account (CustomerID, Balance) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Optional: Check if CustomerID exists in the Customer table first
            // (Requires another query)

            pst.setInt(1, customerId);
            pst.setDouble(2, initialDeposit);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ Account Opened Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                customerIdField.setText("");
                initialDepositField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Account Opening Failed. Customer ID might not exist.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "❌ Account Opening Failed. Ensure the Customer ID exists and is valid.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error opening account: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Create Admin Login Panel
    private JPanel createAdminLoginPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // More padding for login
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField usernameField = styleTextField(new JTextField());
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField();
        // Style password field similarly to text field
        passwordField.setFont(TEXT_FIELD_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.add(passwordField, gbc);


        // Login Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton loginButton = styleButton(new JButton("Login"));
        loginButton.addActionListener(e -> adminLogin(usernameField, passwordField));
        panel.add(loginButton, gbc);

        return panel;
    }

    // Admin Login in the system
    private void adminLogin(JTextField usernameField, JPasswordField passwordField) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter both username and password.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // IMPORTANT: Password should be hashed in a real system!
        // This is just a basic example.
        String query = "SELECT * FROM Employees WHERE username = ? AND password = ?"; // Use a secure password hash comparison in production

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password); // Compare hashed password in production
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "✅ Login Successful!", "Authentication", JOptionPane.INFORMATION_MESSAGE);
                // Here you would typically open an Admin Dashboard window/panel
                // For now, just clear fields
                usernameField.setText("");
                passwordField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid Username or Password.", "Authentication Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error during login: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Create Deposit/Withdraw Transaction Panel
    private JPanel createTransactionPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Account ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Account ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField accountIdField = styleTextField(new JTextField());
        panel.add(accountIdField, gbc);

        // Transaction Type
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Transaction Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JComboBox<String> transactionTypeBox = new JComboBox<>(new String[]{"Deposit", "Withdraw"});
        transactionTypeBox.setFont(TEXT_FIELD_FONT);
        transactionTypeBox.setBackground(Color.WHITE);
        panel.add(transactionTypeBox, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createStyledLabel("Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField amountField = styleTextField(new JTextField());
        panel.add(amountField, gbc);

        // Submit Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton transactionButton = styleButton(new JButton("Submit Transaction"));
        transactionButton.addActionListener(e -> handleTransaction(accountIdField, amountField, transactionTypeBox));
        panel.add(transactionButton, gbc);

        return panel;
    }

    // Handle Deposit/Withdraw transaction
    private void handleTransaction(JTextField accountIdField, JTextField amountField, JComboBox<String> transactionTypeBox) {
        int accountId;
        double amount;
        String transactionType = transactionTypeBox.getSelectedItem().toString();

        try {
            accountId = Integer.parseInt(accountIdField.getText().trim());
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Transaction amount must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for Account ID and Amount.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false); // Start transaction

            // Check current balance
            String checkQuery = "SELECT Balance FROM Account WHERE AccountID = ? FOR UPDATE"; // Lock row
            PreparedStatement pstCheck = con.prepareStatement(checkQuery);
            pstCheck.setInt(1, accountId);
            ResultSet rs = pstCheck.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "❌ Account not found.", "Transaction Error", JOptionPane.ERROR_MESSAGE);
                con.rollback();
                return;
            }
            double currentBalance = rs.getDouble("Balance");
            rs.close();
            pstCheck.close();

            // Perform transaction
            String updateQuery;
            if (transactionType.equals("Deposit")) {
                updateQuery = "UPDATE Account SET Balance = Balance + ? WHERE AccountID = ?";
            } else { // Withdraw
                if (currentBalance < amount) {
                    JOptionPane.showMessageDialog(this, "❌ Insufficient balance for withdrawal.", "Transaction Error", JOptionPane.WARNING_MESSAGE);
                    con.rollback();
                    return;
                }
                updateQuery = "UPDATE Account SET Balance = Balance - ? WHERE AccountID = ?";
            }

            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
            pstUpdate.setDouble(1, amount);
            pstUpdate.setInt(2, accountId);

            int result = pstUpdate.executeUpdate();

            if (result > 0) {
                // Optional: Log the transaction in a separate Transactions table
                con.commit(); // Commit transaction
                JOptionPane.showMessageDialog(this, "✅ Transaction Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                accountIdField.setText("");
                amountField.setText("");
            } else {
                con.rollback(); // Rollback if update failed unexpectedly
                JOptionPane.showMessageDialog(this, "❌ Transaction Failed. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
            pstUpdate.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException se) { se.printStackTrace(); } // Rollback on error
            JOptionPane.showMessageDialog(this, "⚠️ Error during transaction: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException se) { se.printStackTrace(); } // Ensure connection is closed
        }
    }


    // Create Loan Application Panel
    private JPanel createLoanApplicationPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Account ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Account ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField accountIdLoanField = styleTextField(new JTextField());
        panel.add(accountIdLoanField, gbc);

        // Loan Amount
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Loan Amount (> 0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField loanAmountField = styleTextField(new JTextField());
        panel.add(loanAmountField, gbc);

        // Tenure
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createStyledLabel("Tenure (Months > 0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField loanTenureField = styleTextField(new JTextField());
        panel.add(loanTenureField, gbc);

        // Apply Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton loanButton = styleButton(new JButton("Apply for Loan"));
        loanButton.addActionListener(e -> applyForLoan(accountIdLoanField, loanAmountField, loanTenureField));
        panel.add(loanButton, gbc);

        return panel;
    }

    // Apply for Loan in the database
    private void applyForLoan(JTextField accountIdLoanField, JTextField loanAmountField, JTextField loanTenureField) {
        int accountId;
        double loanAmount;
        int loanTenure;

        try {
            accountId = Integer.parseInt(accountIdLoanField.getText().trim());
            loanAmount = Double.parseDouble(loanAmountField.getText().trim());
            loanTenure = Integer.parseInt(loanTenureField.getText().trim());

            if (loanAmount <= 0 || loanTenure <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Loan Amount and Tenure must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Assuming a LoanApplications table exists
        String query = "INSERT INTO LoanApplications (AccountID, LoanAmount, Tenure, ApplicationDate, Status) VALUES (?, ?, ?, CURDATE(), 'Pending')";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Optional: Check if AccountID exists

            pst.setInt(1, accountId);
            pst.setDouble(2, loanAmount);
            pst.setInt(3, loanTenure);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ Loan Application Submitted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                accountIdLoanField.setText("");
                loanAmountField.setText("");
                loanTenureField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Loan Application Failed. Please check Account ID.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(this, "❌ Loan Application Failed. Ensure the Account ID exists.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error submitting loan application: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Create EMI Payment Panel
    private JPanel createEmiPaymentPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Loan ID (assuming you have a unique LoanID, not just AccountID)
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Loan ID:"), gbc); // Changed from Loan Account ID for clarity
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField loanIdField = styleTextField(new JTextField());
        panel.add(loanIdField, gbc);

        // EMI Amount
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("EMI Amount:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField emiAmountField = styleTextField(new JTextField());
        panel.add(emiAmountField, gbc);

        // Pay EMI Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton emiPaymentButton = styleButton(new JButton("Pay EMI"));
        emiPaymentButton.addActionListener(e -> payEmi(loanIdField, emiAmountField));
        panel.add(emiPaymentButton, gbc);

        return panel;
    }

    // Pay EMI
    private void payEmi(JTextField loanIdField, JTextField emiAmountField) {
        int loanId; // Assuming you have a LoanID in your Loan table
        double emiAmount;

        try {
            loanId = Integer.parseInt(loanIdField.getText().trim());
            emiAmount = Double.parseDouble(emiAmountField.getText().trim());
            if (emiAmount <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ EMI amount must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for Loan ID and EMI Amount.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Assumption: You have a 'Loans' table with LoanID (PK), RemainingAmount, etc.
        // This query reduces the remaining amount. In a real system, you'd also record
        // the payment date, transaction ID, etc., likely in a LoanPayments table.
        String query = "UPDATE Loans SET RemainingAmount = RemainingAmount - ? WHERE LoanID = ? AND RemainingAmount >= ?";

        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false); // Transaction

            // First, check if the loan exists and has sufficient balance
            String checkQuery = "SELECT RemainingAmount FROM Loans WHERE LoanID = ? FOR UPDATE";
            PreparedStatement pstCheck = con.prepareStatement(checkQuery);
            pstCheck.setInt(1, loanId);
            ResultSet rs = pstCheck.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "❌ Loan ID not found.", "Payment Error", JOptionPane.ERROR_MESSAGE);
                con.rollback();
                return;
            }

            double remaining = rs.getDouble("RemainingAmount");
            // Allow payment even if it's slightly more than remaining (final payment)
            // Or add stricter check: if (emiAmount > remaining + some_tolerance) { error }


            rs.close();
            pstCheck.close();

            // Now, perform the update
            PreparedStatement pstUpdate = con.prepareStatement(query);
            pstUpdate.setDouble(1, emiAmount);
            pstUpdate.setInt(2, loanId);
            pstUpdate.setDouble(3, 0); // Ensure we don't make remaining amount negative (though DB constraint is better)

            int result = pstUpdate.executeUpdate();

            if (result > 0) {
                // TODO: Log this payment in a LoanPayments table
                con.commit();
                JOptionPane.showMessageDialog(this, "✅ EMI Payment Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loanIdField.setText("");
                emiAmountField.setText("");
            } else {
                con.rollback();
                // Could be due to loan not found again (race condition unlikely with FOR UPDATE)
                // or remaining amount became zero/negative just before update
                JOptionPane.showMessageDialog(this, "❌ EMI Payment Failed. Please check Loan ID or status.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
            pstUpdate.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException se) { se.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "⚠️ Error during EMI payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException se) { se.printStackTrace(); }
        }
    }


    // Create Complaint Registration Panel
    private JPanel createComplaintPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Account ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Account ID (Optional):"), gbc); // Make optional?
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField accountIdComplaintField = styleTextField(new JTextField());
        panel.add(accountIdComplaintField, gbc);

        // Complaint Text Area
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST; // Align label top-left
        panel.add(createStyledLabel("Complaint Details:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0; // Allow text area to grow
        JTextArea complaintArea = new JTextArea(5, 20);
        complaintArea.setFont(TEXT_FIELD_FONT);
        complaintArea.setLineWrap(true);
        complaintArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(complaintArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        panel.add(scrollPane, gbc);

        // Submit Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE; gbc.weighty = 0.0;
        JButton submitComplaintButton = styleButton(new JButton("Submit Complaint"));
        submitComplaintButton.addActionListener(e -> submitComplaint(accountIdComplaintField, complaintArea));
        panel.add(submitComplaintButton, gbc);

        return panel;
    }

    // Submit Complaint to the database
    private void submitComplaint(JTextField accountIdField, JTextArea complaintArea) {
        String accountIdStr = accountIdField.getText().trim();
        String complaintText = complaintArea.getText().trim();
        Integer accountId = null; // Use Integer wrapper class for nullability

        if (complaintText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter complaint details.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Try parsing Account ID if provided, but don't fail if it's empty
        if (!accountIdStr.isEmpty()) {
            try {
                accountId = Integer.parseInt(accountIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid Account ID format. Please enter a number or leave blank.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }


        // Assuming a Complaint table exists with ComplaintID (auto-increment), AccountID (nullable FK), ComplaintText, ComplaintDate, Status
        String query = "INSERT INTO Complaint (AccountID, ComplaintText, ComplaintDate, Status) VALUES (?, ?, CURDATE(), 'Open')";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            if (accountId != null) {
                pst.setInt(1, accountId);
            } else {
                pst.setNull(1, Types.INTEGER); // Handle NULL for AccountID
            }
            pst.setString(2, complaintText);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "✅ Complaint Submitted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                accountIdField.setText(""); // Clear fields
                complaintArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to Submit Complaint.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error submitting complaint: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Panel for Fixed Deposit
    private JPanel createFixedDepositPanel() {
        JPanel panel = createStyledPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Account ID
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createStyledLabel("Source Account ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField fixedDepositAccountIdField = styleTextField(new JTextField());
        panel.add(fixedDepositAccountIdField, gbc);

        // Deposit Amount
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0.0;
        panel.add(createStyledLabel("Deposit Amount (> 0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField fixedDepositAmountField = styleTextField(new JTextField());
        panel.add(fixedDepositAmountField, gbc);

        // Tenure
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createStyledLabel("Tenure (Months > 0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        JTextField fixedDepositTenureField = styleTextField(new JTextField());
        panel.add(fixedDepositTenureField, gbc);

        // Create FD Button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        JButton fixedDepositButton = styleButton(new JButton("Create Fixed Deposit"));
        fixedDepositButton.addActionListener(e -> createFixedDeposit(
                fixedDepositAccountIdField, fixedDepositAmountField, fixedDepositTenureField));
        panel.add(fixedDepositButton, gbc);

        return panel;
    }

    // Create Fixed Deposit
    private void createFixedDeposit(JTextField fixedDepositAccountIdField, JTextField fixedDepositAmountField, JTextField fixedDepositTenureField) {
        int accountId;
        double depositAmount;
        int tenure;

        try {
            accountId = Integer.parseInt(fixedDepositAccountIdField.getText().trim());
            depositAmount = Double.parseDouble(fixedDepositAmountField.getText().trim());
            tenure = Integer.parseInt(fixedDepositTenureField.getText().trim());

            if (depositAmount <= 0 || tenure <= 0) {
                JOptionPane.showMessageDialog(this, "⚠️ Deposit Amount and Tenure must be positive.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Please enter valid numbers for all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection con = null;
        // This requires a transaction: 1. Deduct from Account, 2. Insert into FixedDeposits
        try {
            con = getConnection();
            con.setAutoCommit(false); // Start transaction

            // 1. Check balance and deduct from source account
            String checkQuery = "SELECT Balance FROM Account WHERE AccountID = ? FOR UPDATE";
            PreparedStatement pstCheck = con.prepareStatement(checkQuery);
            pstCheck.setInt(1, accountId);
            ResultSet rs = pstCheck.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "❌ Source Account not found.", "FD Error", JOptionPane.ERROR_MESSAGE);
                con.rollback();
                return;
            }
            double currentBalance = rs.getDouble("Balance");
            if (currentBalance < depositAmount) {
                JOptionPane.showMessageDialog(this, "❌ Insufficient balance in source account.", "FD Error", JOptionPane.WARNING_MESSAGE);
                con.rollback();
                return;
            }
            rs.close();
            pstCheck.close();

            // Deduct amount
            String updateQuery = "UPDATE Account SET Balance = Balance - ? WHERE AccountID = ?";
            PreparedStatement pstUpdate = con.prepareStatement(updateQuery);
            pstUpdate.setDouble(1, depositAmount);
            pstUpdate.setInt(2, accountId);
            int updateResult = pstUpdate.executeUpdate();
            pstUpdate.close();

            if (updateResult <= 0) {
                JOptionPane.showMessageDialog(this, "❌ Failed to deduct amount from source account.", "FD Error", JOptionPane.ERROR_MESSAGE);
                con.rollback();
                return;
            }

            // 2. Insert into FixedDeposits table
            // Assuming FixedDeposits table: FD_ID (auto), AccountID, PrincipalAmount, Tenure, InterestRate, StartDate, MaturityDate, Status
            // InterestRate calculation would likely happen here based on amount/tenure rules
            double interestRate = calculateInterestRate(depositAmount, tenure); // Example helper
            String insertQuery = "INSERT INTO FixedDeposits (AccountID, PrincipalAmount, Tenure, InterestRate, StartDate, MaturityDate, Status) VALUES (?, ?, ?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? MONTH), 'Active')";
            PreparedStatement pstInsert = con.prepareStatement(insertQuery);
            pstInsert.setInt(1, accountId);
            pstInsert.setDouble(2, depositAmount);
            pstInsert.setInt(3, tenure);
            pstInsert.setDouble(4, interestRate); // Calculated rate
            pstInsert.setInt(5, tenure); // For DATE_ADD interval

            int insertResult = pstInsert.executeUpdate();
            pstInsert.close();


            if (insertResult > 0) {
                con.commit(); // Commit transaction
                JOptionPane.showMessageDialog(this, "✅ Fixed Deposit Created Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                fixedDepositAccountIdField.setText("");
                fixedDepositAmountField.setText("");
                fixedDepositTenureField.setText("");
            } else {
                con.rollback(); // Rollback if insert failed
                JOptionPane.showMessageDialog(this, "❌ Fixed Deposit Creation Failed after deduction. Rolled back.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            try { if (con != null) con.rollback(); } catch (SQLException se) { se.printStackTrace(); }
            JOptionPane.showMessageDialog(this, "⚠️ Error creating Fixed Deposit: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (SQLException se) { se.printStackTrace(); }
        }
    }

    // Dummy interest rate calculation - replace with actual bank logic
    private double calculateInterestRate(double amount, int tenureMonths) {
        if (tenureMonths < 6) return 3.0;
        if (tenureMonths < 12) return 4.5;
        if (tenureMonths < 24) return 5.5;
        if (amount < 100000) return 6.0;
        return 6.5; // Example rates
    }


    // Main method
    public static void main(String[] args) {
        // Set Look and Feel (Nimbus recommended)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to the default
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Failed to set LookAndFeel.");
            }
        }

        // Ensure GUI creation is done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new BankingSystem();
        });
    }
}