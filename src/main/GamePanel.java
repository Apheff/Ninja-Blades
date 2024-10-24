package main;


import java.awt.Dimension;
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
// importing all the constants
import static utils.Constants.GameWindow.*;

public class GamePanel extends JPanel {

    private KeyboardInputs keyboardInputs = new KeyboardInputs();
    private Player player = new Player(keyboardInputs); // Store the player object
    private List<Blades> bladesList = new ArrayList<>();
    private long timeToSpawn = System.currentTimeMillis();
    private long timeLimit = 1000; // ms


    public GamePanel() {
        addKeyListener(keyboardInputs);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(pannelSize));
        setMinimumSize(new Dimension(pannelSize));
    }

    // Update game logic
    public void Update() {
        // Updates player's state, position and handles the frames
        player.update();
        for(Blades balde : bladesList){
            if(player.collisionCheck(balde))
                System.err.println("sei stato colpito");
        }

        // updates the bladesList and removes all destroyed blades
        for (Blades blade : bladesList) {
            if((player.y < blade.y && player.x + player.width / 2 > blade.x && player.x + player.width / 2 < blade.x + blade.width) || blade.y < -blade.height) {
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
        // applies the scale factor
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1/scaleFactor, 1/scaleFactor);

        // adds the player
        g2d.fill(player.hitbox);
        player.draw(g2d);
        for (Blades blade : bladesList) {
            blade.draw(g2d);
        }
        Update();
    }

}//end class
