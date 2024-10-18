package main;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import utils.KeyboardInputs;
import entities.Blades;
import entities.Player;

// importing all the constants
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {
    
    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private Player player; // Store the player object
    private boolean startjump;
    private Blades blades;
    private BufferedImage wallpaper;

    public GamePanel() { 
        addKeyListener(keyboardInputs);
        player = new Player();
        blades = new Blades();
        setSize(SCREEN_SIZE);
        // Load sprite sheet
        try {
            wallpaper = ImageIO.read(new File("src/img/wallpaper3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   
    
    public void bladesSpawner(){
        //TO-DO
    }


    // Update game logic
    public void Update() {
        // Check keyboard inputs and update player's position or state

        // this method is just for jumping
        if(keyboardInputs.isSpace() && startjump){
            player.jump();
            startjump = false;
        }
        if (player.canDoubleJump() && player.getState() > 0 || keyboardInputs.isMoveRight() && !player.isOnGround()){
            player.setJumpRightState();
        }
        if (!player.canDoubleJump() && player.getState() > 0 && !player.isOnGround()){
            player.setDoubleJumpRightState();
        }
        if (player.canDoubleJump() && player.getState() < 0 || keyboardInputs.isMoveLeft() && !player.isOnGround()){
            player.setJumpLeftState();
        }
        if (!player.canDoubleJump() && player.getState() < 0 && !player.isOnGround()){
            player.setDoubleJumpLeftState();
        }
        if(!keyboardInputs.isSpace()){
            startjump = true;
        }

        // left and right movment
        if (player.getState() > 0 && player.isOnGround()){
            player.setIdleRightState();
        }
        if (keyboardInputs.isMoveRight()) {
            if(player.isOnGround()){
                player.setRunRightState(); // Change state to runRight
            }
            player.moveX(player.speedX);
        }

        if (player.getState() < 0 && player.isOnGround()){
            player.setIdleLeftState();
        }       
        if (keyboardInputs.isMoveLeft()) {
            if(player.isOnGround()){
                player.setRunLeftState(); // Change state to runLeft
            }
            player.moveX(-player.speedX);
        }
        if(player.y < blades.y && player.x >= blades.x && player.x <= blades.x + blades.width){
            blades.setWhiteState();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        
        // Applica il fattore di scala
        g2d.scale(1/scaleFactor, 1/scaleFactor);
        g2d.drawImage(wallpaper, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, getFocusCycleRootAncestor());
        g2d.fill3DRect(player.x, player.y, player.width, player.height, true);
        player.draw(g2d);
        blades.draw(g2d);
        Update();
    }

}//end class
