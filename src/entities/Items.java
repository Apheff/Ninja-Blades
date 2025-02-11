package entities;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.Random;
import static utils.Constants.GamePanel.PANEL_HEIGHT;

public class Items extends Entity {
 
    public BufferedImage[][] item;
    public int type = 0; // default coin (0)
    /*
     * 0 coin
     * 1 shield
     * 2 magnet
     * 4 heart
     */

    private boolean destroyed = false;
    private float magnetSpeed = 0.1;

    public Items(int x, int y) {
        item = new BufferedImage[4][5];
        spriteSheet = loadImage("items-test.png");
        this.x = x;
        this.y = y;
        this.speedY = 3;
        this.height = 64;
        this.width = 64;

        // Randomly assign type with higher probability for coin (0)
        Random rand = new Random();
        int randomValue = rand.nextInt(100);
        if (randomValue < 50) {
            type = 0; // 50% chance for coin
        } else if (randomValue < 70) {
            type = 1; // 20% chance for shield
        } else if (randomValue < 85) {
            type = 2; // 15% chance for magnet
        } else {
            type = 4; // 15% chance for clock
        }

        // loading all the frames of all items
        loadAllFrames(); 
    }


    public void draw(Graphics2D g2d) {

        if(currentFrame >= item[type].length){
            currentFrame = 0;
        }
        if(item[type][currentFrame] != null){
            g2d.drawImage(item[type][currentFrame], x, y, width, height, null);
        }
    }


    public void update(Player player){
        frameCount++;
        y += speedY;
        speedY += 0.1;
        if (y >= PANEL_HEIGHT - this.height) { 
            speedY = -speedY * 0.8; // inverts the velocity
        }

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }
        if(player.isMagnetized && player.x > this.x){
            this.x += magnetSpeed;
            magnetSpeed += 0.1;
        }else if(player.isMagnetized && player.x < this.x){
            this.x -= magnetSpeed;
            magnetSpeed += 0.1;
        }else if(player.isMagnetized && player.y > this.y){
            this.y += magnetSpeed;
            magnetSpeed += 0.1;
        }else if(player.isMagnetized && player.y < this.y){
            this.y -= magnetSpeed;
            magnetSpeed += 0.1;
        }
        
    }

    public void loadAllFrames(){
        item[0] = loadFrames(0, 0, 5,32, 32);
        item[1] = loadFrames(0,1 * 32, 5,32, 32);
        item[2] = loadFrames(0, 2 * 32, 5,32, 32);
        item[3] = loadFrames(0, 3 * 32, 1,32, 32);
    }


    public boolean isDestroyed(){
        return destroyed;
    }

    public void destroyItem(){
        this.destroyed = true;
    }
}