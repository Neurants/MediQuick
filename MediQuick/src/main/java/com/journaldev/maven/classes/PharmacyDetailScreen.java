package com.journaldev.maven.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public class PharmacyDetailScreen extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel mapLabel;
    private com.journaldev.maven.classes.PharmacyList.Pharmacy pharmacy;

    public PharmacyDetailScreen(JPanel mainPanel, CardLayout cardLayout) {
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
                cardLayout.show(mainPanel, "PharmacyList");
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Create a map label to show the pharmacy location
        mapLabel = new JLabel();
        mapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mapLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(mapLabel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton nonPrescribedButton = new JButton("Non-Prescribed");
        JButton prescribedButton = new JButton("Prescribed");
        prescribedButton.addActionListener(e -> cardLayout.show(mainPanel, "PrescribedScreen"));
        nonPrescribedButton.addActionListener(e -> cardLayout.show(mainPanel, "NonPrescribedScreen"));

        
        buttonPanel.add(nonPrescribedButton);
        buttonPanel.add(prescribedButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setPharmacy(com.journaldev.maven.classes.PharmacyList.Pharmacy pharmacy2) {
        this.pharmacy = pharmacy2;
        updateMap();
    }

    private void updateMap() {
        if (pharmacy != null) {
            double latitude = pharmacy.getLatitude();
            double longitude = pharmacy.getLongitude();
            
            double dLon = Math.toRadians(longitude - InitialScreen.getUserLongitude());
            double Bx = Math.cos(Math.toRadians(latitude)) * Math.cos(dLon);
            double By = Math.cos(Math.toRadians(latitude)) * Math.sin(dLon);
            double latitudeCenter = Math.toDegrees(Math.atan2(Math.sin(Math.toRadians(InitialScreen.getUserLatitude())) + Math.sin(Math.toRadians(latitude)), Math.sqrt((Math.cos(Math.toRadians(InitialScreen.getUserLatitude())) + Bx) * (Math.cos(Math.toRadians(InitialScreen.getUserLatitude())) + Bx) + By * By)));
            double longitudeCenter = Math.toDegrees(Math.toRadians(InitialScreen.getUserLongitude()) + Math.atan2(By, Math.cos(Math.toRadians(InitialScreen.getUserLatitude())) + Bx));
            

            try {
                String mapUrl = "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=360&height=450&center=lonlat:" + longitudeCenter + "," + latitudeCenter + "&zoom=13&marker=lonlat:" + InitialScreen.getUserLongitude() + "," + InitialScreen.getUserLatitude() + ";type:awesome;color:red;icon:landmark;icontype:material;iconsize:large;text:you;textsize:small;shadow:no|lonlat:" + longitude + "," + latitude + ";type:awesome;color:red;icon:landmark;textsize:small;shadow:no&apiKey=4af0c857b34b4e798aba9dfc32f8ae62";
                mapLabel.setIcon(new ImageIcon(new URL(mapUrl)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Pharmacy {
        private String name;
        private String address;
        private double distance;
        private double latitude;
        private double longitude;

        public Pharmacy(String name, String address, double distance, double latitude, double longitude) {
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

        public double getDistance() {
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
