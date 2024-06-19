package com.journaldev.maven.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PharmacyList extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel boxPanel;
    private double userLatitude;
    private double userLongitude;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public PharmacyList(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());
        
     // Create a header panel for the application name and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(640, 35));
        headerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        headerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel headerLabel = new JLabel("MediQuick");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        headerPanel.add(headerLabel, BorderLayout.WEST);
        
        JLabel backButton = new JLabel("Refresh", SwingConstants.RIGHT);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.setBorder(new EmptyBorder(0, 10, 0, 10));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "InitialScreen");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create a panel with a vertical BoxLayout to hold the boxes
        boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // Add margins (top, left, bottom, right)

        

        // Create a scroll pane to make the box panel scrollable
        JScrollPane scrollPane = new JScrollPane(boxPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create a main panel to hold the header and the scroll pane
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BorderLayout());
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainContentPanel);
    }

    public void setUserLocation(double latitude, double longitude) {
        this.userLatitude = latitude;
        this.userLongitude = longitude;
        fetchNearbyPharmacies();
    }

    private void fetchNearbyPharmacies() {
        try {
            // Use Overpass API to search for pharmacies within a 2.5 km radius
            String apiUrl = "https://overpass-api.de/api/interpreter?data=[out:json];node[amenity=pharmacy](around:2500," + userLatitude + "," + userLongitude + ");out;";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                // Parse the JSON response
                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
                JsonArray elements = jsonObject.getAsJsonArray("elements");

                ArrayList<Pharmacy> pharmacies = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                	if(i < 9) {
		                JsonObject element = elements.get(i).getAsJsonObject();
		                String name = element.has("tags") && element.getAsJsonObject("tags").has("name") ? element.getAsJsonObject("tags").get("name").getAsString() : "Unnamed Pharmacy";
		                String address = element.has("tags") && element.getAsJsonObject("tags").has("addr:street") ? element.getAsJsonObject("tags").get("addr:street").getAsString() : "Unknown Address";
		                double lat = element.get("lat").getAsDouble();
		                double lon = element.get("lon").getAsDouble();
		                int distance = (int) calculateDistance(userLatitude, userLongitude, lat, lon);
		                pharmacies.add(new Pharmacy(name, address, distance, lat, lon));
                    } else {break;}
                }

                // Sort the pharmacies by distance
                Collections.sort(pharmacies, Comparator.comparingDouble(Pharmacy::getDistance));

                // Clear existing boxes and add new ones
                boxPanel.removeAll();
                for (Pharmacy pharmacy : pharmacies) {
                    boxPanel.add(createBox(pharmacy));
                    boxPanel.add(Box.createVerticalStrut(10)); // Add space between boxes
                }
                boxPanel.revalidate();
                boxPanel.repaint();
            } else {
                System.err.println("API request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JPanel createBox(Pharmacy pharmacy) {
        JPanel box = new JPanel();
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        box.setPreferredSize(new Dimension(300, 80)); // Fix the size of each box
        box.setMaximumSize(new Dimension(300, 80));
        box.setMinimumSize(new Dimension(300, 80));
        box.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(pharmacy.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel addressLabel = new JLabel(pharmacy.getAddress());
        addressLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JLabel distanceLabel = new JLabel(String.format("%d meters", pharmacy.getDistance()));
        distanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        box.add(nameLabel, BorderLayout.NORTH);
        box.add(addressLabel, BorderLayout.CENTER);
        box.add(distanceLabel, BorderLayout.SOUTH);

        // Add mouse listener to handle click events
        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle the click event to open the pharmacy detail screen
                PharmacyDetailScreen pharmacyDetailScreen = (PharmacyDetailScreen) mainPanel.getComponent(2);
                pharmacyDetailScreen.setPharmacy(pharmacy);
                cardLayout.show(mainPanel, "PharmacyDetailScreen");
            }
        });

        return box;
    }

    private int calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        int distance = (int) (R * c * 1000); // Convert to meters
        return distance;
    }

    class Pharmacy {
        private String name;
        private String address;
        private int distance;
        private double latitude;
        private double longitude;

        public Pharmacy(String name, String address, int distance, double latitude, double longitude) {
            this.name = name;
            this.address = address;
            this.distance = distance;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public int getDistance() {
            return distance;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }
}
