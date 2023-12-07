package me.tanluc.starfarm.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import me.tanluc.starfarm.StarsFarm;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class User {
  public static final HashMap<String, User> ch = new HashMap<>();
  
  private final File data;
  
  private final YamlConfiguration yaml;
  
  private final String name;
  
  public static User of(String name) {
    User user = ch.get(name);
    if (user == null) {
      user = new User(name);
      ch.put(name, user);
    } 
    return user;
  }
  
  public static User of(OfflinePlayer p) {
    return of(p.getName());
  }
  
  private User(String name) {
    this.name = name;
    this.data = new File(StarsFarm.PlayerData, name + ".yml");
    if (!this.data.exists())
      try {
        this.data.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }  
    this.yaml = YamlConfiguration.loadConfiguration(this.data);
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getHave(String name) {
    return this.yaml.getInt(name + ".have", 0);
  }
  
  public int getSell(String name) {
    return this.yaml.getInt(name + ".sell", 0);
  }
  
  public void setHave(String name, int i) {
    this.yaml.set(name + ".have", Integer.valueOf(i));
    try {
      this.yaml.save(this.data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void setSell(String name, int i) {
    this.yaml.set(name + ".sell", Integer.valueOf(i));
    try {
      this.yaml.save(this.data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public int addHave(String name, int i) {
    setHave(name, getHave(name) + i);
    return i;
  }
  
  public int addSell(String name, int i) {
    setSell(name, getSell(name) + i);
    return i;
  }

  /*
  public void reset() {
    int i = 0;
    this.yaml.set("CARROT.sell", Integer.valueOf(i));
    this.yaml.set("CARROT.have", Integer.valueOf(i));
    this.yaml.set("IRON_BLOCK.sell", Integer.valueOf(i));
    this.yaml.set("IRON_BLOCK.have", Integer.valueOf(i));
    this.yaml.set("POTATO.sell", Integer.valueOf(i));
    this.yaml.set("POTATO.have", Integer.valueOf(i));
    this.yaml.set("WHEAT.sell", Integer.valueOf(i));
    this.yaml.set("WHEAT.have", Integer.valueOf(i));
    this.yaml.set("NETHER_WART.sell", Integer.valueOf(i));
    this.yaml.set("NETHER_WART.have", Integer.valueOf(i));
    this.yaml.set("SUGAR_CANE.sell", Integer.valueOf(i));
    this.yaml.set("SUGAR_CANE.have", Integer.valueOf(i));
    this.yaml.set("BEETROOT.sell", Integer.valueOf(i));
    this.yaml.set("BEETROOT.have", Integer.valueOf(i));
    this.yaml.set("WHEAT_SEEDS.have", Integer.valueOf(i));
    this.yaml.set("BEETROOT_SEEDS.have", Integer.valueOf(i));
    try {
      this.yaml.save(this.data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
   */
}
