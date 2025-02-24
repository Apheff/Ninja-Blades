package main;


import static utils.Constants.GamePanel.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class GameWindow {
    private JPanel leftBorder, rightBorder;
    private JFrame window;

    public GameWindow(JLayeredPane layeredPane) {
        // Create the main JFrame
        window = new JFrame();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setBackground(Color.black);

        // Set GridBagLayout for precise control
        window.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;  // Same height for all elements

        // Create left and right borders
        leftBorder = new JPanel();
        rightBorder = new JPanel();

        // Set debug colors
        leftBorder.setBackground(Color.black);
        rightBorder.setBackground(Color.black);

        // Set fixed dimensions for borders (¼ screen width each)
        leftBorder.setPreferredSize(new Dimension(BORDER_WIDTH, SCREEN_HEIGHT));
        rightBorder.setPreferredSize(new Dimension(BORDER_WIDTH, SCREEN_HEIGHT));

        // Add left border (¼ screen width)
        gbc.gridx = 0;  // Column 0
        gbc.weightx = 0.25;  // ¼ width
        window.add(leftBorder, gbc);

        // Add game panel (½ screen width)
        gbc.gridx = 1;  // Column 1
        gbc.weightx = 0.51;  // ½ width
        window.add(layeredPane, gbc);

        // Add right border (¼ screen width)
        gbc.gridx = 2;  // Column 2
        gbc.weightx = 0.25;  // ¼ width
        window.add(rightBorder, gbc);

        // Pack and show window
        window.setVisible(true);
    }
}
