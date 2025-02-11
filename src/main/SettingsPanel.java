package main;

import javax.swing.JPanel;
import java.awt.Graphics;

public class SettingsPanel extends JPanel{
    private volatile boolean running = true;

    public void stopSettings() {
        running = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Render settings elements here
        g.drawString("MENU", 0, 0);
    }
}
