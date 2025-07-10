package NinjaBlades.Panels;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import NinjaBlades.NinjaMain;
import NinjaBlades.utils.ConfigManager;
import NinjaBlades.utils.KeyboardInputs;
import NinjaBlades.utils.SoundManager;

import static NinjaBlades.utils.Constants.GamePanel.PANEL_HEIGHT;
import static NinjaBlades.utils.Constants.GamePanel.PANEL_WIDTH;
import static NinjaBlades.utils.Constants.GamePanel.customFont;
import static NinjaBlades.utils.Constants.GamePanel.customYellow;
import static NinjaBlades.utils.Constants.GamePanel.scaleFactor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SettingsPanel extends JPanel implements KeyListener {
    
    private NinjaMain mainClass;

    // Components for settings
    private JSlider volumeSlider;
    
    private String[] options = {"Controls: WASD",  "music: ON",  "< Back"};
    private int selectedOption = 0; // Indice dell'opzione selezionata
    private boolean active = false;
    int value = ConfigManager.getVolume();
    
    public SettingsPanel(NinjaMain mainClass) {
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
                }else if (i == 1){
                    options[i] = "music: " + (SoundManager.isMuted() ? "OFF" : "ON");
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
            if(i != options.length - 1){
                g2d.fillRect(textX, startY + 8 + (i * spacing), textWidth, 4);
            }
        }
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
            selectedOption = (selectedOption > 0) ? selectedOption - 1 : options.length - 1;
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
            selectedOption = (selectedOption < options.length - 1) ? selectedOption + 1 : 0;
        } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE){
            selectOption();
        } else if (keyCode == KeyEvent.VK_ESCAPE){
            mainClass.showMenu();
        } else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
            volumeSlider.setValue(volumeSlider.getValue() - 10);
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
            volumeSlider.setValue(volumeSlider.getValue() + 10);
        } else if (keyCode == KeyEvent.VK_ESCAPE){
            mainClass.showMenu();
        }
        repaint();
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0:
                KeyboardInputs.toggleControlType();
                break;
            case 1:
                SoundManager.toggleMute();
                break;
            case 2:
                mainClass.showMenu();
                ConfigManager.setWASD(KeyboardInputs.isWASD());
                ConfigManager.setVolume(value);
                ConfigManager.saveConfig();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    
    @Override
    public void keyTyped(KeyEvent e) { }
}