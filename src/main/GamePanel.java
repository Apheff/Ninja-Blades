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
import ui.Hearts;
import ui.Smokes;
import entities.Items;
import ui.Wallpapers;
import utils.KeyboardInputs;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.pannelSize;
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {


    // UI elements
    private Wallpapers wallpapers = new Wallpapers();
    private Hearts hearts = new Hearts();
    private Smokes smokes = new Smokes();


    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private Player player = new Player(keyboardInputs);
    private List<Blades> bladesList = new ArrayList<>();
    private List<Items> itemList = new ArrayList<>();
    private Timer bladesSpawner = new Timer(1000, e -> {bladesList.add(new Blades());});
    private long lastCollisionTime = 0; 

    private boolean gameOver = false;
    private boolean running = true;

    public GamePanel() {
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(pannelSize));
        setMinimumSize(new Dimension(pannelSize));

        // Game loop using Swing Timer (updates every 16 ms ~ 60 FPS)
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver && running) {
                    Update();
                    repaint();
                }
            }
        });
        timer.start();
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
                }
            }
        }

        for (Blades blade : bladesList) {
            if ((player.y + player.height < blade.y && player.x + player.width / 2 > blade.x && player.x + player.width / 2 < blade.x + blade.width)) {
                blade.destroyBlade();
                smokes.setSmokePosition(blade.x, blade.y);
                itemList.add(new Items(blade.x, blade.y));
            }
            blade.update();
        }
        for(Items item : itemList){
            if(player.collisionCheck(item)){
                switch (item.type) {
                    case 0:
                        player.score += 1;
                        break;
                    case 1:
                        player.isInvincible = true;
                        new Timer(5000, e -> player.isInvincible = false).start();
                        break;
                    case 2:
                        player.isMagnetized = true;
                        new Timer(5000, e -> player.isMagnetized = false).start();
                        break;
                    case 4:
                        if(player.hearts < 3){
                            player.hearts++;
                        }
                        break;
                }
                item.destroyItem();
            }
            item.update(player);
        }

        itemList.removeIf(item -> item.isDestroyed());
        bladesList.removeIf(blade -> blade.y < -blade.height || blade.destroyed);

        bladesSpawner.start(); // Avvia il timer (si ripete ogni 1 secondo)
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1 / scaleFactor, 1 / scaleFactor);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        wallpapers.draw(g2d, 1);
        hearts.draw(g2d, player.hearts);
        if (gameOver) {
            drawGameOver(g2d);
        }else {
            player.draw(g2d);
            for (Blades blade : bladesList) {
                blade.draw(g2d);
            }
            smokes.draw(g2d);
            for (Items item : itemList) {
                item.draw(g2d);
            }
        }
        // pause overlay
        // if (!running) {
        //     g2d.setColor(new Color(0, 0, 0, 150)); // overlay semi-transperent
        //     g2d.fillRect(0, 0, getWidth(), getHeight());
        //     g2d.setColor(Color.WHITE);
        //     g2d.drawString("Game Paused", getWidth() / 2 - 30, getHeight() / 2);
        //     g2d.drawString("Press 'P' to resume", getWidth() / 2 - 50, getHeight() / 2 + 20);
        // }
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
