package main;

import javax.swing.JPanel;
import java.awt.Graphics;

public class SettingsPanel extends JPanel implements Runnable {
    private volatile boolean running = true;

    public void startSettings() {
        Thread settingsThread = new Thread(this);
        settingsThread.start();
    }

    @Override
    public void run() {
        while (running) {
            // Settings logic and rendering updates
            repaint();
            try {
                Thread.sleep(16); // Approximately 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopSettings() {
        running = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Render settings elements here
    }
}
