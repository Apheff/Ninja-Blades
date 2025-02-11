package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.ImageLoader;

public class Hearts extends ImageLoader{
    public int size = 32;
    public int padding = 5; 
    public BufferedImage[] blackheart;
    public BufferedImage[] redheart;

    public Hearts(){
        // Load heart img
        spriteSheet = loadImage("hearts.png");

        // Load all frames
        loadAllFrames();
    }

    public void draw(Graphics g, int hearts){
        // if there is a current image, draws it
        for (int i = 0; i < hearts; i++) {
            int x = padding + (i * (size + padding));
            int y = padding;
            g.drawImage(redheart[0], x, y, size, size, null);
        }
        for(int i = hearts; i < 3; i++){
            int x = padding + (i * (size + padding));
            int y = padding;
            g.drawImage(blackheart[0], x, y, size, size, null);
        }
    }

    public void loadAllFrames(){
        // load all frames
        redheart = loadFrames(0, 0, 1, 32, 32);
        blackheart = loadFrames(32, 0, 1, 32, 32);
    }
    
}
