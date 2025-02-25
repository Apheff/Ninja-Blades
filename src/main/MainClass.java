package main;


import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import utils.SoundManager;

import ui.HUD;
import ui.Wallpapers;

import static utils.Constants.GamePanel.BORDER_WIDTH;
import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_SIZE;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.scaleFactor;
import static utils.Constants.GamePanel.SCREEN_SIZE;
import static utils.Constants.GamePanel.dpi;



public class MainClass extends JPanel implements Runnable{

    private GamePanel gamePanel;
    // private MenuPanel menuPanel = new MenuPanel();
    private SettingsPanel settingsPanel;
    private JLayeredPane layeredPane = new JLayeredPane();
    private Thread gameThread;
    private LockerPanel lockerPanel;
    private MenuPanel menuPanel;
    private boolean running = true;
    private TutorialPanel tutorialPanel;
    private int currentTheme = 0;

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
        SoundManager.preloadSound("magnet.wav");
        SoundManager.preloadSound("heal.wav");
        SoundManager.preloadSound("pop.wav");
        SoundManager.preloadSound("explosion.wav");


        System.out.println("scale Factor : " + scaleFactor);
        System.out.println("Screen size: " + SCREEN_SIZE);
        System.out.println("Panel size: " + PANEL_SIZE);
        System.out.println("dpi: " + dpi);
        System.out.println("border width: " + BORDER_WIDTH);

        HUD.loadHUD();
        Wallpapers.LoadWallpapers();

        // Inizializziamo il layeredPane con una dimensione fissa
        layeredPane.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        layeredPane.setMaximumSize(PANEL_SIZE);
    
        // Inizializziamo i pannelli
        lockerPanel = new LockerPanel(this);
        gamePanel = new GamePanel(this);
        settingsPanel = new SettingsPanel(this);
        menuPanel = new MenuPanel(this);
        tutorialPanel = new TutorialPanel(this);

        // Aggiungiamo i pannelli al layeredPane
        layeredPane.add(gamePanel, Integer.valueOf(0));
        layeredPane.add(settingsPanel, Integer.valueOf(1));
        layeredPane.add(tutorialPanel, Integer.valueOf(2)); // Strato sopra il gioco
        layeredPane.add(menuPanel, Integer.valueOf(3)); // Menu sopra tutto
        layeredPane.add(lockerPanel, Integer.valueOf(4)); // Locker sopra tutto
        new GameWindow(layeredPane);
        
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
        lockerPanel.setVisible(false);
        settingsPanel.showPanel();
    }
    
    // Metodo per mostrare il menu
    public void showMenu() {
        menuPanel.setVisible(true);
        gamePanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(false);
        lockerPanel.setVisible(false);
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
        lockerPanel.setVisible(false);
        gamePanel.resumeGame(); // Riavvia il gioco
        gamePanel.requestFocusInWindow();
    
        updatePanels(); // Forza il rendering corretto
    }

    public void startTutorial() {
        gamePanel.setVisible(false);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(true);
        lockerPanel.setVisible(false);
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

    public int getTheme() {
        return currentTheme;
    }

    public void setTheme(int themeIndex) {
        currentTheme = themeIndex;
        System.out.println("Tema selezionato: " + themeIndex);
        // Qui puoi cambiare lo sfondo del gioco o le texture
    }

    public void showLocker() {
        gamePanel.setVisible(false);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(false);
        lockerPanel.setVisible(true);
        lockerPanel.showPanel();
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
}
