package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OnBreak implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onShou(BlockBreakEvent e) {
        User user = User.of((OfflinePlayer) e.getPlayer());
        Player p = e.getPlayer();
        if (StarsFarm.players.contains(e.getPlayer())) {
            Block block = e.getBlock();
            for (ItemStack drop : block.getDrops()) {
                if (StarsFarm.materials.contains(drop.getType())) {
                    String type = drop.getType().name();
                    user.setHave(type, user.getHave(type) + drop.getAmount());
                    e.setDropItems(false);
                } else {
                    e.setDropItems(true);
                }
            }
        }
    }
}
