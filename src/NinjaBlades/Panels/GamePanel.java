package NinjaBlades.Panels;

// import libraries
import javax.swing.Timer;

import NinjaBlades.MainClass;
import NinjaBlades.entities.Blades;
import NinjaBlades.entities.Items;
import NinjaBlades.entities.Player;
import NinjaBlades.ui.GameOverMenu;
import NinjaBlades.ui.HUD;
import NinjaBlades.ui.PauseMenu;
import NinjaBlades.ui.Smokes;
import NinjaBlades.ui.Wallpapers;
import NinjaBlades.utils.KeyboardInputs;
import NinjaBlades.utils.SoundManager;
import NinjaBlades.utils.ConfigManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.scaleFactor;
import static NinjaBlades.utils.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    // Game inputs
    private KeyboardInputs keyboardInputs = new KeyboardInputs();

    private PauseMenu pauseMenu;
    private MainClass mainClass;
    private GameOverMenu gameOverMenu;

    // Game Effects
    private Smokes smokes = new Smokes();

    // Game variables
    private int highScore = ConfigManager.getHighScore();
    public int score = 0;

    // Game entities
    private Player player = new Player(keyboardInputs);
    private List<Blades> bladesList = new ArrayList<>();
    private List<Items> itemList = new ArrayList<>();
    private Random random = new Random();
    private int timeSpawner = 3000;
    private Timer bladesSpawner = new Timer(0, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!pauseMenu.isPaused()) { // if the game is paused stop the spawner
                if(bladesList.size() <= 10){
                    bladesList.add(new Blades());
                }
                timeSpawner = Math.max(300, timeSpawner - 25); // decrease the delay over time (minimum 500ms)
                bladesSpawner.setDelay(random.nextInt(timeSpawner) + 200); // randomize the delay
            }
        }
    });

    // Game loop (refreshes each 16 ms ~ 60 FPS)
    Timer gameLoopTimer = new Timer(16, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOverMenu.isActive() && !pauseMenu.isPaused()) { // if the game is not over and not paused
                repaint();
                Update();
            }
        }
    });

    // Game state
    private long lastCollisionTime = 0; // Time of the last collision

    public GamePanel(MainClass mainClass) {

        // Set the panel properties
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);

        this.mainClass = mainClass;
    
        // load pause and game over menu
        pauseMenu = new PauseMenu(this, keyboardInputs);
        gameOverMenu = new GameOverMenu(this, keyboardInputs);
    
        // starts the game loop
        gameLoopTimer.start();
    }

    // Update game logic
    public void Update() {
        if (pauseMenu.isPaused()) return;

        // Update player and set the state
        player.update();
        setPlayerState();

        for (Blades blade : bladesList) {
            if (player.collisionCheck(blade) && System.currentTimeMillis() > lastCollisionTime) {
                if (!player.isInvincible) {
                    if (!player.damaged) {
                        player.hearts--;
                        player.setDamage(2000);
                        SoundManager.playSound("hit.wav"); // 🔊 hit sound
                        lastCollisionTime = System.currentTimeMillis() + 2000;
                        if (player.hearts <= 0) {
                            gameOverMenu.show();
                            highScore = Math.max(highScore, score);
                            ConfigManager.setHighScore(highScore);
                        }
                    }
                } else {
                    SoundManager.playSound("pop.wav"); // 🔊 pop sound
                    smokes.setSmoke(3, player.x, player.y);
                    player.isInvincible = false;
                    lastCollisionTime = System.currentTimeMillis() + 2000;
                    player.knockback(15);
                }
            }
        }

        for (Blades blade : bladesList) {
            if (blade.checkBladeDestroy(player)) {
                smokes.setSmoke(0, blade.x, blade.y);
                itemList.add(new Items(blade.x, blade.y));
                blade.destroyBlade();
                SoundManager.playSound("explosion.wav"); // 🔊 explosion sound
            }
            blade.update();
        }

        for (Items item : itemList) {
            if (player.collisionCheck(item)) {
                switch (item.type) {
                    case 0:
                        SoundManager.playSound("coin.wav"); // 🔊 coin grabbed sound
                        score += 1;
                        break;
                    case 1:
                        SoundManager.playSound("shield.wav"); // 🔊 shield sound
                        player.setInvincible(5000);
                        break;
                    case 2:
                        SoundManager.playSound("magnet.wav"); // 🔊 magnet sound
                        player.setMagnetize(7000);
                        break;
                    case 3:
                        if (player.hearts < 3) {
                            SoundManager.playSound("heal.wav"); // 🔊 heal sound
                            player.hearts++;
                        }
                        break;
                }
                item.grabItem();
            }
            item.update(player);
        }

        itemList.removeIf(Items::isGrabed);
        bladesList.removeIf(blade -> blade.y < -blade.height || blade.destroyed);

        bladesSpawner.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // black background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        Graphics2D g2d = (Graphics2D) g;

        // apply scaling
        g2d.scale(scaleFactor, scaleFactor);



        Wallpapers.draw(g2d, mainClass.getTheme());
        
        if (gameOverMenu.isActive()) {
            gameOverMenu.show();
            gameOverMenu.draw(g2d);
        } else {
            player.draw(g2d);
            for (Blades blade : bladesList) {
                blade.draw(g2d);
            }
            smokes.draw(g2d);
            for (Items item : itemList) {
                item.draw(g2d);
            }
        }
        HUD.draw(g2d, player, score);
        pauseMenu.draw(g2d); // Disegna il menu di pausa
    }

    public int getHighScore() {
        return highScore;
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }
    // Metodo per fermare il timer quando il gioco è in pausa
    public void pauseGame() {
        gameLoopTimer.stop(); // Ferma il loop del gioco
    }
    
    public void resumeGame() {
        gameLoopTimer.start(); // Riavvia il loop del gioco
    }
    public void returnToMenu() {
        pauseGame(); // Ferma il gioco
        mainClass.showMenu(); // Torna al menu principale
    }

    public void resetPlayer(){
        player.hearts = 3;
        player.resetPosition(); // resets the player position
        player.isInvincible = false; // resets the invincibility
        player.isMagnetized = false; // resets the magnetism
        player.damaged = false; // resets the damage
    }

    public void resetGame() {
        resetPlayer();
        pauseMenu.setPaused(false); // Resetta il menu di pausa
        bladesList.clear(); // Rimuove tutte le lame
        itemList.clear();   // Rimuove tutti gli oggetti
        timeSpawner = 3000; // Reimposta il tempo di spawn iniziale delle lame
        gameOverMenu.hide(); // Nasconde il menu di fine partita
        score = 0; // Resetta il punteggio
        // Riavvia il gioco
        resumeGame();
    }

    // sets the state for every players movement
    public void setPlayerState(){
        if(!player.onGround){
            if(player.doubleJump){
                // jump frames
                if(player.state < 4 || keyboardInputs.right){
                    player.state = JUMP_RIGHT;
                }
                if(player.state >= 4 || keyboardInputs.left){
                    player.state = JUMP_LEFT;
                }
            }else{
                // double jump frames
                if(player.state < 4 || keyboardInputs.right){
                    player.state = DOUBLE_JUMP_RIGHT;
                }
                if(player.state >= 4 || keyboardInputs.left){
                    player.state = DOUBLE_JUMP_LEFT;
                }               
            }
        }else{
            // on ground right frames (idle and run) 
            if(keyboardInputs.right){
                player.state = RUN_RIGHT;
            }else if (player.state < 4){
                player.state = IDLE_RIGHT;
            }
            // on ground left frames (idle and run)
            if(keyboardInputs.left){
                player.state = RUN_LEFT;
            }else if (player.state >= 4){
                player.state = IDLE_LEFT;
            }
            if (keyboardInputs.left && keyboardInputs.right) {
                player.state = IDLE_RIGHT;
            }
        }
    }
}
