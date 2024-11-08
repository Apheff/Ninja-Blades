package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainClass implements Runnable, KeyListener {
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int SET_FPS = 60; // frames per second
    private boolean isPaused = false; // Flag per la pausa

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainClass());
    }

    // Constructor
    public MainClass() {
        gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        gamePanel.requestFocus();
        gamePanel.addKeyListener(this); // Aggiungi il KeyListener per rilevare i tasti
        startGameLoop();
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / SET_FPS; // in nanoseconds
        long lastTime = System.nanoTime();
        int frames = 0;
        long lastCheck = System.currentTimeMillis();

        while (!gamePanel.isGameOver()) {
            long now = System.nanoTime();
            if (!isPaused && now - lastTime >= timePerFrame) {
                lastTime = now;
                gamePanel.repaint(); // Ridisegna solo se il gioco non è in pausa
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                System.out.println("FPS: " + frames);
                lastCheck = System.currentTimeMillis();
                frames = 0;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {//aggiungilo ALLE UTILS
        if (e.getKeyCode() == KeyEvent.VK_P) { // Tasto P per mettere in pausa o riprendere
            gamePanel.setPaused();
            isPaused = !isPaused;
            System.out.println(isPaused ? "Game Paused" : "Game Resumed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}


}
