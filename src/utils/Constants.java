package utils;

import static utils.Constants.GameWindow.WINDOW_WIDTH;

public class Constants {
    /* this clas is to store all the constant and to make the other classes slimmer and easy to read  */

    public static class Entities{
        public static final int ENTITY_SIZE = 64;
    }

    public static class PlayerConstants {
        // stats and variables
        public static final int SPEEDX = 10;
        public static final int PLAYER_SIZE = 64;
        //gravity
        public static final int JUMP_FORCE = -20; // Initial velocity upwards
        public static final int GRAVITY = 1; // Gravity pulls the player down

        public static final int MAX_X = WINDOW_WIDTH - PLAYER_SIZE;
        public static final int MIN_X = 0;
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
    public static class GameWindow{
        public static final int WINDOW_WIDTH = 600;
        public static final int WINDOW_HEIGHT = 780; // jpanel size = (720 x 840)
    }
}
