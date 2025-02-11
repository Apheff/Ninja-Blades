package ui;

import java.awt.Graphics2D;
import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import java.awt.image.BufferedImage;

import utils.ImageLoader;

public class Wallpapers extends ImageLoader{

    public BufferedImage wallpapers[];

    public Wallpapers(){
        this.wallpapers = new BufferedImage[4]; 
        // Load wallpapers
        for(int i = 0; i < 4 ; i++){
            String s = "wallpaper" + i + ".jpg";
            this.wallpapers[i] = loadImage(s);
        }

    }

    public void draw(Graphics2D g2d, int numWallpaper){
        // if there is a current image, draws it
        g2d.drawImage(wallpapers[numWallpaper], 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
    }
}
