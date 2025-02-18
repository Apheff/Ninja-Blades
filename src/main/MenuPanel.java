package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import utils.ImageLoader;
import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.panelSize;
import static utils.Constants.GameWindow.scaleFactor;

public class MenuPanel extends JPanel implements KeyListener {

    private MainClass mainClass;
    private String[] options = {"Play", "Tutorial", "Options", "Locker", "Exit"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private BufferedImage logoImage; // Per caricare l'immagine del logo
    private BufferedImage menuWallpaper;
    private BufferedImage[] wallpaperFrames;
    private Font customFont;

    public MenuPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        addKeyListener(this);
        setBackground(new Color(215, 215 , 215));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(panelSize));
        setMinimumSize(new Dimension(panelSize));

        // Carichiamo il logo usando la funzione esistente
        logoImage = ImageLoader.loadImage("logo.png"); // Assicurati che il file sia in img/
        menuWallpaper = ImageLoader.loadImage("menuWallpaper.png");
        wallpaperFrames = ImageLoader.loadFrames(menuWallpaper, 0, 0, 8, 920, 1080);

        // Load the custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/customFont.ttf")).deriveFont(36f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 36); // Fallback font
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1 / scaleFactor, 1 / scaleFactor);

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
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW); // Evidenziazione per l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }

            String text = options[i];
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textX = (PANEL_WIDTH - textWidth) / 2;
            int textY = startY + (i * spacing);
            g2d.drawString(text, textX, textY);
        }
    }

    private void drawWallpaper(Graphics2D g2d) {
        int frameIndex = (int) (System.currentTimeMillis() / 200) % wallpaperFrames.length;
        g2d.drawImage(wallpaperFrames[frameIndex], 0, 0, null);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_UP:
                selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
                break;
            case KeyEvent.VK_DOWN:
                selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
                break;
            case KeyEvent.VK_ENTER:
                selectOption();
                break;
        }
        repaint(); // Aggiorna la grafica
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0:
                mainClass.startGame();
                break;
            case 1:
                System.out.println("Tutorial selezionato (da implementare)");
                break;
            case 2:
                mainClass.showSettings();
                break;
            case 3:
                System.out.println("Armadietto selezionato (da implementare)");
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
