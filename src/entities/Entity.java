package entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static utils.Constants.GamePanel.*;

public class Entity {

    /*
    * **************************************************************
    * *                                                            *
    * *                  Entity's variables                        *
    * *                                                            *
    * **************************************************************
    */    
    public BufferedImage spriteSheet;
    public int x, y; // position
    public int width, height; // size
    public int state;
    public int frameCount;
    public int frameDelay = 10;
    public int currentFrame;
    public double speedX;
    public double speedY;
    public Rectangle hitbox;

    /*
    * **************************************************************
    * *                                                            *
    * *                     constractor                            *
    * *                                                            *
    * **************************************************************
    */    
    public Entity(){
        this.x = 0;
        this.y = 0;
        this.width = 80;
        this.height = 80;
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
    public BufferedImage[] loadFrames(int startX, int startY, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(startX + (i * frameWidth), startY, frameWidth, frameHeight);
        }
        return frames;
    }

    


    /*
    * **************************************************************
    * *                                                            *
    * *                       set methods                          *
    * *                                                            *
    * **************************************************************
    */    
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void moveX(double dx){
        if (x + dx < 0) {
            this.x = 0; // limits the left border
        } else if (x + width + dx > PANEL_WIDTH) {
            this.x = PANEL_WIDTH - width; // limits the right border
        } else {
            this.x += dx; // normal movement
        }
    }
    public void moveY(double dy){
        if (y + height + dy > PANEL_HEIGHT) {
            this.y = PANEL_HEIGHT - height; // limits the bottom border
        } else {
            this.y += dy; // normal movement
        }
    }


    /*
    * **************************************************************
    * *                                                            *
    * *                       get methods                          *
    * *                                                            *
    * **************************************************************
    */    
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

    public Rectangle getHitbox() {
        return hitbox;
    }

    // will tell if two entities had a collision
    public boolean collisionCheck(Entity entity){
        return this.hitbox.intersects(entity.hitbox);
    } 
    
}
