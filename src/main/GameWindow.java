package main;

import static utils.Constants.GamePanel.BORDER_WIDTH;
import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.SCREEN_HEIGHT;
import static utils.Constants.GamePanel.SCREEN_SIZE;
import static utils.Constants.GamePanel.SCREEN_WIDTH;

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
        window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        window.setMaximumSize(SCREEN_SIZE);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true); // Removes the title bar
        window.setLayout(new BorderLayout());
        window.setBackground(Color.black);

        // Create the black side borders
        leftBorder = new JPanel();  
        rightBorder = new JPanel();

        // Set fixed dimensions for the black borders (scaled with the pixel density of the screen)
        leftBorder.setSize(new Dimension(BORDER_WIDTH, PANEL_HEIGHT));
        rightBorder.setSize(new Dimension(BORDER_WIDTH, PANEL_HEIGHT));
        leftBorder.setPreferredSize(new Dimension(BORDER_WIDTH, PANEL_HEIGHT));
        rightBorder.setPreferredSize(new Dimension(BORDER_WIDTH, PANEL_HEIGHT));

        // Set the black color for the borders
        leftBorder.setBackground(Color.BLACK);
        rightBorder.setBackground(Color.BLACK);

        // Add the components to the JFrame
        window.add(leftBorder, BorderLayout.WEST);   // Left black border
        window.add(layeredPane, BorderLayout.CENTER);  // Game panel in the center
        window.add(rightBorder, BorderLayout.EAST);  // Right black border

        // Show the window
        window.setVisible(true);
    }
}
