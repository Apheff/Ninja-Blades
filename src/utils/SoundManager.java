package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static final Map<String, Clip> soundMap = new HashMap<>();

    public static void preloadSound(String soundFile) {
        try {
            URL url = SoundManager.class.getClassLoader().getResource("sounds/" + soundFile);
            if (url == null) {
                System.err.println("Sound file not found: " + soundFile);
                return;
            }
            System.out.println("Preloading sound: " + soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundMap.put(soundFile, clip);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playSound(String soundFile) {
        Clip clip = soundMap.get(soundFile);

        if (clip != null) {
            if (!clip.isRunning()) {
                clip.setFramePosition(0); // Rewind to the beginning
                System.out.println("Playing sound: " + soundFile);
                clip.start();
            } else {
                System.err.println("Sound already playing: " + soundFile);
            }
        } else {
            System.err.println("Sound not preloaded: " + soundFile);
        }
    }

    // New method to update the volume of all clips.
    public static void setVolume(int volumePercentage) {
        // Convert a 0-100 value into decibels.
        float volume = Math.max(0, Math.min(volumePercentage, 100)) / 100f;
        float dB = (float) (volume == 0 ? -80.0 : (Math.log(volume) / Math.log(10.0)) * 20.0);

        for (Clip clip : soundMap.values()) {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(dB);
                System.out.println("Volume set to: " + volumePercentage + "%");
            }
        }
    }
}