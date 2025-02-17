package ui;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import utils.KeyboardInputs;
import main.GamePanel;
public class PauseMenu {

    private boolean isPaused = false;
    private String[] options = {"Riprendi", "Esci al menu principale"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private GamePanel gamePanel;

    public PauseMenu(GamePanel gamePanel, KeyboardInputs keyboardInputs) {
        this.gamePanel = gamePanel;
        keyboardInputs.setPauseMenu(this);
    }
    public void draw(Graphics2D g2d) {
        if (!isPaused) return;

        // Overlay semi-trasparente
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Titolo "Pausa"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "PAUSA";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH - titleWidth) / 2, PANEL_HEIGHT / 3);

        // Opzioni del menu
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW); // Evidenzia l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT / 2 + i * 40);
        }
    }

    public void handleInput(int keyCode) {
        if (!isPaused) return;
    
        switch (keyCode) {
            case KeyEvent.VK_UP:
                selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
                break;
            case KeyEvent.VK_DOWN:
                selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
                break;
            case KeyEvent.VK_ENTER:
                selectOption();
                break;
        }
    }
    
    private void selectOption() {
        switch (selectedOption) {
            case 0: // Riprendi
                togglePause();
                break;
            case 1: // Torna al Menu
                gamePanel.returnToMenu();
                break;
            case 2: // Esci
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
