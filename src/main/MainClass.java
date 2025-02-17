package main;


import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import java.awt.Dimension;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MainClass extends JPanel implements Runnable{

    private GamePanel gamePanel = new GamePanel(this);
    // private MenuPanel menuPanel = new MenuPanel();
    private SettingsPanel settingsPanel = new SettingsPanel();
    private JLayeredPane layeredPane = new JLayeredPane();
    private Thread gameThread;
    private MenuPanel menuPanel;
    private boolean running = true;

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> new MainClass());
    }


    // Constructor
    public MainClass() {
        setLayout(null); // Assicura che i pannelli vengano posizionati correttamente
    
        // Inizializziamo il layeredPane con una dimensione fissa
        layeredPane.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT)); 
    
        // Inizializziamo i pannelli
        gamePanel = new GamePanel(this);
        settingsPanel = new SettingsPanel();
        menuPanel = new MenuPanel(this);
    
        // Aggiungiamo i pannelli al layeredPane
        layeredPane.add(gamePanel, Integer.valueOf(0));
        layeredPane.add(settingsPanel, Integer.valueOf(1));
        layeredPane.add(menuPanel, Integer.valueOf(2)); // Menu sopra tutto
    
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
    }
    
    // Metodo per mostrare il menu
    public void showMenu() {
        menuPanel.setVisible(true);
        gamePanel.setVisible(false);
        settingsPanel.setVisible(false);
        
        gamePanel.pauseGame(); // Pausa completa del gioco
        menuPanel.requestFocusInWindow();
        
        updatePanels(); // Forza il rendering corretto
    }
    
    public void startGame() {
        gamePanel.resetGame(); // Resetta il gioco
        gamePanel.setVisible(true);
        menuPanel.setVisible(false);
        settingsPanel.setVisible(false);
    
        gamePanel.resumeGame(); // Riavvia il gioco
        gamePanel.requestFocusInWindow();
    
        updatePanels(); // Forza il rendering corretto
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

}
