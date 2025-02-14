package ui;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import entities.Player;

import utils.ImageLoader;

public class HUD extends ImageLoader{
    public int size = 36;
    public int padding = 5; 

    // frames
    public BufferedImage[] blackheart;
    public BufferedImage[] redheart;
    public BufferedImage[] magnet;
    public BufferedImage[] shield;

    // images
    public BufferedImage powerupSheet;
    public BufferedImage heartSheet;

    public HUD(){
        // Load img
        heartSheet = loadImage("hearts.png");
        powerupSheet = loadImage("items.png");
        // Load all frames
        loadAllFrames();
    }

    public void draw(Graphics g, Player player){
        // if there is a current image, draws it
        for (int i = 0; i < player.hearts; i++) {
            int x = padding + (i * (size + padding));
            int y = padding;
            g.drawImage(redheart[0], x, y, size, size, null);
        }
        for(int i = player.hearts; i < 3; i++){
            int x = padding + (i * (size + padding));
            int y = padding;
            g.drawImage(blackheart[0], x, y, size, size, null);
        }

        if(player.isInvincible && player.isMagnetized){
            g.drawImage(shield[0], padding, size + 2 * padding, size, size, null);
            g.drawImage(magnet[0], size + 2 * padding, size + 2 * padding, size, size, null);
        }else if(player.isMagnetized){
            g.drawImage(magnet[0], padding, size + 2 * padding, size, size, null);
        }else if(player.isInvincible){
            g.drawImage(shield[0], padding, size + 2 * padding, size, size, null);
        }
    }

    public void loadAllFrames(){
        // load all frames
        redheart = loadFrames(heartSheet, 0, 0, 1, 32, 32);
        blackheart = loadFrames(heartSheet, 32, 0, 1, 32, 32);
        shield = loadFrames(powerupSheet, 0, 32, 1, 32, 32);
        magnet = loadFrames(powerupSheet, 0, 2 * 32, 1, 32, 32);

    }
    
}
