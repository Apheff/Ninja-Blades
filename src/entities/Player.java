package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GameWindow.*;
import static utils.Constants.PlayerConstants.*;

public class Player extends Entity{

    public boolean onGround;
    public boolean doubleJump = false; // Modificato: inizia come false, sarà abilitato solo dopo il primo salto
    public boolean isJumping = false;  // Indica se il giocatore sta eseguendo il primo salto

    private long jumpStartTime; // Tempo di inizio della pressione del tasto per il primo salto
    private final long MAX_JUMP_HOLD_TIME = 250; // Tempo massimo per il salto lungo (in millisecondi)
    private int jumpForce; // Forza del salto variabile in base al tempo di pressione

    private final int FIXED_DOUBLE_JUMP = -15; // Forza fissa per il secondo salto
    private final int INITIAL_JUMP_FORCE = -10; // Valore iniziale per il primo salto

    private double gravity = 0.6f;

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
        this.x = PANEL_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = PANEL_HEIGHT - this.height;
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
        loadAllFrames();
    }


    // Metodo per gestire l'inizio del salto
    public void jump() {
        if (onGround) {
            jumpStartTime = System.currentTimeMillis(); // Inizia il timer per il primo salto
            isJumping = true;
            onGround = false; // Il giocatore è ora in aria
            jumpForce = INITIAL_JUMP_FORCE; // Imposta la forza iniziale del primo salto
        }
    }

    public void doubleJump(){
        if (doubleJump && !onGround) { // Assicurati che il tasto sia stato rilasciato
            doubleJump = false;  // Impedisce ulteriori doppi salti
            speedY = FIXED_DOUBLE_JUMP; // Imposta la velocità fissa per il doppio salto
        }
    }

    // Metodo per gestire la continuazione del salto (mentre il tasto è tenuto premuto)
    public void continueJump() {
        if (isJumping) {
            long heldTime = System.currentTimeMillis() - jumpStartTime;
            // Aumenta l'altezza del salto se il tasto viene tenuto premuto, fino al limite massimo
            if (heldTime < MAX_JUMP_HOLD_TIME) {
                jumpForce = INITIAL_JUMP_FORCE - (int)(heldTime / 40); // La forza aumenta fino a un certo limite
            }else{
                isJumping = false;
            }   

            speedY = jumpForce; // Applica la forza del salto aggiornata
        }
    }

    // Metodo per applicare la gravità e gestire la logica del movimento
    public void applyGravity() {
        if (!onGround) {
            speedY += gravity; // La gravità agisce sul giocatore
            this.y += speedY;

            if (y >= SCREEN_HEIGHT - this.height) {
                y = SCREEN_HEIGHT - this.height;
                onGround = true;
                doubleJump = false; // Resetta il doppio salto
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
    public void setIdleRightState(){
        this.state = IDLE_RIGHT;
    }
    public void setRunRightState(){
        this.state = RUN_RIGHT;
    }
    public void setJumpRightState(){
        this.state = JUMP_RIGHT;
    }
    public void setDoubleJumpRightState(){
        this.state = DOUBLE_JUMP_RIGHT;
    }
    public void setIdleLeftState(){
        this.state = IDLE_LEFT;
    }
    public void setRunLeftState(){
        this.state = RUN_LEFT;
    }
    public void setJumpLeftState(){
        this.state = JUMP_LEFT;
    }
    public void setDoubleJumpLeftState(){
        this.state = DOUBLE_JUMP_LEFT;
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


    // get boolean Methods
    public boolean canDoubleJump(){
        return doubleJump;
    }
    public boolean isOnGround(){
        return onGround;
    }
}