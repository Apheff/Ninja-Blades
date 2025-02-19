package main;

// import libraries
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

// import entities
import entities.Blades;
import entities.Items;
import entities.Player;

// import ui elements
import ui.HUD;
import ui.Smokes;
import ui.PauseMenu;
import ui.Wallpapers;
import ui.GameOverMenu;

// import utilites
import utils.KeyboardInputs;
import utils.SoundManager;

// import constants
import static utils.Constants.GamePanel.*;
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {

    // Game inputs
    private KeyboardInputs keyboardInputs = new KeyboardInputs();

    private PauseMenu pauseMenu;
    private MainClass mainClass;
    private GameOverMenu gameOverMenu;

    // Game Effects
    private Smokes smokes = new Smokes();

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
                bladesList.add(new Blades());
                timeSpawner = Math.max(500, timeSpawner - 25); // decrease the delay over time (minimum 500ms)
                bladesSpawner.setDelay(random.nextInt(timeSpawner)); // randomize the delay
            }
        }
    });

    // Game loop (refreshes each 16 ms ~ 60 FPS)
    Timer gameLoopTimer = new Timer(16, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOverMenu.isActive() && !pauseMenu.isPaused()) { // if the game is not over and not paused
                Update();
                repaint();
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
        setPreferredSize(new Dimension(panelSize));
        setMinimumSize(new Dimension(panelSize));
        setMaximumSize(new Dimension(panelSize));

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

        player.update();

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
                        }
                    }
                } else {
                    SoundManager.playSound("pop.wav"); // 🔊 pop sound
                    smokes.setSmoke(13, player.x, player.y);
                    player.isInvincible = false;
                    lastCollisionTime = System.currentTimeMillis() + 2000;
                }
            }
        }

        for (Blades blade : bladesList) {
            if (player.checkBladeDestroy(blade)) {
                SoundManager.playSound("explosion.wav"); // 🔊 explosion sound
                blade.destroyBlade();
                smokes.setSmoke(0, blade.x, blade.y);
                itemList.add(new Items(blade.x, blade.y));
            }
            blade.update();
        }

        for (Items item : itemList) {
            if (player.collisionCheck(item)) {
                switch (item.type) {
                    case 0:
                        SoundManager.playSound("coin.wav"); // 🔊 coin grabbed sound
                        player.score += 1;
                        break;
                    case 1:
                        SoundManager.playSound("shield.wav"); // 🔊 shield sound
                        player.setInvincible(5000);
                        break;
                    case 2:
                        SoundManager.playSound("magnet1.wav"); // 🔊 magnet sound
                        player.setMagnetize(7000);
                        break;
                    case 3:
                        if (player.hearts < 3) {
                            SoundManager.playSound("heal.wav"); // 🔊 heal sound
                            player.hearts++;
                        }
                        break;
                }
                item.destroyItem();
            }
            item.update(player);
        }
        

        itemList.removeIf(Items::isDestroyed);
        bladesList.removeIf(blade -> blade.y < -blade.height || blade.destroyed);

        bladesSpawner.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1 / scaleFactor, 1 / scaleFactor);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        Wallpapers.draw(g2d, 1);
        HUD.draw(g2d, player);

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
        pauseMenu.draw(g2d); // Disegna il menu di pausa
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

    public void resetGame() {
        player.resetPlayer(); // Resetta il giocatore
        pauseMenu.setPaused(false); // Resetta il menu di pausa
        bladesList.clear(); // Rimuove tutte le lame
        itemList.clear();   // Rimuove tutti gli oggetti
        timeSpawner = 3000; // Reimposta il tempo di spawn iniziale delle lame
        gameOverMenu.hide(); // Nasconde il menu di fine partita
        // Riavvia il gioco
        resumeGame();
    }
}
