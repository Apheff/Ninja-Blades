package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import static utils.Constants.PlayerConstants.PLAYER_SIZE;

public class GamePanel extends JPanel {
    
    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private Player player; // Store the player object
    private boolean startjump;
    private Blades blades;
    private BufferedImage wallpaper;

    public GamePanel() { 
        setPanelSize();
        addKeyListener(keyboardInputs);
        player = new Player(); // Use player size as 64x64
        blades = new Blades();
        try {
            wallpaper = ImageIO.read(new File("src/img/wallpaper3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public void setPanelSize(){
        Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

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
            player.changePositionRight();
        }

        if (player.getState() < 0 && player.isOnGround()){
            player.idleLeftState();
        }       
        if (keyboardInputs.isMoveLeft()) {
            if(player.isOnGround()){
                player.runLeftState(); // Change state to runLeft
            }
            player.changePositionLeft();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(wallpaper, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        if(player.getBounds().intersects(blades.getBounds())){
            g.setColor(new Color(255,0,0));
            g.fillRect(player.getX(), player.getY(), PLAYER_SIZE, PLAYER_SIZE);
            
        }
        //g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        // entities draw        
        player.draw(g);
        blades.draw(g);
        Update();
    }

}//end class
