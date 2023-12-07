package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import me.tanluc.starfarm.ui.AssistantGui;
import me.tanluc.starfarm.ui.Gui;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Objects;

public class UiClick implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        User user = User.of((OfflinePlayer) p);

        if (e.getSlot() == -999) {
            return;
        }

        Inventory clickedInventory = e.getClickedInventory();
        if (clickedInventory == null || clickedInventory.getTitle() == null) {
            return;
        }

        File guiFolder = new File(StarsFarm.getInstance().getDataFolder(), "gui");
        File[] guiFiles = guiFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".yml"));

        if (guiFiles != null) {
            for (File file : guiFiles) {
                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf('.'));

                FileConfiguration guiConfig = YamlConfiguration.loadConfiguration(file);
                String title = guiConfig.getString("title");
                if (e.getView().getTitle().equals(title)) {
                    e.setCancelled(true);
                    if (e.getSlot() == 53) {
                        if (StarsFarm.players.contains(p)) {
                            StarsFarm.players.remove(p);
                            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 50.0F, 50.0F);
                        } else {
                            StarsFarm.players.add(p);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50.0F, 50.0F);
                        }
                        Gui.OpenMenu(p, fileName);
                    }

                    if (e.getClickedInventory() == p.getOpenInventory().getTopInventory() &&
                            e.getCurrentItem() != null &&
                            e.getCurrentItem().hasItemMeta())
                        if (e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
                            for (String s : ((ConfigurationSection)Objects.<ConfigurationSection>requireNonNull(guiConfig.getConfigurationSection("Menu"))).getKeys(false)) {
                                if (e.getSlot() - 9 == guiConfig.getInt("Menu." + s + ".slot")) {
                                    ItemStack is = ((Inventory)Objects.<Inventory>requireNonNull(e.getClickedInventory())).getItem(e.getSlot() - 9);
                                    assert is != null;
                                    String type = is.getType().name();
                                    int have = user.getHave(type);
                                    int sell = user.getSell(type);
                                    int max = 0;
                                    double price = 0;
                                    String itemName = "";

                                    ConfigurationSection blockSupport = guiConfig.getConfigurationSection("blocks");
                                    ItemStack clickedItem = e.getView().getItem(e.getSlot() - 9);
                                    if (clickedItem != null) {
                                        String clickedItemType = clickedItem.getType().toString();

                                        for (String blockkey : blockSupport.getKeys(false)) {
                                            if (blockkey.equalsIgnoreCase(clickedItemType)) {
                                                max = guiConfig.getInt("blocks." + blockkey + ".maxSell");
                                                price = guiConfig.getDouble("blocks." + blockkey + ".price");
                                                itemName = guiConfig.getString("blocks." + blockkey + ".name");
                                                break;
                                            }
                                        }
                                    }
                                    if (have > 0) {
                                        if (sell > max) {
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50.0F, 50.0F);
                                            p.closeInventory();
                                            p.sendMessage("§r[" + StarsFarm.prefix + "§r]§e Đã đạt giới hạn ngày hôm nay");
                                            System.out.println("Sell total: " + sell + " " + "Max total: " + max);
                                            continue;
                                        } else if (max > sell) {
                                            int km = max - sell;
                                            if (have < km)
                                                km = have;
                                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50.0F, 50.0F);
                                            p.closeInventory();
                                            double totalPrice = km * price;
                                            p.sendMessage(StarsFarm.messageManager.sellSuccess().replace("{name}", ChatColor.translateAlternateColorCodes('&', itemName)).replace("{totalPrice}", String.valueOf(totalPrice)).replace("{amount}", String.valueOf(km)).replace("{price}", String.valueOf(price)));
                                            StarsFarm.getEconomy().depositPlayer((OfflinePlayer)p, km * price);
                                            user.setHave(type, have - km);
                                            user.setSell(type, sell + km);
                                            continue;
                                        }
                                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 50.0F, 50.0F);
                                        p.closeInventory();
                                        p.sendMessage(StarsFarm.messageManager.sellMaxToday());
                                        continue;
                                    }
                                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 50.0F, 50.0F);
                                    p.closeInventory();
                                    p.sendMessage(StarsFarm.messageManager.notStoraged());
                                }
                            }
                        } else if (StarsFarm.materials.contains(e.getCurrentItem().getType())) {
                            String type = e.getCurrentItem().getType().name();
                            if (user.getHave(type) > 0) {
                                if (p.getInventory().firstEmpty() == -1) {
                                    p.closeInventory();
                                    p.sendMessage(StarsFarm.messageManager.inventoryFull());
                                } else {
                                    AssistantGui.StorageGui(p, e.getCurrentItem().getType());
                                }
                            } else {
                                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 50.0F, 50.0F);
                                p.sendMessage(StarsFarm.messageManager.notStoraged());
                            }
                        }
                }
            }
        }
    }
}
