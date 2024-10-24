package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import utils.KeyboardInputs;

import java.awt.Graphics;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GameWindow.*;
import static utils.Constants.PlayerConstants.*;

public class Player extends Entity{

    /*
    * **************************************************************
    * *                                                            *
    * *                   player's variables                       *
    * *                                                            *
    * **************************************************************
    */    
    public boolean onGround;
    public boolean doubleJump = false; // will be true when the first jump was made
    private boolean holdingJump = true;
    private final int maxJumpForce = 300;
    private final double gravity = .4;
    private double variableJumpForce = 0; // this will change to let the player make a long jump
    private final double jumpIncrement = 10; // will increment the jump force 
    private int fixedJumpForce = 12; 

    private BufferedImage[] idleRightFrames;
    private BufferedImage[] idleLeftFrames;
    private BufferedImage[] runRightFrames;
    private BufferedImage[] runLeftFrames;
    private BufferedImage[] jumpRightFrames;
    private BufferedImage[] jumpLeftFrames;
    private BufferedImage[] doubleJumpRightFrames;    
    private BufferedImage[] doubleJumpLeftFrames;
    private int currentFrame;
    private KeyboardInputs keyboardInputs;

    /*
    * **************************************************************
    * *                                                            *
    * *                      constractor                           *
    * *                                                            *
    * **************************************************************
    */    
    public Player(KeyboardInputs keyboardInputs) {
        this.keyboardInputs = keyboardInputs;
        this.x = PANEL_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = PANEL_HEIGHT - this.height;
        this.speedX = 6;
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
        loadAllFrames();
    }

    /*
    * **************************************************************
    * *                                                            *
    * *                    movement logic                          *
    * *                                                            *
    * **************************************************************
    */       

    public void applyGravity() {
        if (!onGround) {
            speedY += gravity;
            this.y += speedY;

            if (y >= SCREEN_HEIGHT - this.height) {
                y = SCREEN_HEIGHT - this.height;
                onGround = true;
                doubleJump = false;
                speedY = 0;
            }
        }
    }

    public void playerMovement(){

        // left move
        if(keyboardInputs.left){
            moveX(-speedX);
        }

        // right move
        if(keyboardInputs.right){
            moveX(speedX);
        }

        // Check if jump button is pressed and released properly before allowing a new jump
        if (keyboardInputs.space) {
            if (onGround && !holdingJump) {
                // First long jump
                holdingJump = true;
                variableJumpForce = 0; // Reset jump force
                onGround = false; // Player is no longer on the ground
                variableJumpForce = 0;
                doubleJump = true; // Enable double jump after the first jump
            }

            // While the space key is held, increase the jump force gradually
            if (holdingJump && variableJumpForce < maxJumpForce) {
                variableJumpForce += jumpIncrement; // Aumenta la forza del salto
                speedY = -jumpIncrement; // applies an upper force
            }

            // If player is in the air and doubleJump is available
            if (doubleJump && !holdingJump) {
                speedY = -fixedJumpForce; // Fixed height for the double jump
                doubleJump = false; // Double jump is now used
            }

        }else {
            // Release jump and allow the next jump
            holdingJump = false; // Reset after releasing space key
        }



        // Applies gravity
        applyGravity();

        // sets the state
        setState();

        // reset of the hitbox
        this.hitbox.setBounds(this.x, this.y, this.width, this.height);
    }


    // sets the state for every movement
    public void setState(){
        if(!onGround){
            if(doubleJump){
                // jump frames
                if(this.state > 0 || keyboardInputs.right){
                    this.state = JUMP_RIGHT;
                }
                if(this.state < 0 || keyboardInputs.left){
                    this.state = JUMP_LEFT;
                }
            }else{
                // double jump frames
                if(this.state > 0 || keyboardInputs.right){
                    this.state = DOUBLE_JUMP_RIGHT;
                }
                if(this.state < 0 || keyboardInputs.left){
                    this.state = DOUBLE_JUMP_LEFT;
                }               
            }
        }else{
            // on ground right frames (idle and run) 
            if(keyboardInputs.right){
                this.state = RUN_RIGHT;
            }else if (this.state > 0){
                this.state = IDLE_RIGHT;
            }
            // on ground left frames (idle and run)
            if(keyboardInputs.left){
                this.state = RUN_LEFT;
            }else if (this.state < 0){
                this.state = IDLE_LEFT;
            }
        }
    }


    /*
    * **************************************************************
    * *                                                            *
    * *                      update method                         *
    * *                                                            *
    * **************************************************************
    */    
    public void update() {

        frameCount++;

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }

        // Update player movement
        playerMovement();

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
                if (currentFrame >= idleRightFrames.length){
                    currentFrame = 0;
                }
                currentImage = idleRightFrames[currentFrame];
                break;
            case RUN_RIGHT: // run right
                if (currentFrame >= runRightFrames.length){
                    currentFrame = 0;
                }
                currentImage = runRightFrames[currentFrame];
                break;
            case JUMP_RIGHT: // jump right
                if (currentFrame >= jumpRightFrames.length){
                    currentFrame = 0;
                }
                currentImage = jumpRightFrames[currentFrame];
                break;
            case DOUBLE_JUMP_RIGHT: // double-jump right
                if (currentFrame >= doubleJumpRightFrames.length){
                    currentFrame = 0;
                }
                currentImage = doubleJumpRightFrames[currentFrame];
                break;
            case IDLE_LEFT: // idle left
                if (currentFrame >= idleLeftFrames.length){
                    currentFrame = 0;
                }
                currentImage = idleLeftFrames[currentFrame];
                break;
            case RUN_LEFT: // run left
                if (currentFrame >= runLeftFrames.length){
                    currentFrame = 0;
                }
                currentImage = runLeftFrames[currentFrame];
                break;
            case JUMP_LEFT: // jump left
                if (currentFrame >= jumpLeftFrames.length){
                    currentFrame = 0;
                } 
                currentImage = jumpLeftFrames[currentFrame];
                break;
            case DOUBLE_JUMP_LEFT: // double-jump left
                if (currentFrame >= doubleJumpLeftFrames.length){
                    currentFrame = 0;
                }
                currentImage = doubleJumpLeftFrames[currentFrame];
                break;
        }

        // if there is a current image, draws it
        if (currentImage != null) {
            g.drawImage(currentImage, this.x, this.y, this.height, this.width, null);
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
    public void setState(int newState){
        this.state = newState;
    }

    /*
    * **************************************************************
    * *                                                            *
    * *               Loading frames Method                        *
    * *                                                            *
    * **************************************************************
    */
    // this method loads the frame from a player_sprite located on the ../img folder

    public void loadAllFrames(){
        idleRightFrames = loadFrames(0, 0, 3, 32, 32); // idle right
        runRightFrames = loadFrames(0, 32, 4, 32, 32); // run right
        jumpRightFrames = loadFrames(0, 64, 1, 32, 32); // jump right
        doubleJumpRightFrames = loadFrames(0, 96, 3, 32, 32);  //double jump right
        idleLeftFrames = loadFrames(0, 128, 3, 32, 32);  // idle left
        runLeftFrames = loadFrames(0, 160, 4, 32, 32);  // run left
        jumpLeftFrames = loadFrames(0, 192, 1, 32, 32);  // jump left
        doubleJumpLeftFrames = loadFrames(0, 224, 3, 32, 32); // double jump left 
    }
}