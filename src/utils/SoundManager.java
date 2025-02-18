package utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SoundManager {

    private static final Map<String, List<Clip>> soundMap = new HashMap<>();

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
            soundMap.putIfAbsent(soundFile, new LinkedList<>());
            soundMap.get(soundFile).add(clip);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playSound(String soundFile) {
        List<Clip> clips = soundMap.get(soundFile);
        if (clips != null) {
            Clip clip = null;
            for (Clip c : clips) {
                if (!c.isRunning()) {
                    clip = c;
                    break;
                }
            }
            if (clip == null) {
                try {
                    URL url = SoundManager.class.getClassLoader().getResource("sounds/" + soundFile);
                    if (url == null) {
                        System.err.println("Sound file not found: " + soundFile);
                        return;
                    }
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clips.add(clip);
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
            if (clip != null) {
                clip.setFramePosition(0); // Rewind to the beginning
                clip.start();
            }
        } else {
            System.err.println("Sound not preloaded: " + soundFile);
        }
    }

    // New method to update the volume of all clips.
    public static void setVolume(int volumePercentage) {
        // Convert a 0-100 value into decibels.
        float volume = volumePercentage / 100f;
        // Avoid log(0)
        float dB = (float) (volume == 0 ? -80.0 : (Math.log(volume) / Math.log(10.0)) * 20.0);
        for (List<Clip> clips : soundMap.values()) {
            for (Clip clip : clips) {
                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(dB);
                }
            }
        }
    }
}