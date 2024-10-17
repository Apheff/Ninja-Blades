package main;

public class MainClass implements Runnable{
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int SET_FPS = 60; // fps
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainClass();
          }
      });
    }

    // constructor
    public MainClass() {
        gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }
    
    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start(); 
    }

    @Override
    public void run(){
        double timePerSecond = 1000000000.0 / SET_FPS;
        long lastTime = System.nanoTime();
        long now = System.nanoTime();
        int frames = 0;
        long lastCheck = System.currentTimeMillis();
        while(true){
            now = System.nanoTime();
            if(now - lastTime >= timePerSecond){
                gamePanel.repaint();
                lastTime = now;
                frames++;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000) {
                System.out.println("FPS:" + frames);
                lastCheck = System.currentTimeMillis();
                frames = 0;
            }
        }
    }
}
