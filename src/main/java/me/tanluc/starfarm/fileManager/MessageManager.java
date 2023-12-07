package me.tanluc.starfarm.fileManager;

import me.tanluc.starfarm.StarsFarm;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageManager {
    private static YamlConfiguration messagesConfig;

    public MessageManager() {
        // Load the messages from the file
        messagesConfig = YamlConfiguration.loadConfiguration(StarsFarm.messages);
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
}
