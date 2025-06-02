package lol.vifez.auth.storage;

import java.util.Map;

public interface StorageProvider {

    void saveAll();

    boolean isRegistered(String playerName);

    void registerPlayer(String playerName, String hashedPassword);

    String getPasswordHash(String playerName);

    void setPasswordHash(String playerName, String hashedPassword);

    void unregisterPlayer(String playerName);

    Map<String, String> getAllPlayers();
}