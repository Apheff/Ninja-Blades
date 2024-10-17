package entities;

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

    public Entity(){
        this.x = 0;
        this.y = 0;
        this.width = 64;
        this.height = 64;
        this.spriteSheet = null;
        this.state = 0;
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

    // moves the Entity
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    // get Methods
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    // Method to get the Player's bounding box
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getState(){
        return state;
    }
}
