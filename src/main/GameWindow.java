package main;

import javax.swing.JFrame;

// importing all the GameWindoo constants

public class GameWindow{


    public JFrame window = new JFrame();
    public GameWindow(GamePanel gamePanel) {
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setUndecorated(true); // removes the TitleBar
        window.add(gamePanel);
        // makes the windows visible
        window.setVisible(true);
    }

} // end class