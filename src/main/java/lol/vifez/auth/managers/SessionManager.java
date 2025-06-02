package lol.vifez.auth.managers;

import lol.vifez.auth.config.ConfigManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private final Map<UUID, Long> lastLoginTimestamps = new HashMap<>();

    private final AuthManager authManager;
    private final ConfigManager configManager;

    public SessionManager(lol.vifez.auth.AuthEase plugin, AuthManager authManager, ConfigManager configManager) {
        this.authManager = authManager;
        this.configManager = configManager;
    }

    public void setLastLogin(Player player, long timestamp) {
        lastLoginTimestamps.put(player.getUniqueId(), timestamp);
    }

    public boolean isAutoLoggedIn(Player player, String currentIp, String storedIp) {
        if (!authManager.isLoggedIn(player)) {
            long lastLogin = lastLoginTimestamps.getOrDefault(player.getUniqueId(), 0L);
            long timeoutSeconds = configManager.getSettings().getLong("AUTH.AUTO_LOGIN.TIMEOUT_SECONDS", 3600L);
            boolean checkIp = configManager.getSettings().getBoolean("AUTH.AUTO_LOGIN.CHECK_IP", true);

            if (System.currentTimeMillis() - lastLogin > timeoutSeconds * 1000) {
                return false;
            }
            if (checkIp && !currentIp.equals(storedIp)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public void clearSession(Player player) {
        lastLoginTimestamps.remove(player.getUniqueId());
    }
}
