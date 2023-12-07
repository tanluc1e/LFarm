package me.tanluc.starfarm.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.tanluc.starfarm.data.User;
import me.tanluc.starfarm.ui.Ui;
import me.tanluc.starfarm.StarsFarm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

public class Tk extends BukkitRunnable {
  private YamlConfiguration yaml;
  
  public void run() {
    Calendar calendar = Calendar.getInstance();
    String time = StarsFarm.getInstance().getConfig().getString("time");
    assert time != null;
    String[] args = time.split(":");
    int minute = Integer.parseInt(args[1]);
    int hour = Integer.parseInt(args[0]);

    if (calendar.get(11) == hour && 
      calendar.get(12) == minute) {
      List<File> file = getFile("" + StarsFarm.instance.getDataFolder() + "/PlayerData");
      file.forEach(a -> {
        this.yaml = YamlConfiguration.loadConfiguration(a);

        this.yaml.set(StarsFarm.materials + ".sell", Integer.valueOf(0));
/*
          ConfigurationSection blockSupport = StarsFarm.ui.getConfigurationSection("blocks");
      for (String blockkey : blockSupport.getKeys(false)) {
        if (blockkey.toUpperCase().equals(blockkey))
          this.yaml.set(StarsFarm.materials + ".sell", Integer.valueOf(0));

      }*/

      try {
        this.yaml.save(a);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }


    });
    User.ch.clear();
      Bukkit.getOnlinePlayers().forEach(v -> {
            InventoryView openInventory = v.getOpenInventory();
            String title = openInventory.getTitle();
            if (title.equals(StarsFarm.name))
              Ui.MainUi(v);
          });
    } 
  }
  
  public static List<File> getFile(String path) {
    File file = new File(path);
    List<File> files = new ArrayList<>();
    if (!file.isDirectory()) {
      files.add(file);
    } else {
      File[] subFiles = file.listFiles();
      for (File file1 : subFiles)
        files.addAll(getFile(file1.getAbsolutePath())); 
    } 
    return files;
  }

}
