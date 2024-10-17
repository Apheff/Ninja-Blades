package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utils.Constants.GameWindow.SCREEN_HEIGHT;
import static utils.Constants.GameWindow.SCREEN_WIDTH;

import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Blades extends Entity{

    private BufferedImage [] redFrames;

    public Blades() {
        this.x = SCREEN_WIDTH / 2 - this.width / 2; // centers the player in the middle of the panel
        this.y = 0;
        // Load sprite sheet
        try {
            spriteSheet = ImageIO.read(new File("src/img/red-blade.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //loading the frames
        loadRed();
    }


    public void update(){
        frameCount++;
        
        bladesMovement();

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }

        currentFrame %= redFrames.length;
    }

    public void draw(Graphics g) {

        BufferedImage currentImage = null;

        if (currentFrame >= redFrames.length){
            currentFrame = 0;
        }
        currentImage = redFrames[currentFrame];

        // if there is a current image, draws it
        if (currentImage != null) {
            g.drawImage(currentImage, x, y, width, height, null);

            this.update();
        }
    }


    public void loadRed() {
        redFrames = loadFrames(0, 0, 3, 30, 30); 
    }
    
    

    //without gravity or speed reduction
    public void bladesMovement(){
        y += speedY;
        x += speedX;
        if (y + this.height > SCREEN_HEIGHT || y < 0) { 
            speedY = -speedY * 0.8; // changing the blades direction
        }
        if (x < 0 || x + width > SCREEN_WIDTH) {
            speedX = -speedX; // horizontal bouncing
        }
    }
}
