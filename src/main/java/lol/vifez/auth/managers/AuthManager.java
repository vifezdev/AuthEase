package lol.vifez.auth.managers;

import lol.vifez.auth.config.ConfigManager;
import lol.vifez.auth.storage.StorageProvider;
import lol.vifez.auth.util.HashUtil;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthManager {

    private final StorageProvider storage;
    private final ConfigManager configManager;

    private final Map<UUID, Boolean> loggedInPlayers = new HashMap<>();

    public AuthManager(lol.vifez.auth.AuthEase plugin, StorageProvider storage, ConfigManager configManager) {
        this.storage = storage;
        this.configManager = configManager;
    }

    public boolean isRegistered(String playerName) {
        return storage.isRegistered(playerName);
    }

    public boolean registerPlayer(String playerName, String password) {
        if (isRegistered(playerName)) return false;

        String hashed = hashPassword(password);
        storage.registerPlayer(playerName, hashed);
        return true;
    }

    public boolean checkPassword(String playerName, String password) {
        String storedHash = storage.getPasswordHash(playerName);
        if (storedHash == null) return false;
        return storedHash.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        String algorithm = configManager.getSettings().getString("AUTH.PASSWORD.HASH_ALGORITHM", "SHA-256");
        return HashUtil.hash(password, algorithm);
    }

    public void loginPlayer(Player player) {
        loggedInPlayers.put(player.getUniqueId(), true);
    }

    public void logoutPlayer(Player player) {
        loggedInPlayers.remove(player.getUniqueId());
    }

    public boolean isLoggedIn(Player player) {
        return loggedInPlayers.getOrDefault(player.getUniqueId(), false);
    }
}