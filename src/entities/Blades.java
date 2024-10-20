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
        this.x = PANEL_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = 0;
        this.frameDelay = 3;
        this.speedX = random.nextInt(10) + 1; // random velocity
        this.speedY = random.nextInt(10) + 1; // random velocity
        this.state = 0; // sets the state to red (default)
        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("src/img/blade_sheet_prova.png"));
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
                g.drawImage(currentImage, x, y, width, height, null);

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
        y += speedY;
        x += speedX;
        if (x < 0 || x + width > PANEL_WIDTH) {
            speedX = -speedX; // horizontal bouncing
            speedX += random.nextDouble();
        }
        if (y + this.height > PANEL_HEIGHT || y < 0) { 
            speedY = -speedY; // changing the blades direction
        }
    }

    public void setGreenState(){
        state = GREEN_STATE;
    }
    
    public void setRedState(){
        state = RED_STATE;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
