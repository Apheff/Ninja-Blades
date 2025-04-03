package ninjablades.entities;

import static ninjablades.utils.Constants.EntityConstants.GRAVITY;
import static ninjablades.utils.Constants.GamePanel.*;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import ninjablades.ui.Smokes;
import ninjablades.utils.ImageLoader;

public class Entity extends ImageLoader{


    /* ========= ENTITY'S VARIABLES ========= */

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
    public int theme;
    public BufferedImage[][] frames;

    /* ========= CONSTRACTOR ========= */

    public Entity(){
        this.x = 0;
        this.y = 0;
        this.width = 80;
        this.height = 80;
        spriteSheet = null;
        this.speedX = 0;
        this.speedY = 0;
        this.smoke = new Smokes();
        hitbox = new Rectangle(this.x, this.y, this.width, this.height);
        this.frames = new BufferedImage[10][10];
        this.theme = 0;
    }

    public void draw(Graphics2D g2d){
        BufferedImage currentImage = null;

        currentFrame %= frames.length;
        currentImage = frames[theme][currentFrame];

        // if there is a current image, draws it
        if (currentImage != null) {
            g2d.drawImage(currentImage, this.x, this.y, this.width, this.height, null);
        }
    }

    // applies the gravity to the entity
    public void applyGravity() {
            this.speedY += GRAVITY;
            this.moveY(this.speedY);

            if (this.y >= PANEL_HEIGHT - this.height) {
                onGroundLogic();
            }
    }

    // applies a logic when on ground
    public void onGroundLogic(){
        if (this.speedY > 0) {
            this.speedY = 0;
        }
    }

    /* =========  SET METHOD ========= */

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

    public void setState(int state){
        this.state = state;
    }

    public void setHitbox(int x, int y, int width, int height){
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public void setTheme(int theme){
        this.theme = theme;
    }

    /* =========  GET METHODS ========= */

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

    public int getTheme(){
        return theme;
    }

    // will test if the hitbox of the entity is colliding with another entity
    public boolean collisionCheck(Entity entity){
        return this.hitbox.intersects(entity.hitbox);
    } 
    
}
