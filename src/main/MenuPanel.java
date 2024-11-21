package main;

import javax.swing.JPanel;
import java.awt.Graphics;

public class MenuPanel extends JPanel{

    Boolean running = true;

    public void stopMenu() {
        running = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Render menu elements here
        g.drawString("MENU", 0, 0);
    }
}

