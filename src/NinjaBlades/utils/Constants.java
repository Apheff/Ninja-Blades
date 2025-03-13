package NinjaBlades.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.IOException;

import static NinjaBlades.utils.Constants.GamePanel.scaleFactor;

import java.awt.Color;
import java.awt.Dimension;

public class Constants {
    /* this clas is to store all the constant and to make the other classes slimmer and easy to read  */

    public static class GamePanel {
        public static final int BASE_WIDTH = 1920;
        public static final int BASE_HEIGHT = 1080; 
        public static final Font customFont;
        public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
        public static int SCREEN_WIDTH = (int)(SCREEN_SIZE.getWidth());
        public static int SCREEN_HEIGHT = (int)(SCREEN_SIZE.getHeight());
        public static float scaleX = (float) SCREEN_WIDTH / BASE_WIDTH;
        public static float scaleY = (float) SCREEN_HEIGHT / BASE_HEIGHT;

        // Use the smaller scale to maintain the aspect ratio
        public static float scaleFactor = Math.min(scaleX, scaleY);
        public static int PANEL_HEIGHT = (int)(BASE_HEIGHT);
        public static int PANEL_WIDTH = (int) (BASE_WIDTH / 2);
        public static Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
        public static int BORDER_WIDTH = (int) (BASE_WIDTH / 4);
        public static int BORDER_HEIGHT = (int)(BASE_WIDTH);
        public static Color customYellow = new Color(248, 214, 104);

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
    public static class EntityConstants {
        public static final double GRAVITY = .6 * scaleFactor;
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
        public static final double jumpIncrement = 10 * scaleFactor; // will increment the jump force 
        public static final int FIXED_JUMP_FORCE = (int)(20 * scaleFactor); 
        public static final int MAX_JUMP_FORCE = (int)(280 * scaleFactor);
    }
    public static class ItemConstants {
        public static final int COIN = 0;
        public static final int SHIELD = 1;
        public static final int MAGNET = 2;
        public static final int SLOW_TIME = 3;
        public static final int HEART = 4; 
    }
}
