package com.journaldev.maven.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MedicineDetailScreen extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JTextField phoneField;
    private JTextField emailField;
    private JTextField nameField;

    public MedicineDetailScreen(JPanel mainPanel, CardLayout cardLayout) {
    	this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        setLayout(new BorderLayout());

        // Create header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(640, 35));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel backButton = new JLabel("<= Back", SwingConstants.LEFT);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorder(new EmptyBorder(0, 10, 0, 10));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "NonPrescribedScreen");
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email (optional):");
        emailField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        add(formPanel, BorderLayout.CENTER);

        // Create submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	validateAndSubmit();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void validateAndSubmit() {
        String name = nameField.getText();
        String phoneNumber = phoneField.getText();
        String email = emailField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        storeUserData(name, phoneNumber, email);
        JOptionPane.showMessageDialog(this, "Prescription submitted successfully. Wait for the pharmacy to message you updates.");
        cardLayout.show(mainPanel, "PharmacyList");
    }
    
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[0-9. ()-]{7,}$"; // Simple regex for phone number validation
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void storeUserData(String name, String phoneNumber, String email) {
        // Store the user data
    	System.out.println("Full Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
    }
}
