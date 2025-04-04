package ninjablades.ui;


import static ninjablades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static ninjablades.utils.Constants.GamePanel.PANEL_WIDTH;
import static ninjablades.utils.Constants.GamePanel.customFont;
import static ninjablades.utils.Constants.GamePanel.customYellow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import ninjablades.Panels.GamePanel;
import ninjablades.utils.KeyboardInputs;

public class PauseMenu {

    private boolean isPaused = false;
    private String[] options = {"Resume", "restart", "Return to Menu", "Exit"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private GamePanel gamePanel;

    public PauseMenu(GamePanel gamePanel, KeyboardInputs keyboardInputs) {
        this.gamePanel = gamePanel;
        keyboardInputs.setPauseMenu(this);
    }

    public void draw(Graphics2D g2d){
        if (!isPaused) return;

        // Overlay semi-trasparente
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Titolo "Pausa"
        g2d.setColor(Color.WHITE);
        g2d.setFont(customFont);
        String title = "PAUSA";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH - titleWidth) / 2, PANEL_HEIGHT / 3);

        // Opzioni del menu
        g2d.setFont(customFont);
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(customYellow); // Evidenzia l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT / 2 + i * 40);
        }
    }

    public void handleInput(int keyCode) {
        if (!isPaused) return;
        
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE){
            selectOption();
        } else if(keyCode == KeyEvent.VK_P || keyCode == KeyEvent.VK_ESCAPE){
            togglePause();
        }
    }
    
    private void selectOption() {
        switch (selectedOption) {
            case 0: // Riprendi
                togglePause();
                break;
            case 1: // resetta il gioco
                gamePanel.resetGame();
                break;
            case 2: // Torna al Menu
                gamePanel.returnToMenu();
                break;
            case 3: // Esci
                System.exit(0);
                break;
        }
    }
    
    public void togglePause() {
        isPaused = !isPaused;
    
        // Pausa/Riprendi il timer nel GamePanel
        if (isPaused) {
            gamePanel.pauseGame();
        } else {
            gamePanel.resumeGame();
        }
    }
    
    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public boolean isPaused() {
        return isPaused;
    }
    
}
