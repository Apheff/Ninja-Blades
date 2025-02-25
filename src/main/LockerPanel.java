package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import utils.ImageLoader;
import static utils.Constants.GamePanel.*;

public class LockerPanel extends JPanel implements KeyListener {

    private MainClass mainClass;
    private int selectedTheme = 0; // default: 0
    private BufferedImage[] themeImages;
    private String[] themeNames = {"CLASSIC THEME", "RICK AND MORTY THEME", "Tema Neon"};
    private boolean active = false;

    public LockerPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setMaximumSize(PANEL_SIZE);

        themeImages = new BufferedImage[3];
        themeImages[0] = ImageLoader.loadImage("wallpaper0.jpg");
        themeImages[1] = ImageLoader.loadImage("wallpaper1.jpg");
        themeImages[2] = ImageLoader.loadImage("wallpaper2.jpg");
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!active) return;
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scaleFactor, scaleFactor);

        // Sfondo scuro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Disegna il tema selezionato con ombra gialla
        for (int i = 0; i < themeImages.length; i++) {
            int x = (PANEL_WIDTH / 2 - 150) + (i - selectedTheme) * 350;
            int y = PANEL_HEIGHT / 3;


            if (i == selectedTheme) {
                g2d.setColor(customYellow);
                g2d.fillRoundRect(x - 10, y - 10, 320, 220, 20, 20);
            }
            
            g2d.drawImage(themeImages[i], x, y, 300, 200, null);
            
            // Nome del tema
            g2d.setFont(customFont);
            String text = themeNames[i];
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.setColor(i == selectedTheme ? Color.YELLOW : Color.WHITE);
            g2d.drawString(text, x + (300 - textWidth) / 2, y + 234);
        }


        // arrow indications 
        String left = "<";
        String right = ">";
        int leftWidth = g2d.getFontMetrics().stringWidth(left);
        int rightWidth = g2d.getFontMetrics().stringWidth(right);
        g2d.setColor(Color.BLACK);
        g2d.drawString(right, (PANEL_WIDTH / 4 * 3) + rightWidth + 32, PANEL_HEIGHT / 3 + 96 + 2);
        g2d.drawString(left, (PANEL_WIDTH / 4) - leftWidth - 32, PANEL_HEIGHT / 3 + 96 + 2);
        g2d.setColor(customYellow);
        g2d.drawString(right, (PANEL_WIDTH / 4 * 3) + rightWidth + 32, PANEL_HEIGHT / 3 + 96);
        g2d.drawString(left, (PANEL_WIDTH / 4) - leftWidth - 32, PANEL_HEIGHT / 3 + 96);
        
        // Testo di istruzioni
        g2d.setColor(Color.WHITE);
        String instructions = "ENTER (SELECT) | ESC (BACK)";
        int textWidth = g2d.getFontMetrics().stringWidth(instructions);
        g2d.drawString(instructions, (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT - 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                selectedTheme = (selectedTheme > 0) ? selectedTheme - 1 : themeImages.length - 1;
                repaint();
                break;
            case KeyEvent.VK_RIGHT:
                selectedTheme = (selectedTheme < themeImages.length - 1) ? selectedTheme + 1 : 0;
                repaint();
                break;
            case KeyEvent.VK_ENTER:
                mainClass.setTheme(selectedTheme); // Applica il tema selezionato
                mainClass.showMenu(); // Torna al menu principale
                break;
            case KeyEvent.VK_ESCAPE:
                mainClass.showMenu();
                hidePanel();
                break;
        }
    }

    public void showPanel() {
        active = true;
        requestFocusInWindow(); // Mantiene il focus sulla finestra
        repaint();
    }
    
    public void hidePanel() {
        active = false;
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
