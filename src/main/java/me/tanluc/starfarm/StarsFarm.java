package me.tanluc.starfarm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.tanluc.starfarm.command.Cmd;
import me.tanluc.starfarm.event.OnBreak;
import me.tanluc.starfarm.event.OnKilled;
import me.tanluc.starfarm.event.OnPickUp;
import me.tanluc.starfarm.event.UiClick;
import me.tanluc.starfarm.fileManager.GuiManager;
import me.tanluc.starfarm.fileManager.MessageManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author TanLuc
 * @version 1.0
 */
public class StarsFarm extends JavaPlugin {
  public static String name;
  
  public static File config;
  
  public static FileConfiguration cc;

  public static File PlayerData;
  public static File messages;
  
  public static StarsFarm instance;
  public static MessageManager messageManager;
  public static GuiManager guiManager;

  public static StarsFarm getInstance() {
    return instance;
  }
  
  public static ArrayList<Player> players = new ArrayList<>();
  
  public static ArrayList<Material> materials = new ArrayList<>();
  public static Map<String, Map<Material, String>> materialsName = new HashMap<>();
  
  public static String prefix;
  
  public String time;
  
  private static Economy econ = null;

  private static boolean isEco;
  private static Map<String, FileConfiguration> guiConfigurations = new HashMap<>();

  
  public void onEnable() {
    instance = this;
    LoadConfig();
    messageManager = new MessageManager();
    guiManager = new GuiManager();
    Bukkit.getConsoleSender().sendMessage("§f+--------------------------------------------------------------------+");
    Bukkit.getConsoleSender().sendMessage("§f| §aAuthor: §4TanLuc");
    Bukkit.getConsoleSender().sendMessage("§f| §aDependencies: §bVault");
    Bukkit.getConsoleSender().sendMessage("§f| §aVersion: §d1.0");
    Bukkit.getConsoleSender().sendMessage("§f+--------------------------------------------------------------------+");
    Bukkit.getPluginManager().registerEvents(new OnBreak(), this);
    Bukkit.getPluginManager().registerEvents(new OnKilled(), this);
    Bukkit.getPluginManager().registerEvents(new OnPickUp(), this);
    getServer().getPluginManager().registerEvents((Listener)new UiClick(), (Plugin)this);

    if (getServer().getPluginManager().getPlugin("Vault") != null) {
      isEco = setupEconomy();
    } else {
      getServer().getPluginManager().disablePlugin((Plugin)this);
    }
    registerCommands();

    LoadGUIConfig("gui/crops");
    LoadGUIConfig("gui/blocks");

    guiManager.loadItemsName();

    Bukkit.getScheduler().runTaskLater(this, () -> {
      guiManager.loadItemsSupport();
      SendLog("Load all blocks supported correctly");
    }, 20L);
  }
  
  public void onDisable() {}
  
  public static Economy getEconomy() {
    return econ;
  }
  
  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null)
      return false; 
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
      return false; 
    econ = (Economy)rsp.getProvider();
    return true;
  }

  private void registerCommands() {
    PluginCommand starsFarmCommand = Objects.requireNonNull(getServer().getPluginCommand("starsfarm"));
    starsFarmCommand.setExecutor(new Cmd());
  }

  public void LoadConfig() {
    config = new File(getDataFolder(), "config.yml");
    if (!config.exists())
      saveResource("config.yml", false); 
    cc = (FileConfiguration)YamlConfiguration.loadConfiguration(config);

    PlayerData = new File(getDataFolder(), "PlayerData");
    if (!PlayerData.exists())
      PlayerData.mkdirs(); 
    this.time = getConfig().getString("time");
    prefix = getConfig().getString("prefix");

    messages = new File(getDataFolder(), "messages.yml");
    if (!messages.exists())
      saveResource("messages.yml", false);
  }

  private void LoadGUIConfig(String guiName) {
    File guiFile = new File(getDataFolder(), guiName + ".yml");
    if (!guiFile.exists())
      saveResource(guiName + ".yml", false);

    FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(guiFile);
    guiConfigurations.put(guiName, guiConfig);
  }

  public void SendLog(String log) { Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + getInstance().getName() + " " + log); }
}
