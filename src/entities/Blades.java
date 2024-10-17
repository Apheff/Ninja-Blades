package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utils.Constants.Entities.ENTITY_SIZE;
import static utils.Constants.GameWindow.WINDOW_HEIGHT;
import static utils.Constants.GameWindow.WINDOW_WIDTH;

import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Blades extends Entity{

    private BufferedImage [] redFrames;
    private int SPEED = 2;

    public Blades() {
        super();
        this.x = WINDOW_WIDTH / 2 - ENTITY_SIZE / 2; // centers the player in the middle of the panel
        this.y = WINDOW_HEIGHT - ENTITY_SIZE;
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
    
    
    public void bladesMovement(){
        
    }
}
