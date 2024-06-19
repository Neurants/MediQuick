package com.journaldev.maven.classes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class NonPrescribedScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel boxPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private List<Medicine> medicines;
    
    
    
    public NonPrescribedScreen(JPanel mainPanel, CardLayout cardLayout) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());

     // Create a header panel for the application name and back button
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
                cardLayout.show(mainPanel, "PharmacyDetailScreen");
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);

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
        medicines = fetchMedicines();
        for (Medicine medicine : medicines) {
            boxPanel.add(createBox(medicine));
            boxPanel.add(Box.createVerticalStrut(10)); // Add space between boxes
        }
        boxPanel.revalidate();
        boxPanel.repaint();
    }

    private List<Medicine> fetchMedicines() {
        // Mock data for medicines. Replace with actual data fetching logic.
        List<Medicine> medicineList = new ArrayList<>();
        medicineList.add(new Medicine("Medicine A", "100 mg", "Headache", "$10"));
        medicineList.add(new Medicine("Medicine B", "200 mg", "Cold", "$15"));
        medicineList.add(new Medicine("Medicine C", "150 mg", "Cough", "$8"));
        return medicineList;
    }

    private JPanel createBox(Medicine medicine) {
        JPanel box = new JPanel();
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        box.setPreferredSize(new Dimension(300, 80)); // Fix the size of each box
        box.setMaximumSize(new Dimension(300, 80));
        box.setMinimumSize(new Dimension(300, 80));
        box.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("<html><b>" + medicine.getName() + " (" + medicine.getSize() + ")</b></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel purposeLabel = new JLabel(medicine.getPurpose());
        purposeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        JLabel priceLabel = new JLabel(medicine.getPrice());
        priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        box.add(nameLabel, BorderLayout.NORTH);
        box.add(purposeLabel, BorderLayout.WEST);
        box.add(priceLabel, BorderLayout.EAST);
        
        box.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MedicineDetailScreen medicineDetailScreen = new MedicineDetailScreen(mainPanel, cardLayout);
                mainPanel.add(medicineDetailScreen, "MedicineDetailScreen");
                cardLayout.show(mainPanel, "MedicineDetailScreen");
            }
        });
        
        return box;
    }

    class Medicine {
        private String name;
        private String size;
        private String purpose;
        private String price;

        public Medicine(String name, String size, String purpose, String price) {
            this.name = name;
            this.size = size;
            this.purpose = purpose;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getSize() {
            return size;
        }

        public String getPurpose() {
            return purpose;
        }

        public String getPrice() {
            return price;
        }
    }
}
