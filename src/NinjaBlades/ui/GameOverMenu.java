package NinjaBlades.ui;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.customFont;
import static NinjaBlades.utils.Constants.GamePanel.customYellow;

import java.awt.*;
import java.awt.event.KeyEvent;

import NinjaBlades.Panels.GamePanel;
import NinjaBlades.utils.KeyboardInputs;

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

        g2d.setColor(Color.WHITE);

        String scoreText = "Score: " + gamePanel.score;
        int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
        g2d.drawString(scoreText, (PANEL_WIDTH - scoreWidth) / 2, PANEL_HEIGHT / 3 + 80);

        String highScoreText = "High score: " + gamePanel.getHighScore();
        int highscoreWidth = g2d.getFontMetrics().stringWidth(highScoreText);
        g2d.drawString(highScoreText, (PANEL_WIDTH -  highscoreWidth) / 2, PANEL_HEIGHT / 3 + 120);

        // Opzioni di gioco
        g2d.setFont(customFont);
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(customYellow); // Evidenziazione opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (PANEL_WIDTH- textWidth) / 2, PANEL_HEIGHT / 2 + i * 40 + 80);
        }
    }

    public void handleInput(int keyCode) {
        if (!active) return;

        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE){
            selectOption();
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
