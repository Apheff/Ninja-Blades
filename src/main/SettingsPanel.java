package main;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import utils.KeyboardInputs;
import utils.SoundManager;

import static utils.Constants.GamePanel.PANEL_HEIGHT;
import static utils.Constants.GamePanel.PANEL_WIDTH;
import static utils.Constants.GamePanel.customFont;
import static utils.Constants.GamePanel.customYellow;
import static utils.Constants.GamePanel.scaleFactor;

public class SettingsPanel extends JPanel implements KeyListener {
    
    private MainClass mainClass;

    // Components for settings
    private JSlider volumeSlider;
    
    private String[] options = {"Controls: WASD", "< Back"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private boolean active = false;
    private int value = 100;
    
    public SettingsPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        addKeyListener(this);
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setBackground(Color.BLACK);
        setLayout(null);
        
        // Create volume slider
        volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, value);
        volumeSlider.setBackground(Color.BLACK);
        volumeSlider.setFocusable(true);
        // Imposta un bordo trasparente iniziale
        volumeSlider.setBounds((PANEL_WIDTH - 400)/2, PANEL_HEIGHT/3 + 160, 400, 40);
        
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                value = volumeSlider.getValue();
                System.out.println("Volume: " + value);
                SoundManager.setVolume(value);
            }
        });
        add(volumeSlider);
    }
    
    public void showPanel() {
        active = true;
        requestFocusInWindow(); // Mantiene il focus sulla finestra
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
        g2d.scale(scaleFactor, scaleFactor);

        // Draw a background overlay (already set by components but additional painting can be done)
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Draw title if desired
        g2d.setColor(Color.WHITE);
        g2d.setFont(customFont);

        String title = "SETTINGS";
        int titleWidth = g2d.getFontMetrics().stringWidth(title);
        g2d.drawString(title, (PANEL_WIDTH - titleWidth) / 2, PANEL_HEIGHT/3);

        String volumeLabel = "volume";
        int volumeWidth = g2d.getFontMetrics().stringWidth(volumeLabel);
        g2d.drawString(volumeLabel, (PANEL_WIDTH - volumeWidth) / 2, PANEL_HEIGHT/3  + 160);

        int startY = PANEL_HEIGHT / 2 + 80; // Punto di partenza verticale per i pulsanti
        int spacing = 160; // Distanza tra i pulsanti

        
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                if(i == 0){
                    options[i] = "Controls: " + KeyboardInputs.getControlType();
                }
                g2d.setColor(customYellow); // Evidenziazione per l'opzione selezionata
            } else {
                g2d.setColor(Color.WHITE);
            }
            
            String text = options[i];
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textX = (PANEL_WIDTH - textWidth) / 2;
            int textY = startY + (i * spacing);
            g2d.drawString(text, textX, textY);
            if(i ==0){
                g2d.fillRect(textX, startY + 8, textWidth, 4);
            }
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
            case KeyEvent.VK_LEFT:
                volumeSlider.setValue(volumeSlider.getValue() - 10);
                break;
            case KeyEvent.VK_RIGHT:
                volumeSlider.setValue(volumeSlider.getValue() + 10);
                break;
            case KeyEvent.VK_ENTER:
                selectOption();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
        repaint();
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