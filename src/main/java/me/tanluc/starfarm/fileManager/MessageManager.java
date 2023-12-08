package me.tanluc.starfarm.fileManager;

import me.tanluc.starfarm.StarsFarm;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class MessageManager {
    private static YamlConfiguration messagesConfig;

    public MessageManager() {
        // Load the messages from the file
        messagesConfig = YamlConfiguration.loadConfiguration(StarsFarm.messages);
        reloadMessagesConfig();
    }
    public static void reloadConfig() {
        reloadMessagesConfig(); // Load the messages from the file during initialization
    }
    public static String getReloadMsg() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("reload"));
    }
    public static String sellSuccess() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("sell-success"));
    }
    public static String sellMaxToday() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("sell-max-today"));
    }
    public static String notStoraged() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("not-storaged"));
    }
    public static String inventoryFull() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("inventory-full"));
    }
    public static List<String> helpCmd() {
        return messagesConfig.getStringList("commands.help");
    }
    public static String addUsageCmd() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("commands.add.usage"));
    }
    public static String addSellCmd() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("commands.add.sell"));
    }
    public static String addHaveCmd() {
        return ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("commands.add.have"));
    }
    public static void reloadMessagesConfig() {
        messagesConfig = YamlConfiguration.loadConfiguration(StarsFarm.messages);
    }
}
