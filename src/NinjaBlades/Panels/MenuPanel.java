package NinjaBlades.Panels;
import javax.swing.*;

import NinjaBlades.MainClass;
import NinjaBlades.utils.ImageLoader;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_SIZE;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.customFont;
import static NinjaBlades.utils.Constants.GamePanel.customYellow;
import static NinjaBlades.utils.Constants.GamePanel.scaleFactor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;


public class MenuPanel extends JPanel implements KeyListener {

    private MainClass mainClass;
    private String[] options = {"PLAY", "TUTORIAL", "SETTINGS", "THEMES", "EXIT"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private BufferedImage logoImage; // Per caricare l'immagine del logo
    private BufferedImage menuWallpaper;
    private BufferedImage[] wallpaperFrames;

    public MenuPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setMaximumSize(PANEL_SIZE);

        // Carichiamo il logo usando la funzione esistente
        logoImage = ImageLoader.loadImage("logo.png"); // Assicurati che il file sia in img/
        menuWallpaper = ImageLoader.loadImage("menuWallpaper.png");
        wallpaperFrames = ImageLoader.loadFrames(menuWallpaper, 0, 0, 8, 920, 1080);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.scale(scaleFactor, scaleFactor);

        drawWallpaper(g2d);
        // Overlay semi-trasparente
        g2d.setColor(new Color(0, 0, 0, 80));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        g2d.setColor(Color.BLACK);

        // Disegniamo il logo al centro in alto
        if (logoImage != null) {
            int logoX = (PANEL_WIDTH - 460) / 2;
            int logoY = PANEL_HEIGHT / 6; // Un po' sopra il centro
            g2d.drawImage(logoImage, logoX, logoY, 460, 230, null);
        }

        // Disegniamo le opzioni del menu
        g2d.setFont(customFont);
        int startY = PANEL_HEIGHT / 2; // Punto di partenza verticale per i pulsanti
        int spacing = 60; // Distanza tra i pulsanti

        for (int i = 0; i < options.length; i++) {

            // color and size set
            String text = options[i];
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textX = (PANEL_WIDTH - textWidth) / 2;
            int textY = startY + (i * spacing);
            g2d.setColor(Color.BLACK);
            g2d.setFont(customFont);
            g2d.drawString(text, textX + 2, textY + 2);
            
            if (i == selectedOption) {
                g2d.setColor(customYellow); // Evidenziazione per l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            
            g2d.drawString(text, textX, textY);
        }
    }

    private void drawWallpaper(Graphics2D g2d) {
        int frameIndex = (int) (System.currentTimeMillis() / 120) % wallpaperFrames.length;
        g2d.drawImage(wallpaperFrames[frameIndex], 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE){
            selectOption();
        }
        repaint(); // Aggiorna la grafica
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0:
                mainClass.startGame();
                break;
            case 1:
                mainClass.startTutorial();
                break;
            case 2:
                mainClass.showSettings();
                break;
            case 3:
                mainClass.showLocker();
                break;
            case 4:
                System.exit(0);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
