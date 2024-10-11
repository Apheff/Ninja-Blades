package utils;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyboardInputs implements KeyListener {

    private boolean moveLeft, moveRight, jump;

    // Method to handle key pressed events
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("a key is pressed"); // for debugging

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                //System.out.println("left pressed");
                moveLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                //System.out.println("right pressed");
                moveRight = true;
                break;
            case KeyEvent.VK_SPACE:
                //System.out.println("space pressed");
                jump = true;
                break;
        }  
    }


    // Method to handle key released events
    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("a key is released"); // for debugging
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                moveLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                moveRight = false;
                break;
            case KeyEvent.VK_SPACE:
                jump = false;
            default :
                break;
        }  
    }

    @Override
    public void keyTyped(KeyEvent e){
        // not used 
    }

    public boolean isMoveLeft() {
        return moveLeft;
    }

    public boolean isMoveRight() {
        return moveRight;
    }

    public boolean isJump() {
        return jump;
    }

    public boolean isIdle(){
        return !jump && !moveLeft && !moveRight;
    }
}