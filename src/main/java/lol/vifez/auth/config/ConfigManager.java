package lol.vifez.auth.config;

import lol.vifez.auth.util.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final JavaPlugin plugin;

    private FileConfiguration settingsConfig;
    private File settingsFile;

    private FileConfiguration messagesConfig;
    private File messagesFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;

        loadSettings();
        loadMessages();
    }

    private void loadSettings() {
        settingsFile = new File(plugin.getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            plugin.saveResource("settings.yml", false);
        }
        settingsConfig = YamlConfiguration.loadConfiguration(settingsFile);
    }

    private void loadMessages() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getSettings() {
        return settingsConfig;
    }

    public FileConfiguration getMessages() {
        return messagesConfig;
    }

    public String getMessage(String path) {
        String msg = messagesConfig.getString(path);
        if (msg == null) return CC.translate("&cMessage missing: " + path);
        return CC.translate(msg);
    }

    public void reload() {
        loadSettings();
        loadMessages();
    }

    public void saveSettings() {
        try {
            settingsConfig.save(settingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMessages() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}