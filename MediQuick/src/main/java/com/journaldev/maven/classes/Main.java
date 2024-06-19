package com.journaldev.maven.classes;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pharmacy Locator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(360, 640);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            InitialScreen initialScreen = new InitialScreen(mainPanel, cardLayout);
            PharmacyList PharmacyList = new PharmacyList(mainPanel, cardLayout);
            PharmacyDetailScreen pharmacyDetailScreen = new PharmacyDetailScreen(mainPanel, cardLayout);
            NonPrescribedScreen nonPrescribedScreen = new NonPrescribedScreen(mainPanel, cardLayout);
            MedicineDetailScreen medicineDetailScreen = new MedicineDetailScreen(mainPanel, cardLayout);
            PrescribedScreen prescribedScreen = new PrescribedScreen(mainPanel, cardLayout);

            mainPanel.add(initialScreen, "InitialScreen");
            mainPanel.add(PharmacyList, "PharmacyList");
            mainPanel.add(pharmacyDetailScreen, "PharmacyDetailScreen");
            mainPanel.add(nonPrescribedScreen, "NonPrescribedScreen");
            mainPanel.add(medicineDetailScreen, "MedicineDetailScreen");
            mainPanel.add(prescribedScreen, "PrescribedScreen");

            frame.add(mainPanel);
            frame.setVisible(true);

            cardLayout.show(mainPanel, "InitialScreen");
        });
    }
}
