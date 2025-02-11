package ui;

import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Button extends JButton {
    int padding = 5;

    public Button(String text, int x, int y, int width, int height) {
        super(text);
        setBounds(x, y, width, height);
    }

    public static Button createExitButton(int x, int y, int width, int height) {
        Button button = new Button("Exit", x, y, width, height);
        button.addActionListener(e -> System.exit(0));
        return button;
    }

    public static Button createCustomButton(String text, int x, int y, int width, int height, ActionListener actionListener) {
        Button button = new Button(text, x, y, width, height);
        button.addActionListener(actionListener);
        return button;
    }

    public static Button createDefaultButton(String text, int x, int y, int width, int height) {
        return new Button(text, x, y, width, height);
    }
}