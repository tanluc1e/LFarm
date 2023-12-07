package me.tanluc.starfarm.event;

import me.tanluc.starfarm.StarsFarm;
import me.tanluc.starfarm.data.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OnDrop implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        User user = User.of(player);
        if (StarsFarm.players.contains(e.getPlayer())) {
            ItemStack droppedItem = e.getItemDrop().getItemStack();
            if (StarsFarm.materials.contains(droppedItem.getType())) {
                if (isVanillaItem(droppedItem)) {
                    String type = droppedItem.getType().name();
                    user.setHave(type, user.getHave(type) + droppedItem.getAmount());

                    e.getItemDrop().remove();
                }
            }
        }
    }

    private boolean isVanillaItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasDisplayName() || itemMeta.hasLore() || !itemStack.getEnchantments().isEmpty()) {
            return false;
        }
        return true;
    }
}
