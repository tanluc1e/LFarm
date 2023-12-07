package me.tanluc.starfarm.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.tanluc.starfarm.data.User;
import me.tanluc.starfarm.StarsFarm;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ui {
  public static void MainUi(Player p) {
    Inventory ui = Bukkit.createInventory(null, 54, Objects.<String>requireNonNull(StarsFarm.getInstance().getConfig().getString("name")));
    ((StarsFarm)StarsFarm.getPlugin(StarsFarm.class)).LoadConfig();
    p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 50.0F, 50.0F);
    setupZSItems(p, ui);
    setupMenuItems(p, ui);
    setupHelpAndShouItems(p, ui);
    p.openInventory(ui);
  }
  private static void setupZSItems(Player p, Inventory ui) {
    List<String> lore = StarsFarm.getUi().getStringList("ZS1.lore");
    int ZS1data = StarsFarm.getUi().getInt("ZS1.model_data");
    ItemStack bl = new ItemStack(Material.getMaterial(StarsFarm.getUi().getString("ZS1.Material")), 1, (short) ZS1data);
    ItemMeta bm = bl.getItemMeta();
    assert bm != null;
    bm.setDisplayName(StarsFarm.getUi().getString("ZS1.name"));
    bm.setLore(lore);
    bl.setItemMeta(bm);
    int i = 0;
    while (i < 54) {
      ui.setItem(i, bl);
      i++;
    }
    List<String> l1 = StarsFarm.getUi().getStringList("ZS2.lore");
    int ZS2data = StarsFarm.getUi().getInt("ZS2.model_data");
    bl = new ItemStack(Material.getMaterial(StarsFarm.getUi().getString("ZS2.Material")), 1, (short) ZS2data);
    bm = bl.getItemMeta();
    assert bm != null;
    bm.setDisplayName(StarsFarm.getUi().getString("ZS2.name"));
    bm.setLore(l1);
    bl.setItemMeta(bm);
    i = 10;
    int a = 1;
    while (i <= 43) {
      ui.setItem(i, bl);
      i++;
      i++;
      if (a == 4) {
        a = 0;
        i++;
      }
      a++;
    }
  }
  private static void setupMenuItems(Player p, Inventory ui) {
    User user = User.of((OfflinePlayer)p);
    for (String s : ((ConfigurationSection)Objects.<ConfigurationSection>requireNonNull(StarsFarm.getUi().getConfigurationSection("Menu"))).getKeys(false)) {
      int Menudata = StarsFarm.getUi().getInt("Menu." + s + ".model_data");
      ItemStack itemStack = new ItemStack(Material.getMaterial(s.toUpperCase()), 1, (short) Menudata);
      ItemMeta itemMeta = itemStack.getItemMeta();
      assert itemMeta != null;
      itemMeta.setDisplayName(StarsFarm.getUi().getString("Menu." + s + ".name"));
      ArrayList<String> arrayList = new ArrayList<>();
      String type = itemStack.getType().name();
      for (String l : StarsFarm.getUi().getStringList("Menu." + s + ".lore"))
        arrayList.add(l.replace("{0}", String.valueOf(user.getHave(type))));
      itemMeta.setLore(arrayList);
      itemStack.setItemMeta(itemMeta);
      int slot = StarsFarm.getUi().getInt("Menu." + s + ".slot");
      ui.setItem(slot, itemStack);
      if (slot < 48) {
        itemStack = new ItemStack(Objects.<Material>requireNonNull(Material.getMaterial(Objects.<String>requireNonNull(StarsFarm.getUi().getString("Sell.Material")))));
        itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(((String)Objects.<String>requireNonNull(StarsFarm.getUi().getString("Sell.name"))).replace("{0}", Objects.<CharSequence>requireNonNull(StarsFarm.getUi().getString("Menu." + s + ".name"))));
        arrayList = new ArrayList<>();
        int maxSell = StarsFarm.getUi().getInt("blocks." + s + ".maxSell");
        for (String l : StarsFarm.getUi().getStringList("Sell.lore")) {
          //l = l.replace("{1}", "" + user.getSell(type) + "/" + user.getSell(type));
          l = l.replace("{1}", "" + user.getSell(type) + "/" + maxSell);
          if (user.getHave(type) >= StarsFarm.getUi().getInt("blocks." + s + ".maxSell") - user.getSell(type)) {
            // SỐ LƯỢNG CÓ SẴN ĐỂ BÁN = Tổng bán mỗi ngày - Tổng đã bán
            l = l.replace("{2}", String.valueOf(StarsFarm.getUi().getInt("blocks." + s + ".maxSell") - user.getSell(type)));
            // TỔNG GIÁ BÁN = Số lượng có sẵn - Giá config
            l = l.replace("{3}", String.valueOf((StarsFarm.getUi().getInt("blocks." + s + ".maxSell") - user.getSell(type)) * StarsFarm.getUi().getDouble("blocks." + s + ".price")));
          } else {
            l = l.replace("{2}", String.valueOf(user.getHave(type)));
            l = l.replace("{3}", String.valueOf(user.getHave(type) * StarsFarm.getUi().getDouble("blocks." + s + ".price")));
          }
          arrayList.add(l);
        }
        itemMeta.setLore(arrayList);
        itemStack.setItemMeta(itemMeta);
        ui.setItem(slot + 9, itemStack);
      }
    }
  }
  private static void setupHelpAndShouItems(Player p, Inventory ui) {
    int Helpdata = StarsFarm.getUi().getInt("help.model_data");
    ItemStack is = new ItemStack(Material.getMaterial(StarsFarm.getUi().getString("help.Material")), 1, (short) Helpdata);
    ItemMeta im = is.getItemMeta();
    assert im != null;
    im.setDisplayName(StarsFarm.getUi().getString("help.name"));
    im.setLore(StarsFarm.getUi().getStringList("help.lore"));
    is.setItemMeta(im);
    ui.setItem(45, is);
    int Shoudata = StarsFarm.getUi().getInt("shou.model_data");
    is = new ItemStack(Material.getMaterial(StarsFarm.getUi().getString("shou.Material")), 1, (short) Shoudata);
    im = is.getItemMeta();
    assert im != null;
    im.setDisplayName(StarsFarm.getUi().getString("shou.name"));
    ArrayList<String> al = new ArrayList<>();
    for (String s : StarsFarm.getUi().getStringList("shou.lore")) {
      if (StarsFarm.players.contains(p)) {
        al.add(s.replace("{0}", Objects.<CharSequence>requireNonNull(StarsFarm.getUi().getString("shou.true"))));
        continue;
      }
      al.add(s.replace("{0}", Objects.<CharSequence>requireNonNull(StarsFarm.getUi().getString("shou.false"))));
    }
    im.setLore(al);
    is.setItemMeta(im);
    ui.setItem(53, is);
  }

}
