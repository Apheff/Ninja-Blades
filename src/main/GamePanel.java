package main;


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import entities.Blades;
import entities.Player;
import utils.KeyboardInputs;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.pannelSize;
// importing all the constants
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel{

    private Player player = new Player(); // Store the player object
    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private boolean spaceReleased = true;
    private List<Blades> bladesList = new ArrayList<>();
    private Random random = new Random();


    public GamePanel() { 
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(pannelSize));
        setMinimumSize(new Dimension(pannelSize));
    }

    // Update game logic
    public void Update() {
        // this method is just for jumping
        // Primo salto quando il player è a terra
        if (keyboardInputs.space && player.isOnGround()) {
            player.jump(); // Inizia il primo salto
            spaceReleased = false; // Resetta il rilascio del tasto
        }

        // Continuazione del salto quando tieni premuto lo spazio (per regolare l'altezza)
        if (keyboardInputs.space && player.isJumping) {
            player.continueJump(); // Aumenta l'altezza del primo salto
        }

        // Verifica se il tasto spazio è stato rilasciato
        if (!keyboardInputs.space) {
            spaceReleased = true; // Il tasto è stato rilasciato
        }

        // Doppio salto: si attiva solo se il tasto è stato rilasciato dopo il primo salto
        if (keyboardInputs.space && !player.onGround && !player.isJumping && player.doubleJump && spaceReleased) {
            player.doubleJump(); // Attiva il doppio salto
            spaceReleased = false; // Dopo il doppio salto, resetta il flag
        }
        
        if (player.isJumping && player.getState() > 0 || keyboardInputs.right && !player.onGround){
            player.setJumpRightState();
        }
        if (!player.isJumping && player.getState() > 0 && !player.onGround){
            player.setDoubleJumpRightState();
        }
        if (player.isJumping && player.getState() < 0 || keyboardInputs.left && !player.onGround){
            player.setJumpLeftState();
        }
        if (!player.isJumping && player.getState() < 0 && !player.onGround){
            player.setDoubleJumpLeftState();
        }
        if(!keyboardInputs.space){

        }
        // left and right movment
        if (player.getState() > 0 && player.onGround){
            player.setIdleRightState();
        }
        if (keyboardInputs.right) {
            if(player.onGround){
                player.setRunRightState(); // Change state to runRight
            }
            player.moveX(player.speedX);
        }

        if (player.getState() < 0 && player.onGround){
            player.setIdleLeftState();
        }       
        if (keyboardInputs.left) {
            if(player.onGround){
                player.setRunLeftState(); // Change state to runLeft
            }
            player.moveX(-player.speedX);
        }

        for (Blades blade : bladesList) {
            if(player.y < blade.y)
                blade.destroyed = true;
        }

        // Update player's state and position
        player.update();

        
        // updates the bladesList and removes all destroyed blades
        bladesList.removeIf(blade -> blade.isDestroyed());
        for (Blades blade : bladesList) {
            blade.update();
        }
        if (random.nextInt(100) < 5) { // spawn probability
            bladesList.add(new Blades());
        }
    }

    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // applies the scale factor
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1/scaleFactor, 1/scaleFactor);

        // adds the player
        player.draw(g2d);
        for (Blades blade : bladesList) {
            blade.draw(g2d);
        }
        Update();
    }

}//end class
