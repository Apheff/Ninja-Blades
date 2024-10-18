package entities;

import static utils.Constants.GameWindow.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

    public BufferedImage spriteSheet;
    public int x, y; // position
    public int width, height; // size
    public int state;
    public int frameCount;
    public int frameDelay = 5;
    public int currentFrame;
    public double speedX;
    public double speedY;
    public Rectangle hitbox;

    public Entity(){
        this.x = 0;
        this.y = 0;
        this.width = 96;
        this.height = 96;
        this.spriteSheet = null;
        this.state = 0;
        this.speedX = 0;
        this.speedY = 0;
        hitbox = new Rectangle(this.x, this.y, this.width, this.height);
    }

    /* (General method to load frames from sprite sheet)
     *
     * makes an array of BufferedImages from a spritesheet which is found in the /img directory,
     * the loading image Methods
     */
    public BufferedImage[] loadFrames(int startX, int startY, int frameCount, int frameWidth, int frameHeight, int offset) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(startX + (i * frameWidth) + offset, startY, frameWidth, frameHeight);
        }
        return frames;
    }

    


    // set Methods
    public boolean setX(int x){
        if(x <= SCREEN_WIDTH){
            this.x = x;
            return true;
        }
        else{
            return false;
        }
    }
    public boolean setY(int y){
        if(y <= SCREEN_HEIGHT){
            this.y = y;
            return true;
        }
        else{
            return false;
        }
    }
    public boolean moveX(double dx){
        if(x + width + dx <= SCREEN_WIDTH && x + dx >= 0){
            this.x += dx;
            return true;
        }
        else{
            //this.x = SCREEN_WIDTH;
            return false;
        }
    }
    public boolean moveY(double dy){
        if(y + height + dy <= SCREEN_WIDTH && y + dy < 0){
            this.x += dy;
            return true;
        }
        else{
            //this.x = SCREEN_HEIGHT;
            return false;
        }
    }



    // get Methods
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getState(){
        return state;
    }

    // Method to get the bounding box
    public Rectangle getHitbox() {
        return hitbox;
    }

}
