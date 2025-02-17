package entities;

import java.awt.image.BufferedImage;
import utils.KeyboardInputs;
import java.awt.Graphics2D;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GameWindow.*;
import static utils.Constants.PlayerConstants.*;
import java.awt.image.RescaleOp;

public class Player extends Entity{

    /*
    * **************************************************************
    * *                                                            *
    * *                   player's variables                       *
    * *                                                            *
    * **************************************************************
    */

    // Player's variables
    public boolean onGround;
    public boolean doubleJump = false; // will be true when the first jump was made
    private boolean holdingJump = true;
    private double variableJumpForce = 0; // this will change to let the player make a long jump
    public int hearts = 3;
    public int score = 0;

    // Powerups variables
    public boolean isMagnetized = false;
    public boolean isInvincible = false;
    public boolean damaged = false;
    public long invincibilityEndTime;
    public long magnetizedEndTime;
    public long damagedEndTime;
    
    // images for the player
    private BufferedImage shieldSheet = loadImage("shield.png");
    private BufferedImage[] idleRightFrames;
    private BufferedImage[] idleLeftFrames;
    private BufferedImage[] runRightFrames;
    private BufferedImage[] runLeftFrames;
    private BufferedImage[] jumpRightFrames;
    private BufferedImage[] jumpLeftFrames;
    private BufferedImage[] doubleJumpRightFrames;    
    private BufferedImage[] doubleJumpLeftFrames;
    private int currentFrame;

    // input for the player
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
        this.speedX = 14;
        /*
         * -1: idleLeft, 1: idleRight,
         * -2: RunLeft, 2: RunRight,
         * -3: JumpLeft, 3: JumpRight,
         * -4: DoubleJumpLeft, 4: DoubleJumpRight
         */
        this.state = 1;

        // Load sprite sheet
        spriteSheet = loadImage("player.png");

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
        if (!onGround || this.y < SCREEN_HEIGHT - this.height) {
            speedY += GRAVITY;
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
                doubleJump = true; // Enable double jump after the first jump
                smoke.setSmoke(9, this.x, this.y + 20);
            }

            // While the space key is held, increase the jump force gradually
            if (holdingJump && variableJumpForce < MAX_JUMP_FORCE) {
                variableJumpForce += jumpIncrement; // Aumenta la forza del salto
                speedY = -jumpIncrement; // applies an upper force
            }

            // If player is in the air and doubleJump is available
            if (doubleJump && !holdingJump) {
                speedY = - FIXED_JUMP_FORCE; // Fixed height for the double jump
                doubleJump = false; // Double jump is now used
                smoke.setSmoke(11, this.x, this.y + 20);
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
        this.hitbox.setBounds(this.x + this.width / 4, this.y + this.width / 4, this.width - this.width / 2, this.height - this.height / 2);
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
        // check if the player is damaged
        checkDamage();

        // check if the player has powerups
        checkPowerups();

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
    public void draw(Graphics2D g2d) {

        BufferedImage currentImage = null;

        switch (state) {
            case IDLE_RIGHT: // idle right
                currentFrame %= idleRightFrames.length;
                currentImage = idleRightFrames[currentFrame];
                break;
            case RUN_RIGHT: // run right
                currentFrame %= runRightFrames.length;
                currentImage = runRightFrames[currentFrame];
                break;
            case JUMP_RIGHT: // jump right
                currentFrame %= jumpRightFrames.length;
                currentImage = jumpRightFrames[currentFrame];
                break;
            case DOUBLE_JUMP_RIGHT: // double-jump right
                currentFrame %= doubleJumpRightFrames.length;
                currentImage = doubleJumpRightFrames[currentFrame];
                break;
            case IDLE_LEFT: // idle left
                currentFrame %= idleLeftFrames.length;
                currentImage = idleLeftFrames[currentFrame];
                break;
            case RUN_LEFT: // run left
                currentFrame %= runLeftFrames.length;
                currentImage = runLeftFrames[currentFrame];
                break;
            case JUMP_LEFT: // jump left
                currentFrame %= jumpLeftFrames.length; 
                currentImage = jumpLeftFrames[currentFrame];
                break;
            case DOUBLE_JUMP_LEFT: // double-jump left
                currentFrame %= doubleJumpLeftFrames.length;
                currentImage = doubleJumpLeftFrames[currentFrame];
                break;
        }

        // if there is a current image, draws it
        if (currentImage != null) {
            drawPlayer(g2d, currentImage);
        }


    }  
    
    // draws the player with all the effects
    public void drawPlayer(Graphics2D g2d, BufferedImage currentImage){
        // If the player is damaged and we're on the "blink" frame, overlay a red tint.
        if (damaged && (System.currentTimeMillis() % 2 == 0)) {
            // Create a filter to add a red tint to the character
            float[] scales = {255f, 0f, 0f, 1f}; // Scale factors for R, G, B, Alpha
            float[] offsets = {0f, 0f, 0f, 0f}; // No offset applied
            RescaleOp op = new RescaleOp(scales, offsets, null);

            // Create a temporary image to apply the effect
            BufferedImage redEffect = new BufferedImage(currentImage.getWidth(), currentImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = redEffect.createGraphics();
            g2.drawImage(currentImage, 0, 0, null);
            g2.dispose();

            // Apply the red tint filter and draw the modified image
            g2d.drawImage(op.filter(redEffect, null), x, y, this.width, this.height, null);
        }else{
            // Draw the base image.
            g2d.drawImage(currentImage, this.x, this.y, this.width, this.height, null);
        }
        // Draw the shield if the player is invincible
        if(isInvincible){
            g2d.drawImage(shieldSheet, this.x, this.y, this.height, this.width, null);
        }
        smoke.draw(g2d);
    }

    /*
    * **************************************************************
    * *                                                            *
    * *                       Set Methods                          *
    * *                                                            *
    * **************************************************************
    */
    public void setState(int newState){
        this.state = newState;
    }

    public void setDamage(long  durationMillis){
        this.damaged = true;
        this.y -= 2;          // to lift the player from the ground
        this.speedY = -10;    // knockback  y (upwards)
        this.x += 10 * state; // knockback x (opposite of the direction the player is facing)
        this.damagedEndTime = System.currentTimeMillis() + durationMillis;
    }

    public void setMagnetize(long  durationMillis){
        isMagnetized = true;
        magnetizedEndTime = System.currentTimeMillis() + durationMillis;
    }

    public void setInvincible(long durationMillis) {
        isInvincible = true;
        invincibilityEndTime = System.currentTimeMillis() + durationMillis;
    }

    public void resetPosition(){
        this.x = PANEL_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = PANEL_HEIGHT - this.height;
        this.isInvincible = false;
        this.damaged = false;
    }
    public void resetPlayer(){
        score = 0;
        hearts = 3;
        resetPosition(); // Assicurati che player abbia un metodo per ripristinare la posizione
        isInvincible = false; // Resetta l'invincibilità
        isMagnetized = false; // Resetta il magnetismo
        damaged = false; // Resetta il danno
    }
    /*
    * **************************************************************
    * *                                                            *
    * *                     check Methods                          *
    * *                                                            *
    * **************************************************************
    */    
    // checks if the player has powerups
    public void checkPowerups(){
        // check if the player is invincible
        if(isInvincible){
            if(System.currentTimeMillis() > invincibilityEndTime){
                isInvincible = false;
            }
        }
        // check if the player is magnetized
        if(isMagnetized){
            if(System.currentTimeMillis() > magnetizedEndTime){
                isMagnetized = false;
            }
        }
    }
    
    // checks if the player is damaged
    public void checkDamage(){
        if(damaged){
            if(System.currentTimeMillis() > damagedEndTime){
                damaged = false;
            }
        }
    }

    /*
    * **************************************************************
    * *                                                            *
    * *               Loading frames Method                        *
    * *                                                            *
    * **************************************************************
    */
    // this method loads the frame from player.png located on the ../img folder

    public void loadAllFrames(){
        idleRightFrames = loadFrames(spriteSheet, 0, 0, 3, 32, 32); // idle right
        runRightFrames = loadFrames(spriteSheet, 0, 32, 4, 32, 32); // run right
        jumpRightFrames = loadFrames(spriteSheet, 0, 64, 1, 32, 32); // jump right
        doubleJumpRightFrames = loadFrames(spriteSheet, 0, 96, 3, 32, 32);  //double jump right
        idleLeftFrames = loadFrames(spriteSheet, 0, 128, 3, 32, 32);  // idle left
        runLeftFrames = loadFrames(spriteSheet, 0, 160, 4, 32, 32);  // run left
        jumpLeftFrames = loadFrames(spriteSheet, 0, 192, 1, 32, 32);  // jump left
        doubleJumpLeftFrames = loadFrames(spriteSheet, 0, 224, 3, 32, 32); // double jump left 
    }
}