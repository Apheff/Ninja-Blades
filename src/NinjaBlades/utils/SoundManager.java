package ninjablades.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private static Map<String, Clip> soundMap = new HashMap<>();


    public static void preloadAllSounds(){
        // Preload sounds
        SoundManager.preloadSound("hit.wav");
        SoundManager.preloadSound("coin.wav");
        SoundManager.preloadSound("shield.wav");
        SoundManager.preloadSound("magnet.wav");
        SoundManager.preloadSound("heal.wav");
        SoundManager.preloadSound("pop.wav");
        SoundManager.preloadSound("explosion.wav");
        SoundManager.preloadSound("background.wav");
    }

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
            soundMap.put(soundFile, clip); // Store the preloaded clip in the map
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void loopSound(String soundFile) {
        Clip clip = soundMap.get(soundFile);
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Clip not ready to loop: " + soundFile);
            preloadSound(soundFile); // Preload the sound if not already loaded
        }
    }

    public static void toggleMute() {
        Clip clip = soundMap.get("background.wav");
        if (clip.isActive()) {
            clip.stop();
        } else {
            clip.start();
        }
    }

    public static boolean isMuted(){
        return !soundMap.get("background.wav").isActive();
    }

    public static void stopSound(String soundFile) {
        Clip clip = soundMap.get(soundFile);
        if (clip != null) {
            clip.stop();
        } else {
            System.err.println("Clip not ready to stop: " + soundFile);
            preloadSound(soundFile); // Preload the sound if not already loaded
        }
    }

    public static void playSound(String soundFile) {
        Clip clip = soundMap.get(soundFile);
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            // System.out.println("Playing sound: " + soundFile);
            clip.start();
        } else {
            System.err.println("Clip not ready to play: " + soundFile);
            preloadSound(soundFile); // Preload the sound if not already loaded
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
            }
        }
        System.out.println("Volume set to: " + volumePercentage + "%");
    }
}