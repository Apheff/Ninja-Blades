package NinjaBlades.entities;

import java.awt.image.BufferedImage;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.PlayerConstants.*;
import NinjaBlades.utils.ConfigManager;

import java.awt.Graphics2D;
import java.awt.image.RescaleOp;

import NinjaBlades.utils.KeyboardInputs;

public class Player extends Entity{

    /* =========  PLAYER'S VARIABLES ========= */

    // Player's variables
    public boolean onGround;
    public boolean doubleJump = false; // will be true when the first jump was made
    public boolean holdingJump = true;
    public double variableJumpForce = 0; // this will change to let the player make a long jump
    public int hearts = 3;

    // Powerups variables
    public boolean isMagnetized = false;
    public boolean isInvincible = false;
    public boolean damaged = false;
    public long invincibilityEndTime;
    public long magnetizedEndTime;
    public long damagedEndTime;
    
    // images for the player
    public BufferedImage shieldSheet = loadImage("shield.png");
    public BufferedImage[][][] frames;
    public int currentFrame;

    // input for the player
    private KeyboardInputs keyboardInputs;

    /* =========  CONSTRACTOR ========= */

    public Player(KeyboardInputs keyboardInputs) {

        this.keyboardInputs = keyboardInputs;
        this.x = PANEL_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = PANEL_HEIGHT - this.height;
        this.speedX = 14;
        this.frames = new BufferedImage[2][8][4];
        /*
         * 4: idleLeft, 0: idleRight,
         * 5: RunLeft, 1: RunRight,
         * 6: JumpLeft, 2: JumpRight,
         * 7: DoubleJumpLeft, 3: DoubleJumpRight
         */
        this.state = IDLE_RIGHT;

        // Load sprite sheet
        if (spriteSheet == null) {
            spriteSheet = loadImage("player.png");
            if (spriteSheet == null) {
                // Image not found; stop further processing.
                System.err.println("Failed to load player.png");
                return;
            }
        }

        this.theme = ConfigManager.getTheme();

        // Loads the Frames for every state
        loadAllFrames();
    }

    /* =========  UPDATE METHOD ========= */

    @Override
    public void onGroundLogic() {
        // logic for when the player is on the ground
        this.y = PANEL_HEIGHT - this.height;
        this.onGround = true;
        this.doubleJump = false;
        this.speedY = 0;
    }

    public void playerMovement() {

        // left move
        if(keyboardInputs.left){
            this.moveX(-this.speedX);
        }

        // right move
        if(keyboardInputs.right){
            this.moveX(this.speedX);
        }

        // Check if jump button is pressed and released properly before allowing a new jump
        if (keyboardInputs.space) {

            if (this.onGround && !this.holdingJump) {
                // First long jump
                this.holdingJump = true;
                this.variableJumpForce = 0; // Reset jump force
                this.onGround = false; // Player is no longer on the ground
                this.doubleJump = true; // Enable double jump after the first jump
                smoke.setSmoke(1, this.x, this.y);
            }

            // While the space key is held, increase the jump force gradually
            if (this.holdingJump && this.variableJumpForce < MAX_JUMP_FORCE) {
                this.variableJumpForce += jumpIncrement; // Aumenta la forza del salto
                this.speedY = -jumpIncrement; // applies an upper force
            }

            // If player is in the air and doubleJump is available
            if (this.doubleJump && !this.holdingJump) {
                this.speedY = - FIXED_JUMP_FORCE; // Fixed height for the double jump
                this.doubleJump = false; // Double jump is now used
                smoke.setSmoke(2, this.x, this.y);
            }

        }else {
            // Release jump and allow the next jump
            this.holdingJump = false; // Reset after releasing space key
        }

        // Applies gravity
        this.applyGravity();

        // reset of the hitbox
        hitbox.setBounds(this.x + this.width / 4, this.y + this.width / 4, this.width - this.width / 2, this.height - this.height / 2);
    }

    public void update() {
        // check if the player is damaged
        checkDamage();
        theme = ConfigManager.getTheme();
        // check if the player has powerups
        checkPowerups();

        frameCount++;

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }

        // Update player movement
        playerMovement();
        currentFrame %= frames[theme][state].length;
    }

    /* ========= DRAWING METHODS ========= */

    @Override
    public void draw(Graphics2D g2d) {

        BufferedImage currentImage = null;

        currentFrame %= frames[theme][state].length;
        currentImage = frames[theme][state][currentFrame];

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
            g2d.drawImage(op.filter(redEffect, null), this.x, this.y, this.width, this.height, null);
        }else{
            // Draw the base image.
            g2d.drawImage(currentImage, this.x, this.y, this.width, this.height, null);
        }
        // Draw the shield if the player is invincible
        if(isInvincible){
            g2d.drawImage(shieldSheet, this.x, this.y, this.width, this.height, null);
        }
        smoke.draw(g2d);
    }

    /* ========= SET METHODS ========= */

    public void setDamage(long  durationMillis){
        this.damaged = true;
        this.y -= 2;          // to lift the player from the ground
        knockback(12);
        this.damagedEndTime = System.currentTimeMillis() + durationMillis;
    }

    // knockback the player
    public void knockback(int value){
        this.speedY = -value;    // knockback  y (upwards)
        if(state < 4){           // if the player is facing right
            this.x -= value;     // knockback x (opposite of the direction the player is facing)
        }else{
            this.x +=value;
        }
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
    
    /* ========= CHECK METHODS ========= */

    public boolean hasMaxJumped(){
        return variableJumpForce >= MAX_JUMP_FORCE;
    }

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

    public boolean hasJumped(){
        return !onGround;
    }

    public boolean hasDoubleJumped(){
        return !doubleJump && !onGround;
    }

    public boolean isMovingLeft(){
        return keyboardInputs.left;
    }

    public boolean isMovingRight(){
        return keyboardInputs.right;
    }

    public boolean hasMoved(){
        boolean moved = false;
        if(this.isMovingLeft() || this.isMovingRight()){
            moved = true;
        }
        return moved;
    }

    /* ========= LOADING FRAMES METHOD ========= */
    // this method loads the frame from player.png located on the ../img folder
    public void loadAllFrames(){
        frames[0][IDLE_RIGHT] = loadFrames(spriteSheet, 0, 0, 3, 32, 32); // idle right
        frames[0][RUN_RIGHT] = loadFrames(spriteSheet, 0, 32, 4, 32, 32); // run right
        frames[0][JUMP_RIGHT] = loadFrames(spriteSheet, 0, 64, 1, 32, 32); // jump right
        frames[0][DOUBLE_JUMP_RIGHT] = loadFrames(spriteSheet, 0, 96, 3, 32, 32);  //double jump right
        frames[0][IDLE_LEFT] = loadFrames(spriteSheet, 0, 128, 3, 32, 32);  // idle left
        frames[0][RUN_LEFT] = loadFrames(spriteSheet, 0, 160, 4, 32, 32);  // run left
        frames[0][JUMP_LEFT] = loadFrames(spriteSheet, 0, 192, 1, 32, 32);  // jump left
        frames[0][DOUBLE_JUMP_LEFT] = loadFrames(spriteSheet, 0, 224, 3, 32, 32); // double jump left
        frames[1][IDLE_RIGHT] = loadFrames(spriteSheet, 0, 256, 3, 32, 32); // idle right
        frames[1][RUN_RIGHT] = loadFrames(spriteSheet, 0, 288, 4, 32, 32); // run right
        frames[1][JUMP_RIGHT] = loadFrames(spriteSheet, 0, 320, 1, 32, 32); // jump right
        frames[1][DOUBLE_JUMP_RIGHT] = loadFrames(spriteSheet, 0, 352, 3, 32, 32);  //double jump right
        frames[1][IDLE_LEFT] = loadFrames(spriteSheet, 0, 384, 3, 32, 32);  // idle left
        frames[1][RUN_LEFT] = loadFrames(spriteSheet, 0, 416, 4, 32, 32);  // run left
        frames[1][JUMP_LEFT] = loadFrames(spriteSheet, 0, 448, 1, 32, 32);  // jump left
        frames[1][DOUBLE_JUMP_LEFT] = loadFrames(spriteSheet, 0, 480, 3, 32, 32); // double jump left
    }
}