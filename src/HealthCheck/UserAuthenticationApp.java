package HealthCheck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

public class UserAuthenticationApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;

    // In-memory user storage (replace with a database in a real application)
    private Map<String, String> users = new HashMap<>();

    public UserAuthenticationApp() {
        setTitle("User Authentication App");
        setSize(900, 400); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 4)); // Increased rows

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (users.containsKey(username) && users.get(username).equals(password)) {
                    JOptionPane.showMessageDialog(UserAuthenticationApp.this, "Login successful!");

                    // Open a new user dashboard frame
                    openUserDashboard(username);
                } else {
                    JOptionPane.showMessageDialog(UserAuthenticationApp.this, "Login failed. Please check your credentials.");
                }
            }
        });
        
        

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);

                if (users.containsKey(username)) {
                    JOptionPane.showMessageDialog(UserAuthenticationApp.this, "Username already exists. Please choose another one.");
                } else {
                    users.put(username, password);
                    JOptionPane.showMessageDialog(UserAuthenticationApp.this, "Registration successful!");
                }
            }
        });
    }
    

    private void openUserDashboard(String username) {
        // Create a new JFrame for the user dashboard
        final JFrame userDashboardFrame = new JFrame("User Dashboard: " + username);
        userDashboardFrame.setSize(900, 400);
        userDashboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add content to the user dashboard frame
        JPanel panel = new JPanel(new GridLayout(4, 1));

        JButton healthCheckButton = new JButton("Health Check");
        JButton doctorConsultButton = new JButton("Doctor Consultation");
        JButton getMedicineButton = new JButton("Get Medicine");

        panel.add(healthCheckButton);
        panel.add(doctorConsultButton);
        panel.add(getMedicineButton);

        userDashboardFrame.add(panel);

        // Display the user dashboard frame
        userDashboardFrame.setVisible(true);

        // Attach action listeners for the buttons
        healthCheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement health check functionality here
                JOptionPane.showMessageDialog(userDashboardFrame, "Performing health check...");
            }
        });

//        doctorConsultButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Implement doctor consultation functionality here
//                JOptionPane.showMessageDialog(userDashboardFrame, "Requesting doctor consultation...");
//            }
//        });

        getMedicineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement medicine selection process
                openMedicineSelectionDialog(userDashboardFrame);
            }
        });
        doctorConsultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the camera preview when the "Consult Doctor" button is clicked
                openCameraPreview();
            }
        });
    }
    private void openCameraPreview() {
        Webcam webcam = Webcam.getDefault(); // Get the default webcam
        webcam.setViewSize(WebcamResolution.VGA.getSize()); // Set the resolution

        WebcamPanel webcamPanel = new WebcamPanel(webcam); // Create a webcam panel
        webcamPanel.setFPSDisplayed(true); // Display frames per second
        webcamPanel.setDisplayDebugInfo(true); // Display debug information

        JFrame cameraFrame = new JFrame("Consult Doctor Camera");
        cameraFrame.add(webcamPanel, BorderLayout.CENTER);
        cameraFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cameraFrame.pack();
        cameraFrame.setVisible(true);

        // Start capturing from the webcam
        webcam.open();
    }
    
    
    private void openMedicineSelectionDialog(final JFrame parentFrame) {
        // Create a dialog for medicine selection
        final JDialog medicineDialog = new JDialog(parentFrame, "Select Medicine", true);
        medicineDialog.setSize(900, 400);
        medicineDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Add content to the medicine selection dialog
        JPanel panel = new JPanel(new GridLayout(0, 1)); // Use a single column for medicine options

        final JButton medicineButtonA = new JButton("Medicine A");
        final JButton medicineButtonB = new JButton("Medicine B");
        final JButton medicineButtonC = new JButton("Medicine C");
        final JTextField quantityField = new JTextField();
        final JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMedicine = ""; // Store the selected medicine here
                if (medicineButtonA.getModel().isPressed()) {
                    selectedMedicine = "Medicine A";
                } else if (medicineButtonB.getModel().isPressed()) {
                    selectedMedicine = "Medicine B";
                } else if (medicineButtonC.getModel().isPressed()) {
                    selectedMedicine = "Medicine C";
                }

                String quantity = quantityField.getText();

                // Implement buy functionality here
                JOptionPane.showMessageDialog(medicineDialog, "Buying " + quantity + " of " + selectedMedicine);

                // Open the receipt frame
                openReceiptFrame(selectedMedicine, quantity);
                medicineDialog.dispose(); // Close the medicine selection dialog
            }
        });
        panel.add(medicineButtonA);
        panel.add(medicineButtonB);
        panel.add(medicineButtonC);
        panel.add(new JLabel("Select Quantity:"));
        panel.add(quantityField);
        panel.add(buyButton);

        medicineDialog.add(panel);

        // Display the medicine selection dialog
        medicineDialog.setVisible(true);

        // Attach action listeners for the medicine buttons
        ActionListener medicineButtonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton selectedButton = (JButton) e.getSource();
                String selectedMedicine = selectedButton.getText();

                // Set the selected medicine in a JLabel or other component
                // You can also display the selected medicine elsewhere in your UI
                JOptionPane.showMessageDialog(medicineDialog, "Selected " + selectedMedicine);
            }
        };

        medicineButtonA.addActionListener(medicineButtonActionListener);
        medicineButtonB.addActionListener(medicineButtonActionListener);
        medicineButtonC.addActionListener(medicineButtonActionListener);

        // Attach action listener for the buy button
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMedicine = ""; // Store the selected medicine here
                if (medicineButtonA.getModel().isPressed()) {
                    selectedMedicine = "Medicine A";
                } else if (medicineButtonB.getModel().isPressed()) {
                    selectedMedicine = "Medicine B";
                } else if (medicineButtonC.getModel().isPressed()) {
                    selectedMedicine = "Medicine C";
                }

                String quantity = quantityField.getText();

                // Implement buy functionality here
                JOptionPane.showMessageDialog(medicineDialog, "Buying " + quantity + " of " + selectedMedicine);

                // Open the receipt frame
                openReceiptFrame(selectedMedicine, quantity);
                medicineDialog.dispose(); // Close the medicine selection dialog
            }
        });
    }
    

    private void openReceiptFrame(String selectedMedicine, String quantity) {
        // Create a new JFrame for the receipt
        final JFrame receiptFrame = new JFrame("Receipt");
        receiptFrame.setSize(800, 600); // Set the size as per your preference
        receiptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a JTextArea for displaying the receipt details
        final JTextArea receiptTextArea = new JTextArea();
        receiptTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptTextArea.setEditable(false);

        // Generate receipt content
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiptContent = "Receipt\n\n";
        receiptContent += "Date: " + dateFormat.format(new Date()) + "\n";
        receiptContent += "Medicine: " + selectedMedicine + "\n";
        receiptContent += "Quantity: " + quantity + "\n";

        receiptTextArea.setText(receiptContent);

        // Create a JScrollPane for the receipt text area
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);

        // Create a "Print" button for printing the receipt
        JButton printButton = new JButton("Print");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    receiptTextArea.print(); // Print the receipt text
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(receiptFrame, "Error printing receipt.");
                }
            }
        });

        // Create a JPanel to hold the receipt text area and the print button
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.add(scrollPane, BorderLayout.CENTER);
        receiptPanel.add(printButton, BorderLayout.SOUTH);

        // Add the receipt panel to the receipt frame
        receiptFrame.add(receiptPanel);

        // Display the receipt frame
        receiptFrame.setVisible(true);
    }

    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserAuthenticationApp app = new UserAuthenticationApp();
                app.setVisible(true);
            }
        });
    }
}
