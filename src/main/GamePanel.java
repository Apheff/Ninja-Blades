package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import entities.Blades;
import entities.Player;
import ui.HUD;
import ui.Smokes;
import entities.Items;
import ui.Wallpapers;
import ui.PauseMenu; // <-- Importiamo il menu di pausa
import utils.KeyboardInputs;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import ui.GameOverMenu;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.pannelSize;
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {

    // Game inputs
    private KeyboardInputs keyboardInputs = new KeyboardInputs();

    // UI elements
    private Wallpapers wallpapers = new Wallpapers();
    private HUD hud = new HUD();
    private PauseMenu pauseMenu; // <-- Aggiungiamo il menu di pausa
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
            if (!pauseMenu.isPaused()) { // Se il gioco è in pausa, non generiamo lame
                bladesList.add(new Blades());
                timeSpawner = Math.max(500, timeSpawner - 25); // Evita che scenda sotto 500 ms
                bladesSpawner.setDelay(random.nextInt(timeSpawner)); // Aggiorna il delay
            }
        }
    });

    // Game loop usando Swing Timer (aggiorna ogni 16 ms ~ 60 FPS)
    Timer gameLoopTimer = new Timer(16, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOverMenu.isActive() && !pauseMenu.isPaused()) { // <-- Ferma gli update se in pausa
                Update();
                repaint();
            }
        }
    });

    // Game state
    private long lastCollisionTime = 0; // Time of the last collision

    public GamePanel(MainClass mainClass) {
        this.mainClass = mainClass; //  Salviamo il riferimento alla MainClass
        setDoubleBuffered(true);
        setBackground(Color.BLACK);
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(pannelSize));
        setMinimumSize(new Dimension(pannelSize));
    
        // Inizializziamo il menu di pausa
        pauseMenu = new PauseMenu(this, keyboardInputs);
        gameOverMenu = new GameOverMenu(this, keyboardInputs);
    
        // Inizializziamo il game loop
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
                        lastCollisionTime = System.currentTimeMillis() + 2000;
                        if (player.hearts <= 0) {

                            gameOverMenu.show();
                        }
                    }
                } else {
                    smokes.setSmoke(13, player.x, player.y);
                    player.isInvincible = false;
                    lastCollisionTime = System.currentTimeMillis() + 2000;
                }
            }
        }

        for (Blades blade : bladesList) {
            if ((player.y + player.height < blade.y && player.x + player.width / 2 > blade.x && player.x + player.width / 2 < blade.x + blade.width)) {
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
                        player.score += 1;
                        break;
                    case 1:
                        player.setInvincible(5000);
                        break;
                    case 2:
                        player.setMagnetize(7000);
                        break;
                    case 3:
                        if (player.hearts < 3) {
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
        wallpapers.draw(g2d, 1);
        hud.draw(g2d, player);

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
