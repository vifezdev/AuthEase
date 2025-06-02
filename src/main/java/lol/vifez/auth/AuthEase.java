package lol.vifez.auth;

import co.aikar.commands.BukkitCommandManager;
import lol.vifez.auth.commands.LoginCommand;
import lol.vifez.auth.commands.RegisterCommand;
import lol.vifez.auth.config.ConfigManager;
import lol.vifez.auth.listeners.*;
import lol.vifez.auth.managers.AuthManager;
import lol.vifez.auth.managers.SecurityManager;
import lol.vifez.auth.managers.SessionManager;
import lol.vifez.auth.storage.FileStorageProvider;
import lol.vifez.auth.storage.StorageProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AuthEase extends JavaPlugin {

    private ConfigManager configManager;
    private StorageProvider storageProvider;
    private AuthManager authManager;
    private SessionManager sessionManager;
    private SecurityManager securityManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);

        configManager = new ConfigManager(this);

        storageProvider = new FileStorageProvider(this);

        authManager = new AuthManager(this, storageProvider, configManager);
        sessionManager = new SessionManager(this, authManager, configManager);
        securityManager = new SecurityManager(authManager, configManager);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(authManager, sessionManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(authManager, sessionManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(authManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(authManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerCommandPreprocessListener(authManager), this);

        getLogger().info("AuthEase enabled.");
        registerCommands();
    }

    private void registerCommands() {
        BukkitCommandManager manager = new BukkitCommandManager(this);

        manager.registerCommand(new LoginCommand(authManager, securityManager, sessionManager));
        manager.registerCommand(new RegisterCommand(authManager, securityManager));
    }

    @Override
    public void onDisable() {
        storageProvider.saveAll();
        getLogger().info("AuthEase disabled.");
    }


    public AuthManager getAuthManager() {
        return authManager;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}