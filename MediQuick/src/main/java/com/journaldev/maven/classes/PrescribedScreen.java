package com.journaldev.maven.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrescribedScreen extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JLabel photoLabel;
    private File photoFile;

    public PrescribedScreen(JPanel mainPanel, CardLayout cardLayout) {
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
        backButton.setBorder(new EmptyBorder(0, 10, 0, 10));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "PharmacyList");
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Full Name:");
        nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email (optional):");
        emailField = new JTextField();
        photoLabel = new JLabel("Upload Prescription Photo");

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(photoLabel);

        add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        JButton uploadButton = new JButton("Upload Photo");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPhoto();
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateAndSubmit();
            }
        });

        buttonPanel.add(uploadButton);
        buttonPanel.add(submitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void uploadPhoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            photoFile = fileChooser.getSelectedFile();
            photoLabel.setText("Photo: " + photoFile.getName());
        }
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

        if (photoFile == null) {
            JOptionPane.showMessageDialog(this, "Photo of prescription is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        storeUserData(name, phoneNumber, email, photoFile);
        JOptionPane.showMessageDialog(this, "Prescription submitted successfully. Wait for the pharmacy to message you updates.");
        cardLayout.show(mainPanel, "PharmacyList");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+?[0-9. ()-]{7,}$"; // Simple regex for phone number validation
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    private void storeUserData(String name, String phoneNumber, String email, File photoFile) {
        // Store the user data
        System.out.println("Full Name: " + name);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Photo: " + photoFile.getAbsolutePath());
    }
}
