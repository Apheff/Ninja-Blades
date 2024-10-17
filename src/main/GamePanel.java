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

// importing all the GameWindoo constants
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
        System.out.println(scaleFactor);
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
            player.jumpRightState();
        }
        if (!player.canDoubleJump() && player.getState() > 0 && !player.isOnGround()){
            player.doubleJumpRightState();
        }
        if (player.canDoubleJump() && player.getState() < 0 || keyboardInputs.isMoveLeft() && !player.isOnGround()){
            player.jumpLeftState();
        }
        if (!player.canDoubleJump() && player.getState() < 0 && !player.isOnGround()){
            player.doubleJumpLeftState();
        }
        if(!keyboardInputs.isSpace()){
            startjump = true;
        }

        // left and right movment
        if (player.getState() > 0 && player.isOnGround()){
            player.idleRightState();
        }
        if (keyboardInputs.isMoveRight()) {
            if(player.isOnGround()){
                player.runRightState(); // Change state to runRight
            }
            player.moveX(player.speedX);
        }

        if (player.getState() < 0 && player.isOnGround()){
            player.idleLeftState();
        }       
        if (keyboardInputs.isMoveLeft()) {
            if(player.isOnGround()){
                player.runLeftState(); // Change state to runLeft
            }
            player.moveX(-player.speedX);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        
        // Applica il fattore di scala
        g2d.scale(1/scaleFactor, 1/scaleFactor);
        g2d.drawImage(wallpaper, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, getFocusCycleRootAncestor());
        player.draw(g2d);
                
        Update();
    }

}//end class
