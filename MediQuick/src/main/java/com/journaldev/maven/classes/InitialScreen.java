package com.journaldev.maven.classes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialScreen extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static double userLatitude;
    private static double userLongitude;
    private JPanel mainPanel;
    public InitialScreen(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("MediQuick", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);

        JButton proceedButton = new JButton("Proceed");
        proceedButton.setFont(new Font("Arial", Font.PLAIN, 18));
        
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "This application requires access to your location. Do you allow access?", "Location Access", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // Prompt user for location and retrieve it
                    getLocationFromOpenStreetMap();
                        // Pass the location data to the PharmacyList
                        PharmacyList PharmacyList = getPharmacyList();
                        if (PharmacyList != null) {
                            PharmacyList.setUserLocation(userLatitude, userLongitude);
                            cardLayout.show(mainPanel, "PharmacyList");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to switch to the next screen.");
                        }
                } else {
                    JOptionPane.showMessageDialog(null, "The application requires your location to proceed.");
                }
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(proceedButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private PharmacyList getPharmacyList() {
        for (Component component : mainPanel.getComponents()) {
            if (component instanceof PharmacyList) {
                return (PharmacyList) component;
            }
        }
        return null;
    }

    private void getLocationFromOpenStreetMap() {
    	// For demonstration purposes, fixed location
        userLatitude = 14.549416;
        userLongitude = 121.01279;
        
     // Uncomment the following code to use the OSM API to get the location
    	/*
        try {
            
        	
        	String apiUrl = "https://nominatim.openstreetmap.org/search?q=Your+Address&format=json&addressdetails=1&limit=1";

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                // Parse the JSON response
                JsonArray jsonArray = JsonParser.parseString(response).getAsJsonArray();
                if (jsonArray.size() > 0) {
                    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                    userLatitude = jsonObject.get("lat").getAsDouble();
                    userLongitude = jsonObject.get("lon").getAsDouble();
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "Location not found. Please try again.");
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "API request failed with response code: " + responseCode);
            }
            

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, "An error occurred while fetching location.");
        }
        */
    }

    public static double getUserLatitude() {
        return userLatitude;
    }

    public static double getUserLongitude() {
        return userLongitude;
    }
}
