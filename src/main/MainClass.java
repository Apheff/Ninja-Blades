package main;


import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class MainClass extends JPanel implements Runnable{

    private GamePanel gamePanel = new GamePanel();
    // private MenuPanel menuPanel = new MenuPanel();
    private SettingsPanel settingsPanel = new SettingsPanel();
    private JLayeredPane layeredPane = new JLayeredPane();
    private Thread gameThread;
    private boolean running = true;

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> new MainClass());
    }


    // Constructor
    public MainClass() {

        layeredPane.add(gamePanel, Integer.valueOf(0)); // Ensure correct layering
        layeredPane.add(settingsPanel, Integer.valueOf(1)); 
    
        new GameWindow(layeredPane);
        
        // Request focus for gamePanel after adding it to the window
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
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
