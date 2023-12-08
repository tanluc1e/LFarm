package me.tanluc.starfarm.ui;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class AssistantGui {

    public static void StorageGui(Player p, Material material) {
        User user = User.of((OfflinePlayer)p);
        if (StarsFarm.materials.contains(material)) {
            Inventory inv = Bukkit.createInventory((InventoryHolder)p, 54, StarsFarm.getInstance().getConfig().getString("warehouseTitle"));
            p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 50.0F, 50.0F);
            ItemStack is = new ItemStack(material);
            String type = is.getType().name();
            int have = user.getHave(type);
            if (have >= 3456) {
                is.setAmount(64);
                int i = 0;
                while (i < 54) {
                    inv.setItem(i, is);
                    i++;
                }
            } else {
                ItemStack bl = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                ItemMeta bm = bl.getItemMeta();
                assert bm != null;
                bm.setDisplayName(StarsFarm.cc.getString("As.name"));
                //bm.setCustomModelData(Integer.valueOf(StarsFarm.cc.getInt("As.model_data")));
                int Asdata = Integer.valueOf(StarsFarm.cc.getInt("As.model_data"));
                bl.setDurability((short) Asdata);
                bm.setLore(StarsFarm.cc.getStringList("As.lore"));
                bl.setItemMeta(bm);
                int i = 0;
                while (i < 54) {
                    if (have >= 64) {
                        is.setAmount(64);
                        inv.addItem(new ItemStack[] { is });
                        have -= 64;
                    } else if (have > 0) {
                        is.setAmount(have);
                        inv.addItem(new ItemStack[] { is });
                        have = 0;
                    } else {
                        inv.setItem(i, bl);
                    }
                    i++;
                }
            }
            p.openInventory(inv);
        }
    }
}
