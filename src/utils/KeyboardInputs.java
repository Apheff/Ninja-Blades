package utils;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import ui.PauseMenu;
import ui.TutorialEndMenu;
import ui.GameOverMenu;

public class KeyboardInputs implements KeyListener {

    public boolean left, right, space;
    private PauseMenu pauseMenu;
    private GameOverMenu gameOverMenu;
    private TutorialEndMenu tutorialEndMenu;

    private static boolean isWASD = false;

    public static void toggleControlType() {
        isWASD = !isWASD;
    }

    public static String getControlType() {
        if(isWASD) {
            return "WASD";
        } else {
            return "Arrow keys";
        }
    }

    // Metodo per collegare il menu di pausa
    public void setPauseMenu(PauseMenu pauseMenu) {
        this.pauseMenu = pauseMenu;
    }

    public void setGameOverMenu(GameOverMenu gameOverMenu) {
        this.gameOverMenu = gameOverMenu;
    }

    public void setEndTutorialMenu(TutorialEndMenu tutorialEndMenu){
        this.tutorialEndMenu = tutorialEndMenu;
    }

    // Metodo per gestire i tasti premuti
    @Override
    public void keyPressed(KeyEvent e) {
        // Se il menu di pausa è attivo, gestiamo solo il menu
        if (pauseMenu != null && pauseMenu.isPaused()) {
            pauseMenu.handleInput(e.getKeyCode());
            return;
        }

        if(gameOverMenu != null && gameOverMenu.isActive()) {
            gameOverMenu.handleInput(e.getKeyCode());
            return;
        }

        if(tutorialEndMenu != null && tutorialEndMenu.isActive()) {
            tutorialEndMenu.handleInput(e.getKeyCode());
            return;
        }

        // Tasto "P" per attivare/disattivare il menu di pausa
        if (e.getKeyCode() == KeyEvent.VK_P || e.getKeyCode() == KeyEvent.VK_ESCAPE && pauseMenu != null) {
            pauseMenu.togglePause();
            return;
        }

        if(isWASD){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    left = true;
                    break;
                case KeyEvent.VK_D:
                    right = true;
                    break;
                case KeyEvent.VK_W:
                    space = true;
                    break;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    left = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = true;
                    break;
                case KeyEvent.VK_UP:
                    space = true;
                    break;
            }
        }
    }

    // Metodo per gestire i tasti rilasciati
    @Override
    public void keyReleased(KeyEvent e) {
        if(isWASD){
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    left = false;
                    break;
                case KeyEvent.VK_D:
                    right = false;
                    break;
                case KeyEvent.VK_W:
                    space = false;
                    break;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    left = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    right = false;
                    break;
                case KeyEvent.VK_UP:
                    space = false;
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // non usato
    }
}
