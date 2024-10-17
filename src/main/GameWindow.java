package main;

import javax.swing.JFrame;
import java.awt.Dimension;

// importing all the GameWindoo constants
import static utils.Constants.GameWindow.*;

public class GameWindow {

    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();
        jframe.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        jframe.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(null);
        jframe.setUndecorated(true); // removes the TitleBar
    
        jframe.add(gamePanel);
        // makes the windows visible
        jframe.setVisible(true);
    }

} // end class