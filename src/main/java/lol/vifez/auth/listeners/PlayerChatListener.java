package lol.vifez.auth.listeners;

import lol.vifez.auth.managers.AuthManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerChatListener implements Listener {

    private final AuthManager authManager;

    public PlayerChatListener(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (!authManager.isLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou must log in first to chat.");
        }
    }
}