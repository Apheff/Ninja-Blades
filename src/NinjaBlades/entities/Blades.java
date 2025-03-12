package NinjaBlades.entities;

import java.awt.image.BufferedImage;
import java.util.Random;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Graphics2D;

public class Blades extends Entity{
    
    private BufferedImage [] frames;
    Random random = new Random();
    public boolean destroyed = false;
    private static BufferedImage bladeSheet;

    public Blades() {
        // Load the sprite sheet only once
        if (bladeSheet == null) {
            bladeSheet = loadImage("blades.png");
            if (bladeSheet == null) {
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

        //loading the frames
        loadFrames();
    }

    public void update(){
        if (!destroyed) {
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

    public void draw(Graphics2D g2d) {
        if(!destroyed){
            BufferedImage currentImage = null;

            currentFrame %= frames.length;
            currentImage = frames[currentFrame];

            // if there is a current image, draws it
            if (currentImage != null) {
                g2d.drawImage(currentImage, this.x, this.y, this.width, this.height, null);
            }
        }else{
            smoke.setSmoke(5, this.x, this.y);
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

    /*
    * **************************************************************
    * *                                                            *
    * *                   destruction logic                        *
    * *                                                            *
    * **************************************************************
    */
    public boolean isDestroyed(){
        return destroyed;
    }

    public void destroyBlade(){
        this.destroyed = true;
    }

    /*
    * **************************************************************
    * *                                                            *
    * *               Loading frames Method                        *
    * *                                                            *
    * **************************************************************
    */
    // this method loads the frame from blades.png located on the ../img folder
        
    public void loadFrames() {
        frames = loadFrames(bladeSheet, 0, 0, 3, 32, 32); 
    }
}
