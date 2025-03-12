package NinjaBlades.ui;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import NinjaBlades.utils.ImageLoader;

public class Wallpapers extends ImageLoader{

    public static BufferedImage wallpapers[];

    public static void LoadWallpapers(){
        wallpapers = new BufferedImage[3]; 
        // Load wallpapers
        for(int i = 0; i < 3 ; i++){
            String s = "wallpaper" + i + ".jpg";
            wallpapers[i] = loadImage(s);
        }

    }

    public static void draw(Graphics2D g2d, int numWallpaper){
        // if there is a current image, draws it
        g2d.drawImage(wallpapers[numWallpaper], 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
    }
}
