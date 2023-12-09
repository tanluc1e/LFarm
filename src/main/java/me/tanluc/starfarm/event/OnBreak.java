package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import me.tanluc.starfarm.ui.Gui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class OnBreak implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakBlock(BlockBreakEvent e) {
        User user = User.of((OfflinePlayer) e.getPlayer());
        Player p = e.getPlayer();
        if (StarsFarm.players.contains(e.getPlayer())) {
            Block block = e.getBlock();
            for (ItemStack drop : block.getDrops()) {
                if (StarsFarm.materials.contains(drop.getType())) {
                    String type = drop.getType().name();
                    user.setHave(type, user.getHave(type) + drop.getAmount());
                    e.setDropItems(false);

                    if (StarsFarm.cc.getBoolean("title.enable")) {
                        String blockCategory = getBlockCategory(drop.getType());
                        if (blockCategory != null) {
                            Map<Material, String> blockMap = StarsFarm.guiBlockNames.get(blockCategory);
                            if (blockMap != null) {
                                String blockName = blockMap.get(drop.getType());
                                if (blockName != null) {
                                    sendTitleMessage(p, ChatColor.GREEN + "+" + drop.getAmount() + " " + blockName);
                                }
                            }
                        }
                    }

                } else {
                    e.setDropItems(true);
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
        for (Map.Entry<String, Map<Material, String>> entry : StarsFarm.guiBlockNames.entrySet()) {
            if (entry.getValue().containsKey(material)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
