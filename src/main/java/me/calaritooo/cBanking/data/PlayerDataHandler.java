package me.calaritooo.cBanking.data;

import me.calaritooo.cBanking.cBanking;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
public class PlayerDataHandler {

    private final cBanking plugin;
    private FileConfiguration playerDataConfig = null;
    private File playerDataConfigFile = null;

    public PlayerDataHandler(cBanking plugin) {
        this.plugin = plugin;
        saveDefaultPlayerDataConfig();
    }

    public void reloadPlayerDataConfig() {
        if (playerDataConfigFile == null) {
            playerDataConfigFile = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataConfigFile);
    }

    public FileConfiguration getPlayerDataConfig() {
        if (playerDataConfig == null) {
            reloadPlayerDataConfig();
        }
        return playerDataConfig;
    }

    public void savePlayerDataConfig() {
        if (playerDataConfig == null || playerDataConfigFile == null) {
            return;
        }
        try {
            getPlayerDataConfig().save(playerDataConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save config to " + playerDataConfigFile);
        }
    }

    public void saveDefaultPlayerDataConfig() {
        if (playerDataConfigFile == null) {
            playerDataConfigFile = new File(plugin.getDataFolder(), "playerdata.yml");
        }
        if (!playerDataConfigFile.exists()) {
            plugin.saveResource("playerdata.yml", false);
        }
    }

    public void savePlayerBankData(String playerName, String playerID, double balance, String bankID, double accountBalance) {
        getPlayerDataConfig().set("players." + playerID + ".playerName", playerName);
        getPlayerDataConfig().set("players." + playerID + ".balance", balance);
        getPlayerDataConfig().set("players." + playerID + ".accounts" + bankID + ".balance", accountBalance);
        savePlayerDataConfig();
    }

    public void savePlayerData(String playerName, String playerID, double balance) {
        getPlayerDataConfig().set("players." + playerID + ".playerName", playerName);
        getPlayerDataConfig().set("players." + playerID + ".balance", balance);
        getPlayerDataConfig().createSection("players." + playerID + ".accounts");
        savePlayerDataConfig();
    }

    public void deletePlayerData(String playerID) {
        getPlayerDataConfig().set("players." + playerID, null);
        savePlayerDataConfig();
    }

    public FileConfiguration loadPlayerData(String playerID) {
        return (FileConfiguration) getPlayerDataConfig().getConfigurationSection("players." + playerID);
    }
}