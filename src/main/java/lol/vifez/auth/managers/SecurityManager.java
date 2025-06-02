package lol.vifez.auth.managers;

import lol.vifez.auth.config.ConfigManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SecurityManager {

    private final Map<UUID, Integer> failedAttempts = new HashMap<>();
    private final Map<UUID, Long> lockoutExpiry = new HashMap<>();

    private final AuthManager authManager;
    private final ConfigManager configManager;

    public SecurityManager(AuthManager authManager, ConfigManager configManager) {
        this.authManager = authManager;
        this.configManager = configManager;
    }

    public boolean isLockedOut(UUID playerId) {
        if (!lockoutExpiry.containsKey(playerId)) return false;
        long expiry = lockoutExpiry.get(playerId);
        if (System.currentTimeMillis() > expiry) {
            lockoutExpiry.remove(playerId);
            failedAttempts.remove(playerId);
            return false;
        }
        return true;
    }

    public int getRemainingLockoutTime(UUID playerId) {
        if (!lockoutExpiry.containsKey(playerId)) return 0;
        long expiry = lockoutExpiry.get(playerId);
        return (int) ((expiry - System.currentTimeMillis()) / 1000);
    }

    public void recordFailedAttempt(UUID playerId) {
        if (isLockedOut(playerId)) return;

        int attempts = failedAttempts.getOrDefault(playerId, 0);
        attempts++;
        int maxAttempts = configManager.getSettings().getInt("AUTH.LOGIN.MAX_ATTEMPTS", 5);

        if (attempts >= maxAttempts) {
            int lockoutSeconds = configManager.getSettings().getInt("AUTH.LOGIN.LOCKOUT_TIME_SECONDS", 300);
            lockoutExpiry.put(playerId, System.currentTimeMillis() + (lockoutSeconds * 1000L));
            failedAttempts.remove(playerId);
        } else {
            failedAttempts.put(playerId, attempts);
        }
    }

    public int getAttemptsLeft(UUID playerId) {
        int maxAttempts = configManager.getSettings().getInt("AUTH.LOGIN.MAX_ATTEMPTS", 5);
        int attempts = failedAttempts.getOrDefault(playerId, 0);
        return maxAttempts - attempts;
    }

    public void resetAttempts(UUID playerId) {
        failedAttempts.remove(playerId);
        lockoutExpiry.remove(playerId);
    }
}