package main;

import static utils.Constants.GameWindow.*;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.Dimension;

public class GameWindow {

    private JPanel leftBorder, rightBorder;
    private JFrame window;

    public GameWindow(JLayeredPane layeredPane) {
        // Create the main JFrame
        window = new JFrame();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen mode
        window.setPreferredSize(SCREEN_SIZE);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true); // Removes the title bar
        window.setLayout(new BorderLayout()); // Use BorderLayout

        // Create the black side borders
        leftBorder = new JPanel();
        rightBorder = new JPanel();

        // Set fixed dimensions for the black borders (scaled with the pixel density of the screen)
        leftBorder.setPreferredSize(new Dimension((int)(500/scaleFactor),(int)( 1080/scaleFactor))); // 500px wide borders, 1080px height
        rightBorder.setPreferredSize(new Dimension((int)(500/scaleFactor),(int)( 1080/scaleFactor))); 

        // Set the black color for the borders
        leftBorder.setBackground(new Color(0, 0, 0));
        rightBorder.setBackground(new Color(0, 0, 0));

        // Add the components to the JFrame
        window.add(leftBorder, BorderLayout.WEST);   // Left black border
        window.add(layeredPane, BorderLayout.CENTER);  // Game panel in the center
        window.add(rightBorder, BorderLayout.EAST);  // Right black border

        // Show the window
        window.setVisible(true);
    }
}
