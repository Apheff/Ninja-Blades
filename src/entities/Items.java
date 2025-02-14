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
     * 3 heart
     */

    private boolean grabed = false;
    private double magnetSpeed = 0.1;
    private static BufferedImage itemSheet;

    public Items(int x, int y) {
        item = new BufferedImage[4][5];
        if (itemSheet == null) {
            itemSheet = loadImage("items.png");
            if (itemSheet == null) {
                // Image not found; stop further processing.
                System.err.println("Failed to load blades.png");
                return;
            }
        }
        this.x = x;
        this.y = y;
        this.speedY = 3;
        this.height = 56;
        this.width = 56;

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
            type = 3; // 15% chance for heart
        }

        // loading all the frames of all items
        loadAllFrames(); 
    }


    public void draw(Graphics2D g2d) {

        currentFrame %= item[type].length;
        if(item[type][currentFrame] != null){
            g2d.drawImage(item[type][currentFrame], x, y, width, height, null);
        }

    }


    public void update(Player player){
        frameCount++;
        y += speedY;
        speedY += 0.4; // gravity
        if (y >= PANEL_HEIGHT - this.height) { 
            speedY = -speedY * 0.8; // inverts the velocity
        }

        if (frameCount >= frameDelay) {
            currentFrame++;
            frameCount = 0; // Resets the frame counter
        }
        if (player.isMagnetized) {
            if (player.x > this.x) {
                this.x += magnetSpeed;
            } else if (player.x < this.x) {
                this.x -= magnetSpeed;
            }
            
            if (player.y > this.y) {
                this.y += magnetSpeed;
            } else if (player.y < this.y) {
                this.y -= magnetSpeed;
            }
            
            magnetSpeed += 0.2; // or control the increment differently
        }
        hitbox.setBounds(this.x, this.y, this.width, this.height);
    }

    public void loadAllFrames(){
        item[0] = loadFrames(itemSheet, 0, 0, 5,32, 32);
        item[1] = loadFrames(itemSheet, 0,1 * 32, 5,32, 32);
        item[2] = loadFrames(itemSheet, 0, 2 * 32, 5,32, 32);
        item[3] = loadFrames(itemSheet, 0, 3 * 32, 1,32, 32);
    }



    public boolean isDestroyed(){
        return grabed;
    }

    public void destroyItem(){
        this.grabed = true;
    }
}