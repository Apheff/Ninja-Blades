package entities;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import static utils.Constants.GamePanel.*;

public class Entity {

    public BufferedImage spriteSheet;
    public int x, y; // position
    public int width, height; // size
    public int state;
    public int frameCount;
    public int frameDelay = 8;
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
    public BufferedImage[] loadFrames(int startX, int startY, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = spriteSheet.getSubimage(startX + (i * frameWidth), startY, frameWidth, frameHeight);
        }
        return frames;
    }

    


    // set Methods
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void moveX(double dx){
        if(x + width + dx <= PANEL_WIDTH && x + dx >= 0)
            this.x += dx;
        if(x + dx < 0)
            this.x = 0;
        if(x + width + dx > PANEL_WIDTH)
            this.x = PANEL_WIDTH - width;
    }
    public void moveY(double dy){
        if(y + height + dy <= PANEL_HEIGHT && y + dy > 0)
            this.y += dy;
        if(y + height + dy > PANEL_HEIGHT)
            this.y = PANEL_HEIGHT - this.height;
        if(y + dy < 0)
            this.y = 0;
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

    // will tell if two entities had a collision
    public boolean getCollision(Rectangle entityHitbox){
        return this.hitbox.intersects(entityHitbox);
    } 
    
}
