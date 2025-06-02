package lol.vifez.auth.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.managers.SecurityManager;
import lol.vifez.auth.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("register|signup")
@CommandPermission("auth.register")
public class RegisterCommand extends BaseCommand {

    private final AuthManager authManager;
    private final SecurityManager securityManager;

    public RegisterCommand(AuthManager authManager, SecurityManager securityManager) {
        this.authManager = authManager;
        this.securityManager = securityManager;
    }

    @Default
    @Syntax("<password>")
    public void onRegister(CommandSender sender, @Optional String password) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can register.");
            return;
        }
        Player player = (Player) sender;

        if (authManager.isLoggedIn(player)) {
            player.sendMessage(CC.translate("&cYou are already logged in."));
            return;
        }

        if (password == null) {
            player.sendMessage(CC.translate("&cUsage: /register <password>"));
            return;
        }

        if (password.length() < 6 || password.length() > 32) {
            player.sendMessage(CC.translate("&cPassword length must be between 6 and 32 characters."));
            return;
        }

        boolean registered = authManager.registerPlayer(player.getName(), password);
        if (!registered) {
            player.sendMessage(CC.translate("&cYou are already registered."));
            return;
        }

        player.sendMessage(CC.translate("&aRegistration successful! Please log in."));
    }
}