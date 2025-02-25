package entities;

import java.awt.Rectangle;
import ui.Smokes;
import utils.ImageLoader;

import static utils.Constants.GamePanel.*;

public class Entity extends ImageLoader{

    /*
    * **************************************************************
    * *                                                            *
    * *                  Entity's variables                        *
    * *                                                            *
    * **************************************************************
    */    
    public int x, y; // position
    public int width, height; // size
    public int state;
    public int frameCount;
    public int frameDelay = 16;
    public int currentFrame;
    public double speedX;
    public double speedY;
    public Rectangle hitbox;
    public Smokes smoke;

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
        this.smoke = new Smokes();
        hitbox = new Rectangle(this.x, this.y, this.width, this.height);
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

    public void setSpeed(int speedX, int speedY){
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public void setSpeedX(int speedX){
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY){
        this.speedY = speedY;
    }


    /*
    * **************************************************************
    * *                                                            *
    * *                       get methods                          *
    * *                                                            *
    * **************************************************************
    */    
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
    public int getState(){
        return state;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    // will test if the hitbox of the entity is colliding with another entity
    public boolean collisionCheck(Entity entity){
        return this.hitbox.intersects(entity.hitbox);
    } 
    
}
