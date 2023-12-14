package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OnKilled implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            Player killer = e.getEntity().getKiller();
            if (StarsFarm.players.contains(killer)) {
                User user = User.of(killer);
                List<ItemStack> itemsToRemove = new ArrayList<>();

                for (ItemStack drop : e.getDrops()) {
                    if (StarsFarm.materials.contains(drop.getType())) {
                        String type = drop.getType().name();

                        if(isVanillaItem(drop)){
                            user.setHave(type, user.getHave(type) + drop.getAmount());
                            itemsToRemove.add(drop);

                            if (StarsFarm.cc.getBoolean("title.enable")) {
                                String blockCategory = getBlockCategory(drop.getType());
                                if (blockCategory != null) {
                                    Map<Material, String> blockMap = StarsFarm.materialsName.get(blockCategory);
                                    if (blockMap != null) {
                                        String blockName = blockMap.get(drop.getType());
                                        if (blockName != null) {
                                            sendTitleMessage(killer, ChatColor.GREEN + "+" + drop.getAmount() + " " + blockName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (ItemStack itemToRemove : itemsToRemove) {
                    e.getDrops().remove(itemToRemove);
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
        }.runTask(StarsFarm.getInstance());
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
