package entities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import static utils.Constants.Entities.ENTITY_SIZE;
import static utils.Constants.GameWindow.WINDOW_HEIGHT;
import static utils.Constants.GameWindow.WINDOW_WIDTH;

import java.awt.Graphics;
import javax.imageio.ImageIO;

public class Blades extends Entity{

    private BufferedImage [] redFrames;
    private double SPEED_Y = 2;
    private double SPEED_X;
    private Random rand;
    public Blades() {
        this.rand= new Random();
        this.SPEED_X= 5;
        this.x=x;
        this.y=y;
        //this.x = WINDOW_WIDTH / 2 - ENTITY_SIZE / 2; // centers the player in the middle of the panel
        //this.y = WINDOW_HEIGHT - ENTITY_SIZE;
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
        x+=SPEED_X;
        y+=SPEED_Y;
        SPEED_Y+=0.1;
        if (y>=600) { // 
             // Ripristina la posizione
            SPEED_Y = -SPEED_Y * 0.8; // Inverti la velocità e applica un coefficiente di restituzione
        }

        // Controllo dei bordi
        if (x < 0 || x > 535) {
            SPEED_X = -SPEED_X; // Rimbalzo orizzontale
        }
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
