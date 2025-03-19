package NinjaBlades.ui;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.customFont;
import static NinjaBlades.utils.Constants.GamePanel.customYellow;

import java.awt.*;
import java.awt.event.KeyEvent;

import NinjaBlades.Panels.TutorialPanel;
import NinjaBlades.utils.KeyboardInputs;

public class TutorialEndMenu {

    private TutorialPanel tutorialPanel;
    private String[] options = {"Riprova", "Esci"};
    private int selectedOption = 0;
    private boolean active = false;

    public TutorialEndMenu(TutorialPanel tutorialPanel, KeyboardInputs keyboardInputs) {
        this.tutorialPanel = tutorialPanel;
        keyboardInputs.setEndTutorialMenu(this);
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
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Titolo "Tutorial Completato"
        g2d.setColor(Color.WHITE);
        g2d.setFont(customFont);
        String title = "Tutorial Completato!";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH - titleWidth) / 2, PANEL_HEIGHT / 3);

        // Opzioni di fine tutorial
        g2d.setFont(customFont);
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(customYellow);
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (PANEL_WIDTH - textWidth) / 2, PANEL_HEIGHT / 2 + i * 50);
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
        } else if(keyCode == KeyEvent.VK_ESCAPE){
            tutorialPanel.exitToMenu();
        }
    }

    private void selectOption() {
        if (selectedOption == 0) {
            tutorialPanel.restartTutorial(); // Riprova
        } else {
            tutorialPanel.exitToMenu(); // Esci
        }
    }
}
