package main;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import utils.SoundManager;

import ui.HUD;
import ui.Wallpapers;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;



public class MainClass extends JPanel implements Runnable{

    private GamePanel gamePanel;
    // private MenuPanel menuPanel = new MenuPanel();
    private SettingsPanel settingsPanel;
    private JLayeredPane layeredPane = new JLayeredPane();
    private Thread gameThread;
    private MenuPanel menuPanel;
    private boolean running = true;
    private TutorialPanel tutorialPanel;

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> new MainClass());
    }


    // Constructor
    public MainClass() {
        setLayout(null); // Assicura che i pannelli vengano posizionati correttamente
    
        // Preload sounds
        SoundManager.preloadSound("hit.wav");
        SoundManager.preloadSound("coin.wav");
        SoundManager.preloadSound("shield.wav");
        SoundManager.preloadSound("magnet1.wav");
        SoundManager.preloadSound("heal.wav");
        SoundManager.preloadSound("pop.wav");
        SoundManager.preloadSound("explosion.wav");

        HUD.loadHUD();
        Wallpapers.LoadWallpapers();

        // Inizializziamo il layeredPane con una dimensione fissa
        layeredPane.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT)); 
    
        // Inizializziamo i pannelli
        gamePanel = new GamePanel(this);
        settingsPanel = new SettingsPanel(this);
        menuPanel = new MenuPanel(this);
        tutorialPanel = new TutorialPanel(this);

        // Aggiungiamo i pannelli al layeredPane
        layeredPane.add(gamePanel, Integer.valueOf(0));
        layeredPane.add(settingsPanel, Integer.valueOf(1));
        layeredPane.add(tutorialPanel, Integer.valueOf(2)); // Strato sopra il gioco
        layeredPane.add(menuPanel, Integer.valueOf(3)); // Menu sopra tutto
        new GameWindow(layeredPane);
        
        addFont(); // Carica il font personalizzato
        showMenu(); // Mostra il menu all'avvio
        startGameLoop();
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Metodo per mostrare le impostazioni
    public void showSettings() {
        settingsPanel.setVisible(true);
        menuPanel.setVisible(false);
        gamePanel.setVisible(false);
        tutorialPanel.setVisible(false);

        settingsPanel.showPanel();
    }
    
    // Metodo per mostrare il menu
    public void showMenu() {
        menuPanel.setVisible(true);
        gamePanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(false);
        
        gamePanel.pauseGame(); // Pausa completa del gioco
        menuPanel.requestFocusInWindow();
        
        updatePanels(); // Forza il rendering corretto
    }
    
    public void startGame() {
        gamePanel.resetGame(); // Resetta il gioco
        gamePanel.setVisible(true);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(false);
    
        gamePanel.resumeGame(); // Riavvia il gioco
        gamePanel.requestFocusInWindow();
    
        updatePanels(); // Forza il rendering corretto
    }

    public void startTutorial() {
        tutorialPanel.setVisible(true);
        menuPanel.setVisible(false);
        gamePanel.setVisible(false);
        tutorialPanel.requestFocusInWindow();
    }

    private void updatePanels() {
        menuPanel.revalidate();
        menuPanel.repaint();
        gamePanel.revalidate();
        gamePanel.repaint();
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }


    @Override
    public void run() {
        while (running) {
            // Settings logic and rendering updates
            layeredPane.repaint();
            try {
                Thread.sleep(16); // Approximately 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void addFont() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // Load the font from the "fonts" directory
            InputStream is = getClass().getResourceAsStream("/font/customFont.ttf");
            if (is == null) {
                System.err.println("Font file not found in the fonts directory.");
                return;
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
