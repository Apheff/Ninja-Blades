package utils;


import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

public class Constants {
    /* this clas is to store all the constant and to make the other classes slimmer and easy to read  */

    public static class BladesConstants {
        public static final int RED_STATE = 0;
        public static final int GREEN_STATE = 1;        
    }
    public static class GamePanel {
        public static final int PANEL_WIDTH = 920;
        public static final int PANEL_HEIGHT = 1080;
        public static final int BORDER_WIDTH = 400;
        public static final Dimension pannelSize = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
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
    }
    public static class GameWindow {
        public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        public static final int SCREEN_WIDTH = gd.getDisplayMode().getWidth();
        public static final int SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
        public static final int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        public static final Dimension SCREEN_SIZE = new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT); 

        // DPI 96 corrisponds to 100% scaling dpi
        public static final double scaleFactor = dpi / 96.0;
    }
}
