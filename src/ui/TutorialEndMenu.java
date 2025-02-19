package ui;

import main.TutorialPanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TutorialEndMenu {

    private TutorialPanel tutorialPanel;
    private String[] options = {"Riprova", "Esci"};
    private int selectedOption = 0;
    private boolean active = false;

    public TutorialEndMenu(TutorialPanel tutorialPanel) {
        this.tutorialPanel = tutorialPanel;
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

    public void draw(Graphics2D g2d, int width, int height) {
        if (!active) return;

        // Overlay semi-trasparente
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, width, height);

        // Titolo "Tutorial Completato"
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        String title = "Tutorial Completato!";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, height / 3);

        // Opzioni di fine tutorial
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                g2d.setColor(Color.YELLOW);
            } else {
                g2d.setColor(Color.WHITE);
            }
            int textWidth = g2d.getFontMetrics().stringWidth(options[i]);
            g2d.drawString(options[i], (width - textWidth) / 2, height / 2 + i * 50);
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
            tutorialPanel.restartTutorial(); // Riprova
        } else {
            tutorialPanel.exitToMenu(); // Esci
        }
    }
}
