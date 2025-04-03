package ninjablades;

import static ninjablades.utils.Constants.GamePanel.BORDER_WIDTH;
import static ninjablades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static ninjablades.utils.Constants.GamePanel.PANEL_SIZE;
import static ninjablades.utils.Constants.GamePanel.PANEL_WIDTH;
import static ninjablades.utils.Constants.GamePanel.SCREEN_SIZE;
import static ninjablades.utils.Constants.GamePanel.scaleFactor;

import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import ninjablades.Panels.GamePanel;
import ninjablades.Panels.GameWindow;
import ninjablades.Panels.MenuPanel;
import ninjablades.Panels.SettingsPanel;
import ninjablades.Panels.ThemePanel;
import ninjablades.Panels.TutorialPanel;
import ninjablades.ui.HUD;
import ninjablades.ui.Wallpapers;
import ninjablades.utils.ConfigManager;
import ninjablades.utils.SoundManager;


public class NinjaMain extends JPanel implements Runnable{

    private GamePanel gamePanel;
    // private MenuPanel menuPanel = new MenuPanel();
    private SettingsPanel settingsPanel;
    private JLayeredPane layeredPane = new JLayeredPane();
    private Thread gameThread;
    private ThemePanel lockerPanel;
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

        ConfigManager.loadConfig(); // Carica la configurazione del gioco

        // preloads all the sounds
        SoundManager.preloadAllSounds();
        SoundManager.setVolume(0); // remove lag on first sound play
        SoundManager.playSound("coin.wav");

        System.out.println("scale Factor : " + scaleFactor);
        System.out.println("Screen size: " + SCREEN_SIZE);
        System.out.println("Panel size: " + PANEL_SIZE);
        System.out.println("border width: " + BORDER_WIDTH);

        HUD.loadHUD();
        Wallpapers.LoadWallpapers();


        // Inizializziamo il layeredPane con una dimensione fissa
        layeredPane.setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        layeredPane.setMaximumSize(PANEL_SIZE);
    
        // Inizializziamo i pannelli
        lockerPanel = new ThemePanel(this);
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

        // Imposta il volume in base alla configurazione
        SoundManager.setVolume(ConfigManager.getVolume());
        SoundManager.loopSound("background.wav");
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
        menuPanel.stopBlades();
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
        menuPanel.startBlades(); // Avvia il movimento delle lame
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
        menuPanel.stopBlades();
        updatePanels(); // Forza il rendering corretto
    }

    public void startTutorial() {
        gamePanel.setVisible(false);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(true);
        lockerPanel.setVisible(false);
        tutorialPanel.requestFocusInWindow();
        menuPanel.stopBlades();
    }

    private void updatePanels() {
        menuPanel.revalidate();
        menuPanel.repaint();
        gamePanel.revalidate();
        gamePanel.repaint();
        settingsPanel.revalidate();
        settingsPanel.repaint();
    }

    public void showLocker() {
        gamePanel.setVisible(false);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
        tutorialPanel.setVisible(false);
        lockerPanel.setVisible(true);
        lockerPanel.showPanel();
        menuPanel.stopBlades();
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
