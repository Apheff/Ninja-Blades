package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;


import static utils.Constants.GameWindow.*;
import static utils.Constants.PlayerConstants.*;

public class Player extends Entity{

    private boolean onGround;
    private boolean doubleJump = true;

    private BufferedImage[] idleRightFrames;
    private BufferedImage[] idleLeftFrames;
    private BufferedImage[] runRightFrames;
    private BufferedImage[] runLeftFrames;
    private BufferedImage[] jumpRightFrames;
    private BufferedImage[] jumpLeftFrames;
    private BufferedImage[] doubleJumpRightFrames;    
    private BufferedImage[] doubleJumpLeftFrames;
    private int currentFrame;

    // Constructor
    public Player() {
        this.x = SCREEN_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = 0;
        this.speedX = 10;
        this.speedY = 0;

        /*
         * -1: idleLeft, 1: idleRight,
         * -2: RunLeft, 2: RunRight,
         * -3: JumpLeft, 3: JumpRight,
         * -4: DoubleJumpLeft, 4: DoubleJumpRight
         */
        this.state = 1;

        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("src/img/player_sprite.png"));
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


    // Handles jumping mechanics
    public void jump() {
        if(onGround){
            speedY = JUMP_FORCE; // first jump is height than the second 
            onGround = false;
            doubleJump = true;
        }else if(!onGround && doubleJump){
            doubleJump = false; 
            speedY = JUMP_FORCE;
        }
    }
    
    // Updates the player's position based on gravity
    public void applyGravity() {
        if (!onGround) {
            speedY += GRAVITY; // Gravity pulls the player down
            this.y += speedY;

            if (y >= SCREEN_HEIGHT - this.height) { // Check if the player has landed
                y = SCREEN_HEIGHT - this.height; // Reset y position to the ground level
                onGround = true;
                doubleJump = true; // resets the double-jump when on ground 
            }
        }
    }


    // Method that updates the state and the current frames
    public void update() {

        frameCount++;

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }

        // Applies gravity
        applyGravity();

        // checks the current state and cicles the frames
        switch (state) {
            case IDLE_RIGHT: // idle right
                currentFrame %= idleRightFrames.length;
                break;
            case RUN_RIGHT: // run right
                currentFrame %= runRightFrames.length;
                break;
            case JUMP_RIGHT: // jump right
                currentFrame %= jumpRightFrames.length;
                break;
            case DOUBLE_JUMP_RIGHT: // double-jump right
                currentFrame %= doubleJumpRightFrames.length;
                break;
            case IDLE_LEFT: // idle left
                currentFrame %= idleLeftFrames.length;
                break;
            case RUN_LEFT: // run left
                currentFrame %= runLeftFrames.length;
                break;
            case JUMP_LEFT: // jump left
                currentFrame %= jumpLeftFrames.length;
                break;
            case DOUBLE_JUMP_LEFT: // double-jump left
                currentFrame %= doubleJumpLeftFrames.length;
                break;
        }
    }


    /*
    * **************************************************************
    * *                                                            *
    * *                    Drawing methods                         *
    * *                                                            *
    * **************************************************************
    */
    public void draw(Graphics g) {

        BufferedImage currentImage = null;

        switch (state) {
            case IDLE_RIGHT: // idle right
                if (currentFrame >= idleRightFrames.length) currentFrame = 0;
                currentImage = idleRightFrames[currentFrame];
                break;
            case RUN_RIGHT: // run right
                if (currentFrame >= runRightFrames.length) currentFrame = 0;
                currentImage = runRightFrames[currentFrame];
                break;
            case JUMP_RIGHT: // jump right
                if (currentFrame >= jumpRightFrames.length) currentFrame = 0;
                currentImage = jumpRightFrames[currentFrame];
                break;
            case DOUBLE_JUMP_RIGHT: // double-jump right
                if (currentFrame >= doubleJumpRightFrames.length) currentFrame = 0;
                currentImage = doubleJumpRightFrames[currentFrame];
                break;
            case IDLE_LEFT: // idle left
                if (currentFrame >= idleLeftFrames.length) currentFrame = 0;
                currentImage = idleLeftFrames[currentFrame];
                break;
            case RUN_LEFT: // run left
                if (currentFrame >= runLeftFrames.length) currentFrame = 0;
                currentImage = runLeftFrames[currentFrame];
                break;
            case JUMP_LEFT: // jump left
                if (currentFrame >= jumpLeftFrames.length) currentFrame = 0;
                currentImage = jumpLeftFrames[currentFrame];
                break;
            case DOUBLE_JUMP_LEFT: // double-jump left
                if (currentFrame >= doubleJumpLeftFrames.length) currentFrame = 0;
                currentImage = doubleJumpLeftFrames[currentFrame];
                break;
        }

        // if there is a current image, draws it
        if (currentImage != null) {
            g.drawImage(currentImage, x, y, height, width, null);
            this.update();
        }

    }

    /*
    * **************************************************************
    * *                                                            *
    * *             Method to change player state                  *
    * *                                                            *
    * **************************************************************
    */
    public void idleRightState(){
        this.state = IDLE_RIGHT;
    }
    public void runRightState(){
        this.state = RUN_RIGHT;
    }
    public void jumpRightState(){
        this.state = JUMP_RIGHT;
    }
    public void doubleJumpRightState(){
        this.state = DOUBLE_JUMP_RIGHT;
    }
    public void idleLeftState(){
        this.state = IDLE_LEFT;
    }
    public void runLeftState(){
        this.state = RUN_LEFT;
    }
    public void jumpLeftState(){
        this.state = JUMP_LEFT;
    }
    public void doubleJumpLeftState(){
        this.state = DOUBLE_JUMP_LEFT;
    }



    /*
    * **************************************************************
    * *                                                            *
    * *               Loading frames Methods                       *
    * *                                                            *
    * **************************************************************
    */
    // those methods loads the frame from a player_sprite located on the ../img folder
    public void loadIdleRight() {
        idleRightFrames = loadFrames(0, 0, 4, 32, 32); 
    }

    public void loadRunRight() {
        runRightFrames = loadFrames(0, 32, 4, 32, 32);
    }

    public void loadJumpRight() {
        jumpRightFrames = loadFrames(96, 64, 1, 32, 32);
    }

    public void loadDoubleJumpRight() {
        doubleJumpRightFrames = loadFrames(0, 96, 3, 32, 32);
    }

    public void loadIdleLeft() {
        idleLeftFrames = loadFrames(0, 128, 4, 32, 32);
    }

    public void loadRunLeft() {
        runLeftFrames = loadFrames(0, 160, 4, 32, 32);
    }

    public void loadJumpLeft() {
        jumpLeftFrames = loadFrames(96, 192, 1, 32, 32);
    }

    public void loadDoubleJumpLeft() {
        doubleJumpLeftFrames = loadFrames(0, 224, 3, 32, 32);
    }

    public boolean canDoubleJump(){
        return doubleJump;
    }
    public boolean isOnGround(){
        return onGround;
    }
}