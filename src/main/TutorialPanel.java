package main;

// Importazioni necessarie
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import entities.Blades;
import entities.Player;
import ui.TutorialEndMenu;
import ui.Smokes;
import utils.KeyboardInputs;
import static utils.Constants.GamePanel.*;
import static utils.Constants.GamePanel.scaleFactor;

public class TutorialPanel extends JPanel {

    private long lastCollisionTime = System.currentTimeMillis();
    private MainClass mainClass;
    private Player player;
    private List<Blades> bladesList = new ArrayList<>();
    private int tutorialStep = 0; // Fasi del tutorial
    private int bladesDestroyed = 0; // Contatore delle lame distrutte
    private TutorialEndMenu tutorialEndMenu;
    private Smokes smokes = new Smokes();
    private String[] tutorialTexts = {
            "MUOVITI A SINISTRA E DESTRA",
            "ESEGUI UN SALTO",
            "ESEGUI UN SALTO PROLUNGATO",
            "ESEGUI UN DOPPIO SALTO!",
            "DISTRUGGI LE 3 LAME SALTANDOCI SOPRA!"
    };

    public TutorialPanel(MainClass mainClass) {
        this.mainClass = mainClass;
        setFocusable(true);
        setBackground(new Color(248, 214, 104));
        setSize(PANEL_WIDTH, PANEL_HEIGHT);
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        // Creiamo il giocatore e colleghiamo gli input
        KeyboardInputs keyboardInputs = new KeyboardInputs();
        player = new Player(keyboardInputs);
        addKeyListener(keyboardInputs);

        tutorialEndMenu = new TutorialEndMenu(this, keyboardInputs); // ✅ Aggiunto il menu di fine tutorial
        spawnBladeForStep(); // Genera la lama iniziale
        
        // Game loop (aggiorna ogni 16 ms ~ 60 FPS)
        Timer tutorialTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTutorial();
                repaint();
            }
        });
        tutorialTimer.start();
    }

    // Genera una lama solo se siamo alla fase giusta
    private void spawnBladeForStep() {
        bladesList.clear();
        if (tutorialStep == 4) {
            bladesList.add(new Blades());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.scale(scaleFactor, scaleFactor);
    
        player.draw(g2d);
    
        for (Blades blade : bladesList) {
            blade.draw(g2d);
        }
    
        // Testo del tutorial
        g2d.setFont(customFont);
        String text = tutorialTexts[tutorialStep];
        int textWidth = g2d.getFontMetrics().stringWidth(text);
        int textX = (PANEL_WIDTH - textWidth) / 2;
        int textY = (int) (PANEL_HEIGHT * 0.25);
    
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, textX + 2, textY + 2);
        smokes.draw(g2d);
        // Disegna il menu di fine tutorial se attivo
        tutorialEndMenu.draw(g2d);
    }
    

    // Logica per avanzare nelle fasi del tutorial
    public void updateTutorial() {
        player.update();

        // Aggiorna le lame
        for (Blades blade : bladesList) {
            blade.update();
        }

        switch (tutorialStep) {
            case 0: // Movimento
                if (player.hasMoved()) {
                    tutorialStep++;
                }
                break;
            case 1: // Salto
                if (player.hasJumped()) {
                    tutorialStep++;
                }
                break;
            case 2: // salto prolungato
                if(player.hasMaxJumped()){
                    tutorialStep++;
                }
            case 3: // Doppio Salto
                if (player.hasDoubleJumped()) {
                    tutorialStep++;
                    spawnBladeForStep(); // Genera la prima lama per la fase 3
                }
                break;
            case 4: // Distruzione delle lame
                if(player.collisionCheck(bladesList.get(0)) && System.currentTimeMillis() > lastCollisionTime) {
                    player.setDamage(2000);
                    lastCollisionTime = System.currentTimeMillis() + 2000;
                }
                if (!bladesList.isEmpty() && bladesList.get(0).y < -bladesList.get(0).height) {
                    bladesList.clear();
                    bladesList.add(new Blades());
                }

                if (!bladesList.isEmpty() && player.checkBladeDestroy(bladesList.get(0))) {
                    bladesDestroyed++;
                    smokes.setSmoke(0, bladesList.get(0).x, bladesList.get(0).y);
                    bladesList.clear(); // Rimuoviamo la lama distrutta
                    if (bladesDestroyed >= 3) {
                        tutorialEndMenu.show(); // ✅ Ora mostra il menu invece del messaggio orribile
                    } else {
                        bladesList.add(new Blades());
                    }
                }
                break;
        }
        repaint();
    }

    // Riprova il tutorial
    public void restartTutorial() {
        tutorialStep = 0;
        bladesDestroyed = 0;
        bladesList.clear();
        spawnBladeForStep();
        tutorialEndMenu.hide(); // Chiudi il menu e ricomincia
    }

    // Torna al menu principale
    public void exitToMenu() {
        mainClass.showMenu();
    }

}
