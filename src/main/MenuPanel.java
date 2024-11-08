package main;

import javax.swing.JPanel;
import java.awt.Graphics;

public class MenuPanel extends JPanel implements Runnable {
    private volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            // Menu logic and rendering updates
            repaint();
            try {
                Thread.sleep(16); // Approximately 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

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

