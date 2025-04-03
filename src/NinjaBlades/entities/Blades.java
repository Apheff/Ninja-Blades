package ninjablades.entities;

import java.awt.image.BufferedImage;
import java.util.Random;

import ninjablades.utils.ConfigManager;

import static ninjablades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static ninjablades.utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Graphics2D;

public class Blades extends Entity{
    
    Random random = new Random();
    public boolean destroyed = false;
    static BufferedImage spriteSheet;

    public Blades() {

        // sets the size of the frames
        this.frames = new BufferedImage[2][3];

        // Load the sprite sheet only once
        if (spriteSheet == null) {
            spriteSheet = loadImage("blades.png");
            if (spriteSheet == null) {
                // Image not found; stop further processing.
                System.err.println("Failed to load blades.png");
                return;
            }
        }
        this.x = random.nextInt(PANEL_WIDTH - this.width); // centers the player in the middle of the panel
        this.y = - this.height;
        this.frameDelay = 3;
        // random velocity
        this.speedX = random.nextBoolean() ? 2 : -2;
        this.speedY = 6; // constant velocity
        this.state = 0; // sets the state to red (default)
        this.theme = ConfigManager.getTheme();
        //loading the frames
        loadAllFrames();
    }

    /* =========  UPDATE METHOD ========= */

    public void update(){
        if (!destroyed) {
            theme = ConfigManager.getTheme();
            frameCount++;
            bladesMovement();
            // set the hitbox position every time does it move
            hitbox.setBounds(this.x, this.y, this.width, this.height);


            if (frameCount >= frameDelay) {
                currentFrame++;
                frameCount = 0; // Resetta il contatore dei frame
            }
            currentFrame %= frames.length;
        }else{
            smoke.setSmokePosition(this.x, this.y);
        }

    }

    public void drawBlades(Graphics2D g2d) {
        if(!destroyed){
            draw(g2d);
        }else{
            smoke.setSmoke(5, this.x, this.y);
        }
    }

    /* =========  DRAW METHOD ========= */
    public void draw(Graphics2D g2d){
        BufferedImage currentImage = null;

        currentFrame %= frames.length;
        currentImage = frames[theme % 2][currentFrame];

        // if there is a current image, draws it
        if (currentImage != null) {
            g2d.drawImage(currentImage, this.x, this.y, this.width, this.height, null);
        }
    }

    // without gravity or speed reduction
    public void bladesMovement(){
        moveX(speedX);
        moveY(speedY);
        if (this.x == 0 || this.x + this.width == PANEL_WIDTH) {
            this.speedX = -this.speedX; // horizontal bouncing
        }
        if (this.y + this.height == PANEL_HEIGHT) { 
            this.speedY = -this.speedY; // changing the blades direction
        }
    }

    // checks if the player has jumped the blade
    public boolean checkBladeDestroy(Player player){
        boolean destroyed = false;
        if(player.y + player.height < this.y){
            if(player.x + player.width / 2 > this.x && player.x + player.width / 2 < this.x + this.width){
                destroyed = true;
            }
        }
        return destroyed;
    }

    /* =========  DESTRUCTION LOGIC ========= */

    public boolean isDestroyed(){
        return destroyed;
    }

    public void destroyBlade(){
        this.destroyed = true;
    }

    /* =========  LOADING FRAMES METHOD ========= */
    // this method loads the frame from blades.png located on the ../img folder
    public void loadAllFrames() {
        frames[0] = loadFrames(spriteSheet, 0, 0, 3, 32, 32); 
        frames[1] = loadFrames(spriteSheet, 0, 32, 3, 32, 32);
    }
}
