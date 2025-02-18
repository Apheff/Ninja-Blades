package main;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import utils.KeyboardInputs;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.panelSize;
import static utils.Constants.GameWindow.scaleFactor;

import utils.SoundManager;

public class SettingsPanel extends JPanel implements KeyListener {
    
    private MainClass mainClass;

    // Components for settings
    private JSlider volumeSlider;
    
    private String[] options = {"Controls: WASD", "Back"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private boolean active = false;

    private Font customFont;
    
    public SettingsPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setLayout(null); // using absolute layout for simplicity
        setFocusable(true);
        addKeyListener(this);
        setBackground(new Color(0, 0, 0, 200)); // semi-transparent overlay
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(panelSize));
        setMinimumSize(new Dimension(panelSize));
        
        // Load the custom font
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/customFont.ttf")).deriveFont(36f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.BOLD, 36); // Fallback font
        }

        // Create volume slider
        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setBounds((PANEL_WIDTH - 300) / 2, 200, 300, 50);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setFocusable(true);
        // Update volume in SoundManager when slider drags
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = volumeSlider.getValue();
                SoundManager.setVolume(value);
            }
        });
        add(volumeSlider);
    }
    
    public void showPanel() {
        active = true;
        requestFocusInWindow();
        repaint();
    }
    
    public void hidePanel() {
        active = false;
        repaint();
    }
    
    public boolean isActive() {
        return active;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!active) return;
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(1 / scaleFactor, 1 / scaleFactor);
        // Draw a background overlay (already set by components but additional painting can be done)
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        // Draw title if desired
        g2d.setColor(Color.WHITE);
        g2d.setFont(customFont);

        String title = "SETTINGS";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 70);

        g2d.setFont(customFont);
        int startY = PANEL_HEIGHT / 2; // Punto di partenza verticale per i pulsanti
        int spacing = 60; // Distanza tra i pulsanti

        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                if(i == 0){
                    options[i] = "Controls: " + KeyboardInputs.getControlType();
                }
                g2d.setColor(Color.YELLOW); // Evidenziazione per l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }

            String text = options[i];
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textX = (PANEL_WIDTH - textWidth) / 2;
            int textY = startY + (i * spacing);
            g2d.drawString(text, textX, textY);
        }
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

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
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
        repaint(); // Aggiorna la grafica
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0:
                KeyboardInputs.toggleControlType();
                break;
            case 1:
                mainClass.showMenu();
                break;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) { }
    
    @Override
    public void keyTyped(KeyEvent e) { }
}