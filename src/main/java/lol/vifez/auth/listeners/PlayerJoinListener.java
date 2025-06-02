package lol.vifez.auth.listeners;

import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.managers.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final AuthManager authManager;
    private final SessionManager sessionManager;

    public PlayerJoinListener(AuthManager authManager, SessionManager sessionManager) {
        this.authManager = authManager;
        this.sessionManager = sessionManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        authManager.logoutPlayer(event.getPlayer());
        event.getPlayer().sendMessage("§aPlease login or register.");
    }
}