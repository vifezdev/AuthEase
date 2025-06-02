package lol.vifez.auth.storage;

import lol.vifez.auth.util.CC;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileStorageProvider implements StorageProvider {

    private final JavaPlugin plugin;
    private final File dataFile;
    private final FileConfiguration dataConfig;

    public FileStorageProvider(JavaPlugin plugin) {
        this.plugin = plugin;

        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data.yml", false);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    @Override
    public void saveAll() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save data.yml");
            e.printStackTrace();
        }
    }

    @Override
    public boolean isRegistered(String playerName) {
        return dataConfig.contains(playerName.toLowerCase() + ".password");
    }

    @Override
    public void registerPlayer(String playerName, String hashedPassword) {
        String key = playerName.toLowerCase();
        dataConfig.set(key + ".password", hashedPassword);
        saveAll();
    }

    @Override
    public String getPasswordHash(String playerName) {
        return dataConfig.getString(playerName.toLowerCase() + ".password");
    }

    @Override
    public void setPasswordHash(String playerName, String hashedPassword) {
        dataConfig.set(playerName.toLowerCase() + ".password", hashedPassword);
        saveAll();
    }

    @Override
    public void unregisterPlayer(String playerName) {
        dataConfig.set(playerName.toLowerCase(), null);
        saveAll();
    }

    @Override
    public Map<String, String> getAllPlayers() {
        Map<String, String> players = new HashMap<>();
        for (String key : dataConfig.getKeys(false)) {
            String pass = dataConfig.getString(key + ".password");
            if (pass != null) {
                players.put(key, pass);
            }
        }
        return players;
    }
}