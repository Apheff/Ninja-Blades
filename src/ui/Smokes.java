package ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utils.ImageLoader;

public class Smokes extends ImageLoader{
    
    public BufferedImage[][] smokes;
    private int currentFrame;
    private int frameCount;
    private int frameDelay = 10;
    private int state = 0;
    private int x, y;
    private int size = 80;

    public Smokes(){
        // Load smoke img
        smokes = new BufferedImage[15][9];
        spriteSheet = loadImage("smokes.png");
        this.x = 0;
        this.y = 0;

        // load all frames
        loadAllFrames();
    }

    public void setSmokePosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2d){
        if(this.x != 0 && this.y != 0){
            frameCount++;
            if(frameCount >= frameDelay){
                currentFrame++;
                frameCount = 0;
            }

            if(currentFrame < smokes[state].length){
                g2d.drawImage(smokes[state][currentFrame], this.x, this.y, size, size, null);
            }else{
                this.x = 0;
                this.y = 0;
                currentFrame = 0;
            }
        }
    }

    public void setSmoke(int state, int x, int y){
        this.state = state;
        setSmokePosition(x, y);
        this.currentFrame = 0;
    }

    public void loadAllFrames(){
        // load all frames
        smokes[0] = loadFrames(0, 0, 9, 64, 64);
        smokes[1] = loadFrames(0, 1 * 64, 9, 64, 64);
        smokes[2] = loadFrames(0, 2 * 64, 9, 64, 64);
        smokes[3] = loadFrames(0, 3 * 64, 9, 64, 64);
        smokes[4] = loadFrames(0, 4 * 64, 9, 64, 64);
        smokes[5] = loadFrames(0, 5 * 64, 9, 64, 64);
        smokes[6] = loadFrames(0, 6 * 64, 9, 64, 64);
        smokes[7] = loadFrames(0, 7 * 64, 9, 64, 64);
        smokes[8] = loadFrames(0, 8 * 64, 9, 64, 64);
        smokes[9] = loadFrames(0, 9 * 64, 9, 64, 64);
        smokes[10] = loadFrames(0, 10 * 64, 9, 64, 64);
        smokes[11] = loadFrames(0, 11 * 64, 9, 64, 64);
        smokes[12] = loadFrames(0, 12 * 64, 9, 64, 64);
        smokes[13] = loadFrames(0, 13 * 64, 9, 64, 64);
        smokes[14] = loadFrames(0, 14 * 64, 9, 64, 64);
    }
    

}
