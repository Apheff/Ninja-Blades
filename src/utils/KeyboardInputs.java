package utils;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyboardInputs implements KeyListener {

    public boolean left, right, space;

    // Method to handle key pressed events
    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("a key is pressed"); // for debugging

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                //System.out.println("left pressed"); // for debugging
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                //System.out.println("right pressed"); // for debugging
                right = true;
                break;
            case KeyEvent.VK_SPACE:    
                space = true;
                break;
        }  
    }


    // Method to handle key released events
    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("a key is released"); // for debugging
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
            case KeyEvent.VK_SPACE:
                space = false;
                break;
        }  
    }

    @Override
    public void keyTyped(KeyEvent e){
        // not used
    }
}