package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OnPickUp implements Listener {
    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        Player p = (Player) e.getEntity();
        User user = User.of(p);
        if (StarsFarm.players.contains(p)) {
            Item item = e.getItem();
            ItemStack itemStack = item.getItemStack();

            if (StarsFarm.materials.contains(itemStack.getType())) {
                if (isVanillaItem(itemStack)) {
                    String type = itemStack.getType().name();
                    user.setHave(type, user.getHave(type) + itemStack.getAmount());

                    if (StarsFarm.cc.getBoolean("title.enable")) {
                        String blockCategory = getBlockCategory(itemStack.getType());
                        if (blockCategory != null) {
                            Map<Material, String> blockMap = StarsFarm.materialsName.get(blockCategory);
                            if (blockMap != null) {
                                String blockName = blockMap.get(itemStack.getType());
                                if (blockName != null) {
                                    sendTitleMessage(p, ChatColor.GREEN + "+" + itemStack.getAmount() + " " + blockName);
                                }
                            }
                        }
                    }

                    item.remove();
                    e.setCancelled(true);
                }

            }
        }
    }

    private void sendTitleMessage(Player player, String message) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle("", message, 10, 10, 10);
            }
        }.runTask(StarsFarm.getInstance()); // Replace 'YourPlugin' with your plugin's main class
    }
    private String getBlockCategory(Material material) {
        for (Map.Entry<String, Map<Material, String>> entry : StarsFarm.materialsName.entrySet()) {
            if (entry.getValue().containsKey(material)) {
                return entry.getKey();
            }
        }
        return null;
    }
    private boolean isVanillaItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName() || itemMeta.hasLore() || !itemStack.getEnchantments().isEmpty()) {
            return false;
        }
        return true;
    }
}
