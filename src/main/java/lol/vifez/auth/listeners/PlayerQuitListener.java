package lol.vifez.auth.listeners;

import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.managers.SessionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final AuthManager authManager;
    private final SessionManager sessionManager;

    public PlayerQuitListener(AuthManager authManager, SessionManager sessionManager) {
        this.authManager = authManager;
        this.sessionManager = sessionManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        authManager.logoutPlayer(event.getPlayer());
        sessionManager.clearSession(event.getPlayer());
    }
}
