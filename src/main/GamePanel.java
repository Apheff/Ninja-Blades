package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import utils.KeyboardInputs;
import entities.Player;

public class GamePanel extends JPanel {
    
    final private int WIDTH = 600, HEIGHT = 720; // jpanel size = (600 x 720)
    private int playerX, playerY;
    private int playerSize = 64;
    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private int step = 5;
    private Player player; // Store the player object

    public GamePanel() { 
        setPanelSize();
        addKeyListener(keyboardInputs);
        playerX = WIDTH / 2 - playerSize / 2;
        playerY = HEIGHT - 100;

        player = new Player(playerX, playerY, 64, 64); // Use player size as 64x64
        
    }   

    public void setPanelSize(){
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }
    
    // Update game logic
    public void Update() {
        // Check keyboard inputs and update player's position or state
        if (keyboardInputs.isMoveLeft()) {
            player.runLeft(); // Change state to runLeft
            player.changePosition(-step);
        }
        if (keyboardInputs.isMoveRight()) {
            player.runRight(); // Change state to runRight
            player.changePosition(step);
        }
        if (keyboardInputs.isIdle() && player.getState() == 2){
            player.idleRight();
        }
        if (keyboardInputs.isIdle() && player.getState() == -2){
            player.idleLeft();
        }
        if(keyboardInputs.isJump()){
            player.jump();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        player.draw(g);
        Update();
    }

}//end class
