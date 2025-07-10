package NinjaBlades.utils;
import java.io.*;
import java.util.Properties;

public class ConfigManager {

    private static final String CONFIG_PATH = "src/config.txt";
    private static Properties properties = new Properties();

    // Carica il file di configurazione
    public static void loadConfig() {
        try (FileReader reader = new FileReader(CONFIG_PATH)) {
            properties.load(reader);
        } catch (FileNotFoundException e) {
            System.out.println("File config.txt non trovato, creando uno nuovo...");
            setDefaults();
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Salva il file di configurazione
    public static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_PATH)) {
            properties.store(writer, "Configurazione di gioco");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Imposta i valori di default
    private static void setDefaults() {
        properties.setProperty("highScore", "0");
        properties.setProperty("WASD", "true");
        properties.setProperty("theme", "0");
        properties.setProperty("volume", "100");
    }

    // Getter e Setter
    public static int getHighScore() {
        return Integer.parseInt(properties.getProperty("highScore", "0"));
    }

    public static void setHighScore(int highScore) {
        properties.setProperty("highScore", String.valueOf(highScore));
        saveConfig();
    }

    public static String getWASD() {
        return properties.getProperty("WASD", "true");
    }

    public static void setWASD(boolean controls) {
        properties.setProperty("WASD", String.valueOf(controls));
        saveConfig();
    }

    public static int getTheme() {
        return Integer.parseInt(properties.getProperty("theme", "0"));
    }

    public static void setTheme(int theme) {
        properties.setProperty("theme", String.valueOf(theme));
        saveConfig();
    }

    public static int getVolume() {
        return Integer.parseInt(properties.getProperty("volume", "50"));
    }

    public static void setVolume(int volume) {
        properties.setProperty("volume", String.valueOf(volume));
        saveConfig();
    }
}
