package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static utils.Constants.GameWindow.SCREEN_HEIGHT;
import static utils.Constants.GameWindow.SCREEN_WIDTH;
import static utils.Constants.BladesConstants.*;

import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Blades extends Entity{
    
    private BufferedImage [] redFrames;
    private BufferedImage [] whiteFrames;
    Random random = new Random();

    public Blades() {
        this.x = SCREEN_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = 0;
        this.frameDelay = 3;
        this.speedX = 10;
        this.speedY = 10;
        this.state = 0; // sets the state to red (default)
        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("src/img/blade_sprite.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //loading the frames
        loadRed();
        loadWhite();
    }


    public void update(){
        frameCount++;

        bladesMovement();

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }
        switch (state) {
            case RED_STATE: // red
                currentFrame %= redFrames.length;
                break;
            case WHITE_STATE: // white
                currentFrame %= whiteFrames.length;
                break;
        }
    }

    public void draw(Graphics g) {

        BufferedImage currentImage = null;

        if (currentFrame >= redFrames.length){
            currentFrame = 0;
        }
        switch (state) {
            case RED_STATE: // red
                currentImage = redFrames[currentFrame];
                break;
            case WHITE_STATE: // white
                currentImage = whiteFrames[currentFrame];
                break;
        }

        // if there is a current image, draws it
        if (currentImage != null) {
            g.drawImage(currentImage, x, y, width, height, null);

            this.update();
        }
    }


    public void loadRed() {
        redFrames = loadFrames(0, 30, 3, 30, 30, 0); 
    }
    
    public void loadWhite(){
        whiteFrames = loadFrames(0, 0, 6, 30, 30, 0); 
    }

    //without gravity or speed reduction
    public void bladesMovement(){
        y += speedY;
        x += speedX;
        if (y + this.height > SCREEN_HEIGHT || y < 0) { 
            speedY = -speedY; // changing the blades direction
        }
        if (x < 0 || x + width > SCREEN_WIDTH) {
            speedX = -speedX; // horizontal bouncing
        }
    }

    public void setWhiteState(){
        state = WHITE_STATE;
    }
    
    public void setRedState(){
        state = RED_STATE;
    }
}
