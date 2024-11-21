package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import entities.Blades;
import entities.Player;
import utils.KeyboardInputs;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.pannelSize;
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {

    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private Player player = new Player(keyboardInputs);
    private List<Blades> bladesList = new ArrayList<>();
    private long timeToSpawn = System.currentTimeMillis();
    private long timeLimit = 1000; // ms
    private long lastCollisionTime = 0; 

    private boolean gameOver = false;
    private boolean isPaused = false;

    public GamePanel() {
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(pannelSize));
        setMinimumSize(new Dimension(pannelSize));
    }

    public void setPaused() {
        this.isPaused = !isPaused;
        repaint();
    }

    // Update game logic
    public void Update() {

        player.update();

        for (Blades blade : bladesList) {
            if (player.collisionCheck(blade)) {
                if (System.currentTimeMillis() - lastCollisionTime > 2000) {
                    player.hearts--;
                    System.err.println("Player hit! Remaining hearts: " + player.hearts);
                    lastCollisionTime = System.currentTimeMillis();
                    if (player.hearts <= 0) {
                        gameOver = true;
                        
                    }
                    break;
                }
            }
        }

        for (Blades blade : bladesList) {
            if ((player.y + player.height < blade.y && player.x + player.width / 2 > blade.x && player.x + player.width / 2 < blade.x + blade.width) || blade.y < -blade.height) {
                blade.destroyed = true;
            }
            blade.update();
        }
        
        bladesList.removeIf(blade -> blade.isDestroyed());

        if (System.currentTimeMillis() - timeToSpawn > timeLimit) {
            bladesList.add(new Blades());
            timeToSpawn = System.currentTimeMillis();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1 / scaleFactor, 1 / scaleFactor);
        
        if (gameOver) {
            drawGameOver(g2d);
        }
        
        if (!gameOver) {
            Update();
    
            g2d.fill(player.hitbox);
            player.draw(g2d);
            for (Blades blade : bladesList) {
                blade.draw(g2d);
            }
    
            drawHearts(g2d);
        }
        // pause overlay
        if (isPaused) {
            g2d.setColor(new Color(0, 0, 0, 150)); // overlay semi-transperent
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.WHITE);
            g2d.drawString("Game Paused", getWidth() / 2 - 30, getHeight() / 2);
            g2d.drawString("Press 'P' to resume", getWidth() / 2 - 50, getHeight() / 2 + 20);
        }
    }

    private void drawHearts(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        int heartSize = 20;
        int padding = 5;
        for (int i = 0; i < player.hearts; i++) {
            int x = padding + (i * (heartSize + padding));
            int y = padding;
            g2d.fillOval(x, y, heartSize, heartSize); 
        }
    }

    private void drawGameOver(Graphics2D g2d) {
        String gameOverText = "GAME OVER";
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        int textWidth = g2d.getFontMetrics().stringWidth(gameOverText);
        int textHeight = g2d.getFontMetrics().getHeight();
        int x = (PANEL_WIDTH - textWidth) / 2;
        int y = (PANEL_HEIGHT - textHeight) / 2;
        g2d.drawString(gameOverText, x, y);
    }

    public boolean isGameOver(){
        return gameOver;
    }
    
}
