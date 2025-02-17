package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {

    public static void playSound(String soundFile) {
        try {
            URL url = SoundManager.class.getClassLoader().getResource("sounds/" + soundFile);
            if (url == null) {
                System.err.println("Sound file not found: " + soundFile);
            }
            
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
