package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static utils.Constants.BladesConstants.*;
import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Blades extends Entity{
    
    private BufferedImage [] redFrames;
    private BufferedImage [] greenFrames;
    Random random = new Random();
    public boolean destroyed = false;

    public Blades() {
        this.x = random.nextInt(PANEL_WIDTH - this.width); // centers the player in the middle of the panel
        this.y = - this.height;
        this.frameDelay = 3;
        // random velocity
        if(random.nextBoolean()){
            this.speedX = 2;
        }else{
            this.speedX = -2;
        }
        
        this.speedY = 2; // constant velocity
        this.state = 0; // sets the state to red (default)
        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("src/img/blades_sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //loading the frames
        loadRed();
        loadGreen();
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

            switch (state) {
                case RED_STATE:
                    currentFrame %= redFrames.length;
                    break;
                case GREEN_STATE:
                    currentFrame %= greenFrames.length;
                    break;
            }
        }

    }

    public void draw(Graphics g) {
        if(!destroyed){
            BufferedImage currentImage = null;

            if (currentFrame >= redFrames.length){
                currentFrame = 0;
            }
            switch (state) {
                case RED_STATE: // red
                    currentImage = redFrames[currentFrame];
                    break;
                case GREEN_STATE: // green
                    currentImage = greenFrames[currentFrame];
                    break;
            }

            // if there is a current image, draws it
            if (currentImage != null) {
                g.drawImage(currentImage, this.x, this.y, this.width, this.height, null);

                this.update();
            }
        }
    }


    public void loadRed() {
        redFrames = loadFrames(0, 0, 3, 32, 32); 
    }
    
    public void loadGreen(){
        greenFrames = loadFrames(0, 32, 3, 32, 32); 
    }

    //without gravity or speed reduction
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

    public void setState(int newState){
        this.state = newState;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
