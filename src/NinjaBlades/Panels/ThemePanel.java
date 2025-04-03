package ninjablades.panels;

import javax.swing.*;

import ninjalades.MainClass;
import ninjalades.utils.ConfigManager;
import ninjalades.utils.ImageLoader;

import static ninjalades.utils.Constants.GamePanel.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class ThemePanel extends JPanel implements KeyListener {

    private MainClass mainClass;
    private int selectedTheme = ConfigManager.getTheme(); // default: 0
    private BufferedImage[] themeImages;
    private String[] themeNames = {"Classic Theme", "Rick and Morty Theme"};
    private boolean active = false;

    public ThemePanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setMaximumSize(PANEL_SIZE);

        themeImages = new BufferedImage[2];
        themeImages[0] = ImageLoader.loadImage("wallpaper0.jpg");
        themeImages[1] = ImageLoader.loadImage("wallpaper1.jpg");
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
            g2d.setColor(i == selectedTheme ? customYellow : Color.WHITE);
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

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
            selectedTheme = (selectedTheme > 0) ? selectedTheme - 1 : themeImages.length - 1;
            repaint();
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
            selectedTheme = (selectedTheme < themeImages.length - 1) ? selectedTheme + 1 : 0;
            repaint();
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE){
            ConfigManager.setTheme(selectedTheme); // Salva il tema selezionato
            mainClass.showMenu(); // Torna al menu principale
        } else if (keyCode == KeyEvent.VK_ESCAPE){
            mainClass.showMenu();
            hidePanel();
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
