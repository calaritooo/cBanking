package me.github.calaritooo.commands;

import me.github.calaritooo.banks.BankHandler;
import me.github.calaritooo.cBanking;
import me.github.calaritooo.data.PlayerDataHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BanksCommand implements CommandExecutor {

    private final cBanking plugin;
    private final BankHandler bankHandler;
    private final PlayerDataHandler playerDataHandler;

    public BanksCommand(cBanking plugin) {
        this.plugin = plugin;
        this.bankHandler = plugin.getBankHandler();
        this.playerDataHandler = plugin.getPlayerDataHandler();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("cbanking.banks")) {
                player.sendMessage("§cYou do not have access to this command!");
                return true;
            }
        }

        Set<String> bankIDs = bankHandler.getBankIDs();
        final String pluginPrefix = "§e[§acBanking§e]";
        final String pluginHeader = "§f------------------- " + pluginPrefix + " §f-------------------";
        sender.sendMessage(pluginHeader);
        sender.sendMessage("§7List of all banks:");

        int bankNumber = 1;
        for (String bankID : bankIDs) {
            int accountCount = 0;
            for (String playerID : playerDataHandler.getPlayerDataConfig().getConfigurationSection("players").getKeys(false)) {
                if (playerDataHandler.getPlayerDataConfig().contains("players." + playerID + ".accounts." + bankID)) {
                    accountCount++;
                }
            }
            sender.sendMessage("§7" + bankNumber + ". §a" + bankID + "§7: " + accountCount + " accounts");
            sender.sendMessage("§7   - Owner: §c" + bankHandler.getBankOwnerByID(bankID));
            bankNumber++;
        }

        return true;
    }
}