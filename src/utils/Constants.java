package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import static utils.Constants.GamePanel.scaleFactor;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Constants {
    /* this clas is to store all the constant and to make the other classes slimmer and easy to read  */

    public static class GamePanel {
        public static final float dpi = Toolkit.getDefaultToolkit().getScreenResolution(); 
        public static final double scaleFactor = 96/dpi;
        public static final Font customFont;
        public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        public static final int SCREEN_WIDTH = gd.getDisplayMode().getWidth();
        public static final int SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
        public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
        public static final int PANEL_HEIGHT = SCREEN_HEIGHT;
        public static final int PANEL_WIDTH = (int) (SCREEN_WIDTH / 2);
        public static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        public static final int BORDER_WIDTH = (int) (SCREEN_WIDTH / 4 * scaleFactor);

        // DPI 96 corrisponds to 100% scaling dpi

        static {
            Font tempFont;
            try {
                tempFont = Font.createFont(Font.TRUETYPE_FONT, Constants.class.getResourceAsStream("/font/customFont.ttf")).deriveFont(36f);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
                tempFont = new Font("Arial", Font.BOLD, 36);
            }
            customFont = tempFont;
        }
    }
    public static class PlayerConstants {
        
        // states 
        public static final int IDLE_RIGHT = 1;
        public static final int RUN_RIGHT = 2;
        public static final int JUMP_RIGHT = 3;
        public static final int DOUBLE_JUMP_RIGHT = 4;
        public static final int IDLE_LEFT = -1;
        public static final int RUN_LEFT = -2;
        public static final int JUMP_LEFT = -3;
        public static final int DOUBLE_JUMP_LEFT = -4;

        //jump constants
        public static final double jumpIncrement = 14; // will increment the jump force 
        public static final int FIXED_JUMP_FORCE = 20; 
        public static final int MAX_JUMP_FORCE = 200;
        public static final double GRAVITY = .6;
    }
    public static class ItemConstants {
        public static final int COIN = 0;
        public static final int SHIELD = 1;
        public static final int MAGNET = 2;
        public static final int SLOW_TIME = 3;
        public static final int HEART = 4; 
    }
}
