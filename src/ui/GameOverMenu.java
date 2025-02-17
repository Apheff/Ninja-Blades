package ui;

import main.GamePanel;
import utils.KeyboardInputs;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverMenu{

    private GamePanel gamePanel;
    private String[] options = {"Riprovare", "Torna al Menu"};
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
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "GAME OVER";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH- titleWidth) / 2, PANEL_HEIGHT / 3);

        // Opzioni di gioco
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW); // Evidenziazione opzione selezionata
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
        if (selectedOption == 0) {
            gamePanel.resetGame(); // Riparte la partita
            hide();
        } else if (selectedOption == 1) {
            gamePanel.returnToMenu(); // Torna al menu principale
        }
    }
}
