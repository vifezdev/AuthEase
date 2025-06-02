package lol.vifez.auth.listeners;

import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.util.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private final AuthManager authManager;
    private final HashMap<UUID, Long> lastMessageTimes = new HashMap<>();
    private static final long MESSAGE_COOLDOWN_MS = 3000;

    public PlayerMoveListener(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!authManager.isLoggedIn(player)) {
            if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
                    event.getFrom().getBlockY() != event.getTo().getBlockY() ||
                    event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

                event.setCancelled(true);

                long now = System.currentTimeMillis();
                UUID uuid = player.getUniqueId();

                if (!lastMessageTimes.containsKey(uuid) || now - lastMessageTimes.get(uuid) > MESSAGE_COOLDOWN_MS) {
                    player.sendMessage(CC.translate("&fPlease login with &c/login <password>"));
                    lastMessageTimes.put(uuid, now);
                }
            }
        }
    }
}