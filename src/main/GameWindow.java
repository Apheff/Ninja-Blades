package main;

import javax.swing.JFrame;
import java.awt.Dimension;

public class GameWindow {

    final private int WIDTH = 640 , HEIGHT = 720 + 37;
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {

        jframe = new JFrame();
        jframe.setSize(new Dimension(WIDTH, HEIGHT));
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null); // this method display the JFrame to center position of a screen
        //jframe.setUndecorated(true); // removes the TitleBar
        jframe.setResizable(false);

        // makes the windows visible
        jframe.setVisible(true);
    }

} // end class