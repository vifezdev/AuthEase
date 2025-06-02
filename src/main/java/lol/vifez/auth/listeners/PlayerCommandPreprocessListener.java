package lol.vifez.auth.listeners;

import lol.vifez.auth.managers.AuthManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    private final AuthManager authManager;

    public PlayerCommandPreprocessListener(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!authManager.isLoggedIn(event.getPlayer())) {
            String cmd = event.getMessage().toLowerCase();
            if (!cmd.startsWith("/login") && !cmd.startsWith("/register")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou must log in first to use commands.");
            }
        }
    }
}