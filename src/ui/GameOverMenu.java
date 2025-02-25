package ui;

import main.GamePanel;
import utils.KeyboardInputs;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.customFont;
import static utils.Constants.GamePanel.customYellow;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverMenu{

    private GamePanel gamePanel;
    private String[] options = {"Retry", "Return to Menu", "Exit"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private boolean active = false; // Mostra/nasconde il menu

    public GameOverMenu(GamePanel gamePanel, KeyboardInputs keyboardInputs) {
        this.gamePanel = gamePanel;
        keyboardInputs.setGameOverMenu(this);
    }

    public void show() {
        active = true;
    }

    public void hide() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void draw(Graphics2D g2d) {
        if (!active) return;

        // Overlay semi-trasparente
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Titolo "GAME OVER"
        g2d.setColor(new Color(200, 10, 15));
        g2d.setFont(customFont);
        String title = "GAME OVER";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH - titleWidth) / 2, PANEL_HEIGHT / 3);

        // Opzioni di gioco
        g2d.setFont(customFont);
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(customYellow); // Evidenziazione opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (PANEL_WIDTH- textWidth) / 2, PANEL_HEIGHT / 2 + i * 40);
        }
    }

    public void handleInput(int keyCode) {
        if (!active) return;

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
            case 0:
                gamePanel.resetGame(); // Riparte la partita
                hide();
                break;
            case 1:
                gamePanel.returnToMenu(); // Torna al menu principale
                break;
            case 2:
                System.exit(0);
                break;
        }
    }
}
