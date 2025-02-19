package utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Constants {
    /* this clas is to store all the constant and to make the other classes slimmer and easy to read  */

    public static class GamePanel {
        public static final int PANEL_WIDTH = 920;
        public static final int PANEL_HEIGHT = 1080;
        public static final int BORDER_WIDTH = 400;
        public static final Dimension panelSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        public static final Font customFont;

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
