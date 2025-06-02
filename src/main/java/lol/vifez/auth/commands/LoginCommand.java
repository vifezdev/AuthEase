package lol.vifez.auth.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.managers.SecurityManager;
import lol.vifez.auth.managers.SessionManager;
import lol.vifez.auth.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("login|signin")
@CommandPermission("auth.login")
public class LoginCommand extends BaseCommand {

    private final AuthManager authManager;
    private final SecurityManager securityManager;
    private final SessionManager sessionManager;

    public LoginCommand(AuthManager authManager, SecurityManager securityManager, SessionManager sessionManager) {
        this.authManager = authManager;
        this.securityManager = securityManager;
        this.sessionManager = sessionManager;
    }

    @Default
    @Syntax("<password>")
    public void onLogin(CommandSender sender, @Optional String password) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can log in.");
            return;
        }
        Player player = (Player) sender;

        if (authManager.isLoggedIn(player)) {
            player.sendMessage(CC.translate("&cYou are already logged in."));
            return;
        }

        if (securityManager.isLockedOut(player.getUniqueId())) {
            int timeLeft = securityManager.getRemainingLockoutTime(player.getUniqueId());
            player.sendMessage(CC.translate("&cToo many failed attempts. Try again in " + timeLeft + " seconds."));
            return;
        }

        if (password == null) {
            player.sendMessage(CC.translate("&cUsage: /login <password>"));
            return;
        }

        if (authManager.checkPassword(player.getName(), password)) {
            authManager.loginPlayer(player);
            securityManager.resetAttempts(player.getUniqueId());
            sessionManager.setLastLogin(player, System.currentTimeMillis());
            player.sendMessage(CC.translate("&aLogin successful! Welcome back."));
        } else {
            securityManager.recordFailedAttempt(player.getUniqueId());
            int attemptsLeft = securityManager.getAttemptsLeft(player.getUniqueId());
            player.sendMessage(CC.translate("&cIncorrect password! Attempts left: " + attemptsLeft));
        }
    }
}