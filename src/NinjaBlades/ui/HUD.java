package NinjaBlades.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import NinjaBlades.entities.Player;
import NinjaBlades.utils.ImageLoader;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.customFont;

import java.awt.Color;

public class HUD extends ImageLoader{
    public static int size = 36;
    public static int padding = 5;

    // frames
    public static BufferedImage[] blackheart;
    public static BufferedImage[] redheart;
    public static BufferedImage[] magnet;
    public static BufferedImage[] shield;

    // images
    public static BufferedImage powerupSheet;
    public static BufferedImage heartSheet;

    public static void loadHUD(){
        // Load img
        heartSheet = loadImage("hearts.png");
        powerupSheet = loadImage("items.png");
        // Load all frames
        loadAllFrames();
    }

    public static void draw(Graphics g, Player player, int score){
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

        String scoreText = "" + score;
        g.setColor(Color.WHITE);
        g.setFont(customFont);
        g.drawString(scoreText, PANEL_WIDTH - g.getFontMetrics().stringWidth(scoreText) - 20, g.getFontMetrics().getHeight());
    }

    public static void loadAllFrames(){
        // load all frames
        redheart = loadFrames(heartSheet, 0, 0, 1, 32, 32);
        blackheart = loadFrames(heartSheet, 32, 0, 1, 32, 32);
        shield = loadFrames(powerupSheet, 0, 32, 1, 32, 32);
        magnet = loadFrames(powerupSheet, 0, 2 * 32, 1, 32, 32);

    }
    
}
