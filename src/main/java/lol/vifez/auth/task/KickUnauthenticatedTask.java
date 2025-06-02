package lol.vifez.auth.task;

import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KickUnauthenticatedTask extends BukkitRunnable {

    private final AuthManager authManager;
    private final long kickTimeMillis;

    public KickUnauthenticatedTask(AuthManager authManager, int kickTimeSeconds) {
        this.authManager = authManager;
        this.kickTimeMillis = kickTimeSeconds * 1000L;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!authManager.isLoggedIn(player)) {
                long joined = player.getFirstPlayed();
                if ((now - joined) > kickTimeMillis) {
                    player.kickPlayer(CC.translate("&cYou have been kicked for not logging in."));
                }
            }
        }
    }
}