package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player {
    private int x, y, width, height;

    private int velocityY = 0; // Player's vertical velocity
    private boolean isJumping = false;
    private boolean canDoubleJump = true; // Tracks if double jump is available
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = 15;
    private final int GROUND_Y = 600; // The Y position of the ground

    public static final int IDLE_RIGHT = 1;
    public static final int RUN_RIGHT = 2;
    public static final int JUMP_RIGHT = 3;
    public static final int DOUBLE_JUMP_RIGHT = 4;
    public static final int IDLE_LEFT = -1;
    public static final int RUN_LEFT = -2;
    public static final int JUMP_LEFT = -3;
    public static final int DOUBLE_JUMP_LEFT = -4;

    private BufferedImage spriteSheet;
    private BufferedImage[] idleRightFrames;
    private BufferedImage[] idleLeftFrames;
    private BufferedImage[] runRightFrames;
    private BufferedImage[] runLeftFrames;
    private BufferedImage[] jumpRightFrames;
    private BufferedImage[] jumpLeftFrames;
    private BufferedImage[] doubleJumpRightFrames;    
    private BufferedImage[] doubleJumpLeftFrames;
    private int currentFrame = 0;
    private int state; // -1: idleLeft, 1: idleRight, -2: RunLeft, 2: RunRight, -3: JumpLeft, 3: JumpRight, -4: DoubleJumpLeft, 4: DoubleJumpRight
    private int frameDelay = 5; // slows the animation
    private int frameCount = 0;

    // Constructor
    public Player(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.state = 1; // initialling idle right

        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("D:/Documents/GitHub/Ninja-Blades/img/player_sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loads the Frames for every state
        loadIdleRight();
        loadRunRight();
        loadJumpRight();
        loadDoubleJumpRight();
        loadIdleLeft();
        loadRunLeft();
        loadJumpLeft();
        loadDoubleJumpLeft();
    }

    /* (General method to load frames from sprite sheet)
     *
     * makes an array of BufferedImages from a spritesheet which is found in the /img directory,
     * the loading image Methods
     */
    private BufferedImage[] loadFrames(int startX, int startY, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(startX + (i * frameWidth), startY, frameWidth, frameHeight);
        }
        return frames;
    }

    // Loading Image Methods
    public void loadIdleRight() {
        idleRightFrames = loadFrames(0, 0, 4, 32, 32); 
    }

    public void loadRunRight() {
        runRightFrames = loadFrames(0, 32, 4, 32, 32);
    }

    public void loadJumpRight() {
        jumpRightFrames = loadFrames(0, 64, 7, 32, 32);
    }

    public void loadDoubleJumpRight() {
        doubleJumpRightFrames = loadFrames(0, 96, 5, 32, 32);
    }

    public void loadIdleLeft() {
        idleLeftFrames = loadFrames(0, 128, 4, 32, 32);
    }

    public void loadRunLeft() {
        runLeftFrames = loadFrames(0, 160, 4, 32, 32);
    }

    public void loadJumpLeft() {
        jumpLeftFrames = loadFrames(0, 192, 7, 32, 32);
    }

    public void loadDoubleJumpLeft() {
        doubleJumpLeftFrames = loadFrames(0, 224, 5, 32, 32);
    }
    
    // Metodo per aggiornare lo stato e il frame corrente
    public void update() {
        // Apply gravity
        if (y < GROUND_Y) {
            velocityY += GRAVITY; // Increase downward velocity
            y += velocityY; // Move player down
        } else {
            // Player has hit the ground, reset jump variables
            y = GROUND_Y;
            velocityY = 0;
            isJumping = false;
            canDoubleJump = true;
        }

        frameCount++;

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }

        // checks the current state and cicles the frames
        switch (state) {
            case (1): // idle right
                currentFrame %= idleRightFrames.length;
                break;
            case (2): // run right
                currentFrame %= runRightFrames.length;
                break;
            case (3): // jump right
                currentFrame %= jumpRightFrames.length;
                break;
            case (4): // double-jump right
                currentFrame %= doubleJumpRightFrames.length;
                break;
            case (-1): // idle left
                currentFrame %= idleLeftFrames.length;
                break;
            case (-2): // run left
                currentFrame %= runLeftFrames.length;
                break;
            case (-3): // jump left
                currentFrame %= jumpLeftFrames.length;
                break;
            case (-4): // double-jump left
                currentFrame %= doubleJumpLeftFrames.length;
                break;
        }
    }

    // Drawing method
    public void draw(Graphics g) {
        BufferedImage currentImage = null;

        switch (state) {
            case (1): // idle right
                currentImage = idleRightFrames[currentFrame];
                break;
            case (2): // run right
                currentImage = runRightFrames[currentFrame];
                break;
            case (3): // jump right
                currentImage = jumpRightFrames[currentFrame];
                break;
            case 4: // double-jump right
                currentImage = doubleJumpRightFrames[currentFrame];
                break;
            case -1: // idle left
                currentImage = idleLeftFrames[currentFrame];
                break;
            case -2: // run left
                currentImage = runLeftFrames[currentFrame];
                break;
            case -3: // jump left
                currentImage = jumpLeftFrames[currentFrame];
                break;
            case -4: // double-jump left
                currentImage = doubleJumpLeftFrames[currentFrame];
                break;
        }
        // if there is a current image, draws it
        if (currentImage != null) {
            g.drawImage(currentImage, x, y, width, height, null);
            this.update();
        }
    }

    // Method to get the Player's bounding box
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Method for jumping
    public void jump() {
        if (!isJumping) {
            // First jump
            velocityY = JUMP_STRENGTH;
            isJumping = true;
        } else if (canDoubleJump) {
            // Double jump
            velocityY = JUMP_STRENGTH;
            canDoubleJump = false; // Disable further double jumps
        }
    }

    // changing state Methods
    public void idleRight(){
        this.state = IDLE_RIGHT;
    }
    public void idleLeft(){
        this.state = IDLE_LEFT;
    }
    public void runRight(){
        this.state = RUN_RIGHT;
    }
    public void runLeft(){
        this.state = RUN_LEFT;
    }
    public void jumpRight(){
        this.state = JUMP_RIGHT;
    }
    public void jumpLeft(){
        this.state = JUMP_LEFT;
    }
    public void doubleJumpRight(){
        this.state = DOUBLE_JUMP_RIGHT;
    }
    public void doubleJumpLeft(){
        this.state = DOUBLE_JUMP_LEFT;
    }

    public int getState() {
        return state;
    }


    // movement
    public void changePosition(int stepX){
        this.x += stepX;
    }
}